package app.revanced.patches.youtube.layout.hidecrowdfundingbox.resource.patch

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.shared.annotation.YouTubeCompatibility

@Name("crowdfunding-box-resource-patch")
@YouTubeCompatibility
@DependsOn([ResourceMappingResourcePatch::class])
@Version("0.0.1")
class CrowdfundingBoxResourcePatch : ResourcePatch {
    companion object {
        internal var crowdfundingBoxId: Long = -1
    }

    override fun execute(context: ResourceContext): PatchResult {

        crowdfundingBoxId = ResourceMappingResourcePatch.resourceMappings.single {
            it.type == "layout" && it.name == "donation_companion"
        }.id

        return PatchResultSuccess()
    }
}