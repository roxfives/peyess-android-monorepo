package com.peyess.salesapp.feature.sale.lens_comparison.state.query

import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesUnionQueryFields
import com.peyess.salesapp.data.utils.query.PeyessGroupBy
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryFunctionOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryPredicateOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.feature.sale.lens_comparison.model.Lens
import com.peyess.salesapp.features.disponibility.contants.LensType

private fun buildQueryFieldsForLensSupplier(lensSupplierId: String): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = LocalLensesUnionQueryFields.LensSupplierId.name(),
            op = PeyessQueryOperation.Equal,
            value = lensSupplierId,
        )
    )
}

private fun buildQueryFieldsForLensFamily(lensFamilyId: String): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = LocalLensesUnionQueryFields.LensFamilyId.name(),
            op = PeyessQueryOperation.Equal,
            value = lensFamilyId,
        )
    )
}

private fun buildQueryFieldsForLensDescription(lensDescriptionId: String): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = LocalLensesUnionQueryFields.LensDescriptionId.name(),
            op = PeyessQueryOperation.Equal,
            value = lensDescriptionId,
        )
    )
}

private fun buildQueryFieldsForLensTech(lensTechId: String): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = LocalLensesUnionQueryFields.LensTechId.name(),
            op = PeyessQueryOperation.Equal,
            value = lensTechId,
        )
    )
}

private fun buildQueryFieldsForLensMaterial(lensMaterialId: String): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = LocalLensesUnionQueryFields.LensMaterialId.name(),
            op = PeyessQueryOperation.Equal,
            value = lensMaterialId,
        )
    )
}

private fun buildQueryFieldsForSpherical(
    prescription: LocalPrescriptionDocument
): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            function = PeyessQueryFunctionOperation.MAX,
            field = LocalLensesUnionQueryFields.MaxSpherical.name(),
            op = PeyessQueryOperation.GreaterThanOrEqual,
            value = prescription.sphericalLeft,
        ),
        buildQueryField(
            function = PeyessQueryFunctionOperation.MAX,
            field = LocalLensesUnionQueryFields.MaxSpherical.name(),
            op = PeyessQueryOperation.GreaterThanOrEqual,
            value = prescription.sphericalRight,
        ),
        buildQueryField(
            function = PeyessQueryFunctionOperation.MIN,
            field = LocalLensesUnionQueryFields.MinSpherical.name(),
            op = PeyessQueryOperation.LessThanOrEqual,
            value = prescription.sphericalLeft,
        ),
        buildQueryField(
            function = PeyessQueryFunctionOperation.MIN,
            field = LocalLensesUnionQueryFields.MinSpherical.name(),
            op = PeyessQueryOperation.LessThanOrEqual,
            value = prescription.sphericalRight,
        ),
    )
}

private fun buildQueryFieldsForCylindrical(
    prescription: LocalPrescriptionDocument,
): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            function = PeyessQueryFunctionOperation.MAX,
            field = LocalLensesUnionQueryFields.MaxCylindrical.name(),
            op = PeyessQueryOperation.GreaterThanOrEqual,
            value = prescription.cylindricalLeft,
        ),
        buildQueryField(
            function = PeyessQueryFunctionOperation.MAX,
            field = LocalLensesUnionQueryFields.MaxCylindrical.name(),
            op = PeyessQueryOperation.GreaterThanOrEqual,
            value = prescription.cylindricalRight,
        ),
        buildQueryField(
            function = PeyessQueryFunctionOperation.MIN,
            field = LocalLensesUnionQueryFields.MinCylindrical.name(),
            op = PeyessQueryOperation.LessThanOrEqual,
            value = prescription.cylindricalLeft,
        ),
        buildQueryField(
            function = PeyessQueryFunctionOperation.MIN,
            field = LocalLensesUnionQueryFields.MinCylindrical.name(),
            op = PeyessQueryOperation.LessThanOrEqual,
            value = prescription.cylindricalRight,
        ),
    )
}

private fun buildQueryFieldsForHeight(
    measuringLeft: LocalMeasuringDocument,
    measuringRight: LocalMeasuringDocument,
): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = LocalLensesUnionQueryFields.Height.name(),
            op = PeyessQueryOperation.LessThanOrEqual,
            value = measuringLeft.fixedHe,
        ),

        buildQueryField(
            field = LocalLensesUnionQueryFields.Height.name(),
            op = PeyessQueryOperation.LessThanOrEqual,
            value = measuringRight.fixedHe,
        ),
    )
}

private fun buildQueryFieldsForDiameter(
    measuringLeft: LocalMeasuringDocument,
    measuringRight: LocalMeasuringDocument,
): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = LocalLensesUnionQueryFields.Diameter.name(),
            op = PeyessQueryOperation.GreaterThanOrEqual,
            value = measuringLeft.diameter,
        ),
        buildQueryField(
            field = LocalLensesUnionQueryFields.Diameter.name(),
            op = PeyessQueryOperation.GreaterThanOrEqual,
            value = measuringRight.diameter,
        ),
    )
}

private fun buildQueryFieldsForAddition(
    prescription: LocalPrescriptionDocument,
): List<PeyessQueryField> {
    return if (prescription.hasPrism) {
        listOf(
            buildQueryField(
                function = PeyessQueryFunctionOperation.MAX,
                field = LocalLensesUnionQueryFields.MaxAddition.name(),
                op = PeyessQueryOperation.GreaterThanOrEqual,
                value = prescription.additionLeft,
            ),
            buildQueryField(
                function = PeyessQueryFunctionOperation.MAX,
                field = LocalLensesUnionQueryFields.MaxAddition.name(),
                op = PeyessQueryOperation.GreaterThanOrEqual,
                value = prescription.additionRight,
            ),
            buildQueryField(
                function = PeyessQueryFunctionOperation.MIN,
                field = LocalLensesUnionQueryFields.MinAddition.name(),
                op = PeyessQueryOperation.LessThanOrEqual,
                value = prescription.additionLeft,
            ),
            buildQueryField(
                function = PeyessQueryFunctionOperation.MIN,
                field = LocalLensesUnionQueryFields.MinAddition.name(),
                op = PeyessQueryOperation.LessThanOrEqual,
                value = prescription.additionRight,
            ),
        )
    } else {
        emptyList()
    }
}

private fun buildQueryFieldsForPrism(
    prescription: LocalPrescriptionDocument,
): List<PeyessQueryField> {
    return if (prescription.hasPrism) {
        listOf(
            buildQueryField(
                function = PeyessQueryFunctionOperation.MAX,
                field = LocalLensesUnionQueryFields.Prism.name(),
                op = PeyessQueryOperation.GreaterThanOrEqual,
                value = prescription.prismDegreeLeft,
            ),
            buildQueryField(
                function = PeyessQueryFunctionOperation.MAX,
                field = LocalLensesUnionQueryFields.Prism.name(),
                op = PeyessQueryOperation.GreaterThanOrEqual,
                value = prescription.prismDegreeRight,
            ),
        )
    } else {
        emptyList()
    }
}

private fun buildQueryFieldsForSumRule(
    prescription: LocalPrescriptionDocument,
): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            op = PeyessQueryPredicateOperation.IFF,
            expression = buildQueryField(
                field = LocalLensesUnionQueryFields.HasSumRule.name(),
                op = PeyessQueryOperation.Equal,
                value = true,
            ),
            ifTrue = buildQueryField(
                field = LocalLensesUnionQueryFields.MinSpherical.name(),
                op = PeyessQueryOperation.LessThanOrEqual,
                value = prescription.sphericalLeft + prescription.cylindricalLeft,
            ),
            ifFalse = buildQueryField(
                constant = 0.0,
            ),
        ),

        buildQueryField(
            op = PeyessQueryPredicateOperation.IFF,
            expression = buildQueryField(
                field = LocalLensesUnionQueryFields.HasSumRule.name(),
                op = PeyessQueryOperation.Equal,
                value = true,
            ),
            ifTrue = buildQueryField(
                field = LocalLensesUnionQueryFields.MinSpherical.name(),
                op = PeyessQueryOperation.LessThan,
                value = prescription.sphericalRight + prescription.cylindricalRight,
            ),
            ifFalse = buildQueryField(
                constant = 0.0,
            ),
        ),
    )
}

private fun buildCommonQuery(
    lens: Lens,
    prescription: LocalPrescriptionDocument,
    measuringLeft: LocalMeasuringDocument,
    measuringRight: LocalMeasuringDocument,
): List<PeyessQueryField> {
    return buildQueryFieldsForLensSupplier(lens.supplierId) +
            buildQueryFieldsForLensFamily(lens.brandId) +
            buildQueryFieldsForLensDescription(lens.designId) +
            buildQueryFieldsForSpherical(prescription) +
            buildQueryFieldsForCylindrical(prescription) +
            buildQueryFieldsForAddition(prescription) +
            buildQueryFieldsForPrism(prescription) +
            buildQueryFieldsForHeight(measuringLeft, measuringRight) +
            buildQueryFieldsForDiameter(measuringLeft, measuringRight) +
            buildQueryFieldsForSumRule(prescription)
}

fun buildQueryFieldsForTechs(
    lens: Lens,
    prescription: LocalPrescriptionDocument,
    measuringLeft: LocalMeasuringDocument,
    measuringRight: LocalMeasuringDocument,
): List<PeyessQueryField> {
    return buildCommonQuery(lens, prescription, measuringLeft, measuringRight)
}

fun buildQueryFieldsForMaterials(
    lens: Lens,
    prescription: LocalPrescriptionDocument,
    measuringLeft: LocalMeasuringDocument,
    measuringRight: LocalMeasuringDocument,
): List<PeyessQueryField> {
    return buildCommonQuery(lens, prescription, measuringLeft, measuringRight)
}

fun buildQueryFieldsForLensWithTech(
    lens: Lens,
    techId: String,
    prescription: LocalPrescriptionDocument,
    measuringLeft: LocalMeasuringDocument,
    measuringRight: LocalMeasuringDocument,
): List<PeyessQueryField> {
    return buildQueryFieldsForLensTech(techId) +
            buildCommonQuery(lens, prescription, measuringLeft, measuringRight)
}

fun buildQueryFieldsForLensWithMaterial(
    lens: Lens,
    materialId: String,
    prescription: LocalPrescriptionDocument,
    measuringLeft: LocalMeasuringDocument,
    measuringRight: LocalMeasuringDocument,
): List<PeyessQueryField> {
    return buildQueryFieldsForLensMaterial(materialId) +
            buildCommonQuery(lens, prescription, measuringLeft, measuringRight)
}

fun buildGroupByForTech(): List<PeyessGroupBy> {
    return listOf(
        PeyessGroupBy(
            field = LocalLensesUnionQueryFields.LensTechId.name(),
        )
    )
}

fun buildGroupByForMaterial(): List<PeyessGroupBy> {
    return listOf(
        PeyessGroupBy(
            field = LocalLensesUnionQueryFields.LensMaterialId.name(),
        )
    )
}

fun buildGroupByForLensPicking(): List<PeyessGroupBy> {
    return listOf(
        PeyessGroupBy(
            field = LocalLensesUnionQueryFields.LensId.name(),
        )
    )
}

fun buildOrderByForTech(): List<PeyessOrderBy> {
    return listOf(
        PeyessOrderBy(
            field = LocalLensesUnionQueryFields.LensTechPriority.name(),
            order = Order.ASCENDING,
        )
    )
}

fun buildOrderByForMaterial(): List<PeyessOrderBy> {
    return listOf(
        PeyessOrderBy(
            field = LocalLensesUnionQueryFields.LensMaterialPriority.name(),
            order = Order.ASCENDING,
        )
    )
}

fun buildOrderByForLensPicking(): List<PeyessOrderBy> {
    return listOf(
        PeyessOrderBy(
            field = LocalLensesUnionQueryFields.LensPriority.name(),
            order = Order.ASCENDING,
        )
    )
}
