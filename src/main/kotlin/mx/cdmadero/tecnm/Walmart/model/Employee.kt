package mx.cdmadero.tecnm.Walmart.model

import jakarta.persistence.*
import mx.cdmadero.tecnm.Walmart.shared.Tables
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = Tables.Employee.NAME)
@SQLDelete(sql = Tables.Employee.DELETE_MODE)
@SQLRestriction("is_active = true")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = "",
    val rfc: String  = "",
    val email: String = "",
    val phone: String = "",
    val entryDate: String = "",
    val isActive: Boolean = true,
)