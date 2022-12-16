package com.peyess.salesapp.data.adapter.lenses

import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
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

fun StoreLensDocument.toLocalLensEntity(): LocalLensEntity {
    return LocalLensEntity(
        id = id,
        isLocalEnabled = isLocalEnabled,
        reasonLocalDisabled = reasonLocalDisabled,
        price = price,
        priceAddColoring = priceAddColoring,
        priceAddTreatment = priceAddTreatment,
        height = height,
        groupId = groupId,
        specialtyId = specialtyId,
        techId = techId,
        typeId = typeId,
        categoryId = categoryId,
        materialId = materialId,
        hasAddition = hasAddition,
        hasFilterBlue = hasFilterBlue,
        hasFilterUv = hasFilterUv,
        isColoringTreatmentMutex = isColoringTreatmentMutex,
        isColoringDiscounted = isColoringDiscounted,
        isColoringIncluded = isColoringIncluded,
        isTreatmentDiscounted = isTreatmentDiscounted,
        isTreatmentIncluded = isTreatmentIncluded,
        priority = priority,
        isGeneric = isGeneric,
        shippingTime = shippingTime,
        observation = observation,
        warning = warning,
        brandId = brandId,
        designId = designId,
        supplierId = supplierId,
        isManufacturingLocal = isManufacturingLocal,
        isEnabled = isEnabled,
        reasonDisabled = reasonDisabled,
    )
}

fun StoreLensDocument.extractFamily(): LocalLensFamilyDocument {
    return LocalLensFamilyDocument(
        id = brandId,
        name = brand,
        priority = brandPriority,
        storePriority = storeBrandPriority,
    )
}

fun StoreLensDocument.extractDescription(): LocalLensDescriptionDocument {
    return LocalLensDescriptionDocument(
        id = designId,
        name = design,
        priority = designPriority,
        storePriority = storeDesignPriority,
    )
}

fun StoreLensDocument.extractSupplier(): LocalLensSupplierDocument {
    return LocalLensSupplierDocument(
        id = supplierId,
        name = supplier,
        picture = supplierPicture,
        priority = supplierPriority,
        storePriority = storeSupplierPriority,
    )
}

fun StoreLensDocument.extractGroup(): LocalLensGroupDocument {
    return LocalLensGroupDocument(
        id = groupId,
        name = group,
        priority = groupPriority,
        storePriority = storeGroupPriority,
    )
}

fun StoreLensDocument.extractSpecialty(): LocalLensSpecialtyDocument {
    return LocalLensSpecialtyDocument(
        id = specialtyId,
        name = specialty,
        priority = specialtyPriority,
        storePriority = storeSpecialtyPriority,
    )
}

fun StoreLensDocument.extractTech(): LocalLensTechDocument {
    return LocalLensTechDocument(
        id = techId,
        name = tech,
        priority = techPriority,
        storePriority = storeTechPriority,
    )
}

fun StoreLensDocument.extractType(): LocalLensTypeDocument {
    return LocalLensTypeDocument(
        id = typeId,
        name = type,
        priority = typePriority,
        storePriority = storeTypePriority,
    )
}

fun StoreLensDocument.extractCategory(): LocalLensCategoryDocument {
    return LocalLensCategoryDocument(
        id = categoryId,
        name = category,
        priority = categoryPriority,
        storePriority = storeCategoryPriority,
    )
}

fun StoreLensDocument.extractMaterial(): LocalLensMaterialDocument {
    return LocalLensMaterialDocument(
        id = materialId,
        name = material,
        priority = materialPriority,
        storePriority = storeMaterialPriority,
        n = materialN,
        observation = materialObservation,
        categoryId = materialCategoryId,
        category = materialCategory,
    )
}

fun StoreLensDocument.extractMaterialCategory(): LocalLensMaterialCategoryDocument {
    return LocalLensMaterialCategoryDocument(
        id = materialCategoryId,
        name = materialCategory,
        priority = materialCategoryPriority,
        storePriority = storeMaterialCategoryPriority,
    )
}
