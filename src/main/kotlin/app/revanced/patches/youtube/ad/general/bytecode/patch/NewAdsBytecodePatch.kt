package app.revanced.patches.youtube.ad.general.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.ad.general.bytecode.fingerprints.NewAdsFingerprint
import app.revanced.patches.youtube.ad.general.resource.patch.NewAdsResourcePatch
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.shared.annotation.NewYouTubeCompatibility

@Patch
@DependsOn([IntegrationsPatch::class, NewAdsResourcePatch::class])
@Name("new-general-ads")
@Description("Removes general ads for v17.44.xx+.")
@NewYouTubeCompatibility
@Version("0.0.1")
class NewAdsBytecodePatch : BytecodePatch(
    listOf(
        NewAdsFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val result = NewAdsFingerprint.result!!
        val method = result.mutableMethod

        val insertIndex =
            result.scanResult.patternScanResult!!.endIndex + 4

        val register = 1

        method.addInstruction(
            insertIndex,
            "invoke-static {p$register}, Lapp/revanced/integrations/patches/HideHomeAdsPatch;->HideHomeAds(Landroid/view/View;)V"
        )

        return PatchResultSuccess()
    }
}