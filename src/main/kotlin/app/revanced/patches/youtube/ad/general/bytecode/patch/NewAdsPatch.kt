package app.revanced.patches.youtube.ad.general.bytecode.patch

import app.revanced.extensions.injectHideCall
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.annotation.NewYouTubeCompatibility
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction21c
import org.jf.dexlib2.iface.instruction.formats.*

@DependsOn([ResourceMappingResourcePatch::class, IntegrationsPatch::class])
@Name("new-general-ads")
@Description("Removes general ads for v17.44.xx+")
@NewYouTubeCompatibility
@Version("0.0.1")
class NewAdsPatch : BytecodePatch() {
    // list of resource names to get the id of
    private val resourceIds = arrayOf(
        "promoted_video_item_land_stark_ad_badge"
    ).map { name ->
        ResourceMappingResourcePatch.resourceMappings.single { it.name == name }.id
    }

    override fun execute(context: BytecodeContext): PatchResult {

        // iterating through all classes is expensive
        for (classDef in context.classes) {
            //var mutableClass: MutableClass? = null

            method@ for (method in classDef.methods) {
                //var mutableMethod: MutableMethod? = null

                if (method.implementation == null) continue@method

                val instructions = method.implementation!!.instructions
                instructions.forEachIndexed { index, instruction ->
                    when (instruction.opcode) {
                        Opcode.CONST -> {
                            // TODO: find a way to de-duplicate code.
                            //  The issue is we need to save mutableClass and mutableMethod to the existing fields
                            when ((instruction as Instruction31i).wideLiteral) {
                                resourceIds[0] -> {
                                    //  and is preceded by an instruction with the mnemonic NEW_INSTANCE
                                    val insertIndex = index - 1
                                    val newInstanceInstruction = instructions.elementAt(insertIndex)
                                    if (newInstanceInstruction.opcode != Opcode.NEW_INSTANCE) return@forEachIndexed

                                    // resolve the constructor method of class with view reference
                                    val targetMutableClass = context.findClass(
                                        (newInstanceInstruction as DexBackedInstruction21c).reference.toString()
                                    )!!.mutableClass
                                    val targetMutableMethod = targetMutableClass.methods.toMutableList()[0]

                                    // and is based on an instruction with the mnemonic MOVE_RESULT_OBJECT
                                    val targetInsertIndex = 5
                                    val targetInvokeInstruction = targetMutableMethod.implementation!!.instructions.elementAt(targetInsertIndex)
                                    if (targetInvokeInstruction.opcode != Opcode.MOVE_RESULT_OBJECT) return@forEachIndexed

                                    // insert hide call to hide the view corresponding to the resource
                                    val targetViewRegister = (targetInvokeInstruction as Instruction11x).registerA
                                    targetMutableMethod.implementation!!.injectHideCall(targetInsertIndex + 1, targetViewRegister)
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
