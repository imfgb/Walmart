package mx.cdmadero.tecnm.Walmart.repository

import mx.cdmadero.tecnm.Walmart.model.Stock
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : JpaRepository<Stock, Long>{
    @Query(
        "SELECT s FROM Stock s WHERE s.storage.id = :storageId"
    )
    fun findByStorage(@Param("storageId") storageId: Long): List<Stock>

    @Query(
        "SELECT s FROM Stock s " +
        "WHERE s.storage.id = :storageId AND s.product.id = :productId"
    )
    fun findByProductAndStorage(
        @Param("productId") productId: Long,
        @Param("storageId") storageId: Long
    ): Stock?
}