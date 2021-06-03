package shopProject;

import javafx.beans.property.*;
import javafx.scene.input.DataFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDTO {
    IntegerProperty id = new SimpleIntegerProperty();
    StringProperty customer_name = new SimpleStringProperty();
    StringProperty product_name = new SimpleStringProperty();
    IntegerProperty quantity = new SimpleIntegerProperty();
    DoubleProperty total = new SimpleDoubleProperty();
    StringProperty ordered_at = new SimpleStringProperty();

    public IntegerProperty idProperty() {
        return id;
    }
    public StringProperty customer_nameProperty() {
        return customer_name;
    }
    public StringProperty product_nameProperty() {
        return product_name;
    }
    public IntegerProperty quantityProperty() {
        return quantity;
    }
    public DoubleProperty totalProperty() {
        return total;
    }
    public StringProperty ordered_atProperty() {
        return ordered_at;
    }

    public OrderDTO(int idValue, String customer_nameValue, String product_nameValue, int quantityValue, Double totalValue, String ordered_atValue) {
        id.set(idValue);
        customer_name.set(customer_nameValue);
        product_name.set(product_nameValue);
        quantity.set(quantityValue);
        total.set(totalValue);
        ordered_at.set(ordered_atValue);
    }

    OrderDTO(){}
}
