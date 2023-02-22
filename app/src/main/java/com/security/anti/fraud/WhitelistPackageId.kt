package com.security.anti.fraud

enum class WhitelistPackageId(private val packageIds: List<String>) {
    SAMSUNG_ACCESSIBILITY(
        listOf(
            "com.samsung.accessibility",
            "com.samsung.android.accessibility.talkback"
        )
    ),
    ANDROID_ACCESSIBILITY(
        listOf(
            "com.android.settings",
            "com.android.bluetooth",
            "com.google.android.marvin.talkback",
            "com.android.email",
            "com.android.systemui",
            "com.google.android.apps.searchlite"
        )
    ),
    HUAWEI_ACCESSIBILITY(
        listOf(
            "com.huawei.recsys",
            "com.huawei.hiai",
            "com.huawei.mycenter",
            "com.huawei.hiwrite",
            "com.huawei.HwMultiScreenShot"
        )
    ),
    VIVO_ACCESSIBILITY(
        listOf(
            "com.vivo.bsptest",
        )
    ),
    XIAOMI_ACCESSIBILITY(
        listOf(
            "com.xiaomi.it",
            "com.xiaomi.gamecenter.sdk.service",
            "com.miui.securitycenter"
        )
    ),
    OPPO_ACCESSIBILITY(
        listOf(
            "com.aiunit.aon"
        )
    );

    fun toIDs(): List<String> = packageIds
}