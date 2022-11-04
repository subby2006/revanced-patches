package app.revanced.patches.youtube.misc.settings.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.smali.toInstruction
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patches.youtube.misc.settings.bytecode.fingerprints.*
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch
import org.jf.dexlib2.util.MethodUtil

@Patch
@DependsOn(
    [
        IntegrationsPatch::class,
        SettingsResourcePatch::class,
        ResourceMappingResourcePatch::class
    ]
)
@Name("settings")
@Description("Adds settings for ReVanced to YouTube.")
@YouTubeCompatibility
@Version("0.0.1")
class SettingsPatch : BytecodePatch(
    listOf(ThemeSetterAppFingerprint, ThemeSetterAppOldFingerprint, ThemeSetterSystemFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        fun buildInvokeInstructionsString(
            registers: String = "v0",
            classDescriptor: String = "Lapp/revanced/integrations/utils/ThemeHelper;",
            methodName: String = "setTheme",
            parameters: String = "Ljava/lang/Object;"
        ) = "invoke-static {$registers}, $classDescriptor->$methodName($parameters)V"

        // apply the current theme of the settings page
        with(ThemeSetterSystemFingerprint.result!!) {
            with(mutableMethod) {
                val call = buildInvokeInstructionsString()

                addInstruction(
                    scanResult.patternScanResult!!.startIndex,
                    call
                )

                addInstruction(
                    mutableMethod.implementation!!.instructions.size - 1,
                    call
                )
            }
        }

        val ThemeSetterAppFingerprintResult = try {
            ThemeSetterAppFingerprint.result!!
        } catch (e: Exception) {
            ThemeSetterAppOldFingerprint.result!!
        }

        // set the theme based on the preference of the app
        with(ThemeSetterAppFingerprintResult) {
            with(mutableMethod) {
                fun buildInstructionsString(theme: Int) = """
                    const/4 v0, 0x$theme
                    ${buildInvokeInstructionsString(parameters = "I")}
                """

                addInstructions(
                    scanResult.patternScanResult!!.endIndex + 1,
                    buildInstructionsString(1)
                )
                addInstructions(
                    scanResult.patternScanResult!!.endIndex - 7,
                    buildInstructionsString(0)
                )

                addInstructions(
                    scanResult.patternScanResult!!.endIndex - 9,
                    buildInstructionsString(1)
                )
                addInstructions(
                    mutableMethod.implementation!!.instructions.size - 2,
                    buildInstructionsString(0)
                )
            }
        }

        return PatchResultSuccess()
    }

    internal companion object {
        val appearanceStringId = ResourceMappingResourcePatch.resourceMappings.find {
            it.type == "string" && it.name == "app_theme_appearance_dark"
        }!!.id
    }
}