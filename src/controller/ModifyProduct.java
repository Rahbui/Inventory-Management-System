package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.getAllParts;
import static model.Inventory.getAllProducts;

public class ModifyProduct {

    Stage stage;
    Parent scene;

    void LoadStage(ActionEvent event, String viewFXML) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/" + viewFXML + ".fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    ObservableList<Part> currentParts = FXCollections.observableArrayList();
    Product selectedProduct;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Part, Integer> currentPartsIdCol;

    @FXML
    private TableColumn<Part, Integer> currentPartsInvCol;

    @FXML
    private TableColumn<Part, String> currentPartsNameCol;

    @FXML
    private TableColumn<Part, Double> currentPartsPriceCol;

    @FXML
    private TableView<Part> associatedPartsTableView;

    @FXML
    private TableView<Part> partSearchTableView;

    @FXML
    private TextField productIdTxt;

    @FXML
    private TextField productInvTxt;

    @FXML
    private TextField productMaxTxt;

    @FXML
    private TextField productMinTxt;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextField productPriceTxt;

    @FXML
    private TextField partSearchTxt;

    @FXML
    private TableColumn<Part, Integer> searchPartIdCol;

    @FXML
    private TableColumn<Part, Integer> searchPartInvCol;

    @FXML
    private TableColumn<Part, String> searchPartNameCol;

    @FXML
    private TableColumn<Part, Double> searchPartPriceCol;

    @FXML
    void onActionAddPartToProduct(ActionEvent event) {
        Part part = partSearchTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
    }

    @FXML
    void onActionDeletePartFromProduct(ActionEvent event) {
        Part part = associatedPartsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Confirm delete?");
        alert.setContentText("Are you sure you want to delete " + selectedProduct.getName() + " from associated parts?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            currentParts.remove(part);
        }
    }

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
    void onActionSave(ActionEvent event) throws IOException {
        int id = selectedProduct.getId();
        String name = productNameTxt.getText();
        double price = Double.parseDouble(productPriceTxt.getText());
        int inv = Integer.parseInt(productInvTxt.getText());
        int max = Integer.parseInt(productMaxTxt.getText());
        int min = Integer.parseInt(productMinTxt.getText());
        int index = Inventory.findProductIndex(selectedProduct);

        Product newProduct = new Product(id, name, price, inv, min, max);
        for(Part part : currentParts){
            newProduct.addAssociatedPart(part);
        }
        if(currentParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No Parts Associated to Product");
            alert.setContentText("The product must have at least one associated part.");
            alert.showAndWait();
        }
        else {
            Inventory.updateProduct(index, newProduct);
            LoadStage(event, "MainMenu");
        }
    }

    @FXML
    void onActionSearchProduct(ActionEvent event) {
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        String searchTxt = partSearchTxt.getText();
        boolean isInt = false;
        for(char c : searchTxt.toCharArray()){
            if(Character.isDigit(c)){
                isInt = true;
            }
        }
        if(searchTxt.isEmpty()){
            searchResults.addAll(Inventory.getAllParts());
        }
        else if(isInt){
            searchResults.add(Inventory.lookupPart(Integer.parseInt(searchTxt)));
        }
        else{
            searchResults = Inventory.lookupPart(searchTxt);
        }
        partSearchTableView.setItems(searchResults);
    }

    @FXML
    void initialize() {
        partSearchTableView.setItems(getAllParts());

        searchPartIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        searchPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        searchPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        searchPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;

        productIdTxt.setText(Integer.toString(product.getId()));
        productNameTxt.setText(product.getName());
        productInvTxt.setText(Integer.toString(product.getStock()));
        productPriceTxt.setText(Double.toString(product.getPrice()));
        productMaxTxt.setText(Integer.toString(product.getMax()));
        productMinTxt.setText(Integer.toString(product.getMin()));

        currentParts = selectedProduct.getAllAssociatedParts();
        associatedPartsTableView.setItems(currentParts);
        currentPartsIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        currentPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        currentPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}
