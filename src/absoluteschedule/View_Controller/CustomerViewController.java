/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class CustomerViewController implements Initializable {

//FXML Declarations
    //Customer etnry fields
    @FXML private TextField CustomerIDField;
    @FXML private TextField CustomerNameField;
    @FXML private TextField CustomerAddress1Field;
    @FXML private TextField CustomerAddress2Field;
    @FXML private TextField CustomerCityField;
    @FXML private TextField CustomerPostalCodeField;
    @FXML private CheckBox CustomerActiveCheckbox;
    //Customer Table View
    @FXML private TableView<?> CustomerTableView;
    //Customer entry buttons
    @FXML private Button CustomerCancelButton;
    @FXML private Button CustomerClearButton;
    @FXML private Button CustomerSaveButton;
    //Header search field/buttons
    @FXML private TextField CustomerSearchField;
    @FXML private Button CustomerSearchButton;
    @FXML private Button CustomerSearchClearButton;
    
//Customer button handlers
    
//Cancel Button handler
    @FXML void CustomerCancelClick(ActionEvent event) throws IOException {
        System.out.println("Cancel clicked. Returning to main screen.");
        
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
//Clear Button handler
    @FXML void CustomerClearClick(ActionEvent event) {

    }
//Save Button handler
    @FXML void CustomerSaveClick(ActionEvent event) {

    }
//Search Button handler
    @FXML void CustomerSearchClick(ActionEvent event) {

    }
//Clear Search Button handler
    @FXML void CustomerSearchClearClick(ActionEvent event) {

    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
