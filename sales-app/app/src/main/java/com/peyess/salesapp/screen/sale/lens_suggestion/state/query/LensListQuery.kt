package com.peyess.salesapp.screen.sale.lens_suggestion.state.query

import com.peyess.salesapp.data.repository.lenses.room.LocalLensesQueryFields
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.feature.lens_suggestion.model.LensListFilter


fun buildLensListQueryFields(filter: LensListFilter): List<PeyessQueryField> {
    val queryFields = mutableListOf<PeyessQueryField>()

    if (filter.lensTypeId.isNotBlank()) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensTypeId.name(),
                op = PeyessQueryOperation.Equal,
                value = filter.lensTypeId,
            )
        )
    }

    if (filter.supplierId.isNotBlank()) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensSupplierId.name(),
                op = PeyessQueryOperation.Equal,
                value = filter.supplierId,
            )
        )
    }

    if (filter.familyId.isNotBlank()) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensFamilyId.name(),
                op = PeyessQueryOperation.Equal,
                value = filter.familyId,
            )
        )
    }

    if (filter.descriptionId.isNotBlank()) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensDescriptionId.name(),
                op = PeyessQueryOperation.Equal,
                value = filter.descriptionId,
            )
        )
    }

    if (filter.materialId.isNotBlank()) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensMaterialId.name(),
                op = PeyessQueryOperation.Equal,
                value = filter.materialId,
            )
        )
    }

    if (filter.specialtyId.isNotBlank()) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensSpecialtyId.name(),
                op = PeyessQueryOperation.Equal,
                value = filter.specialtyId,
            )
        )
    }

    if (filter.groupId.isNotBlank()) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensGroupId.name(),
                op = PeyessQueryOperation.Equal,
                value = filter.groupId,
            )
        )
    }

    if (filter.withFilterBlue) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensBlueLightFilter.name(),
                op = PeyessQueryOperation.Equal,
                value = 1,
            )
        )
    }

    if (filter.withFilterUv) {
        queryFields.add(
            buildQueryField(
                field = LocalLensesQueryFields.LensUVLightFilter.name(),
                op = PeyessQueryOperation.Equal,
                value = 1,
            )
        )
    }

    return queryFields
}

fun buildLensListQueryOrderBy(): List<PeyessOrderBy> {
    return listOf(
        PeyessOrderBy(
            field = LocalLensesQueryFields.LensSupplierPriority.name(),
            order = Order.ASCENDING,
        ),
        PeyessOrderBy(
            field = LocalLensesQueryFields.LensSupplier.name(),
            order = Order.ASCENDING,
        ),
        PeyessOrderBy(
            field = LocalLensesQueryFields.LensTypePriority.name(),
            order = Order.ASCENDING,
        ),
        PeyessOrderBy(
            field = LocalLensesQueryFields.LensType.name(),
            order = Order.ASCENDING,
        ),
        PeyessOrderBy(
            field = LocalLensesQueryFields.LensMaterialPriority.name(),
            order = Order.ASCENDING,
        ),
        PeyessOrderBy(
            field = LocalLensesQueryFields.LensMaterial.name(),
            order = Order.ASCENDING,
        ),
    )
}