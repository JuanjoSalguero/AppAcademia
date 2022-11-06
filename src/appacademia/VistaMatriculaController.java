package appacademia;

import entities.Curso;
import entities.Provincia;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author kristian
 */
public class VistaMatriculaController implements Initializable {

    private Provincia provincia;
    private Curso curso;
    private EntityManager em;

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
    private ToggleGroup toggleGroupMatricula;
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
    private CheckBox checlBoxCertificado;
    @FXML
    private TextField textFieldImporteAbonado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    // Setter
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @FXML
    private void onActionButtonAceptar(ActionEvent event) {
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootVistaMatricula.getScene().getRoot();
        rootMain.getChildren().remove(rootVistaMatricula);
        rootVistaPrincipal.setVisible(true);

    }

    // Luego se implementa en clase de métodos generales
    public void setRootVistaPrincipal(Pane rootVistaPrincipal) {
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    @FXML
    private void onActionButtonLimpiar(ActionEvent event) {
    }

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

}
