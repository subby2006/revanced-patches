package app.revanced.patches.youtube.extended.tabletlayout.patch

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
import app.revanced.patches.youtube.extended.tabletlayout.fingerprints.TabletLayoutFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("tablet-layout")
@Description("Tricks the dpi to use some tablet layouts.")
@YouTubeCompatibility
@Version("0.0.1")
class TabletLayoutPatch : BytecodePatch(
    listOf(
        TabletLayoutFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        TabletLayoutFingerprint.result!!.mutableMethod.addInstructions(
            4, """
                invoke-static {p0}, Lapp/revanced/integrations/patches/TabletLayoutOverridePatch;->getTabletLayoutOverride(I)I
                move-result p0
                """
        )

        return PatchResultSuccess()
    }
}
