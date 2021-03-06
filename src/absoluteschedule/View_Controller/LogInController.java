/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import absoluteschedule.AbsoluteSchedule;
import static absoluteschedule.AbsoluteSchedule.createConfirmAlert;
import static absoluteschedule.AbsoluteSchedule.createStandardAlert;
import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import static absoluteschedule.Helper.SQLManage.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class LogInController implements Initializable {

//FXML Declarations
    @FXML private PasswordField LoginPasswordField;
    @FXML private TextField LoginUserNameField;
    @FXML private Button LoginSubmitButton;
    @FXML private Button LoginExitButton;
    @FXML private Label LoginMessageLabel;
    
//Instance Variables
    private AbsoluteSchedule mainApp;
    private Connection conn;
    private ResultSet rs = null;
    private ResourceBundle localization = loadResourceBundle();
    private static String user;
    private String exceptionMessage = new String();
    
//FXML Button Handlers

//Login Exit Button handler
    @FXML void LoginExitClicked(ActionEvent event) {
        Optional<ButtonType> confirm = createConfirmAlert(localization.getString("exit_confirm"), "Exit Confirmation!", "Confirm!");
        
        if(confirm.get() == ButtonType.OK){
            System.out.println("Exit clicked!");
            System.exit(0);
        }
        else{
            System.out.println("You clicked cancel. Please continue.");
        }
    }
//Login Login Button Clicked
    @FXML void LoginSubmitClicked(ActionEvent event) throws IOException, SQLException {
        System.out.println("Login clicked");
        exceptionMessage = "";
        
    //Capture textfield info
        String userName = LoginUserNameField.getText().trim();
        String password = LoginPasswordField.getText().trim();
        user = userName;
        
        try{
            exceptionMessage = isLoginBlank(exceptionMessage, userName, password);
        
            if(exceptionMessage.length()>0){
                createStandardAlert(exceptionMessage, "Login Error!", "Error!");
            }
            else{
            //Login Success/Failure Boolean & Message
                String logMessage = "";

            //Try statement to run query and complete login
                try(Connection conn = getConn();
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE userName=? AND password=?")){

                //Validating textfield data was gathered and SQL Query values to select instnatiated
                    System.out.println("Username: " + userName);
                    System.out.println("Password: " + password);
                    String dbUserName = "";
                    String dbPassword = "";

                //Query to check macthing

                    ps.setString(1, userName);
                    ps.setString(2, password);
                    rs = ps.executeQuery();
                    System.out.println("SQL Query successful");

                    while(rs.next()){
                        System.out.println("Setting variables from SQL Query");
                        dbUserName = rs.getString("userName");
                        dbPassword = rs.getString("password");
                        System.out.println("Username: " + dbUserName + ", Password: " + dbPassword);
                    }

                    if(userName.equals(dbUserName) && password.equals(dbPassword)){
                        createStandardAlert(localization.getString("login_successful"), "Login Successful!", "Success!");

                    //Log File Message updated
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ssZ");
                        logMessage = ZonedDateTime.now().format(formatter) + " - " + "Successful login attempt by User: " + userName + ".";
                        System.out.println(logMessage);

                    //Load MainView scene
                        Parent mainView = FXMLLoader.load(getClass().getResource("MainView.fxml"));
                        Scene scene = new Scene(mainView);

                    //Loads stage information from main file
                        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                    //Load scene onto stage
                        window.setScene(scene);
                        window.show();

                    //Center Stage on middle of screen
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
                        window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2); 
                    }
                    else{
                        //Error popup
                        String errorMessage = localization.getString("login_error");
                        errorMessage = loadSpanishError(errorMessage);
                        createStandardAlert(errorMessage, "Login Error!", "Error!");

                    //Log File Message updated
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ssZ");
                        logMessage = ZonedDateTime.now().format(formatter) + " - " + "Failed login attempt by User: " + userName + ".";
                        System.out.println(logMessage);
                    }
                }
                catch(SQLException err){

                }

            //Create/Write log file
                String file = "Login_Log.txt"; //File is located in the root directory of the application

                if(Files.exists(Paths.get(file))){
                //Append login attempt to existing log file
                    System.out.println("File exists and login attempt appended to log");

                //Here true is to append the content to file
                    FileWriter fileWriter = new FileWriter(file,true);

                //Use try-with-resource to get auto-closeable writer instance
                    try (BufferedWriter writer = new BufferedWriter(fileWriter)) 
                    {
                        writer.write(logMessage);
                        writer.newLine();
                    }
                }
                else{
                //Create file and add login attempt to new log file
                    System.out.println("File created and login attempt logged");

                //Use try-with-resource to get auto-closeable writer instance
                    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) 
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, YYYY");
                        String date = ZonedDateTime.now().format(formatter);
                        writer.write("Login Log data for the month of: " + date);
                        writer.newLine();
                        writer.newLine();
                        writer.write(logMessage);
                        writer.newLine();
                    }
                }
            }
        }
        catch(NumberFormatException e){
            
        }
    }
    
//Method to pass username to various screens for creation and update fields for SQL DB
    public static String loggedOnUser(){
        return user;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Load Resource Bundle

    }
    
//Set mainApp to the main application.
    public void setMainApp(AbsoluteSchedule mainApp) {
        this.mainApp = mainApp;
    }
    
//Check Login Fields are not blank
    public String isLoginBlank(String message, String testUserName, String testPassword){
    //Test User Name
        if(testUserName.equals("")){
            message = message + localization.getString("login_uName_blank");
        }
    //Test Password
        if(testPassword.equals("")){
            message = message + localization.getString("login_password_blank");
        } 
        return message;
    }
    
//Load Spanish Error
    public String loadSpanishError(String message){
        Locale spanish = new Locale("es", "US");
        ResourceBundle spanishMessage = ResourceBundle.getBundle("resources", spanish);
        message = message + spanishMessage.getString("login_error");
        return message;
    }
}
