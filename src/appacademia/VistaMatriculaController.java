package appacademia;

import entities.Alumno;
import entities.Curso;
import entities.Matricula;
import entities.MatriculaPK;
import entities.Provincia;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

/**
 * FXML Controller class
 *
 * @author kristian
 */
public class VistaMatriculaController implements Initializable {

    @FXML
    private AnchorPane rootVistaMatricula;
    
    private Pane rootVistaPrincipal;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldDNI;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private TextField textFieldDireccion;
    @FXML
    private TextField textFieldLocalidad;
    @FXML
    private ComboBox<Provincia> comboBoxProvincia;
    @FXML
    private RadioButton radioButtonOrdinaria;
    @FXML
    private RadioButton radioButtonRepetidor;
    @FXML
    private RadioButton radioButtonFamNumerosa;
    @FXML
    private DatePicker datePickerFechaMatricula;
    @FXML
    private ComboBox<Curso> comboBoxCurso;
    @FXML
    private CheckBox checkBoxDocumentacion;
    @FXML
    private CheckBox checkBoxCertificado;
    @FXML
    private TextField textFieldImporteAbonado;

    private EntityManager em;
    
    private boolean dniCorrecto;
    private Matricula matriculaNueva;
    private MatriculaPK idMatriculaNueva;
    private Alumno alumnoNuevo;
    private ToggleGroup tipo;
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Control de errores y restricciones de los objetos
        controlYRestriccionErrores();
        
        desactivarCampos(); // Desactivar campos relativos al DNI
        calcularImporte();  // Calcular importe del abono
        
        // ToggleGroup
        Modularizacion.TipoToggleGroup(tipo, radioButtonOrdinaria, radioButtonRepetidor, radioButtonFamNumerosa);
        
        // Listener para el correcto funcionamiento de los campos del alumno dependiendo de si el DNI existe, de si se cambia o si no existe
        textFieldDNI.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue) {
                    if(!textFieldDNI.getText().isEmpty()) {
                        textFieldDNI.setText(textFieldDNI.getText().toUpperCase());
                        if(comprobarDNI(textFieldDNI.getText())) {
                            if(buscarDNI(textFieldDNI.getText())) {
                                mostrarDatos(textFieldDNI.getText());
                            }
                                
                            textFieldNombre.setDisable(false);
                            textFieldDireccion.setDisable(false);
                            textFieldTelefono.setDisable(false);
                            textFieldLocalidad.setDisable(false);
                            comboBoxProvincia.setDisable(false);
                        }
                    }
                }
            }
        });
        
        rootVistaMatricula.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                        rootVistaMatricula.requestFocus();
                    });
    }    

    @FXML
    private void onActionButtonAceptar(ActionEvent event) {
        
        boolean errorFormatoAlumno = false;
        idMatriculaNueva = new MatriculaPK();
        matriculaNueva = new Matricula();
        alumnoNuevo = new Alumno();
        boolean yaExisteAlumno = buscarDNI(textFieldDNI.getText());
        
        // Datos del alumnoNuevo
        if (!textFieldDNI.getText().isEmpty()){
            idMatriculaNueva.setAlumnoDni(textFieldDNI.getText());
            alumnoNuevo.setDni(textFieldDNI.getText());
        }else{
            errorFormatoAlumno = true;
        }
        
        if (!textFieldNombre.getText().isEmpty()){
            alumnoNuevo.setNombre(textFieldNombre.getText());
        }else{
            errorFormatoAlumno = true;
        }
        
        if (!textFieldDireccion.getText().isEmpty()){
            alumnoNuevo.setDireccion(textFieldDireccion.getText());
        }else{
            errorFormatoAlumno = true;
        }
        
        if (!textFieldTelefono.getText().isEmpty()){
            alumnoNuevo.setTelefono(textFieldTelefono.getText());
        }else{
            errorFormatoAlumno = true;
        }
        
        if (!textFieldLocalidad.getText().isEmpty()){
            alumnoNuevo.setLocalidad(textFieldLocalidad.getText());
        }else{
            errorFormatoAlumno = true;
        }
        
        if (comboBoxProvincia.getValue() != null){
            alumnoNuevo.setProvinciaid(comboBoxProvincia.getValue());
        } else {
            errorFormatoAlumno = true;
        }
        
        if (radioButtonRepetidor.isSelected()){
            matriculaNueva.setTipoMatricula("Repetidor");
        } else if (radioButtonOrdinaria.isSelected()){
            matriculaNueva.setTipoMatricula("Ordinaria");
        } else if (radioButtonFamNumerosa.isSelected()){
            matriculaNueva.setTipoMatricula("Familia numerosa");
        }
        
        if (datePickerFechaMatricula.getValue() != null){
            LocalDate localDate = datePickerFechaMatricula.getValue();
            ZonedDateTime zonedDateTime =
            localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            matriculaNueva.setFecha(date);
        } else {
            errorFormatoAlumno = true;
        }
        
        if (comboBoxCurso.getValue() != null){
            idMatriculaNueva.setCursoId(comboBoxCurso.getValue().getId());
        } else {
            errorFormatoAlumno = true;
        }
        
        if(checkBoxDocumentacion.isSelected()){
            matriculaNueva.setDocumentacion(true);
        }else {
            matriculaNueva.setDocumentacion(false);
        }
        
        if(checkBoxCertificado.isSelected()){
            matriculaNueva.setCertificado(true);
        }else {
            matriculaNueva.setCertificado(false);
        }
        
        if (comboBoxCurso.getValue() != null && !textFieldDNI.getText().isEmpty()){
            matriculaNueva.setMatriculaPK(idMatriculaNueva);
        } else {
            errorFormatoAlumno = true;
        }
        
        if(!textFieldImporteAbonado.getText().isEmpty()){
            matriculaNueva.setImporteAbonado(BigDecimal.valueOf(Double.parseDouble(textFieldImporteAbonado.getText())));
        } else {
            errorFormatoAlumno = true;
        }
        
        if (!errorFormatoAlumno) { // Los datos introducidos son correctos
            try {   
                Modularizacion.confirmationTab("Nueva Matrícula");
                Optional<ButtonType> action = Modularizacion.confirmationAlert.showAndWait();
                // Si clickamos aceptar, los datos se guardarán
                if (action.get() == ButtonType.OK) {
                    
                    em.getTransaction().begin();
                    
                    if(!yaExisteAlumno){
                        em.persist(alumnoNuevo);
                    }
                    else{
                        em.merge(alumnoNuevo);
                    }
                    em.persist(matriculaNueva);

                    em.getTransaction().commit();
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Se han guardado los datos correctamente.");
                    alert.showAndWait();
                    // Cuando se presione el botón aceptar, se limpian los campos
                    limpiar();
                }
            } catch (RollbackException ex) { // Los datos introducidos no cumplen requisitos de BD
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. " + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No se han podido guardar los cambios. " + "Compruebe que los datos cumplen los requisitos");
            alert.showAndWait();
        }
        
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootVistaMatricula.getScene().getRoot();
        rootMain.getChildren().remove(rootVistaMatricula);
        rootVistaPrincipal.setVisible(true);
        
    }
    
    // Vista
    public void setRootVistaPrincipal(Pane rootVistaPrincipal){
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    @FXML
    private void onActionButtonLimpiar(ActionEvent event) {
        limpiar();  // Limpiar todos los campos
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    
    }
    
    // Método para el control de los campos y restricciones de los mismos
    private void controlYRestriccionErrores() {
        datePickerFechaMatricula.setValue(LocalDate.now());
        Modularizacion.soloLetras(textFieldNombre);
        Modularizacion.soloNumeros(textFieldTelefono);
        Modularizacion.soloLetras(textFieldLocalidad);
        soloNumerosYLetras(textFieldDNI);
        caracteresValidosDireccion(textFieldDireccion);
    }
    
    // Método de restricción de textField solo letras y números
    private static void soloNumerosYLetras(TextField textField) {

        textField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[a-zA-Z1234567890*]")) {
                    textField.setText(newValue.replaceAll("[^a-zA-Z1234567890]", ""));
                }
            }
        });
    }
    
    // Método para la restricción de caracteres de un textfield DIRECCIÓN
    private static void caracteresValidosDireccion(TextField textField) {

        textField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[a-zA-Z0-9\\sñÑáéíóúÁÉÍÓÚ.,/ºª]")) {
                    textField.setText(newValue.replaceAll("[^a-zA-Z0-9\\sñÑáéíóúÁÉÍÓÚ.,/ºª]", ""));
                }
            }
        });
    }
    
    // Método para desactivarl los campos del alumno
    private void desactivarCampos() {
        textFieldNombre.setDisable(true);
        textFieldDireccion.setDisable(true);
        textFieldTelefono.setDisable(true);
        textFieldLocalidad.setDisable(true);
        comboBoxProvincia.setDisable(true);
        textFieldImporteAbonado.setEditable(false);
    }
    
    // Método para activar los campos del alumno
    private void activarCampos() {
        textFieldNombre.setDisable(false);
        textFieldDireccion.setDisable(false);
        textFieldTelefono.setDisable(false);
        textFieldLocalidad.setDisable(false);
        comboBoxProvincia.setDisable(false);
    }
    
    // Método para borrar los campos del alumno
    private void borrarCampos() {
        textFieldNombre.setText("");
        textFieldDireccion.setText("");
        textFieldTelefono.setText("");
        textFieldLocalidad.setText("");
        comboBoxProvincia.setValue(null);
    }
    
    // Método para el cálculo de los importes de la matricula
    private void calcularImporte() {
        
        comboBoxCurso.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Curso>() {
            @Override
            public void changed(ObservableValue<? extends Curso> observable, Curso oldValue, Curso newValue) {
                double importe = newValue.getImporte().doubleValue();
                importe = calcularExtras(importe);
                importe = Math.round(importe * 100) / 100;
                textFieldImporteAbonado.setText(String.valueOf(importe));
            }
        });
    
        radioButtonOrdinaria.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!textFieldImporteAbonado.getText().isEmpty()) {
                    double importe = comboBoxCurso.getValue().getImporte().doubleValue();
                    importe = calcularExtras(importe);
                    importe = Math.round(importe * 100) / 100;
                    textFieldImporteAbonado.setText(String.valueOf(importe));
                }
            }
        });
        
        radioButtonRepetidor.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!textFieldImporteAbonado.getText().isEmpty()) {
                    double importe = comboBoxCurso.getValue().getImporte().doubleValue();
                    importe = calcularExtras(importe);
                    importe = Math.round(importe * 100) / 100;
                    textFieldImporteAbonado.setText(String.valueOf(importe));   
                }
            }
        });
       
        radioButtonFamNumerosa.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!textFieldImporteAbonado.getText().isEmpty()) {
                    double importe = comboBoxCurso.getValue().getImporte().doubleValue();
                    importe = calcularExtras(importe);
                    importe = Math.round(importe * 100) / 100;
                    textFieldImporteAbonado.setText(String.valueOf(importe));
                }
            }
        });
        
        checkBoxDocumentacion.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!textFieldImporteAbonado.getText().isEmpty()) {
                    double importe = comboBoxCurso.getValue().getImporte().doubleValue();
                    importe = calcularExtras(importe);
                    importe = Math.round(importe * 100) / 100;
                    textFieldImporteAbonado.setText(String.valueOf(importe));
                }
            }
        });
        
    }
    
    // Método para calcular el importe a abonar de la matricula
    private double calcularExtras(double importe) {
        double importeFinal = importe;
        
        if(radioButtonRepetidor.isSelected())
            importeFinal += (importe * 0.1);
        
        else if(radioButtonFamNumerosa.isSelected())
            importeFinal -= (importe * 0.1);
        
        if(checkBoxDocumentacion.isSelected())
            importeFinal += (importe * 0.05);
    
        return importeFinal;
    }
    
    // Método para combrobar que el dni introducido cumple los parametros(8 números y 1 letra)
    private boolean comprobarDNI(String DNI) {
        boolean dniCorrecto = true;
        
        DNI = DNI.toUpperCase();
        
        if(DNI.length() != 9 || !Character.isLetter(DNI.charAt(8)))
            return false;
        
        int i = 0;
        
        while(i < 8 && dniCorrecto) {
            if(!Character.isDigit(DNI.charAt(i)))
                dniCorrecto = false;
            
            i++;
        
        }
    
        if(dniCorrecto) {
            if(!(DNI.charAt(8) >= 65 && DNI.charAt(8) <= 90))
                dniCorrecto = false;
        }
        
        return dniCorrecto;
    }
    
    // Método creado para comprobar si el DNI existe o no
    private boolean buscarDNI(String DNI) {
        boolean dniEncontrado;
        Query queryDNIAlumnos = em.createNamedQuery("Alumno.findByDni");
        queryDNIAlumnos.setParameter("dni", DNI);
        List<String> listDNIAlumnos = queryDNIAlumnos.getResultList();
        
        if(listDNIAlumnos.isEmpty()){
            dniEncontrado = false;
        }
        else{
            dniEncontrado = true;
        }
        return dniEncontrado;
    }
    
    // Método para rellenar los datos de los campos asociados al DNI
    private void mostrarDatos(String DNI) {
        Query queryAlumnos = em.createNamedQuery("Alumno.findByDni");
        queryAlumnos.setParameter("dni", DNI);
        List<Alumno> listAlumnos = queryAlumnos.getResultList();
        Alumno alumno = listAlumnos.get(0);
       
        textFieldNombre.setText(alumno.getNombre());
        textFieldDireccion.setText(alumno.getDireccion());
        textFieldTelefono.setText(alumno.getTelefono());
        textFieldLocalidad.setText(alumno.getLocalidad());
        comboBoxProvincia.setValue(alumno.getProvinciaid());
    }
    
    // Método para rellenar la provincia
    public void rellenarComboBoxProvincia() {
        Query queryProvinciaFindAll = em.createNamedQuery("Provincia.findAll");
        List<Provincia> listProvincia = queryProvinciaFindAll.getResultList();
        comboBoxProvincia.setItems(FXCollections.observableList(listProvincia));
        
        comboBoxProvincia.setCellFactory(
                (ListView<Provincia> l) -> new ListCell<Provincia>() {
            @Override
            protected void updateItem(Provincia provincia, boolean empty) {
                super.updateItem(provincia, empty);
                if (provincia == null || empty) {
                    setText("");
                } else {
                    setText(provincia.getNombre());
                }
            }
        });
        comboBoxProvincia.setConverter(new StringConverter<Provincia>() {
            @Override
            public String toString(Provincia provincia) {
                if (provincia == null) {
                    return null;
                } else {
                    return provincia.getNombre();
                }
            }

            @Override
            public Provincia fromString(String userId) {
                return null;
            }
        });
    
    }
    
    // Método para rellenar el combobox
    public void rellenarComboBoxCurso() {
        Query queryCursoFindAll = em.createNamedQuery("Curso.findAll");
        List <Curso> listCurso = queryCursoFindAll.getResultList();
        comboBoxCurso.setItems(FXCollections.observableList(listCurso));

        comboBoxCurso.setCellFactory(
                (ListView<Curso> l) -> new ListCell<Curso>() {
            @Override
            protected void updateItem(Curso curso, boolean empty) {
                super.updateItem(curso, empty);
                if (curso == null || empty) {
                    setText("");
                } else {
                    setText(curso.getNombre());
                }
            }
        });
        comboBoxCurso.setConverter(new StringConverter<Curso>() {
            @Override
            public String toString(Curso curso) {
                if (curso == null) {
                    return null;
                } else {
                    return curso.getNombre();
                }
            }

            @Override
            public Curso fromString(String userId) {
                return null;
            }
        });
    }
    
    // Método para limpiar todos los campos
    public void limpiar(){
        Modularizacion.limpiarTextField(textFieldDNI);
        Modularizacion.limpiarTextField(textFieldNombre);
        Modularizacion.limpiarTextField(textFieldDireccion);
        Modularizacion.limpiarTextField(textFieldTelefono);
        Modularizacion.limpiarTextField(textFieldLocalidad);
        Modularizacion.limpiarComboBox(comboBoxProvincia);
        Modularizacion.limpiarRadioButton(radioButtonOrdinaria);
        Modularizacion.limpiarRadioButton(radioButtonRepetidor);
        Modularizacion.limpiarRadioButton(radioButtonFamNumerosa);
        Modularizacion.fechaActualDatePicker(datePickerFechaMatricula);
        Modularizacion.limpiarComboBox(comboBoxCurso);
        Modularizacion.limpiarCheckBox(checkBoxCertificado);
        Modularizacion.limpiarCheckBox(checkBoxDocumentacion);
        Modularizacion.limpiarTextField(textFieldImporteAbonado);
    }
    
}