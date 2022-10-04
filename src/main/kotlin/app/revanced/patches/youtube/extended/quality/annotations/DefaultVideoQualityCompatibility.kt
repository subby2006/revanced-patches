package app.revanced.patches.youtube.extended.quality.annotations

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    [Package(
        "com.google.android.youtube", arrayOf("17.32.39", "17.34.36", "17.36.39", "17.37.35", "17.38.36")
    )]
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class DefaultVideoQualityCompatibility