package co.edu.uptc.project2.service;

import co.edu.uptc.project2.domain.Customer;
import co.edu.uptc.project2.enums.DocumentType;

/**
 * Pruebas unitarias manuales para CustomerService.
 * (Sin framework de testing; se ejecutan desde main para verificar comportamiento.)
 */
public class CustomerServiceTests {

    private CustomerService service;
    private int passed = 0;
    private int failed = 0;

    public CustomerServiceTests() {
        service = new CustomerService();
    }

    // ── Utilidad de aserción ─────────────────────────────────────────────────

    private void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName);
            failed++;
        }
    }

    private void assertFalse(String testName, boolean condition) {
        assertTrue(testName, !condition);
    }

    private void assertNotNull(String testName, Object obj) {
        assertTrue(testName, obj != null);
    }

    private void assertNull(String testName, Object obj) {
        assertTrue(testName, obj == null);
    }

    private void assertEquals(String testName, Object expected, Object actual) {
        boolean ok = expected == null ? actual == null : expected.equals(actual);
        assertTrue(testName, ok);
    }

    // ── Tests ────────────────────────────────────────────────────────────────

    public void testCreateCustomer_valid() {
        Customer c = new Customer("Ana", "López", "ana@mail.com", "3001234567", DocumentType.CC, "100200300");
        assertTrue("Crear cliente válido retorna true", service.createCustomer(c));
    }

    public void testCreateCustomer_null() {
        assertFalse("Crear cliente null retorna false", service.createCustomer(null));
    }

    public void testCreateCustomer_duplicateDocument() {
        Customer c1 = new Customer("Luis", "Pérez", "luis@mail.com", "3009876543", DocumentType.CC, "999888777");
        Customer c2 = new Customer("Otro", "Nombre", "otro@mail.com", "3001111111", DocumentType.TI, "999888777");
        service.createCustomer(c1);
        assertFalse("Crear cliente con documento duplicado retorna false", service.createCustomer(c2));
    }

    public void testGetAllCustomers_notEmpty() {
        service.createCustomer(new Customer("Maria", "Torres", "m@mail.com", "3002", DocumentType.CC, "111222333"));
        assertTrue("getAllCustomers no está vacío tras agregar un cliente", !service.getAllCustomers().isEmpty());
    }

    public void testGetCustomerById_found() {
        service.createCustomer(new Customer("Pedro", "Gómez", "p@mail.com", "3003", DocumentType.TI, "444555666"));
        assertNotNull("getCustomerById encuentra cliente existente", service.getCustomerById("444555666"));
    }

    public void testGetCustomerById_notFound() {
        assertNull("getCustomerById retorna null si no existe", service.getCustomerById("000000000"));
    }

    public void testUpdateCustomer_success() {
        service.createCustomer(new Customer("Carlos", "Ruiz", "c@mail.com", "3004", DocumentType.CC, "777888999"));
        Customer updated = new Customer("Carlos Alberto", "Ruiz", "ca@mail.com", "3004", DocumentType.CC, "777888999");
        assertTrue("Actualizar cliente existente retorna true", service.updateCustomer("777888999", updated));
        assertEquals("Nombre actualizado correctamente", "Carlos Alberto",
                service.getCustomerById("777888999").getName());
    }

    public void testUpdateCustomer_notFound() {
        Customer updated = new Customer("Fake", "User", "f@mail.com", "0000", DocumentType.CC, "000111222");
        assertFalse("Actualizar cliente inexistente retorna false", service.updateCustomer("000111222", updated));
    }

    public void testDeleteCustomer_success() {
        service.createCustomer(new Customer("Laura", "Díaz", "l@mail.com", "3005", DocumentType.PA, "321321321"));
        assertTrue("Eliminar cliente existente retorna true", service.deleteCustomer("321321321"));
        assertNull("Cliente eliminado ya no se encuentra", service.getCustomerById("321321321"));
    }

    public void testDeleteCustomer_notFound() {
        assertFalse("Eliminar cliente inexistente retorna false", service.deleteCustomer("nonexistent"));
    }

    // ── Punto de entrada ─────────────────────────────────────────────────────

    public void runAll() {
        System.out.println("\n===== CustomerServiceTest =====");
        testCreateCustomer_valid();
        testCreateCustomer_null();
        testCreateCustomer_duplicateDocument();
        testGetAllCustomers_notEmpty();
        testGetCustomerById_found();
        testGetCustomerById_notFound();
        testUpdateCustomer_success();
        testUpdateCustomer_notFound();
        testDeleteCustomer_success();
        testDeleteCustomer_notFound();
        System.out.println("Resultado: " + passed + " pasaron, " + failed + " fallaron.");
    }

    public static void main(String[] args) {
        new CustomerServiceTests().runAll();
    }
}