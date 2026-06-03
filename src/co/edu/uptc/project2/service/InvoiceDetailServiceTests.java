package co.edu.uptc.project2.service;

import co.edu.uptc.project2.domain.InvoiceDetail;
import co.edu.uptc.project2.enums.ProductCategory;

/**
 * Pruebas unitarias manuales para InvoiceDetailService.
 */
public class InvoiceDetailServiceTests {

    private InvoiceDetailService service;
    private int passed = 0;
    private int failed = 0;

    public InvoiceDetailServiceTests() {
        service = new InvoiceDetailService();
    }

    private void assertTrue(String testName, boolean condition) {
        if (condition) { System.out.println("  [PASS] " + testName); passed++; }
        else            { System.out.println("  [FAIL] " + testName); failed++; }
    }

    private void assertFalse(String name, boolean condition) { assertTrue(name, !condition); }
    private void assertNotNull(String name, Object obj)       { assertTrue(name, obj != null); }
    private void assertNull(String name, Object obj)          { assertTrue(name, obj == null); }
    private void assertEquals(String name, Object exp, Object act) {
        assertTrue(name, exp == null ? act == null : exp.equals(act));
    }

    public void testCreateDetail_valid() {
        InvoiceDetail d = new InvoiceDetail("Laptop", 2, 1500000, ProductCategory.TECHNOLOGY);
        assertTrue("Crear detalle válido retorna true", service.createDetail(d));
    }

    public void testCreateDetail_null() {
        assertFalse("Crear detalle null retorna false", service.createDetail(null));
    }

    public void testCreateDetail_invalidQuantity() {
        InvoiceDetail d = new InvoiceDetail("Monitor", 0, 800000, ProductCategory.TECHNOLOGY);
        assertFalse("Crear detalle con cantidad 0 retorna false", service.createDetail(d));
    }

    public void testCreateDetail_duplicateProduct() {
        InvoiceDetail d1 = new InvoiceDetail("Teclado", 1, 150000, ProductCategory.TECHNOLOGY);
        InvoiceDetail d2 = new InvoiceDetail("Teclado", 3, 200000, ProductCategory.OFFICE);
        service.createDetail(d1);
        assertFalse("Crear detalle con producto duplicado retorna false", service.createDetail(d2));
    }

    public void testGetAllDetails_notEmpty() {
        service.createDetail(new InvoiceDetail("Mouse", 1, 50000, ProductCategory.TECHNOLOGY));
        assertTrue("getAllDetails no está vacío", !service.getAllDetails().isEmpty());
    }

    public void testGetDetailById_found() {
        service.createDetail(new InvoiceDetail("Cuaderno", 5, 3000, ProductCategory.OFFICE));
        assertNotNull("getDetailById encuentra detalle existente", service.getDetailById("Cuaderno"));
    }

    public void testGetDetailById_notFound() {
        assertNull("getDetailById retorna null si no existe", service.getDetailById("Producto Fantasma"));
    }

    public void testSubtotalCalculated() {
        InvoiceDetail d = new InvoiceDetail("Camiseta", 3, 40000, ProductCategory.CLOTHING);
        service.createDetail(d);
        InvoiceDetail found = service.getDetailById("Camiseta");
        assertEquals("Subtotal calculado correctamente (3 x 40000 = 120000)", 120000.0, found.getSubtotal());
    }

    public void testUpdateDetail_success() {
        service.createDetail(new InvoiceDetail("Zapatos", 2, 120000, ProductCategory.CLOTHING));
        InvoiceDetail updated = new InvoiceDetail("Zapatos", 4, 130000, ProductCategory.CLOTHING);
        assertTrue("Actualizar detalle existente retorna true", service.updateDetail("Zapatos", updated));
        assertEquals("Cantidad actualizada", 4, service.getDetailById("Zapatos").getQuantity());
    }

    public void testDeleteDetail_success() {
        service.createDetail(new InvoiceDetail("Arroz", 10, 3500, ProductCategory.FOOD));
        assertTrue("Eliminar detalle existente retorna true", service.deleteDetail("Arroz"));
        assertNull("Detalle eliminado ya no existe", service.getDetailById("Arroz"));
    }

    public void testDeleteDetail_notFound() {
        assertFalse("Eliminar detalle inexistente retorna false", service.deleteDetail("Producto Inexistente"));
    }

    public void runAll() {
        System.out.println("\n===== InvoiceDetailServiceTest =====");
        testCreateDetail_valid();
        testCreateDetail_null();
        testCreateDetail_invalidQuantity();
        testCreateDetail_duplicateProduct();
        testGetAllDetails_notEmpty();
        testGetDetailById_found();
        testGetDetailById_notFound();
        testSubtotalCalculated();
        testUpdateDetail_success();
        testDeleteDetail_success();
        testDeleteDetail_notFound();
        System.out.println("Resultado: " + passed + " pasaron, " + failed + " fallaron.");
    }

    public static void main(String[] args) {
        new InvoiceDetailServiceTests().runAll();
    }
}