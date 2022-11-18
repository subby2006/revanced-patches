package app.revanced.patches.youtube.extended.parseuriredirect.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.extended.parseuriredirect.fingerprints.ParseUriRedirectFirstFingerprint
import app.revanced.patches.youtube.extended.parseuriredirect.fingerprints.ParseUriRedirectSecondFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.iface.instruction.formats.Instruction11x
import org.jf.dexlib2.iface.instruction.formats.Instruction35c
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("parse-uri-redirect")
@Description("Follow direct links, bypassing youtube.com/redirect.")
@YouTubeCompatibility
@Version("0.0.1")
class ParseUriRedirectPatch : BytecodePatch(
    listOf(
        ParseUriRedirectFirstFingerprint, ParseUriRedirectSecondFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        ParseUriRedirectFirstFingerprint.hookUriParser(true)
        ParseUriRedirectSecondFingerprint.hookUriParser(false)

        return PatchResultSuccess()
    }
}

fun MethodFingerprint.hookUriParser(isPrimaryFingerprint: Boolean) {
    fun getTargetRegister(instruction: Instruction): Int {
        if (isPrimaryFingerprint) return (instruction as Instruction35c).registerC
        return (instruction as Instruction11x).registerA
    }
    with(this.result!!) {
        val startIndex = scanResult.patternScanResult!!.startIndex
        val instruction = method.implementation!!.instructions.elementAt(startIndex + 1)
        val insertIndex = if (isPrimaryFingerprint) 1 else 2
        val targetRegister = getTargetRegister(instruction)

        mutableMethod.addInstructions(
            startIndex + insertIndex, """
               invoke-static {v$targetRegister}, Lapp/revanced/integrations/patches/UriRedirectPatch;->parseRedirectUri(Ljava/lang/String;)Ljava/lang/String;
               move-result-object v$targetRegister
            """
        )
    }
}