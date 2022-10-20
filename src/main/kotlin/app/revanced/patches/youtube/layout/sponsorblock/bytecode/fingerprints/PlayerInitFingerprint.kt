package app.revanced.patches.youtube.layout.sponsorblock.bytecode.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.extensions.YouTubeCompatibility

@Name("player-init-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object PlayerInitFingerprint : MethodFingerprint(
    strings = listOf(
        "playVideo called on player response with no videoStreamingData."
    ),
)