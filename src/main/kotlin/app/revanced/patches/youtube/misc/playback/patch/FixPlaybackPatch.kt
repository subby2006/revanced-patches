package app.revanced.patches.youtube.misc.playback.patch

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.playercontroller.patch.PlayerControllerPatch
import app.revanced.patches.youtube.misc.videoid.patch.NewVideoIdPatch

@DependsOn([
    IntegrationsPatch::class,
    PlayerControllerPatch::class, // updates video length and adds method to seek in video, necessary for this patch
    NewVideoIdPatch::class
])
@Name("fix-playback")
@Description("Fixes the issue with videos not playing when video ads are removed.")
@YouTubeCompatibility
@Version("0.0.1")
class FixPlaybackPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        // If a new video loads, fix the playback issue
        NewVideoIdPatch.injectCall("Lapp/revanced/integrations/patches/FixPlaybackPatch;->newVideoLoaded(Ljava/lang/String;)V")

        return PatchResultSuccess()
    }
}
