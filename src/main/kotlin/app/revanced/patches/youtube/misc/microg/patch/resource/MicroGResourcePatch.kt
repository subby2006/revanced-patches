package app.revanced.patches.youtube.misc.microg.patch.resource

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.music.misc.microg.shared.Constants
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.patches.youtube.misc.microg.annotations.MicroGPatchCompatibility
import app.revanced.patches.youtube.misc.microg.shared.Constants.PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.REVANCED_APP_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.REVANCED_PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.SPOOFED_PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.SPOOFED_PACKAGE_SIGNATURE
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch
import app.revanced.util.microg.Constants.MICROG_VENDOR
import app.revanced.util.microg.MicroGManifestHelper
import app.revanced.util.microg.MicroGResourceHelper

@Name("microg-resource-patch")
@DependsOn([FixLocaleConfigErrorPatch::class, SettingsResourcePatch::class])
@Description("Resource patch to allow YouTube ReVanced to run without root and under a different package name.")
@MicroGPatchCompatibility
@Version("0.0.1")
class MicroGResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val xfile = context["res/xml/revanced_prefs.xml"]
        xfile.writeText(
                xfile.readText()
                        .replace(
                                "<PreferenceCategory android:layout_height=\"fill_parent\" android:title=\"@string/revanced_settings\" />",
                                "<PreferenceCategory android:title=\"@string/microg_settings\">\n        TEMP1\n    <PreferenceCategory android:layout_height=\"fill_parent\" android:title=\"@string/revanced_settings\" />"
                        ).replace(
                                "TEMP1",
                                "<PreferenceScreen android:title=\"@string/microg_notification_settings\" android:summary=\"@string/microg_notification_settings_summary\">\n            TEMP2"
                        ).replace(
                                "TEMP2",
                                "<intent android:targetPackage=\"$MICROG_VENDOR.android.gms\" android:targetClass=\"org.microg.gms.ui.SettingsActivity\" />\n        TEMP3"
                        ).replace(
                                "TEMP3",
                                "</PreferenceScreen>\n    TEMP4"
                        ).replace(
                                "TEMP4",
                                "</PreferenceCategory>"
                        )
        )

        val settingsFragment = context["res/xml/settings_fragment.xml"]
        settingsFragment.writeText(
            settingsFragment.readText().replace(
                "android:targetPackage=\"com.google.android.youtube",
                "android:targetPackage=\"$REVANCED_PACKAGE_NAME"
            )
        )

        // update manifest
        MicroGResourceHelper.patchManifest(
            context,
            PACKAGE_NAME,
            REVANCED_PACKAGE_NAME
        )

        // add metadata to manifest
        MicroGManifestHelper.addSpoofingMetadata(
            context,
            SPOOFED_PACKAGE_NAME,
            SPOOFED_PACKAGE_SIGNATURE
        )
        return PatchResultSuccess()
    }
}