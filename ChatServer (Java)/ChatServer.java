import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final class ChatServer {
    private static int uniqueId = 0;
    private final List<ClientThread> clients = new ArrayList<>();
    private final int port;
    private final String file1;

    private ChatServer(int port, String file1) {
        this.port = port;
        this.file1 = file1;
    }

     //This is what starts the ChatServer.

    private void start() {
        ServerSocket serverSocket = null;
        Socket socket;
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            System.out.println("Banned Words File: " + file1 + ".txt");
            System.out.println("Banned Words: ");
            String line;
            while ((line = br.readLine())!= null) {
                System.out.println(line);
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        while (true) {
            try {
                socket = serverSocket.accept();
                ClientThread r = new ClientThread(socket, uniqueId++);
                Thread t = new Thread(r);
                clients.add(r);
                t.start();

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date now = new Date();
                String strDate = dateFormat.format(now);

                System.out.println(strDate + " " + clients.get(count).username + " has connected.");
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void broadcast(String message) throws IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String strDate = dateFormat.format(now);

        StringBuilder outputMessage = new StringBuilder();
        outputMessage.append(strDate);
        outputMessage.append(" ");
        outputMessage.append(message);

        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).writeMessage(outputMessage.toString());
        }
        System.out.println(outputMessage);
    }

    private synchronized void remove(int id){
        for(int i = 0; i < clients.size(); i++){
            if(clients.get(i).id == id) {
                clients.remove(i);
                break;
            }
        }
    }
    
    /*
     *  > java ChatServer
     *  > java ChatServer portNumber
     *  If the port number is not specified 1500 is used
     */
    public static void main(String[] args) {
        ChatServer server = null;
        if (args.length == 0) {
            server = new ChatServer(1500, "badwords.txt");
        } else if (args.length == 1) {
            server = new ChatServer(Integer.parseInt(args[0]), "badwords.txt");
        } else if (args.length == 2) {
            server = new ChatServer(Integer.parseInt(args[0]), args[1]);
        }

        server.start();
    }

    /*
     * This is a private class inside of the ChatServer
     * A new thread will be created to run this every time a new client connects.
     */
    private final class ClientThread implements Runnable {
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        String username;
        ChatMessage cm;

        private ClientThread(Socket socket, int id) {
            this.id = id;
            this.socket = socket;
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                username = (String) sInput.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private boolean writeMessage(String msg) throws IOException {
            if(socket.isConnected()){
                sOutput.writeObject(msg);
                return true;
            }
            else{
                return false;
            }
        }
        private synchronized void directMessage(String message, String username1) throws IOException {

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = dateFormat.format(now);

            String[] k = message.split(" ");
            String m = "";

            for (int i = 2; i < k.length; i++) {
                m += k[i] + " ";
            }

            for (int i = 0; i < clients.size(); i++) {
                if ((clients.get(i).username).equals(username1)) {
                    clients.get(i).writeMessage(strDate + " " + username + " --> " + username1 + ": "+ m);
                    writeMessage(strDate + " " + username + " --> " + username1 + ": "+ m);
                }
            }
            System.out.println(strDate + " " + username + " --> " + username1 + ": "+ m);
        }

        /*
         * This is what the client thread actually runs.
         */
        @Override
        public void run() {
            // Read the username sent to you by client
            ChatFilter file2 = new ChatFilter(file1);
            while (true) {
                try {
                    cm = (ChatMessage) sInput.readObject();
                    // check if user is logging out and broadcast message
                    cm.setMessage(file2.filter(cm.getMessage()));
                    String[] j = cm.getMessage().split(" ");
                    if (cm.getMessageType() == 1) {
                        remove(id);
                        broadcast(username + " disconnected with a LOGOUT message");
                    } else if (j[0].equals("/msg")) {
                        directMessage(cm.getMessage(), cm.getRecipient());
                    } else if (j[0].equals("/list")) {
                        String mm = "";
                        for (int i = 0; i < clients.size(); i++) {
                            if (!(clients.get(i).username).equals(username)) {
                                mm += clients.get(i).username + "\n";
                            }
                        }
                        writeMessage("Users: \n" + mm);
                        System.out.println(username + " asked for list of users.");
                    }
                    else {
                        broadcast(username + ": " + cm.getMessage());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date now = new Date();
                    String strDate = dateFormat.format(now);
                    System.out.println(strDate + " " + username + " disconnected with a LOGOUT message.");
                    break;
                }
            }
        }
    }
}