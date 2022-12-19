package org.openjfx.cafeteria;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.Cafe;
import org.openjfx.cafeteria.PrimaryController;

public class SecondaryController implements Initializable {

    PrimaryController pc;

    @FXML
    private AnchorPane scenePane;

    @FXML
    public ToggleGroup botonesTipo;

    @FXML
    public Button btnCambiar;

    @FXML
    public Button btnCancelar;

    @FXML
    public ComboBox<String> cbTamanio;

    @FXML
    public RadioButton rbCapuccino;

    @FXML
    public RadioButton rbCortado;

    @FXML
    public RadioButton rbLatte;

    public ObservableList<Cafe> cafe;

    public Stage stage;
    public Scene scene;
    public Parent root;
    public Boolean cambiar = false;

    @FXML
    void cambiarCafe(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Quieres guardar los cambios?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
            cambiar = true;
        }
    }

    @FXML
    private void cancelarCambios(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Quieres cancelar los cambios?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
            cambiar = false;
        }
    }

    Boolean seDeseaCambiar() {
        return cambiar;
    }

    Cafe getBounds() {
        RadioButton tipoCafeSeleccionado = (RadioButton) botonesTipo.getSelectedToggle();      // Saber que tipo de cafe a elegido
        String tipo = tipoCafeSeleccionado.getText();       // Pasar el tipo seleccionado a String
        String tamanio = cbTamanio.getValue();         // Pasar el tamaño seleccionado a String
        double precio;      // Definir el precio para dependiendo del tamaño aplicarlo
        if (tamanio.equalsIgnoreCase("Pequeño")) {
            precio = 1;
        } else if (tamanio.equalsIgnoreCase("Grande")) {
            precio = 2;
        } else {
            precio = 1.5;
        }
        String fecha = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Cafe cafeCambiado = new Cafe(tipo, tamanio, precio, fecha);

        return cafeCambiado;
    }

    Cafe setBounds(Cafe cafe) {
        cbTamanio.setValue(cafe.getTamanio());
        String value = cafe.getTipo();
        if (value.equalsIgnoreCase(rbCapuccino.getText())) {
            rbCapuccino.setSelected(true);
        } else if (value.equalsIgnoreCase(rbCortado.getText())) {
            rbCortado.setSelected(true);
        } else if (value.equalsIgnoreCase(rbLatte.getText())) {
            rbLatte.setSelected(true);
        }
        return cafe;
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.cbTamanio.getItems().addAll("Grande", "Mediano", "Pequeño");
    }

}
