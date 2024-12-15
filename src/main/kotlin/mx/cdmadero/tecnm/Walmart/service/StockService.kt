package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.Stock
import mx.cdmadero.tecnm.Walmart.repository.StockRepository
import org.springframework.stereotype.Service

@Service
class StockService(
    private val stockRepository: StockRepository,
) {
    fun save(stock: Stock): Stock {
        return stockRepository.save(stock)
    }

    fun getAll(): List<Stock> {
        return stockRepository.findAll()
    }

    fun getById(id: Long): Stock? =
        stockRepository.findById(id).orElse(null)

    fun update(id: Long, newStock: Stock) : Stock?{
        val stock = stockRepository.findById(id)
        return if (stock.isPresent) {
            val updated = stock.get().copy(
                availableItems = newStock.availableItems,
                storage = newStock.storage,
                product = newStock.product,
                isActive = newStock.isActive
            )
            stockRepository.save(updated)
        } else null
    }

    fun delete(id: Long) {
        val stock = getById(id)
        if(stock === null) {
            return
        }
        stockRepository.delete(stock)
    }


    fun getStockOfStorage(storageId: Long): List<Stock> =
        stockRepository.findByStorage(storageId)

    fun getStockOfProductInStorage(productId: Long, storageId: Long): Stock? =
        stockRepository.findByProductAndStorage(productId, storageId)
}