
package app.revanced.patches.youtube.misc.speed.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.DirectPatternScanMethod
import app.revanced.patcher.fingerprint.method.annotation.MatchingMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.youtube.misc.speed.annotations.DefaultVideoSpeedCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

@Name("video-speed-setter-fingerprint")
@MatchingMethod(
    "Lklg;", "b"
)
@DirectPatternScanMethod
@DefaultVideoSpeedCompatibility
@Version("0.0.1")
object VideoSpeedSetterFingerprint : MethodFingerprint(
    "V",
    AccessFlags.PUBLIC or AccessFlags.FINAL,
    listOf("[L", "I"),
    listOf(
        Opcode.MOVE_RESULT,
        Opcode.IF_EQZ,
        Opcode.INVOKE_VIRTUAL,
        Opcode.INVOKE_STATIC,
        Opcode.INVOKE_VIRTUAL,
    )
)