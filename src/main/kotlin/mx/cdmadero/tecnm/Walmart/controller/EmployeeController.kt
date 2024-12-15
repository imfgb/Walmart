package mx.cdmadero.tecnm.Walmart.controller

import mx.cdmadero.tecnm.Walmart.model.Employee
import mx.cdmadero.tecnm.Walmart.service.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class EmployeeController {

    @Autowired
    lateinit var employeeService: EmployeeService

    @GetMapping("/empleados")
    fun showEmployeePage(model: Model): String {
        model.addAttribute("employees", employeeService.getAll())
        return "employee/employees"
    }

    @GetMapping("/empleados/nuevo")
    fun showNewEmployeePage(): String {
        return "employee/new_employee"
    }

    @GetMapping("/empleados/editar/{id}")
    fun showEditEmployeePage(@PathVariable id: Long, model: Model): String {
        val employee = employeeService.getById(id)
        return if (employee != null) {
            model.addAttribute("employee", employee)
            "employee/show_employee"
        } else {
            "redirect:/empleados"
        }
    }

    @PostMapping("/empleados")
    fun saveEmployee(
        @RequestParam name: String,
        @RequestParam rfc: String,
        @RequestParam email: String,
        @RequestParam phone: String
    ): String {
        val employee = Employee(
            name = name,
            rfc = rfc,
            email = email,
            phone = phone
        )
        employeeService.save(employee)
        return "redirect:/empleados"
    }

    // Actualizar un empleado existente
    @PutMapping("/empleados/{id}")
    fun updateEmployee(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam rfc: String,
        @RequestParam email: String,
        @RequestParam phone: String
    ): String {
        val employee = Employee(
            id = id,
            name = name,
            rfc = rfc,
            email = email,
            phone = phone
        )
        employeeService.update(id, employee)
        return "redirect:/empleados"
    }

    // Eliminar un empleado
    @DeleteMapping("/empleados/{id}")
    fun deleteEmployee(@PathVariable id: Long): String {
        employeeService.delete(id)
        return "redirect:/empleados"
    }
}
