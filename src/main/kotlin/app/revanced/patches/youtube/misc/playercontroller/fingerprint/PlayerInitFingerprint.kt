package app.revanced.patches.youtube.misc.playercontroller.fingerprint

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

@Name("player-init-fingerprint")

@YouTubeCompatibility
@Version("0.0.1")
object PlayerInitFingerprint : MethodFingerprint(
    strings = listOf(
        "playVideo called on player response with no videoStreamingData."
    ),
)