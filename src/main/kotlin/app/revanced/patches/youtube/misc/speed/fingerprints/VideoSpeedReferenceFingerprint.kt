package app.revanced.patches.youtube.misc.speed.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.fingerprint.method.annotation.DirectPatternScanMethod
import app.revanced.patcher.fingerprint.method.annotation.MatchingMethod
import app.revanced.patches.youtube.misc.speed.annotations.DefaultVideoSpeedCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

@Name("video-speed-reference-fingerprint")
@MatchingMethod("Lklg;", "a")
@DirectPatternScanMethod
@DefaultVideoSpeedCompatibility
@Version("0.0.1")
object VideoSpeedReferenceFingerprint : MethodFingerprint(
    "V", AccessFlags.PUBLIC or AccessFlags.FINAL, listOf("L"), listOf(
        Opcode.IPUT_OBJECT, Opcode.RETURN_VOID
    )
)