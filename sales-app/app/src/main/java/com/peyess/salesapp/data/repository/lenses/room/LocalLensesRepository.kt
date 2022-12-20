package com.peyess.salesapp.data.repository.lenses.room

import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.coloring.StoreLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensCategoryDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensDescriptionDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensFamilyDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensGroupDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensMaterialCategoryDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensMaterialDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensSpecialtyDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensSupplierDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensTechDocument
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensTypeDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument

interface LocalLensesRepository {
    suspend fun addFamily(family: LocalLensFamilyDocument)

    suspend fun addDescription(description: LocalLensDescriptionDocument)

    suspend fun addSupplier(supplier: LocalLensSupplierDocument)

    suspend fun addGroup(group: LocalLensGroupDocument)

    suspend fun addSpecialty(specialty: LocalLensSpecialtyDocument)

    suspend fun addTech(tech: LocalLensTechDocument)

    suspend fun addType(type: LocalLensTypeDocument)

    suspend fun addCategory(category: LocalLensCategoryDocument)

    suspend fun addMaterial(material: LocalLensMaterialDocument)

    suspend fun addMaterialCategory(materialCategory: LocalLensMaterialCategoryDocument)

    suspend fun addColoring(coloring: LocalLensColoringDocument)

    suspend fun addTreatment(treatment: LocalLensTreatmentDocument)

    suspend fun addLens(lens: StoreLensDocument)
}
