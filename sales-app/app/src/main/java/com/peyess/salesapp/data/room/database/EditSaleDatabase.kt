package com.peyess.salesapp.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.data.dao.edit_service_order.client_picked.EditClientPickedDao
import com.peyess.salesapp.data.dao.edit_service_order.frames.EditFramesDataDao
import com.peyess.salesapp.data.dao.edit_service_order.lens_comparison.EditLensComparisonDao
import com.peyess.salesapp.data.dao.edit_service_order.payment.EditLocalPaymentDao
import com.peyess.salesapp.data.dao.edit_service_order.payment_discount.EditOverallDiscountDao
import com.peyess.salesapp.data.dao.edit_service_order.payment_fee.EditPaymentFeeDao
import com.peyess.salesapp.data.dao.edit_service_order.positioning.EditPositioningDao
import com.peyess.salesapp.data.dao.edit_service_order.prescription.EditPrescriptionDao
import com.peyess.salesapp.data.dao.edit_service_order.product_picked.EditProductPickedDao
import com.peyess.salesapp.data.dao.edit_service_order.sale.EditSaleDao
import com.peyess.salesapp.data.dao.edit_service_order.service_order.EditServiceOrderDao
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedEntity
import com.peyess.salesapp.data.model.edit_service_order.frames.EditFramesDataEntity
import com.peyess.salesapp.data.model.edit_service_order.lens_comparison.EditLensComparisonEntity
import com.peyess.salesapp.data.model.edit_service_order.payment.EditLocalPaymentEntity
import com.peyess.salesapp.data.model.edit_service_order.payment_discount.EditOverallDiscountEntity
import com.peyess.salesapp.data.model.edit_service_order.payment_fee.EditPaymentFeeEntity
import com.peyess.salesapp.data.model.edit_service_order.positioning.EditPositioningEntity
import com.peyess.salesapp.data.model.edit_service_order.prescription.EditPrescriptionEntity
import com.peyess.salesapp.data.model.edit_service_order.product_picked.EditProductPickedEntity
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity
import com.peyess.salesapp.data.model.local_client.LocalClientEntity
import com.peyess.salesapp.data.room.converter.ConverterClientRole
import com.peyess.salesapp.data.room.converter.ConverterEye
import com.peyess.salesapp.data.room.converter.ConverterFramesType
import com.peyess.salesapp.data.room.converter.ConverterLensTypeCategoryName
import com.peyess.salesapp.data.room.converter.ConverterOverallDiscountCalcMethod
import com.peyess.salesapp.data.room.converter.ConverterPaymentFeeMethod
import com.peyess.salesapp.data.room.converter.ConverterPrismPosition
import com.peyess.salesapp.data.room.converter.ConverterSex
import com.peyess.salesapp.data.room.converter.ConverterUri
import com.peyess.salesapp.data.room.converter.ConverterZonedDateTime

@Database(
    entities = [
        EditSaleEntity::class,
        EditServiceOrderEntity::class,
        EditFramesDataEntity::class,
        EditLensComparisonEntity::class,
        EditLocalPaymentEntity::class,
        EditPaymentFeeEntity::class,
        EditOverallDiscountEntity::class,
        EditPositioningEntity::class,
        EditPrescriptionEntity::class,
        EditProductPickedEntity::class,
        EditClientPickedEntity::class,
    ],

    version = 15,
)
@TypeConverters(
    ConverterUri::class,
    ConverterLensTypeCategoryName::class,
    ConverterPrismPosition::class,
    ConverterFramesType::class,
    ConverterEye::class,
    ConverterClientRole::class,
    ConverterOverallDiscountCalcMethod::class,
    ConverterPaymentFeeMethod::class,
    ConverterZonedDateTime::class,
    ConverterSex::class,
)
abstract class EditSaleDatabase: RoomDatabase() {
    abstract fun editSaleDao(): EditSaleDao

    abstract fun editServiceOrderDao(): EditServiceOrderDao

    abstract fun editFramesDataDao(): EditFramesDataDao

    abstract fun editLensComparisonDao(): EditLensComparisonDao

    abstract fun editLocalPaymentDao(): EditLocalPaymentDao

    abstract fun editPaymentFeeDao(): EditPaymentFeeDao

    abstract fun editOverallDiscountDao(): EditOverallDiscountDao

    abstract fun editPositioningDao(): EditPositioningDao

    abstract fun editPrescriptionDao(): EditPrescriptionDao

    abstract fun editProductPickedDao(): EditProductPickedDao

    abstract fun editClientPickedDao(): EditClientPickedDao
}
