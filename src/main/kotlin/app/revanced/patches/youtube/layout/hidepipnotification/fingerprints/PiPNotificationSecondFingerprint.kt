package app.revanced.patches.youtube.layout.hidepipnotification.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.youtube.layout.hidepipnotification.annotations.PiPNotificationCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

@Name("pip-notification-second-fingerprint")
@PiPNotificationCompatibility
@Version("0.0.1")
object PiPNotificationSecondFingerprint : MethodFingerprint(
    "V", AccessFlags.PUBLIC or AccessFlags.FINAL, listOf("L"), listOf(
        Opcode.IGET_OBJECT,
        Opcode.CHECK_CAST,
        Opcode.INVOKE_VIRTUAL,
        Opcode.CHECK_CAST,
        Opcode.INVOKE_VIRTUAL,
        Opcode.IPUT_BOOLEAN,
        Opcode.IGET_OBJECT,
        Opcode.IF_NEZ,
        Opcode.NEW_INSTANCE,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_DIRECT,
        Opcode.IPUT_OBJECT,
        Opcode.IGET_OBJECT,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_STATIC,
        Opcode.IGET_OBJECT,
        Opcode.NEW_INSTANCE,
        Opcode.INVOKE_DIRECT,
        Opcode.CONST_STRING
    ),
    strings = listOf("honeycomb.Shell\$HomeActivity")
)