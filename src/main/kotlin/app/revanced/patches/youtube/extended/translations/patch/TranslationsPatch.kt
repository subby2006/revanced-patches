package app.revanced.patches.youtube.extended.translations.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.youtube.extended.translations.annotations.TranslationsCompatibility
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Patch
@Name("translations")
@Description("Add Crowdin Translations")
@TranslationsCompatibility
@Version("0.0.1")
class TranslationsPatch : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {

        val revanced_translations = "translate" to arrayOf(
                "ar-v21",
                "as-rIN-v21",
                "az-rAZ-v21",
                "bn-rBD-v21",
                "bn-rIN-v21",
                "de-rDE-v21",
                "el-rGR-v21",
                "es-rES-v21",
                "fr-rFR-v21",
                "hi-rIN-v21",
                "hu-rHU-v21",
                "id-rID-v21",
                "in-v21",
                "it-rIT-v21",
                "ja-rJP-v21",
                "kn-rIN-v21",
                "ko-rKR-v21",
                "ml-rIN-v21",
                "my-rMM-v21",
                "pl-rIN-v21",
                "pl-rPL-v21",
                "pt-rBR-v21",
                "ru-rRU-v21",
                "tr-rTR-v21",
                "uk-rUA-v21",
                "vi-rVN-v21",
                "zh-rCN-v21",
                "zh-rTW-v21"
        )

        val TranslationsResources = arrayOf(revanced_translations)

        val classLoader = this.javaClass.classLoader
        TranslationsResources.forEach { (path, languageNames) ->
            languageNames.forEach { name ->
                val resDirectory = data["res"].resolve("values-$name")
                val relativePath = "values-$name/strings.xml"

                Files.createDirectory(resDirectory.toPath())
                Files.copy(
                        classLoader.getResourceAsStream("$path/$relativePath")!!,
                        data["res"].resolve(relativePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                )		
            }
        }

        return PatchResultSuccess()
    }
}
