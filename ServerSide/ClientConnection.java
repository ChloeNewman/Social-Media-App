import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection implements Runnable {

    private JabberDatabase jabberDatabase;
    private Socket socket;
    public String message;
    private static String username;

    public ClientConnection(Socket clientSocket, JabberDatabase jabberDatabase) {

        this.socket = clientSocket;
        this.jabberDatabase = jabberDatabase;

        //new thread
       new Thread(this).start();
    }

    public void run() {

        while (true) {
            try {
                //initialise ois as an object input stream for the clientSocket
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                //read the request
                JabberMessage request = (JabberMessage) ois.readObject();

                //read the object/turn into string
                String message = request.getMessage();
                System.out.println(message);

                if (message.contains("signin")) {
                    //get username from the ois
                    String[] messageArray = message.split(" ");

                    //set username
                    username = messageArray[1];
                    System.out.println(username);

                    //check whether username is in the database
                    //if not in the database message is "unknown-user" - oos
                    if (jabberDatabase.getUserID(messageArray[1]) == -1) {

                        //send response to the client
                        String response = "unknown-user";
                        JabberMessage jm = new JabberMessage(response);

                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(jm);
                            oos.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        String response = "signedin";
                        JabberMessage jm = new JabberMessage(response);

                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(jm);
                            oos.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (message.contains("register")) {
                    //split the message in half
                    String[] messageArray = message.split(" ");

                    try {
                        //add the username to the database
                        jabberDatabase.addUser(messageArray[1], messageArray[1] + "@gmail.com");
                        System.out.println("Registered user");

                        //if successful return message saying "signedin"
                        String response = "signedin";
                        JabberMessage jm = new JabberMessage(response);

                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(jm);
                            oos.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //get taken to timeline in the clientside

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Could not register user");
                    }

                } else if (message.contains("signout")) {
                    socket.close();
                    System.exit((0));

                } else if (message.contains("timeline")) {
                    //print the username
                    System.out.println(username);

                    //call method
                    ArrayList<ArrayList<String>> data = jabberDatabase.getTimelineOfUserEx(username);
                    System.out.println(jabberDatabase.getTimelineOfUserEx(username));

                    //return protocol - 'timeline' +  reply data: {data} DO THIS
                    String response = "timeline";
                    JabberMessage jm = new JabberMessage(response, data);

                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(jm);
                        oos.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.contains("users")) {
                    //call getUserID method and store in a variable
                    int numID = jabberDatabase.getUserID(username);

                    //call getUsersNotFollowed method
                    //process data
                    ArrayList<ArrayList<String>> data =  jabberDatabase.getUsersNotFollowed(numID);

                    //return protocol - 'users' + data {data}
                    String response = "users";
                    JabberMessage jm = new JabberMessage(response, data);

                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(jm);
                        oos.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.contains("post")) {
                    //post the jab
                    //get the jabtext by removing 'post ' from the message
                    String jabtext = message.replace("post ", "");

                    jabberDatabase.addJab(username, jabtext);

                    //return protocol - 'posted'
                    String response = "posted";
                    JabberMessage jm = new JabberMessage(response);

                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(jm);
                        oos.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.contains("like")) {
                    //remove 'like ' so the jabid remains and mae it an int
                    String jabidString = message.replace("like ", "");
                    int jabid = Integer.parseInt(jabidString);

                    //get the userID through the username
                    int userID = jabberDatabase.getUserID(username);

                    //like the jab
                    jabberDatabase.addLike(userID, jabid);

                    //return protocol - 'posted'
                    String response = "posted";
                    JabberMessage jm = new JabberMessage(response);

                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(jm);
                        oos.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.contains("follow")) {
                    //get the name of the person to follow
                    String followName = message.replace("follow ", "");

                    //get ID of followname
                    int followID = jabberDatabase.getUserID(followName);

                    //get the userID through the username
                    int userID = jabberDatabase.getUserID(username);

                    jabberDatabase.addFollower(userID, followID);

                    //return protocol - 'posted'
                    String response = "posted";
                    JabberMessage jm = new JabberMessage(response);

                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(jm);
                        oos.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }






}
