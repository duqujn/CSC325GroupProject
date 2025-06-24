import javafx.application.Application;
import javafx.stage.Stage;

    //appLaucher class to set up a splashscreen for starting up the app
public class appLauncher extends Application {

    public void start(Stage primaryStage) {
        //call the splashscreen class constructor to show.
        SplashScreen splash = new SplashScreen(primaryStage);
        splash.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
