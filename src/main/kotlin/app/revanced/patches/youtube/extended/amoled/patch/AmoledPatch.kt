package app.revanced.patches.youtube.extended.amoled.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
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
import org.w3c.dom.Element

@Patch
@DependsOn([FixLocaleConfigErrorPatch::class, ThemePatch::class, ThemeResourcePatch::class])
@Name("amoled")
@Description("Enables pure black theme.")
@YouTubeUniversalCompatibility
@Version("0.0.1")
class AmoledPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        context.xmlEditor["res/values/colors.xml"].use { editor ->
            val resourcesNode = editor.file.getElementsByTagName("resources").item(0) as Element

            for (i in 0 until resourcesNode.childNodes.length) {
                val node = resourcesNode.childNodes.item(i) as? Element ?: continue

                node.textContent = when (node.getAttribute("name")) {
                    "yt_black0", "yt_black1", "yt_black1_opacity95", "yt_black1_opacity98", "yt_black2", "yt_black3",
                    "yt_black4", "yt_status_bar_background_dark", "material_grey_100", "material_grey_50",
                    "material_grey_600", "material_grey_800", "material_grey_850", "material_grey_900",
                    "material_grey_white_1000", "sud_glif_v3_dialog_background_color_dark" -> "@android:color/black"

                    else -> continue
                }
            }
        }

        return PatchResultSuccess()
    }
}
