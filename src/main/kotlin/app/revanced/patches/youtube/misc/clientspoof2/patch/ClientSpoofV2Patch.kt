package app.revanced.patches.youtube.misc.clientspoof2.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.BytecodeData
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.BytecodePatch
import app.revanced.patches.youtube.misc.clientspoof2.annotations.ClientSpoofV2Compatibility
import app.revanced.patches.youtube.misc.clientspoof2.fingerprints.ClientSpoofV2Fingerprint
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@Name("client-spoof-v2")
@Description("Spoof the YouTube client version to prevent fullscreen rotation issue.")
@ClientSpoofV2Compatibility
@Version("0.0.1")
class ClientSpoofV2Patch : BytecodePatch(
    listOf(
        ClientSpoofV2Fingerprint
    )
) {
    override fun execute(data: BytecodeData): PatchResult {

        val result = ClientSpoofV2Fingerprint.result!!
        val method = result.mutableMethod
        val index = result.scanResult.patternScanResult!!.endIndex
        val register = (method.implementation!!.instructions[index] as OneRegisterInstruction).registerA

        method.addInstructions(
            index + 1, """
            invoke-static {v$register}, Lapp/revanced/integrations/patches/VersionOverridePatch;->getVersionOverride(Ljava/lang/String;)Ljava/lang/String;
            move-result-object v$register
        """
        )

        return PatchResultSuccess()
    }
}