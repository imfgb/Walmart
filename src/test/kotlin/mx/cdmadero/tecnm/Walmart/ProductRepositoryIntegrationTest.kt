package mx.cdmadero.tecnm.Walmart

import mx.cdmadero.tecnm.Walmart.model.Product
import mx.cdmadero.tecnm.Walmart.repository.ProductRepository
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIntegrationTest {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun givenNewProduct_whenSave_thenSuccess() {
        val product = Product(
            name = "Soda",
            upc = "0112001",
            description = "Soda",
            lastCost = "5.00",
            lastPrice = "10.00",
            isActive = true
        )
        val savedProduct = productRepository.save(product)
        val foundProduct = entityManager.find(Product::class.java, savedProduct.id)

        assertNotNull(foundProduct)
        assertEquals(product.name, foundProduct.name)
        assertEquals(product.upc, foundProduct.upc)
    }

    @Test
    fun givenExistingProduct_whenFindById_thenSuccess() {
        val product = Product(
            name = "Soda",
            upc = "0112001",
            description = "Soda",
            lastCost = "5.00",
            lastPrice = "10.00",
            isActive = true
        )
        val persistedProduct = entityManager.persistFlushFind(product)

        val foundProduct = productRepository.findById(persistedProduct.id)
        assertTrue(foundProduct.isPresent)
        assertEquals(persistedProduct.name, foundProduct.get().name)
    }

    @Test
    fun givenExistingProduct_whenUpdate_thenSuccess() {
        val product = Product(
            name = "Soda",
            upc = "0112001",
            description = "Soda",
            lastCost = "5.00",
            lastPrice = "10.00",
            isActive = true
        )
        val persistedProduct = entityManager.persistFlushFind(product)

        val updatedProduct = productRepository.save(persistedProduct.copy(name = "Updated Product"))

        val foundProduct = entityManager.find(Product::class.java, updatedProduct.id)
        assertEquals("Updated Product", foundProduct.name)
    }

    @Test
    fun givenExistingProduct_whenSoftDelete_thenSuccess() {
        val product = Product(
            name = "To Be Deleted",
            upc = "998877665544",
            description = "Soft delete test",
            lastCost = "1.00",
            lastPrice = "2.00",
            isActive = true
        )
        val persistedProduct = entityManager.persistFlushFind(product)

        productRepository.delete(persistedProduct)

        val query = entityManager.entityManager
            .createNativeQuery("SELECT is_active FROM ${Tables.Product.NAME} WHERE id = :id")
            .setParameter("id", persistedProduct.id)
        val isActive = query.singleResult as Boolean
        assertFalse(isActive)
    }

    @Test
    fun givenSoftDeletedProduct_whenFind_thenFails() {
        val product = Product(
            name = "Producto",
            upc = "667788990011",
            description = "Test for soft deletion",
            lastCost = "15.00",
            lastPrice = "25.00",
            isActive = true
        )
        val persistedProduct = entityManager.persistFlushFind(product)
        val persistedId = persistedProduct.id

        productRepository.delete(persistedProduct)

        val result = productRepository.findById(persistedId)
        assertFalse(result.isPresent)
    }

    @Test
    fun givenMultipleProducts_whenFindActive_thenSuccess() {
        val activeProduct = Product(
            name = "Activo",
            upc = "555666777888",
            description = "Active product",
            lastCost = "5.00",
            lastPrice = "10.00",
            isActive = true
        )
        val inactiveProduct = Product(
            name = "Inactivo",
            upc = "555666777888",
            description = "Inactive product",
            lastCost = "5.00",
            lastPrice = "10.00",
            isActive = false
        )
        entityManager.persist(activeProduct)
        entityManager.persist(inactiveProduct)
        entityManager.flush()

        val activeProducts = productRepository.findAll().filter { it.isActive }
        assertEquals(1, activeProducts.size)
        assertEquals("Activo", activeProducts.first().name)
    }
}
