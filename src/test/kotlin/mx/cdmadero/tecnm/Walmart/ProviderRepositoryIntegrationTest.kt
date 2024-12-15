package mx.cdmadero.tecnm.Walmart

import mx.cdmadero.tecnm.Walmart.model.Provider
import mx.cdmadero.tecnm.Walmart.repository.ProviderRepository
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.util.*

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProviderRepositoryIntegrationTest {
    @Autowired
    private lateinit var providerRepository: ProviderRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun givenNewProvider_whenSave_thenSuccess() {
        val newProvider = Provider(
            name = "Test Provider",
            rfc = "RFC123",
            email = "test@example.com",
            phone = "1234567890",
            createdOn = Date(),
        )
        val savedProvider = providerRepository.save(newProvider)
        val foundProvider = entityManager.find(Provider::class.java, savedProvider.id)

        assertNotNull(foundProvider)
        assertEquals(newProvider.name, foundProvider.name)
    }

    @Test
    fun givenExistingProvider_whenFindById_thenSuccess() {
        val provider = Provider(
            name = "Test Provider",
            rfc = "RFC123",
            email = "test@example.com",
            phone = "1234567890",
            createdOn = Date(),
        )
        val persistedProvider = entityManager.persistFlushFind(provider)

        val foundProvider = providerRepository.findById(persistedProvider.id)
        assertTrue(foundProvider.isPresent)
        assertEquals(persistedProvider.name, foundProvider.get().name)
    }

    @Test
    fun givenProviders_whenFindByName_thenSuccess() {
        val provider1 = Provider(
            name = "Shared Name",
            rfc = "RFC123",
            email = "provider1@example.com",
            phone = "1111111111",
            createdOn = Date(),
        )
        val provider2 = Provider(
            name = "Shared Name",
            rfc = "RFC124",
            email = "provider2@example.com",
            phone = "2222222222",
            createdOn = Date(),
        )
        entityManager.persist(provider1)
        entityManager.persist(provider2)
        entityManager.flush()

        val foundProviders = providerRepository.findByName("Shared Name")
        assertEquals(2, foundProviders.size)
    }

    @Test
    fun givenExistingProvider_whenUpdate_thenSuccess() {
        val provider = Provider(
            name = "Old Name",
            rfc = "RFC123",
            email = "test@example.com",
            phone = "1234567890",
            createdOn = Date(),
        )
        val persistedProvider = entityManager.persistFlushFind(provider)

        val updatedProvider = providerRepository.save(persistedProvider.copy(name = "Updated Name"))

        val foundProvider = entityManager.find(Provider::class.java, updatedProvider.id)
        assertEquals("Updated Name", foundProvider.name)
    }

    @Test
    fun givenExistingProvider_whenSoftDelete_thenSuccess() {
        val provider = Provider(
            name = "Test Provider",
            rfc = "RFC123",
            email = "test@example.com",
            phone = "1234567890",
            createdOn = Date(),
        )
        val persistedProvider = entityManager.persistFlushFind(provider)

        providerRepository.delete(persistedProvider)

        // Verify soft delete (not deleted from DB but marked as inactive)
        val query = entityManager.entityManager
            .createNativeQuery("SELECT is_active FROM ${Tables.Provider.NAME} WHERE id = :id")
            .setParameter("id", persistedProvider.id)
        val isActive = query.singleResult as Boolean
        assertFalse(isActive)
    }

    @Test
    fun givenSoftDeletedProvider_whenFind_thenFails() {
        val provider = Provider(
            name = "Test Provider",
            rfc = "RFC123",
            email = "test@example.com",
            phone = "1234567890",
            createdOn = Date(),
        )
        val persistedProvider = entityManager.persistFlushFind(provider)
        val persistedId = persistedProvider.id

        providerRepository.delete(persistedProvider)

        val result = providerRepository.findById(persistedId)
        assertFalse(result.isPresent)
    }

}
