import java.io.File;

public class FSum {

    private File file;
    private String checkSum;

    public FSum(File file, String checkSum) {
        this.file = file;
        this.checkSum = checkSum;
    }

    @Override
    public String toString() {
        return "Filename: " + file.getName() + ", MD5: " + checkSum;
    }

    public File getFile() {
        return file;
    }

    public String getCheckSum() {
        return checkSum;
    }

}
