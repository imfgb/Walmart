package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.*
import mx.cdmadero.tecnm.Walmart.repository.TransactionRepository
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val storageService: StorageService,
    private val clientService: ClientService,
    private val providerService: ProviderService
) {
    fun getAll(): List<Transaction> =
        transactionRepository.findAll()

    fun getAllByDateRange(startDate: String, endDate: String): List<Transaction> =
        transactionRepository.findAllInDateRange(startDate, endDate)

    fun getById(id: Long): Transaction? {
        val transaction = transactionRepository.findById(id)
        return if(transaction.isPresent) {
            transaction.get()
        } else null
    }

    fun save(transaction: Transaction): Transaction? =
        transactionRepository.save(transaction)

    fun getAllByOriginStorage(storageId: Long, startDate : String, endDate: String): List<Transaction> =
        transactionRepository.findByOriginStorage(storageId, startDate, endDate)

    fun getAllByEmployee(employeeId: Long, startDate: String, endDate: String): List<Transaction> =
        transactionRepository.findByEmployee(employeeId, startDate, endDate)

    fun getAllByClient(clientId: Long, startDate: String, endDate: String): List<Transaction> {
        return transactionRepository.findByOriginDestiny(clientId, parseStartOfDay(startDate), parseEndOfDay(endDate))
    }

    fun getAllByProvider(providerId: Long, startDate: String, endDate: String): List<Transaction> =
        transactionRepository.findByOriginDestiny(providerId, parseStartOfDay(startDate), parseEndOfDay(endDate))

    fun getAllByOriginDestinyStorage(storageId : Long, startDate: String, endDate: String) : List<Transaction> =
        transactionRepository.findByOriginDestiny(storageId, parseStartOfDay(startDate), parseEndOfDay(endDate))

    fun getAllByStorageAndProduct(productId: Long, storageId: Long, startDate: String, endDate: String): List<Transaction> =
        transactionRepository.findByProductAndStorage(productId, storageId, startDate, endDate)

    fun filterTransactions(
        startDate: String?,
        endDate: String?,
        filter: String?,
        filterValue: String?,
        secondFilterValue: Long?
    ): List<Transaction> {
        return when (filter) {
            "employees" -> {
                if(startDate == null || endDate == null || filterValue == null) {
                    return emptyList()
                }
                return getAllByEmployee(filterValue.toLong(), startDate, endDate)
            }
            "storages" -> {
                if(startDate == null || endDate == null || filterValue == null) {
                    return emptyList()
                }
                return getAllByOriginStorage(filterValue.toLong(), startDate, endDate)
            }
            "originDestiny" -> {
                if(startDate == null || endDate == null || filterValue == null || secondFilterValue == null) {
                    return emptyList()
                }
                return when(filterValue) {
                    "providers" -> getAllByProvider(secondFilterValue, startDate, endDate)
                    "clients" -> getAllByClient(secondFilterValue, startDate, endDate)
                    "storages" -> getAllByOriginDestinyStorage(secondFilterValue, startDate, endDate)
                    else -> emptyList()
                }
            }
            "storagesAndProducts" -> {
                if(startDate == null || endDate == null || filterValue == null || secondFilterValue == null) {
                    return emptyList()
                }
                return getAllByStorageAndProduct(filterValue.toLong(), secondFilterValue, startDate, endDate)
            }
            else -> {
                if(startDate == null || endDate == null) {
                    return emptyList()
                }
                return getAllByDateRange(startDate, endDate)
            }
        }
    }

    fun delete(id: Long) {
        val transaction = getById(id) ?: return
        transactionRepository.delete(transaction)
    }

    fun getOriginDestinyType(originDestiny: OriginDestiny): String {
        return when(originDestiny) {
            is Client -> "clients"
            is Provider -> "providers"
            is Storage -> "storages"
            else -> ""
        }
    }

    fun getOriginDestiny(id: Long, type: String): OriginDestiny? {
        return when(type) {
            "clients" -> clientService.getById(id)
            "storages" -> storageService.getById(id)
            "providers" -> providerService.getById(id)
            else -> null
        }
    }

    fun parseStartOfDay(date: String): LocalDateTime =
        LocalDate.parse(date).atStartOfDay()

    fun parseEndOfDay(date: String): LocalDateTime =
        LocalDate.parse(date).atTime(23, 59, 59, 999_999_999)
}