package com.peyess.salesapp.data.repository.lenses.room

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

interface LocalLensesRepository {
    fun addFamily(family: LocalLensFamilyDocument)

    fun addDescription(description: LocalLensDescriptionDocument)

    fun addSupplier(supplier: LocalLensSupplierDocument)

    fun addGroup(group: LocalLensGroupDocument)

    fun addSpecialty(specialty: LocalLensSpecialtyDocument)

    fun addTech(tech: LocalLensTechDocument)

    fun addType(type: LocalLensTypeDocument)

    fun addCategory(category: LocalLensCategoryDocument)

    fun addMaterial(material: LocalLensMaterialDocument)

    fun addMaterialCategory(materialCategory: LocalLensMaterialCategoryDocument)
}
