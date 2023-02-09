package com.peyess.salesapp.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.data.dao.local_sale.client_picked.ClientPickedDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.dao.local_sale.positioning.PositioningDao
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.dao.local_sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.data.dao.local_sale.payment.LocalPaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentEntity
import com.peyess.salesapp.data.dao.local_sale.local_prescription.LocalPrescriptionDao
import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedDao
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.data.dao.discount.OverallDiscountDao
import com.peyess.salesapp.data.dao.payment_fee.PaymentFeeDao
import com.peyess.salesapp.data.model.discount.OverallDiscountEntity
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeEntity
import com.peyess.salesapp.data.room.converter.ConverterOverallDiscountCalcMethod
import com.peyess.salesapp.data.room.converter.ConverterPaymentFeeMethod
import com.peyess.salesapp.data.room.converter.ConverterClientRole
import com.peyess.salesapp.data.room.converter.ConverterEye
import com.peyess.salesapp.data.room.converter.ConverterFramesType
import com.peyess.salesapp.data.room.converter.ConverterLensTypeCategoryName
import com.peyess.salesapp.data.room.converter.ConverterLocalDate
import com.peyess.salesapp.data.room.converter.ConverterPrismPosition
import com.peyess.salesapp.data.room.converter.ConverterUri
import com.peyess.salesapp.data.room.converter.ConverterZonedDateTime

@Database(
    entities = [
        ActiveSalesEntity::class,
        ActiveSOEntity::class,
        PrescriptionEntity::class,
        FramesEntity::class,
        PositioningEntity::class,
        LensComparisonEntity::class,
        ProductPickedEntity::class,
        ClientPickedEntity::class,
        LocalPaymentEntity::class,
        OverallDiscountEntity::class,
        PaymentFeeEntity::class,
    ],
    version = 59,
)
@TypeConverters(
    ConverterLocalDate::class,
    ConverterUri::class,
    ConverterLensTypeCategoryName::class,
    ConverterPrismPosition::class,
    ConverterFramesType::class,
    ConverterEye::class,
    ConverterClientRole::class,
    ConverterOverallDiscountCalcMethod::class,
    ConverterPaymentFeeMethod::class,
    ConverterZonedDateTime::class,
)
abstract class ActiveSalesDatabase: RoomDatabase() {
    abstract fun activeSalesDao(): ActiveSalesDao

    abstract fun activeSODao(): ActiveSODao

    abstract fun prescriptionPictureDao(): LocalPrescriptionDao

    abstract fun framesDataDao(): FramesDataDao

    abstract fun positioningDao(): PositioningDao

    abstract fun lensComparisonDao(): LensComparisonDao

    abstract fun productPickedDao(): ProductPickedDao

    abstract fun clientPickedDao(): ClientPickedDao

    abstract fun localPaymentsDao(): LocalPaymentDao

    abstract fun overallDiscountDao(): OverallDiscountDao

    abstract fun paymentFeeDao(): PaymentFeeDao
}
