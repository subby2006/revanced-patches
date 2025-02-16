package app.revanced.patches.music.misc.microg.patch.bytecode

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.music.misc.clientspoof.patch.ClientSpoofMusicPatch
import app.revanced.patches.music.misc.microg.fingerprints.*
import app.revanced.patches.music.misc.microg.patch.resource.MusicMicroGResourcePatch
import app.revanced.patches.music.misc.microg.shared.Constants.MUSIC_PACKAGE_NAME
import app.revanced.patches.music.misc.microg.shared.Constants.REVANCED_MUSIC_PACKAGE_NAME
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.patches.youtube.misc.microg.shared.Constants
import app.revanced.shared.annotation.YouTubeMusicCompatibility
import app.revanced.util.microg.MicroGBytecodeHelper

@Patch
@DependsOn(
    [
        MusicMicroGResourcePatch::class,
        ResourceMappingResourcePatch::class,
        ClientSpoofMusicPatch::class
    ]
)
@Name("music-microg-support")
@Description("Allows YouTube Music ReVanced to run without root and under a different package name.")
@YouTubeMusicCompatibility
@Version("0.0.2")
class MusicMicroGBytecodePatch : BytecodePatch(
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
                    Constants.REVANCED_PACKAGE_NAME
                )
            ),
            MicroGBytecodeHelper.PrimeMethodTransformationData(
                PrimeFingerprint,
                MUSIC_PACKAGE_NAME,
                REVANCED_MUSIC_PACKAGE_NAME
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
