package memberQueue;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Server {
    private static final int PORT = 9090;
    private static HashSet<ObjectOutputStream> outputStreams = new HashSet<>();

    public static void main(String[] args) {
        System.out.println(new Date() + "\nServer online.\n");

        try (ServerSocket listener = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = listener.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException ioe) {}



    }



    private static class ClientHandler extends Thread {

        private Socket s;
        private ObjectOutputStream out;
        private static ArrayList receive = new ArrayList();
        private InputStream is;
        private ObjectInputStream ois;

        public ClientHandler(Socket socket) {
            this.s = socket;
        }

        @Override
        public void run() {
            try {

                out = new ObjectOutputStream(s.getOutputStream());
                is = s.getInputStream();
                ois = new ObjectInputStream(is);

                System.out.println("connected. IP: " + s.getInetAddress().getHostAddress()+ " Hostname: "+ s.getInetAddress().getHostName());

                outputStreams.add(out);

                if (!receive.isEmpty())
                out.writeObject(receive);

                while (true) {
                    try {
                        receive = (ArrayList<Member>) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("from client "+receive);

                    messageAll(receive);
                }
            } catch (IOException e) {
                outputStreams.remove(out);

            } finally {
                try {
                    s.close();
                } catch (IOException e) {}
            }
        }
    }

    private static void messageAll(ArrayList<Member> curList) throws IOException {
        if (!outputStreams.isEmpty()){
                for (ObjectOutputStream sender : outputStreams) {
                    sender.writeObject(curList);
                }
            }
        }

}