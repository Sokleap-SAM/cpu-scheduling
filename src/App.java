import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a Label
        Label helloLabel = new Label("Hello, JavaFX!");

        // Create a Button
        Button changeTextButton = new Button("Click Me!");

        // Set the action for the button
        changeTextButton.setOnAction(event -> {
            helloLabel.setText("You clicked the button!");
        });

        // Create a layout pane (VBox) to hold the controls
        VBox root = new VBox(20); // 20 is the spacing between nodes
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(helloLabel, changeTextButton);

        // Create a Scene and set it on the Stage
        Scene scene = new Scene(root, 300, 200);

        // Set the title of the window
        primaryStage.setTitle("JavaFX Test App");

        // Set the Scene on the Stage and show the Stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}