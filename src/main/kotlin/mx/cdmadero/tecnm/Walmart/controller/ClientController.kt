package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.Client
import mx.cdmadero.tecnm.Walmart.service.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/clientes")
class ClientController {
    @Autowired
    lateinit var clientService: ClientService

    @GetMapping()
    fun showClientPage(model: Model): String {
        model.addAttribute("clients", clientService.getAll())
        return "client/clients"
    }

    @GetMapping("/nuevo")
    fun showNewClientPage(model: Model): String {
        return "client/new_client"
    }

    @GetMapping("/editar/{id}")
    fun showEditClientPage(@PathVariable id: Long, model: Model): String {
        val client = clientService.getById(id)
        if (client != null) {
            model.addAttribute("client", client)
            return "client/show_client"
        }
        return "redirect:/clientes"
    }

    @PostMapping()
    fun saveClient(@RequestParam name: String,
                    @RequestParam rfc: String,
                    @RequestParam email: String,
                    @RequestParam phone: String) : String{
        val client = Client(
            name = name,
            rfc = rfc,
            email = email,
            phone = phone
        )
        clientService.save(client)
        return "redirect:/clientes"
    }

    @PutMapping("/{id}")
    fun updateStorage(@RequestParam id: Long,
                      @RequestParam name: String,
                      @RequestParam rfc: String,
                      @RequestParam email: String,
                      @RequestParam phone: String): String {
        val client = Client(
            name = name,
            rfc = rfc,
            email = email,
            phone = phone
        )
        clientService.update(id, client)
        return "redirect:/clientes"
    }

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Long): String {
        clientService.delete(id)
        return "redirect:/clientes"
    }
}