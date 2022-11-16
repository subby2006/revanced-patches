package app.revanced.patches.music.misc.integrations.patch

import app.revanced.annotation.YouTubeMusicCompatibility
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.revanced.patcher.util.smali.toInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.music.misc.integrations.fingerprints.InitFingerprint
import app.revanced.patches.music.misc.integrations.fingerprints.MusicActivityFingerprint
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.immutable.ImmutableField
import org.jf.dexlib2.immutable.ImmutableMethod
import org.jf.dexlib2.immutable.ImmutableMethodImplementation
import org.jf.dexlib2.immutable.ImmutableMethodParameter
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.reference.FieldReference
import org.jf.dexlib2.iface.reference.MethodReference

@Name("music-integrations")
@Description("Applies mandatory patches to implement the ReVanced Music integrations into the application.")
@YouTubeMusicCompatibility
@Version("0.0.1")
class MusicIntegrationsPatch : BytecodePatch(
    listOf(
        InitFingerprint,
        MusicActivityFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        if (context.findClass("Lapp/revanced/integrations/utils/ReVancedMusicUtils") == null)
            return PatchResultError("Integrations have not been merged yet. This patch can not succeed without the integrations.")
        val result = InitFingerprint.result!!

        val method = result.mutableMethod
        val implementation = method.implementation!!
        val count = implementation.registerCount - 1

        method.addInstruction(
            0, "sput-object v$count, Lapp/revanced/integrations/utils/ReVancedMusicUtils;->context:Landroid/content/Context;"
        )

        val MusicActivityResult = MusicActivityFingerprint.result!!
        val MusicActivityMethod = MusicActivityResult.mutableMethod

        MusicActivityMethod.addInstruction(
            0, "invoke-direct/range {p0 .. p0}, Lcom/google/android/apps/youtube/music/activities/MusicActivity;->SetSharedPrefs()V"
        )

        val classDef = MusicActivityResult.mutableClass
        classDef.methods.add(
            ImmutableMethod(
                classDef.type,
                "SetSharedPrefs",
                null,
                "V",
                AccessFlags.PRIVATE.getValue(),
                null,
                null,
                ImmutableMethodImplementation(
                    4, """
                        const-string v1, "youtube"
                        const/4 v2, 0x0
                        invoke-virtual {v3, v1, v2}, Lcom/google/android/apps/youtube/music/activities/MusicActivity;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;
                        move-result-object v0
                        sput-object v0, Lapp/revanced/integrations/utils/ReVancedMusicUtils;->sharedPreferences:Landroid/content/SharedPreferences;
                        return-void
                    """.toInstructions(), null, null
                )
            ).toMutable()
        )

        return PatchResultSuccess()
    }
}