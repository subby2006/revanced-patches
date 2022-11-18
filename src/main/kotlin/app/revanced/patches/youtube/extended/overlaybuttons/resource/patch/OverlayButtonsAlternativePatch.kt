package app.revanced.patches.youtube.extended.overlaybuttons.resource.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.extended.overlaybuttons.resource.patch.OverlayButtonsResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.shared.annotation.YouTubeCompatibility
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Patch
@Name("overlay-buttons-alternative-icon")
@DependsOn([FixLocaleConfigErrorPatch::class, OverlayButtonsResourcePatch::class])
@Description("Use alternative Icons for the overlay buttons.")
@YouTubeCompatibility
@Version("0.0.1")
class OverlayButtonsAlternativePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val copy = "drawable-xxhdpi" to arrayOf(
                "ic_fullscreen_vertical_button.png",
                "quantum_ic_fullscreen_exit_grey600_24.png",
                "quantum_ic_fullscreen_exit_white_24.png",
                "quantum_ic_fullscreen_grey600_24.png",
                "quantum_ic_fullscreen_white_24.png",
                "revanced_yt_copy_icon.png",
                "revanced_yt_copy_icon_with_time.png",
                "revanced_yt_download_icon.png",
                "yt_outline_arrow_repeat_1_white_24.png",
                "yt_outline_arrow_shuffle_1_white_24.png",
                "yt_outline_screen_full_exit_white_24.png",
                "yt_outline_screen_full_white_24.png"
        )

        val copyResources = arrayOf(copy)

        copyResources.forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name"

                Files.copy(
                        this.javaClass.classLoader.getResourceAsStream("youtube/overlaybuttons-alternative/$relativePath")!!,
                        context["res"].resolve(relativePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }
}