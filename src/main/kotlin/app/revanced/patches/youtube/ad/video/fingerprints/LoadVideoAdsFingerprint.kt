package app.revanced.patches.youtube.ad.video.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.extensions.YouTubeCompatibility

@Name("load-ads-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object LoadVideoAdsFingerprint : MethodFingerprint(
    strings = listOf(
        "validateEnterSlot",
        "markFillRequested",
        "Trying to enter a slot when a slot of same type and physical position is already active. Its status: ",
    )
)