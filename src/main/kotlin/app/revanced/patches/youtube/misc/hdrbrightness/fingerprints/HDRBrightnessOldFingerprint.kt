package app.revanced.patches.youtube.misc.hdrbrightness.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.FuzzyPatternScanMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.reference.FieldReference
import org.jf.dexlib2.Opcode

@Name("hdr-brightness-old-fingerprint")
@FuzzyPatternScanMethod(3)
@YouTubeCompatibility
@Version("0.0.1")
object HDRBrightnessOldFingerprint : MethodFingerprint(
    "V", AccessFlags.PUBLIC or AccessFlags.FINAL,
    listOf("I", "I", "I", "I"),
    listOf(
        Opcode.SGET_OBJECT,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.IGET,
        Opcode.IPUT,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_VIRTUAL
    ),
    customFingerprint = { methodDef ->
        methodDef.implementation!!.instructions.any {
            ((it as? ReferenceInstruction)?.reference as? FieldReference)?.let { field ->
                // iput vx, vy, Landroid/view/WindowManager$LayoutParams;->screenBrightness:F
                field.definingClass == "Landroid/view/WindowManager\$LayoutParams;" && field.name == "screenBrightness"
            } == true
        }
    }
)