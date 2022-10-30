package app.revanced.patches.youtube.layout.watermark.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.FuzzyPatternScanMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags

@Name("hide-watermark-parent-signature")
@FuzzyPatternScanMethod(3)
@YouTubeCompatibility
@Version("0.0.1")
object HideWatermarkParentFingerprint : MethodFingerprint (
    "L", AccessFlags.PUBLIC or AccessFlags.FINAL, null, null, listOf("player_overlay_in_video_programming"), null
)
