
package appacademia;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


public class VistaAlumnosController implements Initializable {

    @FXML
    private AnchorPane rootVistaAlumnos;
    
    private Pane rootVistaPrincipal;
    @FXML
    private TableColumn<?, ?> columnDNI;
    @FXML
    private TableColumn<?, ?> columnNombre;
    @FXML
    private TableColumn<?, ?> columnDireccion;
    @FXML
    private TableColumn<?, ?> columnLocalidad;
    @FXML
    private TableColumn<?, ?> columnProvincia;
    @FXML
    private TableColumn<?, ?> columnTelefono;
    @FXML
    private TableView<?> tableViewAlumnos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionButtonSalir(ActionEvent event) {
        StackPane rootMain = (StackPane) rootVistaAlumnos.getScene().getRoot();
        rootMain.getChildren().remove(rootVistaAlumnos);
        rootVistaPrincipal.setVisible(true);
    }
    
    // Luego se implementa en clase de métodos generales
    public void setRootVistaPrincipal(Pane rootVistaPrincipal){
        this.rootVistaPrincipal = rootVistaPrincipal;
    }
    
}
