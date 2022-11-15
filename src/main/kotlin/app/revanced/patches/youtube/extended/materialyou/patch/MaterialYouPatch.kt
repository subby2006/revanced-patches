package app.revanced.patches.youtube.extended.materialyou.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.annotation.YouTubeUniversalCompatibility
import app.revanced.patches.youtube.extended.theme.bytecode.patch.ThemePatch
import app.revanced.patches.youtube.extended.theme.resource.patch.ThemeResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.w3c.dom.Element

@Patch(false)
@DependsOn([FixLocaleConfigErrorPatch::class, ThemePatch::class, ThemeResourcePatch::class])
@Name("materialyou")
@Description("Enables MaterialYou theme for Android 12+")
@YouTubeUniversalCompatibility
@Version("0.0.1")
class MaterialYouPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val drawables1 = "drawable-night-v31" to arrayOf(
            "new_content_dot_background.xml"
        )

        val drawables2 = "drawable-v31" to arrayOf(
            "new_content_count_background.xml",
            "new_content_dot_background.xml"
        )

        val layout1 = "layout-v31" to arrayOf(
            "new_content_count.xml"
        )

        val MonetResources = arrayOf(drawables1, drawables2, layout1)

        MonetResources.forEach { (path, resourceNames) ->
            Files.createDirectory(context["res"].resolve("$path").toPath())
            resourceNames.forEach { name ->
                val monetPath = "$path/$name"

                Files.copy(
                    this.javaClass.classLoader.getResourceAsStream("youtube/materialyou/$monetPath")!!,
                    context["res"].resolve(monetPath).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

         val sourcePath = this.javaClass.classLoader.getResourceAsStream("youtube/materialyou/host/values-v31/colors.xml")!!
         val relativePath = context.xmlEditor["res/values-v31/colors.xml"]

        "resources".copyXmlNode(
            context.xmlEditor[sourcePath],
            relativePath
        ).also {}.close()

        return PatchResultSuccess()
    }
}
