package app.revanced.patches.youtube.extended.hidebuttoncontainer.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patcher.util.smali.toInstructions
import app.revanced.extensions.YouTubeCompatibility
import app.revanced.extensions.MethodExtensions.addMethod
import app.revanced.extensions.MethodExtensions.insertBlocks
import app.revanced.extensions.MethodExtensions.toDescriptor
import app.revanced.patches.youtube.extended.hidebuttoncontainer.fingerprints.LithoFingerprint
import app.revanced.patches.youtube.extended.hidebuttoncontainer.utils.MethodUtils
import app.revanced.patches.youtube.ad.general.bytecode.patch.GeneralBytecodeAdsPatch
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.builder.BuilderInstruction
import org.jf.dexlib2.builder.MutableMethodImplementation
import org.jf.dexlib2.builder.instruction.*
import org.jf.dexlib2.iface.MethodImplementation
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction
import org.jf.dexlib2.iface.reference.MethodReference
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference

@Patch
@DependsOn([IntegrationsPatch::class, ResourceMappingResourcePatch::class, GeneralBytecodeAdsPatch::class])
@Name("hide-button-container")
@Description("Removes button container.")
@YouTubeCompatibility
@Version("0.0.1")
class HideButtonContainerPatch : BytecodePatch(
    listOf(
        LithoFingerprint
    )
)
{
    override fun execute(context: BytecodeContext): PatchResult {

        //Litho
        val lithoResult = LithoFingerprint.result
        val lithoMethod = lithoResult!!.mutableMethod
        val lithoClass = lithoResult.mutableClass
        val lithoImplementation = lithoMethod.implementation!!
        val lithoInstructions = lithoMethod.implementation!!.instructions

        lithoClass.addGetIsEmptyMethod()
        lithoClass.addMethod(lithoClass.createGetTemplateNameMethod(lithoImplementation))

        val lithoPatchIndex = lithoInstructions.indexOfFirst {
            it.opcode == Opcode.CONST_STRING &&
            (it as BuilderInstruction21c).reference.toString() == "Error while converting "
        } + 11

        val lithoMethodThirdParam = lithoMethod.parameters[2]
        val block = """
            move-object/from16 v1, v27
            invoke-virtual {v1}, Ljava/lang/Object;->toString()Ljava/lang/String;
            move-result-object v1
            move-object/from16 v2, v28
            iget-object v2, v2, $lithoMethodThirdParam->b:Ljava/nio/ByteBuffer;
            invoke-static {v1, v2}, Lapp/revanced/integrations/patches/HideButtonContainerPatch;->ContainerLithoView(Ljava/lang/String;Ljava/nio/ByteBuffer;)Z
            move-result v3
            const/4 v0, 0x0
        """.toInstructions()

        lithoImplementation.insertBlocks(
            lithoPatchIndex + 1,
            block
        )

        val blockEndIndex = lithoPatchIndex + block.size

        val ifNez = (lithoInstructions[lithoPatchIndex] as BuilderInstruction21t)
        lithoImplementation.removeInstruction(lithoPatchIndex)
        lithoImplementation.insertBlocks(
            blockEndIndex,
            listOf(
                ifNez
            )
        )

        lithoImplementation.insertBlocks(
            blockEndIndex - 1,
            listOf(
                BuilderInstruction21t(Opcode.IF_EQZ, 3, lithoImplementation.newLabelForIndex(blockEndIndex)),
            )
        )

        return PatchResultSuccess()
    }

    private fun MutableClass.addGetIsEmptyMethod() {
        val getIsEmptyImplementation = MutableMethodImplementation(1)

        // create target instructions
        val firstTargetInstruction = BuilderInstruction11n(Opcode.CONST_4, 0, 1)
        val secondTargetInstruction = BuilderInstruction11n(Opcode.CONST_4, 0, 0)

        // add instructions to the instruction list
        getIsEmptyImplementation.addInstructions(
            0, listOf(
                BuilderInstruction35c(
                    Opcode.INVOKE_VIRTUAL,
                    1,
                    0,
                    0,
                    0,
                    0,
                    0,
                    ImmutableMethodReference("Ljava/lang/String;", "isEmpty", null, "Z")
                ),
                BuilderInstruction11x(Opcode.MOVE_RESULT, 0),
                secondTargetInstruction,
                BuilderInstruction11x(Opcode.RETURN, 0),
                firstTargetInstruction,
                BuilderInstruction11x(Opcode.RETURN, 0),
            )
        )

        val getIsEmptyInstructions = getIsEmptyImplementation.instructions

        // create labels for the target instructions
        val firstLabel =
            getIsEmptyImplementation.newLabelForIndex(getIsEmptyInstructions.indexOf(firstTargetInstruction))
        val secondLabel =
            getIsEmptyImplementation.newLabelForIndex(getIsEmptyInstructions.indexOf(secondTargetInstruction))

        // create branch instructions to the labels
        val ifEqzFirstInstruction = BuilderInstruction21t(Opcode.IF_EQZ, 0, firstLabel)
        val ifEqzSecondInstruction = BuilderInstruction21t(Opcode.IF_EQZ, 0, secondLabel)
        val gotoInstruction = BuilderInstruction10t(Opcode.GOTO, firstLabel)

        // insert remaining branch instructions, order of adding those instructions is important
        getIsEmptyImplementation.addInstructions(
            2, listOf(
                ifEqzSecondInstruction, gotoInstruction
            )
        )
        getIsEmptyImplementation.addInstruction(
            0, ifEqzFirstInstruction
        )

        this.addMethod(
            MethodUtils.createMutableMethod(
                this.type, "getIsEmpty", "Z", "Ljava/lang/String;", getIsEmptyImplementation
            )
        )
    }

    private fun MutableClass.createGetTemplateNameMethod(lithoMethodImplementation: MethodImplementation): MutableMethod {
        var counter = 1
        val descriptors = buildList {
            for (instruction in lithoMethodImplementation.instructions) {
                if (instruction !is ReferenceInstruction) continue
                if (counter++ > 4) break

                add(instruction.toDescriptor<MethodReference>())
            }
        }

        val getTemplateNameImplementation = MutableMethodImplementation(2)

        // create code blocks
        val block1 = """
            invoke-virtual {p0}, ${descriptors[0]}
            move-result-object p0
            const v0, 0xaed2868
            invoke-static {p0, v0}, ${descriptors[1]}
            move-result-object p0
        """.toInstructions()
        val block2 = """
              invoke-static {p0}, ${descriptors[2]}
              move-result-object p0
              invoke-virtual {p0}, ${descriptors[3]}
              move-result-object v0
              invoke-static {v0}, ${this.type}->getIsEmpty(Ljava/lang/String;)Z
              move-result v0
        """.toInstructions()
        val block3 = """
              invoke-virtual {p0}, ${descriptors[3]}
              move-result-object p0
              return-object p0
        """.toInstructions()

        // create target instruction
        val targetInstruction = BuilderInstruction11n(Opcode.CONST_4, 1, 0)
        // and remaining instruction
        val returnInstruction = BuilderInstruction11x(Opcode.RETURN_OBJECT, 1)

        // insert blocks and instructions
        getTemplateNameImplementation.insertBlocks(
            0,
            block1,
            block2,
            block3,
            listOf(
                targetInstruction, returnInstruction
            ),
        )

        // create label for target instruction
        val targetInstructionLabel =
            getTemplateNameImplementation.newLabelForIndex(getTemplateNameImplementation.instructions.size - 2)

        // create branch instructions to the label
        val ifEqzInstruction = BuilderInstruction21t(Opcode.IF_EQZ, 1, targetInstructionLabel)
        val ifNezInstruction = BuilderInstruction21t(Opcode.IF_NEZ, 0, targetInstructionLabel)

        // insert branch instructions
        getTemplateNameImplementation.addInstruction(
            block1.size, ifEqzInstruction
        )
        getTemplateNameImplementation.addInstruction(
            block1.size + block2.size + 1, ifNezInstruction
        )

        // create the method
        return MethodUtils.createMutableMethod(
            this.type,
            "getTemplateName",
            "Ljava/lang/String;",
            descriptors[0].split("->")[0], // a bit weird to get the type this way,
            getTemplateNameImplementation
        )
    }
}
