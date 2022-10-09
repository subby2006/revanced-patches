package app.revanced.patches.youtube.misc.enabledebugging.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patches.youtube.misc.enabledebugging.annotations.EnableDebuggingCompatibility
import org.w3c.dom.Element

@Name("enable-debugging")
@Description("Enables app debugging by patching the manifest file.")
@EnableDebuggingCompatibility
@Version("0.0.1")
class EnableDebuggingPatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        // create an xml editor instance
        context.xmlEditor["AndroidManifest.xml"].use { dom ->
            // get the application node
            val applicationNode = dom
                .file
                .getElementsByTagName("application")
                .item(0) as Element

            // set application as debuggable
            applicationNode.setAttribute("android:debuggable", "true")
        }

        return PatchResultSuccess()
    }
}
