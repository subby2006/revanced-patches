package app.revanced.patches.youtube.layout.amoled.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.BytecodeData
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.BytecodePatch
import app.revanced.patches.youtube.layout.amoled.annotations.AmoledCompatibility
import app.revanced.patches.youtube.layout.amoled.bytecode.fingerprints.AmoledFingerprint
import app.revanced.patches.youtube.layout.amoled.resource.patch.AmoledResourcePatch
import app.revanced.patches.youtube.misc.manifest.patch.FixLocaleConfigErrorPatch

@Patch
@DependsOn(
    dependencies = [FixLocaleConfigErrorPatch::class, AmoledResourcePatch::class]
)
@Name("amoled")
@Description("Enables pure black theme.")
@AmoledCompatibility
@Version("0.0.1")
class AmoledPatch : BytecodePatch(
    listOf(
        AmoledFingerprint
    )
) {
    override fun execute(data: BytecodeData): PatchResult {

        val result = AmoledFingerprint.result!!

        result.mutableMethod.addInstructions(
            result.scanResult.patternScanResult!!.endIndex - 1, """
            const v1, -0xdededf
			if-ne v1, p1, :dark
			const/4 p1, 0x0
			:dark
			nop
        """
        )
        return PatchResultSuccess()
    }
}