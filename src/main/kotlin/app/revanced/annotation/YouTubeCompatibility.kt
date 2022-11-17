package app.revanced.annotation

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    [Package(
        "com.google.android.youtube", arrayOf("17.34.36", "17.38.36", "17.40.41", "17.41.37", "17.42.35", "17.43.37", "17.44.35", "17.45.34")
    )]
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class YouTubeCompatibility

