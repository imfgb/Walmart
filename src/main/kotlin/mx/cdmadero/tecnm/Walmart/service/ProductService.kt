package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.Product
import mx.cdmadero.tecnm.Walmart.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getAll(): List<Product> = productRepository.findAll()

    fun save(product: Product): Product = productRepository.save(product)

    fun getById(id: Long): Product? {
        return productRepository.findById(id).orElse(null) // Or is active
    }

    fun update(id: Long, newProduct: Product): Product? {
        val product = productRepository.findById(id)
        return if (product.isPresent) {
            val updatedProduct = product.get().copy(
                upc = newProduct.upc,
                description = newProduct.description,
                lastCost = newProduct.lastCost,
                lastPrice = newProduct.lastPrice,
                isActive = newProduct.isActive
            )
            productRepository.save(updatedProduct)
        } else null
    }

    fun delete(id: Long) {
        val product = productRepository.findById(id)
        if (product.isPresent) {
            productRepository.delete(product.get())
        }
    }
}