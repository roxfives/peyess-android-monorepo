package com.peyess.salesapp.data.repository.lenses.room

import androidx.paging.PagingSource
import arrow.core.Either
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.material_type.StoreLensMaterialTypeDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesMaterialSimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesTechSimplified
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
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensGroupDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDescriptionDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensFamilyDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensMaterialDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSpecialtyDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSupplierDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument
import com.peyess.salesapp.data.utils.query.PeyessQuery


typealias LensesTypesResponse = Either<LocalLensRepositoryException, List<StoreLensTypeDocument>>

typealias LensesTypeCategoriesResponse =
        Either<LocalLensRepositoryException, List<StoreLensTypeCategoryDocument>>

typealias LensesTypeCategoryResponse =
        Either<LocalLensRepositoryException, StoreLensTypeCategoryDocument>

typealias LensesSuppliersResponse =
        Either<LocalLensRepositoryException, List<StoreLensSupplierDocument>>

typealias LensesFamiliesResponse =
        Either<LocalLensRepositoryException, List<StoreLensFamilyDocument>>

typealias LensesDescriptionsResponse =
        Either<LocalLensRepositoryException, List<StoreLensDescriptionDocument>>

typealias LensesMaterialsResponse =
        Either<LocalLensRepositoryException, List<StoreLensMaterialDocument>>

typealias LensesSpecialtiesResponse =
        Either<LocalLensRepositoryException, List<StoreLensSpecialtyDocument>>

typealias LensesGroupsResponse =
        Either<LocalLensRepositoryException, List<StoreLensGroupDocument>>

typealias LensesResponse =
        Either<LocalLensRepositoryException, () -> PagingSource<Int, StoreLensWithDetailsDocument>>

typealias LensFilteredByDisponibilitiesResponse = Either<
        LocalLensRepositoryException,
        StoreLensWithDetailsDocument?,
    >

typealias ColoringsResponse =
        Either<LocalLensRepositoryException, List<LocalLensColoringDocument>>

typealias SingleColoringResponse =
        Either<LocalLensRepositoryException, LocalLensColoringDocument>

typealias TreatmentsResponse =
        Either<LocalLensRepositoryException, List<LocalLensTreatmentDocument>>

typealias SingleTreatmentResponse =
        Either<LocalLensRepositoryException, LocalLensTreatmentDocument>

typealias SingleLensResponse = Either<LocalLensRepositoryException, StoreLensWithDetailsDocument>

typealias TechsResponse = Either<LocalLensRepositoryException, List<LocalLensesTechSimplified>>

typealias MaterialsResponse = Either<LocalLensRepositoryException, List<LocalLensesMaterialSimplified>>

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

    suspend fun addMaterialType(materialType: StoreLensMaterialTypeDocument)

    suspend fun addTypeToMaterial(typeId: String, materialId: String)

    suspend fun addMaterialCategory(materialCategory: LocalLensMaterialCategoryDocument)

    suspend fun addColoring(coloring: LocalLensColoringDocument)

    suspend fun addColoringToLens(coloringId: String, lensId: String)

    suspend fun getColoringsForLens(lensId: String): ColoringsResponse

    suspend fun getColoringById(coloringId: String): SingleColoringResponse

    suspend fun addTreatment(treatment: LocalLensTreatmentDocument)

    suspend fun addTreatmentToLens(treatmentId: String, lensId: String)

    suspend fun getTreatmentsForLens(lensId: String): TreatmentsResponse

    suspend fun getTreatmentById(treatmentId: String): SingleTreatmentResponse

    suspend fun addAlternativeHeight(alternativeHeight: StoreLensAltHeightDocument)

    suspend fun addAlternativeHeightToLens(alternativeHeightId: String, lensId: String)

    suspend fun addLens(lens: StoreLensDocument)

    suspend fun lensTypeCategoryById(categoryId: String): LensesTypeCategoryResponse

    suspend fun lensTypeCategories(): LensesTypeCategoriesResponse

    suspend fun paginateLensesWithDetailsOnly(query: PeyessQuery = PeyessQuery()): LensesResponse

    suspend fun getLensFilteredByDisponibility(
        query: PeyessQuery = PeyessQuery(),
    ): LensFilteredByDisponibilitiesResponse

    suspend fun getLensById(id: String): SingleLensResponse

    suspend fun getFilteredTypes(query: PeyessQuery = PeyessQuery()): LensesTypesResponse

    suspend fun getFilteredSuppliers(query: PeyessQuery = PeyessQuery()): LensesSuppliersResponse

    suspend fun getFilteredFamilies(query: PeyessQuery = PeyessQuery()): LensesFamiliesResponse

    suspend fun getFilteredDescriptions(query: PeyessQuery = PeyessQuery()): LensesDescriptionsResponse

    suspend fun getFilteredMaterials(query: PeyessQuery = PeyessQuery()): LensesMaterialsResponse

    suspend fun getFilteredSpecialties(query: PeyessQuery = PeyessQuery()): LensesSpecialtiesResponse

    suspend fun getFilteredGroups(query: PeyessQuery = PeyessQuery()): LensesGroupsResponse

    suspend fun getTechsFilteredByDisponibilities(query: PeyessQuery): TechsResponse

    suspend fun getMaterialsFilteredByDisponibilities(query: PeyessQuery): MaterialsResponse
}
