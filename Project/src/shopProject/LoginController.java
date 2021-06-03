package shopProject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class LoginController extends BaseController implements Initializable {
    private BaseController baseController;

    public void setMainController(BaseController baseController) {
        this.baseController = baseController;
    }
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;

    @FXML
    public void login(){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            String query = "select * from customers where email='"+email.getText()+"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(!rs.next()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Provided E-Mail not found in our DataBase");
                a.show();
                return;
            }
            if(rs.getString("password").equals(passwordHasher(password.getText()))){
                setLoggedInUserId(rs.getInt("id"));
                setLoggedInUserName(rs.getString("customer_name"));
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Success!");
                a.setContentText("Hello "+getLoggedInUserName());
                a.show();
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("base.fxml"));
//                Parent root = loader.load();
//                loader.<LoginController>getController().setMainController(this);
//                loggedIn();
            }
            else{
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Incorrect Password!");
                a.show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
