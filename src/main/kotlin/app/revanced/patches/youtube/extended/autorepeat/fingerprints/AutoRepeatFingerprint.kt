package app.revanced.patches.youtube.extended.autorepeat.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.annotation.FuzzyPatternScanMethod
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.extensions.YouTubeCompatibility
import org.jf.dexlib2.AccessFlags

@Name("auto-repeat-fingerprint")
@FuzzyPatternScanMethod(2)
@YouTubeCompatibility
@Version("0.0.1")
//Finds method:
/*
public final void ae() {
        aq(aabj.ENDED);
    }
 */
object AutoRepeatFingerprint : MethodFingerprint(
    "V",
    AccessFlags.PUBLIC or AccessFlags.FINAL,
    listOf(),
    null,
    null,
    customFingerprint = { methodDef -> methodDef.implementation!!.instructions.count() == 3 && methodDef.annotations.isEmpty()}
)