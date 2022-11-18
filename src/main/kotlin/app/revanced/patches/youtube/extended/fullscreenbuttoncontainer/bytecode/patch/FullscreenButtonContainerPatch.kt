package app.revanced.patches.youtube.extended.fullscreenbuttoncontainer.bytecode.patch

import app.revanced.extensions.MethodExtensions.findMutableMethodOf
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.iface.instruction.formats.*
import org.jf.dexlib2.Opcode

@Patch
@DependsOn(
    [
        IntegrationsPatch::class,
        ResourceMappingResourcePatch::class
    ]
)
@Name("hide-fullscreen-buttoncontainer")
@Description("Hides fullscreen buttoncontainer.")
@YouTubeCompatibility
@Version("0.0.1")
class FullscreenButtonContainerPatch : BytecodePatch() {

    // list of resource names to get the id of
    private val resourceIds = arrayOf(
        "quick_actions_element_container"
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
                            when ((instruction as Instruction31i).wideLiteral) {
                                resourceIds[0] -> { // fullscreen panel
                                    val insertIndex = index + 3
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.CHECK_CAST) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val viewRegister = (invokeInstruction as Instruction21c).registerA
                                    mutableMethod!!.addInstruction(
                                        insertIndex,
                                        "invoke-static {v$viewRegister}, Lapp/revanced/integrations/patches/FullscreenButtonContainerRemoverPatch;->HideFullscreenButtonContainer(Landroid/view/View;)V"
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
