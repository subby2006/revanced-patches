package app.revanced.patches.youtube.extended.layoutswitch.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patches.youtube.extended.layoutswitch.fingerprints.LayoutSwitchFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("layout-switch")
@Description("Tricks the dpi to use some tablet/phone layouts.")
@YouTubeCompatibility
@Version("0.0.1")
class LayoutSwitchPatch : BytecodePatch(
    listOf(
        LayoutSwitchFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        LayoutSwitchFingerprint.result!!.mutableMethod.addInstructions(
            4, """
                invoke-static {p0}, Lapp/revanced/integrations/patches/LayoutSwitchOverridePatch;->getLayoutSwitchOverride(I)I
                move-result p0
                """
        )

        return PatchResultSuccess()
    }
}
