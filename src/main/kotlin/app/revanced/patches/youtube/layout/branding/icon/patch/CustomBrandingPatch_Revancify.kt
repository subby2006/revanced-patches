package app.revanced.patches.youtube.layout.branding.icon.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.*
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.w3c.dom.Element

@Patch(false)
@Name("custom-branding-icon-revancify")
@Description("Changes the YouTube launcher icon to your choice (revancify).")
@YouTubeCompatibility
@Version("0.0.1")
class CustomBrandingPatch_Revancify : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        val classLoader = this.javaClass.classLoader
        val resDirectory = context["res"]
        if (!resDirectory.isDirectory) return PatchResultError("The res folder can not be found.")

        // App Icon
        val AppiconNames = arrayOf(
            "adaptiveproduct_youtube_background_color_108",
            "adaptiveproduct_youtube_foreground_color_108",
            "ic_launcher",
            "ic_launcher_round"
        )

        // Splash Icon
        val SplashiconNames = arrayOf(
            "product_logo_youtube_color_24",
            "product_logo_youtube_color_36",
            "product_logo_youtube_color_144",
            "product_logo_youtube_color_192"
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
                    classLoader.getResourceAsStream("branding/revancify/launchericon/$size/$iconName.png")!!,
                    resDirectory.resolve("mipmap-$iconDirectory").resolve("$iconName.png").toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
            SplashiconNames.forEach iconLoop@{ iconName ->
                Files.copy(
                    classLoader.getResourceAsStream("branding/revancify/splashicon/$size/$iconName.png")!!,
                    resDirectory.resolve("drawable-$iconDirectory").resolve("$iconName.png").toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        // Splash Icon
        context["res/values-v31/styles.xml"].writeText(
                context["res/values-v31/styles.xml"].readText()
                        .replace(
                                "<item name=\"android:windowSplashScreenAnimatedIcon\">@drawable/avd_anim</item>",
                                ""
                        )
        )
        context["res/values-night-v31/styles.xml"].writeText(
                context["res/values-night-v31/styles.xml"].readText()
                        .replace(
                                "<item name=\"android:windowSplashScreenAnimatedIcon\">@drawable/avd_anim</item>",
                                ""
                        )
        )

        // MonoChrome Icon
        arrayOf("drawable" to arrayOf("adaptive_monochrome_ic_youtube_launcher")).forEach { (path, resourceNames) ->
            resourceNames.forEach { name ->
                val relativePath = "$path/$name.xml"

                Files.copy(
                    classLoader.getResourceAsStream("branding/revancify/monochromeicon/$relativePath")!!,
                    context["res"].resolve(relativePath).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return PatchResultSuccess()
    }

}
