package co.edu.uptc.project2.ui.view;

import java.util.List;

import co.edu.uptc.project2.domain.Customer;

public class CustomerView {

    // Mostrar menú de cliente
    public void showMenu() {
        System.out.println("\n========== GESTIÓN DE CLIENTES ==========");
        System.out.println("1. Crear cliente");
        System.out.println("2. Consultar todos los clientes");
        System.out.println("3. Consultar cliente por número de documento");
        System.out.println("4. Actualizar cliente");
        System.out.println("5. Eliminar cliente");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    // Mostrar un cliente
    public void showCustomer(Customer customer) {
        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("\n--- Datos del Cliente ---");
        System.out.println("Nombre:           " + customer.getName() + " " + customer.getLastName());
        System.out.println("Tipo documento:   " + customer.getDocumentType());
        System.out.println("Núm. documento:   " + customer.getDocumentNumber());
        System.out.println("Email:            " + customer.getEmail());
        System.out.println("Teléfono:         " + customer.getPhone());
        System.out.println("-------------------------");
    }

    // Mostrar lista de clientes
    public void showAllCustomers(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("\n========== LISTA DE CLIENTES ==========");
        for (Customer c : customers) {
            showCustomer(c);
        }
    }

    // Mensajes de éxito / error
    public void showMessage(String message) {
        System.out.println(message);
    }
}