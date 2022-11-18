package app.revanced.patches.youtube.layout.hidemixplaylists.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.layout.hidemixplaylists.bytecode.fingerprints.*
import app.revanced.patches.youtube.layout.hidemixplaylists.resource.patch.MixPlaylistsResourcePatch
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.iface.instruction.formats.Instruction21c
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction

@Patch
@DependsOn([IntegrationsPatch::class, MixPlaylistsResourcePatch::class])
@Name("hide-my-mix")
@Description("Removes mix playlists from home feed and video player.")
@YouTubeCompatibility
@Version("0.0.1")
class MixPlaylistsPatch : BytecodePatch(
    listOf(
        CreateMixPlaylistFingerprint, SecondCreateMixPlaylistFingerprint, ThirdCreateMixPlaylistFingerprint, FourthCreateMixPlaylistFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        arrayOf(CreateMixPlaylistFingerprint, SecondCreateMixPlaylistFingerprint).forEach(::addHook)
        arrayOf(ThirdCreateMixPlaylistFingerprint).forEach(::addthirdHook)
        arrayOf(FourthCreateMixPlaylistFingerprint).forEach(::addfourthHook)

        return PatchResultSuccess()
    }

    private fun addHook(fingerprint: MethodFingerprint) {
        with (fingerprint.result!!) {
            val insertIndex = scanResult.patternScanResult!!.endIndex - 3

            val register = (mutableMethod.instruction(insertIndex - 2) as OneRegisterInstruction).registerA

            mutableMethod.addInstruction(
                insertIndex,
                "invoke-static {v$register}, Lapp/revanced/integrations/patches/HideMixPlaylistsPatch;->hideMixPlaylists(Landroid/view/View;)V"
            )
        }
    }

    private fun addthirdHook(fingerprint: MethodFingerprint) {
        with (fingerprint.result!!) {
            val insertIndex = scanResult.patternScanResult!!.endIndex

            val register = (mutableMethod.instruction(insertIndex) as TwoRegisterInstruction).registerA

            mutableMethod.addInstruction(
                insertIndex,
                "invoke-static {v$register}, Lapp/revanced/integrations/patches/HideMixPlaylistsPatch;->hideMixPlaylists(Landroid/view/View;)V"
            )
        }
    }

    private fun addfourthHook(fingerprint: MethodFingerprint) {
        with (fingerprint.result!!) {
            val insertIndex = scanResult.patternScanResult!!.endIndex

            val register = (mutableMethod.instruction(insertIndex) as Instruction21c).registerA

            mutableMethod.addInstruction(
                insertIndex,
                "invoke-static {v$register}, Lapp/revanced/integrations/patches/HideMixPlaylistsPatch;->hideMixPlaylists(Landroid/view/View;)V"
            )
        }
    }
}
