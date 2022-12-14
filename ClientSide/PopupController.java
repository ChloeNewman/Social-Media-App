import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PopupController implements Initializable {
    @FXML private Button successAcknowledged;
    @FXML private Button noSuccessOK;

    @Override public void initialize(URL location, ResourceBundle resources) {

    }

    //method for successful login - this is for the popup window
    public void pressSuccessOK(ActionEvent event) {
        //hide the popup telling the user that login is successful
        ((Node)event.getSource()).getScene().getWindow().hide();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane timeline;

        try {
            timeline = loader.load(getClass().getResource("Timeline.fxml").openStream());
            Scene scene = new Scene(timeline);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            //go to the TimelineController
            TimelineController tlc = (TimelineController)loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method for unsuccessful login
    public void pressNoSuccessOK(ActionEvent event) {
        //hide the popup when ok is pressed
        ((Node)event.getSource()).getScene().getWindow().hide();

        //login screen should still be there
    }

    //method for successful registering
    public void pressRegSuccOK(ActionEvent event) {
        //hide window when ok is pressed
        ((Node)event.getSource()).getScene().getWindow().hide();

            //open the timeline
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane timeline;

        try {
            timeline = loader.load(getClass().getResource("Timeline.fxml").openStream());
            Scene scene = new Scene(timeline);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

             TimelineController tlc = (TimelineController)loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
