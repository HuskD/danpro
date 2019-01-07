import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;

public class Writer extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader reader;
    private DataInputStream dis;
    private DataOutputStream dos;


    private FileManager fileManager = new FileManager(Main.myFolderPath);

    public Writer(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        String userInput;

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(System.in));

            //obiekty operacji na plikach
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());

            String outPut;

            while ((userInput = reader.readLine()) != null) {

                outPut = processInput(userInput);

                if(outPut.equals("hlp")) {
                    out.println("...");
                }else if (outPut.equals("1")){
                    out.println(outPut);
                }else if (outPut.startsWith("pushfile")){
                    out.println("2"); // 2 - komunikat dla serwera, oczekuj nadchodzacego pliku

                    String fileName = outPut.substring(9);

                    fileManager.pushFile(fileName, dos, out);
                }else if (outPut.startsWith("pullfile")){
                    out.println(outPut); // 3 - komunikat dla serwera, ze ktos chce pobrac plik

                }

                out.println(userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeStreams() throws IOException {
        this.out.close();
        this.reader.close();
    }

    public String processInput(String input){
        String processedLine = null;

        if(input.equals("help")) { //wyswietlamy liste polecen
            String console;
            console = "Oto lista komend \n" +
                    "LISTFILES                                      wyswietl pliki polaczonego hosta wraz z sumami kontrolnymi \n" +
                    "PUSHFILE <nazwa pliku z rozszerzeniem>         wyslij plik na serwer \n" +
                    "PULLFILE <nazwa pliku z rozszerzeniem>         pobierz plik z serwera \n" +
                    "USEUDP                                         pracuj pod nadzorem udp \n";
            System.out.println(console);

            processedLine = "hlp";
        }else if(input.equals("listfiles")){
            processedLine = "1";
        }else if(input.startsWith("pushfile")){
            processedLine = input;
        }else if(input.startsWith("pullfile")){
            processedLine = input;
        }

        return processedLine;
    }


}
