import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CALCServer extends Thread {
    
    private Socket socket;

    public CALCServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String message;

            while ( (message = reader.readLine()) != null) {
                System.out.println("Client: " + message);
            }
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Socket socket = serverSocket.accept();
        System.out.println("Client connected...");

        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        PrintStream outPut = new PrintStream(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String message;

        
        
        while ( (message = reader.readLine()) != null) {
            ArrayList<String> list = new ArrayList<String>();
            for (String s : message.split((" "))) {
                list.add(s);
            }

            if (list.get(1).equals("+")) {
                int result = Integer.parseInt(list.get(0)) + Integer.parseInt(list.get(2));
                outPut.println("Server: " + result);
            }
            else if (list.get(1).equals("-")) {
                int result = Integer.parseInt(list.get(0)) - Integer.parseInt(list.get(2));
                outPut.println("Server: " + result);
            }
            else if (list.get(1).equals("*")) {
                int result = Integer.parseInt(list.get(0)) * Integer.parseInt(list.get(2));
                outPut.println("Server: " + result);
            }
            else if (list.get(1).equals("/")) {
                int result = Integer.parseInt(list.get(0)) / Integer.parseInt(list.get(2));
                outPut.println("Server: " + result);
            }
            else {
                outPut.println("Server: Invalid input");
            }
        }
    }
}
