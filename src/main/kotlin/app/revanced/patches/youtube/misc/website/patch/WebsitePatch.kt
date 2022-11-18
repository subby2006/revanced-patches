package app.revanced.patches.youtube.misc.website.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.*
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch
import app.revanced.shared.annotation.YouTubeUniversalCompatibility
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import org.w3c.dom.Element

@Patch (false)
@DependsOn(
    [
        IntegrationsPatch::class,
        SettingsResourcePatch::class,
        ResourceMappingResourcePatch::class
    ]
)
@Name("website")
@Description("Leave website URL in ReVanced settings.")
@YouTubeUniversalCompatibility
@Version("0.0.1")
class WebsitePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val revancedprefs = context["res/xml/revanced_prefs.xml"]
        revancedprefs.writeText(
                revancedprefs.readText()
                        .replace(
                                "<Preference android:title=\" \" android:selectable=\"false\" android:summary=\"@string/revanced_tool_used\" />",
                                "<Preference android:title=\" \" android:selectable=\"false\" android:summary=\"@string/pref_about_category\" /><Preference android:title=\"@string/website\" android:summary=\"websitesummary\" ><intent android:action=\"android.intent.action.VIEW\" android:data=\"websiteurl\" /></Preference><Preference android:title=\" \" android:selectable=\"false\" android:summary=\"@string/revanced_tool_used\" />"
                        ).replace(
                                "websiteurl",
                                "$websiteURL"
                        ).replace(
                                "websitesummary",
                                "$websiteSummary"
                        )
        )

        return PatchResultSuccess()
    }

    companion object : OptionsContainer() {
        private var websiteURL: String? by option(
            PatchOption.StringOption(
                key = "websiteURL",
                default = "https://t.me/revanced_extended",
                title = "Website Name",
                description = "ReVanced Extended website.",
                required = true
            )
        )

        private var websiteSummary: String? by option(
            PatchOption.StringOption(
                key = "websiteSummary",
                default = "Visit the ReVanced Extended channel.",
                title = "Website Summary",
                description = "ReVanced Extended website description.",
                required = true
            )
        )
    }
}
