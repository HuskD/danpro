import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{

    private String hostname;
    private int port = 0;

    public Client(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public Client(String hostname){
        this.hostname = hostname;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(this.port == 0){
            this.port = findHostPort();

        }else {
            Socket theSocket;
            Writer w;

            Reader r;

            theSocket = establishConnection(this.port);
            r = new Reader(theSocket);
            r.start();
            w = new Writer(theSocket);
            w.start();
        }


    }

    public int findHostPort(){
        int portFrom = 10000 + Main.myAppId + 1;
        int portTo = 15000;

        int connectionPort = portFrom;

        boolean success = false;

        do {
            while (connectionPort <= portTo && connectionPort >= portFrom) {
                try {
                    Socket tmp = new Socket(this.hostname, connectionPort);
                    System.out.println("Klient znalazl port! Jego numer to: " + connectionPort);
                    PrintWriter tmpOut = new PrintWriter(tmp.getOutputStream(), true);
                    tmpOut.println("portcheck");
                    tmpOut.close();
                    tmp.close();
                    success = true;
                    break;
                } catch (UnknownHostException e) {
                    System.err.println("Nieznany host!");
                    break;
                } catch (Exception e) {
                    System.err.println("Szukam portu...");
                    connectionPort++;
                }
            }
            System.err.println("Nie znaleziono portu. Ponawiam probe...");
        } while (!success);

        return connectionPort;
    }

    public Socket establishConnection(int thePort){
        boolean success = false;

        Socket theSocket = null;

        while (!success) {
            try {
                theSocket = new Socket(this.hostname, thePort);
                success = true;
            } catch (IOException e) {
                System.out.println("Problem z polaczeniem, szukam innego portu.");
                thePort = findHostPort();
            }
        }

        return theSocket;
    }

}
