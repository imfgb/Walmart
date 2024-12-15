package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.Provider
import mx.cdmadero.tecnm.Walmart.repository.ProviderRepository
import org.springframework.stereotype.Service

@Service
class ProviderService(private val providerRepository: ProviderRepository) {
    fun getAll(): List<Provider> = providerRepository.findAll()

    fun save(provider: Provider): Provider = providerRepository.save(provider)

    fun getById(id: Long): Provider? = providerRepository.findById(id).orElse(null)

    fun update(id: Long, newProvider: Provider): Provider? {
        val provider = providerRepository.findById(id)
        return if (provider.isPresent) {
            val updated = provider.get().copy(
                name = newProvider.name,
                rfc = newProvider.rfc,
                email = newProvider.email,
                phone = newProvider.phone,
                createdOn = newProvider.createdOn
            )
            providerRepository.save(updated)
        } else null
    }

    fun delete(id: Long) {
        val provider = providerRepository.findById(id)
        if (provider.isPresent) {
            providerRepository.delete(provider.get())
        }
    }
}