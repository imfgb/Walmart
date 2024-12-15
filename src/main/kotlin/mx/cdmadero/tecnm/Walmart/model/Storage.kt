package mx.cdmadero.tecnm.Walmart.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
data class Storage(
    val name: String = "",
    val address: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val capacity: Int? = null,
    val typeVehicle: String? = null,

    @OneToMany(mappedBy = "storage", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonBackReference
    val locations: List<Location> = emptyList()
): OriginDestiny()