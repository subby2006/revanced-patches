package app.revanced.patches.youtube.extended.header.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.shared.annotation.YouTubeUniversalCompatibility
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.exists

@Patch(false)
@DependsOn([FixLocaleConfigErrorPatch::class])
@Name("premium-heading")
@Description("Shows premium branding on the home screen.")
@YouTubeUniversalCompatibility
@Version("0.0.1")
class PremiumHeadingPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        val resDirectory = context["res"]
        if (!resDirectory.isDirectory) return PatchResultError("The res folder can not be found.")

        val (original, replacement) = "yt_premium_wordmark_header" to "yt_wordmark_header"
        val modes = arrayOf("light", "dark")

        arrayOf("xxxhdpi", "xxhdpi", "xhdpi", "hdpi", "mdpi").forEach { size ->
            val headingDirectory = resDirectory.resolve("drawable-$size")
            modes.forEach { mode ->
                val fromPath = headingDirectory.resolve("${original}_$mode.png").toPath()
                val toPath = headingDirectory.resolve("${replacement}_$mode.png").toPath()

                if (!fromPath.exists())
                    return PatchResultError("The file $fromPath does not exist in the resources. Therefore, this patch can not succeed.")
                Files.copy(
                    fromPath,
                    toPath,
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        val revancedprefs = context["res/xml/revanced_prefs.xml"]
        revancedprefs.writeText(
            revancedprefs.readText()
                .replace(
                    "<SwitchPreference android:title=\"@string/revanced_override_premium_header_title\" android:key=\"revanced_override_premium_header\" android:defaultValue=\"false\" android:summaryOn=\"@string/revanced_override_premium_header_summary_on\" android:summaryOff=\"@string/revanced_override_premium_header_summary_off\" />",
                    ""
                )
        )

        return PatchResultSuccess()
    }
}
