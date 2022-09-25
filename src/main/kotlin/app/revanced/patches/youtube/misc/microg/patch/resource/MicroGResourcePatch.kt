package app.revanced.patches.youtube.misc.microg.patch.resource

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.patches.youtube.misc.microg.annotations.MicroGPatchCompatibility
import app.revanced.patches.youtube.misc.microg.shared.Constants.BASE_MICROG_PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.REVANCED_PACKAGE_NAME
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch

@Name("microg-resource-patch")
@DependsOn([FixLocaleConfigErrorPatch::class, SettingsResourcePatch::class])
@Description("Resource patch to allow YouTube ReVanced to run without root and under a different package name.")
@MicroGPatchCompatibility
@Version("0.0.1")
class MicroGResourcePatch : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {

        val xfile = data["res/xml/revanced_prefs.xml"]
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
                                "<intent android:targetPackage=\"com.mgoogle.android.gms\" android:targetClass=\"org.microg.gms.ui.SettingsActivity\" />\n        TEMP3"
                        ).replace(
                                "TEMP3",
                                "</PreferenceScreen>\n    TEMP4"
                        ).replace(
                                "TEMP4",
                                "</PreferenceCategory>"
                        )
        )

        val settingsFragment = data["res/xml/settings_fragment.xml"]
        settingsFragment.writeText(
            settingsFragment.readText().replace(
                "android:targetPackage=\"com.google.android.youtube",
                "android:targetPackage=\"$REVANCED_PACKAGE_NAME"
            )
        )

        val manifest = data["AndroidManifest.xml"]
        manifest.writeText(
            manifest.readText()
                .replace(
                    "package=\"com.google.android.youtube", "package=\"$REVANCED_PACKAGE_NAME"
                ).replace(
                    "android:authorities=\"com.google.android.youtube", "android:authorities=\"$REVANCED_PACKAGE_NAME"
                ).replace(
                    "com.google.android.youtube.permission.C2D_MESSAGE", "$REVANCED_PACKAGE_NAME.permission.C2D_MESSAGE"
                ).replace( // might not be needed
                    "com.google.android.youtube.lifecycle-trojan", "$REVANCED_PACKAGE_NAME.lifecycle-trojan"
                ).replace( // might not be needed
                    "com.google.android.youtube.photopicker_images", "$REVANCED_PACKAGE_NAME.photopicker_images"
                ).replace(
                    "com.google.android.c2dm", "$BASE_MICROG_PACKAGE_NAME.android.c2dm"
                ).replace(
                    "</queries>", "<package android:name=\"$BASE_MICROG_PACKAGE_NAME.android.gms\"/></queries>"
                )
        )

        return PatchResultSuccess()
    }
}