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
import app.revanced.patches.youtube.ad.video.fingerprints.LoadAdsFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("video-ads")
@Description("Removes ads in the video player.")
@YouTubeCompatibility
@Version("0.0.1")
class VideoAdsPatch : BytecodePatch(
    listOf(
        LoadAdsFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        with(LoadAdsFingerprint.result!!) {
            val insertIndex = scanResult.patternScanResult!!.startIndex
            with(mutableMethod) {
                addInstructions(
                    insertIndex,
                    """ 
                            invoke-static { }, Lapp/revanced/integrations/patches/VideoAdsPatch;->shouldShowAds()Z
                            move-result v4
                            if-nez v4, :show_video_ads
                            return-object v9
                         """,
                    listOf(ExternalLabel("show_video_ads", instruction(insertIndex)))
                )
            }
        }

        return PatchResultSuccess()
    }
}
