package app.revanced.patches.youtube.extended.theme.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.youtube.extended.theme.fingerprints.LithoThemeFingerprint
import app.revanced.patches.youtube.extended.theme.resource.patch.ThemeResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.shared.annotation.YouTubeCompatibility

@DependsOn(
    dependencies = [FixLocaleConfigErrorPatch::class]
)
@Name("theme")
@Description("Applies a custom theme to litho components.")
@YouTubeCompatibility
@Version("0.0.1")
class ThemePatch : BytecodePatch(
    listOf(
        LithoThemeFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val result = LithoThemeFingerprint.result!!
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
