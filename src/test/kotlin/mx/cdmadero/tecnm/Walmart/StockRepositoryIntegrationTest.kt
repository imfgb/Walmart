package mx.cdmadero.tecnm.Walmart.repository

import mx.cdmadero.tecnm.Walmart.model.Product
import mx.cdmadero.tecnm.Walmart.model.Stock
import mx.cdmadero.tecnm.Walmart.model.Storage
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StockRepositoryIntegrationTest {

    @Autowired
    private lateinit var stockRepository: StockRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun `given new Stock when save then success`() {
        val storage = entityManager.persist(Storage(name = "Main Storage"))
        val product = entityManager.persist(Product())
        val stock = Stock(storage = storage, product = product, availableItems = 50)

        val persistedStock = stockRepository.save(stock)

        val foundStock = entityManager.find(Stock::class.java, persistedStock.id)
        assertNotNull(foundStock)
        assertEquals(50, foundStock.availableItems)
    }

    @Test
    fun `given existing Stock when update then success`() {
        val storage = entityManager.persist(Storage(name = "Main Storage"))
        val product = entityManager.persist(Product())
        val stock = entityManager.persist(Stock(storage = storage, product = product, availableItems = 50))

        stockRepository.save(stock.copy(availableItems = 100))

        val updatedStock = entityManager.find(Stock::class.java, stock.id)
        assertNotNull(updatedStock)
        assertEquals(100, updatedStock.availableItems)
    }

    @Test
    fun `given existing Stock when delete then is inactive`() {
        val storage = entityManager.persist(Storage(name = "Main Storage"))
        val product = entityManager.persist(Product(name = "Product 1"))
        val stock = entityManager.persist(Stock(storage = storage, product = product, availableItems = 50))

        stockRepository.delete(stock)

        val deletedStock = entityManager.find(Stock::class.java, stock.id)
        assertNull(deletedStock) // Hibernate's filtering applies
    }

    @Test
    fun `given existing Stock when delete then row is inactive in database`() {
        val storage = entityManager.persist(Storage(name = "Main Storage"))
        val product = entityManager.persist(Product(name = "Product 1"))
        val stock = entityManager.persist(Stock(storage = storage, product = product, availableItems = 50))

        stockRepository.delete(stock)

        val query = entityManager.entityManager.createNativeQuery("SELECT is_active FROM ${Tables.Stock.NAME} WHERE id = :id")
            .setParameter("id", stock.id)
        val isActive = query.singleResult as Boolean

        assertEquals(false, isActive)
    }

    @Test
    fun `given Stocks in storage when findAll then retrieve only active`() {
        val storage = entityManager.persist(Storage(name = "Main Storage"))
        val product1 = entityManager.persist(Product(name = "Product 1"))
        val product2 = entityManager.persist(Product(name = "Product 2"))
        val stock1 = entityManager.persist(Stock(storage = storage, product = product1, availableItems = 30))
        val stock2 = entityManager.persist(Stock(storage = storage, product = product2, availableItems = 40))
        stockRepository.delete(stock1)

        val stocks = stockRepository.findAll();
        assertEquals(1, stocks.size)
        assertEquals(stock2.id, stocks.first().id)
    }

    @Test
    fun `given Stocks in storage when findByStorage then retrieve only active`() {
        val storage = entityManager.persist(Storage(name = "Main Storage"))
        val product1 = entityManager.persist(Product(name = "Product 1"))
        val product2 = entityManager.persist(Product(name = "Product 2"))
        val stock1 = entityManager.persist(Stock(storage = storage, product = product1, availableItems = 30))
        val stock2 = entityManager.persist(Stock(storage = storage, product = product2, availableItems = 40))
        stockRepository.delete(stock1)

        val stocks = stockRepository.findByStorage(storage.id)

        assertEquals(1, stocks.size)
        assertEquals(stock2.id, stocks.first().id)
    }

    @Test
    fun `given Stock in storage and product when findByProductAndStorage then retrieve active`() {
        val storage = entityManager.persist(Storage(name = "Main Storage"))
        val product = entityManager.persist(Product(name = "Product 1"))
        val stock = entityManager.persist(Stock(storage = storage, product = product, availableItems = 30))
        stockRepository.delete(stock)

        val foundStock = stockRepository.findByProductAndStorage(product.id, storage.id)

        assertNull(foundStock) // Deleted stock should not be retrieved
    }
}
