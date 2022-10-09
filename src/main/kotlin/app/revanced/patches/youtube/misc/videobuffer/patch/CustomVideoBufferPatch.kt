package app.revanced.patches.youtube.misc.videobuffer.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patches.youtube.misc.videobuffer.annotations.CustomVideoBufferCompatibility
import app.revanced.patches.youtube.misc.videobuffer.fingerprints.MaxBufferFingerprint
import app.revanced.patches.youtube.misc.videobuffer.fingerprints.PlaybackBufferFingerprint
import app.revanced.patches.youtube.misc.videobuffer.fingerprints.ReBufferFingerprint
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@Name("custom-video-buffer")
@Description("Lets you change the buffers of videos.")
@CustomVideoBufferCompatibility
@Version("0.0.1")
class CustomVideoBufferPatch : BytecodePatch(
    listOf(
        MaxBufferFingerprint, PlaybackBufferFingerprint, ReBufferFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        execMaxBuffer()
        execPlaybackBuffer()
        execReBuffer()
        return PatchResultSuccess()
    }

    private fun execMaxBuffer() {
        val (method, result) = MaxBufferFingerprint.unwrap(true, -1)
        val (index, register) = result

        method.addInstructions(
            index + 1, """
           invoke-static {}, Lapp/revanced/integrations/patches/VideoBufferPatch;->getMaxBuffer()I
           move-result v$register
        """
        )
    }

    private fun execPlaybackBuffer() {
        val (method, result) = PlaybackBufferFingerprint.unwrap()
        val (index, register) = result

        method.addInstructions(
            index + 1, """
           invoke-static {}, Lapp/revanced/integrations/patches/VideoBufferPatch;->getPlaybackBuffer()I
           move-result v$register
        """
        )
    }

    private fun execReBuffer() {
        val (method, result) = ReBufferFingerprint.unwrap()
        val (index, register) = result

        method.addInstructions(
            index + 1, """
           invoke-static {}, Lapp/revanced/integrations/patches/VideoBufferPatch;->getReBuffer()I
           move-result v$register
        """
        )
    }

    private fun MethodFingerprint.unwrap(
        forEndIndex: Boolean = false,
        offset: Int = 0
    ): Pair<MutableMethod, Pair<Int, Int>> {
        val result = this.result!!
        val method = result.mutableMethod
        val scanResult = result.scanResult.patternScanResult!!
        val index = (if (forEndIndex) scanResult.endIndex else scanResult.startIndex) + offset

        val register = (method.instruction(index) as OneRegisterInstruction).registerA

        return method to (index to register)
    }
}
