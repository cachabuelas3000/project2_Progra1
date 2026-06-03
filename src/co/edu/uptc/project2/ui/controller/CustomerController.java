package co.edu.uptc.project2.ui.controller;

import java.util.Scanner;

import co.edu.uptc.project2.domain.Customer;
import co.edu.uptc.project2.enums.DocumentType;
import co.edu.uptc.project2.service.CustomerService;
import co.edu.uptc.project2.ui.view.CustomerView;
import co.edu.uptc.project2.util.Validator;

public class CustomerController {

    private CustomerService service;
    private CustomerView view;
    private Scanner scanner;

    public CustomerController(Scanner scanner) {
        this.service = new CustomerService();
        this.view = new CustomerView();
        this.scanner = scanner;
    }

    public CustomerController(Scanner scanner, CustomerService service) {
        this.service = service;
        this.view = new CustomerView();
        this.scanner = scanner;
    }

    public CustomerService getService() {
        return service;
    }

    // ── Menú ─────────────────────────────────────────────────────────────────

    public void showMenu() {
        String input;
        int option = -1;
        do {
            view.showMenu();
            input = scanner.nextLine().trim();

            // Validación de opción de menú con regex
            if (!Validator.isValidMenuOption(input)) {
                view.showMessage(Validator.MSG_MENU_OPTION);
                continue;
            }

            option = Integer.parseInt(input);

            switch (option) {
                case 1 -> createCustomer();
                case 2 -> listAllCustomers();
                case 3 -> findCustomerById();
                case 4 -> updateCustomer();
                case 5 -> deleteCustomer();
                case 0 -> view.showMessage("Volviendo al menú principal...");
                default -> view.showMessage("Opción no válida. Elija entre 0 y 5.");
            }
        } while (option != 0);
    }

    // ── Crear cliente ─────────────────────────────────────────────────────────

    private void createCustomer() {
        String name     = readName("Nombre");
        String lastName = readName("Apellido");
        String email    = readEmail();
        String phone    = readPhone();

        DocumentType docType  = selectDocumentType();
        String docNumber      = readDocumentNumber();

        Customer customer = new Customer(name, lastName, email, phone, docType, docNumber);
        boolean result = service.createCustomer(customer);
        view.showMessage(result
                ? "✓ Cliente creado exitosamente."
                : "✗ Error: ya existe un cliente con ese número de documento.");
    }

    // ── Listar clientes ───────────────────────────────────────────────────────

    private void listAllCustomers() {
        view.showAllCustomers(service.getAllCustomers());
    }

    // ── Buscar cliente ────────────────────────────────────────────────────────

    private void findCustomerById() {
        String docNumber = readDocumentNumber();
        view.showCustomer(service.getCustomerById(docNumber));
    }

    // ── Actualizar cliente ────────────────────────────────────────────────────

    private void updateCustomer() {
        String docNumber = readDocumentNumber();

        Customer existing = service.getCustomerById(docNumber);
        if (existing == null) {
            view.showMessage("✗ Cliente no encontrado.");
            return;
        }

        // Nombre — ENTER para mantener, si escribe algo debe ser válido
        String name = readOptionalName("Nuevo nombre", existing.getName());
        String lastName = readOptionalName("Nuevo apellido", existing.getLastName());
        String email = readOptionalEmail(existing.getEmail());
        String phone = readOptionalPhone(existing.getPhone());

        Customer updated = new Customer(name, lastName, email, phone,
                existing.getDocumentType(), existing.getDocumentNumber());

        boolean result = service.updateCustomer(docNumber, updated);
        view.showMessage(result ? "✓ Cliente actualizado." : "✗ Error al actualizar.");
    }

    // ── Eliminar cliente ──────────────────────────────────────────────────────

    private void deleteCustomer() {
        String docNumber = readDocumentNumber();
        boolean result = service.deleteCustomer(docNumber);
        view.showMessage(result ? "✓ Cliente eliminado." : "✗ Cliente no encontrado.");
    }

    // ── Helpers de lectura con validación ────────────────────────────────────

    /**
     * Lee un nombre o apellido. Repite hasta recibir solo letras y espacios.
     */
    private String readName(String label) {
        String value;
        do {
            System.out.print(label + ": ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidName(value)) {
                view.showMessage(Validator.MSG_NAME);
            }
        } while (!Validator.isValidName(value));
        return value;
    }

    /**
     * Lee un nombre opcional: si el usuario presiona Enter se conserva el actual,
     * pero si escribe algo debe cumplir la validación.
     */
    private String readOptionalName(String label, String current) {
        String value;
        do {
            System.out.print(label + " (actual: " + current + ", ENTER para mantener): ");
            value = scanner.nextLine().trim();
            if (value.isBlank()) {
            	return current;
            }
            if (!Validator.isValidName(value)) {
                view.showMessage(Validator.MSG_NAME);
            }
        } while (!Validator.isValidName(value));
        return value;
    }

    /**
     * Lee un correo electrónico. Repite hasta recibir un formato válido.
     */
    private String readEmail() {
        String value;
        do {
            System.out.print("Email: ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidEmail(value)) {
                view.showMessage(Validator.MSG_EMAIL);
            }
        } while (!Validator.isValidEmail(value));
        return value;
    }

    private String readOptionalEmail(String current) {
        String value;
        do {
            System.out.print("Nuevo email (actual: " + current + ", ENTER para mantener): ");
            value = scanner.nextLine().trim();
            if (value.isBlank()) {
            	return current;
            }
            if (!Validator.isValidEmail(value)) {
                view.showMessage(Validator.MSG_EMAIL);
            }
        } while (!Validator.isValidEmail(value));
        return value;
    }

    /**
     * Lee un número de teléfono. Debe tener 10 dígitos y empezar por 3.
     */
    private String readPhone() {
        String value;
        do {
            System.out.print("Teléfono (10 dígitos, empieza por 3): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidPhone(value)) {
                view.showMessage(Validator.MSG_PHONE);
            }
        } while (!Validator.isValidPhone(value));
        return value;
    }

    private String readOptionalPhone(String current) {
        String value;
        do {
            System.out.print("Nuevo teléfono (actual: " + current + ", ENTER para mantener): ");
            value = scanner.nextLine().trim();
            if (value.isBlank()) {
            	return current;
            }
            if (!Validator.isValidPhone(value)) {
                view.showMessage(Validator.MSG_PHONE);
            }
        } while (!Validator.isValidPhone(value));
        return value;
    }

    /**
     * Lee un número de documento. Solo dígitos, entre 6 y 12 caracteres.
     */
    private String readDocumentNumber() {
        String value;
        do {
            System.out.print("Número de documento (solo números, 6-12 dígitos): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidDocumentNumber(value)) {
                view.showMessage(Validator.MSG_DOCUMENT_NUMBER);
            }
        } while (!Validator.isValidDocumentNumber(value));
        return value;
    }

    /**
     * Selección de tipo de documento validada con regex (solo opciones 1-4).
     */
    private DocumentType selectDocumentType() {
        String opt;
        // Regex: solo acepta exactamente 1, 2, 3 o 4
        String validOptions = "^[1-4]$";
        do {
            System.out.println("Tipo de documento:");
            System.out.println("  1. CC  (Cédula de Ciudadanía)");
            System.out.println("  2. TI  (Tarjeta de Identidad)");
            System.out.println("  3. PA  (Pasaporte nacional)");
            System.out.println("  4. PASSPORT (Pasaporte extranjero)");
            System.out.print("Seleccione [1-4]: ");
            opt = scanner.nextLine().trim();
            if (!opt.matches(validOptions)) {
                view.showMessage("  ✗ Opción inválida. Ingrese un número entre 1 y 4.");
            }
        } while (!opt.matches(validOptions));

        return switch (opt) {
            case "1" -> DocumentType.CC;
            case "2" -> DocumentType.TI;
            case "3" -> DocumentType.PA;
            default  -> DocumentType.PASSPORT;
        };
    }
}
