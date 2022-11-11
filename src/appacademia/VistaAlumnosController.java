package appacademia;

import entities.Alumno;
import entities.Provincia;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

public class VistaAlumnosController implements Initializable {

    @FXML
    private AnchorPane rootVistaAlumnos;

    private Pane rootVistaPrincipal;
    @FXML
    private TableView<Alumno> tableViewAlumnos;
    @FXML
    private TableColumn<Alumno, String> columnDNI;
    @FXML
    private TableColumn<Alumno, String> columnNombre;
    @FXML
    private TableColumn<Alumno, String> columnDireccion;
    @FXML
    private TableColumn<Alumno, String> columnLocalidad;
    @FXML
    private TableColumn<Alumno, String> columnProvincia;
    @FXML
    private TableColumn<Alumno, String> columnTelefono;
    @FXML
    private ComboBox<Provincia> comboBoxProvincia;

    private EntityManager em;
    private Alumno alumnoNuevo, alumnoSeleccionado;
    
    @FXML
    private TextField textFieldDNI;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldDireccion;
    @FXML
    private TextField textFieldLocalidad;
    @FXML
    private TextField textFieldTelefono;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        //Consigue el nombre de la id de provincia que tiene alumno
        columnProvincia.setCellValueFactory((cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue().getProvinciaid() != null) {
                property.setValue(cellData.getValue().getProvinciaid().getNombre());
            }
            return property;
        }));
        columnTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tableViewAlumnos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    alumnoSeleccionado = newValue;

                    if (alumnoSeleccionado != null) {
                        textFieldDNI.setText(alumnoSeleccionado.getDni());
                        textFieldNombre.setText(alumnoSeleccionado.getNombre());
                        textFieldDireccion.setText(alumnoSeleccionado.getDireccion());
                        textFieldLocalidad.setText(alumnoSeleccionado.getLocalidad());
                        comboBoxProvincia.setValue(alumnoSeleccionado.getProvinciaid());
                        textFieldTelefono.setText(alumnoSeleccionado.getTelefono());
                    } else {
                        textFieldDNI.setText("");
                        textFieldNombre.setText("");
                        textFieldDireccion.setText("");
                        textFieldLocalidad.setText("");
                        comboBoxProvincia.setValue(null);
                        textFieldTelefono.setText("");
                    }
                });
        
        caracteresValidos();
        
    }

    public void cargarAlumnos() {
        Query queryAlumnoFindAll = em.createNamedQuery("Alumno.findAll");
        List<Alumno> listAlumno = queryAlumnoFindAll.getResultList();
        tableViewAlumnos.setItems(FXCollections.observableArrayList(listAlumno)
        );
    }

    @FXML
    private void onActionButtonSalir(ActionEvent event) {
        StackPane rootMain = (StackPane) rootVistaAlumnos.getScene().getRoot();
        rootMain.getChildren().remove(rootVistaAlumnos);
        rootVistaPrincipal.setVisible(true);
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText("¿Desea suprimir el siguiente registro?");
        alert.setContentText(alumnoSeleccionado.getDni() + " " + alumnoSeleccionado.getNombre());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Acciones a realizar si el usuario acepta
            em.getTransaction().begin();
            em.merge(alumnoSeleccionado);
            em.remove(alumnoSeleccionado);
            em.getTransaction().commit();
            tableViewAlumnos.getItems().remove(alumnoSeleccionado);
            tableViewAlumnos.getFocusModel().focus(null);
            tableViewAlumnos.requestFocus();
        } else {
            // Acciones a realizar si el usuario cancela
        }

    }

    @FXML
    private void onActionButtonAnadir(ActionEvent event) {

        boolean dniCorrecto = !textFieldDNI.getText().isEmpty() && Modularizacion.comprobarDNI(textFieldDNI.getText());
        boolean errorFormatoAlumno = false;
        alumnoNuevo = new Alumno();
        boolean yaExisteAlumno = buscarDNI(textFieldDNI.getText());

        // Datos del alumnoNuevo
        if (dniCorrecto) {
            alumnoNuevo.setDni(textFieldDNI.getText());
        } else {
            errorFormatoAlumno = true;
        }

        if (!textFieldNombre.getText().isEmpty()) {
            alumnoNuevo.setNombre(textFieldNombre.getText());
        } else {
            errorFormatoAlumno = true;
        }

        if (!textFieldDireccion.getText().isEmpty()) {
            alumnoNuevo.setDireccion(textFieldDireccion.getText());
        } else {
            errorFormatoAlumno = true;
        }

        if (!textFieldTelefono.getText().isEmpty()) {
            alumnoNuevo.setTelefono(textFieldTelefono.getText());
        } else {
            errorFormatoAlumno = true;
        }

        if (!textFieldLocalidad.getText().isEmpty()) {
            alumnoNuevo.setLocalidad(textFieldLocalidad.getText());
        } else {
            errorFormatoAlumno = true;
        }

        if (comboBoxProvincia.getValue() != null) {
            alumnoNuevo.setProvinciaid(comboBoxProvincia.getValue());
        } else {
            errorFormatoAlumno = true;
        }

        if (!errorFormatoAlumno) { // Los datos introducidos son correctos
            try {
                Modularizacion.confirmationTab("Nuevo Alumno");
                Optional<ButtonType> action = Modularizacion.confirmationAlert.showAndWait();
                // Si clickamos aceptar, los datos se guardarán
                if (action.get() == ButtonType.OK) {

                    em.getTransaction().begin();

                    if (!yaExisteAlumno) {
                        em.persist(alumnoNuevo);
                    } else {
                        em.merge(alumnoNuevo);
                    }

                    em.getTransaction().commit();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Se han guardado los datos correctamente.");
                    alert.showAndWait();
                    // Cuando se presione el botón aceptar, se limpian los campos
                    limpiar();
                    cargarAlumnos();
                    tableViewAlumnos.refresh();
                }
            } catch (RollbackException ex) { // Los datos introducidos no cumplen requisitos de BD
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. " + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No se han podido guardar los cambios. " + "Compruebe que los datos cumplen los requisitos");
            alert.showAndWait();
        }

    }
    
    @FXML
    private void onActionButtonLimpiar(ActionEvent event) {
        limpiar();
    }

    // Luego se implementa en clase de métodos generales
    public void setRootVistaPrincipal(Pane rootVistaPrincipal) {
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
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

    // Método para limpiar todos los campos
    public void limpiar() {
        Modularizacion.limpiarTextField(textFieldDNI);
        Modularizacion.limpiarTextField(textFieldNombre);
        Modularizacion.limpiarTextField(textFieldDireccion);
        Modularizacion.limpiarTextField(textFieldTelefono);
        Modularizacion.limpiarTextField(textFieldLocalidad);
        Modularizacion.limpiarComboBox(comboBoxProvincia);
    }

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
    public void caracteresValidos(){
        caracteresValidosDireccion(textFieldDireccion);
        Modularizacion.soloNumeros(textFieldTelefono);
        Modularizacion.soloLetras(textFieldNombre);
        Modularizacion.soloLetras(textFieldLocalidad);
    }

}
