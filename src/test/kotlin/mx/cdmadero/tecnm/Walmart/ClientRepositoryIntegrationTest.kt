package mx.cdmadero.tecnm.Walmart

import mx.cdmadero.tecnm.Walmart.model.Client
import mx.cdmadero.tecnm.Walmart.repository.ClientRepository
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryIntegrationTest {
    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun givenNewClient_whenSave_thenSuccess() {
        val newClient = Client(
            name = "Fulanito",
            rfc = "ABCD012345EFG",
            email = "example@email.com",
            phone = "1234567890",
            entryDate = LocalDate.now(),
            isActive = true
        )
        val savedClient = clientRepository.save(newClient)
        val foundClient = entityManager.find(Client::class.java, savedClient.id)

        assertNotNull(foundClient)
        assertEquals(newClient.name, foundClient.name)
    }

    @Test
    fun givenExistingClient_whenFindById_thenSuccess() {
        val client = Client(
            name = "Fulanito",
            rfc = "ABCD012345EFG",
            email = "example@email.com",
            phone = "1234567890",
            entryDate = LocalDate.now(),
            isActive = true
        )
        val persistedClient = entityManager.persistFlushFind(client)

        val foundClient = clientRepository.findById(persistedClient.id)
        assertTrue(foundClient.isPresent)
        assertEquals(persistedClient.name, foundClient.get().name)
    }

    @Test
    fun givenClients_whenFindByName_thenSuccess() {
        val client1 = Client(
            name = "Fulanito",
            rfc = "ABCD1234",
            email = "client1@example.com",
            phone = "1111111111",
            entryDate = LocalDate.now(),
            isActive = true
        )
        val client2 = Client(
            name = "Fulanito",
            rfc = "EFGH5678",
            email = "client2@example.com",
            phone = "2222222222",
            entryDate = LocalDate.now(),
            isActive = true
        )
        entityManager.persist(client1)
        entityManager.persist(client2)
        entityManager.flush()

        val foundClients = clientRepository.findByName("Shared Name")
        assertEquals(2, foundClients.size)
    }

    @Test
    fun givenExistingClient_whenUpdate_thenSuccess() {
        val client = Client(
            name = "Fulano",
            rfc = "ABCD012345EFG",
            email = "example@email.com",
            phone = "1234567890",
            entryDate = LocalDate.now(),
            isActive = true
        )
        val persistedClient = entityManager.persistFlushFind(client)

        val updatedClient = clientRepository.save(persistedClient.copy(name = "Mengano"))

        val foundClient = entityManager.find(Client::class.java, updatedClient.id)
        assertEquals("Updated Name", foundClient.name)
    }

    @Test
    fun givenExistingClient_whenSoftDelete_thenSuccess() {
        val client = Client(
            name = "Fulanito",
            rfc = "ABCD012345EFG",
            email = "example@email.com",
            phone = "1234567890",
            entryDate = LocalDate.now(),
            isActive = true
        )
        val persistedClient = entityManager.persistFlushFind(client)

        clientRepository.delete(persistedClient)

        val query = entityManager.entityManager
            .createNativeQuery("SELECT is_active FROM ${Tables.Client.NAME} WHERE id = :id")
            .setParameter("id", persistedClient.id)
        val isActive = query.singleResult as Boolean
        assertFalse(isActive)
    }

    @Test
    fun givenSoftDeletedClient_whenFind_thenFails() {
        val client = Client(
            name = "Fulanito",
            rfc = "ABCD012345EFG",
            email = "example@email.com",
            phone = "1234567890",
            entryDate = LocalDate.now(),
            isActive = true
        )
        val persistedClient = entityManager.persistFlushFind(client)
        val persistedId = persistedClient.id

        clientRepository.delete(persistedClient)

        val result = clientRepository.findById(persistedId)
        assertFalse(result.isPresent)
    }
}
