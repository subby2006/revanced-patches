package app.revanced.patches.youtube.extended.hidebuttoncontainer.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags

@Name("litho-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object LithoFingerprint : MethodFingerprint(
    "L", AccessFlags.PUBLIC or AccessFlags.FINAL, listOf("L", "L", "L", "L", "L", "I", "Z"),
    strings = listOf("LoggingProperties are not in proto format",
        "Number of bits must be positive",
        "Failed to parse LoggingProperties")
)