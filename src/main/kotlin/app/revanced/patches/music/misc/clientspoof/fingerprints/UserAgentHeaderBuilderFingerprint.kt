package app.revanced.patches.music.misc.clientspoof.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeMusicCompatibility
import org.jf.dexlib2.Opcode

@Name("user-agent-header-builder-fingerprint")
@YouTubeMusicCompatibility

@Version("0.0.1")
object UserAgentHeaderBuilderFingerprint : MethodFingerprint(
    parameters = listOf("L"),
    opcodes = listOf(Opcode.MOVE_RESULT_OBJECT, Opcode.INVOKE_VIRTUAL, Opcode.CONST_16),
    strings = listOf("(Linux; U; Android ")
)