package app.revanced.patches.youtube.layout.captionsbutton.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.SubtitleButtonControllerFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.Opcode

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-captions-button")
@Description("Hides the captions button in the video player.")
@YouTubeCompatibility
@Version("0.0.1")
class HideCaptionsButtonPatch : BytecodePatch(listOf(
    SubtitleButtonControllerFingerprint,
)) {
    override fun execute(context: BytecodeContext): PatchResult {

        val subtitleButtonControllerMethod = SubtitleButtonControllerFingerprint.result!!.mutableMethod

        //Due to previosly applied patches, scanResult index cannot be used in this context
        val igetBooleanIndex = subtitleButtonControllerMethod.implementation!!.instructions.indexOfFirst {
            it.opcode == Opcode.IGET_BOOLEAN
        }

        subtitleButtonControllerMethod.addInstructions(
            igetBooleanIndex + 1, """
                invoke-static {v0}, Lapp/revanced/integrations/patches/HideCaptionsButtonPatch;->hideCaptionsButton(Landroid/widget/ImageView;)V
            """
        )

        return PatchResultSuccess()
    }
}