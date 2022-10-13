package app.revanced.patches.youtube.misc.packagename.patch.resource

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.patches.youtube.misc.microg.annotations.MicroGPatchCompatibility
import app.revanced.patches.youtube.misc.microg.shared.Constants.PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.REVANCED_PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.REVANCED_EXTENDED_PACKAGE_NAME
import app.revanced.util.microg.MicroGResourceHelper

@Name("custom-package-name-resource-patch")
@DependsOn([FixLocaleConfigErrorPatch::class])
@Description("Resource patch to allow ReVanced Extended to run under a different package name than ReVanced.")
@MicroGPatchCompatibility
@Version("0.0.1")
class CustomPackageNameResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val settingsFragment = context["res/xml/settings_fragment.xml"]
        settingsFragment.writeText(
            settingsFragment.readText()
            .replace(
                "android:targetPackage=\"$PACKAGE_NAME",
                "android:targetPackage=\"$REVANCED_EXTENDED_PACKAGE_NAME"
            ).replace(
                "android:targetPackage=\"$REVANCED_PACKAGE_NAME",
                "android:targetPackage=\"$REVANCED_EXTENDED_PACKAGE_NAME"
            )
        )

        // update manifest
        MicroGResourceHelper.patchManifest(
            context,
            PACKAGE_NAME,
            REVANCED_EXTENDED_PACKAGE_NAME
        )

        MicroGResourceHelper.patchManifest(
            context,
            REVANCED_PACKAGE_NAME,
            REVANCED_EXTENDED_PACKAGE_NAME
        )

        return PatchResultSuccess()
    }
}