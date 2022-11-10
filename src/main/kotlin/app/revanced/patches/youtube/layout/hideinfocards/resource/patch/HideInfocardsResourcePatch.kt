package app.revanced.patches.youtube.layout.hideinfocards.resource.patch

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch

@YouTubeCompatibility
@DependsOn([ResourceMappingResourcePatch::class])
@Name("hide-info-cards-resource-patch")
@Version("0.0.1")
class HideInfocardsResourcePatch : ResourcePatch {
    internal companion object {
        var drawerResourceId: Long = -1
    }

    override fun execute(context: ResourceContext): PatchResult {
        drawerResourceId = ResourceMappingResourcePatch.resourceMappings.single {
            it.type == "id" && it.name == "info_cards_drawer_header"
        }.id

        return PatchResultSuccess()
    }
}