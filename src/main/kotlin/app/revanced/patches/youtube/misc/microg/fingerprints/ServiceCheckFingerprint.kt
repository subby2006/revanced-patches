package app.revanced.patches.youtube.misc.microg.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.FuzzyPatternScanMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags

@Name("google-play-service-checker-fingerprint")
@FuzzyPatternScanMethod(2) // FIXME: Test this threshold and find the best value.
@YouTubeCompatibility
@Version("0.0.1")
object ServiceCheckFingerprint : MethodFingerprint(
    "V", AccessFlags.PUBLIC or AccessFlags.STATIC, listOf("L", "I"),
    strings = listOf("Google Play Services not available", "GooglePlayServices not available due to error ")
)