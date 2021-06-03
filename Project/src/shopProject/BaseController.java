package shopProject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class BaseController implements Initializable {
    protected static String dburl = "jdbc:mysql://localhost:3306/shop";
    private static Integer selectedId;
    protected enum cat {products, orders, customers};
    protected static cat category;
    private static String loggedInUserName;
    private static Integer loggedInUserId;

    @FXML
    private BorderPane mainPane;
    @FXML
    protected Button editBTN;
    @FXML
    protected Button deleteBTN;
    @FXML
    protected Button logIn;
    @FXML
    protected Button reg;
    @FXML
    protected Label greetLabel;
    @FXML
    protected Button logout;

    public static String getLoggedInUserName() {
        return loggedInUserName;
    }
    public void setLoggedInUserName(String loggedInUserName) {
        BaseController.loggedInUserName = loggedInUserName;
    }
    public static Integer getLoggedInUserId() {
        return loggedInUserId;
    }
    public static void setLoggedInUserId(Integer loggedInUserId) {
        BaseController.loggedInUserId = loggedInUserId;
    }
    public void setSelectedId(Integer selectedId) {
        BaseController.selectedId = selectedId;
    }
    public Integer getSelectedId() {
        return selectedId;
    }


    @FXML
    public void add(){
        try {
            String view = "";
            if (category == cat.products) {
                if(loginCheck()) {
                    view = "productFrm.fxml";
                }
                else {
                    return;
                }
            } else if (category == cat.orders) {
                view = "orderFrm.fxml";
            } else if (category == cat.customers) {
                view = "customerFrm.fxml";
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Please Select Category");
                a.show();
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
            Pane root = loader.load();
            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void edit(){
        try {
            if(loginCheck()) {
                FXMLLoader loader;
                if (category == cat.products) {
                    loader = new FXMLLoader(getClass().getResource("productFrm.fxml"));
                } else if (category == cat.orders) {
                    loader = new FXMLLoader(getClass().getResource("orderFrm.fxml"));
                } else if (category == cat.customers) {
                    loader = new FXMLLoader(getClass().getResource("customerFrm.fxml"));
                } else {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setHeaderText("Please Select Category");
                    a.show();
                    return;
                }

                Pane root = loader.load();
                mainPane.setCenter(root);
                if (category == cat.products) {
                    ProductFrmController controller = loader.getController();
                    controller.update();
                } else if (category == cat.orders) {
                    OrderFrmController controller = loader.getController();
//                controller.update();
                } else if (category == cat.customers) {
                    CustomerFrmController controller = loader.getController();
                    controller.update();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void register(){
        category = cat.customers;
        add();
    }
    @FXML
    public void login(){
        try {
            category = cat.customers;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Pane root = loader.load();
            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void logout(){
        if(getLoggedInUserName() == null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("You are not logged in.");
            a.show();
        }
        else {
            setLoggedInUserId(null);
            setLoggedInUserName(null);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Successfully logged out!");
            a.show();
        }
    }
    public void loggedIn(){ //Boolean isLoggedIn, String name
//        reg.setVisible(false);
//        logIn.setText("Log Out");
//        greetLabel.setText("Hello "+loggedInUserName);
    }
    @FXML
    public void viewProducts(){
        category = cat.products;
        viewOperation();
    }
    @FXML
    public void viewOrders(){
        if(loginCheck()) {
            category = cat.orders;
            viewOperation();
        }
    }
    @FXML
    public void viewCustomers(){
        if(loginCheck()) {
            category = cat.customers;
            viewOperation();
        }
    }
    public void viewOperation(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            Pane root = loader.load();
            mainPane.setCenter(root);
            ViewController controller = loader.getController();
            controller.getData(category);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void delete(){
        if(loginCheck()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DeleteConfirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Delete can not be undone.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    if (selectedId != null) {
                        Connection con = DriverManager.getConnection(dburl, "root", "");
                        String query;
                        if (category == cat.products) {
                            query = "delete from products where id=?";
                        } else if (category == cat.orders) {
                            query = "delete from orders where id=?";
                        } else if (category == cat.customers) {
                            query = "delete from customers where id=?";
                        } else {
                            Alert a = new Alert(Alert.AlertType.WARNING);
                            a.setHeaderText("Please Select Data in View");
                            a.show();
                            return;
                        }
                        PreparedStatement st = con.prepareStatement(query);
                        st.setInt(1, selectedId);
                        st.executeUpdate();
                        st.close();
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setHeaderText("Success!");
                        a.show();
                    } else {
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setHeaderText("Please Select Data in View");
                        a.show();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public String passwordHasher(String passwordToHash){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    public Boolean loginCheck(){
        if(getLoggedInUserName() == null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Please Log In");
            a.show();
            return false;
        }
        return true;
    }
}
