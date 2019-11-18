package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part part){
        allParts.add(part);
    }

    public static void addProduct(Product product){
        allProducts.add(product);
    }

    public static int findPartIndex(Part part){
        for(int i = 0; i < allParts.size(); i++){
            if(part.getId() == allParts.get(i).getId()){
                return i;
            }
        }
        return -1;
    }

    public static int findProductIndex(Product product){
        for(int i = 0; i < allProducts.size(); i++){
            if(product.getId() == allProducts.get(i).getId()){
                return i;
            }
        }
        return -1;
    }

    public static Part lookupPart(int partId){
        int temp = 0;
        for(int i = 0; i < allParts.size(); i++){
            if (partId == allParts.get(i).getId()) {
                return allParts.get(i);
            }
        }
        return null;
    }

    public static Product lookupProduct(int productId){
        for(Product product: allProducts){
            if (productId == product.getId()){
                return product;
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        for(int i = 0; i < allParts.size(); i++){
            if(partName.equalsIgnoreCase( allParts.get(i).getName())){
                searchResults.add(allParts.get(i));
            }
        }
        if(searchResults.isEmpty()){
            return null;
        }
        else{
            return searchResults;
        }
    }

    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        for(int i = 0; i < allProducts.size(); i++){
            if(productName.equalsIgnoreCase(allProducts.get(i).getName())){
                searchResults.add(allProducts.get(i));
            }
        }
        if(searchResults.isEmpty()){
            return null;
        }
        else{
            return searchResults;
        }
    }

    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    public static void deletePart(Part selectedPart){
         allParts.remove(selectedPart);
    }

    public static void deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
