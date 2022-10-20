package app.revanced.patches.youtube.ad.video.patch

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
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.ad.video.fingerprints.LoadVideoAdsFingerprint
import app.revanced.patches.youtube.ad.video.fingerprints.ShowVideoAdsFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("video-ads")
@Description("Removes ads in the video player.")
@YouTubeCompatibility
@Version("0.0.1")
class VideoAdsPatch : BytecodePatch(
    listOf(
        LoadVideoAdsFingerprint,
        ShowVideoAdsFingerprint,
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        val lithoAdsFingerprintMethod = LoadVideoAdsFingerprint.result!!.mutableMethod

        lithoAdsFingerprintMethod.addInstructions(
            0, """
                invoke-static { }, Lapp/revanced/integrations/patches/VideoAdsPatch;->shouldShowAds()Z
                move-result v0
                if-nez v0, :show_video_ads
                return-void
            """, listOf(ExternalLabel("show_video_ads", lithoAdsFingerprintMethod.instruction(0)))
        )

        ShowVideoAdsFingerprint.result!!.mutableMethod.addInstructions(
            0, """
                invoke-static { }, Lapp/revanced/integrations/patches/VideoAdsPatch;->shouldShowAds()Z
                move-result v1
            """
        )

        return PatchResultSuccess()
    }
}
