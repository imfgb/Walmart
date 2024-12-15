package mx.cdmadero.tecnm.Walmart.service

import mx.cdmadero.tecnm.Walmart.model.Employee
import mx.cdmadero.tecnm.Walmart.repository.EmployeeRepository
import org.springframework.stereotype.Service

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {
    fun getAll(): List<Employee> = employeeRepository.findAll()

    fun save(employee: Employee): Employee = employeeRepository.save(employee)

    fun getById(id: Long): Employee? = employeeRepository.findById(id).orElse(null)

    fun update(id: Long, nuevoEmployee: Employee): Employee? {
        val employee = employeeRepository.findById(id)
        return if (employee.isPresent) {
            val updated = employee.get().copy(
                name = nuevoEmployee.name,
                rfc = nuevoEmployee.rfc,
                email = nuevoEmployee.email,
                phone = nuevoEmployee.phone,
                entryDate = nuevoEmployee.entryDate
            )
            employeeRepository.save(updated)
        } else null
    }

    fun delete(id: Long) {
        val employee = employeeRepository.findById(id)
        if (employee.isPresent) {
            employeeRepository.delete(employee.get())
        }
    }
}