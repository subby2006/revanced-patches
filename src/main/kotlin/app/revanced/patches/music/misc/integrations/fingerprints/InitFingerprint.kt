package app.revanced.patches.music.misc.integrations.fingerprints

import app.revanced.annotation.YouTubeMusicCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

@Name("init-fingerprint")
@YouTubeMusicCompatibility
@Version("0.0.1")
object InitFingerprint : MethodFingerprint(
    strings = listOf("YouTubeMusic", "activity")
)