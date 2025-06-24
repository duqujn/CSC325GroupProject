import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.*;
import javafx.animation.*;
import javafx.scene.control.*;

public class DietAppMainScreen {

    private Stage stage;

    public DietAppMainScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu newMenu = new Menu("New");
        MenuItem dietEdit = new MenuItem("Diet");

        MenuItem FileNew = new MenuItem("New");
        MenuItem exitMenuItem = new MenuItem("Exit");

        fileMenu.getItems().addAll(exitMenuItem, FileNew);
        editMenu.getItems().addAll(dietEdit);

        menuBar.getMenus().addAll(fileMenu, editMenu, newMenu);

        //profile screen with target weight and time frame, which can be displayed in the main screen
        VBox textBoxFields  = new VBox(10);

        TextField cal  = new TextField();
        cal.setPromptText("Enter Calories");
        TextField date  = new TextField();
        date.setPromptText("Enter Date");
        TextField weight = new TextField();
        weight.setPromptText("Enter Weight");
        TextField goalWeight = new TextField();


        textBoxFields.getChildren().addAll(cal, date, weight, goalWeight);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(textBoxFields);


        Scene scene = new Scene(root, 900, 600);


        stage.setScene(scene);
        stage.setTitle("Diet App Main Screen");


    }
}
