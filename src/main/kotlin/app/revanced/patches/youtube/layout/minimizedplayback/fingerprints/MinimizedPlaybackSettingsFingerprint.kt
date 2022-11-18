package app.revanced.patches.youtube.layout.minimizedplayback.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.FuzzyPatternScanMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction
import org.jf.dexlib2.Opcode

@Name("minimized-playback-manager-fingerprint")
@FuzzyPatternScanMethod(2)
@YouTubeCompatibility
@Version("0.0.1")
object MinimizedPlaybackSettingsFingerprint : MethodFingerprint(
    "L",
    AccessFlags.PUBLIC or AccessFlags.FINAL,
    null,
    listOf(
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IF_EQZ,
        Opcode.IF_NEZ,
        Opcode.GOTO,
        Opcode.IGET_OBJECT,
        Opcode.CHECK_CAST
    ),
)
