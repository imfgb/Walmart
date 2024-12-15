package mx.cdmadero.tecnm.Walmart.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = Tables.Location.NAME)
@SQLDelete(sql = Tables.Location.DELETE_MODE)
@SQLRestriction("is_active = true")
data class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val hallway: Int = -1,
    val shelf: Int = -1,
    val level: Int = -1,
    val isActive: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "storage_id")
    @JsonManagedReference
    val storage: Storage = Storage(),

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    val product: Product = Product()
)
