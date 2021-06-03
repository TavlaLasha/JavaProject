package shopProject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProductFrmController extends BaseController implements Initializable {
    private Integer selectedId = null;

    @FXML
    private TextField name;
    @FXML
    private Spinner<Double> price;
    @FXML
    private Label title;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedId = getSelectedId();
    }
    @FXML
    public void save(){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            String query;
            PreparedStatement st;
            if(selectedId != null){
                query = "UPDATE products SET product_name = ?, price = ? WHERE id = ?";
                st = con.prepareStatement(query);
                st.setInt(3, selectedId);
            }
            else {
                query = "INSERT INTO products (product_name, price) VALUES(?, ?)";
                st = con.prepareStatement(query);
            }
            st.setString(1, name.getText());
            st.setDouble(2, price.getValue());
            st.executeUpdate();
            st.close();
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Success!");
            a.show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void update(){
        if(selectedId != null){
            title.setText("Edit Product");
            fillForm();
        }
        else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Please Select Data in View");
            a.show();
        }
    }
    public void fillForm(){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            String query = "select * from products where id="+selectedId;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            name.setText(rs.getString("product_name"));
            price.getValueFactory().setValue(rs.getDouble("price"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
