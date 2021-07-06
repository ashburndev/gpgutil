package gpgutil;

// import com.google.*;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStreamReader;

public class GpgUtil {

    public static final String DICKENS_QUOTE =
            "It was the best of times, it was the worst of times,\n" +
                    "it was the age of wisdom, it was the age of foolishness,\n" +
                    "it was the epoch of belief, it was the epoch of incredulity,\n" +
                    "it was the season of light, it was the season of darkness,\n" +
                    "it was the spring of hope, it was the winter of despair.";

    public static void main(String[] args) {
        GpgUtil gpgUtil = new GpgUtil();
        System.out.println("Hello GPG");
        System.out.println(DICKENS_QUOTE);
        byte [] plainBytes = DICKENS_QUOTE.getBytes();
        System.out.println(" plainBytes.length = " + plainBytes.length);
        byte [] encryptedBytes = gpgUtil.encrypt(plainBytes, "James Bond");
        System.out.println(" encryptedBytes.length = " + encryptedBytes.length);
    }

    byte[] encrypt(byte[] plain, String recipient) {
        //    String [] commandArray = {
        //            "gpg",
        //            "--no-tty",
        //            "--batch",
        //            "--yes",
        //            "--always-trust",
        //            "--recipient", recipient,
        //            "--encrypt"};
        //    System.out.println(commandArray);
        //    List <String> commandList = Arrays.asList(commandArray);
        //    System.out.println(commandList);
        List <String> commandList = List.of(
                "gpg",
                "--no-tty",
                "--batch",
                "--yes",
                "--always-trust",
                "--recipient", recipient,
                "--encrypt"
        );
        System.out.println(String.join(" ",commandList));
        try {
            //    ProcessBuilder pb = new ProcessBuilder(
            //            "gpg",
            //            "--no-tty",
            //            "--batch",
            //            "--yes",
            //            "--always-trust",
            //            "--recipient", recipient,
            //            "--encrypt");
            ProcessBuilder pb = new ProcessBuilder(commandList);
            System.out.println(pb.toString());
            Process p = pb.start();
            p.getOutputStream().write(plain);
            p.getOutputStream().flush();
            p.getOutputStream().close();
            int code = p.waitFor();
            if (code != 0) {
                String exceptionText = String.format("encryption failed with code %s: %s", code,
                        CharStreams.toString(new InputStreamReader(p.getErrorStream())));
                System.out.println (exceptionText);
                throw new RuntimeException(exceptionText);
            }
            return ByteStreams.toByteArray(p.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

/*
6:02:53 AM: Executing task 'GpgUtil.main()'...

> Task :compileJava
> Task :processResources NO-SOURCE
> Task :classes

> Task :GpgUtil.main()
Hello GPG
It was the best of times, it was the worst of times,
it was the age of wisdom, it was the age of foolishness,
it was the epoch of belief, it was the epoch of incredulity,
it was the season of light, it was the season of darkness,
it was the spring of hope, it was the winter of despair.
 plainBytes.length = 286
gpg --no-tty --batch --yes --always-trust --recipient James Bond --encrypt
java.lang.ProcessBuilder@5674cd4d
 encryptedBytes.length = 718

BUILD SUCCESSFUL in 688ms
2 actionable tasks: 2 executed
6:02:54 AM: Task execution finished 'GpgUtil.main()'.
 */
