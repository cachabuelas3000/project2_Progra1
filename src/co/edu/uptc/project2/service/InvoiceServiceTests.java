package co.edu.uptc.project2.service;

import java.util.HashSet;

import co.edu.uptc.project2.domain.Customer;
import co.edu.uptc.project2.domain.Invoice;
import co.edu.uptc.project2.domain.InvoiceDetail;
import co.edu.uptc.project2.enums.DocumentType;
import co.edu.uptc.project2.enums.InvoiceStatus;
import co.edu.uptc.project2.enums.PaymentMethod;
import co.edu.uptc.project2.enums.ProductCategory;

/**
 * Pruebas unitarias manuales para InvoiceService.
 */
public class InvoiceServiceTests {

    private InvoiceService service;
    private int passed = 0;
    private int failed = 0;

    public InvoiceServiceTests() {
        service = new InvoiceService();
    }

    private void assertTrue(String name, boolean condition) {
        if (condition) { System.out.println("  [PASS] " + name); passed++; }
        else            { System.out.println("  [FAIL] " + name); failed++; }
    }

    private void assertFalse(String name, boolean condition)  { assertTrue(name, !condition); }
    private void assertNotNull(String name, Object obj)       { assertTrue(name, obj != null); }
    private void assertNull(String name, Object obj)          { assertTrue(name, obj == null); }
    private void assertEquals(String name, Object exp, Object act) {
        assertTrue(name, exp == null ? act == null : exp.equals(act));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Customer makeCustomer() {
        return new Customer("Test", "User", "test@mail.com", "3000000001", DocumentType.CC, "123456789");
    }

    private Invoice makeInvoice(int id, String code) {
        HashSet<InvoiceDetail> details = new HashSet<>();
        details.add(new InvoiceDetail("Producto A", 2, 50000, ProductCategory.OTHER));
        return new Invoice(id, code, "2026-05-27", 100000, false,
                PaymentMethod.CASH, InvoiceStatus.PENDING, makeCustomer(), details);
    }

    // ── Tests ────────────────────────────────────────────────────────────────

    public void testCreateInvoice_valid() {
        Invoice inv = makeInvoice(1, "FAC-001");
        assertTrue("Crear factura válida retorna true", service.createInvoice(inv));
    }

    public void testCreateInvoice_null() {
        assertFalse("Crear factura null retorna false", service.createInvoice(null));
    }

    public void testCreateInvoice_noCustomer() {
        Invoice inv = new Invoice(2, "FAC-002", "2026-05-27", 50000, false,
                PaymentMethod.TRANSFER, InvoiceStatus.PENDING, null, new HashSet<>());
        assertFalse("Crear factura sin cliente retorna false", service.createInvoice(inv));
    }

    public void testCreateInvoice_duplicateId() {
        Invoice inv1 = makeInvoice(10, "FAC-010");
        Invoice inv2 = makeInvoice(10, "FAC-010B");
        service.createInvoice(inv1);
        assertFalse("Crear factura con ID duplicado retorna false", service.createInvoice(inv2));
    }

    public void testGetAllInvoices_notEmpty() {
        service.createInvoice(makeInvoice(20, "FAC-020"));
        assertTrue("getAllInvoices no está vacío", !service.getAllInvoices().isEmpty());
    }

    public void testGetInvoiceById_found() {
        service.createInvoice(makeInvoice(30, "FAC-030"));
        assertNotNull("getInvoiceById encuentra factura existente", service.getInvoiceById(30));
    }

    public void testGetInvoiceById_notFound() {
        assertNull("getInvoiceById retorna null si no existe", service.getInvoiceById(9999));
    }

    public void testGetInvoiceById_codeCorrect() {
        service.createInvoice(makeInvoice(40, "FAC-040"));
        assertEquals("Código de factura correcto", "FAC-040", service.getInvoiceById(40).getCode());
    }

    public void testUpdateInvoice_success() {
        service.createInvoice(makeInvoice(50, "FAC-050"));
        Invoice updated = makeInvoice(50, "FAC-050");
        updated.setPaid(true);
        updated.setInvoiceStatus(InvoiceStatus.PAID);
        assertTrue("Actualizar factura existente retorna true", service.updateInvoice(50, updated));
        assertTrue("Factura marcada como pagada", service.getInvoiceById(50).isPaid());
    }

    public void testUpdateInvoice_notFound() {
        Invoice updated = makeInvoice(99, "FAC-099");
        assertFalse("Actualizar factura inexistente retorna false", service.updateInvoice(99, updated));
    }

    public void testDeleteInvoice_success() {
        service.createInvoice(makeInvoice(60, "FAC-060"));
        assertTrue("Eliminar factura existente retorna true", service.deleteInvoice(60));
        assertNull("Factura eliminada ya no existe", service.getInvoiceById(60));
    }

    public void testDeleteInvoice_notFound() {
        assertFalse("Eliminar factura inexistente retorna false", service.deleteInvoice(8888));
    }

    // ── Punto de entrada ─────────────────────────────────────────────────────

    public void runAll() {
        System.out.println("\n===== InvoiceServiceTest =====");
        testCreateInvoice_valid();
        testCreateInvoice_null();
        testCreateInvoice_noCustomer();
        testCreateInvoice_duplicateId();
        testGetAllInvoices_notEmpty();
        testGetInvoiceById_found();
        testGetInvoiceById_notFound();
        testGetInvoiceById_codeCorrect();
        testUpdateInvoice_success();
        testUpdateInvoice_notFound();
        testDeleteInvoice_success();
        testDeleteInvoice_notFound();
        System.out.println("Resultado: " + passed + " pasaron, " + failed + " fallaron.");
    }

    public static void main(String[] args) {
        new InvoiceServiceTests().runAll();
        new CustomerServiceTests().runAll();
        new InvoiceDetailServiceTests().runAll();
    }
}