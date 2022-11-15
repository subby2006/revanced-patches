package app.revanced.patches.youtube.misc.translations.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.annotation.YouTubeUniversalCompatibility
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Patch
@Name("translations")
@Description("Add Crowdin Translations")
@YouTubeUniversalCompatibility
@Version("0.0.1")
class TranslationsPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val revanced_translations = "translate" to arrayOf(
                "ar-v21",
                "az-rAZ-v21",
                "be-rBY-v21",
                "bn-v21",
                "bn-rIN-v21",
                "cs-rCZ-v21",
                "de-rDE-v21",
                "el-rGR-v21",
                "es-rES-v21",
                "fr-rFR-v21",
                "he-rIL-v21",
                "hi-rIN-v21",
                "hu-rHU-v21",
                "id-rID-v21",
                "in-v21",
                "it-rIT-v21",
                "ja-rJP-v21",
                "kn-rIN-v21",
                "ko-rKR-v21",
                "lt-rLT-v21",
                "mk-rMK-v21",
                "ml-rIN-v21",
                "mr-rIN-v21",
                "my-rMM-v21",
                "pl-rPL-v21",
                "pt-rBR-v21",
                "ru-rRU-v21",
                "th-rTH-v21",
                "tr-rTR-v21",
                "uk-rUA-v21",
                "ur-rIN-v21",
                "vi-rVN-v21",
                "zh-rCN-v21",
                "zh-rTW-v21"
        )

        val TranslationsResources = arrayOf(revanced_translations)

        val classLoader = this.javaClass.classLoader
        TranslationsResources.forEach { (path, languageNames) ->
            languageNames.forEach { name ->
                val resDirectory = context["res"].resolve("values-$name")
                val relativePath = "values-$name/strings.xml"

                Files.createDirectory(resDirectory.toPath())
                Files.copy(
                        classLoader.getResourceAsStream("$path/$relativePath")!!,
                        context["res"].resolve(relativePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }
}
