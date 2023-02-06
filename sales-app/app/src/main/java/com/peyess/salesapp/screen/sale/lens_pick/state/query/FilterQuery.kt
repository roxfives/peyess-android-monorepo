package com.peyess.salesapp.screen.sale.lens_pick.state.query

import com.peyess.salesapp.data.repository.lenses.room.SimplifiedQueryFields
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.screen.sale.lens_pick.constant.ListFilter
import com.peyess.salesapp.screen.sale.lens_pick.model.LensListFilter

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

private fun buildQueryFieldsForSupplier(
    filter: ListFilter,
    activeListFilter: LensListFilter,
): List<PeyessQueryField> {
    return if (shouldFilterBySupplier(filter) && activeListFilter.supplierId.isNotEmpty()) {
        listOf(
            buildQueryField(
                field = SimplifiedQueryFields.LensSupplier.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.supplierId,
            ),
        )
    } else {
        emptyList()
    }
}

private fun buildQueryFieldsForFamily(
    filter: ListFilter,
    activeListFilter: LensListFilter,
): List<PeyessQueryField> {
    return if (shouldFilterByFamily(filter) && activeListFilter.familyId.isNotEmpty()) {
        listOf(
            buildQueryField(
                field = SimplifiedQueryFields.LensFamily.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.familyId,
            ),
        )
    } else {
        emptyList()
    }
}

private fun buildQueryFieldsForDescription(
    filter: ListFilter,
    activeListFilter: LensListFilter,
): List<PeyessQueryField> {
    return if (shouldFilterByDescription(filter) && activeListFilter.descriptionId.isNotEmpty()) {
        listOf(
            buildQueryField(
                field = SimplifiedQueryFields.LensDescription.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.descriptionId,
            ),
        )
    } else {
        emptyList()
    }
}

private fun buildQueryFieldsForMaterial(
    filter: ListFilter,
    activeListFilter: LensListFilter,
): List<PeyessQueryField> {
    return if (shouldFilterByMaterial(filter) && activeListFilter.materialId.isNotEmpty()) {
        listOf(
            buildQueryField(
                field = SimplifiedQueryFields.LensMaterial.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.materialId,
            ),
        )
    } else {
        emptyList()
    }
}

private fun buildQueryFieldsForSpecialty(
    filter: ListFilter,
    activeListFilter: LensListFilter,
): List<PeyessQueryField> {
    return if (shouldFilterBySpecialty(filter) && activeListFilter.specialtyId.isNotEmpty()) {
        listOf(
            buildQueryField(
                field = SimplifiedQueryFields.LensSpecialty.name(),
                op = PeyessQueryOperation.Equal,
                value = activeListFilter.specialtyId,
            ),
        )
    } else {
        emptyList()
    }
}

fun buildFilterQueryFields(
    filter: ListFilter,
    activeListFilter: LensListFilter,
): List<PeyessQueryField> {
    return buildQueryFieldsForSupplier(filter, activeListFilter) +
        buildQueryFieldsForFamily(filter, activeListFilter) +
        buildQueryFieldsForDescription(filter, activeListFilter) +
        buildQueryFieldsForMaterial(filter, activeListFilter) +
        buildQueryFieldsForSpecialty(filter, activeListFilter)
}

fun buildFilterQueryOrderBy(): List<PeyessOrderBy> {
    return listOf(
        PeyessOrderBy(
            field = SimplifiedQueryFields.Name.name(),
            order = Order.ASCENDING,
        )
    )
}