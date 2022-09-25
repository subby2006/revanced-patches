package app.revanced.patches.youtube.misc.settings.resource.patch

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.patches.youtube.misc.settings.annotations.SettingsCompatibility
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import org.w3c.dom.Element
import java.nio.file.Files
import java.nio.file.StandardCopyOption


@Name("settings-resource-patch")
@SettingsCompatibility
@DependsOn([FixLocaleConfigErrorPatch::class])
@Version("0.0.1")
class SettingsResourcePatch : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {
        val classLoader = this.javaClass.classLoader
        /*
         * Copy strings
         */

        data.copyXmlNode("settings/host", "values/strings.xml", "resources")

        /*
         * Copy drawables
         */

        data.copyXmlNode("settings/host", "values/drawables.xml", "resources")

        /*
         * Copy preference fragments
         */

        // data.copyXmlNode("settings/host", "xml/settings_fragment.xml", "PreferenceScreen")

        val settingsFragment = data["res/xml/settings_fragment.xml"]
        settingsFragment.writeText(
                settingsFragment.readText()
                        .replace(
                                "<Preference android:title=\"@string/pref_developer_category\" android:key=\"@string/developer_settings_key\" android:fragment=\"com.google.android.apps.youtube.app.settings.developer.DeveloperPrefsFragment\" app:iconSpaceReserved=\"false\" />",
                                "<Preference android:title=\"@string/pref_developer_category\" android:key=\"@string/developer_settings_key\" android:fragment=\"com.google.android.apps.youtube.app.settings.developer.DeveloperPrefsFragment\" app:iconSpaceReserved=\"false\" />\n    TEMP1"
                        ).replace(
                                "TEMP1",
                                "<Preference android:title=\"@string/revanced_settings\" android:summary=\"@string/rvx_dialog_title\">\n        TEMP2"
                        ).replace(
                                "TEMP2",
                                "<intent android:targetPackage=\"com.google.android.youtube\" android:data=\"revanced_settings\" android:targetClass=\"com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity\" />\n    </Preference>\n    TEMP3"
                        ).replace(
                                "TEMP3",
                                "<Preference android:title=\"@string/revanced_ryd_settings_title\" android:summary=\"@string/revanced_ryd_settings_summary\">\n        <intent android:targetPackage=\"com.google.android.youtube\" android:data=\"ryd_settings\" android:targetClass=\"com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity\" />\n    </Preference>\n    TEMP4"
                        ).replace(
                                "TEMP4",
                                "<Preference android:title=\"@string/sb_settings\" android:summary=\"@string/sb_summary\">\n        <intent android:targetPackage=\"com.google.android.youtube\" android:data=\"sponsorblock_settings\" android:targetClass=\"com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity\" />\n    </Preference>\n    "
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
            data.copyResources("settings", resourceGroup)
        }

        data.xmlEditor["AndroidManifest.xml"].use {
            val manifestNode = it
                .file
                .getElementsByTagName("manifest")
                .item(0) as Element

            val element = it.file.createElement("uses-permission")
            element.setAttribute("android:name", "android.permission.SCHEDULE_EXACT_ALARM")
            manifestNode.appendChild(element)
        }

        val drawablexxxhdpi = "drawable-xxxhdpi" to arrayOf(
                "ic_fullscreen_vertical_button",
                "quantum_ic_fullscreen_exit_grey600_24",
                "quantum_ic_fullscreen_exit_white_24",
                "quantum_ic_fullscreen_grey600_24",
                "quantum_ic_fullscreen_white_24",
                "revanced_yt_copy_icon",
                "revanced_yt_copy_icon_with_time",
                "revanced_yt_download_icon",
                "yt_outline_arrow_repeat_1_white_24",
                "yt_outline_arrow_shuffle_1_white_24",
                "yt_outline_screen_full_exit_white_24",
                "yt_outline_screen_full_white_24"
        )

        val layouts = "layout" to arrayOf(
                "youtube_controls_bottom_ui_container"
        )

        val xmlResources = arrayOf(drawablexxxhdpi)
        val xmlResources2 = arrayOf(layouts)

        xmlResources.forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.png"

                Files.copy(
                        classLoader.getResourceAsStream("settings/$relativePath")!!,
                        data["res"].resolve(relativePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        xmlResources2.forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.xml"

                Files.copy(
                        classLoader.getResourceAsStream("settings/$relativePath")!!,
                        data["res"].resolve(relativePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }
}