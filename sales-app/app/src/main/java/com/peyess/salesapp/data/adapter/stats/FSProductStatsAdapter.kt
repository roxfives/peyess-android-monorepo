package com.peyess.salesapp.data.adapter.stats

import com.peyess.salesapp.data.model.stats.FSLensStats
import com.peyess.salesapp.data.model.stats.LensStatsDocument

fun FSLensStats.toProductStatsDocument(): LensStatsDocument {
    return LensStatsDocument(
        total = total,
        disabled = disabled,
    )
}