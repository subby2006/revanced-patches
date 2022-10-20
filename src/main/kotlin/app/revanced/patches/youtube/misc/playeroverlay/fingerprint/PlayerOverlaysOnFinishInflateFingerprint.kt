package app.revanced.patches.youtube.misc.playeroverlay.fingerprint

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.extensions.YouTubeCompatibility

@Name("player-overlays-onFinishInflate-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object PlayerOverlaysOnFinishInflateFingerprint : MethodFingerprint(
    null, null, null, null, null, { methodDef ->
        methodDef.definingClass.endsWith("YouTubePlayerOverlaysLayout;") && methodDef.name == "onFinishInflate"
    }
)
