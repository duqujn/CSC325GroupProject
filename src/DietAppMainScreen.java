import com.google.firebase.auth.FirebaseAuth;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * MainScreen class to display the tableView of meals in the database as well as menuItems to quit
 * and menuItems to update the users profile
 */
public class DietAppMainScreen{
    private final TextField meal  = new TextField();
    private final TextField cal  = new TextField();
    private final TextField pro = new TextField();
    private final TextField carb = new TextField();
    private final TextField fat = new TextField();
    private final Label userName = new Label();
    private final Label goal = new Label();
    private final firebaseDBController db;
    private final String uID;
    private final DatePicker datePicker = new DatePicker();
    private final Stage stage;
    private final TableView<MealEntry> tableView = new TableView<>();

    public DietAppMainScreen(Stage stage, String uID) {
        this.stage = stage;
        this.uID = uID;
        this.db = new firebaseDBController(uID);
    }

    //public method to show the screen
    public void show() {
        //setting up the MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        MenuItem profEdit = new MenuItem("Profile");
        MenuItem exitMenuItem = new MenuItem("Exit");

        //MenuItems: Exit with exit the program
        //loadProfileScreen will pull up the profile screen class
        exitMenuItem.setOnAction(e -> System.exit(0));
        profEdit.setOnAction(e -> loadProfileScreen());

        //setting up the file menus and attaching them to the menu bar
        fileMenu.getItems().addAll(exitMenuItem);
        editMenu.getItems().addAll(profEdit);
        menuBar.getMenus().addAll(fileMenu, editMenu);

        //user profile image
        Image profileImage = new Image("/images/userIcon.jpg");
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(100);
        profileImageView.setFitWidth(100);
        VBox leftbox = new VBox(profileImageView);
        leftbox.setId("leftbox");

        //load profileData from the firestore
        db.loadProfileData(userName, goal);
        //set ID's for CSS Styles
        userName.setId("userName");
        goal.setId("goal");

        leftbox.getChildren().addAll(userName, goal);

        //setting up the TableView for displaying past meals.
        //Center column layout to display meals entered in rightbox
        TableColumn<MealEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateEntered"));

        TableColumn<MealEntry, String> mealCol = new TableColumn<>("Meal");
        mealCol.setCellValueFactory(new PropertyValueFactory<>("mealName"));

        TableColumn<MealEntry, String> calCol = new TableColumn<>("Calories");
        calCol.setCellValueFactory(new PropertyValueFactory<>("calories"));

        TableColumn<MealEntry, String> proteinCol = new TableColumn<>("Protein");
        proteinCol.setCellValueFactory(new PropertyValueFactory<>("protein"));

        TableColumn<MealEntry, String> carbCol = new TableColumn<>("Carbs");
        carbCol.setCellValueFactory(new PropertyValueFactory<>("carbs"));

        TableColumn<MealEntry,String> fatCol = new TableColumn<>("Fat");
        fatCol.setCellValueFactory(new PropertyValueFactory<>("fat"));

        //adding everything to the TableView
        tableView.getColumns().addAll(dateCol, mealCol, calCol, proteinCol, carbCol,fatCol);
        //load data from firestore and populate the tableView
        db.loadData(tableView);

        //setting up the right box with editable text fields that will populate the central TableView when save is clicked
        //ideally these will be saved in a database and will be loaded when the mainscreen is loaded

        VBox rightbox = new VBox();
        Label rightBoxLabel = new Label("Enter Meal Info");
        rightBoxLabel.setId("rightBoxLabel");
        rightbox.setId("rightbox");

        //setting promptText for fields initialized
        datePicker.setPromptText("Select a Date");
        meal.setPromptText("Enter Meal");
        cal.setPromptText("Enter Calories");
        pro.setPromptText("Enter Protein");
        carb.setPromptText("Enter Carbs");
        fat.setPromptText("Enter Fat");
        Button saveButton = new Button("Save");

        //setting up the save button functionality
        saveButton.setOnAction(event -> handleSave());
        //save button loads the data from the database after it is saved to the db
        db.loadData(tableView);
        //adding fields created to rightbox
        rightbox.getChildren().addAll(rightBoxLabel, datePicker, meal, cal, pro, carb,fat, saveButton);

        //setting up borderpane layout and attaching MenuBar/VBoxes/TableView
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setLeft(leftbox);
        root.setCenter(tableView);
        root.setRight(rightbox);

        //create the scene to display the MainAppScreen
        Scene scene = new Scene(root, 1100, 700);
        //connecting style.css
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Diet App Main Screen");
    }

    //private method to change the scene to the Profile Screen
    private void loadProfileScreen() {
        ProfileScreen profileScreen = new ProfileScreen(stage, uID);
        profileScreen.show();
    }

    /**
     * handleSave method to save the mealEntry to the database
     * to be called when the save button is clicked
     */
    private void handleSave(){
        try{
            //Validate data
            String name = meal.getText().trim();
            String caloriesStr = cal.getText().trim();
            String proteinStr = pro.getText().trim();
            String carbStr = carb.getText().trim();
            String fatsStr = fat.getText().trim();
            String id = UUID.randomUUID().toString();
            LocalDate datePicked = datePicker.getValue();
            //validate the date
            if(datePicked == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid date", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
                return;
            }
            String dateEntered = datePicked.format(DateTimeFormatter.ISO_LOCAL_DATE);
            //build mealEntry
            MealEntry entry = new MealEntry(id, dateEntered, name, caloriesStr, proteinStr, carbStr, fatsStr);
            //call the method from the dbController to save the data to the database
            db.addData(entry);

            tableView.getItems().add(entry);
            meal.clear();
            cal.clear();
            pro.clear();
            carb.clear();
            fat.clear();
            datePicker.setValue(null);

        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid data for calories, protein, carbs and fat",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }
}

