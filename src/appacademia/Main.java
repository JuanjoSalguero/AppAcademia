/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appacademia;

import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Winball  
 */
public class Main extends Application {
    
    //Entity manager y StackPane como root de las vistas
    private EntityManagerFactory emf;
    private EntityManager em;
    StackPane rootMain = new StackPane();
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
        Pane rootAgendaView = fxmlLoader.load();
        rootMain.getChildren().add(rootAgendaView);
        
        emf = Persistence.createEntityManagerFactory("AppAcademiaPU");
        em = emf.createEntityManager();
        
        // Asocial objeto a la clase VistaPrincipalController
        VistaPrincipalController vistaPrincipalController = (VistaPrincipalController) fxmlLoader.getController();
        vistaPrincipalController.setEntityManager(em);

                
        Scene scene = new Scene(rootMain, 1080, 800);
        
        stage.setScene(scene);
        stage.show();
    }
    
    // Método para cerrar la conexión
    @Override
    public void stop() throws Exception {
        // Al acabar la conexión cerramos tanto el entity manager como el EM factory
        em.close();
        emf.close();
        try{
            DriverManager.getConnection("jdbc:derby:C:\\Users\\usuario\\Documents\\NetBeansProjects\\appacademia-winball\\BDAcademia;create=true");
        } catch(SQLException ex) {
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
