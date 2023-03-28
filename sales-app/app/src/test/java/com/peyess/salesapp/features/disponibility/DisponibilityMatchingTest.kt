package com.peyess.salesapp.features.disponibility

import com.peyess.salesapp.features.disponibility.contants.LensType
import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported
import com.peyess.salesapp.features.disponibility.model.AlternativeHeight
import com.peyess.salesapp.features.disponibility.model.Disponibility
import com.peyess.salesapp.features.disponibility.model.Prescription
import com.peyess.salesapp.features.disponibility.utils.buildDisponibility
import com.peyess.salesapp.features.disponibility.utils.buildPrescription
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DisponibilityMatchingTest {
    private lateinit var disponibility: Disponibility
    private lateinit var prescription: Prescription

    @Before
    fun setUp() {
        disponibility = buildDisponibility()
        prescription = buildPrescription()
    }

    @Test
    fun `Should return an empty list when passed a prescription and disponibility without alternative heights`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            height = 30.0,
        )
        val prescription = buildPrescription(
            rightHeight = 30.0,
            leftHeight = 30.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldBeEmpty()
    }

    @Test
    fun `Should return an empty list when passed a prescription and disponibility with alternative heights`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            hasAlternativeHeight = true,
            alternativeHeights = listOf(
                AlternativeHeight(value = 45.0),
                AlternativeHeight(value = 50.0),
                AlternativeHeight(value = 55.0),
            )
        )
        val prescription = buildPrescription(leftHeight = 45.0, rightHeight = 45.0)

        val result = supportsPrescription(disponibility, prescription)

        result.shouldBeEmpty()
    }

    @Test
    fun `Should return a list containing only the mismatch about the monofocal lens type with multifocal prescription`() {
        val disponibility = buildDisponibility(
            sumRule = false,isLensTypeMono = true)
        val prescription = buildPrescription(lensType = LensType.MultiFocal)

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(ReasonUnsupported.LensTypeShouldBeMulti)
    }

    @Test
    fun `Should return a list containing only the mismatch about the multifocal lens type with near monofocal prescription`() {
        val disponibility = buildDisponibility(
            sumRule = false,isLensTypeMono = false)
        val prescription = buildPrescription(lensType = LensType.MonofocalNear)

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(ReasonUnsupported.LensTypeShouldBeMono)
    }

    @Test
    fun `Should return a list containing only the mismatch about the multifocal lens type with far monofocal prescription`() {
        val disponibility = buildDisponibility(
            sumRule = false,isLensTypeMono = false)
        val prescription = buildPrescription(lensType = LensType.MonofocalFar)

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(ReasonUnsupported.LensTypeShouldBeMono)
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding spherical degree for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxSpherical = 5.0,
            minSpherical = 0.0,
        )
        val prescription = buildPrescription(
            leftSpherical = 6.0,
            rightSpherical = 5.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MaxSphericalLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding spherical degree for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxSpherical = 5.0,
            minSpherical = 0.0,
        )
        val prescription = buildPrescription(
            leftSpherical = 5.0,
            rightSpherical = 6.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MaxSphericalRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about the minimal spherical degree for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxSpherical = 5.0,
            minSpherical = 1.0,
        )
        val prescription = buildPrescription(
            leftSpherical = 0.0,
            rightSpherical = 1.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MinSphericalLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about the minimal spherical degree for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxSpherical = 5.0,
            minSpherical = 1.0,
        )
        val prescription = buildPrescription(
            leftSpherical = 1.0,
            rightSpherical = 0.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MinSphericalRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding cylindrical degree for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxCylindrical = 5.0,
            minCylindrical = 0.0,
        )
        val prescription = buildPrescription(
            leftCylindrical = 6.0,
            rightCylindrical = 5.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MaxCylindricalLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding cylindrical degree for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxCylindrical = 5.0,
            minCylindrical = 0.0,
        )
        val prescription = buildPrescription(
            leftCylindrical = 5.0,
            rightCylindrical = 6.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MaxCylindricalRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about the minimal cylindrical degree for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxCylindrical = 5.0,
            minCylindrical = 1.0,
        )
        val prescription = buildPrescription(
            leftCylindrical = 0.0,
            rightCylindrical = 1.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MinCylindricalLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about the minimal cylindrical degree for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxCylindrical = 5.0,
            minCylindrical = 1.0,
        )
        val prescription = buildPrescription(
            leftCylindrical = 1.0,
            rightCylindrical = 0.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MinCylindricalRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding addition for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxAddition = 2.0,
            minAddition = 0.0,
        )
        val prescription = buildPrescription(
            hasAddition = true,
            leftAddition = 3.0,
            rightAddition = 2.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MaxAdditionLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding addition for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxAddition = 2.0,
            minAddition = 0.0,
        )
        val prescription = buildPrescription(
            hasAddition = true,
            leftAddition = 2.0,
            rightAddition = 3.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MaxAdditionRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about the minimal addition for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxAddition = 2.0,
            minAddition = 1.0,
        )
        val prescription = buildPrescription(
            hasAddition = true,
            leftAddition = 0.0,
            rightAddition = 1.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MinAdditionLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about the minimal addition for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            maxAddition = 2.0,
            minAddition = 1.0,
        )
        val prescription = buildPrescription(
            hasAddition = true,
            leftAddition = 1.0,
            rightAddition = 0.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.MinAdditionRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding prism for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            hasPrism = true,
            prism = 2.0,
        )
        val prescription = buildPrescription(
            leftPrism = 3.0,
            rightPrism = 2.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.PrismLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding prism for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            hasPrism = true,
            prism = 2.0,
        )
        val prescription = buildPrescription(
            leftPrism = 2.0,
            rightPrism = 3.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.PrismRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding height for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            hasAlternativeHeight = false,
            height = 10.0,
        )
        val prescription = buildPrescription(
            leftHeight = 9.0,
            rightHeight = 10.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.HeightLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding height for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            hasAlternativeHeight = false,
            height = 10.0,
        )
        val prescription = buildPrescription(
            leftHeight = 10.0,
            rightHeight = 9.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.HeightRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding alternative height for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            hasAlternativeHeight = true,
            alternativeHeights = listOf(
                AlternativeHeight(value = 22.0),
                AlternativeHeight(value = 23.0),
                AlternativeHeight(value = 24.0),
            ),
        )
        val prescription = buildPrescription(
            leftHeight = 20.0,
            rightHeight = 25.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.HeightLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding alternative height for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            hasAlternativeHeight = true,
            alternativeHeights = listOf(
                AlternativeHeight(value = 22.0),
                AlternativeHeight(value = 23.0),
                AlternativeHeight(value = 24.0),
            ),
        )
        val prescription = buildPrescription(
            leftHeight = 25.0,
            rightHeight = 20.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.HeightRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding diameter for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            diameter = 50.0,
        )
        val prescription = buildPrescription(
            leftDiameter = 51.0,
            rightDiameter = 50.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.DiameterLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about exceeding diameter for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = false,
            diameter = 50.0,
        )
        val prescription = buildPrescription(
            leftDiameter = 50.0,
            rightDiameter = 51.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.DiameterRight,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about not matching the sum rule for the left eye`() {
        val disponibility = buildDisponibility(
            sumRule = true,
            minSpherical = -3.0,
            maxCylindrical = -1.0,
            minCylindrical = -5.0,
        )
        val prescription = buildPrescription(
            leftSpherical = -3.0,
            leftCylindrical = -3.0,

            rightSpherical = -1.0,
            rightCylindrical = -1.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.SumRuleLeft,
        )
    }

    @Test
    fun `Should return a list containing only the mismatch about not matching the sum rule for the right eye`() {
        val disponibility = buildDisponibility(
            sumRule = true,
            minSpherical = -3.0,
            maxCylindrical = -1.0,
            minCylindrical = -5.0,
        )
        val prescription = buildPrescription(
            leftSpherical = -1.0,
            leftCylindrical = -1.0,

            rightSpherical = -3.0,
            rightCylindrical = -3.0,
        )

        val result = supportsPrescription(disponibility, prescription)

        result.shouldContainOnly(
            ReasonUnsupported.SumRuleRight,
        )
    }
}