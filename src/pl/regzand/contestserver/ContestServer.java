package pl.regzand.contestserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;


public class ContestServer extends Thread {
	
	final int port;
	boolean accepting = true;
	ServerSocket serverSocket;
	List<Client> clients;
	CommandsHandler cmdHandler;

	public ContestServer(int port){
		this.port = port;
		this.clients = new ArrayList<Client>();
		this.cmdHandler = new CommandsHandler(this);
	}
	
	public void run() {
		
		// creating socket
		try {
			this.serverSocket = new ServerSocket(port);
			System.out.println("Listening for new connections on port "+port);
		} catch (IOException e1) {
			System.err.println("Could not create socket on port " + port);
			e1.printStackTrace();
			return;
		}
		
		// wait for connection
		while(accepting){
			try {
				// creating new thread for this specific client
				Client client = new Client(this, serverSocket.accept());
				client.start();
				clients.add(client);
				System.out.println("New client connected from "+client.socket.getRemoteSocketAddress().toString());
			} catch (IOException e) {
				System.err.println("Could not accept connection on port " + port);
				e.printStackTrace();
			}
		}
	}
	
	public void closeConnetion(Client client){
		client.close();
		clients.remove(client);
	}

	public int getPort() {
		return port;
	}

	public boolean isAccepting() {
		return accepting;
	}

	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}

	public List<Client> getClients() {
		return clients;
	}
	
	public CommandsHandler getCommandsHandler() {
		return cmdHandler;
	}
	
}
