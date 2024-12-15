package mx.cdmadero.tecnm.Walmart.repository

import mx.cdmadero.tecnm.Walmart.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionRepositoryIntegrationTest {

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun `given transactions in range, when findAllActiveByDate, then return transactions`() {
        val storage = createAndPersistStorage()
        val product = createAndPersistProduct()
        val employee = createAndPersistEmployee()

        val transaction1 = Transaction(
            storage = storage,
            product = product,
            employee = employee,
            reason = "Restock",
            dateTime = LocalDateTime.now().minusDays(1),
            quantity = 50,
            type = "IN"
        )

        val transaction2 = Transaction(
            storage = storage,
            product = product,
            employee = employee,
            reason = "Sale",
            dateTime = LocalDateTime.now(),
            quantity = 20,
            type = "OUT"
        )

        entityManager.persist(transaction1)
        entityManager.persist(transaction2)
        entityManager.flush()

        val startDate = LocalDateTime.now().minusDays(2).toString()
        val endDate = LocalDateTime.now().plusDays(1).toString()

        val results = transactionRepository.findAllInDateRange(startDate, endDate)

        assertEquals(2, results.size)
    }

    @Test
    fun `given transactions for specific storage, when findByOriginStorage, then return transactions`() {
        val storage = createAndPersistStorage()
        val product = createAndPersistProduct()
        val employee = createAndPersistEmployee()

        val transaction = Transaction(
            storage = storage,
            product = product,
            employee = employee,
            reason = "Stock Transfer",
            dateTime = LocalDateTime.now(),
            quantity = 100,
            type = "OUT"
        )

        entityManager.persist(transaction)
        entityManager.flush()

        val startDate = LocalDateTime.now().minusDays(1).toString()
        val endDate = LocalDateTime.now().plusDays(1).toString()

        val results = transactionRepository.findByOriginStorage(storage.id, startDate, endDate)

        assertEquals(1, results.size)
        assertEquals(storage.id, results[0].storage.id)
    }

    @Test
    fun `given transactions by employee, when findByEmployee, then return transactions`() {
        val storage = createAndPersistStorage()
        val product = createAndPersistProduct()
        val employee = createAndPersistEmployee()

        val transaction = Transaction(
            storage = storage,
            product = product,
            employee = employee,
            reason = "Employee Allocation",
            dateTime = LocalDateTime.now(),
            quantity = 10,
            type = "OUT"
        )

        entityManager.persist(transaction)
        entityManager.flush()

        val startDate = LocalDateTime.now().minusDays(1).toString()
        val endDate = LocalDateTime.now().plusDays(1).toString()

        val results = transactionRepository.findByEmployee(employee.id, startDate, endDate)

        assertEquals(1, results.size)
        assertEquals(employee.id, results[0].employee.id)
    }

    @Test
    fun `given transactions by origin or destiny, when findByOriginDestiny, then return transactions`() {
        val storage = createAndPersistStorage()
        val product = createAndPersistProduct()
        val employee = createAndPersistEmployee()

        val transaction = Transaction(
            storage = storage,
            product = product,
            employee = employee,
            reason = "Inter-warehouse transfer",
            dateTime = LocalDateTime.now(),
            quantity = 15,
            type = "OUT",
        )

        entityManager.persist(transaction)
        entityManager.flush()

        val startDate = LocalDateTime.now().minusDays(1).toString()
        val endDate = LocalDateTime.now().plusDays(1).toString()

        val results = transactionRepository.findByOriginDestiny("Storage", 1, startDate, endDate)

        assertEquals(1, results.size)
    }

    @Test
    fun `given transactions by product and storage, when findByProductAndStorage, then return transactions`() {
        val storage = createAndPersistStorage()
        val product = createAndPersistProduct()
        val employee = createAndPersistEmployee()

        val transaction = Transaction(
            storage = storage,
            product = product,
            employee = employee,
            reason = "Product restock",
            dateTime = LocalDateTime.now(),
            quantity = 100,
            type = "IN"
        )

        entityManager.persist(transaction)
        entityManager.flush()

        val startDate = LocalDateTime.now().minusDays(1).toString()
        val endDate = LocalDateTime.now().plusDays(1).toString()

        val results = transactionRepository.findByProductAndStorage(product.id, storage.id, startDate, endDate)

        assertEquals(1, results.size)
        assertEquals(product.id, results[0].product.id)
        assertEquals(storage.id, results[0].storage.id)
    }

    private fun createAndPersistStorage(): Storage {
        return entityManager.persistAndFlush(
            Storage(
                name = "Central Storage",
                address = "123 Main St",
                lat = 40.7128,
                lon = -74.0060,
                capacity = 1000,
                typeVehicle = "Truck",
                isActive = true
            )
        )
    }

    private fun createAndPersistProduct(): Product {
        return entityManager.persistAndFlush(
            Product(
                name = "Sample Product",
                description = "This is a sample product",
                lastPrice = "$10.0",
                lastCost = "$5.00",
                upc = "01297860",
                isActive = true
            )
        )
    }

    private fun createAndPersistEmployee(): Employee {
        return entityManager.persistAndFlush(
            Employee(
                name = "John Doe",
                rfc = "EMP0178A78",
                phone = "8115678943",
                email = "mail@example.com",
                entryDate = "2024-12-08",
                isActive = true
            )
        )
    }
}
