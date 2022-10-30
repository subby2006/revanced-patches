package app.revanced.patches.youtube.misc.clientspoof.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.annotation.YouTubeClientCompatibility
import org.jf.dexlib2.Opcode

@Name("user-agent-header-builder-fingerprint")
@YouTubeClientCompatibility
@Version("0.0.1")
object UserAgentHeaderBuilderFingerprint : MethodFingerprint(
    parameters = listOf("L", "L", "L"),
    opcodes = listOf(Opcode.MOVE_RESULT_OBJECT, Opcode.INVOKE_VIRTUAL),
    strings = listOf("(Linux; U; Android "),
)