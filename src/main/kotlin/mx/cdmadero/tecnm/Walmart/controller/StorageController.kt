package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.Storage
import mx.cdmadero.tecnm.Walmart.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/almacenes")
class StorageController {
    @Autowired
    lateinit var storageService : StorageService

    @GetMapping
    fun showStoragesPage(model: Model): String {
        model.addAttribute("storages", storageService.getAll())
        return "storage/storages"
    }

    @GetMapping("/nuevo")
    fun showNewStoragePage(model: Model): String {
        return "storage/new_storage"
    }

    @GetMapping("/editar/{id}")
    fun showEditStoragePage(@PathVariable id: Long, model: Model): String {
        val storage = storageService.getById(id)
        if (storage != null) {
            model.addAttribute("storage", storage)
            return "storage/show_storage"
        }
        return "redirect:/almacenes"
    }

    @PostMapping
    fun saveStorage(
        @RequestParam name: String,
        @RequestParam address: String,
        @RequestParam lat: Double,
        @RequestParam lon: Double,
        @RequestParam capacity: Int,
        @RequestParam typeVehicle: String) : String{
        val storage = Storage(
            name = name,
            address = address,
            lat = lat,
            lon = lon,
            capacity = capacity,
            typeVehicle = typeVehicle
        )
        storageService.save(storage)
        return "redirect:/almacenes"
    }

    @PutMapping("/{id}")
    fun updateStorage(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam address: String,
        @RequestParam lat: Double,
        @RequestParam lon: Double,
        @RequestParam capacity: Int,
        @RequestParam typeVehicle: String): String {
        val storage = Storage(
            name = name,
            address = address,
            lat = lat,
            lon = lon,
            capacity = capacity,
            typeVehicle = typeVehicle
        )
        storageService.update(id, storage)
        return "redirect:/almacenes"
    }

    @DeleteMapping("/{id}")
    fun deleteStorage(@PathVariable id: Long): String {
        storageService.delete(id)
        return "redirect:/almacenes"
    }
}