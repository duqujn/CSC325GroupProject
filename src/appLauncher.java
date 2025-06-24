import javafx.application.Application;
import javafx.stage.Stage;


public class appLauncher extends Application {

    public void start(Stage primaryStage) {
        SplashScreen splash = new SplashScreen(primaryStage);
        splash.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
