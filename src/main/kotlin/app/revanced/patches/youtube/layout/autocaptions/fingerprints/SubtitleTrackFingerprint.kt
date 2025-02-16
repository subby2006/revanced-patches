package app.revanced.patches.youtube.layout.autocaptions.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

@Name("subtitletrack-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object SubtitleTrackFingerprint : MethodFingerprint(
    "Z", AccessFlags.PUBLIC or AccessFlags.FINAL, listOf(), listOf(
        Opcode.CONST_STRING,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.RETURN,
    ),
    strings = listOf("DISABLE_CAPTIONS_OPTION")
)