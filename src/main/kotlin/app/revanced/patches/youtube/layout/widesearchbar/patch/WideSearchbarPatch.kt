package app.revanced.patches.youtube.layout.widesearchbar.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.data.toMethodWalker
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patches.youtube.layout.widesearchbar.fingerprints.WideSearchbarOneFingerprint
import app.revanced.patches.youtube.layout.widesearchbar.fingerprints.WideSearchbarOneParentFingerprint
import app.revanced.patches.youtube.layout.widesearchbar.fingerprints.WideSearchbarTwoFingerprint
import app.revanced.patches.youtube.layout.widesearchbar.fingerprints.WideSearchbarTwoParentFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.shared.annotation.YouTubeCompatibility

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("enable-wide-searchbar")
@Description("Replaces the search icon with a wide search bar. This will hide the YouTube logo when active.")
@YouTubeCompatibility
@Version("0.0.1")
class WideSearchbarPatch : BytecodePatch(
    listOf(
        WideSearchbarOneParentFingerprint, WideSearchbarTwoParentFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        WideSearchbarOneFingerprint.resolve(context, WideSearchbarOneParentFingerprint.result!!.classDef)
        WideSearchbarTwoFingerprint.resolve(context, WideSearchbarTwoParentFingerprint.result!!.classDef)

        val resultOne = WideSearchbarOneFingerprint.result

        //This should be the method aF in class fbn
        val targetMethodOne =
            context.toMethodWalker(resultOne!!.method).nextMethod(resultOne.scanResult.patternScanResult!!.endIndex, true).getMethod() as MutableMethod

        //Since both methods have the same smali code, inject instructions using a method.
        addInstructions(targetMethodOne)

        val resultTwo = WideSearchbarTwoFingerprint.result

        //This should be the method aB in class fbn
        val targetMethodTwo =
            context.toMethodWalker(resultTwo!!.method).nextMethod(resultTwo.scanResult.patternScanResult!!.startIndex, true).getMethod() as MutableMethod

        //Since both methods have the same smali code, inject instructions using a method.
        addInstructions(targetMethodTwo)

        return PatchResultSuccess()
    }

    private fun addInstructions(method: MutableMethod) {
        val index = method.implementation!!.instructions.size - 1
        method.addInstructions(
            index, """
            invoke-static {}, Lapp/revanced/integrations/patches/NewActionbarPatch;->getNewActionBar()Z
            move-result p0
        """
        )
    }

    //targetMethodOne: in class fbn
    /*
    .method public static aF(Ltxm;)Z
        invoke-virtual {p0}, Ltxm;->b()Lahah;
        move-result-object p0
        iget-object p0, p0, Lahah;->e:Lakfd;
        if-nez p0, :cond_a
        sget-object p0, Lakfd;->a:Lakfd;
        :cond_a
        iget-boolean p0, p0, Lakfd;->V:Z
        //added code here:
            invoke-static {}, app/revanced/integrations/patches/NewActionbarPatch;->getNewActionBar()Z
            move-result p0
        //original code here:
        return p0
    .end method
   */

    //targetMethodTwo: in class fbn
    /*
    .method public static aB(Ltxm;)Z
        invoke-virtual {p0}, Ltxm;->b()Lahah;
        move-result-object p0
        iget-object p0, p0, Lahah;->e:Lakfd;
        if-nez p0, :cond_a
        sget-object p0, Lakfd;->a:Lakfd;
        :cond_a
        iget-boolean p0, p0, Lakfd;->y:Z
        //added code here:
            invoke-static {}, app/revanced/integrations/patches/NewActionbarPatch;->getNewActionBar()Z
            move-result p0
        //original code here:
        return p0
    .end method

    */
}
