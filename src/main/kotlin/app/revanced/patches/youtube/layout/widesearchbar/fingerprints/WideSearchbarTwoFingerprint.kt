package app.revanced.patches.youtube.layout.widesearchbar.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

@Name("wide-searchbar-methodtwo-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")

/*
public static jis h(Context context, txm txmVar, uag uagVar, txp txpVar) {
        return fbn.aB(txmVar) ? new jhx(context, txmVar, uagVar, txpVar) : jis.d;
    }

Method we search for is located in smali now.
See:
.method public static h(Landroid/content/Context;Ltxm;Luag;Ltxp;)Ljis;
    invoke-static {p1}, Lfbn;->aB(Ltxm;)Z //this is the method we want to change. fbn.aB
    move-result v0
    if-eqz v0, :cond_c
    new-instance v0, Ljhx;
    invoke-direct {v0, p0, p1, p2, p3}, Ljhx;-><init>(Landroid/content/Context;Ltxm;Luag;Ltxp;)V
    goto :goto_e
    :cond_c
    sget-object v0, Ljis;->d:Ljis;
    :goto_e
    return-object v0
.end method

 */

object WideSearchbarTwoFingerprint : MethodFingerprint(
    "L", AccessFlags.PUBLIC or AccessFlags.STATIC, opcodes = listOf(
        Opcode.INVOKE_STATIC, Opcode.MOVE_RESULT, Opcode.IF_EQZ, Opcode.NEW_INSTANCE
    )
)