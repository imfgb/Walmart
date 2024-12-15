package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.Location
import mx.cdmadero.tecnm.Walmart.repository.LocationRepository
import org.springframework.stereotype.Service

@Service
class LocationService(private val locationRepository: LocationRepository) {
    fun getAll(): List<Location> = locationRepository.findAll()

    fun save(location: Location): Location = locationRepository.save(location)

    fun getById(id: Long): Location? = locationRepository.findById(id).orElse(null)

    fun update(id: Long, newLocation: Location): Location? {
        val location = locationRepository.findById(id)
        return if (location.isPresent) {
            val updated = location.get().copy(
                hallway = newLocation.hallway,
                shelf = newLocation.shelf,
                level = newLocation.level
            )
            locationRepository.save(updated)
        } else null
    }

    fun delete(id: Long) {
        val location = locationRepository.findById(id)
        if (location.isPresent) {
            locationRepository.delete(location.get())
        }
    }
}