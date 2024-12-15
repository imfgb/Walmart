package mx.cdmadero.tecnm.Walmart.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = Tables.Product.NAME)
@SQLDelete(sql = Tables.Product.DELETE_MODE)
@SQLRestriction("is_active = true")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = "",
    val upc: String = "",
    val description: String = "",
    val lastCost: String = "",
    val lastPrice: String = "",
    val isActive: Boolean = true,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonBackReference
    val transactions: List<Transaction> = emptyList()
)