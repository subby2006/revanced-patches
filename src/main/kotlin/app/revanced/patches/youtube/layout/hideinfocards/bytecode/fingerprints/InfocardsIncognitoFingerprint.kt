package app.revanced.patches.youtube.layout.hideinfocards.bytecode.fingerprints

import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags

@Name("infocards-incognito-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object InfocardsIncognitoFingerprint : MethodFingerprint(
    "Ljava/lang/Boolean;",
    AccessFlags.PUBLIC or AccessFlags.FINAL,
    strings = listOf("vibrator")
)