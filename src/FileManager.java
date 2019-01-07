import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class FileManager {

    private String directory;
    private HashMap<Integer, FSum> filesMap;

    public FileManager(String directory) {
        this.directory = directory;
        try {
            fetchFilesMap();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void pullFile(DataInputStream dis, BufferedReader input){
        String recName, checkSum;

        try {
            recName = input.readLine();
            checkSum = input.readLine();


            int length = dis.readInt();

            File received = new File(this.directory + "downloaded_" + recName);
            FileOutputStream fos = new FileOutputStream(received);

            if(length > 0 ) {
                byte[] fileMessage = new byte[length];
                dis.readFully(fileMessage);
                fos.write(fileMessage);
            }

            String sum = generateChecksum(received);
            if(sum.equals(checkSum)){
                System.out.println("Pobrano wlasciwy plik.");
            }else {
                System.out.println("Suma kontrolna sie nie zgadza.");
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    public void pushFile(String fileName, DataOutputStream dos, PrintWriter out){
        FSum fSum = null;
        for(int i = 0; i < this.filesMap.size(); i++) {
            if(this.filesMap.get(i).getFile().getName().equals(fileName)){
                fSum = this.filesMap.get(i);
            }
        }

        if (fSum == null){
            out.println("Nie mam takiego pliku.");
        }else {
            out.println(fileName);

            System.err.println("Plik do wyslania to" + fSum.getFile().getName());

            out.println(fSum.getCheckSum());

            try {
                byte[] pack = getFileBytes(fSum.getFile());

                dos.writeInt(pack.length); // wysylam 'rozmiar' pliku
                dos.write(pack);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }






    }

    public String listFiles(){
        String filesLine = "";

        if(this.filesMap == null){
            filesLine = "no files available";
        }else {
            for(int i = 0; i < this.filesMap.size(); i++) {
                filesLine = filesLine + this.filesMap.get(i) + "\n";
            }
        }

        return filesLine;
    }

    public void fetchFilesMap() throws IOException, NoSuchAlgorithmException {
        this.filesMap = new HashMap<>();

        File dir = new File(this.directory);

        File[] files = dir.listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i].isFile()){
                this.filesMap.put(i,new FSum(files[i], generateChecksum(files[i])));
            }
        }
    }

    public String generateChecksum(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] fileBytes = getFileBytes(file);

        md.update(fileBytes);
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for(byte b1 : b){
            sb.append(Integer.toHexString(b1 & 0xff));
        }

        return sb.toString();
    }

    public byte[] getFileBytes(File f) throws IOException {
        Path filePath = f.toPath();

        return Files.readAllBytes(filePath);

    }




}
