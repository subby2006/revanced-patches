package app.revanced.patches.youtube.ad.general.resource.patch

import app.revanced.annotation.NewYouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch

@NewYouTubeCompatibility
@DependsOn([ResourceMappingResourcePatch::class])
@Name("new-ads-resource-patch")
@Version("0.0.1")
class NewAdsResourcePatch : ResourcePatch {
    internal companion object {
        var newadsResourceId: Long = -1
    }

    override fun execute(context: ResourceContext): PatchResult {
        newadsResourceId = ResourceMappingResourcePatch.resourceMappings.single {
            it.type == "layout" && it.name == "promoted_video_item_land_stark_ad_badge_align"
        }.id

        return PatchResultSuccess()
    }
}