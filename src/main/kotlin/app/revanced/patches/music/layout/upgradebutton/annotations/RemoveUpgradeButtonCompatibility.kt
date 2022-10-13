package app.revanced.patches.music.layout.upgradebutton.annotations

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    [Package(
        "com.google.android.apps.youtube.music", arrayOf("5.23.50", "5.25.51", "5.26.53", "5.27.51", "5.28.51")
    )]
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class RemoveUpgradeButtonCompatibility
