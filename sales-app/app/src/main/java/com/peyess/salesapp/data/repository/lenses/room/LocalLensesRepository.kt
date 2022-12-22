package com.peyess.salesapp.data.repository.lenses.room

import arrow.core.Either
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.material_type.StoreLensMaterialTypeDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensWithDetails
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
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument
import com.peyess.salesapp.data.utils.query.PeyessQuery


typealias LensesResponse = Either<LocalLensRepositoryException, List<LocalLensWithDetails>>

interface LocalLensesRepository {
    suspend fun addFamily(family: LocalLensFamilyDocument)

    suspend fun addDescription(description: LocalLensDescriptionDocument)

    suspend fun addSupplier(supplier: LocalLensSupplierDocument)

    suspend fun addGroup(group: LocalLensGroupDocument)

    suspend fun addSpecialty(specialty: LocalLensSpecialtyDocument)

    suspend fun addTech(tech: LocalLensTechDocument)

    suspend fun addLensType(type: LocalLensTypeDocument)

    suspend fun addLensTypeCategory(category: StoreLensTypeCategoryDocument)

    suspend fun addCategoryToType(categoryId: String, typeId: String)

    suspend fun addCategory(category: LocalLensCategoryDocument)

    suspend fun addMaterial(material: LocalLensMaterialDocument)

    suspend fun addMaterialType(material: StoreLensMaterialTypeDocument)

    suspend fun addTypeToMaterial(typeId: String, materialId: String)

    suspend fun addMaterialCategory(materialCategory: LocalLensMaterialCategoryDocument)

    suspend fun addColoring(coloring: LocalLensColoringDocument)

    suspend fun addColoringToLens(coloringId: String, lensId: String)

    suspend fun addTreatment(treatment: LocalLensTreatmentDocument)

    suspend fun addTreatmentToLens(treatmentId: String, lensId: String)

    suspend fun addAlternativeHeight(alternativeHeight: StoreLensAltHeightDocument)

    suspend fun addAlternativeHeightToLens(alternativeHeightId: String, lensId: String)

    suspend fun addLens(lens: StoreLensDocument)

    suspend fun getUnrestrictedLensesWithDetailsOnly(query: PeyessQuery = PeyessQuery()): LensesResponse
}
