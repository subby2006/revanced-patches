package app.revanced.patches.youtube.layout.branding.icon.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.*
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.youtube.layout.branding.icon.annotations.CustomBrandingCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.w3c.dom.Element

@Patch(false)
@Name("custom-branding-decipher3114")
@Description("Changes the YouTube launcher icon and name to your choice (decipher3114).")
@CustomBrandingCompatibility
@Version("0.0.1")
class CustomBrandingPatch_decipher3114 : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {
        val classLoader = this.javaClass.classLoader
        val resDirectory = data["res"]
        if (!resDirectory.isDirectory) return PatchResultError("The res folder can not be found.")

        // Icon branding
        val iconNames = arrayOf(
            "adaptiveproduct_youtube_background_color_108",
            "adaptiveproduct_youtube_foreground_color_108",
            "ic_launcher",
            "ic_launcher_round"
        )

        mapOf(
            "xxxhdpi" to 192,
            "xxhdpi" to 144,
            "xhdpi" to 96,
            "hdpi" to 72,
            "mdpi" to 48
        ).forEach { (iconDirectory, size) ->
            iconNames.forEach iconLoop@{ iconName ->
                val iconFile = this.javaClass.classLoader.getResourceAsStream("branding/decipher3114/$size/$iconName.png")
                    ?: return PatchResultError("The icon $iconName can not be found.")

                Files.write(
                    resDirectory.resolve("mipmap-$iconDirectory").resolve("$iconName.png").toPath(), iconFile.readAllBytes()
                )
            }
        }

        val drawables = "drawable" to arrayOf(
            "adaptive_monochrome_ic_youtube_launcher"
        )

        val xmlResources = arrayOf(drawables)

        xmlResources.forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.xml"

                Files.copy(
                    classLoader.getResourceAsStream("branding/monochrome/$relativePath")!!,
                    data["res"].resolve(relativePath).toPath(),
					StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        // Name branding
        val resourceFileNames = arrayOf(
            "strings.xml"
        )

        data.forEach {
            if (!it.name.startsWithAny(*resourceFileNames)) return@forEach

            // for each file in the "layouts" directory replace all necessary attributes content
            data.xmlEditor[it.absolutePath].use { editor ->
            val resourcesNode = editor.file.getElementsByTagName("resources").item(0) as Element

                for (i in 0 until resourcesNode.childNodes.length) {
                    val node = resourcesNode.childNodes.item(i)
                    if (node !is Element) continue

                    val element = resourcesNode.childNodes.item(i) as Element
                    element.textContent = when (element.getAttribute("name")) {
                        "application_name" -> "YouTube ReVanced"
                        else -> continue
                    }
                }
            }
        }

        return PatchResultSuccess()
    }

}
