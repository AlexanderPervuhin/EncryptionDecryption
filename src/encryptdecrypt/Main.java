package encryptdecrypt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class Main {


    public static void main(String[] args) {
        String mode = "enc";
        int key = 0;
        String data = "";
        String in = "";
        String out = "";
        String alg="unicode";
        EncryptDecrypt encryptDecrypt = new EncryptDecrypt();

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-mode":
                    mode = args[i + 1];

                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);

                    break;
                case "-data":
                    data = args[i + 1];
                    break;
                case "-in":
                    in = args[i + 1];
                    break;
                case "-out":
                    out = args[i + 1];
                    break;
                case "-alg":
                    alg = args[i + 1];
                    break;
            }
        }
        if (data.equals("")) {
            File inputFile = new File(in);
            try (Scanner scanner = new Scanner(inputFile)) {
                data = scanner.nextLine();

            } catch (FileNotFoundException e) {
                System.out.println(" No file found: " + in);
            }
        }

        File outputFile = new File(out);
        if (mode.equals("enc") || mode.equals("dec")) {
            data = encryptDecrypt.Process(data, mode, alg, key);
        } else
            System.out.println("Error: Unknown operation");


        if (out.equals("")) {
            System.out.println(data);
        } else try (FileWriter writer = new FileWriter(out)) {
            writer.write(data);
        } catch (IOException e) {
            System.out.printf("Error: %s", e.getMessage());
        }

    }

    static class EncryptDecrypt {

        ProcessingMethod algorithm;

        public ProcessingMethod setAlgorithm(String alg,String mode) {
            if (alg.equals("unicode")&&mode.equals("enc"))
                return this.algorithm=new EncodeUnicodeMethod();
            else if (alg.equals("unicode")&&mode.equals("dec"))
                return this.algorithm=new DecodeUnicodeMethod();
            else if  (alg.equals("shift")&&mode.equals("enc"))
                return this.algorithm=new EncodeShiftMethod();
            else if(alg.equals("shift")&&mode.equals("dec"))
                return this.algorithm=new DecodeShiftMethod();
            else
                throw new IllegalStateException("Unexpected value: " + alg);
        }



        public String Process(String data, String mode,String alg, int key) {

            this.setAlgorithm(alg,mode);
            return this.algorithm.ProcessData(data,key );

        }
    }
}


interface ProcessingMethod{

    String ProcessData(String data,int key);

}
class EncodeUnicodeMethod implements ProcessingMethod {


    @Override
    public String ProcessData(String data, int key) {


        StringBuilder builder = new StringBuilder();
        for (char item : data.toCharArray()) {

            builder.append((char) (item + key));


        }
        return builder.toString();
    }
}


class DecodeUnicodeMethod implements ProcessingMethod {


    @Override
    public String ProcessData(String data, int key) {


        StringBuilder builder = new StringBuilder();
        for (char item : data.toCharArray()) {

            builder.append((char) (item - key));


        }
        return builder.toString();
    }
}

class EncodeShiftMethod implements ProcessingMethod{

    @Override
    public  String ProcessData(String data, int key)
    {
        char a = 'a';
        char z = 'z';
        char A = 'A';
        char Z = 'Z';
        int size = 26;
        StringBuilder builder=new StringBuilder();
        char[] chars=data.toCharArray();
        for (char letter :chars) {
            if (letter >= a && letter <= z) {
                char shiftedLetter = (char) (((letter - a + key) % size) + a);
                builder.append((shiftedLetter));
            } else if (letter >= A && letter <= Z) {
                char shiftedLetter = (char) (((letter - A + key) % size) + A);
                builder.append((shiftedLetter));
            } else {
                builder.append(letter);
            }
        }
        return  builder.toString();
    }
}

class DecodeShiftMethod implements ProcessingMethod{

    @Override
    public  String ProcessData(String data, int key)
    {
        System.out.println((int)'e');
        System.out.println((int)'a');

        char a = 'a';
        char z = 'z';
        char A = 'A';
        char Z = 'Z';
        int size = 26;
        char shiftedLetter;

        StringBuilder builder=new StringBuilder();
        char[] chars=data.toCharArray();
        for (char letter :chars) {
            if (letter >= a && letter <= z) {
                shiftedLetter=(char)(z-(key-1-letter+a+size)%size);
                builder.append((shiftedLetter));
            } else if (letter >= A && letter <= Z) {
                shiftedLetter=(char)(Z-(key-1-letter+A+size)%size);
                builder.append((shiftedLetter));
            } else {
                builder.append(letter);
            }
        }
        return  builder.toString();
    }
}