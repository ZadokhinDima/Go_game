package client;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by dimaz on 04.06.2017.
 */
public class Notification {


    //Tool for creating notifications to the user
    public static void showNotification(String tittle, String notification){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(tittle);
        Label label = new Label(notification);

        StackPane pane = new StackPane();
        pane.getChildren().addAll(label);

        window.setScene(new Scene(pane, 300, 50));

        window.showAndWait();


    }

}
