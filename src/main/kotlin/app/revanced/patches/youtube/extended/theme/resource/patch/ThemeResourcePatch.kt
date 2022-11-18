package app.revanced.patches.youtube.extended.theme.resource.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.shared.annotation.YouTubeUniversalCompatibility
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.w3c.dom.Element

@DependsOn([FixLocaleConfigErrorPatch::class])
@Name("theme-resource")
@YouTubeUniversalCompatibility
@Version("0.0.1")
class ThemeResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val resourceFileNames = arrayOf(
            "quantum_launchscreen_youtube.xml"
        )

        // the attributes to change the value of
        val replacements = arrayOf(
            "drawable"
        )

        context.forEach {
            if (!it.name.startsWithAny(*resourceFileNames)) return@forEach

            // for each file in the "layouts" directory replace all necessary attributes content
            context.xmlEditor[it.absolutePath].use { editor ->
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

        // copies the resource file to change the splash screen color
        context.copyResources("youtube/theme",
            ResourceUtils.ResourceGroup("values-night-v31", "styles.xml")
        )

        return PatchResultSuccess()
    }
}
