package app.revanced.patches.music.layout.branding.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.*
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.music.layout.branding.annotations.CustomBrandingMusicCompatibility
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.w3c.dom.Element

@Patch(false)
@DependsOn([ResourceMappingResourcePatch::class])
@Name("custom-branding-music-decipher3114")
@Description("Changes the YouTube Music launcher icon to your choice (decipher3114).")
@CustomBrandingMusicCompatibility
@Version("0.0.1")
class CustomBrandingMusicPatch_decipher3114 : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        val classLoader = this.javaClass.classLoader
        val resDirectory = context["res"]
        if (!resDirectory.isDirectory) return PatchResultError("The res folder can not be found.")

        // App Icon
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
                    classLoader.getResourceAsStream("branding-music/decipher3114/launchericon/$size/$iconName.png")!!,
                    resDirectory.resolve("mipmap-$iconDirectory").resolve("$iconName.png").toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        // MonoChrome Icon
        arrayOf("drawable" to arrayOf("ic_app_icons_themed_youtube_music")).forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.xml"

                Files.copy(
                    classLoader.getResourceAsStream("branding-music/decipher3114/monochromeicon/$relativePath")!!,
                    context["res"].resolve(relativePath).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }

}
