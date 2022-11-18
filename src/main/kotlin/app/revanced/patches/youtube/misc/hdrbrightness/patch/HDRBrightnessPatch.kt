package app.revanced.patches.youtube.misc.hdrbrightness.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.misc.hdrbrightness.fingerprints.HDRBrightnessFingerprint
import app.revanced.patches.youtube.misc.hdrbrightness.fingerprints.HDRBrightnessOldFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction
import org.jf.dexlib2.iface.reference.FieldReference

@Patch
@Name("hdr-auto-brightness")
@Description("Makes the brightness of HDR videos follow the system default.")
@YouTubeCompatibility
@Version("0.0.2")
@DependsOn([IntegrationsPatch::class])
class HDRBrightnessPatch : BytecodePatch(
    listOf(HDRBrightnessFingerprint, HDRBrightnessOldFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val result = try {
            HDRBrightnessFingerprint.result!!
        } catch (e: Exception) {
            HDRBrightnessOldFingerprint.result!!
        }

        val method = result.mutableMethod

        method.implementation!!.instructions.filter { instruction ->
            val fieldReference = (instruction as? ReferenceInstruction)?.reference as? FieldReference
            fieldReference?.let { it.name == "screenBrightness" } == true
        }.forEach { instruction ->
            val brightnessRegisterIndex = method.implementation!!.instructions.indexOf(instruction)
            val register = (instruction as TwoRegisterInstruction).registerA

            val insertIndex = brightnessRegisterIndex + 1
            method.addInstructions(
                insertIndex,
                """
                   invoke-static {v$register}, Lapp/revanced/integrations/patches/HDRAutoBrightnessPatch;->getHDRBrightness(F)F
                   move-result v$register
                """
            )
        }

        return PatchResultSuccess()
    }
}
