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
import app.revanced.patches.youtube.layout.hidemixplaylists.fingerprints.MixPlaylistsPatchFirtstFingerprint
import app.revanced.patches.youtube.layout.hidemixplaylists.fingerprints.MixPlaylistsPatchSecondFingerprint
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
        MixPlaylistsPatchFirtstFingerprint, MixPlaylistsPatchSecondFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        val firstresult = MixPlaylistsPatchFirtstFingerprint.result!!
        val firstmethod = firstresult.mutableMethod
        val firstindex = firstresult.scanResult.patternScanResult!!.endIndex - 6
        val firstregister = (firstmethod.implementation!!.instructions[firstindex] as OneRegisterInstruction).registerA

        firstmethod.addInstruction(
            firstindex + 2,
            "invoke-static {v$firstregister}, Lapp/revanced/integrations/patches/HideMixPlaylistsPatch;->hideMixPlaylists(Landroid/view/View;)V"
        )

        val secondresult = MixPlaylistsPatchSecondFingerprint.result!!
        val secondmethod = secondresult.mutableMethod
        val secondindex = secondresult.scanResult.patternScanResult!!.endIndex - 5
        val secondregister = (secondmethod.implementation!!.instructions[secondindex] as OneRegisterInstruction).registerA

        secondmethod.addInstruction(
            secondindex + 2,
            "invoke-static {v$secondregister}, Lapp/revanced/integrations/patches/HideMixPlaylistsPatch;->hideMixPlaylists(Landroid/view/View;)V"
        )

        return PatchResultSuccess()
    }
}
