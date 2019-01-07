import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ClientThread extends Thread {

    //class for establishing and maintaining connection between two sockets

    private Socket socket;
    private FileManager fileManager = new FileManager(Main.myFolderPath);


    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            DataInputStream dis = new DataInputStream(this.socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());


            ServerProtocol protocol = new ServerProtocol(this.socket);


            String ack = in.readLine();
            if(!ack.equals("portcheck")) {

                String outPutline, inputLine;
                outPutline = protocol.processInput(null);
                out.println(outPutline);

                while ((inputLine = in.readLine()) != null) {
                    outPutline = protocol.processInput(inputLine);

                    if(outPutline.equals("startpull")){

                        fileManager.pullFile(dis, in);

                    }else if(outPutline.startsWith("startpush")) {

                        out.println("3");

                        String wantedFileName = outPutline.substring(10);

                        fileManager.pushFile(wantedFileName,
                                dos, out);

                    }

                    out.println(outPutline);
                }

            }else {
                out.close();
                socket.close();
            }

            out.close();
            socket.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
