package mx.cdmadero.tecnm.Walmart.model

import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate

@Entity
data class Client(
    val name: String = "",
    val rfc: String = "",
    val email: String = "",
    val phone: String = "",
    val entryDate: LocalDate = LocalDate.now(),

): OriginDestiny()

