package com.peyess.salesapp.data.adapter.lenses

import android.net.Uri
import com.peyess.salesapp.data.adapter.disponibilities.toDisponibilityDocument
import com.peyess.salesapp.data.adapter.lenses.alt_height.toStoreLensAltHeightDocument
import com.peyess.salesapp.data.adapter.lenses.colorings.toStoreColoringAdapter
import com.peyess.salesapp.data.adapter.lenses.material_type.toStoreLensMaterialTypeDocument
import com.peyess.salesapp.data.adapter.lenses.treatments.toStoreLensTreatmentDocument
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument
import com.peyess.salesapp.data.model.lens.type_category.LensTypeCategoryDocument

fun FSStoreLocalLens.toStoreLensDocument(): StoreLensDocument {
    return StoreLensDocument(
        id = id,
        storeId = storeId,

        priority = priority,
        brandPriority = brandPriority,
        designPriority = designPriority,
        materialPriority = materialPriority,
        techPriority = techPriority,
        specialtyPriority = specialtyPriority,
        typePriority = typePriority,
        groupPriority = groupPriority,
        supplierPriority = supplierPriority,
        categoryPriority = categoryPriority,
        materialCategoryPriority = materialCategoryPriority,
        storeLensPriority = storeLensPriority,
        storeBrandPriority = storeBrandPriority,
        storeDesignPriority = storeDesignPriority,
        storeMaterialPriority = storeMaterialPriority,
        storeTechPriority = storeTechPriority,
        storeSpecialtyPriority = storeSpecialtyPriority,
        storeTypePriority = storeTypePriority,
        storeGroupPriority = storeGroupPriority,
        storeSupplierPriority = storeSupplierPriority,
        storeCategoryPriority = storeCategoryPriority,
        storeMaterialCategoryPriority = storeMaterialCategoryPriority,

        bases = bases,

        disponibilities = disponibilities.map { it.toDisponibilityDocument() },
        height = height,

        group = group,
        groupId = groupId,

        specialty = specialty,
        specialtyId = specialtyId,

        tech = tech,
        techId = techId,

        type = type,
        typeId = typeId,

        typeCategories = typeCategories.map {
            StoreLensTypeCategoryDocument(id = it.key, name = it.value)
        },

        category = category,
        categoryId = categoryId,

        material = material,
        materialN = materialN,
        materialId = materialId,
        materialObservation = materialObservation,
        materialCategory = materialCategory,
        materialCategoryId = materialCategoryId,
        materialTypes = materialTypes.map { it.value.toStoreLensMaterialTypeDocument(it.key) },
        materialTypesIds = materialTypesIds,

        needsCheck = needsCheck,
        hasAddition = hasAddition,
        hasFilterBlue = hasFilterBlue,
        hasFilterUv = hasFilterUv,
        isColoringTreatmentMutex = isColoringTreatmentMutex,
        isColoringDiscounted = isColoringDiscounted,
        isColoringIncluded = isColoringIncluded,
        isTreatmentDiscounted = isTreatmentDiscounted,
        isTreatmentIncluded = isTreatmentIncluded,
        isGeneric = isGeneric,

        altHeights = altHeights.map { it.value.toStoreLensAltHeightDocument(it.key) },

        defaultTreatment = defaultTreatment,
        treatments = treatments.map { it.value.toStoreLensTreatmentDocument(it.key, supplierId) },

        colorings = colorings.map { it.value.toStoreColoringAdapter(it.key, supplierId) },

        costAddColoring = costAddColoring,
        costAddTreatment = costAddTreatment,
        suggestedPriceAddColoring = suggestedPriceAddColoring,
        suggestedPriceAddTreatment = suggestedPriceAddTreatment,
        cost = cost,
        suggestedPrice = suggestedPrice,

        shippingTime = shippingTime,

        explanations = explanations,
        observation = observation,
        warning = warning,

        brand = brand,
        brandId = brandId,

        design = design,
        designId = designId,

        supplierPicture = Uri.parse(supplierPicture),
        supplier = supplier,
        supplierId = supplierId,

        isManufacturingLocal = isManufacturingLocal,
        isEnabled = isEnabled,
        reasonDisabled = reasonDisabled,
        isLocalEnabled = isLocalEnabled,
        reasonLocalDisabled = reasonLocalDisabled,

        price = price,
        priceAddColoring = priceAddColoring,
        priceAddTreatment = priceAddTreatment,

        releaseDate = releaseDate,

        docVersion = docVersion,
        isEditable = isEditable,
        created = created,
        updated = updated,
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}