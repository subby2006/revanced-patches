package app.revanced.patches.youtube.layout.hideendscreencards.resource.patch

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch

@Name("hide-endscreen-cards-resource-patch")
@YouTubeCompatibility
@DependsOn([ResourceMappingResourcePatch::class])
@Version("0.0.1")
class HideEndscreenCardsResourcePatch : ResourcePatch {
    internal companion object {
        var layoutCircle: Long = -1
        var layoutIcon: Long = -1
        var layoutVideo: Long = -1
    }

    override fun execute(context: ResourceContext): PatchResult {

        fun findEndscreenResourceId(name: String) = ResourceMappingResourcePatch.resourceMappings.single {
            it.type == "layout" && it.name == "endscreen_element_layout_$name"
        }.id

        layoutCircle = findEndscreenResourceId("circle")
        layoutIcon = findEndscreenResourceId("icon")
        layoutVideo = findEndscreenResourceId("video")

        return PatchResultSuccess()
    }
}