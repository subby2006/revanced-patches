package app.revanced.patches.youtube.layout.pivotbar.shortsbutton.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.Opcode

@Name("pivot-bar-enum-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object PivotBarEnumFingerprint : MethodFingerprint(
    opcodes = listOf(
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.IF_NEZ, // target reference
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
    )
)