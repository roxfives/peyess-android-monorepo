package com.peyess.salesapp.data.model.stats

data class LensStatsDocument(
    val total: Int = 0,
    val disabled: Int = 0,
) {
    val totalEnabled: Int = total - disabled
}
