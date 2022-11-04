package app.revanced.patches.youtube.misc.playercontroller.fingerprint

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

@Name("create-video-player-seekbar-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object CreateVideoPlayerSeekbarFingerprint : MethodFingerprint(
    "V",
    strings = listOf("timed_markers_width")
)