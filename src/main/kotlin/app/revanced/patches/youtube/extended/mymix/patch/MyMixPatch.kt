package app.revanced.patches.youtube.extended.mymix.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patches.youtube.extended.mymix.annotations.MyMixCompatibility
import app.revanced.patches.youtube.extended.mymix.fingerprints.MyMixFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-my-mix")
@Description("Remove My Mix from home feed and video player.")
@MyMixCompatibility
@Version("0.0.1")
class MyMixPatch : BytecodePatch(
    listOf(
        MyMixFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        val result = MyMixFingerprint.result!!
        val method = result.mutableMethod
        val index = result.scanResult.patternScanResult!!.endIndex - 6
        val register = (method.implementation!!.instructions[index] as OneRegisterInstruction).registerA

        method.addInstruction(
            index + 2,
            "invoke-static {v$register}, Lapp/revanced/integrations/patches/HideMyMixPatch;->HideMyMix(Landroid/view/View;)V"
        )

        return PatchResultSuccess()
    }
}