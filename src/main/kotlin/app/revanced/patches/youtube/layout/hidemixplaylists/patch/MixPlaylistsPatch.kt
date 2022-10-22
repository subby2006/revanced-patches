package app.revanced.patches.youtube.layout.hidemixplaylists.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.layout.hidemixplaylists.fingerprints.MixPlaylistsPatchFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-my-mix")
@Description("Removes mix playlists from home feed and video player.")
@YouTubeCompatibility
@Version("0.0.1")
class MixPlaylistsPatch : BytecodePatch(
    listOf(
        MixPlaylistsPatchFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        val result = MixPlaylistsPatchFingerprint.result!!
        val method = result.mutableMethod
        val index = result.scanResult.patternScanResult!!.endIndex - 6
        val register = (method.implementation!!.instructions[index] as OneRegisterInstruction).registerA

        method.addInstruction(
            index + 2,
            "invoke-static {v$register}, Lapp/revanced/integrations/patches/HideMixPlaylistsPatch;->hideMixPlaylists(Landroid/view/View;)V"
        )

        return PatchResultSuccess()
    }
}
