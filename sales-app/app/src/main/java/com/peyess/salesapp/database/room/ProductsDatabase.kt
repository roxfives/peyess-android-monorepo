package com.peyess.salesapp.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peyess.salesapp.dao.products.room.filter_lens_category.FilterLensCategoryEntity
import com.peyess.salesapp.dao.products.room.filter_lens_description.FilterLensDescriptionEntity
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_group.FilterLensGroupEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensCategoryDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensDescriptionDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensFamilyDao
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

@Database(
    entities = [
        JoinLensTreatmentEntity::class,
        JoinLensColoringEntity::class,

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
    ],
    version = 22,
)
abstract class ProductsDatabase: RoomDatabase() {
    abstract fun joinLensTreatmentDao(): JoinLensTreatmentDao
    abstract fun joinLensColoringDao(): JoinLensColoringDao

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
    abstract fun filterLensFamilyEntity(): FilterLensFamilyDao
    abstract fun filterLensGroupEntity(): FilterLensGroupDao
    abstract fun filterLensMaterialDao(): FilterLensMaterialDao
    abstract fun filterLensSpecialtyEntity(): FilterLensSpecialtyDao
    abstract fun filterLensSupplierDao(): FilterLensSupplierDao
    abstract fun filterLensTypeDao(): FilterLensTypeDao
    abstract fun filterLensTechEntity(): FilterLensTechDao
}