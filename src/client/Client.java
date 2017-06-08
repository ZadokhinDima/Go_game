package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class Client extends Application {

    Stage window;


    static Game game;

    static Socket fromServer = null;

    static PrintWriter out = null;
    static BufferedReader input = null;


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        //Initializing start page
        AnchorPane root = new AnchorPane();

        Button startGame = new Button("Грати білими");
        Button joinGame = new Button("Грати чорними");

        AnchorPane.setLeftAnchor(startGame, 118.0);
        AnchorPane.setTopAnchor(startGame, 85.0);
        AnchorPane.setRightAnchor(startGame, 118.0);
        AnchorPane.setBottomAnchor(startGame, 234.0);
        AnchorPane.setLeftAnchor(joinGame, 118.0);
        AnchorPane.setTopAnchor(joinGame, 225.0);
        AnchorPane.setRightAnchor(joinGame, 118.0);
        AnchorPane.setBottomAnchor(joinGame, 94.0);

        root.getChildren().addAll(startGame, joinGame);

        Scene scene = new Scene(root, 400, 400);
        window.setScene(scene);
        window.setResizable(false);
        window.show();

        //adding listeners which  connect clients and server
        startGame.setOnAction(e -> firstConnect());
        joinGame.setOnAction(e -> secondConnect());

    }


    private void firstConnect() {

        try {

            //connect to server
            fromServer = new Socket("localhost", 4444);

            //initializing streams
            input = new BufferedReader(new InputStreamReader(fromServer.getInputStream()));
            out = new PrintWriter(fromServer.getOutputStream(), true);

            //waiting from server for game to start
            input.readLine();

            window.setTitle("Білий гравець");

            //initializing game
            game = new Game(true, window);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void secondConnect() {

        try {

            //connect to server

            fromServer = new Socket("localhost", 4445);

            //initializing streams
            out = new PrintWriter(fromServer.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(fromServer.getInputStream()));

            //waiting from server for game to start
            input.readLine();

            window.setTitle("Чорний гравець");

            //initializing game
            game = new Game(false, window);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);
    }

}
