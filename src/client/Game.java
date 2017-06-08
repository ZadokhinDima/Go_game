package client;


import javafx.concurrent.Task;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

import java.io.IOException;


import static client.Client.fromServer;
import static client.Client.input;
import static client.Client.out;

public class Game {


    Stage window;


    public final static int size = 19;

    private static Node[][] field;

    Image fieldImage, whiteImage, blackImage, whiteDeadImage, blackDeadImage;

    private StackPane root;

    private boolean white;

    private String enemyMove;


    public static int countWhite, countBlack;

    private Label whiteScore, blackScore;

    public static Node getNode(int x, int y) {
        try {
            return field[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Game(boolean white, Stage window) {
        //initializing user interface for the game
        this.window = window;

        AnchorPane outerPane = new AnchorPane();


        whiteScore = new Label("Білі: 0");
        blackScore = new Label("Чорні: 0");

        Button passButton = new Button("Пропустити");
        Button giveupButton = new Button("Здатись");


        //loading images;
        fieldImage = new Image("resources/gamefield.png");
        whiteImage = new Image("resources/white.png");
        blackImage = new Image("resources/black.png");
        whiteDeadImage = new Image("resources/white_eaten.png");
        blackDeadImage = new Image("resources/black_eaten.png");


        root = new StackPane();
        ImageView fieldView = new ImageView();
        fieldView.setImage(fieldImage);
        root.getChildren().add(fieldView);

        this.white = white;

        //filling field with empty nodes
        field = new Node[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int around = 4;
                if (i == 0 || i == size - 1)
                    around--;
                if (j == 0 || j == size - 1)
                    around--;
                field[i][j] = new Empty(i, j, true, around);
            }
        }

        AnchorPane.setLeftAnchor(passButton, 0.0);
        AnchorPane.setBottomAnchor(passButton, 0.0);
        AnchorPane.setRightAnchor(passButton, 200.0);
        AnchorPane.setTopAnchor(passButton, 440.0);

        AnchorPane.setLeftAnchor(giveupButton, 200.0);
        AnchorPane.setBottomAnchor(giveupButton, 0.0);
        AnchorPane.setRightAnchor(giveupButton, 0.0);
        AnchorPane.setTopAnchor(giveupButton, 440.0);

        AnchorPane.setLeftAnchor(whiteScore, 30.0);
        AnchorPane.setTopAnchor(whiteScore, 15.0);

        AnchorPane.setRightAnchor(blackScore, 30.0);
        AnchorPane.setTopAnchor(blackScore, 15.0);

        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 20.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setTopAnchor(root, 30.0);


        outerPane.getChildren().addAll(root, whiteScore, blackScore, passButton, giveupButton);


        window.setScene(new Scene(outerPane, 400, 480));

        //Initializing score
        countBlack = 0;
        countWhite = 0;


        //if player close program, he loses
        window.setOnCloseRequest(e -> {
            Notification.showNotification("Програш", "Ви здались, гра закінчилась");
            try {
                //if player gve up, all connection is closed and program ends
                input.close();
                out.println("giveup");
                out.close();
                fromServer.close();
                System.exit(0);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        root.setOnMouseClicked(e -> {
            //listener to catch user moves
            int x, y;


            //get coordinates of move
            x = ((int) e.getX() - 9) / 20;
            y = ((int) e.getY() - 23) / 20;

            //some thing with UI
            double screenx, screeny;

            screenx = window.getX() + 125;
            screeny = window.getY() + 29;

            //if the move is accepted, send it to server and wait for response
            if (doMove(x, y, white)) {
                out.println(x + " " + y);
                WaitingPane.waitForMove(screenx, screeny);
                //creating task for the new thread
                Task<Void> read = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        enemyMove = input.readLine();
                        return null;
                    }
                };

                read.setOnSucceeded(event -> {
                    //when get the response, unblock the user window and parse enemy move
                    WaitingPane.closeWindow();
                    getEnemyMove(enemyMove);
                });
                //creating new thread, which will wait response
                new Thread(read).start();
            }


        });

        //listener to "Give Up" button
        giveupButton.setOnAction(e -> {
            Notification.showNotification("Програш", "Ви здались, гра закінчилась");
            try {
                input.close();
                out.println("giveup");
                out.close();
                fromServer.close();
                System.exit(0);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        //listener to "Pass" button
        passButton.setOnAction(e -> {
            //Sending move to the server
            out.println("pass");

            //Waiting for response
            WaitingPane.waitForMove(window.getX() + 125, window.getY() + 29);
            Task<Void> read = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    enemyMove = input.readLine();
                    return null;
                }
            };

            read.setOnSucceeded(event -> {


                WaitingPane.closeWindow();
                getEnemyMove(enemyMove);


            });
            new Thread(read).start();


        });

        if (!white) {
            //Black player need to wait at the beginning
            WaitingPane.waitForMove(window.getX() + 125, window.getY() + 29);
            Task<Void> read = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    enemyMove = input.readLine();
                    return null;
                }
            };

            read.setOnSucceeded(e -> {


                WaitingPane.closeWindow();
                getEnemyMove(enemyMove);


            });
            new Thread(read).start();


        }

    }

    //Parsing enemy move
    public void getEnemyMove(String move) {

        //Other player closed connection
        if (move == null || move.equals("giveup")) {
            Notification.showNotification("Перемога", "Суперник здався");
            try {
                out.close();
                input.close();
                fromServer.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Server tells that the game is over
        if (move.equals("end")) {
            String result = "";
            if (countBlack > countWhite)
                result = "Перемогли чорні ";
            if (countBlack < countWhite)
                result = "Перемогли білі ";
            if (countWhite == countBlack)
                result = "Нічия ";

            result += "з рахунком " + countWhite + " - " + countBlack;

            //Notify the user about the end of the game, then close all connections
            Notification.showNotification("Гра завершена", result);
            try {
                out.close();
                input.close();
                fromServer.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        //If enemy has not passed, repeating his move on our desk
        if (!move.equals("pass")) {
            String[] xy = move.split(" ");

            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);

            doMove(x, y, !white);
        }

    }

    //Atempt to move
    public boolean doMove(int x, int y, boolean white) {

        //if out of desk
        if (x > 18 || y > 18)
            return false;

        //checking if game logic is not damaged
        if (field[x][y].isEnable() && ((Empty) field[x][y]).checkAvailable(white)) {
            //adding new rock to the desk
            field[x][y] = new Rock(x, y, white);
            //redraw all the information
            drawDesk();

            return true;
        } else
            return false;


    }

    //This method shows situation on the board to user
    public void drawDesk() {


        root.getChildren().clear();

        ImageView fieldView = new ImageView();
        fieldView.setImage(fieldImage);
        root.getChildren().add(fieldView);

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (!field[i][j].isEnable()) {


                    ImageView imageView = new ImageView();
                    if (!((Rock) field[i][j]).isWhite())
                        if (((Rock) field[i][j]).getGroup().isAlive()) {
                            imageView.setImage(blackImage);
                        } else {
                            imageView.setImage(blackDeadImage);
                        }
                    else if (((Rock) field[i][j]).getGroup().isAlive()) {
                        imageView.setImage(whiteImage);
                    } else {
                        imageView.setImage(whiteDeadImage);
                    }


                    StackPane.setAlignment(imageView, Pos.TOP_LEFT);
                    StackPane.setMargin(imageView, new Insets(23 + 20 * j, 0, 0, 9 + 20 * i));

                    root.getChildren().add(imageView);


                }
            }

            whiteScore.setText("Білі: " + countWhite);
            blackScore.setText("Чорні: " + countBlack);

    }


}
