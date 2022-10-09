package app.revanced.patches.music.layout.blacknavbar.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.music.layout.blacknavbar.annotation.BlackNavbarCompatibility
import app.revanced.extensions.MethodExtensions.findMutableMethodOf
import app.revanced.extensions.MethodExtensions.toDescriptor
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.formats.Instruction11x
import org.jf.dexlib2.iface.instruction.formats.Instruction31i

@Patch
@DependsOn(
    [
        ResourceMappingResourcePatch::class
    ]
)
@Name("black-navbar")
@Description("Sets the navigation bar color to black.")
@BlackNavbarCompatibility
@Version("0.0.1")
class BlackNavbarPatch : BytecodePatch() {

    // list of resource names to get the id of
    private val resourceIds = arrayOf(
        "ytm_color_grey_12"
    ).map { name ->
        ResourceMappingResourcePatch.resourceMappings.single { it.name == name }.id
    }

    override fun execute(context: BytecodeContext): PatchResult {

        // iterating through all classes is expensive
        for (classDef in context.classes) {
            var mutableClass: MutableClass? = null

            method@ for (method in classDef.methods) {
                var mutableMethod: MutableMethod? = null

                if (method.implementation == null) continue@method

                val instructions = method.implementation!!.instructions
                instructions.forEachIndexed { index, instruction ->
                    when (instruction.opcode) {
                        Opcode.CONST -> {
                            // TODO: find a way to de-duplicate code.
                            //  The issue is we need to save mutableClass and mutableMethod to the existing fields
                            when ((instruction as Instruction31i).wideLiteral) {

                                resourceIds[0] -> { // blacknavbar
                                    //  and is followed by an instruction with the mnemonic IPUT_OBJECT
                                    val insertIndex = index - 1
                                    val insertIndex2 = index + 2
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    val invokeInstruction2 = instructions.elementAt(insertIndex2)
                                    if (invokeInstruction.opcode != Opcode.MOVE_RESULT_OBJECT) return@forEachIndexed
                                    val register = (invokeInstruction2 as Instruction11x).registerA

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    // TODO: dynamically get registers
                                    mutableMethod!!.addInstruction(
                                        index + 3, "const/high16 v${register}, -0x1000000"
                                    )
                                }
                            }
                        }
                        else -> return@forEachIndexed
                    }
                }
            }
        }
        return PatchResultSuccess()
    }
}
