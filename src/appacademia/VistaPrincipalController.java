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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.persistence.EntityManager;

/**
 *
 * @Kristian Johansson
 */
public class VistaPrincipalController implements Initializable {

    private EntityManager em;
    private static boolean isLightMode = true;
    private static boolean configuracionDesplegada = false;
    private static boolean aboutDesplegada = false;

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
    @FXML
    private ImageView icono;
    @FXML
    private VBox about;
    @FXML
    private ImageView iconoAbout;
    @FXML
    private ImageView logo;
    @FXML
    private ImageView cerrar;
    @FXML
    private ImageView minimizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        transiciones(.3, 1.2);
    }

    // Setter
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    // Método para cambiar el modo de la vista (noche o dia)
    @FXML
    public void cambiarModo(ActionEvent event) {
        isLightMode = !isLightMode;

        if (isLightMode) {
            Image imagen = new Image("img/night-mode.png");
            imagenModo.setImage(imagen);
            Image imagen1 = new Image("img/home.png");
            icono.setImage(imagen1);
            Image imagen2 = new Image("img/info1.png");
            iconoAbout.setImage(imagen2);
            Image imagen3 = new Image("img/winball-black.png");
            logo.setImage(imagen3);
            Image imagen4 = new Image("img/letra-x.png");
            cerrar.setImage(imagen4);
            Image imagen5 = new Image("img/minimizar.png");
            minimizar.setImage(imagen5);
        } else {
            Image imagen = new Image("img/light-mode.png");
            imagenModo.setImage(imagen);
            Image imagen1 = new Image("img/home1.png");
            icono.setImage(imagen1);
            Image imagen2 = new Image("img/informacion.png");
            iconoAbout.setImage(imagen2);
            Image imagen3 = new Image("img/winball-white.png");
            logo.setImage(imagen3);
            Image imagen4 = new Image("img/letra-x1.png");
            cerrar.setImage(imagen4);

            Image imagen5 = new Image("img/minimizar1.png");
            minimizar.setImage(imagen5);
           
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
            cerrarConfiguracion(configuracionDesplegada);

            vistaCursoController.setIsLightMode(isLightMode);
           

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
            vistaMatriculaController.setIsLightMode(isLightMode);
            cerrarConfiguracion(configuracionDesplegada);
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
            vistaAlumnosController.rellenarComboBoxProvincia();
            vistaAlumnosController.cargarAlumnos();
            vistaAlumnosController.rellenarComboBoxProvincia();
            vistaAlumnosController.cambiarModo(isLightMode);
            vistaAlumnosController.setIsLightMode(isLightMode);
            cerrarConfiguracion(configuracionDesplegada);
            // Ocultar la vista de la lista
            rootVistaPrincipal.setVisible(false);

            //Añadir la vista Matricula al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootVistaPrincipal.getScene().getRoot();
            rootMain.getChildren().add(rootVistaAlumnos);
        } catch (IOException ex) {
            Logger.getLogger(VistaAlumnosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void transiciones(double secComienzo, double secDuracion) {
        TranslateTransition hBoxTransicion = new TranslateTransition(Duration.seconds(secDuracion), hBoxNuevoCurso);
        hBoxTransicion.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion.setToX(260);
        hBoxTransicion.setCycleCount(1);
        hBoxTransicion.play();

        TranslateTransition hBoxTransicion1 = new TranslateTransition(Duration.seconds(secDuracion), hBoxNuevaMatricula);
        hBoxTransicion1.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion1.setToX(-229);
        hBoxTransicion1.setCycleCount(1);
        hBoxTransicion1.play();

        TranslateTransition hBoxTransicion2 = new TranslateTransition(Duration.seconds(secDuracion), hBoxVistaAlumno);
        hBoxTransicion2.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion2.setToX(260);
        hBoxTransicion2.setCycleCount(1);
        hBoxTransicion2.play();

        TranslateTransition hBoxTransicion3 = new TranslateTransition(Duration.seconds(secDuracion), hBoxAbout);
        hBoxTransicion3.setDelay(Duration.seconds(secComienzo));
        hBoxTransicion3.setToX(-229);
        hBoxTransicion3.setCycleCount(1);
        hBoxTransicion3.play();
    }

    @FXML
    private void hoverOut(MouseEvent event) {
    }

    @FXML
    private void hoverIn(MouseEvent event) {

        hBoxNuevoCurso.setCursor(Cursor.HAND);
        hBoxNuevaMatricula.setCursor(Cursor.HAND);
        hBoxVistaAlumno.setCursor(Cursor.HAND);
        hBoxAbout.setCursor(Cursor.HAND);
        cerrar.setCursor(Cursor.HAND);
        botonConfiguracion.setCursor(Cursor.HAND);
    }

    @FXML
    private void desplegarConfiguracion(MouseEvent event) {
        if (!configuracionDesplegada) {
            configuracionDesplegada = true;
//            TranslateTransition imageTransicion = new TranslateTransition(Duration.seconds(.4), botonConfiguracion);
//            imageTransicion.setDelay(Duration.seconds(0));
//            imageTransicion.setToX(60);
//            imageTransicion.setCycleCount(1);
//            imageTransicion.play();

            TranslateTransition vBoxTransicion = new TranslateTransition(Duration.seconds(.4), configuracion);
            vBoxTransicion.setDelay(Duration.seconds(0));
            vBoxTransicion.setToX(60);
            vBoxTransicion.setCycleCount(1);
            vBoxTransicion.play();
        } else {
            configuracionDesplegada = false;
//            TranslateTransition imageTransicion = new TranslateTransition(Duration.seconds(.4), botonConfiguracion);
//            imageTransicion.setDelay(Duration.seconds(0));
//            imageTransicion.setToX(0);
//            imageTransicion.setCycleCount(1);
//            imageTransicion.play();

            TranslateTransition vBoxTransicion = new TranslateTransition(Duration.seconds(.4), configuracion);
            vBoxTransicion.setDelay(Duration.seconds(0));
            vBoxTransicion.setToX(0);
            vBoxTransicion.setCycleCount(1);
            vBoxTransicion.play();
        }
    }

    private void cerrarConfiguracion(boolean abierto) {
        if (abierto) {
            TranslateTransition vBoxTransicion = new TranslateTransition(Duration.seconds(.4), configuracion);
            vBoxTransicion.setDelay(Duration.seconds(0));
            vBoxTransicion.setToX(0);
            vBoxTransicion.setCycleCount(1);
            vBoxTransicion.play();
        }
    }

    @FXML
    private void aboutTab(MouseEvent event) {
        if (!aboutDesplegada) {
            aboutDesplegada = true;

            TranslateTransition vBoxTransicion = new TranslateTransition(Duration.seconds(1), about);
            vBoxTransicion.setDelay(Duration.seconds(0));
            vBoxTransicion.setToY(740);
            vBoxTransicion.setCycleCount(1);
            vBoxTransicion.play();
        } else {
            aboutDesplegada = false;

            TranslateTransition vBoxTransicion = new TranslateTransition(Duration.seconds(1), about);
            vBoxTransicion.setDelay(Duration.seconds(0));
            vBoxTransicion.setToY(0);
            vBoxTransicion.setCycleCount(1);
            vBoxTransicion.play();
        }
        cerrarConfiguracion(configuracionDesplegada);

    }

    @FXML
    private void onClickCerrar(MouseEvent event) {
        Stage stage = (Stage) cerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizar(MouseEvent event) {
        ((Stage)((ImageView)event.getSource()).getScene().getWindow()).setIconified(true);
    }
}
