package shopProject;

import javafx.beans.property.*;

public class ProductDTO {
    IntegerProperty id = new SimpleIntegerProperty();
    StringProperty product_name = new SimpleStringProperty();
    DoubleProperty price = new SimpleDoubleProperty();

    public IntegerProperty idProperty() {
        return id;
    }
    public StringProperty product_nameProperty() {
        return product_name;
    }
    public DoubleProperty priceProperty() {
        return price;
    }

    public ProductDTO(int idValue, String product_nameValue, double priceValue) {
        id.set(idValue);
        product_name.set(product_nameValue);
        price.set(priceValue);
    }

    public ProductDTO(){}
}
