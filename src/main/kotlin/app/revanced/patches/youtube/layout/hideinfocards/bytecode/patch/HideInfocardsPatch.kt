package app.revanced.patches.youtube.layout.hideinfocards.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.layout.hideinfocards.bytecode.fingerprints.InfocardsIncognitoFingerprint
import app.revanced.patches.youtube.layout.hideinfocards.bytecode.fingerprints.InfocardsIncognitoParentFingerprint
import app.revanced.patches.youtube.layout.hideinfocards.bytecode.fingerprints.InfocardsMethodCallFingerprint
import app.revanced.patches.youtube.layout.hideinfocards.resource.patch.HideInfocardsResourcePatch
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c
import org.jf.dexlib2.Opcode

@Patch
@DependsOn([IntegrationsPatch::class, HideInfocardsResourcePatch::class])
@Name("hide-info-cards")
@Description("Hides info-cards in videos.")
@YouTubeCompatibility
@Version("0.0.1")
class HideInfocardsPatch : BytecodePatch(
    listOf(
        InfocardsIncognitoParentFingerprint,
        InfocardsMethodCallFingerprint,
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        with(InfocardsIncognitoFingerprint.also {
            it.resolve(context, InfocardsIncognitoParentFingerprint.result!!.classDef)
        }.result!!.mutableMethod) {
                val invokeInstructionIndex = implementation!!.instructions.indexOfFirst {
                    it.opcode.ordinal == Opcode.INVOKE_VIRTUAL.ordinal &&
                    ((it as? BuilderInstruction35c)?.reference.toString() ==
                        "Landroid/view/View;->setVisibility(I)V")
                }

                val register = (instruction(invokeInstructionIndex) as? BuilderInstruction35c)?.registerC

                addInstructions(
                invokeInstructionIndex, """
                    invoke-static {}, Lapp/revanced/integrations/patches/HideInfocardsPatch;->hideInfoCard()I
                    move-result v1
                    invoke-virtual {v$register, v1}, Landroid/view/View;->setVisibility(I)V
                """
                )
        }

        return PatchResultSuccess()
    }
}