module com.example.prueba1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens com.example.prueba1 to javafx.fxml;
    exports com.example.prueba1;
    exports controllers;
    opens controllers to javafx.fxml;
}