package app.revanced.patches.youtube.misc.extended.bytecode.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.BytecodeData
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.youtube.misc.extended.annotation.ExtendedCompatibility
import app.revanced.patches.youtube.ad.general.bytecode.extensions.MethodExtensions.findMutableMethodOf
import app.revanced.patches.youtube.ad.general.bytecode.extensions.MethodExtensions.toDescriptor
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.mapping.patch.ResourceMappingResourcePatch
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch
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
@DependsOn(
    [
        IntegrationsPatch::class,
        SettingsResourcePatch::class,
        ResourceMappingResourcePatch::class
    ]
)
@Name("extended")
@Description("Add ReVanced Extended Features.")
@ExtendedCompatibility
@Version("0.0.1")
class ExtendedPatch : BytecodePatch() {

    // list of resource names to get the id of
    private val resourceIds = arrayOf(
        "suggested_action",
        "ytWordmarkHeader",
        "ytPremiumWordmarkHeader",
        "Theme.YouTube.Light"
    ).map { name ->
        ResourceMappingResourcePatch.resourceMappings.single { it.name == name }.id
    }

    override fun execute(data: BytecodeData): PatchResult {

        // iterating through all classes is expensive
        for (classDef in data.classes) {
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

                                resourceIds[0] -> { // suggested_action
                                    //  and is followed by an instruction with the mnemonic IPUT_OBJECT
                                    val insertIndex = index + 4
                                    //val invokeInstruction = instructions.elementAt(insertIndex)
                                    //if (invokeInstruction.opcode != Opcode.IPUT_OBJECT) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = data.proxy(classDef).resolve()
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    // TODO: dynamically get registers
                                    mutableMethod!!.addInstructions(
                                        insertIndex, """
                                                invoke-static {v0}, Lapp/revanced/integrations/patches/SuggestedActionsPatch;->hideSuggestedActions(Landroid/view/View;)V
												"""
                                    )
                                }

                                resourceIds[1] -> { // header
                                    //  and is followed by an instruction with the mnemonic IPUT_OBJECT
                                    val insertIndex = index - 1
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.SGET_OBJECT) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = data.proxy(classDef).resolve()
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)
									val premiumheader = resourceIds[2]

                                    // TODO: dynamically get registers
                                    mutableMethod!!.addInstructions(
                                        insertIndex + 2, """
                                                invoke-static {}, Lapp/revanced/integrations/patches/PremiumHeaderPatch;->getPremiumHeaderOverride()Z
                                                move-result v15
                                                if-eqz v15, :currentheader
                                                const v3, $premiumheader
                                                :currentheader
                                                nop
												"""
                                    )
                                }

                                resourceIds[3] -> { // theme
                                    //  and is followed by an instruction with the mnemonic RETURN_OBJECT
                                    val insertIndex = index + 2
                                    val invokeInstruction = instructions.elementAt(insertIndex)
                                    if (invokeInstruction.opcode != Opcode.RETURN_OBJECT) return@forEachIndexed

                                    // create proxied method, make sure to not re-resolve() the current class
                                    if (mutableClass == null) mutableClass = data.proxy(classDef).resolve()
                                    if (mutableMethod == null) mutableMethod =
                                        mutableClass!!.findMutableMethodOf(method)

                                    // TODO: dynamically get registers
                                    mutableMethod!!.addInstructions(
                                        index + 1, """
                                                const/4 v0, 0x0
                                                invoke-static {v0}, Lapp/revanced/integrations/utils/ThemeHelper;->setTheme(I)V
												"""
                                    )

                                    mutableMethod!!.addInstructions(
                                        index - 1, """
                                                const/4 v0, 0x1
                                                invoke-static {v0}, Lapp/revanced/integrations/utils/ThemeHelper;->setTheme(I)V
												"""
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
