package com.peyess.salesapp.feature.sale.lens_pick.state.query

import com.peyess.salesapp.data.repository.lenses.room.SimplifiedQueryFields
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.feature.sale.lens_pick.constant.ListFilter
import com.peyess.salesapp.feature.sale.lens_pick.model.LensListFilter

private fun shouldFilterBySupplier(filter: ListFilter): Boolean {
    return filter == ListFilter.LensFamily
            || filter == ListFilter.LensDescription
            || filter == ListFilter.LensMaterial
            || filter == ListFilter.LensSpecialty
            || filter == ListFilter.LensGroup
}

private fun shouldFilterByFamily(filter: ListFilter): Boolean {
    return filter == ListFilter.LensDescription
            || filter == ListFilter.LensMaterial
            || filter == ListFilter.LensSpecialty
            || filter == ListFilter.LensGroup
}

private fun shouldFilterByDescription(filter: ListFilter): Boolean {
    return filter == ListFilter.LensMaterial
            || filter == ListFilter.LensSpecialty
            || filter == ListFilter.LensGroup
}

private fun shouldFilterByMaterial(filter: ListFilter): Boolean {
    return filter == ListFilter.LensSpecialty
            || filter == ListFilter.LensGroup
}

private fun shouldFilterBySpecialty(filter: ListFilter): Boolean {
    return filter == ListFilter.LensGroup
}

fun buildFilterQueryFields(
    filter: ListFilter,
    activeListFilter: LensListFilter,
): List<PeyessQueryField> {
    val queryFields = mutableListOf<PeyessQueryField>()

    if (shouldFilterBySupplier(filter) && activeListFilter.supplierId.isNotEmpty()) {
        queryFields.add(
            buildQueryField(
                field = SimplifiedQueryFields.LensSupplier.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.supplierId,
            )
        )
    }

    if (shouldFilterByFamily(filter) && activeListFilter.familyId.isNotEmpty()) {
        queryFields.add(
            buildQueryField(
                field = SimplifiedQueryFields.LensFamily.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.familyId,
            )
        )
    }

    if (shouldFilterByDescription(filter) && activeListFilter.descriptionId.isNotEmpty()) {
        queryFields.add(
            buildQueryField(
                field = SimplifiedQueryFields.LensDescription.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.descriptionId,
            )
        )
    }

    if (shouldFilterByMaterial(filter) && activeListFilter.materialId.isNotEmpty()) {
        queryFields.add(
            buildQueryField(
                field = SimplifiedQueryFields.LensMaterial.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.materialId,
            )
        )
    }

    if (shouldFilterBySpecialty(filter) && activeListFilter.specialtyId.isNotEmpty()) {
        queryFields.add(
            buildQueryField(
                field = SimplifiedQueryFields.LensSpecialty.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.specialtyId,
            )
        )
    }

    return queryFields
}

fun buildFilterQueryOrderBy(): List<PeyessOrderBy> {
    return listOf(
        PeyessOrderBy(
            field = SimplifiedQueryFields.Name.name(),
            order = Order.ASCENDING,
        )
    )
}