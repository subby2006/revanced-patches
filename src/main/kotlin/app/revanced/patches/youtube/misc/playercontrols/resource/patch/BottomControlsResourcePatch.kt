package app.revanced.patches.youtube.misc.playercontrols.resource.patch

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.ResourceData
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.impl.ResourcePatch
import app.revanced.patches.youtube.misc.playercontrols.annotation.PlayerControlsCompatibility
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch
import app.revanced.util.resources.ResourceUtils.copyXmlNode
import java.nio.file.Files

@Name("bottom-controls-resource-patch")
@PlayerControlsCompatibility
@DependsOn([FixLocaleConfigErrorPatch::class])
@Version("0.0.1")
class BottomControlsResourcePatch : ResourcePatch() {
    override fun execute(data: ResourceData): PatchResult {
        val classLoader = this.javaClass.classLoader

        val hostingResourceStream = classLoader.getResourceAsStream("overlaybuttons/host/layout/youtube_controls_bottom_ui_container.xml")!!
        val targetXmlEditor = data.xmlEditor["res/layout/youtube_controls_layout.xml"]
        "android.support.constraint.ConstraintLayout".copyXmlNode(
                data.xmlEditor[hostingResourceStream],
                targetXmlEditor
        ).also {
            val children = targetXmlEditor.file.getElementsByTagName("android.support.constraint.ConstraintLayout").item(0).childNodes

            // Replace the startOf with the voting button view so that the button does not overlap
            for (i in 1 until children.length) {
                val view = children.item(i)

                // Replace the attribute for a specific node only
                if (!(view.hasAttributes() && view.attributes.getNamedItem("yt:layout_constraintRight_toLeftOf").nodeValue.endsWith("fullscreen_button"))) continue

                val CopyButtonId = "@+id/copy_button"

                view.attributes.getNamedItem("yt:layout_constraintRight_toLeftOf").nodeValue = CopyButtonId

                break
            }
        }.close() // close afterwards

        val container = data["res/layout/youtube_controls_bottom_ui_container.xml"]
        container.writeText(
                container.readText()
                        .replace(
                                "TEMP", "@id/fullscreen_button"
                        )
        )
        return PatchResultSuccess()
    }
}
