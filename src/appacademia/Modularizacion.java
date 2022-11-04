/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appacademia;

import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author Juanjo
 */
public class Modularizacion {

    // Método para agregar los radiobuttons de TIPO en un ToggleGroup
    public static void TipoToggleGroup(ToggleGroup toggleGroup, RadioButton radioButton1, RadioButton radioButton2, RadioButton radioButton3){
        
        toggleGroup = new ToggleGroup();
        
        radioButton1.setToggleGroup(toggleGroup);
        radioButton2.setToggleGroup(toggleGroup);
        radioButton3.setToggleGroup(toggleGroup);
        
        radioButton1.setSelected(true);
    }
    
    // Método para limitar la fecha de DatePicker desde hoy hacia atrás
    public static void restringirDatepicker(DatePicker datePicker) {

        datePicker.setDayCellFactory((DatePicker picker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Si la fecha es anterior a la atual
                if (date.isBefore(LocalDate.now())) {
                    this.setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }
    
    // Método para restringir TextField a letras, espacio y tildes
    public static void soloLetras(TextField textField) {

        textField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[a-zA-Z\\sñÑáéíóúüÁÉÍÓÚ*]")) {
                    textField.setText(newValue.replaceAll("[^a-zA-Z\\sñÑáéíóúüÁÉÍÓÚ]", ""));
                }
            }
        });
    }

    // Método para restringir TextField a solo números con coma
    public static void soloNumerosYComa(TextField textField) {

        textField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*,.")) {
                    textField.setText(newValue.replaceAll("[^\\d.]" , ""));
                }
            }
        });
    }

    // Método para restringir TextField a solo números sin coma
    public static void soloNumeros(TextField textField) {

        textField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
