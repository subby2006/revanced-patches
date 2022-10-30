package app.revanced.patches.youtube.interaction.swipecontrols.patch.resource

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.annotation.YouTubeCompatibility
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources

@Name("swipe-controls-resource-patch")
@YouTubeCompatibility
@Version("0.0.1")
class SwipeControlsResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        context.copyResources(
            "swipecontrols",
            ResourceUtils.ResourceGroup(
                "drawable",
                "ic_sc_brightness_auto.xml",
                "ic_sc_brightness_manual.xml",
                "ic_sc_volume_mute.xml",
                "ic_sc_volume_normal.xml"
            )
        )
        return PatchResultSuccess()
    }
}
