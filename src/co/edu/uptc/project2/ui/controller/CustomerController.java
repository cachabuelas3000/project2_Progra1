package co.edu.uptc.project2.ui.controller;

import java.util.Scanner;

import co.edu.uptc.project2.domain.Customer;
import co.edu.uptc.project2.enums.DocumentType;
import co.edu.uptc.project2.service.CustomerService;
import co.edu.uptc.project2.ui.view.CustomerView;

public class CustomerController {

    private CustomerService service;
    private CustomerView view;
    private Scanner scanner;

    public CustomerController(Scanner scanner, CustomerService service) {
        this.service = service;
        this.view = new CustomerView();
        this.scanner = scanner;
    }

    // Menú principal de clientes
    public void showMenu() {
        int option = -1;
        do {
            view.showMenu();
            try {
                option = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                view.showMessage("Opción inválida.");
                continue;
            }

            switch (option) {
                case 1 -> createCustomer();
                case 2 -> listAllCustomers();
                case 3 -> findCustomerById();
                case 4 -> updateCustomer();
                case 5 -> deleteCustomer();
                case 0 -> view.showMessage("Volviendo al menú principal...");
                default -> view.showMessage("Opción no válida.");
            }
        } while (option != 0);
    }

    private void createCustomer() {
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();

        System.out.print("Apellido: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Teléfono: ");
        String phone = scanner.nextLine().trim();

        DocumentType docType = selectDocumentType();

        System.out.print("Número de documento: ");
        String docNumber = scanner.nextLine().trim();

        Customer customer = new Customer(name, lastName, email, phone, docType, docNumber);
        boolean result = service.createCustomer(customer);
        view.showMessage(result ? "Cliente creado exitosamente."
                : "Error: no se pudo crear el cliente (ya existe o datos inválidos).");
    }

    private void listAllCustomers() {
        view.showAllCustomers(service.getAllCustomers());
    }

    private void findCustomerById() {
        System.out.print("Número de documento a buscar: ");
        String docNumber = scanner.nextLine().trim();
        view.showCustomer(service.getCustomerById(docNumber));
    }

    private void updateCustomer() {
        System.out.print("Número de documento del cliente a actualizar: ");
        String docNumber = scanner.nextLine().trim();

        Customer existing = service.getCustomerById(docNumber);
        if (existing == null) {
            view.showMessage("Cliente no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre (actual: " + existing.getName() + ", ENTER para mantener): ");
        String name = scanner.nextLine().trim();
        if (name.isBlank()) name = existing.getName();

        System.out.print("Nuevo apellido (actual: " + existing.getLastName() + ", ENTER para mantener): ");
        String lastName = scanner.nextLine().trim();
        if (lastName.isBlank()) lastName = existing.getLastName();

        System.out.print("Nuevo email (actual: " + existing.getEmail() + ", ENTER para mantener): ");
        String email = scanner.nextLine().trim();
        if (email.isBlank()) email = existing.getEmail();

        System.out.print("Nuevo teléfono (actual: " + existing.getPhone() + ", ENTER para mantener): ");
        String phone = scanner.nextLine().trim();
        if (phone.isBlank()) phone = existing.getPhone();

        Customer updated = new Customer(name, lastName, email, phone,
                existing.getDocumentType(), existing.getDocumentNumber());

        boolean result = service.updateCustomer(docNumber, updated);
        view.showMessage(result ? "Cliente actualizado exitosamente." : "Error al actualizar el cliente.");
    }

    private void deleteCustomer() {
        System.out.print("Número de documento del cliente a eliminar: ");
        String docNumber = scanner.nextLine().trim();
        boolean result = service.deleteCustomer(docNumber);
        view.showMessage(result ? "Cliente eliminado exitosamente." : "Cliente no encontrado.");
    }

    private DocumentType selectDocumentType() {
        System.out.println("Tipo de documento:");
        System.out.println("  1. CC (Cédula de Ciudadanía)");
        System.out.println("  2. TI (Tarjeta de Identidad)");
        System.out.println("  3. PA (Pasaporte nacional)");
        System.out.println("  4. PASSPORT (Pasaporte extranjero)");
        System.out.print("Seleccione: ");
        String opt = scanner.nextLine().trim();
        return switch (opt) {
            case "1" -> DocumentType.CC;
            case "2" -> DocumentType.TI;
            case "3" -> DocumentType.PA;
            case "4" -> DocumentType.PASSPORT;
            default -> {
                System.out.println("Opción inválida, se usará CC por defecto.");
                yield DocumentType.CC;
            }
        };
    }
}