import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Label logIn;
    @FXML private TextField username;
    @FXML private Button signIn;
    @FXML private Button register;

    private static Socket socket;
    private static final int PORT = 44444;

    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public JabberMessage messaging(String message) throws IOException, ClassNotFoundException {
        //initialise the socket
        socket = new Socket("localhost", PORT);

        //get message and send to server
        String request = message;
        JabberMessage jm = new JabberMessage(request);

        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(jm);
        oos.flush();

        if (request.contains("signout")) {
            System.exit((0));
        }

        //server response: receive the response
        ois = new ObjectInputStream(socket.getInputStream());

        //read the request
        JabberMessage response = (JabberMessage) ois.readObject();

        return response;
    }

    public void login (ActionEvent event) throws Exception {

        try {
            //make the message
            String message = "signin " + username.getText();

            //call the messaging method
            JabberMessage response = messaging(message);

            //read the object/turn into string
            String processResponse = response.getMessage();

            //if the username is in the database (boolean - another method)
            if (processResponse.equals("signedin")) {
                //hide login page
                ((Node) event.getSource()).getScene().getWindow().hide();

                //popup login successful
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane successfulPopup = loader.load(getClass().getResource("LoginSuccessful.fxml").openStream());

                PopupController pc1 = (PopupController) loader.getController();
                Scene scene = new Scene(successfulPopup);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

                //remove login page and go to timeline in PopupController when OK is pressed

            } else if (processResponse.equals("unknown-user")) {
                //popup login unsuccessful
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane unsuccessfulPopup = loader.load(getClass().getResource("LoginUnsuccessful.fxml").openStream());

                PopupController pc2 = (PopupController) loader.getController();
                Scene scene = new Scene(unsuccessfulPopup);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

                //when ok is pressed close the popup (in PopupController)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signup (ActionEvent event) throws Exception {
        //get text from textfield and send to server
        String request = "register " + username.getText();
        JabberMessage jm = new JabberMessage(request);

          try {
              ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
              oos.writeObject(jm);
              oos.flush();

          } catch (Exception e) {
              e.printStackTrace();
          }

            //SERVER SIDE - add it to username + return message: signedin
            //server response
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //read the request
            JabberMessage response = (JabberMessage) ois.readObject();

            //read the object/turn into string
            String message = response.getMessage();
            System.out.println(message);

            //if we receive message "signedin" from server do this
            if (message.equals("signedin")) {

                //hide the pane
                ((Node) event.getSource()).getScene().getWindow().hide();

                //registering is successful
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane regSuccessPopup = loader.load(getClass().getResource("RegisterSuccessful.fxml").openStream());

                PopupController pc3 = (PopupController) loader.getController();
                Scene scene = new Scene(regSuccessPopup);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

                //once ok is clicked on the popup get taken to timeline
            }
    }
}



























