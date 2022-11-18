package app.revanced.patches.youtube.layout.oldqualitylayout.patch

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
import app.revanced.patches.youtube.layout.oldqualitylayout.fingerprints.QualityMenuViewInflateFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.iface.instruction.FiveRegisterInstruction

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("old-quality-layout")
@Description("Enables the original quality flyout menu.")
@YouTubeCompatibility
@Version("0.0.1")
class OldQualityLayoutPatch : BytecodePatch(
    listOf(QualityMenuViewInflateFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val inflateFingerprintResult = QualityMenuViewInflateFingerprint.result!!
        val method = inflateFingerprintResult.mutableMethod
        val instructions = method.implementation!!.instructions

        // at this index the listener is added to the list view
        val listenerInvokeRegister = instructions.size - 1 - 1

        // get the register which stores the quality menu list view
        val onItemClickViewRegister = (instructions[listenerInvokeRegister] as FiveRegisterInstruction).registerC

        // insert the integrations method
        method.addInstruction(
            listenerInvokeRegister, // insert the integrations instructions right before the listener
            "invoke-static { v$onItemClickViewRegister }, Lapp/revanced/integrations/patches/OldQualityLayoutPatch;->showOldQualityMenu(Landroid/widget/ListView;)V"
        )

        return PatchResultSuccess()
    }
}
