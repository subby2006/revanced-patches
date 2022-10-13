package app.revanced.patches.youtube.misc.packagename.patch.bytecode

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.misc.packagename.patch.resource.CustomPackageNameResourcePatch
import app.revanced.patches.youtube.misc.microg.annotations.MicroGPatchCompatibility
import app.revanced.patches.youtube.misc.microg.fingerprints.*
import app.revanced.patches.youtube.misc.microg.patch.bytecode.MicroGBytecodePatch
import app.revanced.patches.youtube.misc.microg.shared.Constants.PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.REVANCED_PACKAGE_NAME
import app.revanced.patches.youtube.misc.microg.shared.Constants.REVANCED_EXTENDED_PACKAGE_NAME
import app.revanced.util.microg.MicroGBytecodeHelper

@Patch(false)
@DependsOn(
    [
        MicroGBytecodePatch::class,
        CustomPackageNameResourcePatch::class
    ]
)
@Name("custom-package-name")
@Description("Allows ReVanced Extended to run under a different package name than ReVanced (NON-ROOT users only!).")
@MicroGPatchCompatibility
@Version("0.0.1")
class CustomPackageNameBytecodePatch : BytecodePatch(
    listOf(
        IntegrityCheckFingerprint,
        ServiceCheckFingerprint,
        GooglePlayUtilityFingerprint,
        CastDynamiteModuleFingerprint,
        CastDynamiteModuleV2Fingerprint,
        CastContextFetchFingerprint,
        PrimeFingerprint,
    )
) {
    override fun execute(context: BytecodeContext) =
        // apply common microG patch
        MicroGBytecodeHelper.patchBytecode(
            context, arrayOf(
                MicroGBytecodeHelper.packageNameTransform(
                    PACKAGE_NAME,
                    REVANCED_EXTENDED_PACKAGE_NAME
                ),
                MicroGBytecodeHelper.packageNameTransform(
                    REVANCED_PACKAGE_NAME,
                    REVANCED_EXTENDED_PACKAGE_NAME
                )
            ),
            MicroGBytecodeHelper.PrimeMethodTransformationData(
                PrimeFingerprint,
                REVANCED_PACKAGE_NAME,
                REVANCED_EXTENDED_PACKAGE_NAME
            ),
            listOf(
                IntegrityCheckFingerprint,
                ServiceCheckFingerprint,
                GooglePlayUtilityFingerprint,
                CastDynamiteModuleFingerprint,
                CastDynamiteModuleV2Fingerprint,
                CastContextFetchFingerprint
            )
        ).let { PatchResultSuccess() }
}