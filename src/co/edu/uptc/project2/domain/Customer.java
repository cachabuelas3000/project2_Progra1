package co.edu.uptc.project2.domain;

import co.edu.uptc.project2.enums.DocumentType;

public class Customer {

    // Entidad asociada: Cliente
    private String name;
    private String lastName;
    private String email;
    private String phone;

    private DocumentType documentType;
    private String documentNumber;	
    
    // Constructor vacío
    public Customer() {

    }

    // Constructor lleno

    public Customer(String name, String lastName, String email, String phone, DocumentType documentType,
			String documentNumber) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.documentType = documentType;
		this.documentNumber = documentNumber;
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

 // equals() y hashCode()
    // Para evitar duplicados en Set


	@Override
	public int hashCode() {
	    return documentNumber.hashCode();
	}
	

	@Override
	public boolean equals(Object obj) {

	    if (this == obj)
	        return true;

	    if (obj == null || getClass() != obj.getClass())
	        return false;

	    Customer other = (Customer) obj;

	    return documentNumber.equals(other.documentNumber);
	}

}