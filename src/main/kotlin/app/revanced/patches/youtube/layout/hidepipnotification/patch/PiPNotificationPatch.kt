package app.revanced.patches.youtube.layout.hidepipnotification.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.layout.hidepipnotification.fingerprints.PiPNotificationFirstFingerprint
import app.revanced.patches.youtube.layout.hidepipnotification.fingerprints.PiPNotificationSecondFingerprint

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

        val PiPNotificationFirstResult = PiPNotificationFirstFingerprint.result!!
        val PiPNotificationSecondResult = PiPNotificationSecondFingerprint.result!!

        PiPNotificationFirstResult.mutableMethod.addInstruction(
            PiPNotificationFirstResult.scanResult.patternScanResult!!.startIndex + 1,
            "return-void"
        )

        PiPNotificationSecondResult.mutableMethod.addInstruction(
            PiPNotificationSecondResult.scanResult.patternScanResult!!.startIndex + 2,
            "return-void"
        )

        return PatchResultSuccess()
    }
}