import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ServerProtocol {

    private Socket socket;

    private static final int WAIT = 0;
    private static final String LIST_FILES = "1";

    private FileManager fm = new FileManager(Main.myFolderPath);
    private DataInputStream dis;
    private BufferedReader br;

    private int state = WAIT;


    public ServerProtocol(Socket socket){
        this.socket = socket;
    }


    public String processInput(String theInput){
        String theOutput = "wait";

        if(theInput != null) {
            if (theInput.trim().equals(LIST_FILES)) {
                theOutput = fm.listFiles();
            } else if( theInput.equals("2")) { //serwer otrzymal informacje by pobrac plik

                theOutput = "startpull";

            } else if(theInput.startsWith("pullfile")) { // info dla serwera, zeby wyslal plik

                theOutput = "startpush" + theInput.substring(9);

            }
        }else {
            theOutput = "Hi!";
        }

        return theOutput;
    }
}
