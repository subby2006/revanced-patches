package app.revanced.patches.youtube.extended.extended.bytecode.patch

import app.revanced.extensions.MethodExtensions.findMutableMethodOf
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch
import app.revanced.shared.annotation.YouTubeCompatibility
import org.jf.dexlib2.iface.instruction.formats.*
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.Opcode

@Patch
@DependsOn(
    [
        IntegrationsPatch::class,
        SettingsResourcePatch::class,
        ResourceMappingResourcePatch::class
    ]
)
@Name("extended")
@Description("Add ReVanced Extended Features.")
@YouTubeCompatibility
@Version("0.0.1")
class ExtendedPatch : BytecodePatch() {

    // list of resource names to get the id of
    private val resourceIds = arrayOf(
        "suggested_action",
        "ytWordmarkHeader",
        "ytPremiumWordmarkHeader",
        "album_card",
        "ic_right_comment_32c",
        "horizontal_card_list",
        "reel_player_paused_state_buttons",
        "reel_dyn_remix",
        "scrim_overlay",
        "google_transparent",
        "app_related_endscreen_results"
    ).map { name ->
        ResourceMappingResourcePatch.resourceMappings.single { it.name == name }.id
    }

    override fun execute(context: BytecodeContext): PatchResult {

        // iterating through all classes is expensive
        for (classDef in context.classes) {
            var mutableClass: MutableClass? = null

            method@ for (method in classDef.methods) {
                var mutableMethod: MutableMethod? = null

                if (method.implementation == null) continue@method

                val instructions = method.implementation!!.instructions
                instructions.forEachIndexed { index, instruction ->
                    when (instruction.opcode) {
                        Opcode.CONST -> {
                            when ((instruction as Instruction31i).wideLiteral) {

                                resourceIds[0] -> { // suggested_action
                                    val insertIndex = index + 4

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    mutableMethod!!.addInstructions(
                                        insertIndex, """
                                                invoke-static {v0}, Lapp/revanced/integrations/patches/SuggestedActionsPatch;->hideSuggestedActions(Landroid/view/View;)V
                                                """
                                    )
                                }

                                resourceIds[1] -> { // header
                                    val insertIndex = index - 1
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.SGET_OBJECT) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)
                                    val premiumheader = resourceIds[2]

                                    mutableMethod!!.addInstructions(
                                        insertIndex + 2, """
                                                invoke-static {}, Lapp/revanced/integrations/patches/PremiumHeaderPatch;->getPremiumHeaderOverride()Z
                                                move-result v15
                                                if-eqz v15, :currentheader
                                                const v3, $premiumheader
                                                """, listOf(ExternalLabel("currentheader", mutableMethod!!.instruction(insertIndex + 2)))
                                    )
                                }

                                resourceIds[3] -> { // music container
                                    val insertIndex = index + 4
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.CHECK_CAST) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val viewRegister = 2
                                    mutableMethod!!.addInstruction(
                                        insertIndex,
                                        "invoke-static {p$viewRegister}, Lapp/revanced/integrations/patches/HideAlbumCardsPatch;->hideAlbumCards(Landroid/view/View;)V"
                                    )
                                }

                                resourceIds[4] -> { // shorts comment
                                    val insertIndex = index - 2
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.CONST_HIGH16) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val viewRegister = (instructions.elementAt(index + 3) as OneRegisterInstruction).registerA
                                    mutableMethod!!.addInstruction(
                                        index + 4,
                                        "invoke-static {v$viewRegister}, Lapp/revanced/integrations/patches/HideShortsCommentsButtonPatch;->hideShortsCommentsButton(Landroid/view/View;)V"
                                    )
                                }

                                resourceIds[5] -> { // breaking news
                                    val insertIndex = index + 4
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.CHECK_CAST) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val viewRegister = (invokeInstruction as Instruction21c).registerA
                                    mutableMethod!!.addInstruction(
                                        insertIndex,
                                        "invoke-static {v$viewRegister}, Lapp/revanced/integrations/patches/BreakingNewsPanelsRemoverPatch;->HideBreakingNewsPanels(Landroid/view/View;)V"
                                    )
                                }

                                resourceIds[6] -> { // subscriptions banner (shorts)
                                    val insertIndex = index + 3
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.CHECK_CAST) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val viewRegister = (invokeInstruction as Instruction21c).registerA
                                    mutableMethod!!.addInstruction(
                                        insertIndex,
                                        "invoke-static {v$viewRegister}, Lapp/revanced/integrations/patches/HideShortsPlayerSubscriptionsPatch;->hideSubscriptions(Landroid/view/View;)V"
                                    )
                                }

                                resourceIds[7] -> { // remix button (shorts)
                                    val insertIndex = index - 2
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.CHECK_CAST) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val viewRegister = (invokeInstruction as Instruction21c).registerA
                                    mutableMethod!!.addInstruction(
                                        index - 1,
                                        "invoke-static {v$viewRegister}, Lapp/revanced/integrations/patches/HideShortsRemixButtonPatch;->hideShortsRemixButton(Landroid/view/View;)V"
                                    )
                                }

                                resourceIds[8] -> { // player overlay background
                                    val insertIndex = index + 3
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.CHECK_CAST) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val dummyRegister = (instructions.elementAt(index) as Instruction31i).registerA
                                    val viewRegister = (invokeInstruction as Instruction21c).registerA

                                    val transparent = resourceIds[9]

                                    mutableMethod!!.addInstructions(
                                        insertIndex + 1, """
                                                invoke-static {}, Lapp/revanced/integrations/patches/PlayerOverlayBackgroundPatch;->getPlayerOverlaybackground()Z
                                                move-result v$dummyRegister
                                                if-eqz v$dummyRegister, :currentcolor
                                                const v$dummyRegister, $transparent
                                                invoke-virtual {v$viewRegister, v$dummyRegister}, Landroid/widget/ImageView;->setImageResource(I)V
                                                """, listOf(ExternalLabel("currentcolor", mutableMethod!!.instruction(insertIndex + 1)))
                                    )
                                }

                                resourceIds[10] -> { // end screen result
                                    val insertIndex = index - 13
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.IF_NEZ) return@forEachIndexed

                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val dummyRegister = (instructions.elementAt(index) as Instruction31i).registerA

                                    mutableMethod!!.addInstructions(
                                        insertIndex, """
                                                invoke-static {}, Lapp/revanced/integrations/patches/HideEndscreenOverlayPatch;->getEndscreenOverlay()Z
                                                move-result v$dummyRegister
                                                if-eqz v$dummyRegister, :off
                                                return-void
                                                """, listOf(ExternalLabel("off", mutableMethod!!.instruction(insertIndex)))
                                    )
                                }
                            }
                        }
                        else -> return@forEachIndexed
                    }
                }
            }
        }
        return PatchResultSuccess()
    }
}
