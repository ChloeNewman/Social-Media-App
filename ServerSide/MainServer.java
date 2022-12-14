import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer implements Runnable {

    //initialise the serverSocket and jabberDatabase to null; initialise the port number
    private static final int PORT_NUMBER = 44444;
    private static ServerSocket serverSocket = null;
    private static JabberDatabase jabberDatabase = null;
    public Thread serverThread;
    public static Socket clientSocket;

    public MainServer() {
       try {
           serverSocket = new ServerSocket(PORT_NUMBER);
           //serverSocket.setSoTimeout(1000);
           serverSocket.setReuseAddress(true);

           new Thread(this).start();

       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public static void main(String args[]) throws IOException {
        MainServer serverObject = new MainServer();
    }

    public void run() {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();

                    ClientConnection client = new ClientConnection(clientSocket, new JabberDatabase());
                    Thread.sleep(100);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("run end");
    }
}


