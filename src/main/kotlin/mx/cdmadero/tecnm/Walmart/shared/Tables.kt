package mx.cdmadero.tecnm.Walmart.shared

object Tables {
    object Storage {
        const val NAME = "almacenes"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
    object Stock {
        const val NAME = "almacen_productos"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
    object Client {
        const val NAME = "clientes"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
    object Employee {
        const val NAME = "empleados"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
    object Location {
        const val NAME = "ubicaciones"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
    object Product {
        const val NAME = "productos"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
    object Provider {
        const val NAME = "proveedores"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
    object Transaction {
        const val NAME = "transacciones"
        const val DELETE_MODE = "UPDATE $NAME SET is_active = false WHERE id = ?"
    }
}