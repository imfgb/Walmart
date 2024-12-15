package mx.cdmadero.tecnm.Walmart

import mx.cdmadero.tecnm.Walmart.model.Storage
import mx.cdmadero.tecnm.Walmart.repository.StorageRepository
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
class StorageRepositoryIntegrationTestIntegrationTest {

    @Autowired
    private lateinit var storageRepository: StorageRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun givenNewStorage_whenSave_thenSuccess() {
        val newStorage = Storage(
            name = "Central Warehouse",
            address = "123 Main St",
            lat = 40.7128,
            lon = -74.0060,
            capacity = 1000,
            typeVehicle = "Truck",
            isActive = true
        )

        val savedStorage = storageRepository.save(newStorage)

        val foundStorage = entityManager.find(Storage::class.java, savedStorage.id)

        assertNotNull(foundStorage)
        assertEquals(newStorage.name, foundStorage.name)
        assertEquals(newStorage.address, foundStorage.address)
    }

    @Test
    fun givenExistingStorage_whenFindById_thenReturnStorage() {
        val persistedStorage = entityManager.persistAndFlush(
            Storage(
                name = "Regional Storage",
                address = "456 Elm St",
                lat = 34.0522,
                lon = -118.2437,
                capacity = 500,
                typeVehicle = "Van",
                isActive = true
            )
        )

        val foundStorage = storageRepository.findById(persistedStorage.id)

        assertTrue(foundStorage.isPresent)
        assertEquals(persistedStorage.name, foundStorage.get().name)
    }

    @Test
    fun givenExistingStorage_whenUpdate_thenChangesPersisted() {
        val persistedStorage = entityManager.persistAndFlush(
            Storage(
                name = "Old Storage",
                address = "789 Oak St",
                lat = 41.8781,
                lon = -87.6298,
                capacity = 300,
                typeVehicle = "Car",
                isActive = true
            )
        )

        val updatedStorage = persistedStorage.copy(
            name = "Updated Storage",
            capacity = 600
        )

        storageRepository.save(updatedStorage)

        val foundStorage = entityManager.find(Storage::class.java, persistedStorage.id)

        assertEquals("Updated Storage", foundStorage.name)
        assertEquals(600, foundStorage.capacity)
    }

    @Test
    fun givenExistingStorage_whenDelete_thenSoftDeleteApplied() {
        val persistedStorage = entityManager.persistAndFlush(
            Storage(
                name = "Temporary Storage",
                address = "123 Temp St",
                lat = 29.7604,
                lon = -95.3698,
                capacity = 200,
                typeVehicle = "Bike",
                isActive = true
            )
        )

        storageRepository.delete(persistedStorage)

        // The entity is soft deleted; check if it's active.
        val deletedStorage = entityManager.find(Storage::class.java, persistedStorage.id)

        assertNull(deletedStorage) // Hibernate filter applies: `is_active = true`

        // Verify raw SQL if needed
        val nativeQuery = entityManager.entityManager.createNativeQuery("SELECT is_active FROM ${Tables.Storage.NAME} WHERE id = :id")
        nativeQuery.setParameter("id", persistedStorage.id)
        val isActive = nativeQuery.singleResult as Boolean

        assertFalse(isActive)
    }

    @Test
    fun givenExistingStorage_whenFindAll_thenReturnActiveStoragesOnly() {
        val activeStorage = entityManager.persistAndFlush(
            Storage(
                name = "Active Storage",
                address = "123 Active St",
                lat = 37.7749,
                lon = -122.4194,
                capacity = 400,
                typeVehicle = "Bus",
                isActive = true
            )
        )

        val inactiveStorage = entityManager.persistAndFlush(
            Storage(
                name = "Inactive Storage",
                address = "456 Inactive St",
                lat = 34.0522,
                lon = -118.2437,
                capacity = 300,
                typeVehicle = "Truck",
                isActive = true
            )
        ).also { storageRepository.delete(it) }

        val storages = storageRepository.findAll()

        assertEquals(1, storages.size)
        assertEquals(activeStorage.id, storages[0].id)
    }
}
