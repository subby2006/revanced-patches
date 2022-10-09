package app.revanced.patches.music.misc.microg.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.music.misc.microg.annotations.MusicMicroGPatchCompatibility

@Name("cast-context-fetch-fingerprint")
@MusicMicroGPatchCompatibility
@Version("0.0.1")
object CastContextFetchFingerprint : MethodFingerprint(
    strings = listOf("Error fetching CastContext.")
)