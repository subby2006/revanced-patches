package app.revanced.patches.music.layout.branding.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.*
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.music.layout.branding.annotations.CustomBrandingMusicCompatibility
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.w3c.dom.Element

@Patch
@DependsOn([ResourceMappingResourcePatch::class])
@Name("custom-branding-music")
@Description("Changes the YouTube Music launcher icon and name to your choice (defaults to ReVanced Red).")
@CustomBrandingMusicCompatibility
@Version("0.0.1")
class CustomBrandingMusicPatch : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {
        val classLoader = this.javaClass.classLoader
        val resDirectory = data["res"]
        if (!resDirectory.isDirectory) return PatchResultError("The res folder can not be found.")

        // Icon branding
        val AppiconNames = arrayOf(
            "adaptiveproduct_youtube_music_background_color_108",
            "adaptiveproduct_youtube_music_foreground_color_108",
            "ic_launcher_release"
        )

        mapOf(
            "xxxhdpi" to 192,
            "xxhdpi" to 144,
            "xhdpi" to 96,
            "hdpi" to 72,
            "mdpi" to 48
        ).forEach { (iconDirectory, size) ->
            AppiconNames.forEach iconLoop@{ iconName ->
                Files.copy(
                    classLoader.getResourceAsStream("branding-music/icon/$size/$iconName.png")!!,
                    resDirectory.resolve("mipmap-$iconDirectory").resolve("$iconName.png").toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        val drawables1 = "drawable-hdpi" to arrayOf(
            "action_bar_logo",
            "action_bar_logo_release",
            "record"
        )

        val drawables2 = "drawable-large-hdpi" to arrayOf(
            "record"
        )

        val drawables3 = "drawable-large-mdpi" to arrayOf(
            "record"
        )

        val drawables4 = "drawable-large-xhdpi" to arrayOf(
            "record"
        )

        val drawables5 = "drawable-mdpi" to arrayOf(
            "action_bar_logo",
            "record"
        )

        val drawables6 = "drawable-xhdpi" to arrayOf(
            "action_bar_logo",
            "record"
        )

        val drawables7 = "drawable-xlarge-hdpi" to arrayOf(
            "record"
        )

        val drawables8 = "drawable-xlarge-mdpi" to arrayOf(
            "record"
        )

        val drawables9 = "drawable-xxhdpi" to arrayOf(
            "action_bar_logo",
            "record"
        )

        val drawables10 = "drawable-xxxhdpi" to arrayOf(
            "action_bar_logo"
        )

        val pngResources = arrayOf(drawables1, drawables2, drawables3, drawables4, drawables5, drawables6, drawables7, drawables8, drawables9, drawables10)

        pngResources.forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.png"

                Files.copy(
                    classLoader.getResourceAsStream("branding-music/resource/$relativePath")!!,
                    data["res"].resolve(relativePath).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }

}
