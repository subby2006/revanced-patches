package app.revanced.patches.youtube.layout.returnyoutubedislike.fingerprints

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint


@Name("text-component-spec-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object TextComponentFingerprint : MethodFingerprint(
    strings = listOf("com.google.android.apps.youtube.music")
)