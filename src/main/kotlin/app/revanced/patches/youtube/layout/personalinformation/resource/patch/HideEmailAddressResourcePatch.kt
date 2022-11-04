package app.revanced.patches.youtube.layout.personalinformation.resource.patch

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch

@Name("hide-email-address-resource-patch")
@YouTubeCompatibility
@DependsOn([ResourceMappingResourcePatch::class])
@Version("0.0.1")
class HideEmailAddressResourcePatch : ResourcePatch {
    companion object {
        internal var accountSwitcherAccessibilityLabelId: Long = -1
    }

    override fun execute(context: ResourceContext): PatchResult {

        accountSwitcherAccessibilityLabelId = ResourceMappingResourcePatch.resourceMappings.single {
            it.type == "string" && it.name == "account_switcher_accessibility_label"
        }.id

        return PatchResultSuccess()
    }
}