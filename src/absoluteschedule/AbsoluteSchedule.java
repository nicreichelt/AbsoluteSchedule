/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule;

import absoluteschedule.Helper.ListManage;
import static absoluteschedule.Helper.ListManage.loadAppts;
import static absoluteschedule.Helper.ListManage.loadCustomers;
import absoluteschedule.Model.Calendar;
import absoluteschedule.Model.Customer;
import absoluteschedule.View_Controller.LogInController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author NicR
 */
public class AbsoluteSchedule extends Application {
    
//Instance Variables
    Stage window;
    private static ObservableList<Customer> mainCustomerList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> mainApptList = FXCollections.observableArrayList();
    private static List<String> custNameList = new ArrayList<>();
    private static ListManage l = new ListManage();
    
//Load customers and Appointments
    public static void reloadMainCustList() throws SQLException{
        mainCustomerList.clear();
        mainCustomerList = loadCustomers();
    }
    public static void reloadMainApptList() throws SQLException{
        mainApptList.clear();
        mainApptList = loadAppts();
    }
    public static ObservableList<Customer> getMainCustList(){
        System.out.println("Main Appt Count Retrieve: " + mainCustomerList.size());
        return mainCustomerList;
    }
    public static ObservableList<Calendar> getMainApptList(){
        System.out.println("Main Appt Count Retrieve: " + mainApptList.size());
        return mainApptList;
    }
    
    
//RootLayout
    public void initLogin() throws IOException{
    //Load parts overview
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AbsoluteSchedule.class.getResource("View_Controller/LogIn.fxml"));
        AnchorPane LoginLoader = (AnchorPane) loader.load();
    
    //Setting the scene
        Scene scene = new Scene(LoginLoader);
        
    //Showing the scene on the stage
        window.setScene(scene);
        window.show();
    }
    public void showLogin() throws IOException{
    // Load Login screen.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AbsoluteSchedule.class.getResource("View_Controller/LogIn.fxml"));
        AnchorPane LoginLoader = (AnchorPane) loader.load();
        
    //Give Controller access to the Main Application
        LogInController controller = loader.getController();
        controller.setMainApp(this);
    }
    
    //Starting Method
    @Override
    public void start(Stage primaryStage) throws SQLException, IOException {
    
    //Setting the stage
        window = primaryStage;
        window.setTitle("Absolute Schedule");
        
        initLogin();
        showLogin();
        
    //SQL connection test
        Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U04H1H", "U04H1H","53688238168");
        System.out.println("Connection: " + conn + " was successful.");
        conn.close();
        
    //Populate Customer and Calendar data
        reloadMainCustList();
        reloadMainApptList();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){

        launch(args);
    }
    
//Create Popups
    public static void createStandardAlert(String alertMessage, String alertHeader, String alertTitle){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
    public static Optional<ButtonType> createConfirmAlert(String alertMessage, String alertHeader, String alertTitle){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertMessage);
        Optional<ButtonType> result = alert.showAndWait();
        
        return result;
    }
}