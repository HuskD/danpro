import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static String myFolderPath;
    public static int myAppId = 1;

    public static void main(String[] args) {
        helloUser();
        pickFolder();

        int clientPort = Integer.parseInt(args[0]);

        int serverPort = 10000 + myAppId;

        ServerThread serverThread = new ServerThread(serverPort);
        serverThread.start();

        Client client = new Client("localhost", clientPort);
        client.start();

    }

    private static void helloUser(){
        System.out.println("Witaj. Czy korzystałeś wcześniej z tej aplikajci? \n" +
                "1)Tak \n2)Nie");
        Scanner scan = new Scanner(System.in);
        int hello = scan.nextInt();
        int idUpdate = 0;
        switch (hello){
            case 1:
                System.out.println("Podaj ID, którego używałeś.");
                idUpdate = scan.nextInt();
                myAppId = idUpdate;
                break;
            case 2:
                System.out.println("Twoje id zostanie przydzielone automatycznie.");
                break;
            default:
                System.out.println("Podales niepoprawny komunikat. Id zostanie przydzielone automatycznie.");
                break;
        }
    }

    private static void pickFolder() {
        int appId = myAppId;
        String folderPath = null;
        Scanner scanner;

        boolean folderReady = false;
        boolean feedbackFlag = false;
        while (!folderReady) {
            folderPath = "TORrent_" + appId + "/";
            File folder = new File(folderPath);

            if (folder.exists()) {
                File[] list = folder.listFiles();

                if (folder.isDirectory() && list.length > 0) {

                    if(!feedbackFlag) {
                        System.out.println("Folder o nazwie " + folderPath + " juz istnieje. Czy na pewno chcesz go uzyc?");
                        System.out.println("1 - Tak, to mój folder" + "\n" +
                                "2 - Nie, utworz nowy folder lub wybierz pierwszy pusty");

                        scanner = new Scanner(System.in);
                        int whatToDo = scanner.nextInt();
                        if(whatToDo == 1){
                            folderReady = true;
                            System.out.println("Ok, twoj roboczy folder z plikami to: " + folderPath);
                        }else if(whatToDo == 2){
                            appId++;
                            feedbackFlag = true;
                        }else{
                            System.out.println("Podaj poprawny nr polecenia");
                        }
                    }else appId++;
                }else{
                    folderReady = true;
                    System.out.println("Folder z Twoimi plikami to: " + folderPath);
                    File ocucpyFile = new File(folderPath + "pusty_plik_rezerwujacy.txt");
                    try {
                        ocucpyFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                boolean success = new File(folderPath).mkdir();
                if(success){
                    System.out.println("Utworzono folder dla aplikacji. Twoj osobisty folder z plikami to: " + folderPath);
                }else{
                    System.out.println("Wystapil blad przy tworzeniu folderu aplikacji.");
                }
            }
        }
        myFolderPath = folderPath;
        myAppId = appId;


    }


}
