package mx.cdmadero.tecnm.Walmart.repository

import mx.cdmadero.tecnm.Walmart.model.Location
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<Location, Long> {
    @Query(
        value = "SELECT * FROM ${Tables.Location.NAME} WHERE storage_id = :storageId",
        nativeQuery = true
    )
    fun findByStorage(
        @Param("storageId") storageId: Long
    ): List<Location>
}