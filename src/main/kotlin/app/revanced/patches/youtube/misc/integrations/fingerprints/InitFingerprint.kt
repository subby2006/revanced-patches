package app.revanced.patches.youtube.misc.integrations.fingerprints

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

@Name("init-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object InitFingerprint : MethodFingerprint(
    strings = listOf("Application creation")
)