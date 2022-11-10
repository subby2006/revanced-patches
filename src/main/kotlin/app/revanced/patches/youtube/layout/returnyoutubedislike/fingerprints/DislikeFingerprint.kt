package app.revanced.patches.youtube.layout.returnyoutubedislike.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.FuzzyPatternScanMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags

@Name("dislike-fingerprint")
@FuzzyPatternScanMethod(2)
@YouTubeCompatibility
@Version("0.0.2")
object DislikeFingerprint : MethodFingerprint(
    "V",
    AccessFlags.PROTECTED or AccessFlags.CONSTRUCTOR,
    null,
    null,
    listOf("like/dislike")
)