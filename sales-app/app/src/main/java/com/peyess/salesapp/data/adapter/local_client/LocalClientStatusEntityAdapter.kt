package com.peyess.salesapp.data.adapter.local_client

import com.peyess.salesapp.data.model.local_client.LocalClientStatusDocument
import com.peyess.salesapp.data.model.local_client.LocalClientStatusEntity


fun LocalClientStatusEntity.toLocalClientStatusDocument(): LocalClientStatusDocument {
    return LocalClientStatusDocument(
        hasLatestDownloadFailed = hasLatestDownloadFailed,
        hasLatestUploadFailed = hasLatestUploadFailed,
        latestDownload = latestDownload,
        latestUpload = latestUpload,
    )
}