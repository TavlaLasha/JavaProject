package shopProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderFrmController extends BaseController implements Initializable {
    private Integer productId;
    private Double productPrice;
    private Double totalPrice;

    @FXML
    private TextField customer;
    @FXML
    private ComboBox<String> products;
    @FXML
    private TextField price;
    @FXML
    private Spinner<Integer> quantity;
    @FXML
    private TextField total;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customer.setText(getLoggedInUserName());
        fillCombo();
        quantity.valueProperty().addListener((obs, oldVal, newVal) -> {
            quantityAction();
        });
    }
    @FXML
    public void order(){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            String query;
            query = "INSERT INTO orders (customer_id, product_id, quantity, total, ordered_at) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, getLoggedInUserId());
            st.setInt(2, productId);
            st.setInt(3, quantity.getValue());
            st.setDouble(4, Double.parseDouble(total.getText()));
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            st.setDate(5, date);
            st.executeUpdate();
            st.close();
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Success!");
            a.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void fillCombo(){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            String query = "select * from products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                products.getItems().add(rs.getString("product_name"));
            }
            products.getSelectionModel().select(0);
            getProductIdNPrice(products.getValue());
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void getProductIdNPrice(String pd){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            String query = "select id, price from products where product_name='"+pd+"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            productId =  rs.getInt("id");
            productPrice = rs.getDouble("price");
            price.setText(String.valueOf(productPrice));
            quantityAction();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML
    private void comboAction(ActionEvent event) {
        getProductIdNPrice(products.getValue());
    }
    private void quantityAction() {
        totalPrice = productPrice * quantity.getValue();
        total.setText(String.valueOf(totalPrice));
    }
}
