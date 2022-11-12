package app.revanced.patches.youtube.ad.general.bytecode.fingerprints

import app.revanced.annotation.NewYouTubeCompatibility
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.youtube.ad.general.resource.patch.NewAdsResourcePatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction

@Name("new-ads-fingerprint")
@NewYouTubeCompatibility
@Version("0.0.1")
object NewAdsFingerprint : MethodFingerprint(
    opcodes = listOf(
        Opcode.IF_EQZ,
        Opcode.IGET_BOOLEAN,
        Opcode.IF_EQZ,
        Opcode.IGET_BOOLEAN,
        Opcode.CONST,
        Opcode.IF_EQZ,
        Opcode.IGET_BOOLEAN,
        Opcode.IF_EQZ,
        Opcode.CONST,
    ),
    customFingerprint = { methodDef ->
        methodDef.implementation?.instructions?.any { instruction ->
            instruction.opcode.ordinal == Opcode.CONST.ordinal &&
            (instruction as? WideLiteralInstruction)?.wideLiteral == NewAdsResourcePatch.newadsResourceId
        } == true
    }
)