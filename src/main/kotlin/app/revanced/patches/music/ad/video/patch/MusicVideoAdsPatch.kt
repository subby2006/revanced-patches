package app.revanced.patches.music.ad.video.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.music.ad.video.fingerprints.ShowMusicVideoAdsConstructorFingerprint
import app.revanced.patches.music.ad.video.fingerprints.ShowMusicVideoAdsFingerprint
import app.revanced.patches.music.misc.integrations.patch.MusicIntegrationsPatch
import app.revanced.patches.music.misc.settings.patch.MusicSettingsPatch
import app.revanced.patches.youtube.ad.video.fingerprints.LoadVideoAdsFingerprint
import app.revanced.shared.annotation.YouTubeMusicCompatibility

@Patch
@DependsOn([MusicIntegrationsPatch::class, MusicSettingsPatch::class])
@Name("music-video-ads")
@Description("Removes ads in the music player.")
@YouTubeMusicCompatibility
@Version("0.0.1")
class MusicVideoAdsPatch : BytecodePatch(
    listOf(
        ShowMusicVideoAdsConstructorFingerprint,
        LoadVideoAdsFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        ShowMusicVideoAdsFingerprint.resolve(context, ShowMusicVideoAdsConstructorFingerprint.result!!.classDef)

        val result = ShowMusicVideoAdsFingerprint.result!!

        result.mutableMethod.addInstructions(
            result.scanResult.patternScanResult!!.startIndex, """
                invoke-static {}, Lapp/revanced/integrations/settings/MusicSettings;->getShowAds()Z
                move-result p1
            """
        )

        val loadVideoAdsFingerprintMethod = LoadVideoAdsFingerprint.result!!.mutableMethod

        loadVideoAdsFingerprintMethod.addInstructions(
            0, """
                invoke-static {}, Lapp/revanced/integrations/settings/MusicSettings;->getShowAds()Z
                move-result v0
                if-nez v0, :show_video_ads
                return-void
            """, listOf(ExternalLabel("show_video_ads", loadVideoAdsFingerprintMethod.instruction(0)))
        )

        return PatchResultSuccess()
    }
}
