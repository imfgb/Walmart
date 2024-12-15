package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.Location
import mx.cdmadero.tecnm.Walmart.service.LocationService
import mx.cdmadero.tecnm.Walmart.service.ProductService
import mx.cdmadero.tecnm.Walmart.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/ubicaciones")
class LocationController {
    @Autowired
    lateinit var locationService: LocationService
    @Autowired
    lateinit var productService: ProductService
    @Autowired
    lateinit var storageService: StorageService

    @GetMapping
    fun showLocationPage(model: Model): String {
        val tmp = locationService.getAll()
        model.addAttribute("locations", tmp)
        return "location/locations"
    }

    @GetMapping("/nuevo")
    fun showNewLocationPage(model: Model): String {
        model.addAttribute("products", productService.getAll())
        model.addAttribute("storages", storageService.getAll())
        return "location/new_location"
    }

    @GetMapping("/editar/{id}")
    fun showEditLocationPage(@PathVariable id: Long, model: Model): String {
        val location = locationService.getById(id) ?: return "redirect:location/locations"
        val products = productService.getAll()
        val storages = storageService.getAll()
        model.addAttribute("location", location)
        model.addAttribute("products", products)
        model.addAttribute("storages", storages)
        return "location/show_location"
    }

    @PostMapping
    fun saveLocation(
        @RequestParam storageId: Long,
        @RequestParam productId: Long,
        @RequestParam hallway: Int,
        @RequestParam shelf: Int,
        @RequestParam level: Int
    ): String {
        val storage = storageService.getById(storageId) ?: return "redirect:/ubicaciones"
        val product = productService.getById(productId) ?: return "redirect:/ubicaciones"
        val location = Location(
            product = product,
            storage = storage,
            hallway = hallway,
            shelf = shelf,
            level = level
        )
        locationService.save(location)
        return "redirect:/ubicaciones"
    }

    @PutMapping("/{id}")
    fun updateLocation(
        @PathVariable id: Long,
        @RequestParam storageId: Long,
        @RequestParam productId: Long,
        @RequestParam hallway: Int,
        @RequestParam shelf: Int,
        @RequestParam level: Int
    ): String {
        val storage = storageService.getById(storageId) ?: return "redirect:/ubicaciones"
        val product = productService.getById(productId) ?: return "redirect:/ubicaciones"
        val location = Location(
            product = product,
            storage = storage,
            hallway = hallway,
            shelf = shelf,
            level = level
        )
        locationService.update(id, location)
        return "redirect:/ubicaciones"
    }
    @DeleteMapping("/{id}")
    fun deleteLocation(@PathVariable id: Long): String {
        locationService.delete(id)
        return "redirect:/ubicaciones"
    }
}