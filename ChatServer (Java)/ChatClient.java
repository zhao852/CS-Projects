import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

final class ChatClient {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;
    private final String server;
    private final String username;
    private final int port;

    private ChatClient(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    /*
     * This starts the Chat Client
     */
    private boolean start() throws IOException {
        // Create a socket
        try {
            socket = new Socket(server, port);
            System.out.println("Connection accepted " + socket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.out.println("Server can't connect");
        }

        // Create input and output streams
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException | NullPointerException e) {
            System.exit(0);
        }

        // This thread will listen from the server for incoming messages
        Runnable r = new ListenFromServer();
        Thread t = new Thread(r);
        t.start();

        // After starting, sends the clients username to the server.
        try {
            sOutput.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            ChatMessage cm = new ChatMessage(0, null, null);
            String[] l = input.split(" ");

            if (input.equals("/logout")) {
                logout();
                sendMessage(new ChatMessage(1, input, ""));
                break;
            } else if (input.contains("/msg")) {
                sendMessage(new ChatMessage(0, input, l[1]));
            } else {
                sendMessage(new ChatMessage(0, input,""));
            }
        }
        return true;
    }

    private void logout() throws IOException {
        sInput.close();
        sOutput.close();
        socket.close();
    }

    /*
     * This method is used to send a ChatMessage Objects to the server
     */
    private void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            System.out.println();
        }
    }

    /*
     * To start the Client use one of the following command
     * > java ChatClient
     * > java ChatClient username
     * > java ChatClient username portNumber
     * > java ChatClient username portNumber serverAddress
     *
     * If the portNumber is not specified 1500 should be used
     * If the serverAddress is not specified "localHost" should be used
     * If the username is not specified "Anonymous" should be used
     */
    public static void main(String[] args) throws IOException {
        // Get proper arguments and override defaults

        ChatClient client;

        //= new ChatClient(server, port, username);

        if (args.length == 0) {
            client = new ChatClient("localhost", 1500, "Anonymous");
        } else if (args.length == 1) {
            client = new ChatClient("localhost", 1500, args[0]);
        } else if (args.length == 2) {
            client = new ChatClient("localhost", Integer.parseInt(args[1]), args[0]);
        } else {
            client = new ChatClient(args[2], Integer.parseInt(args[1]), args[0]);
        }

        client.start();
    }

    /*
     * This is a private class inside of the ChatClient
     * It will be responsible for listening for messages from the ChatServer.
     * ie: When other clients send messages, the server will relay it to the client.
     */
    private final class ListenFromServer implements Runnable {
        public void run() {
            while (true) {
                try {
                    String msg = (String) sInput.readObject();
                    System.out.println(msg);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Server has closed the connection");
                    return;
                }
            }
        }
    }
}
