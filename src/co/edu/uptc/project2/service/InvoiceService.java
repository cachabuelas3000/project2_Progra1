package co.edu.uptc.project2.service;

import java.util.List;

import co.edu.uptc.project2.domain.Invoice;
import co.edu.uptc.project2.repository.InvoiceRepository;

public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    public InvoiceService() {
        invoiceRepository = new InvoiceRepository();
    }

    // Crear factura
    public boolean createInvoice(Invoice invoice) {
        if (invoice == null) {
        	return false;
        }
        if (invoice.getCode() == null || invoice.getCode().isBlank()) {
        	return false;
        }
        if (invoice.getCustomer() == null) {
        	return false;
        }
        return invoiceRepository.add(invoice);
    }

    // Consultar todas
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // Consultar por ID
    public Invoice getInvoiceById(int id) {
        return invoiceRepository.findById(id);
    }

    // Actualizar factura
    public boolean updateInvoice(int id, Invoice updated) {
        if (updated == null) {
        	return false;
        }
        return invoiceRepository.update(id, updated);
    }

    // Eliminar factura
    public boolean deleteInvoice(int id) {
        return invoiceRepository.delete(id);
    }
}