package mx.cdmadero.tecnm.Walmart.repository

import mx.cdmadero.tecnm.Walmart.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByDescription(description: String): List<Product>
    fun findByUpc(upc: String): Product?
}