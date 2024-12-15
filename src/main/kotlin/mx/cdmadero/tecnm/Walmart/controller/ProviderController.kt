package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.Provider
import mx.cdmadero.tecnm.Walmart.service.ProviderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/proveedores")
class ProviderController {

    @Autowired
    lateinit var providerService: ProviderService

    @GetMapping()
    fun showProvidersPage(model: Model): String {
        model.addAttribute("providers", providerService.getAll())
        return "provider/providers"
    }

    @GetMapping("/nuevo")
    fun showNewProviderPage(): String {
        return "provider/new_provider"
    }

    @GetMapping("/editar/{id}")
    fun showEditProviderPage(@PathVariable id: Long, model: Model): String {
        val provider = providerService.getById(id)
        return if (provider != null) {
            model.addAttribute("provider", provider)
            "provider/show_provider"
        } else {
            "redirect:/proveedores"
        }
    }

    @PostMapping()
    fun saveProvider(
        @RequestParam name: String,
        @RequestParam rfc: String,
        @RequestParam email: String,
        @RequestParam phone: String
    ): String {
        val provider = Provider(
            name = name,
            rfc = rfc,
            email = email,
            phone = phone
        )
        providerService.save(provider)
        return "redirect:/proveedores"
    }

    @PutMapping("/{id}")
    fun updateProvider(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam rfc: String,
        @RequestParam email: String,
        @RequestParam phone: String
    ): String {
        val provider = Provider(
            name = name,
            rfc = rfc,
            email = email,
            phone = phone
        )
        providerService.update(id, provider)
        return "redirect:/proveedores"
    }

    @DeleteMapping("/{id}")
    fun deleteProvider(@PathVariable id: Long): String {
        providerService.delete(id)
        return "redirect:/proveedores"
    }
}
