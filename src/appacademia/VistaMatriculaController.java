package appacademia;

import entities.Alumno;
import entities.Curso;
import entities.Matricula;
import entities.MatriculaPK;
import entities.Provincia;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
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

    @FXML
    private Button buttonModificarMatricula;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Control de errores y restricciones de los objetos
        controlYRestriccionErrores();
        
        desactivarCamposAlumno(); // Desactivar campos relativos al DNI
        calcularImporte();  // Calcular importe del abono

        // ToggleGroup
        Modularizacion.TipoToggleGroup(tipo, radioButtonOrdinaria, radioButtonRepetidor, radioButtonFamNumerosa);

        // Listener para el correcto funcionamiento de los campos del alumno dependiendo de si el DNI existe, de si se cambia o si no existe
        listenerDNI();
        listenerCurso();
        
        rootVistaMatricula.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            rootVistaMatricula.requestFocus();
        });

        // Método para limitar la longitud de los campos a introducir
        limitarCamposMatricula();
    }

    @FXML
    private void onActionButtonAceptar(ActionEvent event) {
        boolean errorFormatoAlumno = false;
        idMatriculaNueva = new MatriculaPK();
        matriculaNueva = new Matricula();
        alumnoNuevo = new Alumno();
        boolean yaExisteAlumno = buscarDNI(textFieldDNI.getText());
        boolean yaExisteMatricula =false;

        // Datos del alumnoNuevo
        if (!textFieldDNI.getText().isEmpty()) {
            idMatriculaNueva.setAlumnoDni(textFieldDNI.getText());
            alumnoNuevo.setDni(textFieldDNI.getText());
            Modularizacion.resetearError(textFieldDNI);
        } else {
            Modularizacion.errorTextField(textFieldDNI);
            errorFormatoAlumno = true;

        }

        if (!textFieldNombre.getText().isEmpty()) {
            alumnoNuevo.setNombre(textFieldNombre.getText());
            Modularizacion.resetearError(textFieldNombre);
        } else {
            Modularizacion.errorTextField(textFieldNombre);
            errorFormatoAlumno = true;

        }

        if (!textFieldDireccion.getText().isEmpty()) {
            alumnoNuevo.setDireccion(textFieldDireccion.getText());
            Modularizacion.resetearError(textFieldDireccion);
        } else {
            Modularizacion.errorTextField(textFieldDireccion);
            errorFormatoAlumno = true;

        }

        if (!textFieldTelefono.getText().isEmpty()) {
            alumnoNuevo.setTelefono(textFieldTelefono.getText());
            Modularizacion.resetearError(textFieldTelefono);
        } else {
            Modularizacion.errorTextField(textFieldTelefono);
            errorFormatoAlumno = true;

        }

        if (!textFieldLocalidad.getText().isEmpty()) {
            alumnoNuevo.setLocalidad(textFieldLocalidad.getText());
            Modularizacion.resetearError(textFieldLocalidad);
        } else {
            Modularizacion.errorTextField(textFieldLocalidad);
            errorFormatoAlumno = true;

        }

        if (comboBoxProvincia.getValue() != null) {
            alumnoNuevo.setProvinciaid(comboBoxProvincia.getValue());
            Modularizacion.resetearError(comboBoxProvincia);
        } else {
            Modularizacion.errorComboBox(comboBoxProvincia);
            errorFormatoAlumno = true;

        }

        if (radioButtonRepetidor.isSelected()) {
            matriculaNueva.setTipoMatricula("Repetidor");
        } else if (radioButtonOrdinaria.isSelected()) {
            matriculaNueva.setTipoMatricula("Ordinaria");
        } else if (radioButtonFamNumerosa.isSelected()) {
            matriculaNueva.setTipoMatricula("Familia numerosa");
        }

        if (datePickerFechaMatricula.getValue() != null) {
            LocalDate localDate = datePickerFechaMatricula.getValue();
            ZonedDateTime zonedDateTime
                    = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            matriculaNueva.setFecha(date);
            Modularizacion.resetearError(datePickerFechaMatricula);
        } else {
            Modularizacion.errorDatePicker(datePickerFechaMatricula);
            errorFormatoAlumno = true;

        }

        if (comboBoxCurso.getValue() != null) {
            idMatriculaNueva.setCursoId(comboBoxCurso.getValue().getId());
            Modularizacion.resetearError(comboBoxCurso);
        } else {
            Modularizacion.errorComboBox(comboBoxCurso);
            errorFormatoAlumno = true;

        }

        if (checkBoxDocumentacion.isSelected()) {
            matriculaNueva.setDocumentacion(true);
        } else {
            matriculaNueva.setDocumentacion(false);
        }

        if (checkBoxCertificado.isSelected()) {
            matriculaNueva.setCertificado(true);
        } else {
            matriculaNueva.setCertificado(false);
        }

        if (comboBoxCurso.getValue() != null && !textFieldDNI.getText().isEmpty()) {
            yaExisteMatricula = buscarMatricula(textFieldDNI.getText(),comboBoxCurso.getValue().getId());
            matriculaNueva.setMatriculaPK(idMatriculaNueva);
        } else {
            errorFormatoAlumno = true;
        }

        if (!textFieldImporteAbonado.getText().isEmpty()) {
            matriculaNueva.setImporteAbonado(BigDecimal.valueOf(Double.parseDouble(textFieldImporteAbonado.getText())));
        } else {
            errorFormatoAlumno = true;
        }
 
        if (!errorFormatoAlumno) { // Los datos introducidos son correctos
            try {   
                // Si clickamos aceptar, los datos se guardarán   
                    em.getTransaction().begin();
                    if(yaExisteMatricula){
                        Modularizacion.confirmationTab("Actualizar Matrícula");
                        Optional<ButtonType> action1 = Modularizacion.confirmationAlert.showAndWait();
                        if (action1.get() == ButtonType.OK){
                            em.merge(alumnoNuevo);
                            em.merge(matriculaNueva);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Se han guardado los datos correctamente.");
                            alert.showAndWait();
                            // Cuando se presione el botón aceptar, se limpian los campos
                            limpiar();
                        }
                    }
                    else if(!yaExisteAlumno){
                        Modularizacion.confirmationTab("Nueva Matrícula y Nuevo Alumno");
                        Optional<ButtonType> action = Modularizacion.confirmationAlert.showAndWait();
                        if (action.get() == ButtonType.OK) {
                            em.persist(alumnoNuevo);
                            em.persist(matriculaNueva);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Se han guardado los datos correctamente.");
                            alert.showAndWait();
                            // Cuando se presione el botón aceptar, se limpian los campos
                            limpiar();
                        }
                    }
                    else{
                        Modularizacion.confirmationTab("Nueva Matrícula y Actualizar Alumno");
                        Optional<ButtonType> action1 = Modularizacion.confirmationAlert.showAndWait();
                        if (action1.get() == ButtonType.OK){
                            em.merge(alumnoNuevo);
                            em.persist(matriculaNueva);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Se han guardado los datos correctamente.");
                            alert.showAndWait();
                            // Cuando se presione el botón aceptar, se limpian los campos
                            limpiar();
                        }

                    }
                    
                    em.getTransaction().commit();

            } catch (RollbackException ex) { // Los datos introducidos no cumplen requisitos de BD
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("No se han podido guardar los cambios. " + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
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
    public void setRootVistaPrincipal(Pane rootVistaPrincipal) {
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    @FXML
    private void onActionButtonLimpiar(ActionEvent event) {
        limpiar();  // Limpiar todos los campos
        desactivarCamposAlumno();
    }
    
        @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try{
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VistaInfoMatricula.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            VistaInfoMatriculaController vistaInfoMatriculaCursoController = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog();
            dialog.setDialogPane(dialogPane);
            //dialog.initStyle(StageStyle.UNIFIED);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if(clickedButton.get() == ButtonType.CLOSE) dialog.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;

    }

    // Método para el control de los campos y restricciones de los mismos
    private void controlYRestriccionErrores() {
        buttonModificarMatricula.setDisable(true);
        datePickerFechaMatricula.setValue(LocalDate.now());
        Modularizacion.soloLetras(textFieldNombre);
        Modularizacion.numeroYMas(textFieldTelefono);
        Modularizacion.soloLetras(textFieldLocalidad);
        soloNumerosYLetras(textFieldDNI);
        Modularizacion.caracteresValidosDireccion(textFieldDireccion);
    }

    
    private void listenerDNI() {
       textFieldDNI.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!textFieldDNI.getText().isEmpty()) {
                    textFieldDNI.setText(textFieldDNI.getText().toUpperCase());
                    if(Modularizacion.comprobarDNI(textFieldDNI.getText()) && Modularizacion.validarDNI(textFieldDNI.getText())) {
                        Modularizacion.resetearError(textFieldDNI);
                        if(buscarDNI(textFieldDNI.getText()))
                            mostrarDatos(textFieldDNI.getText());
 
                        textFieldNombre.setDisable(false);
                        textFieldDireccion.setDisable(false);
                        textFieldTelefono.setDisable(false);
                        textFieldLocalidad.setDisable(false);
                        comboBoxProvincia.setDisable(false);
                        textFieldLocalidad.requestFocus();
                    }
 
                    else {
                        Modularizacion.errorTextField(textFieldDNI);
                        desactivarCamposAlumno();
                        borrarCampos();
 
                    }
 
                }
 
                else {
                    desactivarCamposAlumno();
                    borrarCampos();
 
                }
 
            }
        });
        
    }
    
    private void listenerCurso() {
        comboBoxCurso.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!textFieldDNI.getText().isEmpty()) {
                Query queryMatricula = em.createNamedQuery("Matricula.findByAlumnoDni");
                queryMatricula.setParameter("alumnoDni", textFieldDNI.getText());
                List<Matricula> listMatricula = queryMatricula.getResultList();
                buttonModificarMatricula.setDisable(true);

                activarCamposMatricula();

                int i = 0;
                boolean encontrado = false;

                while(i < listMatricula.size() && !encontrado) {
                    Matricula m = listMatricula.get(i);
                    MatriculaPK mpk = m.getMatriculaPK();

                    if(mpk.getCursoId() == comboBoxCurso.getSelectionModel().getSelectedItem().getId()) {
                        encontrado = true;
                        ButtonType si = new ButtonType("Si", ButtonBar.ButtonData.OK_DONE);
                        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,  
                        "El alumno introducido ya está matriculado en el curso seleccionado. ¿Quiere modificar la matricula?", si, no);
                        Optional<ButtonType> result = alert.showAndWait();

                        if(m.getTipoMatricula().equals("Ordinaria"))
                                radioButtonOrdinaria.setSelected(true);

                            else if(m.getTipoMatricula().equals("Repetidor"))
                                radioButtonRepetidor.setSelected(true);

                            else
                                radioButtonFamNumerosa.setSelected(true);

                            activarCamposMatricula();
                            datePickerFechaMatricula.setValue(m.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            //comboBoxCurso.getSelectionModel().select(m.getCurso());
                            checkBoxDocumentacion.setSelected(m.getDocumentacion());
                            checkBoxCertificado.setSelected(m.getCertificado());
                            textFieldImporteAbonado.setText(String.valueOf(m.getImporteAbonado()));

                        if(result.get() == no) {
                            desactivarCamposMatricula();
                            buttonModificarMatricula.setDisable(false);
                        }

                    }

                    i++;

                }
                
            }
        
        }); 
        
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

    // Método para desactivarl los campos del alumno
    private void desactivarCamposAlumno() {
        textFieldNombre.setDisable(true);
        textFieldDireccion.setDisable(true);
        textFieldTelefono.setDisable(true);
        textFieldLocalidad.setDisable(true);
        comboBoxProvincia.setDisable(true);
        textFieldImporteAbonado.setEditable(false);
    }

    // Método para activar los campos del alumno
    private void activarCamposAlumno() {
        textFieldNombre.setDisable(false);
        textFieldDireccion.setDisable(false);
        textFieldTelefono.setDisable(false);
        textFieldLocalidad.setDisable(false);
        comboBoxProvincia.setDisable(false);
    }

    //Método para desactivar los campos de la matrícula
    private void desactivarCamposMatricula() {
        radioButtonOrdinaria.setDisable(true);
        radioButtonRepetidor.setDisable(true);
        radioButtonFamNumerosa.setDisable(true);
        datePickerFechaMatricula.setDisable(true);
        checkBoxDocumentacion.setDisable(true);
        checkBoxCertificado.setDisable(true);
        
    }
    
    //Metodo para activar los campos de la matrícula
    private void activarCamposMatricula() {
        radioButtonOrdinaria.setDisable(false);
        radioButtonRepetidor.setDisable(false);
        radioButtonFamNumerosa.setDisable(false);
        datePickerFechaMatricula.setDisable(false);
        checkBoxDocumentacion.setDisable(false);
        checkBoxCertificado.setDisable(false);
        
    }
    
    //Metodo para borrar los campos de matricula
    private void limpiarCamposMatricula() {
        radioButtonOrdinaria.setSelected(true);
        radioButtonRepetidor.setSelected(false);
        radioButtonFamNumerosa.setSelected(false);
        datePickerFechaMatricula.setValue(LocalDate.now());
        checkBoxDocumentacion.setSelected(false);
        checkBoxCertificado.setSelected(false);
        
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
                if(newValue != null) {
                    double importe = newValue.getImporte().doubleValue();
                    importe = calcularExtras(importe);
                    importe = Math.round(importe * 100) / 100;
                    textFieldImporteAbonado.setText(String.valueOf(importe));
                }
            }
        });

        radioButtonOrdinaria.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!textFieldImporteAbonado.getText().isEmpty() && comboBoxCurso.getValue() != null) {
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
                if (!textFieldImporteAbonado.getText().isEmpty() && comboBoxCurso.getValue() != null) {
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
                if (!textFieldImporteAbonado.getText().isEmpty() && comboBoxCurso.getValue() != null) {
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
                if (!textFieldImporteAbonado.getText().isEmpty() && comboBoxCurso.getValue() != null) {
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

        if (radioButtonRepetidor.isSelected()) {
            importeFinal += (importe * 0.1);
        } else if (radioButtonFamNumerosa.isSelected()) {
            importeFinal -= (importe * 0.1);
        }

        if (checkBoxDocumentacion.isSelected()) {
            importeFinal += (importe * 0.05);
        }

        return importeFinal;
    }

    // Método creado para comprobar si el DNI existe o no
    private boolean buscarDNI(String DNI) {
        boolean dniEncontrado;
        Query queryDNIAlumnos = em.createNamedQuery("Alumno.findByDni");
        queryDNIAlumnos.setParameter("dni", DNI);
        List<String> listDNIAlumnos = queryDNIAlumnos.getResultList();

        if (listDNIAlumnos.isEmpty()) {
            dniEncontrado = false;
        } else {
            dniEncontrado = true;
        }
        return dniEncontrado;
    }

    private boolean buscarMatricula(String DNI, int id) {
        boolean matriculaEncontrada;
        Query queryMatricula = em.createNamedQuery("Matricula.findByPK");
        queryMatricula.setParameter("alumnoDni", DNI);
        queryMatricula.setParameter("cursoId", id);
        List<String> listDNIAlumno = queryMatricula.getResultList();
        
        if(listDNIAlumno.isEmpty()){
            matriculaEncontrada = false;
        }
        else{
            matriculaEncontrada = true;
        }
        return matriculaEncontrada;
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
        List<Curso> listCurso = queryCursoFindAll.getResultList();
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
    public void limpiar() {
        Modularizacion.limpiarTextField(textFieldDNI);
        Modularizacion.limpiarTextField(textFieldNombre);
        Modularizacion.limpiarTextField(textFieldDireccion);
        Modularizacion.limpiarTextField(textFieldTelefono);
        Modularizacion.limpiarTextField(textFieldLocalidad);
        Modularizacion.limpiarComboBox(comboBoxProvincia);
        Modularizacion.limpiarRadioButton(radioButtonOrdinaria);
        Modularizacion.limpiarRadioButton(radioButtonRepetidor);
        Modularizacion.limpiarRadioButton(radioButtonFamNumerosa);
        Modularizacion.porDefectRadioButton(radioButtonOrdinaria);
        Modularizacion.fechaActualDatePicker(datePickerFechaMatricula);
        Modularizacion.limpiarComboBox(comboBoxCurso);
        Modularizacion.limpiarCheckBox(checkBoxCertificado);
        Modularizacion.limpiarCheckBox(checkBoxDocumentacion);
        Modularizacion.limpiarTextField(textFieldImporteAbonado);

         // Resetear tambien el color de los nodos en caso de que esté coloreado en rojo por algún error
        Modularizacion.resetearError(textFieldDNI);
        Modularizacion.resetearError(textFieldNombre);
        Modularizacion.resetearError(textFieldDireccion);
        Modularizacion.resetearError(textFieldTelefono);
        Modularizacion.resetearError(textFieldLocalidad);
        Modularizacion.resetearError(comboBoxProvincia);
        Modularizacion.resetearError(datePickerFechaMatricula);
        Modularizacion.resetearError(comboBoxCurso);
    }

    public void cambiarModo(boolean isLightMode) {
        Modularizacion.cambiarModo(rootVistaMatricula, isLightMode);
    }

    // Limitar campos
    public void limitarCamposMatricula() {

        textFieldDNI.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(9));
        textFieldNombre.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(50));
        textFieldDireccion.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(150));
        textFieldTelefono.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(12));
        textFieldLocalidad.addEventFilter(KeyEvent.KEY_TYPED, Modularizacion.longitudMaxima(50));
    }

    @FXML
    private void onActionButtonModificarMatricula(ActionEvent event) {
        activarCamposMatricula();
        buttonModificarMatricula.setDisable(true);
        
    }
}
