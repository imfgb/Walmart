package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DataController {
    @Autowired
    lateinit var employeeService: EmployeeService
    @Autowired
    lateinit var clientService: ClientService
    @Autowired
    lateinit var providerService: ProviderService
    @Autowired
    lateinit var storageService: StorageService
    @Autowired
    lateinit var productService: ProductService

    @GetMapping("/filter-data")
    fun getFilterData(@RequestParam filter: String): List<Any> {
        return when (filter) {
            "products" -> productService.getAll()
            "providers" -> providerService.getAll()
            "clients" -> clientService.getAll()
            "employees" -> employeeService.getAll()
            "storages" -> storageService.getAll()
            else -> emptyList()
        }
    }
}