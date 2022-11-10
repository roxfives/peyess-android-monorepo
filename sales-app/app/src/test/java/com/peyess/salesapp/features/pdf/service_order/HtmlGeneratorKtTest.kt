package com.peyess.salesapp.features.pdf.service_order

import androidx.test.core.app.ApplicationProvider
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.peyess.salesapp.dao.sale.frames.FramesType
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentMethodType
import net.datafaker.Faker
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.random.asJavaRandom

@RunWith(RobolectricTestRunner::class)
class HtmlGeneratorKtTest {

    private lateinit var faker: Faker
    private lateinit var serviceOrderDocument: ServiceOrderDocument
    private lateinit var purchaseDocument: PurchaseDocument

    private fun getBirthday(): ZonedDateTime {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dateString = faker.date().birthday(14, 90, "dd/MM/yyyy")

        val localDate = LocalDate.parse(dateString, dateFormatter)
        val dateTime = LocalDateTime.of(localDate, LocalTime.NOON)

        return dateTime.atZone(ZoneId.systemDefault())
    }

    private fun getPrescriptionDate(): ZonedDateTime {
        val timestamp = faker.date().past(7, TimeUnit.DAYS)
        val dateTime = timestamp.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return dateTime.atZone(ZoneId.systemDefault())
    }

    private fun createPurchase(): PurchaseDocument {
        val payments = listOf(
            PaymentDocument(
                methodName = PaymentMethodType.Money,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = 1,
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
            PaymentDocument(
                methodName = PaymentMethodType.Debit,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = 1,
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
            PaymentDocument(
                methodName = PaymentMethodType.CreditFull,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = 1,
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
            PaymentDocument(
                methodName = PaymentMethodType.Credit,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = faker.number().numberBetween(2, 12),
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
            PaymentDocument(
                methodName = PaymentMethodType.Pix,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = 1,
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
            PaymentDocument(
                methodName = PaymentMethodType.Crediario,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = faker.number().numberBetween(1, 12),
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
            PaymentDocument(
                methodName = PaymentMethodType.BankDeposit,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = 1,
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
            PaymentDocument(
                methodName = PaymentMethodType.BankCheck,
                methodId = "",
                amount = faker.number().randomDouble(0, 500, 1500),
                installments = faker.number().numberBetween(1, 12),
                currency = "BRL",
                document = "",
                documentPicture = "",
                financialInst = FinancialInstitutionType.None,
                cardFlagName = "VISA",
                cardFlagIcon = "",
                payerUid = faker.idNumber().valid(),
                payerDocument = faker.cpf().valid(false),
                payerName = faker.name().fullName(),
                payerPicture = "",
            ),
        )

        val payerUids = payments.map { p -> p.payerUid }

        return PurchaseDocument(
            payerUids = payerUids,
            payments = payments,
        )
    }

    private fun createServiceOrder(): ServiceOrderDocument {
        val leftEyeSet = ProductSoldEyeSetDocument(
            lenses = ProductSoldDescriptionDocument(
                id = faker.idNumber().valid(),
                units = 1,
                nameDisplay = faker.commerce().productName(),
                price = faker.number().randomDouble(2, 1000, 2000),
                discount = DiscountDescriptionDocument(
                    method = DiscountCalcMethod.None,
                    value = 0.0,
                ),
            ),
            treatments = ProductSoldDescriptionDocument(
                id = faker.idNumber().valid(),
                units = 1,
                nameDisplay = faker.commerce().productName(),
                price = faker.number().randomDouble(2, 1000, 2000),
                discount = DiscountDescriptionDocument(
                    method = DiscountCalcMethod.None,
                    value = 0.0,
                ),
            ),
            colorings = ProductSoldDescriptionDocument(
                id = faker.idNumber().valid(),
                units = 1,
                nameDisplay = faker.commerce().productName(),
                price = faker.number().randomDouble(2, 1000, 2000),
                discount = DiscountDescriptionDocument(
                    method = DiscountCalcMethod.None,
                    value = 0.0,
                ),
            ),
        )
        val rightEyeSet = leftEyeSet.copy()

        val framesDescription = ProductSoldFramesDescriptionDocument(
            id = faker.idNumber().valid(),
            design = faker.commerce().brand(),
            reference = faker.idNumber().valid(),
            color = "",
            style = "",
            type = FramesType.MetalEnclosed,
            units = 1,
            price = faker.number().randomDouble(2, 1000, 5000),
            discount = DiscountDescriptionDocument(method = DiscountCalcMethod.None, value = 0.0),
        )

        val miscProducts = emptyList<ProductSoldDescriptionDocument>()

        val phoneRegex = "[\\s()-]".toRegex()
        return ServiceOrderDocument(
            hid = soId(),

            salespersonUid = faker.idNumber().valid(),
            salespersonName = faker.name().fullName(),

            clientUid = faker.idNumber().valid(),
            clientDocument = faker.cpf().valid(false),
            clientName = faker.name().fullName(),
            clientPicture = faker.name().fullName(),
            clientBirthday = getBirthday(),
            clientPhone = faker.phoneNumber().phoneNumber(),
            clientCellphone = faker.phoneNumber().cellPhone(),
            clientNeighborhood = faker.address().secondaryAddress(),
            clientStreet = faker.address().streetName(),
            clientCity = faker.address().city(),
            clientState = faker.address().state(),
            clientHouseNumber = faker.address().buildingNumber(),
            clientZipcode = faker.address().zipCode(),

            responsibleUid = faker.idNumber().valid(),
            responsibleDocument = faker.cpf().valid(false),
            responsibleName = faker.name().fullName(),
            responsiblePicture = faker.name().fullName(),
            responsibleBirthday = getBirthday(),
            responsiblePhone = faker.phoneNumber().phoneNumber().replace(phoneRegex, ""),
            responsibleCellphone = faker.phoneNumber().cellPhone().replace(phoneRegex, ""),
            responsibleNeighborhood = faker.address().secondaryAddress(),
            responsibleStreet = faker.address().streetName(),
            responsibleCity = faker.address().city(),
            responsibleState = faker.address().state(),
            responsibleHouseNumber = faker.address().buildingNumber(),
            responsibleZipcode = faker.address().zipCode().replace(phoneRegex, ""),

            professionalId  = faker.idNumber().valid(),
            professionalName  = faker.name().fullName(),
            prescriptionDate = getPrescriptionDate(),

            lCylinder = faker.number().randomDouble(2, -10, 0),
            lSpheric = faker.number().randomDouble(2, 0, 10),
            lAxisDegree = faker.number().randomDouble(2, 0, 180),
            lAddition = faker.number().randomDouble(2, 0, 7),
            lPrismAxis = faker.number().randomDouble(2, 0, 180),
            lPrismDegree = faker.number().randomDouble(2, 0, 10),
            lPrismPos = PrismPosition.listOfPositions[faker.number().numberBetween(0, PrismPosition.listOfPositions.size)].toName(),
            lIpd = faker.number().randomDouble(2, 28, 34),
            lHe = faker.number().randomDouble(2, 60, 80),
            lBridge = faker.number().randomDouble(2, 14, 18),
            lVerticalHoop = faker.number().randomDouble(2, 14, 18),
            lHorizontalHoop = faker.number().randomDouble(2, 14, 18),

            rCylinder = faker.number().randomDouble(2, -10, 0),
            rSpheric = faker.number().randomDouble(2, 0, 10),
            rAxisDegree = faker.number().randomDouble(2, 0, 180),
            rAddition = faker.number().randomDouble(2, 0, 7),
            rPrismAxis = faker.number().randomDouble(2, 0, 180),
            rPrismDegree = faker.number().randomDouble(2, 0, 10),
            rPrismPos = PrismPosition.listOfPositions[faker.number().numberBetween(0, PrismPosition.listOfPositions.size)].toName(),
            rIpd = faker.number().randomDouble(2, 28, 34),
            rHe = faker.number().randomDouble(2, 60, 80),
            rBridge = faker.number().randomDouble(2, 14, 18),
            rVerticalHoop = faker.number().randomDouble(2, 14, 18),
            rHorizontalHoop = faker.number().randomDouble(2, 14, 18),

            total = faker.number().randomDouble(2, 5000, 7000),
            totalDiscount = faker.number().randomDouble(2, 0, 1),

            hasTakeaway = true,
            takeawayDocument = faker.cpf().valid(false),
            takeawayName = faker.name().fullName(),

            leftProducts = leftEyeSet,
            rightProducts = rightEyeSet,
            framesProducts = framesDescription,
            miscProducts = miscProducts,

            totalPaid = faker.number().randomDouble(2, 1000, 10000),
            leftToPay = faker.number().randomDouble(2, 100, 1000),

            hasOwnFrames = true,
        )
    }

    private fun soId(): String {
        val random = Random.asJavaRandom()
        val size = 9
        val alphabet = charArrayOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'X', 'W', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9'
        )

        val zonedDateTime = ZonedDateTime.now()
        val year = zonedDateTime.year % 1000
        val month = zonedDateTime.month.value
        val day = zonedDateTime.dayOfMonth

        val suffix = "%02d%02d%02d".format(year, month, day)
        val id = NanoIdUtils.randomNanoId(random, alphabet, size)

        return "$suffix-$id"
    }

    @Before
    fun setupDataObjects() {
        faker = Faker(Locale.getDefault())

        serviceOrderDocument = createServiceOrder()
        purchaseDocument = createPurchase()
    }

    @Test
    @Config(qualifiers = "pt-rBR")
    fun testing() {
        val html = buildHtml(
            context = ApplicationProvider.getApplicationContext(),
            serviceOrder = serviceOrderDocument,
            purchase = purchaseDocument,
        )

        println(html)
    }
}