package co.edu.uptc.project2.repository;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.project2.domain.Customer;

public class CustomerRepository {

    private List<Customer> customers;

    public CustomerRepository() {
        customers = new ArrayList<>();
    }

    // Crear
    public boolean add(Customer customer) {
        if (customer == null) return false;
        // Evitar duplicados por número de documento
        for (Customer c : customers) {
            if (c.equals(customer)) return false;
        }
        customers.add(customer);
        return true;
    }

    // Consultar todos
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    // Consultar por número de documento (ID)
    public Customer findById(String documentNumber) {
        for (Customer c : customers) {
            if (c.getDocumentNumber().equalsIgnoreCase(documentNumber)) {
                return c;
            }
        }
        return null;
    }

    // Actualizar
    public boolean update(String documentNumber, Customer updated) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getDocumentNumber().equalsIgnoreCase(documentNumber)) {
                customers.set(i, updated);
                return true;
            }
        }
        return false;
    }

    // Eliminar
    public boolean delete(String documentNumber) {
        return customers.removeIf(c -> c.getDocumentNumber().equalsIgnoreCase(documentNumber));
    }
}