package app.revanced.patches.youtube.layout.sponsorblock.bytecode.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.annotation.YouTubeCompatibility
import org.jf.dexlib2.Opcode

@Name("watch-while-activity-fingerprint")
@YouTubeCompatibility
@Version("0.0.1")
object WatchWhileActivityFingerprint : MethodFingerprint(
    null, null, null, listOf(Opcode.INVOKE_DIRECT_RANGE), null, { methodDef ->
        methodDef.definingClass.endsWith("WatchWhileActivity;") && methodDef.name == "onCreate"
    }
)