package app.revanced.patches.music.misc.websitemusic.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.*
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.annotation.YouTubeMusicCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import org.w3c.dom.Element

@Patch (false)
@DependsOn(
    [
        ResourceMappingResourcePatch::class
    ]
)
@Name("website-music")
@Description("Leave website URL in settings.")
@YouTubeMusicCompatibility
@Version("0.0.1")
class WebsiteMusicPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        /*
         * Copy preference fragments
         */

        context.copyXmlNode("website-music/host", "xml/settings_headers.xml", "PreferenceScreen")
        context.copyXmlNode("website-music/host", "xml/settings_prefs_compat.xml", "com.google.android.apps.youtube.music.ui.preference.PreferenceCategoryCompat")

        val settingsheaders = context["res/xml/settings_headers.xml"]
        settingsheaders.writeText(
                settingsheaders.readText()
                        .replace(
                                "websiteurl",
                                "$websiteURL"
                        ).replace(
                                "websitesummary",
                                "$websiteSummary"
                        )
        )

        val settingsprefscompat = context["res/xml/settings_prefs_compat.xml"]
        settingsprefscompat.writeText(
                settingsprefscompat.readText()
                        .replace(
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
