/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appacademia;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Juanjo
 */
public class VistaCursoController implements Initializable {

    // ------------------------------------------------------------------- VARIABLES
        private Pane rootVistaPrincipal;
        private List<String> instructores;
        
    // ------------------------------------------------------------------- NODOS
    @FXML
    private AnchorPane rootVistaCurso;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldDuracion;
    @FXML
    private TextField textFieldProveedor;
    @FXML
    private ComboBox<?> comboBoxCategoria;
    @FXML
    private TextField textFieldCertificacion;
    @FXML
    private DatePicker datePickerFechaInicio;
    @FXML
    private DatePicker datePickerFechaFin;
    @FXML
    private Spinner<?> spinnerAsistentes;
    @FXML
    private RadioButton radioButtonOficial;
    @FXML
    private RadioButton radioButtonOnline;
    @FXML
    private RadioButton radioButtonVideoDemanda;
    @FXML
    private TextField textFieldImporte;
    @FXML
    private ComboBox<String> comboBoxInstructor;
    @FXML
    private VBox vBoxTipoCurso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Fecha de inicio DatePicker
        datePickerFechaInicio.setValue(LocalDate.now());
        // Instructores ComboBox
        cargarInstructoresComboBox();
    }    

    @FXML
    private void onActionButtonAceptar(ActionEvent event) {
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootVistaCurso.getScene().getRoot();
        rootMain.getChildren().remove(rootVistaCurso);
        rootVistaPrincipal.setVisible(true);
    }
    
    // Luego se implementa en clase de métodos generales
    public void setRootVistaPrincipal(Pane rootVistaPrincipal){
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    @FXML
    private void onActionButtonLimpiar(ActionEvent event) {
    }
    
    // ------------------------------------------------------------------- MÉTODOS
    // Método para cargar los instructores a la lista
    private void cargarInstructoresComboBox(){
        
        // Creamos la lista de los instructores
        instructores = new ArrayList<>();
        
        // Añadimos la listareada anteriormente a ObservableList
        ObservableList<String> instructoresObservableList = FXCollections.observableList(instructores);
        
        // Indroducimos los instructores en la ObservableList
        instructoresObservableList.add("Ana García Lomeña");
        instructoresObservableList.add("David Muñoz Pérez");
        instructoresObservableList.add("Elena López López");
        instructoresObservableList.add("Emilio Gutiérrez Marín");
        instructoresObservableList.add("Soledad Jiménez Jiménez");
        
        // Añadimos la lista de instructores al combobox
        comboBoxInstructor.setItems(instructoresObservableList);        
    }
}
