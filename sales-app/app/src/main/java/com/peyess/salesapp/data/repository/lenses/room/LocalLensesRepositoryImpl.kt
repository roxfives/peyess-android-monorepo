package com.peyess.salesapp.data.repository.lenses.room

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
import com.peyess.salesapp.data.adapter.lenses.room.treatment.toLocalLensTreatmentEntity
import com.peyess.salesapp.data.adapter.lenses.toLocalLensEntity
import com.peyess.salesapp.data.adapter.lenses.toLocalLensMaterialTypeEntity
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
import timber.log.Timber
import javax.inject.Inject

class LocalLensesRepositoryImpl @Inject constructor(
    private val localLensDao: LocalLensDao,
): LocalLensesRepository {
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

    override suspend fun addMaterialType(type: StoreLensMaterialTypeDocument) {
        val entity = type.toLocalLensMaterialTypeEntity()

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

    override suspend fun addColoringToLens(coloringId: String, lensId: String) {
        localLensDao.addLensColoringCrossRef(
            LocalLensColoringCrossRef(
                lensId = lensId,
                coloringId = coloringId,
            )
        )
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

    override suspend fun addTreatmentToLens(treatmentId: String, lensId: String) {
        localLensDao.addLensTreatmentCrossRef(
            LocalLensTreatmentCrossRef(
                lensId = lensId,
                treatmentId = treatmentId,
            )
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
}
