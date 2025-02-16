package app.revanced.patches.youtube.layout.pivotbar.createbutton.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.MethodFingerprintExtensions.name
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.layout.pivotbar.createbutton.fingerprints.PivotBarCreateButtonViewFingerprint
import app.revanced.patches.youtube.layout.pivotbar.fingerprints.PivotBarFingerprint
import app.revanced.patches.youtube.layout.pivotbar.fingerprints.PivotBarNewFingerprint
import app.revanced.patches.youtube.layout.pivotbar.utils.InjectionUtils.injectHook
import app.revanced.patches.youtube.layout.pivotbar.utils.InjectionUtils.REGISTER_TEMPLATE_REPLACEMENT
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.shared.annotation.YouTubeCompatibility

@Patch
@DependsOn([IntegrationsPatch::class, ResourceMappingResourcePatch::class])
@Name("disable-create-button")
@Description("Hides the create button in the navigation bar.")
@YouTubeCompatibility
@Version("0.0.1")
class CreateButtonRemoverPatch : BytecodePatch(
    listOf(PivotBarFingerprint, PivotBarNewFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {

        /*
         * Resolve fingerprints
         */

        val pivotBarResult = try {
            PivotBarFingerprint.result!!
        } catch (e: Exception) {
            PivotBarNewFingerprint.result ?: return PatchResultError("PivotBarFingerprint failed")
        }

        if (!PivotBarCreateButtonViewFingerprint.resolve(context, pivotBarResult.mutableMethod, pivotBarResult.mutableClass))
            return PatchResultError("${PivotBarCreateButtonViewFingerprint.name} failed")

        val createButtonResult = PivotBarCreateButtonViewFingerprint.result!!
        val insertIndex = createButtonResult.scanResult.patternScanResult!!.endIndex

        /*
         * Inject hooks
         */

        val integrationsClass = "Lapp/revanced/integrations/patches/HideCreateButtonPatch;"
        val hook =
            "invoke-static { v$REGISTER_TEMPLATE_REPLACEMENT }, $integrationsClass->hideCreateButton(Landroid/view/View;)V"

        createButtonResult.mutableMethod.injectHook(hook, insertIndex)

        return PatchResultSuccess()
    }
}
