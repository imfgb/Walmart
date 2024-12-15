package mx.cdmadero.tecnm.Walmart

import mx.cdmadero.tecnm.Walmart.model.Employee
import mx.cdmadero.tecnm.Walmart.repository.EmployeeRepository
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
class EmployeeRepositoryIntegrationTest {
    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun givenNewEmployee_whenSave_thenSuccess() {
        val newEmployee = Employee(
            name = "John Doe",
            rfc = "RFCA1234ABC",
            email = "john.doe@example.com",
            phone = "1234567890",
            entryDate = "2023-12-01",
            isActive = true
        )
        val savedEmployee = employeeRepository.save(newEmployee)
        val foundEmployee = entityManager.find(Employee::class.java, savedEmployee.id)

        assertNotNull(foundEmployee)
        assertEquals(newEmployee.name, foundEmployee.name)
        assertEquals(newEmployee.email, foundEmployee.email)
    }

    @Test
    fun givenExistingEmployee_whenFindById_thenSuccess() {
        val employee = Employee(
            name = "Jane Doe",
            rfc = "RFCB1234ABC",
            email = "jane.doe@example.com",
            phone = "0987654321",
            entryDate = "2023-11-15",
            isActive = true
        )
        val persistedEmployee = entityManager.persistFlushFind(employee)

        val foundEmployee = employeeRepository.findById(persistedEmployee.id)
        assertTrue(foundEmployee.isPresent)
        assertEquals(persistedEmployee.name, foundEmployee.get().name)
    }

    @Test
    fun givenExistingEmployee_whenUpdate_thenSuccess() {
        val employee = Employee(
            name = "Alice",
            rfc = "RFCC1234ABC",
            email = "alice@example.com",
            phone = "1231231234",
            entryDate = "2023-10-01",
            isActive = true
        )
        val persistedEmployee = entityManager.persistFlushFind(employee)

        val updatedEmployee = employeeRepository.save(persistedEmployee.copy(name = "Alice Updated"))

        val foundEmployee = entityManager.find(Employee::class.java, updatedEmployee.id)
        assertEquals("Alice Updated", foundEmployee.name)
    }

    @Test
    fun givenExistingEmployee_whenSoftDelete_thenSuccess() {
        val employee = Employee(
            name = "Bob",
            rfc = "RFCD1234ABC",
            email = "bob@example.com",
            phone = "4564564567",
            entryDate = "2023-09-15",
            isActive = true
        )
        val persistedEmployee = entityManager.persistFlushFind(employee)

        employeeRepository.delete(persistedEmployee)

        // Verify soft delete (marked as inactive in the database)
        val query = entityManager.entityManager
            .createNativeQuery("SELECT is_active FROM ${Tables.Employee.NAME} WHERE id = :id")
            .setParameter("id", persistedEmployee.id)
        val isActive = query.singleResult as Boolean
        assertFalse(isActive)
    }

    @Test
    fun givenSoftDeletedEmployee_whenFind_thenFails() {
        val employee = Employee(
            name = "Charlie",
            rfc = "RFCF1234ABC",
            email = "charlie@example.com",
            phone = "7897897890",
            entryDate = "2023-08-10",
            isActive = true
        )
        val persistedEmployee = entityManager.persistFlushFind(employee)
        val persistedId = persistedEmployee.id

        employeeRepository.delete(persistedEmployee)

        val result = employeeRepository.findById(persistedId)
        assertFalse(result.isPresent)
    }

    @Test
    fun givenMultipleEmployees_whenFindActive_thenSuccess() {
        val activeEmployee = Employee(
            name = "Active Employee",
            rfc = "RFCG1234ABC",
            email = "active@example.com",
            phone = "1239876543",
            entryDate = "2023-07-01",
            isActive = true
        )
        val inactiveEmployee = Employee(
            name = "Inactive Employee",
            rfc = "RFCH1234ABC",
            email = "inactive@example.com",
            phone = "4561237890",
            entryDate = "2023-06-01",
            isActive = false
        )
        entityManager.persist(activeEmployee)
        entityManager.persist(inactiveEmployee)
        entityManager.flush()

        val activeEmployees = employeeRepository.findAll().filter { it.isActive }
        assertEquals(1, activeEmployees.size)
        assertEquals("Active Employee", activeEmployees.first().name)
    }
}
