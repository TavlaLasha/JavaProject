package shopProject;

import javafx.beans.property.*;

public class CustomerDTO {
    IntegerProperty id = new SimpleIntegerProperty();
    StringProperty customer_name = new SimpleStringProperty();
    StringProperty email = new SimpleStringProperty();
    StringProperty password = new SimpleStringProperty();
    StringProperty pn = new SimpleStringProperty();
    StringProperty phone = new SimpleStringProperty();

    public IntegerProperty idProperty() {
        return id;
    }
    public StringProperty customer_nameProperty() {
        return customer_name;
    }
    public StringProperty emailProperty() {
        return email;
    }
    public StringProperty passwordProperty() {
        return password;
    }
    public StringProperty pnProperty() {
        return pn;
    }
    public StringProperty phoneProperty() {
        return phone;
    }

    public CustomerDTO(int idValue, String customer_nameValue, String emailValue, String pnValue, String phoneValue) {
        id.set(idValue);
        customer_name.set(customer_nameValue);
        email.set(emailValue);
        pn.set(pnValue);
        phone.set(phoneValue);
    }

    CustomerDTO(){}
}
