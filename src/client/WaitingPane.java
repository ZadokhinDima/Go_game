package client;




import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



/**
 * Created by dimaz on 04.06.2017.
 */
public class WaitingPane {

    //Tool for blocking the window until another player wikk make move


    static Stage window;

    public static void waitForMove(double x, double y){

        window = new Stage(StageStyle.UNDECORATED);
        window.setX(x);
        window.setY(y);

        StackPane pane = new StackPane();
        Label label = new Label("Чекаємо іншого гравця!");

        pane.getChildren().add(label);

        window.setScene(new Scene(pane, 150,40));

        window.initModality(Modality.APPLICATION_MODAL);
        window.show();


    }


    public static void closeWindow(){

        window.close();


    }
}
