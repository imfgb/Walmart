package mx.cdmadero.tecnm.Walmart.model

import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = Tables.Transaction.NAME)
@SQLDelete(sql = Tables.Transaction.DELETE_MODE)
@SQLRestriction("is_active = true")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "storage_id")
    val storage: Storage = Storage(),

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product = Product(),

    val reason: String = "",
    val dateTime: LocalDateTime = LocalDateTime.now(), // For better precision
    val quantity: Int = 0,
    val type: String = "IN", // IN or OUT

    @ManyToOne
    @JoinColumn(name = "origin_destiny_id")
    val originDestiny: OriginDestiny? = null,

    val isActive: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "employee_id")
    val employee: Employee = Employee()
)