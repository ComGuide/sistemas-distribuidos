# Sockets em Java

Este é um projeto de implementação de sockets UDP e TCP em Java, utilizando o livro texto COULOURIS, G., DOLLIMORE, J. e KINDBERG, T., Sistemas Distribuídos: Conceitos e Projetos (Chapter 4. Interprocess Communicaton) como referência.Neste repositório, temos exemplos de implementação de sockets UDP e TCP, e também uma implementação de uma calculadora que executa operações aritméticas simples (soma, subtração, multiplicação e divisão) utilizando sockets TCP. Os códigos fontes estão disponíveis no diretórios `udp`, `tcp` e `calc`. 

Para executar os códigos fontes, basta seguir os passos descritos neste documento.



# Sumário

- [Executando sockets UDP e TCP do livro texto COULOURIS](#1---executando-sockets-udp-e-tcp-do-livro-texto-coulouris-g-dollimore-j-e-kindberg-t-sistemas-distribuídos-conceitos-e-projetos-chapter-4-interprocess-communicaton1---executando-sockets-udp-e-tcp-do-livro-texto-coulouris-g-dollimore-j-e-kindberg-sistemas-distribu%C3%ADdos-conceitos-e-projetos-chapter-4-interprocess-communicaton)
	- [UDP](#udp)
		- [UDPClient.java](#udpclientjava)
		- [UDPServer.java](#udpserverjava)
		- [Executando o servidor UDP](#para-executar-o-servidor-udp-basta-executar-o-comando)
		- [Executando o cliente UDP](#para-executar-o-cliente-udp-basta-executar-o-comando-passando-como-argumento-a-mensagem-e-o-endereço-do-servidor)
	- [TCP](#tcp)
		- [TCPClient.java](#tcpclientjava)
		- [TCPServer.java](#tcpserverjava)
		- [Executando o servidor TCP](#para-executar-o-servidor-tcp-basta-executar-o-comando)
		- [Executando o cliente TCP](#para-executar-o-cliente-tcp-basta-executar-o-comando-passando-como-argumento-a-mensagem-e-o-endereço-do-servidor)
- [Implementação de uma calculadora utilizando sockets TCP](#2---como-implementar-uma-calculadora-que-executa-operações-aritméticas-simples-soma-subtração-multiplicação-e-divisão-utilizando-sockets-tcp)
	- [CALCClient.java](#calcclientjava)
	- [CALCServer.java](#calcserverjava)
	- [Executando o servidor da calculadora](#para-executar-o-servidor-da-calculadora-basta-executar-o-comando)
	- [Executando o cliente da calculadora](#para-executar-o-cliente-da-calculadora-basta-executar-o-comando-passando-como-argumento-a-mensagem-e-o-endereço-do-servidor)


 <!-- dê um espaço entre o # e o texto -->

# 1 - Executando sockets UDP e TCP do livro texto COULOURIS, G., DOLLIMORE, J. e KINDBERG, T., Sistemas Distribuídos: Conceitos e Projetos (Chapter 4. Interprocess Communicaton) 

## UDP
No diretório `udp` temos os seguintes arquivos:
- `UDPClient.java`: Cliente UDP
- `UDPServer.java`: Servidor UDP

Com o seguinte código fonte:

### UDPClient.java
```java
import java.net.*;
import java.io.*;

public class UDPClient{
    public static void main(String args[]){ 
		// args give message contents and destination hostname
		DatagramSocket aSocket = null;
        
		try {
			aSocket = new DatagramSocket();    
			byte [] m = args[0].getBytes();
			InetAddress aHost = InetAddress.getByName(args[1]);
			int serverPort = 6789;		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  args[0].length(), aHost, serverPort);
			aSocket.send(request);			                        
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			System.out.println("Reply: " + new String(reply.getData()));	
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}		      	
}
```
---
### UDPServer.java
```java
import java.net.*;
import java.io.*;

public class UDPServer{
    public static void main(String args[]){ 
    	DatagramSocket aSocket = null;
		try{
	    	aSocket = new DatagramSocket(6789);
					// create socket at agreed port
			byte[] buffer = new byte[1000];
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);     
    			DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
    				request.getAddress(), request.getPort());
    			aSocket.send(reply);
    		}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
    }
}
```

### Para executar o servidor UDP, basta executar o comando:
```bash
$ java UDPServer.java
```

### Para executar o cliente UDP, basta executar o comando passando como argumento a mensagem e o endereço do servidor:
```bash
$ java UDPClient.java "Hello World" localhost
```
A saída será:
```bash
Reply: Hello World
```

## TCP
No diretório `tcp` temos os seguintes arquivos:
- `TCPClient.java`: Cliente TCP
- `TCPServer.java`: Servidor TCP

Com o seguinte código fonte:

### TCPClient.java
```java
import java.net.*;
import java.io.*;

public class TCPClient {
	public static void main (String args[]) {
		// arguments supply message and hostname
		Socket s = null;
		try{
			int serverPort = 1234;
			s = new Socket(args[1], serverPort);    
			DataInputStream in = new DataInputStream( s.getInputStream());
			DataOutputStream out =new DataOutputStream( s.getOutputStream());
			out.writeUTF(args[0]);      	// UTF is a string encoding see Sn. 4.4
			String data = in.readUTF();	    // read a line of data from the stream
			System.out.println("Received: "+ data) ; 
		}catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
     }
}
```

### TCPServer.java
```java
import java.net.*;
import java.io.*;
public class TCPServer {
	public static void main (String args[]) {
		try{
			int serverPort = 1234; // the server port
			ServerSocket listenSocket = new ServerSocket(serverPort);
			while(true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;

	public Connection (Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}

	public void run(){
		try {			                 // an echo server

			String data = in.readUTF();	                  // read a line of data from the stream
			out.writeUTF(data);
		} catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		} catch(IOException e) {System.out.println("readline:"+e.getMessage());
		} finally { try {clientSocket.close();}catch (IOException e){/*close failed*/}}
	
	}
}
```

### Para executar o servidor TCP, basta executar o comando:
```bash
$ java TCPServer.java
```

### Para executar o cliente TCP, basta executar o comando passando como argumento a mensagem e o endereço do servidor:
```bash
$ java TCPClient.java "Hello World" localhost
```
A saída será:
```bash
Received: Hello World
```
---
# 2 - Como implementar uma calculadora que executa operações aritméticas simples (soma, subtração, multiplicação e divisão) utilizando sockets TCP

No diretório `CALC` temos os seguintes arquivos:
- `CALCClient.java`: Cliente TCP
- `CALCServer.java`: Servidor TCP

Com o seguinte código fonte:

### CALCClient.java
```java
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
```

### CALCServer.java
```java
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
```

## Para executar o servidor da calculadora, basta executar o comando:
```bash
$ java CALCServer.java
```

### Para executar o cliente da calculadora, basta executar o comando passando como argumento a mensagem e o endereço do servidor:
```bash
$ java CALCClient.java
```

Exemplos de entrada e saída:
```bash
$ 2 + 2
Client: Server: 4

$ 2 - 2
Client: Server: 0

$ 2 * 3
Client: Server: 6

$ 2 / 2
Client: Server: 1
```

---

