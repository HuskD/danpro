import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader extends Thread {

    private Socket socket;
    private BufferedReader input;
    private DataInputStream dis;

    private FileManager fileManager = new FileManager(Main.myFolderPath);

    public Reader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
             input = new BufferedReader(
                    new InputStreamReader(this.socket.getInputStream()));
             dis = new DataInputStream(this.socket.getInputStream());


            String serverInput;

            while ((serverInput = input.readLine()) != null) {

                if(serverInput.equals("3")){ // 3 - serwer przesyla zadany plik, startpull

                    fileManager.pullFile(dis, input);

                }else {
                    if(!serverInput.equals("wait")) {
                        System.out.println("Server message: " + serverInput);
                    }
                }




            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStreams() throws IOException {
        this.input.close();
    }
}
