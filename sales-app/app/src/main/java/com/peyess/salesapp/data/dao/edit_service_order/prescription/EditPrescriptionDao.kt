package com.peyess.salesapp.data.dao.edit_service_order.prescription

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.prescription.EditPrescriptionEntity
import com.peyess.salesapp.typing.prescription.PrismPosition
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface EditPrescriptionDao {
    @Insert(onConflict = REPLACE)
    suspend fun addPrescription(prescription: EditPrescriptionEntity)

    @Query("""
        SELECT * FROM ${EditPrescriptionEntity.tableName}
        WHERE id = :id
    """)
    suspend fun prescriptionById(id: String): EditPrescriptionEntity?

    @Query("""
        SELECT * FROM ${EditPrescriptionEntity.tableName}
        WHERE id = :id
    """)
    fun streamPrescriptionById(id: String): Flow<EditPrescriptionEntity?>

    @Query("""
        SELECT * FROM ${EditPrescriptionEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    suspend fun prescriptionByServiceOrder(serviceOrderId: String): EditPrescriptionEntity?

    @Query("""
        SELECT * FROM ${EditPrescriptionEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    fun streamPrescriptionByServiceOrder(serviceOrderId: String): Flow<EditPrescriptionEntity?>

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET picture_uri = :picture
        WHERE id = :id
    """)
    suspend fun updatePicture(id: String, picture: Uri)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET professional_name = :professionalName
        WHERE id = :id
    """)
    suspend fun updateProfessionalName(id: String, professionalName: String)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET professional_id = :professionalId
        WHERE id = :id
    """)
    suspend fun updateProfessionalId(id: String, professionalId: String)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET is_copy = :isCopy
        WHERE id = :id
    """)
    suspend fun updateIsCopy(id: String, isCopy: Boolean)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET local_date = :prescriptionDate
        WHERE id = :id
    """)
    suspend fun updatePrescriptionDate(id: String, prescriptionDate: ZonedDateTime)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET spherical_left = :sphericalLeft
        WHERE id = :id
    """)
    suspend fun updateSphericalLeft(id: String, sphericalLeft: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET spherical_right = :sphericalRight
        WHERE id = :id
    """)
    suspend fun updateSphericalRight(id: String, sphericalRight: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET cylindrical_left = :cylindricalLeft
        WHERE id = :id
    """)
    suspend fun updateCylindricalLeft(id: String, cylindricalLeft: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET cylindrical_right = :cylindricalRight
        WHERE id = :id
    """)
    suspend fun updateCylindricalRight(id: String, cylindricalRight: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET axis_left = :axisLeft
        WHERE id = :id
    """)
    suspend fun updateAxisLeft(id: String, axisLeft: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET axis_right = :axisRight
        WHERE id = :id
    """)
    suspend fun updateAxisRight(id: String, axisRight: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET has_addition = :hasAddition
        WHERE id = :id
    """)
    suspend fun updateHasAddition(id: String, hasAddition: Int)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET addition_left = :additionLeft
        WHERE id = :id
    """)
    suspend fun updateAdditionLeft(id: String, additionLeft: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET addition_right = :additionRight
        WHERE id = :id
    """)
    suspend fun updateAdditionRight(id: String, additionRight: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET has_prism = :hasPrism
        WHERE id = :id
    """)
    suspend fun updateHasPrism(id: String, hasPrism: Int)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET prism_degree_left = :prismDegreeLeft
        WHERE id = :id
    """)
    suspend fun updatePrismDegreeLeft(id: String, prismDegreeLeft: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET prism_degree_right = :prismDegreeRight
        WHERE id = :id
    """)
    suspend fun updatePrismDegreeRight(id: String, prismDegreeRight: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET prism_axis_left = :prismAxisLeft
        WHERE id = :id
    """)
    suspend fun updatePrismAxisLeft(id: String, prismAxisLeft: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET prism_axis_right = :prismAxisRight
        WHERE id = :id
    """)
    suspend fun updatePrismAxisRight(id: String, prismAxisRight: Double)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET prism_position_left = :prismPositionLeft
        WHERE id = :id
    """)
    suspend fun updatePrismPositionLeft(id: String, prismPositionLeft: PrismPosition)

    @Query("""
        UPDATE ${EditPrescriptionEntity.tableName}
        SET prism_position_right = :prismPositionRight
        WHERE id = :id
    """)
    suspend fun updatePrismPositionRight(id: String, prismPositionRight: PrismPosition)

    @Query("""
        DELETE FROM ${EditPrescriptionEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    suspend fun deletePrescriptionForServiceOrder(serviceOrderId: String)
}
