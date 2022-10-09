package app.revanced.patches.youtube.extended.overlaybuttons.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patches.youtube.extended.overlaybuttons.annotation.OverlayButtonsCompatibility
import app.revanced.patches.youtube.extended.overlaybuttons.resource.patch.OverlayButtonsResourcePatch
import app.revanced.patches.youtube.misc.playercontrols.bytecode.patch.PlayerControlsBytecodePatch
import app.revanced.patches.youtube.misc.videoid.patch.VideoIdPatch

@Patch
@Name("overlay-buttons")
@DependsOn([OverlayButtonsResourcePatch::class, PlayerControlsBytecodePatch::class, VideoIdPatch::class])
@Description("Add overlay buttons for YouTube - copy, copy with timestamp, repeat, download")
@OverlayButtonsCompatibility
@Version("0.0.1")
class OverlayButtonsBytecodePatch : BytecodePatch() {
    override fun execute(context: BytecodeContext): PatchResult {
        val integrationsPackage = "app/revanced/integrations"
        val classDownloadDescriptor = "L$integrationsPackage/videoplayer/Download;"
        val classAutoRepeatDescriptor = "L$integrationsPackage/videoplayer/AutoRepeat;"
        val classCopyDescriptor = "L$integrationsPackage/videoplayer/Copy;"
        val classCopyWithTimeStampDescriptor = "L$integrationsPackage/videoplayer/CopyWithTimeStamp;"

        /*
        initialize the control
         */

        val initializeDownloadDescriptor = "$classDownloadDescriptor->initializeDownloadButton(Ljava/lang/Object;)V"
        val initializeAutoRepeatDescriptor = "$classAutoRepeatDescriptor->initializeAutoRepeat(Ljava/lang/Object;)V"
        val initializeCopyDescriptor = "$classCopyDescriptor->initializeCopyButton(Ljava/lang/Object;)V"
        val initializeCopyWithTimeStampDescriptor = "$classCopyWithTimeStampDescriptor->initializeCopyButtonWithTimeStamp(Ljava/lang/Object;)V"

        PlayerControlsBytecodePatch.initializeControl(initializeDownloadDescriptor)
        PlayerControlsBytecodePatch.initializeControl(initializeAutoRepeatDescriptor)
        PlayerControlsBytecodePatch.initializeControl(initializeCopyDescriptor)
        PlayerControlsBytecodePatch.initializeControl(initializeCopyWithTimeStampDescriptor)

        /*
         add code to change the visibility of the control
         */

        val changeVisibilityDownloadDescriptor = "$classDownloadDescriptor->changeVisibility(Z)V"
        val changeVisibilityAutoRepeatDescriptor = "$classAutoRepeatDescriptor->changeVisibility(Z)V"
        val changeVisibilityCopyDescriptor = "$classCopyDescriptor->changeVisibility(Z)V"
        val changeVisibilityCopyWithTimeStampDescriptor = "$classCopyWithTimeStampDescriptor->changeVisibility(Z)V"

        PlayerControlsBytecodePatch.injectVisibilityCheckCall(changeVisibilityDownloadDescriptor)
        PlayerControlsBytecodePatch.injectVisibilityCheckCall(changeVisibilityAutoRepeatDescriptor)
        PlayerControlsBytecodePatch.injectVisibilityCheckCall(changeVisibilityCopyDescriptor)
        PlayerControlsBytecodePatch.injectVisibilityCheckCall(changeVisibilityCopyWithTimeStampDescriptor)

        return PatchResultSuccess()
    }
}