package app.revanced.patches.youtube.extended.parseuriredirect.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.extended.parseuriredirect.fingerprints.ParseUriRedirectFirstFingerprint
import app.revanced.patches.youtube.extended.parseuriredirect.fingerprints.ParseUriRedirectSecondFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.iface.instruction.formats.Instruction35c
import org.jf.dexlib2.iface.instruction.formats.Instruction11x

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

        val ParseUriRedirectFirstFingerprintResult = ParseUriRedirectFirstFingerprint.result!!
        val ParseUriFirstPatternScanStartIndex = ParseUriRedirectFirstFingerprintResult.scanResult.patternScanResult!!.startIndex
        val FirsttargetRegister =
            (ParseUriRedirectFirstFingerprintResult.method.implementation!!.instructions.elementAt(ParseUriFirstPatternScanStartIndex + 1) as Instruction35c).registerC

        ParseUriRedirectFirstFingerprintResult.mutableMethod.addInstructions(
            ParseUriFirstPatternScanStartIndex + 1, """
                    invoke-static {v$FirsttargetRegister}, Lapp/revanced/integrations/patches/UriRedirectPatch;->parseRedirectUri(Ljava/lang/String;)Ljava/lang/String;
                    move-result-object v$FirsttargetRegister
            """
        )

        val ParseUriRedirectSecondFingerprintResult = ParseUriRedirectSecondFingerprint.result!!
        val ParseUriSecondPatternScanStartIndex = ParseUriRedirectSecondFingerprintResult.scanResult.patternScanResult!!.startIndex
        val SecondtargetRegister =
            (ParseUriRedirectSecondFingerprintResult.method.implementation!!.instructions.elementAt(ParseUriSecondPatternScanStartIndex + 1) as Instruction11x).registerA

        ParseUriRedirectSecondFingerprintResult.mutableMethod.addInstructions(
            ParseUriSecondPatternScanStartIndex + 2, """
                    invoke-static {v$SecondtargetRegister}, Lapp/revanced/integrations/patches/UriRedirectPatch;->parseRedirectUri(Ljava/lang/String;)Ljava/lang/String;
                    move-result-object v$SecondtargetRegister
            """
        )

        return PatchResultSuccess()
    }
}