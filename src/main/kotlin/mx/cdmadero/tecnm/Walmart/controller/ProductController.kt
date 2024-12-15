package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.Product
import mx.cdmadero.tecnm.Walmart.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/productos")
class ProductController {
    @Autowired
    lateinit var productService: ProductService

    @GetMapping()
    fun showProductsPage(model: Model): String {
        model.addAttribute("products", productService.getAll())
        return "product/products"
    }

    @GetMapping("/nuevo")
    fun showNewProductPage(model: Model) : String {
        return "product/new_product"
    }

    @GetMapping("/editar/{id}")
    fun showEditProductPage(@PathVariable id: Long, model: Model): String {
        val product = productService.getById(id)
        if (product != null) {
            val lastPrice = unformatPrice(product.lastPrice)
            val lastCost  = unformatPrice(product.lastCost)
            model.addAttribute("product", product.copy(
                lastPrice = lastPrice,
                lastCost = lastCost
            ))
            return "product/show_product"
        }
        return "redirect:/productos"
    }


    @PostMapping()
    fun saveProduct(
        @RequestParam name: String,
        @RequestParam upc: String,
        @RequestParam description: String,
        @RequestParam lastCost: String,
        @RequestParam lastPrice: String
    ): String {
        val product = Product(
            name = name,
            upc = upc,
            description = description,
            lastCost = formatPrice(lastCost),
            lastPrice = formatPrice(lastPrice)
        )
        productService.save(product)
        return "redirect:/productos"
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam upc: String,
        @RequestParam description: String,
        @RequestParam lastCost: String,
        @RequestParam lastPrice: String
    ): String {
        val product = Product(
            name = name,
            upc = upc,
            description = description,
            lastCost = formatPrice(lastCost),
            lastPrice = formatPrice(lastPrice)
        )
        productService.update(id, product)
        return "redirect:/productos"
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): String {
        productService.delete(id)
        return "redirect:/productos"
    }

    private fun formatPrice(price: String): String {
        return if(price.contains(".")) {
            "$$price"
        } else "$$price.00"
    }

    private fun unformatPrice(price: String) : String {
        return price.replace("$", "")
    }
}