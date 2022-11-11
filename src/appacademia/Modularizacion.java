/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appacademia;

import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Juanjo
 */
public class Modularizacion {

    static Alert confirmationAlert;

    // Método para agregar los radiobuttons de TIPO en un ToggleGroup
    public static void TipoToggleGroup(ToggleGroup toggleGroup, RadioButton radioButton1, RadioButton radioButton2, RadioButton radioButton3) {

        toggleGroup = new ToggleGroup();

        radioButton1.setToggleGroup(toggleGroup);
        radioButton2.setToggleGroup(toggleGroup);
        radioButton3.setToggleGroup(toggleGroup);

        radioButton1.setSelected(true);
    }

    // Método para limitar la fecha de DatePicker desde hoy hacia atrás
    public static void restringirDatepickers(DatePicker fechaInicio, DatePicker fechaFin) {

        fechaInicio.setDayCellFactory((DatePicker picker) -> new DateCell() {

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

        fechaFin.setDayCellFactory((DatePicker picker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate fInicio = fechaInicio.getValue();
                // Si la fecha es anterior a la atual
                if (date.isBefore(fInicio)) {
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
                if (!newValue.matches("[0-9.*]")) {
                    textField.setText(newValue.replaceAll("[^0-9.]", ""));
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

    // Sobrecarga del método anterior, pero en este caso se pasa por parametro spinner
    // Método para restringir TextField a solo números sin coma
    public static void soloNumeros(Spinner spinner) {

        spinner.getEditor().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    spinner.getEditor().setText(oldValue);
                }
            }
        });
    }

    // Método para confirmar que deseamos enviar los datos
    public static void confirmationTab(String tituloVentana) {
        if(tituloVentana.equals("Nueva Matrícula y Nuevo Alumno")){
            confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setTitle(tituloVentana + " - Confirmación de guardado de registro.");
            confirmationAlert.setContentText("¿Está seguro de que desea guardar el actual registro? Revise todos "
                    + "los campos antes de proceder con guardado del registro.");
        }
        else if(tituloVentana.equals("Nueva Matrícula y Actualizar Alumno")){
            confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setTitle(tituloVentana + " - Confirmación de guardado de registro y acualización de alumno.");
            confirmationAlert.setContentText("¿Está seguro de que desea guardar el actual registro? Revise todos "
                    + "los campos antes de proceder con guardado del registro.");
        }
        else if(tituloVentana.equals("Actualizar Matrícula")){
            confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setTitle(tituloVentana + " - Confirmación de acualización de matrícula.");
            confirmationAlert.setContentText("¿Está seguro de que desea guardar el actual registro? Revise todos "
                    + "los campos antes de proceder con guardado del registro.");
        }
    }

    // Método para combrobar que el dni introducido cumple los parametros(8 números y 1 letra)
    public static boolean comprobarDNI(String DNI) {
        boolean dniCorrecto = true;

        DNI = DNI.toUpperCase();

        if (DNI.length() != 9 || !Character.isLetter(DNI.charAt(8))) {
            return false;
        }

        int i = 0;

        while (i < 8 && dniCorrecto) {
            if (!Character.isDigit(DNI.charAt(i))) {
                dniCorrecto = false;
            }

            i++;

        }

        if (dniCorrecto) {
            if (!(DNI.charAt(8) >= 65 && DNI.charAt(8) <= 90)) {
                dniCorrecto = false;
            }
        }

        return dniCorrecto;
    }

    public static void cambiarModo(AnchorPane root, boolean isLightMode) {
        if (isLightMode) {
            Modularizacion.establecerModoDia(root);
        } else {
            Modularizacion.establecerModoNoche(root);
        }
    }

    public static void establecerModoDia(AnchorPane root) {
        root.getStylesheets().remove("styles/darkMode.css");
        root.getStylesheets().add("styles/lightMode.css");

    }

    // Método para cambiar a modo nocturno
    public static void establecerModoNoche(AnchorPane root) {
        root.getStylesheets().remove("styles/lightMode.css");
        root.getStylesheets().add("styles/darkMode.css");
    }

    // Método para limitar la longitud de los campos introducidos
    public static EventHandler<KeyEvent> longitudMaxima(int longitud) {
        return (KeyEvent event) -> {

            TextField textfield = (TextField) event.getSource();
            if (textfield.getText().length() >= longitud) {
                event.consume();
            }
        };

    //Metodo para comprobar que el DNI introducido es válido. Letra correcta
    public static boolean validarDNI(String DNI) {
        boolean dniValido;
        char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        
        String numerosDNI = DNI.substring(0, 7);
        
        int numero = Integer.parseInt(numerosDNI) % 23;
        
        if(letras[numero] == DNI.charAt(8))
            dniValido = true;
        
        else
            dniValido = false;
        
        return dniValido;

    }
    
    //  ***************************************** MÉTODOS PARA LIMPIAR *****************************************
    // Limpiar TextField
    public static void limpiarTextField(TextField textField) {
        textField.clear();
    }

    // Limpiar ComboBox
    public static void limpiarComboBox(ComboBox comboBox) {
        comboBox.valueProperty().set(null);
    }

    // DatePicker fecha actual
    public static void fechaActualDatePicker(DatePicker datePicker) {
        datePicker.setValue(LocalDate.now());
    }

    // Limpiar DatePicker
    public static void limpiarDatePicker(DatePicker datePicker) {
        datePicker.getEditor().clear();
    }

    // Limpiar Spinner
    public static void limpiarSpinner(Spinner spinner) {
        spinner.getValueFactory().setValue(0);
    }

    // Limpiar Radiobutton
    public static void limpiarRadioButton(RadioButton radioButton) {
        radioButton.setSelected(false);
    }

    // Marcar por defecto RadioButton
    public static void porDefectRadioButton(RadioButton radioButton) {
        radioButton.setSelected(true);
    }

    // Limpiar CheckBox
    public static void limpiarCheckBox(CheckBox checkBox) {
        checkBox.setSelected(false);
    }
    
    //  ***************************************** MÉTODOS PARA RESALTAR ERRORES *****************************************
    
    // ----------------------------- TEXTFIELD
    // Error de textField
    public static void errorTextField(TextField textField){
        textField.setStyle("-fx-background-color: #fcd7d4;");
    }
    // Resetear error
    public static void resetearError(TextField textField){
        textField.setStyle("-fx-background-color: rgba(180, 180, 180, 0.3);");
    }
    
    // ----------------------------- COMBOBOX
    // Error de comboBox
    public static void errorComboBox(ComboBox comboBox){
        comboBox.setStyle("-fx-background-color: #fcd7d4;");
    }
    // Resetear error
    public static void resetearError(ComboBox comboBox){
        comboBox.setStyle("-fx-background-color: rgba(180, 180, 180, 0.3);");
    }
    
    // ----------------------------- DATEPICKER
    // Error de textField
    public static void errorDatePicker(DatePicker datePicker){
        datePicker.setStyle("-fx-background-color: #fcd7d4;");
    }
    // Resetear error
    public static void resetearError(DatePicker datePicker){
        datePicker.setStyle("-fx-background-color: transparent;");
    }
    
    // ----------------------------- SPINNER
    // Error de textField
    public static void errorSpinner(Spinner spinner){
        spinner.setStyle("-fx-background-color: #fcd7d4;");
    }
    // Resetear error
    public static void resetearError(Spinner spinner){
        spinner.setStyle("-fx-background-color: transparent");
    }
}
