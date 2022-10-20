package app.revanced.patches.youtube.misc.settings.bytecode.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.extensions.YouTubeCompatibility

// TODO: This is more of a class fingerprint than a method fingerprint.
//  Convert to a class fingerprint whenever possible.
@Name("revanced-settings-activity-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object ReVancedSettingsActivityFingerprint : MethodFingerprint(
    customFingerprint = { methodDef ->
        methodDef.definingClass.endsWith("ReVancedSettingActivity;") && methodDef.name == "initializeSettings"
    }
)