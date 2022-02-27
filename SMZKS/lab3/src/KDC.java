import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class KDC {

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(3345)) {

            Socket client = server.accept();

            System.out.print("Connection accepted.");

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream  created");

            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");

            Storage.printLine();

            while (!client.isClosed()) {

                System.out.println("Server reading from channel");

                String clientCommand1 = in.readUTF();

                System.out.println("READ from client message - " + clientCommand1);

                String ks = Scrambler.generateKs();
                System.out.println("Generated KS: " + ks);
                Storage.printLine();

                System.out.println("Server try writing to channel");

                String kdsCommand2 = getKdsCommand2(ks, clientCommand1);
//
//                Thread.sleep(2000);

                out.writeUTF(kdsCommand2);

                System.out.println("KDC -> A - " + kdsCommand2 + " - OK");
                System.out.println("Server Wrote message to client.");

                Storage.printLine();

                out.flush();

                break;
            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            in.close();
            out.close();
            client.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getKdsCommand2(String ks, String clientCommand1) {
        String[] array = clientCommand1.split(Storage.DELIMITER);
        String idA = array[0];
        String idB = array[1];
        String N1 = array[2];

        String messageForB = Scrambler.encrypt(Storage.KEY_B, String.join(Storage.DELIMITER, ks, idA));

        return Scrambler.encrypt(Storage.KEY_A, String.join(Storage.DELIMITER, ks, idB, N1, messageForB));
    }
}
