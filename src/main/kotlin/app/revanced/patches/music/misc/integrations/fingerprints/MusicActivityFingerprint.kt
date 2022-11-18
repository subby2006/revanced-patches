package app.revanced.patches.music.misc.integrations.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeMusicCompatibility
import org.jf.dexlib2.Opcode

@Name("music-activity-fingerprint")
@YouTubeMusicCompatibility
@Version("0.0.1")
object MusicActivityFingerprint : MethodFingerprint(
    null, null, null, listOf(Opcode.INVOKE_DIRECT_RANGE), null, { methodDef ->
        methodDef.definingClass.endsWith("MusicActivity;") && methodDef.name == "onCreate"
    }
)