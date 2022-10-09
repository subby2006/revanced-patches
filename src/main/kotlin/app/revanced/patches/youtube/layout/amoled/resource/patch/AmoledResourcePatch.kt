package app.revanced.patches.youtube.layout.amoled.resource.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.youtube.layout.amoled.annotations.AmoledCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import org.w3c.dom.Element
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@DependsOn([FixLocaleConfigErrorPatch::class])
@Name("amoled-resource")
@AmoledCompatibility
@Version("0.0.1")
class AmoledResourcePatch : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {
        val classLoader = this.javaClass.classLoader
        data.xmlEditor["res${File.separator}values${File.separator}colors.xml"].use { editor ->
            val resourcesNode = editor.file.getElementsByTagName("resources").item(0) as Element

            for (i in 0 until resourcesNode.childNodes.length) {
                val node = resourcesNode.childNodes.item(i)
                if (node !is Element) continue

                val element = resourcesNode.childNodes.item(i) as Element
                element.textContent = when (element.getAttribute("name")) {
                    "yt_black1", "yt_black1_opacity95", "yt_black1_opacity98", "yt_black2", "yt_black3", "yt_black4", "yt_status_bar_background_dark" -> "@android:color/black"
                    "yt_selected_nav_label_dark" -> "#ffdf0000"
                    else -> continue
                }
            }
        }

        val resourceFileNames = arrayOf(
            "quantum_launchscreen_youtube.xml"
        )

        // the attributes to change the value of
        val replacements = arrayOf(
            "drawable"
        )

        data.forEach {
            if (!it.name.startsWithAny(*resourceFileNames)) return@forEach

            // for each file in the "layouts" directory replace all necessary attributes content
            data.xmlEditor[it.absolutePath].use { editor ->
                editor.file.doRecursively { node ->
                    replacements.forEach replacement@{ replacement ->
                        if (node !is Element) return@replacement

                        node.getAttributeNode("android:$replacement")?.let { attribute ->
                            attribute.textContent = "@drawable/revanced_splash_bg"
                        }
                    }
                }
            }
        }

        val values_night_v31 = "values-night-v31" to arrayOf(
                "styles"
        )

        val splashscreen_bg = arrayOf(values_night_v31)

        splashscreen_bg.forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.xml"

                Files.copy(
                        classLoader.getResourceAsStream("amoled/$relativePath")!!,
                        data["res"].resolve(relativePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }
}
