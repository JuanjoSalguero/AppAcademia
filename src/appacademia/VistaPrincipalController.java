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
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import javax.persistence.EntityManager;

/**
 *
 * @author
 */
public class VistaPrincipalController implements Initializable {

    private EntityManager em;
    private static boolean isLightMode = true;

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
            vistaCursoController.cambiarModo(isLightMode);
            
            // Ocultar la vista de la lista
            
            // Transición
            Scene scene = rootVistaPrincipal.getScene();
            rootVistaCurso.translateXProperty().set(scene.getWidth());

            //Añadir la vista Curso al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootVistaPrincipal.getScene().getRoot();
            rootMain.getChildren().add(rootVistaCurso);
            
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(rootVistaCurso.translateXProperty(), 0, Interpolator.EASE_BOTH);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(timelineEvent -> {
            rootVistaPrincipal.setVisible(false);
            });
            timeline.play();
            
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
            vistaMatriculaController.cambiarModo(isLightMode);

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
            vistaAlumnosController.rellenarComboBoxProvincia();
            vistaAlumnosController.cargarAlumnos();
            vistaAlumnosController.cambiarModo(isLightMode);

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
    @FXML
    public void cambiarModo(ActionEvent event){
        isLightMode = !isLightMode;
        
        if (isLightMode){
            Image imagen = new Image("img/night-mode.png");
            imagenModo.setImage(imagen);
        } else {
            Image imagen = new Image("img/light-mode.png");
            imagenModo.setImage(imagen);
        }
        Modularizacion.cambiarModo(rootVistaPrincipal, isLightMode);
    }
    
}
