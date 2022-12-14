import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class TimelineController implements Initializable {

    LoginController lc = new LoginController();
    @FXML private TextField writeJab;
    @FXML private Button signOutBtn;
    @FXML private VBox jabberV;
    @FXML private VBox jabV;
    @FXML private VBox shrekV;
    @FXML private VBox likesV;
    @FXML private HBox timelineH;
    Image shrekLike;
    @FXML private VBox jabberWTF;
    @FXML private VBox jabberFollow;
    Image shrekFollow;
    Timer t = new Timer();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline();
        follow();
        TimerTing();
    }

    public void TimerTing() {

        //TIMER
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                  timeline();
                  follow();
            }
        }, 3000, 3000);
    }

    //Timeline method
    public void timeline() {

        //prepare message for server
        String message = "timeline";

        //send to server and wait for response
        JabberMessage response = null;
        try {
            response = lc.messaging(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //process the response
        ArrayList<ArrayList<String>> pregnantArrayList = response.getData();

        shrekLike = new Image("./shreklike.png");
        //ImageView shrekli = new ImageView(shrekLike);

        //set up the spacing
        Platform.runLater(() -> jabberV.setSpacing(23));
        Platform.runLater(() -> jabV.setSpacing(23));
        Platform.runLater(() -> shrekV.setSpacing(2));
        Platform.runLater(() -> likesV.setSpacing(23));

        //clear the boxes
        Platform.runLater(() -> jabberV.getChildren().clear());
        Platform.runLater(() -> jabV.getChildren().clear());
        Platform.runLater(() -> shrekV.getChildren().clear());
        Platform.runLater(() -> likesV.getChildren().clear());

        for (int i = 0; i < pregnantArrayList.size(); i++) {
            ArrayList<String> babyList = pregnantArrayList.get(i);

            Label jabberUser = new Label();
            jabberUser.setText(babyList.get(0));
            //System.out.println(jabberUser.getText());

            Label jabTexts = new Label();
            jabTexts.setText(babyList.get(1));
            //System.out.println(jabTexts.getText());

            Button shrekl = new Button("", new ImageView(shrekLike));
            shrekl.setMaxSize(10, 7);

            //action event ting
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event)  {
                    //send a message to the server to add like
                    //make the message
                    String message = "like " + babyList.get(2);

                    //call the messaging method
                    try {
                        JabberMessage response = lc.messaging(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };

            shrekl.setOnAction(event);

            Label likescount = new Label();
            likescount.setText(babyList.get(3));
            //System.out.println(likescount.getText());

            //add the values to the vboxes
            Platform.runLater(() -> jabberV.getChildren().add(jabberUser));
            Platform.runLater(() -> jabV.getChildren().add(jabTexts));
            Platform.runLater(() -> shrekV.getChildren().add(shrekl));
            Platform.runLater(() -> likesV.getChildren().add(likescount));
        }
    }

    //follow method
    public void follow() {
        Platform.runLater(() -> jabberWTF.getChildren().clear());
        Platform.runLater(() -> jabberFollow.getChildren().clear());

        String message2 = "users";

        //send to server and wait for response
        JabberMessage response2 = null;
        try {
            response2 = lc.messaging(message2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //process the response
        ArrayList<ArrayList<String>> pregnantArrayList2 = response2.getData();

        shrekFollow = new Image("./shrekfollow.png");

        Platform.runLater(() -> jabberWTF.setSpacing(23));
        Platform.runLater(() -> jabberFollow.setSpacing(2));

        for (int i = 0; i < pregnantArrayList2.size(); i++) {
            ArrayList<String> babyList2 = pregnantArrayList2.get(i);

            Label jabberToFollow = new Label();
            jabberToFollow.setText(babyList2.get(0));
            //System.out.println(jabberToFollow.getText());

            Button shrekf = new Button("", new ImageView(shrekFollow));
            //shrekf.setPrefSize(1, 1);

            //action event ting
            EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //send a message to the server to add follow
                    //make the message
                    String message = "follow " + babyList2.get(0);

                    //call the messaging method
                    try {
                        JabberMessage response = lc.messaging(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };

            shrekf.setOnAction(event2);

            Platform.runLater(() -> jabberWTF.getChildren().add(jabberToFollow));
            Platform.runLater(() -> jabberFollow.getChildren().add(shrekf));
        }
    }

    //method for posting a jab
    public void postJab(ActionEvent event) throws Exception {
        //make the message
        String message = "post " + writeJab.getText();

        //call the messaging method
        JabberMessage response = lc.messaging(message);
    }

    //sign out method
    public void signOut(ActionEvent event) throws Exception {

        ((Node) event.getSource()).getScene().getWindow().hide();

        //get text from textfield and send to server
        String message = "signout";

        lc.messaging(message);
    }
}
