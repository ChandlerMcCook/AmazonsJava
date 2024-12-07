package Amazons;

// A Java program for a AmazonsServer
import java.net.*;
import java.io.*;

public class AmazonsServer
{
	//initialize socket and input stream
	private ServerSocket AmazonsServer = null;
	private Socket		 socket1 = null;
	private Socket		 socket2 = null;

	// constructor with port
	public AmazonsServer(int port)
	{
		// starts AmazonsServer and waits for a connection
		try
		{
			InetAddress bindAddress = InetAddress.getByName("10.80.10.191");
			AmazonsServer = new ServerSocket(port, 50, bindAddress);
			System.out.println("AmazonsServer started");

			System.out.println("Waiting for a client ...");

			socket1 = AmazonsServer.accept();
			System.out.println("Client 1 accepted");
			
			socket2 = AmazonsServer.accept();
			System.out.println("Client 2 accepted");
			
			// send who is what player
			DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
			out1.writeUTF("0");
			DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
			out2.writeUTF("1");
			
			DataInputStream in1 = new DataInputStream(socket1.getInputStream());
			DataInputStream in2 = new DataInputStream(socket2.getInputStream());
			
			int counter = 0;
			boolean whiteTurn = true;
			String message = "";
			while (!message.equals("WINNER")) {
				counter++;
				
				if (whiteTurn) {
					message = in1.readUTF();
					System.out.println("Received: "+message);
					
					out2.writeUTF(message);
					out2.flush();
				} else {
					message = in2.readUTF();
					System.out.println("Received: "+message);
					
					out1.writeUTF(message);
					out1.flush();
				}
				
				if (counter % 3 == 0) {
					whiteTurn = !whiteTurn;
				}
			}
			
			try {
				out1.close();
				out2.close();
			} catch (IOException i) {
				System.out.println(i);
			}
//			// start threads
//			Thread client1Thread = new ClientHandler(socket1, socket2);
//			Thread client2Thread = new ClientHandler(socket2, socket1);
//			
//			client1Thread.start();
//			client2Thread.start();
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		AmazonsServer AmazonsServer = new AmazonsServer(5555);
	}
}

class ClientHandler extends Thread {
	Socket CurrentSocket;
	Socket OtherSocket;
	DataInputStream in;
	DataOutputStream out;
	
	ClientHandler(Socket cSocket, Socket oSocket) {
		CurrentSocket = cSocket;
		OtherSocket = oSocket;
		
		try {
			in = new DataInputStream(CurrentSocket.getInputStream());
			out = new DataOutputStream(OtherSocket.getOutputStream());
		} catch (IOException i) {
			System.out.println(i);
		}
	}
	
	@Override
	public void run() {
		String message = "";
		try {
			while (message != "WINNER") {
				message = in.readUTF();
				System.out.println("Received: "+message);
				
				out.writeUTF(message);
				out.flush();
			}
		} catch (IOException e) {
            System.out.println("Connection closed: " + CurrentSocket.getInetAddress());
        } finally {
            try {
                CurrentSocket.close();
                OtherSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
}
