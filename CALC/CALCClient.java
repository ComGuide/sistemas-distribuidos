import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class CALCClient {
 
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        Scanner scanner = new Scanner(System.in);
        
        CALCServer clientThread = new CALCServer(socket);
        clientThread.start();
 
        while (true) {
            PrintStream outPut = new PrintStream(socket.getOutputStream());
            outPut.println(scanner.nextLine());
        }
 
    }
}
