package org.openjfx.cafeteria;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.Cafe;

public class PrimaryController implements Initializable {

    @FXML
    private TextField addSaldo;

    @FXML
    private Button btnEliminarTbl;

    @FXML
    private Button btnModificarTbl;

    @FXML
    private Button btnPedir;

    @FXML
    private Button btnRecargar;

    @FXML
    private ComboBox<String> cbTamanio;

    @FXML
    private ImageView imagenCafe;

    @FXML
    private TextField lbCantidad;

    @FXML
    private RadioButton rbtnCapuccino;

    @FXML
    private RadioButton rbtnCortado;

    @FXML
    private RadioButton rbtnLatte;

    @FXML
    private Label saldoActual;

    @FXML
    public TableView<Cafe> tblCafePedido;

    @FXML
    private TableColumn colPrecio;

    @FXML
    private TableColumn colTamanio;

    @FXML
    private TableColumn colTipo;

    @FXML
    private TableColumn colFecha;

    @FXML
    private ToggleGroup tipoCafe;

    @FXML
    private ObservableList<Cafe> cafe;
    private ObservableList<Cafe> cafeFiltro;
    private ArrayList<Cafe> filteredList;

    @FXML
    private AnchorPane fondo;

    @FXML
    private TextField buscarCafe;

    public Cafe c;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void cambiarListener(ActionEvent event) {
        String newValue;
        newValue = buscarCafe.getText();
        if (newValue.isEmpty()) {
            tblCafePedido.setItems(cafe);
        } else {
            cafeFiltro = listaFiltrada(cafe, newValue);
            tblCafePedido.setItems(cafeFiltro);
        }
    }

    private boolean objetosAFiltrar(Cafe cafe, String searchText) {
        return (cafe.getTipo().toLowerCase().contains(searchText.toLowerCase()))
                || (cafe.getTamanio().toLowerCase().contains(searchText.toLowerCase()))
                || (cafe.getFecha().toLowerCase().contains(searchText.toLowerCase()));
    }

    private ObservableList<Cafe> listaFiltrada(List<Cafe> list, String searchText) {
        filteredList = new ArrayList<Cafe>();
        for (Cafe i : list) {
            if (objetosAFiltrar(i, searchText)) {
                filteredList.add(i);
            }
        }
        return FXCollections.observableList(filteredList);
    }

    @FXML
    void modificarCafe(ActionEvent event) {
        c = tblCafePedido.getSelectionModel().getSelectedItem();
        if (c == null) {
            alertaError("Error","Seleccione algo para modificar");
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
                root = loader.load();

                SecondaryController scene2Controller = loader.getController();
                scene2Controller.setBounds(c);

                scene = new Scene(root);
                stage = new Stage();
                stage.setResizable(false);
                stage.setScene(scene);

                stage.showAndWait();
                Boolean cambiar = scene2Controller.seDeseaCambiar();
                Cafe cafeCambiado = scene2Controller.getBounds();
                double sobrante = 0;
                if (cambiar == true) {
                    if (cafeCambiado.getPrecio() <= c.getPrecio()) {
                        sobrante = c.getPrecio() - cafeCambiado.getPrecio();
                        String saldoSinEuro = saldoActual.getText().substring(0, saldoActual.getText().length() - 1);     // Quitar el € al label
                        double saldoTotal = Double.parseDouble(saldoSinEuro) + sobrante;      // Crear la variable saldoTotal y sumarlo al saldo del textField
                        saldoActual.setText(saldoTotal + "€");
                        cafe.add(cafeCambiado);
                        tblCafePedido.setItems(cafe);
                        cafe.remove(c);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información");
                        alert.setContentText("Los cambios han sido aplicados con exito");
                        alert.showAndWait();
                    } else if (cafeCambiado.getPrecio() > c.getPrecio()) {
                        sobrante = cafeCambiado.getPrecio() - c.getPrecio();
                        String saldoSinEuro = saldoActual.getText().substring(0, saldoActual.getText().length() - 1);     // Quitar el € al label
                        double saldoTotal = Double.parseDouble(saldoSinEuro) - sobrante;      // Crear la variable saldoTotal y sumarlo al saldo del textField
                        if (saldoTotal < 0) {
                            alertaAviso("Warning", "No dispones del dinero suficiente");
                        } else {
                            cafe.add(cafeCambiado);
                            saldoActual.setText(saldoTotal + "€");
                            tblCafePedido.setItems(cafe);
                            cafe.remove(c);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Información");
                            alert.setContentText("Los cambios han sido aplicados con exito");
                            alert.showAndWait();
                        }
                    }
                }

            } catch (Exception e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }
    }

    @FXML
    void eliminarCafe(ActionEvent event) {
        try {
            Cafe selectedItem = tblCafePedido.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                alertaAviso("Warning", "No se ha seleccionado ninguno");
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION, "¿Quieres eliminar este cafe?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    cafe.remove(selectedItem);
                    double precio = selectedItem.getPrecio();
                    String saldoSinEuro = saldoActual.getText().substring(0, saldoActual.getText().length() - 1);
                    double saldoTotal = Double.parseDouble(saldoSinEuro) + precio;
                    saldoActual.setText(saldoTotal + "€");
                    cafeFiltro.remove(selectedItem);
                }
            }
        } catch (Exception x) {

        }
    }
    
    double definirPrecio(String tamanio){
        double precio;
        if (tamanio.equalsIgnoreCase("Pequeño")) {
                precio = 1;
            } else if (tamanio.equalsIgnoreCase("Grande")) {
                precio = 2;
            } else {
                precio = 1.5;
            }
        return precio;
    }
    
    void alertaAviso(String titulo, String textoAviso){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(textoAviso);
        alert.showAndWait();
    }
    
    void alertaError(String titulo, String textoAviso){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(textoAviso);
        alert.showAndWait();
    }

    @FXML
    void pedirCafe(ActionEvent event) {
        try {
            RadioButton tipoCafeSeleccionado = (RadioButton) tipoCafe.getSelectedToggle();
            String tipo = tipoCafeSeleccionado.getText();
            String tamanio = cbTamanio.getValue();
            String fecha = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            int cantidad = Integer.parseInt(lbCantidad.getText());
            double precio = definirPrecio(tamanio);
            String saldoSinEuro = saldoActual.getText().substring(0, saldoActual.getText().length() - 1);
            double saldoTotal = Double.parseDouble(saldoSinEuro);
            if (saldoTotal < precio * cantidad) {
                alertaAviso("Error", "Saldo insuficiente");
            } else if (cantidad <= 0) {
                alertaAviso("Error", "La cantidad es menor o igual a cero, introduzca una cantidad valida");
            }
            else {
                Cafe claseCafe = new Cafe(tipo, tamanio, precio, fecha);
                for (int i = 0; i < cantidad; i++) {
                    saldoTotal -= precio;
                    saldoActual.setText(saldoTotal + "€");

                    cafe.add(claseCafe);
                    tblCafePedido.setItems(cafe);
                }
            }
        } catch (NumberFormatException NFException) {
            alertaError("Error", "Numero de Cafes incorrecto o no especificado");
        } catch (NullPointerException NPException) {
            alertaError("Error", "Tamaño del cafe no especificado");
        }
    }

    @FXML
    void recargarSaldo(ActionEvent event) {     // Accion del boton RECARGAR
        try {
            double saldoAniadir = Double.parseDouble(addSaldo.getText());
            if(saldoAniadir <= 0){
                alertaError("Error", "Saldo incorrecto, el saldo no puede ser negativo o 0");
            }else{
                String saldoSinEuro = saldoActual.getText().substring(0, saldoActual.getText().length() - 1);
                double saldoTotal = Double.parseDouble(saldoSinEuro) + saldoAniadir;
                saldoActual.setText(saldoTotal + "€");
                addSaldo.setText("");
            }
        } catch (NumberFormatException e) {   
            alertaError("Error", "El precio introducido no es correcto");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbTamanio.getItems().addAll("Grande", "Mediano", "Pequeño");
        cafe = FXCollections.observableArrayList();

        colTipo.setCellValueFactory(new PropertyValueFactory("Tipo"));
        colTamanio.setCellValueFactory(new PropertyValueFactory("Tamanio"));
        colFecha.setCellValueFactory(new PropertyValueFactory("Fecha"));
        colPrecio.setCellValueFactory(new PropertyValueFactory("Precio"));
    }
}