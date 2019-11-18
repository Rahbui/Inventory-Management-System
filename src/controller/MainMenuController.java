package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
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
//import model.Inventory;
//import model.Part;
//import model.Product;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuController {

    Stage stage;
    Parent scene;

    void LoadStage(ActionEvent event, String viewFXML) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/" + viewFXML + ".fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Part, Double> partCostCol;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, Integer> productInventoryCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TextField partSearchTxt;

    @FXML
    private TextField productSearchTxt;

    @FXML
    private Button modifyPartBtn;

    @FXML
    private Button modifyProductBtn;

    public boolean search;

    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        LoadStage(event, "AddPart");
    }

    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        LoadStage(event, "AddProduct");
    }

    @FXML
    void onActionDeletePart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Confirm delete?");
        alert.setContentText("Are you sure you want to delete " + selectedPart.getName() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }

    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Confirm delete?");
        alert.setContentText("Are you sure you want to delete " + selectedProduct.getName() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Inventory.deleteProduct(selectedProduct);
        }
    }

    @FXML
    void onActionExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit");
        alert.setHeaderText("Confirm exit?");
        alert.setContentText("Are you sure you want to exit");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    private void onActionModifyPart(ActionEvent event) throws IOException {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            System.out.println("No Selection");
        }
        else{
            Stage stage;
            Parent root;
            stage=(Stage) modifyPartBtn.getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource( "/view/ModifyPart.fxml"));
            root =loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ModifyPart controller = loader.getController();
            controller.setSelectedPart(selectedPart);
        }
    }

    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("You must select a product to modify.");
            alert.showAndWait();
        }
        else{
            Stage stage;
            Parent root;
            stage=(Stage) modifyProductBtn.getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource( "/view/ModifyProduct.fxml"));
            root =loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ModifyProduct controller = loader.getController();
            controller.setSelectedProduct(selectedProduct);
        }
    }

    @FXML
    void onActionSearchPart(ActionEvent event) {
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
        partsTableView.setItems(searchResults);
    }

    @FXML
    void onActionSearchProduct(ActionEvent event) {
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        String searchTxt = productSearchTxt.getText();
        boolean isInt = false;
        for(char c : searchTxt.toCharArray()){
            if(Character.isDigit(c)){
                isInt = true;
            }
        }
        if(searchTxt.isEmpty()){
            searchResults.addAll(Inventory.getAllProducts());
        }
        else if(isInt){
            searchResults.add(Inventory.lookupProduct(Integer.parseInt(searchTxt)));
        }
        else{
            searchResults = Inventory.lookupProduct(searchTxt);
        }

        productsTableView.setItems(searchResults);
    }


    @FXML
    void initialize() {
        partsTableView.setItems(Inventory.getAllParts());
        productsTableView.setItems(Inventory.getAllProducts());

        partIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));



        productIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        
    }



}

