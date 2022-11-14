package app.revanced.patches.youtube.layout.castbutton.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.annotation.YouTubeUniversalCompatibility
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-cast-button")
@Description("Hides the cast button in the video player.")
@YouTubeUniversalCompatibility
@Version("0.0.1")
class HideCastButtonPatch : BytecodePatch() {
    override fun execute(context: BytecodeContext): PatchResult {
        context.classes.forEach { classDef ->
            classDef.methods.forEach { method ->
                if (classDef.type.endsWith("MediaRouteButton;") && method.name == "setVisibility") {
                    val setVisibilityMethod =
                        context.proxy(classDef).mutableClass.methods.first { it.name == "setVisibility" }

                    setVisibilityMethod.addInstructions(
                        0, """
                            invoke-static {p1}, Lapp/revanced/integrations/patches/HideCastButtonPatch;->getCastButtonOverrideV2(I)I
                            move-result p1
                        """
                    )
                }
            }
        }

        return PatchResultSuccess()
    }
}
