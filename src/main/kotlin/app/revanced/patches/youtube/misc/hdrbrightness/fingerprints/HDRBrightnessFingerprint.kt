package app.revanced.patches.youtube.misc.hdrbrightness.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.Opcode

@Name("hdr-brightness-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object HDRBrightnessFingerprint : MethodFingerprint(
    "V",
    opcodes = listOf(Opcode.CMPL_FLOAT),
    strings = listOf("c.SettingNotFound;", "screen_brightness", "android.mediaview"),
)