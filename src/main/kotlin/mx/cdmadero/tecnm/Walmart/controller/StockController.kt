package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.Stock
import mx.cdmadero.tecnm.Walmart.service.ProductService
import mx.cdmadero.tecnm.Walmart.service.StockService
import mx.cdmadero.tecnm.Walmart.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/stock")
class StockController {
    @Autowired
    lateinit var stockService: StockService
    @Autowired
    lateinit var storageService: StorageService
    @Autowired
    lateinit var productService: ProductService

    @GetMapping
    fun showStockPage(model: Model): String {
        model.addAttribute("storages", storageService.getAll())
        model.addAttribute("products", productService.getAll())
        model.addAttribute("stocks", stockService.getAll())
        return "stock/stocks"
    }

    @GetMapping("/filter")
    fun filterStock(
        @RequestParam(required = false) storageId: String?,
        @RequestParam(required = false) productId: String?,
        model: Model
    ): String {
        val storageIdLong = if (storageId == "default" || storageId.isNullOrEmpty()) null else storageId.toLongOrNull()
        val productIdLong = if (productId == "default" || productId.isNullOrEmpty()) null else productId.toLongOrNull()

        val filteredStock = when {
            storageIdLong == null && productIdLong == null -> stockService.getAll()
            storageIdLong != null && productIdLong == null -> stockService.getStockOfStorage(storageIdLong)
            storageIdLong != null && productIdLong != null -> stockService.getStockOfProductInStorage(productIdLong, storageIdLong)
            else -> return "redirect:/stock"
        }
        model.addAttribute("stocks", filteredStock)
        model.addAttribute("storages", storageService.getAll())
        model.addAttribute("products", productService.getAll())
        model.addAttribute("storageId", storageId)
        model.addAttribute("productId", productId)
        return "stock/stocks"
    }

    @GetMapping("/nuevo")
    fun showRegisterProductInStorageScreen(model: Model): String {
        model.addAttribute("storages", storageService.getAll())
        model.addAttribute("products", productService.getAll())
        return "stock/new_stock"
    }

    @PostMapping
    fun saveNewStock(
        @RequestParam storageId: Long,
        @RequestParam productId: Long,
        @RequestParam quantity: Int
    ): String {
        val storage = storageService.getById(storageId)
        val product = productService.getById(productId)
        if(storage == null || product == null) {
            return "redirect:/stock"
        }
        val stock = Stock(
            storage = storage,
            product = product,
            availableItems = quantity
        )
        stockService.save(stock)
        return "redirect:/stock"
    }

    @GetMapping("/editar/{id}")
    fun showEditStockPage(@PathVariable id: Long, model: Model): String {
        val stock = stockService.getById(id) ?: return "redirect:/stock"
        model.addAttribute("stock", stock)
        model.addAttribute("storages", storageService.getAll())
        model.addAttribute("products", productService.getAll())
        return "stock/show_stock"
    }

    @PutMapping("/{id}")
    fun updateStock(
        @PathVariable id: Long,
        @RequestParam storageId: Long,
        @RequestParam productId: Long,
        @RequestParam quantity: Int
    ): String {
        val storage = storageService.getById(storageId)
        val product = productService.getById(productId)

        if(storage == null || product == null) {
            return "redirect:/stock"
        }
        val stock = Stock(
            storage = storage,
            product =  product,
            availableItems = quantity
        )
        stockService.update(id, stock)
        return "redirect:/stock"
    }

    @DeleteMapping("/{id}")
    fun deleteStock(@PathVariable id: Long): String {
        stockService.delete(id)
        return "redirect:/stock"
    }
}