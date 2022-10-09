package app.revanced.patches.youtube.misc.settings.bytecode.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.youtube.layout.returnyoutubedislike.annotations.ReturnYouTubeDislikeCompatibility
import app.revanced.patches.youtube.misc.settings.bytecode.patch.SettingsPatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction

@Name("theme-setter-fingerprint")
@ReturnYouTubeDislikeCompatibility
@Version("0.0.1")
object ThemeSetterFingerprint : MethodFingerprint(
    "L",
    null,
    null,
    listOf(Opcode.RETURN_OBJECT),
    null,
    { methodDef ->
        methodDef.implementation?.instructions?.any {
            it.opcode.ordinal == Opcode.CONST.ordinal && (it as WideLiteralInstruction).wideLiteral == SettingsPatch.appearanceStringId
        } == true
    }
)