package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.OriginDestiny
import mx.cdmadero.tecnm.Walmart.model.Transaction
import mx.cdmadero.tecnm.Walmart.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/transacciones")
class TransactionController {

    @Autowired
    lateinit var transactionService: TransactionService
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

    @GetMapping("/nueva")
    fun showNewTransactionPage(model: Model): String {
        model.addAttribute("storages", storageService.getAll())
        model.addAttribute("products", productService.getAll())
        model.addAttribute("employees", employeeService.getAll())
        return "transaction/new_transaction"
    }

    @GetMapping
    fun showTransactionPage(model: Model): String {
        model.addAttribute("transactions", transactionService.getAll())
        model.addAttribute("products", productService.getAll())
        model.addAttribute("providers", providerService.getAll())
        model.addAttribute("clients", clientService.getAll())
        model.addAttribute("employees", employeeService.getAll())
        model.addAttribute("storages", storageService.getAll())
        return "transaction/transactions"
    }

    @GetMapping("/filtrar")
    fun filterTransactions(
        @RequestParam(required = false) startDate: String,
        @RequestParam(required = false) endDate: String,
        @RequestParam(required = false) filter: String?,
        @RequestParam(required = false) filterValue: String?,
        @RequestParam(required = false) secondFilterValue: Long?,
        model: Model
    ): String {
        val filteredTransactions = transactionService.filterTransactions(
            startDate, endDate, filter, filterValue, secondFilterValue
        )

        model.addAttribute("transactions", filteredTransactions)
        model.addAttribute("products", productService.getAll())
        model.addAttribute("providers", providerService.getAll())
        model.addAttribute("clients", clientService.getAll())
        model.addAttribute("employees", employeeService.getAll())
        model.addAttribute("storages", storageService.getAll())

        return "transaction/transactions"
    }

    @GetMapping("/editar/{id}")
    fun showEditTransactionPage(@PathVariable id: Long, model: Model): String {
        val transaction = transactionService.getById(id)
        if (transaction != null) {
            val originDestinyType = transactionService.getOriginDestinyType(transaction.originDestiny!!)
            model.addAttribute("originDestinyType", originDestinyType)
            model.addAttribute("transaction", transaction)
            model.addAttribute("storages", storageService.getAll())
            model.addAttribute("products", productService.getAll())
            model.addAttribute("employees", employeeService.getAll())
            return "transaction/show_transaction"
        }
        return "redirect:/transacciones"
    }

    @PostMapping
    fun saveTransaction(
        @RequestParam storageId: Long,
        @RequestParam productId: Long,
        @RequestParam reason: String,
        @RequestParam quantity: Int,
        @RequestParam type: String,
        @RequestParam employeeId: Long,
        @RequestParam originDestinyType: String,
        @RequestParam originDestinyId: Long
    ): String {
        val storage = storageService.getById(storageId) ?: return "redirect:/transacciones"
        val product = productService.getById(productId) ?: return "redirect:/transacciones"
        val employee = employeeService.getById(employeeId) ?: return "redirect:/transacciones"
        val originDestiny: OriginDestiny? = when (originDestinyType) {
            "providers" -> providerService.getById(originDestinyId)
            "clients" -> clientService.getById(originDestinyId)
            "storages" -> storageService.getById(originDestinyId)
            else -> null
        }
        val transaction = Transaction(
            storage = storage,
            product = product,
            reason = reason,
            quantity = quantity,
            type = type,
            originDestiny = originDestiny,
            employee = employee
        )
        transactionService.save(transaction)
        return "redirect:/transacciones"
    }

    @PutMapping("/{id}")
    fun updateTransaction(
        @PathVariable id: Long,
        @RequestParam storageId: Long,
        @RequestParam productId: Long,
        @RequestParam reason: String,
        @RequestParam quantity: Int,
        @RequestParam type: String,
        @RequestParam originDestinyType: String,
        @RequestParam originDestinyId: Long,
        @RequestParam employeeId: Long
    ): String {
        val storage = storageService.getById(storageId) ?: return "redirect:/transacciones"
        val product = productService.getById(productId) ?: return "redirect:/transacciones"
        val employee = employeeService.getById(employeeId) ?: return "redirect:/transacciones"
        val originDestiny = transactionService.getOriginDestiny(originDestinyId, originDestinyType)
            ?: return "redirect:/transacciones"

        val transaction = Transaction(
            id = id,
            storage = storage,
            product = product,
            reason = reason,
            quantity = quantity,
            type = type,
            employee = employee,
            originDestiny = originDestiny
        )
        transactionService.save(transaction)
        return "redirect:/transacciones"
    }

    @DeleteMapping("/{id}")
    fun deleteTransaction(@PathVariable id: Long) {
        transactionService.delete(id);
    }
}