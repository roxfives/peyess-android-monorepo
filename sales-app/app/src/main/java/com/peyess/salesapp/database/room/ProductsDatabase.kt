package com.peyess.salesapp.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.dao.products.room.filter_lens_category.FilterLensCategoryEntity
import com.peyess.salesapp.dao.products.room.filter_lens_description.FilterLensDescriptionEntity
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyDao
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_group.FilterLensGroupEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_category.FilterLensCategoryDao
import com.peyess.salesapp.dao.products.room.filter_lens_description.FilterLensDescriptionDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensGroupDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensMaterialDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSpecialtyDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensTechDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensTypeDao
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.dao.products.room.join_lens_coloring.JoinLensColoringDao
import com.peyess.salesapp.dao.products.room.join_lens_coloring.JoinLensColoringEntity
import com.peyess.salesapp.dao.products.room.join_lens_material.JoinLensMaterialDao
import com.peyess.salesapp.dao.products.room.join_lens_material.JoinLensMaterialEntity
import com.peyess.salesapp.dao.products.room.join_lens_tech.JoinLensTechDao
import com.peyess.salesapp.dao.products.room.join_lens_tech.JoinLensTechEntity
import com.peyess.salesapp.dao.products.room.join_lens_treatment.JoinLensTreatmentDao
import com.peyess.salesapp.dao.products.room.join_lens_treatment.JoinLensTreatmentEntity
import com.peyess.salesapp.dao.products.room.local_alt_height.LocalAltHeightDao
import com.peyess.salesapp.dao.products.room.local_alt_height.LocalAltHeightEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringDao
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_coloring_display.LocalColoringDisplayDao
import com.peyess.salesapp.dao.products.room.local_coloring_display.LocalColoringDisplayEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensDao
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_lens_base.LocalLensBaseDao
import com.peyess.salesapp.dao.products.room.local_lens_base.LocalLensBaseEntity
import com.peyess.salesapp.dao.products.room.local_lens_category_type.LocalLensCategoryTypeDao
import com.peyess.salesapp.dao.products.room.local_lens_category_type.LocalLensCategoryTypeEntity
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispDao
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispEntity
import com.peyess.salesapp.dao.products.room.local_lens_disp_manufacturer.LocalLensDispManufacturerDao
import com.peyess.salesapp.dao.products.room.local_lens_disp_manufacturer.LocalLensDispManufacturerEntity
import com.peyess.salesapp.dao.products.room.local_lens_material_type.LocalLensMaterialTypeDao
import com.peyess.salesapp.dao.products.room.local_lens_material_type.LocalLensMaterialTypeEntity
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpDao
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentDao
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.data.dao.products_table_state.ProductsTableStateDao
import com.peyess.salesapp.data.dao.products_table_state.ProductTableStateEntity
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringEntity
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensAltHeightEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityManufacturerEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensAltHeightCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensColoringCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDetailsCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDisponibilityManufacturerCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensMaterialTypeCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensTreatmentCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensTypeCategoryCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensWithDetailsDBView
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentEntity
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentExplanationEntity
import com.peyess.salesapp.database.room.converters.ConverterLocalDate
import com.peyess.salesapp.database.room.converters.ConverterUri
import com.peyess.salesapp.database.room.converters.ConverterZonedDateTime

@Database(
    entities = [
        JoinLensTreatmentEntity::class,
        JoinLensColoringEntity::class,

        JoinLensTechEntity::class,
        JoinLensMaterialEntity::class,

        LocalAltHeightEntity::class,

        LocalColoringEntity::class,
        LocalColoringDisplayEntity::class,

        LocalLensDispEntity::class,

        LocalLensEntity::class,
        LocalLensBaseEntity::class,
        LocalLensCategoryTypeEntity::class,
        LocalLensDispManufacturerEntity::class,
        LocalLensMaterialTypeEntity::class,

        LocalProductExpEntity::class,

        LocalTreatmentEntity:: class,

        FilterLensCategoryEntity::class,
        FilterLensDescriptionEntity::class,
        FilterLensFamilyEntity::class,
        FilterLensGroupEntity::class,
        FilterLensMaterialEntity::class,
        FilterLensSpecialtyEntity::class,
        FilterLensSupplierEntity::class,
        FilterLensTechEntity::class,
        FilterLensTypeEntity::class,

        ProductTableStateEntity::class,

        LocalLensFamilyEntity::class,
        LocalLensDescriptionEntity::class,
        LocalLensSupplierEntity::class,
        LocalLensGroupEntity::class,
        LocalLensSpecialtyEntity::class,
        LocalLensTechEntity::class,
        LocalLensTypeEntity::class,
        LocalLensCategoryEntity::class,
        LocalLensMaterialEntity::class,
        com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialTypeEntity::class,
        LocalLensMaterialCategoryEntity::class,
        LocalLensExplanationEntity::class,
        LocalLensDisponibilityEntity::class,
        LocalLensDisponibilityManufacturerEntity::class,
        LocalLensAltHeightEntity::class,
        LocalLensTypeCategoryEntity::class,
        com.peyess.salesapp.data.model.lens.room.dao.LocalLensBaseEntity::class,
        com.peyess.salesapp.data.model.lens.room.dao.LocalLensEntity::class,

        LocalLensColoringEntity::class,
        LocalLensColoringExplanationEntity::class,

        LocalLensTreatmentEntity::class,
        LocalLensTreatmentExplanationEntity::class,

        LocalLensAltHeightCrossRef::class,
        LocalLensDisponibilityManufacturerCrossRef::class,
        LocalLensColoringCrossRef::class,
        LocalLensTreatmentCrossRef::class,
        LocalLensTypeCategoryCrossRef::class,
        LocalLensMaterialTypeCrossRef::class,
        LocalLensDetailsCrossRef::class,
    ],
    views = [
        LocalLensWithDetailsDBView::class,
    ],
    version = 75,
)
@TypeConverters(
    ConverterUri::class,
    ConverterLocalDate::class,
    ConverterZonedDateTime::class,
)
abstract class ProductsDatabase: RoomDatabase() {
    abstract fun joinLensTreatmentDao(): JoinLensTreatmentDao
    abstract fun joinLensColoringDao(): JoinLensColoringDao
    abstract fun joinLensMaterialDao(): JoinLensMaterialDao
    abstract fun joinLensTechDao(): JoinLensTechDao

    abstract fun localAltHeightDao(): LocalAltHeightDao

    abstract fun localColoringDao(): LocalColoringDao
    abstract fun localColoringDisplayDao(): LocalColoringDisplayDao

    abstract fun localLensDao(): LocalLensDao
    abstract fun localLensBaseDao(): LocalLensBaseDao
    abstract fun localLensCategoryTypeDao(): LocalLensCategoryTypeDao
    abstract fun localLensDispManufacturerDao(): LocalLensDispManufacturerDao
    abstract fun localLensMaterialTypeDao(): LocalLensMaterialTypeDao

    abstract fun localLensDispEntityDao(): LocalLensDispDao

    abstract fun localProdExpDao(): LocalProductExpDao

    abstract fun localTreatmentDao(): LocalTreatmentDao

    abstract fun filterLensCategoryEntity(): FilterLensCategoryDao
    abstract fun filterLensDescriptionEntity(): FilterLensDescriptionDao
    abstract fun filterLensFamilyDao(): FilterLensFamilyDao
    abstract fun filterLensGroupEntity(): FilterLensGroupDao
    abstract fun filterLensMaterialDao(): FilterLensMaterialDao
    abstract fun filterLensSpecialtyDao(): FilterLensSpecialtyDao
    abstract fun filterLensSupplierDao(): FilterLensSupplierDao
    abstract fun filterLensTypeDao(): FilterLensTypeDao
    abstract fun filterLensTechDao(): FilterLensTechDao

    abstract fun productsTableStateDao(): ProductsTableStateDao

    abstract fun lensDao(): com.peyess.salesapp.data.dao.lenses.room.LocalLensDao
}