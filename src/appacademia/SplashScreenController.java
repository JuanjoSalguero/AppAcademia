/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package appacademia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;

/**
 * FXML Controller class
 *
 * @author Adrian
 */
public class SplashScreenController implements Initializable {

    private EntityManager em;
    StackPane rootMain = new StackPane();
    
    @FXML
    private AnchorPane rootPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SplashScreen().start();
        
    }    
    
    class SplashScreen extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
                        Pane rootAgendaView = null;
                        try {
                            rootAgendaView = fxmlLoader.load();
                        } catch (IOException ex) {
                            Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        rootMain.getChildren().add(rootAgendaView);

                        // Asocial objeto a la clase SplashScreenController
                        VistaPrincipalController vistaPrincipalController = (VistaPrincipalController) fxmlLoader.getController();
                        vistaPrincipalController.setEntityManager(em);


                        Scene scene = new Scene(rootMain, 1080, 800);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        rootPane.getScene().getWindow().hide();
                        
                    }
                
                });
                
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                
            }
             
        }
        
    }
    
    public void setEntityManager(EntityManager em) {
        this.em = em;
        
    }
    
}
