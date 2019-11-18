package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddPart {

    Stage stage;
    Parent scene;
    private boolean isOutsourced;

    void LoadStage(ActionEvent event, String viewFXML) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/" + viewFXML + ".fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private Label partMachineCompanyLabel;

    @FXML
    private TextField partMachineCompanyTxt;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField partInvTxt;

    @FXML
    private TextField partMaxTxt;

    @FXML
    private TextField partMinTxt;

    @FXML
    private TextField partNameTxt;

    @FXML
    private TextField partPriceTxt;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ToggleGroup sourceTG;

    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel");
        alert.setHeaderText("Confirm cancel?");
        alert.setContentText("Are you sure you want to cancel without saving");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            LoadStage(event, "MainMenu");
        }
    }

    @FXML
    void onActionInHouse(ActionEvent event) {
        isOutsourced = false;
        partMachineCompanyLabel.setText("Machine ID");
    }

    @FXML
    void onActionOutsourced(ActionEvent event) throws IOException {
        isOutsourced = true;
        partMachineCompanyLabel.setText("Company Name");
    }

    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        int id = Inventory.getAllParts().size() + 1;
        String name = partNameTxt.getText();
        int stock = Integer.parseInt(partInvTxt.getText());
        double price = Double.parseDouble(partPriceTxt.getText());
        int min = Integer.parseInt(partMinTxt.getText());
        int max = Integer.parseInt(partMaxTxt.getText());
        String Machine_Company = partMachineCompanyTxt.getText();

        if(isOutsourced){
            Inventory.addPart(new Outsourced(id, name, price, stock, min, max, Machine_Company));
        }
        else {
            Inventory.addPart(new InHouse(id, name, price, stock, min, max, Integer.parseInt(Machine_Company)));
        }

        LoadStage(event, "MainMenu");
    }


    @FXML
    void initialize() {
        assert sourceTG != null : "fx:id=\"sourceTG\" was not injected: check your FXML file 'AddPartOutsourced.fxml'.";


    }

}
