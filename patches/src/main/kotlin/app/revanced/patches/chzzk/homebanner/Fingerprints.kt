package app.revanced.patches.chzzk.homebanner

import app.morphe.patcher.Fingerprint

internal object StreamingHomeBannersFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    strings = listOf("StreamingHomeBanners(banners="),
)

internal object FootballCampaignFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    strings = listOf("FootballCampaign(exposure="),
)

// No return type: up to 3.15.x this is a void method, from 3.17.0 R8 merges it into a
// synthetic Function1.invoke that returns Object. The strings alone are unique.
internal object TopicBannerRowsFingerprint : Fingerprint(
    strings = listOf("topic_imageBannerListItem_", "topic_spacialEventBannerListItem_"),
)