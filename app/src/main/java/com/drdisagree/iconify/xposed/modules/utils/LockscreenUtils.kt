package com.drdisagree.iconify.xposed.modules.utils

import android.os.Build
import com.drdisagree.iconify.common.Const.SYSTEMUI_PACKAGE
import com.drdisagree.iconify.xposed.modules.extras.utils.toolkit.XposedHook.Companion.findClass
import java.util.Calendar

object LockscreenUtils {

    /**
     * Android 15 QPR2+ moved the keyguard to a Compose based layout.
     * Detect whether the current SystemUI uses the new Compose lockscreen.
     */
    val isComposeLockscreen: Boolean by lazy {
        val hasAodBurnInLayer = findClass(
            "$SYSTEMUI_PACKAGE.keyguard.ui.view.layout.sections.AodBurnInLayer",
            suppressError = true
        ) != null

        val hasBatteryMeterViewEx = findClass(
            "com.nothing.systemui.battery.BatteryMeterViewEx",
            suppressError = true
        ) != null

        val isSupportedAndroidVersion = Build.VERSION.SDK_INT >= 35

        val isAfterSecurityPatch = TimeUtils.isSecurityPatchAfter(
            Calendar.getInstance().apply { set(2024, Calendar.NOVEMBER, 30) }
        )

        hasAodBurnInLayer && !hasBatteryMeterViewEx && isSupportedAndroidVersion && isAfterSecurityPatch
    }
}
