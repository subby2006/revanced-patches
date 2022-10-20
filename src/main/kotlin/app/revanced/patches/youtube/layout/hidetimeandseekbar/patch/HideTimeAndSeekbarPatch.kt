package app.revanced.patches.youtube.layout.hidetimeandseekbar.patch

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
import app.revanced.patches.youtube.layout.hidetimeandseekbar.fingerprints.TimeCounterFingerprint
import app.revanced.patches.youtube.layout.sponsorblock.bytecode.fingerprints.CreateVideoPlayerSeekbarFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-time-and-seekbar")
@Description("Hides progress bar and time counter on videos.")
@YouTubeCompatibility
@Version("0.0.1")
class HideTimeAndSeekbarPatch : BytecodePatch(
    listOf(
        CreateVideoPlayerSeekbarFingerprint, TimeCounterFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        val createVideoPlayerSeekbarMethod = CreateVideoPlayerSeekbarFingerprint.result!!.mutableMethod

        createVideoPlayerSeekbarMethod.addInstructions(
            0, """
            const/4 v0, 0x0
            invoke-static { }, Lapp/revanced/integrations/patches/HideTimeAndSeekbarPatch;->hideTimeAndSeekbar()Z
            move-result v0
            if-eqz v0, :hide_time_and_seekbar
            return-void
            :hide_time_and_seekbar
            nop
        """
        )

        val timeCounterMethod = TimeCounterFingerprint.result!!.mutableMethod

        timeCounterMethod.addInstructions(
            0, """
            invoke-static { }, Lapp/revanced/integrations/patches/HideTimeAndSeekbarPatch;->hideTimeAndSeekbar()Z
            move-result v0
            if-eqz v0, :hide_time_and_seekbar
            return-void
            :hide_time_and_seekbar
            nop
        """
        )

        return PatchResultSuccess()
    }
}
