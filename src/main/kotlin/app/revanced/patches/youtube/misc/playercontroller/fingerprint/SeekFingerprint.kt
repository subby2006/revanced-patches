package app.revanced.patches.youtube.misc.playercontroller.fingerprint

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility

@Name("seek-fingerprint")

@YouTubeCompatibility
@Version("0.0.1")
object SeekFingerprint : MethodFingerprint(
    strings = listOf("Attempting to seek during an ad")
)