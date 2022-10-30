package app.revanced.patches.youtube.ad.general.bytecode.patch

import app.revanced.extensions.injectHideCall
import app.revanced.extensions.injectHideCallReels
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.extensions.MethodExtensions.findMutableMethodOf
import app.revanced.extensions.MethodExtensions.toDescriptor
import app.revanced.patches.youtube.ad.general.resource.patch.GeneralResourceAdsPatch
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.iface.instruction.formats.Instruction21c
import org.jf.dexlib2.iface.instruction.formats.Instruction22c
import org.jf.dexlib2.iface.instruction.formats.Instruction31i
import org.jf.dexlib2.iface.instruction.formats.Instruction35c
import org.jf.dexlib2.iface.reference.FieldReference
import org.jf.dexlib2.iface.reference.MethodReference
import org.jf.dexlib2.iface.reference.StringReference

@Patch
@DependsOn([ResourceMappingResourcePatch::class, IntegrationsPatch::class, GeneralResourceAdsPatch::class])
@Name("general-ads")
@Description("Removes general ads.")
@YouTubeCompatibility
@Version("0.0.1")
class GeneralBytecodeAdsPatch : BytecodePatch() {
    // a constant used by litho
    private val lithoConstant = 0xaed2868

    // list of resource names to get the id of
    private val resourceIds = arrayOf(
        "ad_attribution",
        "reel_multiple_items_shelf",
        "info_cards_drawer_header",
        "endscreen_element_layout_video",
        "endscreen_element_layout_circle",
        "endscreen_element_layout_icon",
        "promoted_video_item_land",
        "promoted_video_item_full_bleed"
    ).map { name ->
        ResourceMappingResourcePatch.resourceMappings.single { it.name == name }.id
    }

    private val stringReferences = arrayOf(
        "Claiming to use more elements than provided",
        "loadVideo() called on LocalDirector in wrong state",
        "LoggingProperties are not in proto format"
    )

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
                            // TODO: find a way to de-duplicate code.
                            //  The issue is we need to save mutableClass and mutableMethod to the existing fields
                            when ((instruction as Instruction31i).wideLiteral) {
                                resourceIds[0] -> { // general ads
                                    //  and is followed by an instruction with the mnemonic INVOKE_VIRTUAL
                                    val insertIndex = index + 1
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.INVOKE_VIRTUAL) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    // insert hide call to hide the view corresponding to the resource
                                    val viewRegister = (invokeInstruction as Instruction35c).registerC
                                    mutableMethod!!.implementation!!.injectHideCall(insertIndex, viewRegister)

                                }

                                resourceIds[1] -> { // reel ads
                                    //  and is followed by an instruction at insertIndex with the mnemonic IPUT_OBJECT
                                    val insertIndex = index + 4
                                    val iPutInstruction = instructions.elementAt(insertIndex)
                                    if (iPutInstruction.opcode != Opcode.IPUT_OBJECT) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    val viewRegister = (iPutInstruction as Instruction22c).registerA
                                    mutableMethod!!.implementation!!.injectHideCallReels(insertIndex, viewRegister)
                                }

                                resourceIds[2] -> { // info cards ads
                                    //  and is followed by an instruction with the mnemonic INVOKE_VIRTUAL
                                    val removeIndex = index - 1
                                    val invokeInstruction = instructions.elementAt(removeIndex)
                                    if (invokeInstruction.opcode != Opcode.INVOKE_VIRTUAL) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    //ToDo: Add Settings toggle for whatever this is
                                    mutableMethod!!.implementation!!.removeInstruction(removeIndex)
                                }

                                resourceIds[3], resourceIds[4], resourceIds[5] -> { // end screen ads
                                    //  and is followed by an instruction with the mnemonic IPUT_OBJECT
                                    val insertIndex = index + 7
                                    //val invokeInstruction = instructions.elementAt(insertIndex)
                                    //if (invokeInstruction.opcode != Opcode.IPUT_OBJECT) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    // TODO: dynamically get registers
                                    mutableMethod!!.addInstructions(
                                        insertIndex, """
                                                invoke-static {}, Lapp/revanced/integrations/patches/HideInfoCardSuggestionsPatch;->hideInfoCardSuggestions()I
                                                move-result v1
                                                invoke-virtual {v0,v1}, Landroid/widget/FrameLayout;->setVisibility(I)V
                                            """
                                    )
                                }

                                resourceIds[6] -> {
                                    //  and is followed by an instruction with the mnemonic INVOKE_DIRECT
                                    val insertIndex = index + 3
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.INVOKE_DIRECT) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    // insert hide call to hide the view corresponding to the resource
                                    val viewRegister = (invokeInstruction as Instruction35c).registerE
                                    mutableMethod!!.implementation!!.injectHideCall(insertIndex, viewRegister)
                                }

                                resourceIds[7] -> {
                                    // TODO, go to class, hide the inflated view
                                }

                            }
                        }

                        Opcode.CONST_STRING -> {
                            when (((instruction as Instruction21c).reference as StringReference).string) {
                                stringReferences[0] -> {
                                    val stringInstruction = instructions.elementAt(3)
                                    if (stringInstruction.opcode == Opcode.CONST_STRING) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = context.proxy(classDef).mutableClass
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    // return the method
                                    val insertIndex = 1 // after super constructor
                                    //ToDo: Add setting here
                                    mutableMethod!!.implementation!!.addInstruction(
                                        insertIndex, BuilderInstruction10x(Opcode.RETURN_VOID)
                                    )
                                }

                                stringReferences[1] -> {
                                    // TODO: migrate video ads patch to here if necessary
                                }

                                stringReferences[2] -> { // Litho ads
                                    val proxy = context.proxy(classDef)
                                    val proxiedClass = proxy.mutableClass

                                    val lithoMethod = getLithoMethod(proxiedClass)
                                        ?: return PatchResultError("Could not find required Litho method to patch.")

                                    val instructionWithNeededDescriptor =
                                        lithoMethod.implementation!!.instructions.indexOfFirst {
                                            it.opcode == Opcode.INVOKE_STATIC_RANGE
                                        }

                                    // get required descriptors
                                    val createEmptyComponentDescriptor =
                                        lithoMethod.instruction(instructionWithNeededDescriptor)
                                            .toDescriptor<MethodReference>()
                                    val emptyComponentFieldDescriptor =
                                        lithoMethod.instruction(instructionWithNeededDescriptor + 2)
                                            .toDescriptor<FieldReference>()

                                    val pathBuilderAnchorFingerprint = object : MethodFingerprint(
                                        opcodes = listOf(
                                            Opcode.CONST_16,
                                            Opcode.INVOKE_VIRTUAL,
                                            Opcode.IPUT_OBJECT
                                        )
                                    ) {}

                                    val pathBuilderScanResult = pathBuilderAnchorFingerprint.also {
                                        it.resolve(context, lithoMethod, classDef)
                                    }.result!!.scanResult.patternScanResult!!

                                    val clobberedRegister =
                                        (lithoMethod.instruction(pathBuilderScanResult.startIndex) as OneRegisterInstruction).registerA

                                    val insertIndex = pathBuilderScanResult.endIndex + 1
                                    lithoMethod.addInstructions(
                                        insertIndex, // right after setting the component.pathBuilder field,
                                        """
                                                invoke-static {v5, v2}, Lapp/revanced/integrations/patches/LithoFilterPatch;->filter(Ljava/lang/StringBuilder;Ljava/lang/String;)Z
                                                move-result v$clobberedRegister
                                                if-eqz v$clobberedRegister, :not_an_ad
                                                move-object/from16 v2, p1
                                                invoke-static {v2}, $createEmptyComponentDescriptor
                                                move-result-object v0
                                                iget-object v0, v0, $emptyComponentFieldDescriptor
                                                return-object v0
                                            """,
                                        listOf(ExternalLabel("not_an_ad", lithoMethod.instruction(insertIndex)))
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

    private fun getLithoMethod(mutableClass: MutableClass) = mutableClass.methods.firstOrNull {
        it.implementation?.instructions?.any { instruction ->
            instruction.opcode == Opcode.CONST && (instruction as Instruction31i).narrowLiteral == lithoConstant
        } ?: false
    }
}
