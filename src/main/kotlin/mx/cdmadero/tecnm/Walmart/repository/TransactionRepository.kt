package mx.cdmadero.tecnm.Walmart.repository

import mx.cdmadero.tecnm.Walmart.model.Transaction
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    @Query(
        value = "SELECT * FROM ${Tables.Transaction.NAME} " +
                "WHERE DATE(date_time) BETWEEN :startDate AND :endDate",
        nativeQuery = true
    )
    fun findAllInDateRange(
        @Param("startDate") startDate: String,
        @Param("endDate") endDate: String
    ): List<Transaction>

    @Query(
        value = "SELECT * FROM ${Tables.Transaction.NAME} " +
                "WHERE storage_id = :storageId " +
                "AND DATE(date_time) BETWEEN :startDate AND :endDate",
        nativeQuery = true
    )
    fun findByOriginStorage(
        @Param("storageId") storageId: Long,
        @Param("startDate") startDate: String,
        @Param("endDate") endDate: String
    ): List<Transaction>

    @Query(
        value = "SELECT * FROM ${Tables.Transaction.NAME} " +
                "WHERE employee_id = :employeeId " +
                "AND DATE(date_time) BETWEEN :startDate AND :endDate",
        nativeQuery = true
    )
    fun findByEmployee(
        @Param("employeeId") employeeId: Long,
        @Param("startDate") startDate: String,
        @Param("endDate") endDate: String
    ): List<Transaction>

    @Query(
        value = "SELECT t FROM Transaction t " +
                "WHERE t.originDestiny.id = :referenceId " +
                "AND t.dateTime BETWEEN :startDate AND :endDate",
    )
    fun findByOriginDestiny(
        @Param("referenceId") referenceId: Long,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Transaction>

    @Query(
        value = "SELECT * FROM ${Tables.Transaction.NAME} " +
                "WHERE product_id = :productId " +
                "AND storage_id = :storageId " +
                "AND DATE(date_time) BETWEEN :startDate AND :endDate",
        nativeQuery = true
    )
    fun findByProductAndStorage(
        @Param("productId") productId: Long,
        @Param("storageId") storageId : Long,
        @Param("startDate") startDate: String,
        @Param("endDate") endDate: String
    ) : List<Transaction>
}


