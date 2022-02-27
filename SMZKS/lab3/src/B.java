import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class B {

    private static final String N2 = String.valueOf((int) (Math.random() * 1000000));

    static void parseReplyFromAModifiedN2(String ks, String modifiedN2) {
        modifiedN2 = Scrambler.decrypt(ks, modifiedN2);
        String receivedN2 = new BigInteger(modifiedN2).subtract(BigInteger.ONE).toString();
        if(receivedN2.equals(N2)){
            System.out.println("Received N2 is equal to real N2");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("B is started!");
        boolean established = false;

        try (ServerSocket server = new ServerSocket(3346)) {

            Socket client = server.accept();

            System.out.print("Connection accepted.");

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            while (!client.isClosed()) {

                if (!established) {

                    System.out.println("Server reading from channel A");

                    String replyFromA3 = in.readUTF();

                    System.out.println("READ from A message - " + replyFromA3);

                    String decryptedReplyFromA3 = Scrambler.decrypt(Storage.KEY_B, replyFromA3);
                    String[] arrayReplyFromA3 = decryptedReplyFromA3.split(Storage.DELIMITER);
                    String ks = arrayReplyFromA3[0];
                    String idA = arrayReplyFromA3[1];
                    System.out.println("KS: " + ks);
                    System.out.println("ID_A: " + idA);

                    Storage.printLine();

                    System.out.println("Server B writing to channel");

                    out.writeUTF(Scrambler.encrypt(ks, N2));
                    System.out.println("B send - " + N2 + " to A - OK");

                    out.flush();

                    String replyFromAModifiedN2 = in.readUTF();

                    parseReplyFromAModifiedN2(ks, replyFromAModifiedN2);

                    established = true;

                } else {
                    System.out.println();
                    System.out.println("Connection established!");
                    System.out.println();

                    while (!client.isClosed()) {

                        System.out.println("B reading from channel");

                        String entry = in.readUTF();

                        System.out.println("READ from A message - " + entry);

                        if (entry.equalsIgnoreCase("quit")) {
                            System.out.println("A initialize connections suicide ...");
                            out.writeUTF("B reply - " + entry + " - OK");
                            out.flush();
                            Thread.sleep(3000);
                            break;
                        }

                        out.writeUTF("B reply - " + entry + " - OK");
                        System.out.println("B Wrote '" + entry + "'to A.");
                        out.flush();
                    }
                }
            }

            System.out.println("Client A disconnected");

            in.close();
            out.close();
            client.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
