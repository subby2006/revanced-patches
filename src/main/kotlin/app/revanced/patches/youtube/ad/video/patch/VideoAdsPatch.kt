package app.revanced.patches.youtube.ad.video.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.youtube.ad.video.annotations.VideoAdsCompatibility
import app.revanced.patches.youtube.ad.video.fingerprints.LoadAdsFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("video-ads")
@Description("Removes ads in the video player.")
@VideoAdsCompatibility
@Version("0.0.1")
class VideoAdsPatch : BytecodePatch(
    listOf(
        LoadAdsFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        LoadAdsFingerprint.result!!.mutableMethod.let { method ->
            method.addInstructions(
                0,
                """ 
                    invoke-static { }, Lapp/revanced/integrations/patches/VideoAdsPatch;->shouldShowAds()Z
                    move-result v1
                    if-nez v1, :show_video_ads
                    const/4 v1, 0x0
                    return-object v1
                """,
                listOf(ExternalLabel("show_video_ads", method.instruction(0)))
            )
        }

        return PatchResultSuccess()
    }
}