package com.peyess.salesapp.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.client.room.ClientPickedDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningDao
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.data.dao.local_sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.data.dao.local_sale.payment.SalePaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataDao
import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedDao
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.data.dao.discount.OverallDiscountDao
import com.peyess.salesapp.data.dao.payment_fee.PaymentFeeDao
import com.peyess.salesapp.data.model.discount.OverallDiscountEntity
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeEntity
import com.peyess.salesapp.data.room.converter.ConverterOverallDiscountCalcMethod
import com.peyess.salesapp.data.room.converter.ConverterPaymentFeeMethod
import com.peyess.salesapp.database.room.converters.ConverterClientRole
import com.peyess.salesapp.database.room.converters.ConverterEye
import com.peyess.salesapp.database.room.converters.ConverterFramesType
import com.peyess.salesapp.database.room.converters.ConverterLensTypeCategoryName
import com.peyess.salesapp.database.room.converters.ConverterLocalDate
import com.peyess.salesapp.database.room.converters.ConverterPrismPosition
import com.peyess.salesapp.database.room.converters.ConverterUri

@Database(
    entities = [
        ActiveSalesEntity::class,
        ActiveSOEntity::class,
        PrescriptionPictureEntity::class,
        PrescriptionDataEntity::class,
        FramesEntity::class,
        PositioningEntity::class,
        LensComparisonEntity::class,
        ProductPickedEntity::class,
        ClientEntity::class,
        SalePaymentEntity::class,
        OverallDiscountEntity::class,
        PaymentFeeEntity::class,
    ],
    version = 51,
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
)
abstract class ActiveSalesDatabase: RoomDatabase() {
    abstract fun activeSalesDao(): ActiveSalesDao

    abstract fun activeSODao(): ActiveSODao

    abstract fun prescriptionPictureDao(): PrescriptionPictureDao

    abstract fun prescriptionDataDao(): PrescriptionDataDao

    abstract fun framesDataDao(): FramesDataDao

    abstract fun positioningDao(): PositioningDao

    abstract fun lensComparisonDao(): LensComparisonDao

    abstract fun productPickedDao(): ProductPickedDao

    abstract fun clientPickedDao(): ClientPickedDao

    abstract fun salePaymentsDao(): SalePaymentDao

    abstract fun overallDiscountDao(): OverallDiscountDao

    abstract fun paymentFeeDao(): PaymentFeeDao
}