package com.peyess.salesapp.data.repository.lenses.room

import com.peyess.salesapp.data.adapter.lenses.room.coloring.toLocalLensColoringEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensCategoryEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensDescriptionEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensFamilyEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensGroupEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensMaterialCategoryEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensMaterialEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensSpecialtyEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensSupplierEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensTechEntity
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensTypeEntity
import com.peyess.salesapp.data.adapter.lenses.room.treatment.toLocalLensTreatmentEntity
import com.peyess.salesapp.data.adapter.lenses.toLocalLensEntity
import com.peyess.salesapp.data.dao.lenses.room.LocalLensDao
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringExplanationEntity
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

    override suspend fun addType(type: LocalLensTypeDocument) {
        val entity = type.toLocalLensTypeEntity()

        localLensDao.addType(entity)
    }

    override suspend fun addCategory(category: LocalLensCategoryDocument) {
        val entity = category.toLocalLensCategoryEntity()

        localLensDao.addLensCategory(entity)
    }

    override suspend fun addMaterial(material: LocalLensMaterialDocument) {
        val entity = material.toLocalLensMaterialEntity()

        localLensDao.addMaterial(entity)
    }

    override suspend fun addMaterialCategory(materialCategory: LocalLensMaterialCategoryDocument) {
        val entity = materialCategory.toLocalLensMaterialCategoryEntity()

        localLensDao.addMaterialCategory(entity)
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

    override suspend fun addLens(lens: StoreLensDocument) {
        localLensDao.addLens(lens.toLocalLensEntity())
    }
}