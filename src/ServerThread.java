import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

    //each time server establishes connection with the client, we need to create independent thread,
    //so the server can continuously accept new client connections

    private int serverPort;
    private boolean isRunning;

    public ServerThread(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        isRunning = true;
        //we're creating server socket
        try {
            ServerSocket serverSocket = new ServerSocket(this.serverPort);
            System.err.println("Server Socket created! Server port is " + this.serverPort);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Polaczono z hostem. Wpisz komende lub wpisz <help>, aby wyswietlic liste komend");

                ClientThread clientThread = new ClientThread(clientSocket);
                clientThread.start();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
