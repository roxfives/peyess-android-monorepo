package com.peyess.salesapp.data.repository.lenses.room

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
import com.peyess.salesapp.data.dao.lenses.room.LocalLensCategoryDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensDescriptionDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensFamilyDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensGroupDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensMaterialCategoryDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensMaterialDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensSpecialtyDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensSupplierDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensTechDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensTypeDao
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
import javax.inject.Inject

class LocalLensesRepositoryImpl @Inject constructor(
    private val localLensFamilyDao: LocalLensFamilyDao,
    private val localLensDescriptionDao: LocalLensDescriptionDao,
    private val localLensSupplierDao: LocalLensSupplierDao,
    private val localLensGroupDao: LocalLensGroupDao,
    private val localLensSpecialtyDao: LocalLensSpecialtyDao,
    private val localLensTechDao: LocalLensTechDao,
    private val localLensTypeDao: LocalLensTypeDao,
    private val localLensCategoryDao: LocalLensCategoryDao,
    private val localLensMaterialDao: LocalLensMaterialDao,
    private val localLensMaterialCategoryDao: LocalLensMaterialCategoryDao,
): LocalLensesRepository {
    override fun addFamily(family: LocalLensFamilyDocument) {
        val entity = family.toLocalLensFamilyEntity()

        localLensFamilyDao.add(entity)
    }

    override fun addDescription(description: LocalLensDescriptionDocument) {
        val entity = description.toLocalLensDescriptionEntity()

        localLensDescriptionDao.add(entity)
    }

    override fun addSupplier(supplier: LocalLensSupplierDocument) {
        val entity = supplier.toLocalLensSupplierEntity()

        localLensSupplierDao.add(entity)
    }

    override fun addGroup(group: LocalLensGroupDocument) {
        val entity = group.toLocalLensGroupEntity()

        localLensGroupDao.add(entity)
    }

    override fun addSpecialty(specialty: LocalLensSpecialtyDocument) {
        val entity = specialty.toLocalLensSpecialtyEntity()

        localLensSpecialtyDao.add(entity)
    }

    override fun addTech(tech: LocalLensTechDocument) {
        val entity = tech.toLocalLensTechEntity()

        localLensTechDao.add(entity)
    }

    override fun addType(type: LocalLensTypeDocument) {
        val entity = type.toLocalLensTypeEntity()

        localLensTypeDao.add(entity)
    }

    override fun addCategory(category: LocalLensCategoryDocument) {
        val entity = category.toLocalLensCategoryEntity()

        localLensCategoryDao.add(entity)
    }

    override fun addMaterial(material: LocalLensMaterialDocument) {
        val entity = material.toLocalLensMaterialEntity()

        localLensMaterialDao.add(entity)
    }

    override fun addMaterialCategory(materialCategory: LocalLensMaterialCategoryDocument) {
        val entity = materialCategory.toLocalLensMaterialCategoryEntity()

        localLensMaterialCategoryDao.add(entity)
    }
}