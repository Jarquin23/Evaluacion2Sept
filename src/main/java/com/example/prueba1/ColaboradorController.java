package com.example.prueba1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ColaboradorController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
