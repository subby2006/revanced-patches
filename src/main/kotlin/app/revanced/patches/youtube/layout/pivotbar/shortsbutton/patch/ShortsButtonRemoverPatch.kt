package app.revanced.patches.youtube.layout.pivotbar.shortsbutton.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.MethodFingerprintExtensions.name
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patches.youtube.layout.pivotbar.utils.InjectionUtils.REGISTER_TEMPLATE_REPLACEMENT
import app.revanced.patches.youtube.layout.pivotbar.utils.InjectionUtils.injectHook
import app.revanced.patches.youtube.layout.pivotbar.fingerprints.PivotBarFingerprint
import app.revanced.patches.youtube.layout.pivotbar.shortsbutton.annotations.ShortsButtonCompatibility
import app.revanced.patches.youtube.layout.pivotbar.shortsbutton.fingerprints.PivotBarEnumFingerprint
import app.revanced.patches.youtube.layout.pivotbar.shortsbutton.fingerprints.PivotBarShortsButtonViewFingerprint
import app.revanced.patches.youtube.layout.pivotbar.shortsbutton.fingerprints.PivotBarButtonsViewFingerprint
import app.revanced.patches.youtube.layout.pivotbar.shortsbutton.fingerprints.PivotBarButtonTabEnumFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.Opcode

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-shorts-button")
@Description("Hides the shorts button on the navigation bar.")
@ShortsButtonCompatibility
@Version("0.0.1")
class ShortsButtonRemoverPatch : BytecodePatch(
    listOf(PivotBarFingerprint, PivotBarButtonsViewFingerprint, PivotBarButtonTabEnumFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        /*
         * Resolve fingerprints
         */

        try {
            val pivotBarResult = PivotBarFingerprint.result!!
            val fingerprintResults = arrayOf(PivotBarEnumFingerprint, PivotBarShortsButtonViewFingerprint)
                .onEach {
                        val resolutionSucceeded = it.resolve(
                            context,
                            pivotBarResult.method,
                            pivotBarResult.classDef
                        )

                        //if (!resolutionSucceeded) return PatchResultError("${it.name} failed")
                }
                .map { it.result!!.scanResult.patternScanResult!! }

            val enumScanResult = fingerprintResults[0]
            val buttonViewResult = fingerprintResults[1]

            val enumHookInsertIndex = enumScanResult.startIndex
            val buttonHookInsertIndex = buttonViewResult.endIndex

            /*
             * Inject hooks
             */

            val integrationsClass = "Lapp/revanced/integrations/patches/HideShortsButtonPatch;"

            val enumHook =
                "sput-object v$REGISTER_TEMPLATE_REPLACEMENT, $integrationsClass->lastPivotTab:Ljava/lang/Enum;"
            val buttonHook =
                "invoke-static { v$REGISTER_TEMPLATE_REPLACEMENT }, $integrationsClass->hideShortsButton(Landroid/view/View;)V"

            // Inject bottom to top to not mess up the indices
            mapOf(
                buttonHook to buttonHookInsertIndex,
                enumHook to enumHookInsertIndex
            ).forEach { (hook, insertIndex) ->
                pivotBarResult.mutableMethod.injectHook(hook, insertIndex)
            }
        } catch (e: Exception) {}
        try {
            val tabEnumResult = PivotBarButtonTabEnumFingerprint.result!!
            val tabEnumImplementation = tabEnumResult.mutableMethod.implementation!!
            val moveEnumInstruction = tabEnumImplementation.instructions[tabEnumResult.scanResult.patternScanResult!!.endIndex]
            val enumRegister = (moveEnumInstruction as OneRegisterInstruction).registerA

            val buttonsViewResult = PivotBarButtonsViewFingerprint.result!!
            val buttonsViewImplementation = buttonsViewResult.mutableMethod.implementation!!
            val moveViewInstruction = buttonsViewImplementation.instructions[buttonsViewResult.scanResult.patternScanResult!!.startIndex + 1]
            val viewRegister = (moveViewInstruction as OneRegisterInstruction).registerA


            // Save the tab enum in XGlobals to avoid smali/register workarounds
            tabEnumResult.mutableMethod.addInstruction(
                tabEnumResult.scanResult.patternScanResult!!.endIndex + 1,
                "sput-object v$enumRegister, Lapp/revanced/integrations/patches/HideShortsButtonPatch;->lastPivotTab:Ljava/lang/Enum;"
            )

            // Hide the button view via proxy by passing it to the hideShortsButton method
            // It only hides it if the last tab name is "TAB_SHORTS"
            buttonsViewResult.mutableMethod.addInstruction(
                buttonsViewResult.scanResult.patternScanResult!!.startIndex + 3,
                "invoke-static { v$viewRegister }, Lapp/revanced/integrations/patches/HideShortsButtonPatch;->hideShortsButton(Landroid/view/View;)V"
            )
        } catch (e: Exception) {
            PatchResultError("Could not find the correct register")
        }

        return PatchResultSuccess()
    }
}