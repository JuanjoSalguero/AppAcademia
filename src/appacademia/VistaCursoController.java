/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appacademia;

import entities.Curso;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

/**
 * FXML Controller class
 *
 * @author Juanjo
 */
public class VistaCursoController implements Initializable {

    // ------------------------------------------------------------------- VARIABLES
    private Pane rootVistaPrincipal;
    private List<String> instructores;
    private List<String> categorias;
    private ToggleGroup tipo;
    private EntityManager em;
    private Curso curso;
    private static boolean isLightMode = true;

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
    private ComboBox<String> comboBoxCategoria;
    @FXML
    private TextField textFieldCertificacion;
    @FXML
    private DatePicker datePickerFechaInicio;
    @FXML
    private DatePicker datePickerFechaFin;
    @FXML
    private Spinner<Integer> spinnerAsistentes;
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
    private CheckBox checkBoxBeca;
    @FXML
    private ImageView icono;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Control de errores y restricciones de los objetos
        controlYRestriccionErrores();
        //Font.loadFont(getClass().getResourceAsStream("styles/KronaOneRegular.ttf"), 14);
        // Nº asistentes Spinner
        cargarSpinner();
        // Instructores ComboBox
        cargarInstructoresComboBox();
        // Tipo de curso (ToggleGroup)
        Modularizacion.TipoToggleGroup(tipo, radioButtonOficial, radioButtonOnline, radioButtonVideoDemanda);
        // Categorías
        cargarCategoriasComboBox();
    }

    // Setter
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    public void setRootVistaPrincipal(Pane rootVistaPrincipal) {
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    @FXML
    private void onActionButtonAceptar(ActionEvent event) {

        boolean errorFormato = false;
        curso = new Curso();

        // Nombre del curso
        if (!textFieldNombre.getText().isEmpty()) {
            curso.setNombre(textFieldNombre.getText());
            Modularizacion.resetearError(textFieldNombre);
        } else {
            Modularizacion.errorTextField(textFieldNombre);
            errorFormato = true;
        }

        // Duración del curso
        if (!textFieldDuracion.getText().isEmpty()) {
            curso.setDuracion(Integer.parseInt(textFieldDuracion.getText()));
            Modularizacion.resetearError(textFieldDuracion);
        } else {
            Modularizacion.errorTextField(textFieldDuracion);
            errorFormato = true;
        }

        // Proveedor
        if (!textFieldProveedor.getText().isEmpty()) {
            curso.setProveedor(textFieldProveedor.getText());
            Modularizacion.resetearError(textFieldProveedor);
        } else {
            Modularizacion.errorTextField(textFieldProveedor);
            errorFormato = true;
        }

        // Categoria
        if (comboBoxCategoria.getValue() != null) {
            curso.setCategoria(comboBoxCategoria.getValue());
            Modularizacion.resetearError(comboBoxCategoria);
        } else {
            Modularizacion.errorComboBox(comboBoxCategoria);
            errorFormato = true;
        }

        // Certificación
        if (!textFieldCertificacion.getText().isEmpty()) {
            curso.setCertificacion(textFieldCertificacion.getText());
            Modularizacion.resetearError(textFieldCertificacion);
        } else {
            Modularizacion.errorTextField(textFieldCertificacion);
            errorFormato = true;
        }

        // Fecha inicio
        if (datePickerFechaInicio.getValue() != null) {
            LocalDate localDate = datePickerFechaInicio.getValue();
            ZonedDateTime zonedDateTime
                    = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            curso.setFechaInicio(date);
            Modularizacion.resetearError(datePickerFechaInicio);
        } else {
            Modularizacion.errorDatePicker(datePickerFechaInicio);
            errorFormato = true;
        }

        // Fecha fin
        if (datePickerFechaFin.getValue() != null) {
            LocalDate localDate = datePickerFechaFin.getValue();
            ZonedDateTime zonedDateTime
                    = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            curso.setFechaFin(date);
            Modularizacion.resetearError(datePickerFechaFin);
        } else {
            Modularizacion.errorDatePicker(datePickerFechaFin);
            errorFormato = true;
        }

        // Número de asistentes
        if (!spinnerAsistentes.getValue().equals(0)) {
            curso.setNumAsistentes(spinnerAsistentes.getValue());
            Modularizacion.resetearError(spinnerAsistentes);
        } else {
            Modularizacion.errorSpinner(spinnerAsistentes);
            errorFormato = true;
        }

        // Tipo de curso 
        if (radioButtonOficial.isSelected()) {  // Oficial
            curso.setTipo("Oficial");
        } else if (radioButtonOnline.isSelected()) {    // Online
            curso.setTipo("Online");
        } else if (radioButtonVideoDemanda.isSelected()) {    // Video bajo demanda
            curso.setTipo("Vídeo bajo demanda");
        }

        // Importe
        if (!textFieldImporte.getText().isEmpty()) {
            curso.setImporte(BigDecimal.valueOf(Double.parseDouble(textFieldImporte.getText().substring(0, textFieldImporte.getText().length() -1))));
            Modularizacion.resetearError(textFieldImporte);
        } else {
            Modularizacion.errorTextField(textFieldImporte);
            errorFormato = true;
        }

        // Instructor
        if (comboBoxInstructor.getValue() != null) {
            curso.setInstructor(comboBoxInstructor.getValue());
            Modularizacion.resetearError(comboBoxInstructor);
        } else {
            Modularizacion.errorComboBox(comboBoxInstructor);
            errorFormato = true;
        }

        // Beca
        if (checkBoxBeca.isSelected()) {
            curso.setBeca(true);
        } else {
            curso.setBeca(false);
        }

        if (!errorFormato) { // Los datos introducidos son correctos
            try {
                Modularizacion.confirmationTab("Nuevo Curso");
                Optional<ButtonType> action = Modularizacion.confirmationAlert.showAndWait();
                // Si clickamos aceptar, los datos se guardarán
                if (action.get() == ButtonType.OK) {
                    em.getTransaction().begin();
                    em.persist(curso);
                    em.getTransaction().commit();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Se han guardado los datos correctamente.");
                    alert.showAndWait();
                    // Cuando se presione el botón aceptar, se limpian los campos
                    limpiar();
                }
            } catch (RollbackException ex) { // Los datos introducidos no cumplen requisitos de BD
            }
        } else {
            Modularizacion.errorTab();
        }
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootVistaCurso.getScene().getRoot();
        rootMain.getChildren().remove(rootVistaCurso);
        rootVistaPrincipal.setVisible(true);
    }

    @FXML
    private void onActionButtonLimpiar(ActionEvent event) {
        limpiar(); // Limpiar todos los campos de la pestaña
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VistaInfoCurso.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            VistaInfoCursoController vistaInfoCursoController = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog();
            dialog.setDialogPane(dialogPane);
            //dialog.initStyle(StageStyle.UNIFIED);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.CLOSE) {
                dialog.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(VistaCursoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ------------------------------------------------------------------- MÉTODOS
    // Método para cargar los instructores a la lista
    private void cargarInstructoresComboBox() {

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

    // Método para crear el número de asistentes (spinner)
    private void cargarSpinner() {
        // Value factory.
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);

        spinnerAsistentes.setValueFactory(valueFactory);
        spinnerAsistentes.setEditable(true);

        spinnerAsistentes.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinnerAsistentes.increment(0); // No va a cambiar el valor pero si lo modificará
            }
        });
    }

    // Método de control y restricción de los nodos de la vista
    private void controlYRestriccionErrores() {

        // TextField Nombre restricción solo letras
        Modularizacion.soloLetras(textFieldNombre);
        // TextField Duración restricción solo números
        Modularizacion.soloNumeros(textFieldDuracion);
        // TextField Proveedor del curso restricción solo letras
        Modularizacion.soloLetras(textFieldProveedor);
        // TextField Certificación restricción solo letras
        Modularizacion.soloLetras(textFieldCertificacion);
        // Fecha por defecto del DatePicker Fecha de inicio 
        datePickerFechaInicio.setValue(LocalDate.now());
        // Spinner número de asistentes solo números
        Modularizacion.soloNumeros(spinnerAsistentes);
        // Restricción del DatePicker Fecha de Inicio
        Modularizacion.restringirDatepickers(datePickerFechaInicio, datePickerFechaFin);
        // TextField Duración restricción solo números
        Modularizacion.soloNumerosYComaYEuro(textFieldImporte);
        anadirEuro(textFieldImporte);

        // Limitar campos 
        limitarCamposMatricula();
    }

    // Método para limpiar los campos
    public void limpiar() {
        // Detalles académicos del curso
        Modularizacion.limpiarTextField(textFieldNombre);
        Modularizacion.limpiarComboBox(comboBoxCategoria);
        Modularizacion.limpiarTextField(textFieldDuracion);
        Modularizacion.limpiarTextField(textFieldCertificacion);
        Modularizacion.limpiarTextField(textFieldProveedor);
        // Detalles adicionales
        Modularizacion.fechaActualDatePicker(datePickerFechaInicio);
        Modularizacion.limpiarDatePicker(datePickerFechaFin);
        Modularizacion.limpiarSpinner(spinnerAsistentes);
        Modularizacion.limpiarComboBox(comboBoxInstructor);
        Modularizacion.limpiarTextField(textFieldImporte);
        Modularizacion.porDefectRadioButton(radioButtonOficial);
        Modularizacion.limpiarRadioButton(radioButtonOnline);
        Modularizacion.limpiarRadioButton(radioButtonVideoDemanda);
        Modularizacion.limpiarCheckBox(checkBoxBeca);
        // Resetear tambien el color de los nodos en caso de que esté coloreado en rojo por algún error
        Modularizacion.resetearError(textFieldNombre);
        Modularizacion.resetearError(textFieldDuracion);
        Modularizacion.resetearError(textFieldProveedor);
        Modularizacion.resetearError(comboBoxCategoria);
        Modularizacion.resetearError(textFieldCertificacion);
        Modularizacion.resetearError(datePickerFechaInicio);
        Modularizacion.resetearError(datePickerFechaFin);
        Modularizacion.resetearError(spinnerAsistentes);
        Modularizacion.resetearError(textFieldImporte);
        Modularizacion.resetearError(comboBoxInstructor);
    }

    private void cargarCategoriasComboBox() {

        // Creamos la lista de las categorias
        categorias = new ArrayList<>();

        // Añadimos la lista creada anteriormente a ObservableList
        ObservableList<String> categoriasObservableList = FXCollections.observableList(categorias);

        // Indroducimos las categorias en la ObservableList
        categoriasObservableList.add("BI / Big Data");
        categoriasObservableList.add("Ciberseguridad");
        categoriasObservableList.add("DevOps");
        categoriasObservableList.add("ERP");
        categoriasObservableList.add("IT");
        categoriasObservableList.add("Int. Artificial");
        categoriasObservableList.add("Ofimática");
        categoriasObservableList.add("Programación");
        categoriasObservableList.add("Testing");

        // Añadimos la lista de categorias al combobox
        comboBoxCategoria.setItems(categoriasObservableList);
    }

    public void cambiarModo(boolean isLightMode) {
        if (isLightMode) {
            Image imagen = new Image("img/gorro1.png");
            icono.setImage(imagen);
        } else {
            Image imagen = new Image("img/gorra-de-posgrado.png");
            icono.setImage(imagen);
        }
        Modularizacion.cambiarModo(rootVistaCurso, isLightMode);
    }

    // Método para limitar la longitud de los campos
    public void limitarCamposMatricula() {

        textFieldNombre.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(50));
        textFieldCertificacion.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(100));
        textFieldProveedor.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(100));
        textFieldImporte.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(8));
    }
    
     // Método para añadir el € al importe 
    public static void anadirEuro(TextField textField){
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!textField.getText().contains("€") && !textField.getText().isEmpty()){
                    textField.setText(textField.getText() + "€");
                }
            }
        });
    }
}
