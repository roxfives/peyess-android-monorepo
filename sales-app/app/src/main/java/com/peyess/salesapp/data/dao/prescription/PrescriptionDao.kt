package com.peyess.salesapp.data.dao.prescription

import com.peyess.salesapp.data.model.prescription.FSPrescription

interface PrescriptionDao {
    suspend fun add(document: FSPrescription)
}