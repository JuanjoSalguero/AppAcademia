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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author
 */
public class VistaPrincipalController implements Initializable {

    private EntityManagerFactory emf;
    private EntityManager em;

    @FXML
    private AnchorPane rootVistaPrincipal;

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

            // Ocultar la vista de la lista
            rootVistaPrincipal.setVisible(false);

            //Añadir la vista Matricula al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootVistaPrincipal.getScene().getRoot();
            rootMain.getChildren().add(rootVistaAlumnos);
        } catch (IOException ex) {
            Logger.getLogger(VistaAlumnosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
