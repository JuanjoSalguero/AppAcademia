package appacademia;

import entities.Alumno;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class VistaAlumnosController implements Initializable {

    private EntityManager em;

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

    // Luego se implementa en clase de métodos generales
    public void setRootVistaPrincipal(Pane rootVistaPrincipal) {
        this.rootVistaPrincipal = rootVistaPrincipal;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

}