package mx.cdmadero.tecnm.Walmart.model

import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.Date

@Entity
data class Provider(
    val name: String = "",
    val rfc: String = "",
    val email: String = "",
    val phone: String = "",
    val createdOn: Date = Date(),
) : OriginDestiny()