package app.revanced.patches.youtube.layout.hidemixplaylists.resource.patch

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.shared.annotation.YouTubeCompatibility

@Name("hide-my-mix-resource-patch")
@YouTubeCompatibility
@DependsOn([ResourceMappingResourcePatch::class])
@Version("0.0.1")
class MixPlaylistsResourcePatch : ResourcePatch {
    companion object {
        internal var abclistmenuitemLabelId: Long = -1
        internal var bottompaneloverlaytextLabelId: Long = -1
    }

    override fun execute(context: ResourceContext): PatchResult {

        abclistmenuitemLabelId = ResourceMappingResourcePatch.resourceMappings.single {
            it.type == "layout" && it.name == "abc_list_menu_item_layout"
        }.id

        bottompaneloverlaytextLabelId = ResourceMappingResourcePatch.resourceMappings.single {
            it.type == "id" && it.name == "bottom_panel_overlay_text"
        }.id

        return PatchResultSuccess()
    }
}