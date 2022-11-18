package app.revanced.patches.music.misc.optimizeresource.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.shared.annotation.YouTubeMusicCompatibility
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Patch
@Name("optimize-resource-music")
@Description("Remove unnecessary resources.")
@YouTubeMusicCompatibility
@Version("0.0.1")
class OptimizeResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {

        val relativePath = "raw/third_party_licenses"

        Files.copy(
            this.javaClass.classLoader.getResourceAsStream("youtube/resource/$relativePath")!!,
            context["res"].resolve(relativePath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
        
        return PatchResultSuccess()
    }
}
