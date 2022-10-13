package app.revanced.patches.youtube.layout.captionsbutton.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.SubtitleButtonControllerFingerprint
import app.revanced.patches.youtube.layout.captionsbutton.annotations.HideCaptionsButtonCompatibility
import app.revanced.patches.youtube.layout.captionsbutton.fingerprints.CaptionsButtonOnClickFingerprint
import app.revanced.patches.youtube.layout.captionsbutton.fingerprints.CaptionsButtonOnLongClickFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-captions-button")
@Description("Hides the captions button in the video player.")
@HideCaptionsButtonCompatibility
@Version("0.0.1")
class HideCaptionsButtonPatch : BytecodePatch(listOf(
    SubtitleButtonControllerFingerprint,
    CaptionsButtonOnClickFingerprint,
    CaptionsButtonOnLongClickFingerprint,
)) {
    override fun execute(context: BytecodeContext): PatchResult {

        val subtitleButtonControllerMethod = SubtitleButtonControllerFingerprint.result!!.mutableMethod
        subtitleButtonControllerMethod.addInstructions(
            0, """
                invoke-static {}, Lapp/revanced/integrations/patches/HideCaptionsButtonPatch;->hideCaptionsButton()Z
                move-result v0
                if-nez v0, :hide_captions_button
                return-void
            """, listOf(ExternalLabel("hide_captions_button", subtitleButtonControllerMethod.instruction(0)))
        )

        val captionsButtonOnClickMethod = CaptionsButtonOnClickFingerprint.result!!.mutableMethod
        captionsButtonOnClickMethod.addInstructions(
            0, """
                invoke-static {}, Lapp/revanced/integrations/patches/HideCaptionsButtonPatch;->hideCaptionsButton()Z
                move-result v0
                if-nez v0, :hide_captions_button
                return-void
            """, listOf(ExternalLabel("hide_captions_button", captionsButtonOnClickMethod.instruction(0)))
        )

        val captionsButtonOnLongClickMethod = CaptionsButtonOnLongClickFingerprint.result!!.mutableMethod
        captionsButtonOnLongClickMethod.addInstructions(
            0, """
                invoke-static {}, Lapp/revanced/integrations/patches/HideCaptionsButtonPatch;->hideCaptionsButton()Z
                move-result v0
                if-nez v0, :hide_captions_button
                const/4 v0, 0x0
                return v0
            """, listOf(ExternalLabel("hide_captions_button", captionsButtonOnLongClickMethod.instruction(0)))
        )

        return PatchResultSuccess()
    }
}
