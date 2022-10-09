package app.revanced.patches.youtube.layout.autocaptions.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patches.youtube.layout.autocaptions.annotations.AutoCaptionsCompatibility
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.StartVideoInformerFingerprint
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.SubtitleButtonControllerFingerprint
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.SubtitleTrackFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("disable-auto-captions")
@Description("Disable forced captions from being automatically enabled.")
@AutoCaptionsCompatibility
@Version("0.0.1")
class AutoCaptionsPatch : BytecodePatch(
    listOf(
        StartVideoInformerFingerprint, SubtitleButtonControllerFingerprint, SubtitleTrackFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val startVideoInformerMethod = StartVideoInformerFingerprint.result!!.mutableMethod

        startVideoInformerMethod.addInstructions(
            0, """
            const/4 v0, 0x0
            sput-boolean v0, Lapp/revanced/integrations/patches/DisableAutoCaptionsPatch;->captionsButtonDisabled:Z
        """
        )

        val subtitleButtonControllerMethod = SubtitleButtonControllerFingerprint.result!!.mutableMethod

        subtitleButtonControllerMethod.addInstructions(
            0, """
            const/4 v0, 0x1
            sput-boolean v0, Lapp/revanced/integrations/patches/DisableAutoCaptionsPatch;->captionsButtonDisabled:Z
        """
        )

        val subtitleTrackMethod = SubtitleTrackFingerprint.result!!.mutableMethod

        subtitleTrackMethod.addInstructions(
            0, """
            invoke-static {}, Lapp/revanced/integrations/patches/DisableAutoCaptionsPatch;->autoCaptionsEnabled()Z
            move-result v0
            if-eqz v0, :forced_captions
            sget-boolean v0, Lapp/revanced/integrations/patches/DisableAutoCaptionsPatch;->captionsButtonDisabled:Z
            if-nez v0, :forced_captions
            const/4 v0, 0x1
            return v0
            :forced_captions
            nop
        """
        )

        return PatchResultSuccess()
    }
}