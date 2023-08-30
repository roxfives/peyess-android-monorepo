package com.peyess.salesapp.data.repository.lenses.room

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.data.adapter.lenses.lens_type_category.toStoreLensTypeCategoryDocument
import com.peyess.salesapp.data.adapter.lenses.room.coloring.toLocalLensColoringDocument
import com.peyess.salesapp.data.adapter.lenses.room.coloring.toLocalLensColoringEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensAltHeight
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensCategoryEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensDescriptionEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensDisponibilityEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensDisponibilityManufacturerEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensFamilyEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensGroupEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensMaterialCategoryEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensMaterialEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensSpecialtyEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensSupplierEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensTechEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensTypeCategoryEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensTypeEntity
import com.peyess.salesapp.data.adapter.lenses.room.toStoreLensGroupDocument
import com.peyess.salesapp.data.adapter.lenses.room.toStoreLensDescriptionDocument
import com.peyess.salesapp.data.adapter.lenses.room.toStoreLensFamilyDocument
import com.peyess.salesapp.data.adapter.lenses.room.toStoreLensMaterialDocument
import com.peyess.salesapp.data.adapter.lenses.room.toStoreLensSpecialtyDocument
import com.peyess.salesapp.data.adapter.lenses.room.toStoreLensSupplierDocument
import com.peyess.salesapp.data.adapter.lenses.room.toStoreLensTypeDocument
import com.peyess.salesapp.data.adapter.lenses.room.treatment.toLocalLensTreatmentDocument
import com.peyess.salesapp.data.adapter.lenses.room.treatment.toLocalLensTreatmentEntity
import com.peyess.salesapp.data.adapter.lenses.toLocalLensEntity
import com.peyess.salesapp.data.adapter.lenses.toLocalLensMaterialTypeEntity
import com.peyess.salesapp.data.adapter.lenses.toStoreLensWithDetailsDocument
import com.peyess.salesapp.data.dao.lenses.room.LocalLensDao
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.material_type.StoreLensMaterialTypeDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensAltHeightCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensColoringCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDetailsCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDisponibilityManufacturerCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensMaterialTypeCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensTreatmentCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensTypeCategoryCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensFullUnionWithHeightAndLensTypeDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensWithDetailsDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesDescriptionDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesFamilyDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesGroupDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesMaterialDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesSpecialtyDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesSupplierDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesTypeDBView
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
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentExplanationEntity
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryFunctionOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryMinMaxField
import com.peyess.salesapp.data.utils.query.adapter.toSqlQuery
import com.peyess.salesapp.utils.room.MappingPagingSource
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class LocalLensesRepositoryImpl @Inject constructor(
    private val localLensDao: LocalLensDao,
): LocalLensesRepository {
    override suspend fun totalLenses(): TotalLensesResponse = Either.catch {
        localLensDao.totalLenses()
    }.mapLeft {
        val errorMessage = "Error while fetching total lenses: ${it.message}"
        Timber.e(errorMessage, it)

        Unexpected(errorMessage, it)
    }

    override suspend fun addFamily(family: LocalLensFamilyDocument) {
        val entity = family.toLocalLensFamilyEntity()

        localLensDao.addFamily(entity)
    }

    override suspend fun addDescription(description: LocalLensDescriptionDocument) {
        val entity = description.toLocalLensDescriptionEntity()

        localLensDao.addDescription(entity)
    }

    override suspend fun addSupplier(supplier: LocalLensSupplierDocument) {
        val entity = supplier.toLocalLensSupplierEntity()

        localLensDao.addSupplier(entity)
    }

    override suspend fun addGroup(group: LocalLensGroupDocument) {
        val entity = group.toLocalLensGroupEntity()

        localLensDao.addGroup(entity)
    }

    override suspend fun addSpecialty(specialty: LocalLensSpecialtyDocument) {
        val entity = specialty.toLocalLensSpecialtyEntity()

        localLensDao.addSpecialty(entity)
    }

    override suspend fun addTech(tech: LocalLensTechDocument) {
        val entity = tech.toLocalLensTechEntity()

        localLensDao.addTech(entity)
    }

    override suspend fun addLensType(type: LocalLensTypeDocument) {
        val entity = type.toLocalLensTypeEntity()

        localLensDao.addType(entity)
    }

    override suspend fun addLensTypeCategory(category: StoreLensTypeCategoryDocument) {
        val entity = category.toLocalLensTypeCategoryEntity()

        localLensDao.addTypeCategory(entity)
    }

    override suspend fun addCategoryToType(categoryId: String, typeId: String) {
        localLensDao.addTypeCategoryCrossRef(
            LocalLensTypeCategoryCrossRef(
                categoryId = categoryId,
                lensTypeId = typeId,
            )
        )
    }

    override suspend fun addCategory(category: LocalLensCategoryDocument) {
        val entity = category.toLocalLensCategoryEntity()

        localLensDao.addLensCategory(entity)
    }

    override suspend fun addMaterial(material: LocalLensMaterialDocument) {
        val entity = material.toLocalLensMaterialEntity()

        localLensDao.addMaterial(entity)
    }

    override suspend fun addMaterialType(materialType: StoreLensMaterialTypeDocument) {
        val entity = materialType.toLocalLensMaterialTypeEntity()

        localLensDao.addMaterialType(entity)
    }

    override suspend fun addTypeToMaterial(typeId: String, materialId: String) {
        localLensDao.addMaterialTypeCrossRef(
            LocalLensMaterialTypeCrossRef(
                materialId = materialId,
                materialTypeId = typeId,
            )
        )
    }

    override suspend fun addMaterialCategory(materialCategory: LocalLensMaterialCategoryDocument) {
        val entity = materialCategory.toLocalLensMaterialCategoryEntity()

        localLensDao.addMaterialCategory(entity)
    }

    private suspend fun addDisponibilityForLens(
        lensId: String,
        disponibility: StoreLensDisponibilityDocument,
    ) {
        val entity = disponibility.toLocalLensDisponibilityEntity(lensId)
        val disponibilityId = localLensDao.addLensDisponibility(entity)

        disponibility.manufacturers.forEach {
            localLensDao.addLensDisponibilityManufacturer(
                it.toLocalLensDisponibilityManufacturerEntity()
            )

            localLensDao.addLensDisponibilityManufacturerCrossRef(
                LocalLensDisponibilityManufacturerCrossRef(
                    dispId = disponibilityId,
                    manufacturerId = it.id,
                )
            )
        }
    }

    override suspend fun addColoring(coloring: LocalLensColoringDocument) {
        val coloringExplanations = coloring.explanations
        val coloringEntity = coloring.toLocalLensColoringEntity()

        localLensDao.addLensColoring(coloringEntity)
        coloringExplanations.forEach {
            Timber.i("Adding explanation for coloring ${coloring.id}")
            localLensDao.addLensColoringExp(
                LocalLensColoringExplanationEntity(
                    coloringId = coloring.id,
                    explanation = it,
                )
            )
        }
    }

    override suspend fun addColoringToLens(coloringId: String, lensId: String, price: BigDecimal) {
        localLensDao.addLensColoringCrossRef(
            LocalLensColoringCrossRef(
                lensId = lensId,
                coloringId = coloringId,
                price = price.toDouble(),
            )
        )
    }

    override suspend fun getColoringById(
        lensId: String,
        coloringId: String,
    ): SingleColoringResponse = Either.catch {
        val entity = localLensDao.getColoringById(lensId, coloringId)

        entity?.coloring
            ?.toLocalLensColoringDocument(
                explanations = entity.explanations.map { it.explanation },
            )
    }.mapLeft {
        Unexpected(
            description = "Error while getting coloring by id $coloringId: ${it.message}",
            error = it,
        )
    }.leftIfNull {
        Unexpected("Coloring with id $coloringId not found")
    }

    override suspend fun addTreatment(treatment: LocalLensTreatmentDocument) {
        val treatmentExplanations = treatment.explanations
        val treatmentEntity = treatment.toLocalLensTreatmentEntity()

        localLensDao.addLensTreatment(treatmentEntity)
        treatmentExplanations.forEach {
            Timber.i("Adding explanation for treatment ${treatment.id}")
            localLensDao.addLensTreatmentExp(
                LocalLensTreatmentExplanationEntity(
                    treatmentId = treatment.id,
                    explanation = it,
                )
            )
        }
    }

    override suspend fun addTreatmentToLens(treatmentId: String, lensId: String, price: BigDecimal) {
        localLensDao.addLensTreatmentCrossRef(
            LocalLensTreatmentCrossRef(
                lensId = lensId,
                treatmentId = treatmentId,
                price = price.toDouble(),
            )
        )
    }

    override suspend fun getTreatmentsForLens(lensId: String): TreatmentsResponse = Either.catch {
        localLensDao.getTreatmentsForLens(lensId)
            .map {
                it.treatment
                    .toLocalLensTreatmentDocument(
                        explanations = it.explanations.map { entity ->
                            entity.explanation
                        }
                    )
            }
    }.mapLeft {
        Unexpected(
            description = "Error while getting treatments for lens $lensId: ${it.message}",
            error = it,
        )
    }

    override suspend fun getTreatmentById(
        lensId: String,
        treatmentId: String,
    ): SingleTreatmentResponse = Either.catch {
        val entity = localLensDao.getTreatmentById(lensId, treatmentId)

        entity?.treatment
            ?.toLocalLensTreatmentDocument(
                explanations = entity.explanations.map { exp ->
                    exp.explanation
                }
            )
    }.mapLeft {
        Unexpected(
            description = "Error while getting treatment $treatmentId: ${it.message}",
            error = it,
        )
    }.leftIfNull {
        Unexpected(
            description = "Treatment $treatmentId not found",
        )
    }

    override suspend fun getColoringsForLens(lensId: String): ColoringsResponse = Either.catch {
        localLensDao.getColoringsForLens(lensId)
            .map {
                it.coloring
                    .toLocalLensColoringDocument(
                        explanations = it.explanations.map { entity ->
                            entity.explanation
                        }
                    )
            }
    }.mapLeft {
        Unexpected(
            description = "Error while getting colorings for lens $lensId: ${it.message}",
            error = it,
        )
    }

    override suspend fun addAlternativeHeight(
        alternativeHeight: StoreLensAltHeightDocument,
    ) {
        val entity = alternativeHeight.toLocalLensAltHeight()

        localLensDao.addLensAltHeight(entity)
    }

    override suspend fun addAlternativeHeightToLens(
        alternativeHeightId: String,
        lensId: String,
    ) {
        localLensDao.addLensAltHeightCrossRef(
            LocalLensAltHeightCrossRef(
                lensId = lensId,
                altHeightId = alternativeHeightId,
            )
        )
    }

    override suspend fun addLens(lens: StoreLensDocument) {
        localLensDao.addLens(lens.toLocalLensEntity())

        lens.explanations.forEach {
            localLensDao.addLensExplanation(
                LocalLensExplanationEntity(
                    lensId = lens.id,
                    explanation = it,
                )
            )
        }

        lens.disponibilities.forEach {
            addDisponibilityForLens(lens.id, it)
        }

        localLensDao.addLensDetails(
            LocalLensDetailsCrossRef(
                brandId = lens.brandId,
                designId = lens.designId,
                supplierId = lens.supplierId,
                groupId = lens.groupId,
                specialtyId = lens.specialtyId,
                techId = lens.techId,
                typeId = lens.typeId,
                categoryId = lens.categoryId,
                materialId = lens.materialId,
            )
        )
    }

    override suspend fun lensTypeCategoryById(
        categoryId: String,
    ): LensesTypeCategoryResponse = Either.catch {
        localLensDao.lensTypeCategoryById(categoryId)?.toStoreLensTypeCategoryDocument()
    }.mapLeft {
        Unexpected(
            description = "Error while fetching lens type category by id $categoryId",
            error = it,
        )
    }.leftIfNull {
        Unexpected(description = "Lens type category with id $categoryId not found")
    }

    override suspend fun lensTypeCategories(): LensesTypeCategoriesResponse = Either.catch {
        localLensDao.lensTypeCategories().map { it.toStoreLensTypeCategoryDocument() }
    }.mapLeft {
        Unexpected(
            description = "Error while fetching lens type categories",
            error = it,
        )
    }

    override suspend fun paginateLensesWithDetailsOnly(query: PeyessQuery): LensesResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = "SELECT * $aggregatesStr FROM ${LocalLensWithDetailsDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            val pagingSourceFactory = {
                MappingPagingSource(
                    originalSource = localLensDao.getFilteredLenses(sqlQuery),
                    mapper = { it.toStoreLensWithDetailsDocument() }
                )
            }

            pagingSourceFactory
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getLensFilteredByDisponibility(
        query: PeyessQuery,
    ): LensFilteredByDisponibilitiesResponse = Either.catch {
        val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
        val aggregatesStr = if (aggregates.isEmpty()) {
            ""
        } else {
            ", " + buildMinMaxAggregates(aggregates)
        }

        val selectStatement = "SELECT * $aggregatesStr " +
                "FROM ${LocalLensFullUnionWithHeightAndLensTypeDBView.viewName}"
        val sqlQuery = query
            .copy(withLimit = 1)
            .toSqlQuery(selectStatement)

        val response = localLensDao
            .getLensFilteredWithDisponibilities(sqlQuery)
            ?.toStoreLensWithDetailsDocument()

        response
    }.mapLeft {
        Unexpected(
            description = "Unexpected error: ${it.message}",
            error = it,
        )
    }

    override suspend fun getLensById(id: String): SingleLensResponse = Either.catch {
        val entity = localLensDao.getLensById(id)

        entity?.toStoreLensWithDetailsDocument()
    }.mapLeft {
        Unexpected(
            description = "Unexpected error: ${it.message}",
            error = it,
        )
    }.leftIfNull {
        LensNotFound(
            "Lens with id $id not found",
        )
    }

    override suspend fun getFilteredTypes(query: PeyessQuery): LensesTypesResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = "SELECT DISTINCT id, name $aggregatesStr " +
                    "FROM ${LocalLensesTypeDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao
                .getFilteredTypes(sqlQuery)
                .map { it.toStoreLensTypeDocument() }
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getFilteredSuppliers(query: PeyessQuery): LensesSuppliersResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = "SELECT DISTINCT id, name $aggregatesStr " +
                    "FROM ${LocalLensesSupplierDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao
                .getFilteredSuppliers(sqlQuery)
                .map { it.toStoreLensSupplierDocument() }
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getFilteredFamilies(query: PeyessQuery): LensesFamiliesResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = "SELECT DISTINCT id, name $aggregatesStr " +
                    "FROM ${LocalLensesFamilyDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao
                .getFilteredFamilies(sqlQuery)
                .map { it.toStoreLensFamilyDocument() }
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getFilteredDescriptions(query: PeyessQuery): LensesDescriptionsResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = "SELECT DISTINCT id, name $aggregatesStr " +
                    "FROM ${LocalLensesDescriptionDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao
                .getFilteredDescriptions(sqlQuery)
                .map { it.toStoreLensDescriptionDocument() }
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getFilteredMaterials(query: PeyessQuery): LensesMaterialsResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }
            val selectStatement = "SELECT DISTINCT id, name $aggregatesStr " +
                    "FROM ${LocalLensesMaterialDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao
                .getFilteredMaterials(sqlQuery)
                .map { it.toStoreLensMaterialDocument() }
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getFilteredSpecialties(query: PeyessQuery): LensesSpecialtiesResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = "SELECT DISTINCT id, name $aggregatesStr " +
                    "FROM ${LocalLensesSpecialtyDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao
                .getFilteredSpecialties(sqlQuery)
                .map { it.toStoreLensSpecialtyDocument() }
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getFilteredGroups(query: PeyessQuery): LensesGroupsResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = "SELECT DISTINCT id, name $aggregatesStr " +
                    "FROM ${LocalLensesGroupDBView.viewName}"
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao
                .getFilteredGroups(sqlQuery)
                .map { it.toStoreLensGroupDocument() }
        }.mapLeft {
            Unexpected(
                description = "Unexpected error: ${it.message}",
                error = it,
            )
        }

    override suspend fun getTechsFilteredByDisponibilities(query: PeyessQuery): TechsResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = """
                SELECT
                    techId as id,
                    techName as name
                    $aggregatesStr
                FROM ${LocalLensFullUnionWithHeightAndLensTypeDBView.viewName}
            """.trimIndent()
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao.lensTechsFilteredByDisponibility(sqlQuery)
        }.mapLeft {
            Unexpected(
                description = "Unexpected error while fetching tech: ${it.message}",
                error = it,
            )
        }

    override suspend fun getMaterialsFilteredByDisponibilities(query: PeyessQuery): MaterialsResponse =
        Either.catch {
            val aggregates = query.queryFields.filterIsInstance<PeyessQueryMinMaxField>()
            val aggregatesStr = if (aggregates.isEmpty()) {
                ""
            } else {
                ", " + buildMinMaxAggregates(aggregates)
            }

            val selectStatement = """
                SELECT
                    materialId as id,
                    materialName as name
                    $aggregatesStr
                FROM ${LocalLensFullUnionWithHeightAndLensTypeDBView.viewName}
            """.trimIndent()
            val sqlQuery = query.toSqlQuery(selectStatement)

            localLensDao.lensMaterialsFilteredByDisponibility(sqlQuery)
        }.mapLeft {
            Unexpected(
                description = "Unexpected error while fetching material: ${it.message}",
                error = it,
            )
        }

    private fun buildMinMaxAggregates(fields: List<PeyessQueryMinMaxField>): String {
        return fields.joinToString(separator = ", ") {
            when (it.function) {
                is PeyessQueryFunctionOperation.MIN ->
                    "MIN(${it.field}) as __${it.field}__"
                is PeyessQueryFunctionOperation.MAX ->
                    "MAX(${it.field}) as __${it.field}__"
            }
        }
    }
}
