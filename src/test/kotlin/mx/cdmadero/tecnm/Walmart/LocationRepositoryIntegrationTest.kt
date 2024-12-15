package mx.cdmadero.tecnm.Walmart

import mx.cdmadero.tecnm.Walmart.model.Location
import mx.cdmadero.tecnm.Walmart.model.Storage
import mx.cdmadero.tecnm.Walmart.repository.LocationRepository
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
class LocationRepositoryIntegrationTest {
    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var storageRepository: StorageRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun givenNewLocation_whenSave_thenSuccess() {
        val storage = Storage(
            name = "Almacen 1",
            address = "123 Calle 14, Colonia C",
            isActive = true
        )
        val savedStorage = storageRepository.save(storage)

        val location = Location(
            hallway = "H1",
            shelf = "S1",
            level = "L1",
            isActive = true,
            storage = savedStorage
        )
        val savedLocation = locationRepository.save(location)
        val foundLocation = entityManager.find(Location::class.java, savedLocation.id)

        assertNotNull(foundLocation)
        assertEquals(location.hallway, foundLocation.hallway)
        assertEquals(savedStorage.id, foundLocation.storage.id)
    }

    @Test
    fun givenExistingLocation_whenFindById_thenSuccess() {
        val storage = Storage(
            name = "Almacen 1",
            address = "123 Calle 14, Colonia C",
            isActive = true
        )
        val savedStorage = storageRepository.save(storage)

        val location = Location(
            hallway = "H2",
            shelf = "S2",
            level = "L2",
            isActive = true,
            storage = savedStorage
        )
        val persistedLocation = entityManager.persistFlushFind(location)

        val foundLocation = locationRepository.findById(persistedLocation.id)
        assertTrue(foundLocation.isPresent)
        assertEquals(persistedLocation.hallway, foundLocation.get().hallway)
        assertEquals(savedStorage.id, foundLocation.get().storage.id)
    }

    @Test
    fun givenExistingLocation_whenUpdate_thenSuccess() {
        val storage = Storage(
            name = "Almacen 1",
            address = "123 Calle 14, Colonia C",
            isActive = true
        )
        val savedStorage = storageRepository.save(storage)

        val location = Location(
            hallway = "H3",
            shelf = "S3",
            level = "L3",
            isActive = true,
            storage = savedStorage
        )
        val persistedLocation = entityManager.persistFlushFind(location)

        val updatedLocation = locationRepository.save(persistedLocation.copy(hallway = "Updated Hallway"))

        val foundLocation = entityManager.find(Location::class.java, updatedLocation.id)
        assertEquals("Updated Hallway", foundLocation.hallway)
    }

    @Test
    fun givenExistingLocation_whenSoftDelete_thenSuccess() {
        val storage = Storage(
            name = "Almacen 1",
            address = "123 Calle 14, Colonia C",
            isActive = true
        )
        val savedStorage = storageRepository.save(storage)

        val location = Location(
            hallway = "H4",
            shelf = "S4",
            level = "L4",
            isActive = true,
            storage = savedStorage
        )
        val persistedLocation = entityManager.persistFlushFind(location)

        locationRepository.delete(persistedLocation)

        // Verify soft delete
        val query = entityManager.entityManager
            .createNativeQuery("SELECT is_active FROM ${Tables.Location.NAME} WHERE id = :id")
            .setParameter("id", persistedLocation.id)
        val isActive = query.singleResult as Boolean
        assertFalse(isActive)
    }

    @Test
    fun givenSoftDeletedLocation_whenFind_thenFails() {
        val storage = Storage(
            name = "Almacen 1",
            address = "123 Calle 14, Colonia C",
            isActive = true
        )
        val savedStorage = storageRepository.save(storage)

        val location = Location(
            hallway = "H5",
            shelf = "S5",
            level = "L5",
            isActive = true,
            storage = savedStorage
        )
        val persistedLocation = entityManager.persistFlushFind(location)
        val persistedId = persistedLocation.id

        locationRepository.delete(persistedLocation)

        val result = locationRepository.findById(persistedId)
        assertFalse(result.isPresent)
    }

    @Test
    fun givenMultipleLocations_whenFindActive_thenSuccess() {
        val storage = Storage(
            name = "Almacen 1",
            address = "123 Calle 14, Colonia C",
            isActive = true
        )
        val savedStorage = storageRepository.save(storage)

        val activeLocation = Location(
            hallway = "H6",
            shelf = "S6",
            level = "L6",
            isActive = true,
            storage = savedStorage
        )
        val inactiveLocation = Location(
            hallway = "H7",
            shelf = "S7",
            level = "L7",
            isActive = false,
            storage = savedStorage
        )
        entityManager.persist(activeLocation)
        entityManager.persist(inactiveLocation)
        entityManager.flush()

        val activeLocations = locationRepository.findAll().filter { it.isActive }
        assertEquals(1, activeLocations.size)
        assertEquals("H6", activeLocations.first().hallway)
    }
}
