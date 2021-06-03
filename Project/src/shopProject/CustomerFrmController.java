package shopProject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerFrmController extends BaseController implements Initializable {
    private Integer selectedId = null;

    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField pn;
    @FXML
    private TextField phone;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label passwordLabel;
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
                if(password.getText().equals("")){
                    query = "UPDATE customers SET customer_name = ?, email = ?, pn = ?, phone = ? WHERE id = ?";
                    st = con.prepareStatement(query);
                    st.setString(1, name.getText());
                    st.setString(2, email.getText());
                    st.setString(3, pn.getText());
                    st.setString(4, phone.getText());
                    st.setInt(5, selectedId);
                }
                else {
                    query = "UPDATE customers SET customer_name = ?, email = ?, password = case when password is not null then ? end, pn = ?, phone = ? WHERE id = ?";
                    st = con.prepareStatement(query);
                    st.setString(1, name.getText());
                    st.setString(2, email.getText());
                    st.setString(3, passwordHasher(password.getText()));
                    st.setString(4, pn.getText());
                    st.setString(5, phone.getText());
                    st.setInt(6, selectedId);
                }
            }
            else {
                if(!customerValidator()) {
                    return;
                }
                query = "INSERT INTO customers (customer_name, email,  password, pn, phone) VALUES(?, ?, ?, ?, ?)";
                st = con.prepareStatement(query);
                st.setString(1, name.getText());
                st.setString(2, email.getText());
                st.setString(3, passwordHasher(password.getText()));
                st.setString(4, pn.getText());
                st.setString(5, phone.getText());
            }
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
            title.setText("Edit Customer");
            passwordLabel.setText("New Password:");
            passwordLabel.setStyle("-fx-translate-x:-27px;");
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
            String query = "select * from customers where id="+selectedId;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            name.setText(rs.getString("customer_name"));
            email.setText(rs.getString("email"));
            pn.setText(rs.getString("pn"));
            phone.setText(rs.getString("phone"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public boolean findCustomer(String pn){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            String query = "select * from customers where pn="+pn;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public Boolean customerValidator(){
        ArrayList<String> error = new ArrayList<>();
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email.getText());

        if(!matcher.matches()){
            error.add("Invalid E-Mail");
        }
        if(findCustomer(pn.getText())) {
            error.add("User with provided PN already exists!");
        }
        if(!password.getText().equals(confirmPassword.getText())){
            error.add("Password confirmation does not match!");
        }
        if(password.getText().length() < 5){
            error.add("Password too small!");
        }


        if(error.iterator().hasNext()){
            Alert a = new Alert(Alert.AlertType.WARNING);
            StringBuilder info = new StringBuilder();
            for (String err : error) {
                info.append(err).append("\n");
            }
            a.setHeaderText(info.toString());
            a.show();
            return false;
        }
        else{
            return true;
        }
    }
}
