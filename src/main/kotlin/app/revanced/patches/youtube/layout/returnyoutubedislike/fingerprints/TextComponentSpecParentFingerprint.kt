package app.revanced.patches.youtube.layout.returnyoutubedislike.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.annotation.YouTubeCompatibility

@Name("text-component-spec-parent-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object TextComponentSpecParentFingerprint : MethodFingerprint(
    null,
    null,
    null,
    null,
    listOf("TextComponentSpec: No converter for extension: ")
)