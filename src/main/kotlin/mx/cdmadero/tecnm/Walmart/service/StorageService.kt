package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.Storage
import mx.cdmadero.tecnm.Walmart.repository.StorageRepository
import org.springframework.stereotype.Service

@Service
class StorageService(private val storageRepository: StorageRepository) {
    fun getAll(): List<Storage> = storageRepository.findAll()

    fun save(storage: Storage): Storage = storageRepository.save(storage)

    fun getById(id: Long): Storage? = storageRepository.findById(id).orElse(null)

    fun update(id: Long, newStorage: Storage): Storage? {
        val storage = storageRepository.findById(id)
        return if (storage.isPresent) {
            val updated = storage.get().copy(
                name = newStorage.name,
                address = newStorage.address,
                lat = newStorage.lat,
                lon = newStorage.lon,
                capacity = newStorage.capacity,
                typeVehicle = newStorage.typeVehicle,
            )
            storageRepository.save(updated)
        } else null
    }

    fun delete(id: Long) {
        val storage = storageRepository.findById(id)
        if (storage.isPresent) {
            storageRepository.delete(storage.get())
        }
    }
}