package app.revanced.patches.youtube.ad.infocardsuggestions.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.replaceInstruction
import app.revanced.patcher.extensions.removeInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.annotation.YouTubeCompatibility
import app.revanced.patches.youtube.ad.infocardsuggestions.fingerprints.HideInfocardSuggestionsFingerprint
import app.revanced.patches.youtube.ad.infocardsuggestions.fingerprints.HideInfocardSuggestionsParentFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c

@Patch
@DependsOn([IntegrationsPatch::class])
@Name("hide-infocard-suggestions")
@Description("Hides infocards in videos.")
@YouTubeCompatibility
@Version("0.0.1")
class HideInfocardSuggestionsPatch : BytecodePatch(
    listOf(
        HideInfocardSuggestionsParentFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val parentResult = HideInfocardSuggestionsParentFingerprint.result
            ?: return PatchResultError("Parent fingerprint not resolved!")


        HideInfocardSuggestionsFingerprint.resolve(context, parentResult.classDef)
        val result = HideInfocardSuggestionsFingerprint.result
            ?: return PatchResultError("Required parent method could not be found.")

        val method = result.mutableMethod
        val implementation = method.implementation
            ?: return PatchResultError("Implementation not found.")

        val index = implementation.instructions.indexOfFirst { ((it as? BuilderInstruction35c)?.reference.toString() == "Landroid/view/View;->setVisibility(I)V") }

        method.removeInstruction(index)
        method.addInstructions(
            index, """
            invoke-static {}, Lapp/revanced/integrations/patches/HideSuggestionsPatch;->hideSuggestions()I
            move-result v1
            invoke-virtual {p1,v1}, Landroid/view/View;->setVisibility(I)V
        """
        )

        return PatchResultSuccess()
    }

}