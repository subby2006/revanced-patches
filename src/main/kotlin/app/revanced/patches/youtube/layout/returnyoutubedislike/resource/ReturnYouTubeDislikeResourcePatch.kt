package app.revanced.patches.youtube.layout.returnyoutubedislike.resource.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.shared.annotation.YouTubeCompatibility

@DependsOn([FixLocaleConfigErrorPatch::class])
@Name("return-youtube-dislike-resource-patch")
@Description("Adds the preferences for Return YouTube Dislike.")
@YouTubeCompatibility
@Version("0.0.1")
class ReturnYouTubeDislikeResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val settingsFragment = context["res/xml/settings_fragment.xml"]
        settingsFragment.writeText(
                settingsFragment.readText()
                        .replace(
                                "<Preference android:title=\"@string/pref_about_category\" android:key=\"@string/about_key\" android:fragment=\"com.google.android.apps.youtube.app.settings.AboutPrefsFragment\" app:iconSpaceReserved=\"false\" />",
                                "<Preference android:title=\"@string/revanced_ryd_settings_title\" android:summary=\"@string/revanced_ryd_settings_summary\"><intent android:targetPackage=\"com.google.android.youtube\" android:data=\"ryd_settings\" android:targetClass=\"com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity\" /></Preference><Preference android:title=\"@string/pref_about_category\" android:key=\"@string/about_key\" android:fragment=\"com.google.android.apps.youtube.app.settings.AboutPrefsFragment\" app:iconSpaceReserved=\"false\" />"
                        )
        )

        return PatchResultSuccess()
    }
}