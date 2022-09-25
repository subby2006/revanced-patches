package app.revanced.patches.youtube.misc.clientspoof2.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.MatchingMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.youtube.misc.clientspoof2.annotations.ClientSpoofV2Compatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

@Name("client-spoof-v2-fingerprint")
@MatchingMethod("Ltec;", "d")
@ClientSpoofV2Compatibility
@Version("0.0.1")
object ClientSpoofV2Fingerprint : MethodFingerprint(
    "Ljava/lang/String;", AccessFlags.PUBLIC or AccessFlags.STATIC, listOf("L"), listOf(
        Opcode.CONST_STRING,
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT,
        Opcode.CONST_4,
        Opcode.IF_EQZ,
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.IGET_OBJECT,
        Opcode.IF_EQZ,
        Opcode.IGET_OBJECT
    ),
    strings = listOf("pref_override_build_version_name")
)