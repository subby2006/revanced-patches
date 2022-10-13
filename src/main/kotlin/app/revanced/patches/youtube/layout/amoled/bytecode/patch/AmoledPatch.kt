package app.revanced.patches.youtube.layout.amoled.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.youtube.layout.amoled.annotations.AmoledCompatibility
import app.revanced.patches.youtube.layout.amoled.bytecode.fingerprints.AmoledFingerprint
import app.revanced.patches.youtube.layout.amoled.resource.patch.AmoledResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch

@Patch
@DependsOn(
    dependencies = [FixLocaleConfigErrorPatch::class, AmoledResourcePatch::class]
)
@Name("amoled")
@Description("Enables pure black theme.")
@AmoledCompatibility
@Version("0.0.1")
class AmoledPatch : BytecodePatch(
    listOf(
        AmoledFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val result = AmoledFingerprint.result!!
        val method = result.mutableMethod
        val patchIndex = result.scanResult.patternScanResult!!.endIndex - 1

        method.addInstructions(
            patchIndex, """
                invoke-static {}, Lapp/revanced/integrations/utils/ThemeHelper;->isDarkTheme()Z
                move-result v2
                if-nez v2, :comments_filter_white
                const v1, -0x1
                if-ne v1, p1, :comments_filter_white
                const/4 p1, 0x0
                :comments_filter_white
                if-eqz v2, :comments_filter_dark
                const v1, -0xf0f0f1
                if-ne v1, p1, :comments_filter_new_dark
                const/4 p1, 0x0
                :comments_filter_new_dark
                const v1, -0xdededf
                if-ne v1, p1, :comments_filter_dark
                const/4 p1, 0x0
            """, listOf(ExternalLabel("comments_filter_dark", method.instruction(patchIndex)))
        )
        return PatchResultSuccess()
    }
}