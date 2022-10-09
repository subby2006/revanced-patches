package app.revanced.patches.youtube.extended.overlaybuttons.resource.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.extended.overlaybuttons.annotation.OverlayButtonsCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Name("overlay-buttons-resource-patch")
@DependsOn([FixLocaleConfigErrorPatch::class])
@Description("Makes necessary changes to resources for the overlay buttons.")
@OverlayButtonsCompatibility
@Version("0.0.1")
class OverlayButtonsResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        val classLoader = this.javaClass.classLoader

        /*
         * Copy resources
         */

        val drawables = "drawable" to arrayOf(
                "playlist_repeat_button",
                "playlist_shuffle_button",
                "revanced_yt_repeat_icon"
        )

        val xmlResources = arrayOf(drawables)

        xmlResources.forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.xml"

                Files.copy(
                        classLoader.getResourceAsStream("overlaybuttons/$relativePath")!!,
                        context["res"].resolve(relativePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        /*
         * Copy preference fragments
         */

        context.copyXmlNode("overlaybuttons/host", "layout/youtube_controls_bottom_ui_container.xml", "android.support.constraint.ConstraintLayout")

        val container = context["res/layout/youtube_controls_bottom_ui_container.xml"]
        container.writeText(
            container.readText()
			.replace(
                "yt:layout_constraintRight_toLeftOf=\"@id/fullscreen_button",
                "yt:layout_constraintRight_toLeftOf=\"@+id/copy_button"
            )
        )

        return PatchResultSuccess()
    }
}