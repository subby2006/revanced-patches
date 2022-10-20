package app.revanced.patches.music.misc.packagename.patch.bytecode

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.music.misc.packagename.patch.resource.CustomPackageNameResourcePatch
import app.revanced.extensions.YouTubeMusicCompatibility
import app.revanced.patches.music.misc.microg.fingerprints.*
import app.revanced.patches.music.misc.microg.patch.bytecode.MusicMicroGBytecodePatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.patches.music.misc.microg.shared.Constants.REVANCED_MUSIC_PACKAGE_NAME
import app.revanced.patches.music.misc.microg.shared.Constants.REVANCED_EXTENDED_MUSIC_PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants
import app.revanced.util.microg.MicroGBytecodeHelper

@Patch(false)
@DependsOn(
    [
        MusicMicroGBytecodePatch::class,
        CustomPackageNameResourcePatch::class
    ]
)
@Name("custom-package-name-music")
@Description("Allows ReVanced Extended Music to run under a different package name than ReVanced Music (NON-ROOT users only!).")
@YouTubeMusicCompatibility
@Version("0.0.1")
class CustomPackageNameBytecodePatch : BytecodePatch(
    listOf(
        ServiceCheckFingerprint,
        GooglePlayUtilityFingerprint,
        CastDynamiteModuleFingerprint,
        CastDynamiteModuleV2Fingerprint,
        CastContextFetchFingerprint,
        PrimeFingerprint,
    )
) {
    // NOTE: the previous patch also replaced the following strings, but it seems like they are not needed:
    // - "com.google.android.gms.chimera.GmsIntentOperationService",
    // - "com.google.android.gms.phenotype.internal.IPhenotypeCallbacks",
    // - "com.google.android.gms.phenotype.internal.IPhenotypeService",
    // - "com.google.android.gms.phenotype.PACKAGE_NAME",
    // - "com.google.android.gms.phenotype.UPDATE",
    // - "com.google.android.gms.phenotype",
    override fun execute(context: BytecodeContext) =
        // apply common microG patch
        MicroGBytecodeHelper.patchBytecode(
            context,
            arrayOf(
                MicroGBytecodeHelper.packageNameTransform(
                    Constants.PACKAGE_NAME,
                    Constants.REVANCED_EXTENDED_PACKAGE_NAME
                ),
                MicroGBytecodeHelper.packageNameTransform(
                    Constants.REVANCED_PACKAGE_NAME,
                    Constants.REVANCED_EXTENDED_PACKAGE_NAME
                )
            ),
            MicroGBytecodeHelper.PrimeMethodTransformationData(
                PrimeFingerprint,
                REVANCED_MUSIC_PACKAGE_NAME,
                REVANCED_EXTENDED_MUSIC_PACKAGE_NAME
            ),
            listOf(
                ServiceCheckFingerprint,
                GooglePlayUtilityFingerprint,
                CastDynamiteModuleFingerprint,
                CastDynamiteModuleV2Fingerprint,
                CastContextFetchFingerprint
            )
        ).let { PatchResultSuccess() }

}