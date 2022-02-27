import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

public class A {

    private static final String N1 = String.valueOf((int) (Math.random() * 1000000));

    static String modifyN2(String N2) {
        return String.valueOf(new BigInteger(N2).add(BigInteger.ONE));
    }

    public static void main(String[] args) throws InterruptedException {

        try (Socket socket = new Socket("localhost", 3345);
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected to socket.");

            String replyFromKDC2 = "";
            while (!socket.isOutputShutdown()) {

                System.out.println("Client start writing in channel...");
                Storage.printLine();
                Thread.sleep(1000);
                String clientCommand1 = String.join(Storage.DELIMITER, Storage.ID_A, Storage.ID_B, N1);

                oos.writeUTF(clientCommand1);
                oos.flush();
                System.out.println("Client sent message " + clientCommand1 + " to server.");
                Thread.sleep(1000);

                System.out.println("Client sent message & start waiting for data from server...");
                Thread.sleep(2000);

                System.out.println("reading...");
                replyFromKDC2 = ois.readUTF();
                System.out.println("replyFromKDC2 = " + replyFromKDC2);
                Storage.printLine();

                break;
            }

            connectWithB(replyFromKDC2);

            System.out.println("Closing connections & channels on clientSide - DONE.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void connectWithB(String replyFromKDC) {
        String decryptedReplyFromKDS = Scrambler.decrypt(Storage.KEY_A, replyFromKDC);
        System.out.println("Decrypted reply from KDS: " + decryptedReplyFromKDS);
        String[] array = decryptedReplyFromKDS
                .split(Storage.DELIMITER);
        String ks = array[0];
        String idB = array[1];
        String N1FromKDC = array[2];
        if (N1.equals(N1FromKDC)) {
            System.out.println("Got N1 from KDS - " + N1FromKDC);
        } else {
            System.out.println("Expected: " + N1);
            System.out.println("Got: " + N1FromKDC);
            throw new RuntimeException("N1 from KDS is wrong!!!");
        }
        System.out.println("KS: " + ks);
        System.out.println("ID_B: " + idB);
        System.out.println("------------------------------------");
        String messageToB3 = array[3];

        boolean established = false;

        try (Socket socket = new Socket("localhost", 3346);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream())) {

            System.out.println("A connected to socket B.");

            while (!socket.isOutputShutdown()) {

                if (!established) {

                    System.out.println("A start writing in channel...");

                    oos.writeUTF(messageToB3);
                    oos.flush();
                    System.out.println("A sent message " + messageToB3 + " to B.");

                    System.out.println("A sent message & start waiting for data from server...");
                    Thread.sleep(2000);

                    System.out.println("reading B...");
                    String replyFromB4 = ois.readUTF();
                    String decryptedN2 = Scrambler.decrypt(ks, replyFromB4);
                    System.out.println("Decrypted N2 from B = " + decryptedN2);

                    String modifiedN2 = modifyN2(decryptedN2);
                    oos.writeUTF(Scrambler.encrypt(ks, modifiedN2));
                    oos.flush();

                    established = true;
                } else {
                    System.out.println();
                    System.out.println("Connection established!");
                    System.out.println();

                    while (!socket.isOutputShutdown()) {

                        if (br.ready()) {

                            Thread.sleep(1000);
                            String clientCommand = br.readLine();

                            oos.writeUTF(clientCommand);
                            oos.flush();
                            System.out.println("A sent message " + clientCommand + " to B.");
                            Thread.sleep(1000);

                            if (clientCommand.equalsIgnoreCase("quit")) {

                                System.out.println("A kill connections");
                                Thread.sleep(2000);

                                System.out.println("reading...");
                                String in = ois.readUTF();
                                System.out.println(in);
                                break;
                            }

                            System.out.println("Client sent message & start waiting for data from B...");
                            Thread.sleep(2000);

                            String in = ois.readUTF();
                            System.out.println(in);
                        }
                    }
                }
            }

            System.out.println("Closing connections & channels on clientSide - DONE.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
