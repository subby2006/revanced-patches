package app.revanced.patches.youtube.misc.videobuffer.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.extensions.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction

@Name("playbackbuffer-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object ReBufferFingerprint : MethodFingerprint(
    "I", AccessFlags.PUBLIC or AccessFlags.FINAL, listOf(),
    listOf(Opcode.IF_LEZ, Opcode.RETURN),
    customFingerprint = { methodDef ->
        methodDef.definingClass == "Lcom/google/android/libraries/youtube/innertube/model/media/PlayerConfigModel;"
                && methodDef.implementation!!.instructions.any {
            ((it as? NarrowLiteralInstruction)?.narrowLiteral == 5000)
        }
    }
)