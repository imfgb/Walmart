package mx.cdmadero.tecnm.Walmart.model

import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = Tables.Stock.NAME)
@SQLDelete(sql = Tables.Stock.DELETE_MODE)
@SQLRestriction("is_active = true")
data class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "storage_id")
    val storage: Storage = Storage(),

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product = Product(),

    val availableItems: Int = 0,
    val isActive: Boolean = true,
)