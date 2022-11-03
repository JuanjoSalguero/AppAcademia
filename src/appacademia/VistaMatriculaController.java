package appacademia;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
    private ComboBox<?> comboBoxProvincia;
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
    private ComboBox<?> comboBoxCurso;
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
        // TODO
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
    public void setRootVistaPrincipal(Pane rootVistaPrincipal){
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    @FXML
    private void onActionButtonLimpiar(ActionEvent event) {
    }
    
}