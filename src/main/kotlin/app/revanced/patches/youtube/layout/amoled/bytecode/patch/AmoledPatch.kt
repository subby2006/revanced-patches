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
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.layout.amoled.bytecode.fingerprints.AmoledFingerprint
import app.revanced.patches.youtube.layout.amoled.resource.patch.AmoledResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch

@Patch
@DependsOn(
    dependencies = [FixLocaleConfigErrorPatch::class, AmoledResourcePatch::class]
)
@Name("amoled")
@Description("Enables pure black theme.")
@YouTubeCompatibility
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
                invoke-static {p1}, Lapp/revanced/integrations/patches/LithoThemePatch;->applyLithoTheme(I)I
                move-result p1
            """
        )
        return PatchResultSuccess()
    }
}