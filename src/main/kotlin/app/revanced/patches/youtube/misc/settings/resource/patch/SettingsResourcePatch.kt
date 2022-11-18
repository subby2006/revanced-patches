package app.revanced.patches.youtube.misc.settings.resource.patch

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.shared.annotation.YouTubeUniversalCompatibility
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.w3c.dom.Element


@Name("settings-resource-patch")
@YouTubeUniversalCompatibility
@DependsOn([FixLocaleConfigErrorPatch::class])
@Version("0.0.1")
class SettingsResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        /*
         * Copy strings
         */

        context.copyXmlNode("youtube/settings/host", "values/strings.xml", "resources")

        /*
         * Copy drawables
         */

        context.copyXmlNode("youtube/settings/host", "values/drawables.xml", "resources")

        /*
         * Copy preference fragments
         */

        // context.copyXmlNode("youtube/settings/host", "xml/settings_fragment.xml", "PreferenceScreen")

        val settingsFragment = context["res/xml/settings_fragment.xml"]
        settingsFragment.writeText(
                settingsFragment.readText()
                        .replace(
                                "<Preference android:title=\"@string/pref_developer_category\" android:key=\"@string/developer_settings_key\" android:fragment=\"com.google.android.apps.youtube.app.settings.developer.DeveloperPrefsFragment\" app:iconSpaceReserved=\"false\" />",
                                "<Preference android:title=\"@string/pref_developer_category\" android:key=\"@string/developer_settings_key\" android:fragment=\"com.google.android.apps.youtube.app.settings.developer.DeveloperPrefsFragment\" app:iconSpaceReserved=\"false\" /><Preference android:title=\"@string/revanced_settings\" android:summary=\"@string/rvx_dialog_title\"><intent android:targetPackage=\"com.google.android.youtube\" android:data=\"revanced_settings\" android:targetClass=\"com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity\" /></Preference>"
                        )
        )

        /*
         * Copy layout resources
         */
        arrayOf(
            ResourceUtils.ResourceGroup(
                "layout",
                "revanced_settings_toolbar.xml",
                "revanced_settings_with_toolbar.xml",
                "revanced_settings_with_toolbar_layout.xml"
            ),
            ResourceUtils.ResourceGroup(
                "xml",
                "revanced_prefs.xml"
            ),
            ResourceUtils.ResourceGroup(
                "values-night",
                "drawables.xml"
            ),
            ResourceUtils.ResourceGroup(
                "drawable",
                "ic_rvx_logo.xml"
            )
        ).forEach { resourceGroup ->
            context.copyResources("youtube/settings", resourceGroup)
        }

        context.xmlEditor["AndroidManifest.xml"].use {
            val manifestNode = it
                .file
                .getElementsByTagName("manifest")
                .item(0) as Element

            val element = it.file.createElement("uses-permission")
            element.setAttribute("android:name", "android.permission.SCHEDULE_EXACT_ALARM")
            manifestNode.appendChild(element)
        }

        // remove Divider
        arrayOf("values-v21" to arrayOf("styles")).forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val resDirectory = context["res"].resolve("values-v21")
                val relativePath = "$path/$name.xml"

                Files.createDirectory(resDirectory.toPath())
                Files.copy(
                    this.javaClass.classLoader.getResourceAsStream("youtube/settings/$relativePath")!!,
                    context["res"].resolve(relativePath).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }
}