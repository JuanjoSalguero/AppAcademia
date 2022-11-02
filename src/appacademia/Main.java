/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appacademia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Ana
 */
public class Main extends Application {
    
    // Entity manager y StackPane como root de las vistas
    // private EntityManagerFactory emf;
    // private EntityManager em;
    StackPane rootMain = new StackPane();
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
        Pane rootAgendaView = fxmlLoader.load();
        rootMain.getChildren().add(rootAgendaView);
                
        Scene scene = new Scene(rootMain, 1080, 800);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
