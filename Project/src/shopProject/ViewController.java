package shopProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewController extends BaseController implements Initializable {
    private Integer selectedId = null;

    @FXML
    private TableView<ProductDTO> productViewTable;
    @FXML
    private TableView<CustomerDTO> customerViewTable;
    @FXML
    private TableView<OrderDTO> orderViewTable;

    @FXML
    public void getData(cat job){
        try {
            Connection con = DriverManager.getConnection(dburl, "root", "");
            Statement st = con.createStatement();
            ObservableList dbData;
            ResultSet rs;
            if(job == cat.products){
                productViewTable.visibleProperty().set(true);
                String query = "select * from products";
                rs = st.executeQuery(query);
                dbData = FXCollections.observableArrayList(productDBArrayList(rs));
            }else if(job == cat.orders){
                orderViewTable.visibleProperty().set(true);
                String query = "select orders.id, customer_name, product_name, quantity, total, ordered_at from orders " +
                        "inner join products on orders.product_id = products.id " +
                        "inner join customers on orders.customer_id = customers.id";
                rs = st.executeQuery(query);
                dbData = FXCollections.observableArrayList(orderDBArrayList(rs));
            }
            else if(job == cat.customers){
                customerViewTable.visibleProperty().set(true);
                String query = "select id, customer_name, email, pn, phone from customers";
                rs = st.executeQuery(query);
                dbData = FXCollections.observableArrayList(customerDBArrayList(rs));
            }else{
                throw new Exception("Select Operation Not Found!");
            }

            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++) {
                TableColumn column = new TableColumn<>();
                switch (rs.getMetaData().getColumnName(i+1)) {
                    case "id":
                        column.setText("ID");
                        break;
                    case "name":
                        column.setText("Name");
                        break;
                    case "email":
                        column.setText("E-Mail");
                        break;
                    case "customer_name":
                        column.setText("Customer");
                        break;
                    case "total":
                        column.setText("Total");
                        break;
                    case "product_name":
                        column.setText("Product");
                        break;
                    case "price":
                        column.setText("Price");
                        break;
                    case "pn":
                        column.setText("PN");
                        break;
                    case "phone":
                        column.setText("Phone");
                        break;
                    case "quantity":
                        column.setText("Quantity");
                        break;
                    case "ordered_at":
                        column.setText("Order Date");
                        break;
                    default: column.setText(rs.getMetaData().getColumnName(i+1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                        break;
                }
                column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i+1))); //Setting cell property value to correct variable from Person class.
                if(job == cat.products){
                    productViewTable.getColumns().add(column);
                }else if(job == cat.orders){
                    orderViewTable.getColumns().add(column);
                }
                else if(job == cat.customers){
                    customerViewTable.getColumns().add(column);
                }
            }
            if(job == cat.products){
                productViewTable.setItems(dbData);
            }else if(job == cat.orders){
                orderViewTable.setItems(dbData);
            }
            else if(job == cat.customers){
                customerViewTable.setItems(dbData);
            }


        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
    private ArrayList productDBArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<ProductDTO> data =  new ArrayList<>();
        while (resultSet.next()) {
            ProductDTO product = new ProductDTO();
            product.id.set(resultSet.getInt("id"));
            product.product_name.set(resultSet.getString("product_name"));
            product.price.set(resultSet.getDouble("price"));
            data.add(product);
        }
        return data;
    }
    private ArrayList customerDBArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<CustomerDTO> data =  new ArrayList<>();
        while (resultSet.next()) {
            CustomerDTO customer = new CustomerDTO();
            customer.id.set(resultSet.getInt("id"));
            customer.customer_name.set(resultSet.getString("customer_name"));
            customer.email.set(resultSet.getString("email"));
            customer.pn.set(resultSet.getString("pn"));
            customer.phone.set(resultSet.getString("phone"));
            data.add(customer);
        }
        return data;
    }
    private ArrayList orderDBArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<OrderDTO> data =  new ArrayList<>();
        while (resultSet.next()) {
            OrderDTO order = new OrderDTO();
            order.id.set(resultSet.getInt("id"));
            order.customer_name.set(resultSet.getString("customer_name"));
            order.product_name.set(resultSet.getString("product_name"));
            order.quantity.set(resultSet.getInt("quantity"));
            order.total.set(resultSet.getDouble("total"));
            order.ordered_at.set(resultSet.getTimestamp("ordered_at").toString());
            data.add(order);
        }
        return data;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setSelectedId(selectedId);

        if(category == cat.products){
            productViewTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedId = null;
                    return;
                }
                selectedId = newValue.id.get();
                setSelectedId(selectedId);
            });
        }else if(category == cat.orders){
            orderViewTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedId = null;
                    return;
                }
                selectedId = newValue.id.get();
                setSelectedId(selectedId);
            });
        }
        else if(category == cat.customers){
            customerViewTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedId = null;
                    return;
                }
                selectedId = newValue.id.get();
                setSelectedId(selectedId);
            });
        }
    }
}
