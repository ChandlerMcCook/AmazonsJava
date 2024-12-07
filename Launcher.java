package Amazons;

import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;


public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) {
        Text title = new Text("Amazons Launcher");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 40));
        title.setFill(Color.RED);
        title.setStroke(Color.BLACK);

        Rectangle titleBackground = new Rectangle();
        titleBackground.setFill(Color.LIGHTBLUE);
        titleBackground.setArcWidth(20);
        titleBackground.setArcHeight(20);
        titleBackground.setWidth(380);
        titleBackground.setHeight(75);

        StackPane titlePane = new StackPane(titleBackground, title);
        Label body = new Label("Choose the version you'd like to play.");
        body.setFont(Font.font("Comic Sans MS", FontPosture.ITALIC, 20));
        body.setTextFill(Color.WHITE);

        Button local = new Button("Local Play");
        local.setFont(Font.font("Comic Sans MS", 15));
        local.setStyle("-fx-background-color: royalblue; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5");
        local.setTextFill(Color.BISQUE);

        Button multi = new Button("Online Play");
        multi.setFont(Font.font("Comic Sans MS", 15));
        multi.setStyle("-fx-background-color: royalblue; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5");
        multi.setTextFill(Color.BISQUE);

        HBox buttons = new HBox(40, local, multi);
        buttons.setAlignment(Pos.CENTER);

        VBox bottom = new VBox(20, body, buttons);
        bottom.setAlignment(Pos.CENTER);

        VBox root = new VBox(50, titlePane, bottom);
        root.setAlignment(Pos.CENTER);
        root.setTranslateY(80);

        VBox root1 = new VBox(root);
        root1.setStyle("-fx-background-color: darkgray");

        Scene scene = new Scene(root1, 400, 400);

        // Button Action for Local Play
        local.setOnAction(e -> {
            System.out.println("Launching Local Play...");
            Stage localStage = new Stage();
            LocalVersion localGui = new LocalVersion();
            try {
                localGui.start(localStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Button Action for Online Play
        multi.setOnAction(e -> {
            System.out.println("Launching Online Play...");
            Stage onlineStage = new Stage();
            OnlineVersion onlineGui = new OnlineVersion();
            try {
                onlineGui.start(onlineStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Amazons Launcher");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
