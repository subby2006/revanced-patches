package app.revanced.patches.youtube.layout.watchinvr.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.youtube.layout.watchinvr.fingerprints.WatchinVRFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility

@Patch
@Name("hide-watch-in-vr")
@Description("Hide the Watch in VR item from the menu item.")
@YouTubeCompatibility
@Version("0.0.1")
class WatchinVRPatch : BytecodePatch(
    listOf(
        WatchinVRFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        WatchinVRFingerprint.result!!.mutableMethod.addInstructions(
            0, """
                invoke-static {}, Lapp/revanced/integrations/patches/HideWatchinVRPatch;->hideWatchinVR()Z
                move-result v0
                if-eqz v0, :shown
                return-void
                """, listOf(ExternalLabel("shown", WatchinVRFingerprint.result!!.mutableMethod.instruction(0)))
        )

        return PatchResultSuccess()
    }
}