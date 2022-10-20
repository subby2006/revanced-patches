package app.revanced.patches.music.misc.packagename.patch.resource

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.extensions.YouTubeMusicCompatibility
import app.revanced.patches.music.misc.microg.shared.Constants.REVANCED_MUSIC_PACKAGE_NAME
import app.revanced.patches.music.misc.microg.shared.Constants.REVANCED_EXTENDED_MUSIC_PACKAGE_NAME
import app.revanced.patches.music.misc.microg.shared.Constants.SPOOFED_PACKAGE_NAME
import app.revanced.patches.music.misc.microg.shared.Constants.SPOOFED_PACKAGE_SIGNATURE
import app.revanced.util.microg.MicroGManifestHelper
import app.revanced.util.microg.MicroGResourceHelper

@Name("custom-package-name-music-resource-patch")
@Description("Resource patch to allow ReVanced Extended Music to run under a different package name than ReVanced Music.")
@YouTubeMusicCompatibility
@Version("0.0.1")
class CustomPackageNameResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        // update manifest
        MicroGResourceHelper.patchManifest(
            context,
            REVANCED_MUSIC_PACKAGE_NAME,
            REVANCED_EXTENDED_MUSIC_PACKAGE_NAME
        )
        return PatchResultSuccess()
    }
}