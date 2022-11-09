/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appacademia;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;

/**
 *
 * @author
 */
public class VistaPrincipalController implements Initializable {

    private EntityManager em;
    private boolean isLightMode = true;

    @FXML
    private AnchorPane rootVistaPrincipal;
    
    @FXML
    private ImageView imagenModo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    // Setter
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @FXML
    private void onActionMenuItemNuevo(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaCurso.fxml"));
            Parent rootVistaCurso = fxmlLoader.load();
            VistaCursoController vistaCursoController = (VistaCursoController) fxmlLoader.getController();
            vistaCursoController.setRootVistaPrincipal(rootVistaPrincipal);
            // Asociar objeto a la clase VistaCursoController
            vistaCursoController.setEntityManager(em);
            
            // Ocultar la vista de la lista
            rootVistaPrincipal.setVisible(false);

            //Añadir la vista Curso al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootVistaPrincipal.getScene().getRoot();
            rootMain.getChildren().add(rootVistaCurso);
        } catch (IOException ex) {
            Logger.getLogger(VistaCursoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionMenuItemMatricula(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaMatricula.fxml"));
            Parent rootVistaMatricula = fxmlLoader.load();
            VistaMatriculaController vistaMatriculaController = (VistaMatriculaController) fxmlLoader.getController();
            vistaMatriculaController.setRootVistaPrincipal(rootVistaPrincipal);
            // Asociar objeto a la clase VistaMatriculaController
            vistaMatriculaController.setEntityManager(em);
            vistaMatriculaController.rellenarComboBoxProvincia();
            vistaMatriculaController.rellenarComboBoxCurso();

            // Ocultar la vista de la lista
            rootVistaPrincipal.setVisible(false);

            //Añadir la vista Matricula al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootVistaPrincipal.getScene().getRoot();
            rootMain.getChildren().add(rootVistaMatricula);
        } catch (IOException ex) {
            Logger.getLogger(VistaMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionMenuItemTablaAlumnos(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaAlumnos.fxml"));
            Parent rootVistaAlumnos = fxmlLoader.load();
            VistaAlumnosController vistaAlumnosController = (VistaAlumnosController) fxmlLoader.getController();
            vistaAlumnosController.setRootVistaPrincipal(rootVistaPrincipal);
            // Asociar objeto a la clase VistaMatriculaController
            vistaAlumnosController.setEntityManager(em);
            vistaAlumnosController.cargarAlumnos();

            // Ocultar la vista de la lista
            rootVistaPrincipal.setVisible(false);

            //Añadir la vista Matricula al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootVistaPrincipal.getScene().getRoot();
            rootMain.getChildren().add(rootVistaAlumnos);
        } catch (IOException ex) {
            Logger.getLogger(VistaAlumnosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para cambiar el modo de la vista (noche o dia)
    public void cambiarModo(ActionEvent event){
        isLightMode = !isLightMode;
        if (isLightMode){
            establecerModoDia();
        } else {
            establecerModoNoche();
        }
    }
    
    // Método para cambiar a modo diurno
    private void establecerModoDia(){
        rootVistaPrincipal.getStylesheets().remove("styles/darkMode.css");
        rootVistaPrincipal.getStylesheets().add("styles/lightMode.css");
        Image imagen = new Image("img/night-mode.png");
        imagenModo.setImage(imagen);
    }
    
        // Método para cambiar a modo nocturno
    private void establecerModoNoche(){
        rootVistaPrincipal.getStylesheets().remove("styles/lightMode.css");
        rootVistaPrincipal.getStylesheets().add("styles/darkMode.css");
        Image imagen = new Image("img/light-mode.png");
        imagenModo.setImage(imagen);
    }
}
