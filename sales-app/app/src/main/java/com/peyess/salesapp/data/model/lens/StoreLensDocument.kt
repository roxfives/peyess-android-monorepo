package com.peyess.salesapp.data.model.lens

import android.net.Uri
import com.google.firebase.Timestamp
import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.coloring.StoreLensColoringDocument
import com.peyess.salesapp.data.model.lens.disponibility.DisponibilityDocument
import com.peyess.salesapp.data.model.lens.material_type.StoreLensMaterialTypeDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument
import com.peyess.salesapp.data.model.lens.treatment.StoreLensTreatmentDocument
import com.peyess.salesapp.data.model.lens.type_category.LensTypeCategoryDocument
import java.util.Date

data class StoreLensDocument(
    val id: String = "",
    val storeId: String = "",

    val priority: Double = 0.0,
    val brandPriority: Int = 0,
    val designPriority: Int = 0,
    val materialPriority: Int = 0,
    val techPriority: Int = 0,
    val specialtyPriority: Int = 0,
    val typePriority: Int = 0,
    val groupPriority: Int = 0,
    val supplierPriority: Int = 0,
    val categoryPriority: Int = 0,
    val materialCategoryPriority: Int = 0,
    val storeLensPriority: Int = 0,
    val storeBrandPriority: Int = 0,
    val storeDesignPriority: Int = 0,
    val storeMaterialPriority: Int = 0,
    val storeTechPriority: Int = 0,
    val storeSpecialtyPriority: Int = 0,
    val storeTypePriority: Int = 0,
    val storeGroupPriority: Int = 0,
    val storeSupplierPriority: Int = 0,
    val storeCategoryPriority: Int = 0,
    val storeMaterialCategoryPriority: Int = 0,

    val bases: List<Double> = emptyList(),

    val disponibilities: List<StoreLensDisponibilityDocument> = emptyList(),
    val height: Double = 0.0,

    val group: String = "",
    val groupId: String = "",

    val specialty: String = "",
    val specialtyId: String = "",

    val tech: String = "",
    val techId: String = "",

    val type: String = "",
    val typeId: String = "",
    val typeCategories: List<StoreLensTypeCategoryDocument> = emptyList(),

    val category: String = "",
    val categoryId: String = "",

    val material: String = "",
    val materialN: Double = 0.0,
    val materialId: String = "",
    val materialObservation: String = "",
    val materialCategory: String = "",
    val materialCategoryId: String = "",
    val materialTypes: List<StoreLensMaterialTypeDocument> = emptyList(),
    val materialTypesIds: List<String> = emptyList(),

    val needsCheck: Boolean = false,
    val hasAddition: Boolean = false,
    val hasFilterBlue: Boolean = false,
    val hasFilterUv: Boolean = false,
    val isColoringTreatmentMutex: Boolean = false,
    val isColoringDiscounted: Boolean = false,
    val isColoringIncluded: Boolean = false,
    val isTreatmentDiscounted: Boolean = false,
    val isTreatmentIncluded: Boolean = false,
    val isGeneric: Boolean = false,

    val altHeights: List<StoreLensAltHeightDocument> = emptyList(),

    val defaultTreatment: String = "",
    val treatments: List<StoreLensTreatmentDocument> = emptyList(),

    val colorings: List<StoreLensColoringDocument> = emptyList(),

    val costAddColoring: Double = 0.0,
    val costAddTreatment: Double = 0.0,
    val suggestedPriceAddColoring: Double = 0.0,
    val suggestedPriceAddTreatment: Double = 0.0,
    val cost: Double = 0.0,
    val suggestedPrice: Double = 0.0,

    val shippingTime: Double = 0.0,

    val explanations: List<String> = emptyList(),
    val observation: String = "",
    val warning: String = "",

    val brand: String = "",
    val brandId: String = "",

    val design: String = "",
    val designId: String = "",

    val supplierPicture: Uri = Uri.EMPTY,
    val supplier: String = "",
    val supplierId: String = "",

    val isManufacturingLocal: Boolean = false,
    val isEnabled: Boolean = false,
    val reasonDisabled: String = "",
    val isLocalEnabled: Boolean = true,
    val reasonLocalDisabled: String = "",

    val price: Double = 0.0,
    val priceAddColoring: Double = 0.0,
    val priceAddTreatment: Double = 0.0,

    val releaseDate: Date = Date(),

    val docVersion: Int = 0,
    val isEditable: Boolean = false,
    val created: Timestamp = Timestamp.now(),
    val updated: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
