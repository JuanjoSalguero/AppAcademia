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
import javafx.animation.TranslateTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javax.persistence.EntityManager;

/**
 *
 * @author
 */
public class VistaPrincipalController implements Initializable {

    private EntityManager em;
    private static boolean isLightMode = true;
    private static boolean configuracionDesplegada = false;

    @FXML
    private AnchorPane rootVistaPrincipal;
    
    @FXML
    private ImageView imagenModo;
    @FXML
    private HBox hBoxNuevoCurso;
    @FXML
    private HBox hBoxNuevaMatricula;
    @FXML
    private HBox hBoxVistaAlumno;
    @FXML
    private HBox hBoxAbout;
    @FXML
    private VBox configuracion;
    @FXML
    private ImageView botonConfiguracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        transiciones(1, 1.2);
    }

    // Setter
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
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

    @FXML
    private void setOnMouseClickedCurso(MouseEvent event) {
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
    private void setOnMouseClickedMatricula(MouseEvent event) {
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
    private void setOnMouseClickedAlumnos(MouseEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaAlumnos.fxml"));
            Parent rootVistaAlumnos = fxmlLoader.load();
            VistaAlumnosController vistaAlumnosController = (VistaAlumnosController) fxmlLoader.getController();
            vistaAlumnosController.setRootVistaPrincipal(rootVistaPrincipal);
            // Asociar objeto a la clase VistaMatriculaController
            vistaAlumnosController.setEntityManager(em);
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

    public void transiciones(double secComienzo, double secDuracion){
        TranslateTransition hBoxTransicion = new TranslateTransition(Duration.seconds(secDuracion), hBoxNuevoCurso);
        hBoxTransicion.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion.setToX(260);
        hBoxTransicion.setCycleCount(1);
        hBoxTransicion.play();
        
        TranslateTransition hBoxTransicion1 = new TranslateTransition(Duration.seconds(secDuracion), hBoxNuevaMatricula);
        hBoxTransicion1.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion1.setToX(-228);
        hBoxTransicion1.setCycleCount(1);
        hBoxTransicion1.play();
        
        TranslateTransition hBoxTransicion2 = new TranslateTransition(Duration.seconds(secDuracion), hBoxVistaAlumno);
        hBoxTransicion2.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion2.setToX(260);
        hBoxTransicion2.setCycleCount(1);
        hBoxTransicion2.play();
        
        TranslateTransition hBoxTransicion3 = new TranslateTransition(Duration.seconds(secDuracion), hBoxAbout);
        hBoxTransicion3.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion3.setToX(-228);
        hBoxTransicion3.setCycleCount(1);
        hBoxTransicion3.play();
    }
    
    public void reestablecerPosiciones(){
        hBoxAbout.setLayoutX(424);
    }

    @FXML
    private void hoverOut(MouseEvent event) {
        
    }

    @FXML
    private void hoverIn(MouseEvent event) {
        
        
    }

    @FXML
    private void desplegarConfiguracion(MouseEvent event) {
        if(!configuracionDesplegada){
            configuracionDesplegada = true;
            TranslateTransition imageTransicion = new TranslateTransition(Duration.seconds(1), botonConfiguracion);
            imageTransicion.setDelay(Duration.seconds(0));
            imageTransicion.setToX(60);
            imageTransicion.setCycleCount(1);
            imageTransicion.play();
            
            TranslateTransition vBoxTransicion = new TranslateTransition(Duration.seconds(1), configuracion);
            vBoxTransicion.setDelay(Duration.seconds(0));
            vBoxTransicion.setToX(60);
            vBoxTransicion.setCycleCount(1);
            vBoxTransicion.play();
        }
        else{
            configuracionDesplegada = false;
            TranslateTransition imageTransicion = new TranslateTransition(Duration.seconds(1), botonConfiguracion);
            imageTransicion.setDelay(Duration.seconds(0));
            imageTransicion.setToX(0);
            imageTransicion.setCycleCount(1);
            imageTransicion.play();
            
            TranslateTransition vBoxTransicion = new TranslateTransition(Duration.seconds(1), configuracion);
            vBoxTransicion.setDelay(Duration.seconds(0));
            vBoxTransicion.setToX(0);
            vBoxTransicion.setCycleCount(1);
            vBoxTransicion.play();
        }
    }
}
