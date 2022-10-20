package app.revanced.patches.youtube.interaction.swipecontrols.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.extensions.YouTubeCompatibility

@Name("watch-while-activity-fingerprint")

@YouTubeCompatibility
@Version("0.0.1")
object WatchWhileActivityFingerprint : MethodFingerprint(
    customFingerprint = { methodDef ->
        methodDef.definingClass.endsWith("WatchWhileActivity;") && methodDef.name == "<init>"
    }
)
