package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.Client
import mx.cdmadero.tecnm.Walmart.model.Provider
import mx.cdmadero.tecnm.Walmart.repository.ClientRepository
import org.springframework.stereotype.Service

@Service
class ClientService(private val clientRepository: ClientRepository) {
    fun getAll(): List<Client> = clientRepository.findAll()

    fun save(client: Client): Client = clientRepository.save(client)

    fun getById(id: Long): Client? = clientRepository.findById(id).orElse(null)

    fun delete(id: Long) {
        val client = clientRepository.findById(id)
        if (client.isPresent) {
            clientRepository.delete(client.get())
        }
    }

    fun update(id: Long, newClient: Client): Client? {
        val client = clientRepository.findById(id)
        return if (client.isPresent) {
            val updated = client.get().copy(
                name = newClient.name,
                rfc = newClient.rfc,
                email = newClient.email,
                phone = newClient.phone,
            )
            clientRepository.save(updated)
        } else null
    }
}