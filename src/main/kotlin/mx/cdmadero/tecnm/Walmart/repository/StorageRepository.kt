package mx.cdmadero.tecnm.Walmart.repository

import mx.cdmadero.tecnm.Walmart.model.Storage
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface StorageRepository : JpaRepository<Storage, Long>{
    fun findByName(name: String) : List<Storage>
    fun findByAddress(address: String) : List<Storage>

    @Query(
        value = "SELECT * FROM ${Tables.Storage.NAME} WHERE is_active = 1",
        nativeQuery = true
    )
    fun getAllActive() : List<Storage>
}