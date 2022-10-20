package app.revanced.patches.youtube.layout.materialyou.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.util.resources.ResourceUtils
import app.revanced.util.resources.ResourceUtils.copyResources
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import org.w3c.dom.Element

@Patch(false)
@DependsOn([FixLocaleConfigErrorPatch::class])
@Name("materialyou")
@Description("Enables MaterialYou theme for Android 12+")
@YouTubeCompatibility
@Version("0.0.1")
class MaterialYouPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        val classLoader = this.javaClass.classLoader
		 
		 val sourcePath = classLoader.getResourceAsStream("materialyou/host/values-v31/colors.xml")!!
		 val relativePath = context.xmlEditor["res/values-v31/colors.xml"]

        "resources".copyXmlNode(
            context.xmlEditor[sourcePath],
            relativePath
        ).also {}.close()

        return PatchResultSuccess()
    }
}
