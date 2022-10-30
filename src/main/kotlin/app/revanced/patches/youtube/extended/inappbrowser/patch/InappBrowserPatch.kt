package app.revanced.patches.youtube.extended.inappbrowser.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.patches.youtube.extended.inappbrowser.fingerprints.*
import org.jf.dexlib2.iface.instruction.formats.Instruction21c

@Patch
@Name("inapp-browser")
@Description("Use an external browser to open the url.")
@YouTubeCompatibility
@Version("0.0.1")
class InappBrowserPatch : BytecodePatch(
    listOf(
        InappBrowserFirstFingerprint,
        InappBrowserSecondFingerprint,
        InappBrowserThirdFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        val InappBrowserFirstResult = InappBrowserFirstFingerprint.result!!
        val InappBrowserFirstEndIndex = InappBrowserFirstResult.scanResult.patternScanResult!!.endIndex
        val FirsttargetRegister =
            (InappBrowserFirstResult.method.implementation!!.instructions.elementAt(InappBrowserFirstEndIndex) as Instruction21c).registerA

        InappBrowserFirstResult.mutableMethod.addInstructions(
            InappBrowserFirstEndIndex + 1, """
            invoke-static {v$FirsttargetRegister}, Lapp/revanced/integrations/patches/InappBrowserPatch;->getInappBrowser(Ljava/lang/String;)Ljava/lang/String;
            move-result-object v$FirsttargetRegister
        """
        )

        val InappBrowserSecondResult = InappBrowserSecondFingerprint.result!!
        val InappBrowserSecondEndIndex = InappBrowserSecondResult.scanResult.patternScanResult!!.endIndex
        val SecondtargetRegister =
            (InappBrowserSecondResult.method.implementation!!.instructions.elementAt(InappBrowserSecondEndIndex) as Instruction21c).registerA

        InappBrowserSecondResult.mutableMethod.addInstructions(
            InappBrowserSecondEndIndex + 1, """
            invoke-static {v$SecondtargetRegister}, Lapp/revanced/integrations/patches/InappBrowserPatch;->getInappBrowser(Ljava/lang/String;)Ljava/lang/String;
            move-result-object v$SecondtargetRegister
        """
        )

        val InappBrowserThirdResult = InappBrowserThirdFingerprint.result!!
        val InappBrowserThirdEndIndex = InappBrowserThirdResult.scanResult.patternScanResult!!.endIndex
        val ThirdtargetRegister =
            (InappBrowserThirdResult.method.implementation!!.instructions.elementAt(InappBrowserThirdEndIndex) as Instruction21c).registerA

        InappBrowserThirdResult.mutableMethod.addInstructions(
            InappBrowserThirdEndIndex + 1, """
            invoke-static {v$ThirdtargetRegister}, Lapp/revanced/integrations/patches/InappBrowserPatch;->getInappBrowser(Ljava/lang/String;)Ljava/lang/String;
            move-result-object v$ThirdtargetRegister
        """
        )

        return PatchResultSuccess()
    }
}