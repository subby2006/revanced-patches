package app.revanced.patches.youtube.layout.hidepipnotification.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.layout.hidepipnotification.fingerprints.PiPNotificationFirstFingerprint
import app.revanced.patches.youtube.layout.hidepipnotification.fingerprints.PiPNotificationSecondFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility

@Patch
@Name("hide-pip-notification")
@Description("Disable pip notification when you first launch pip mode.")
@YouTubeCompatibility
@Version("0.0.1")
class PiPNotificationPatch : BytecodePatch(
    listOf(
        PiPNotificationFirstFingerprint, PiPNotificationSecondFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        PiPNotificationFirstFingerprint.hookNotificationParser(true)
        PiPNotificationSecondFingerprint.hookNotificationParser(false)

        return PatchResultSuccess()
    }
}

fun MethodFingerprint.hookNotificationParser(isPrimaryFingerprint: Boolean) {
    with(this.result!!) {
        val startIndex = scanResult.patternScanResult!!.startIndex
        val insertIndex = if (isPrimaryFingerprint) 1 else 2

        mutableMethod.addInstruction(
            startIndex + insertIndex,
            "return-void"
        )
    }
}