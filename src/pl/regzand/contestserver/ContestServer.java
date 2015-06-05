package pl.regzand.contestserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;


public class ContestServer extends Thread {
	
	protected final int port;
	protected boolean accepting = true;
	protected ServerSocket serverSocket;
	protected List<Client> clients;
	protected CommandsHandler cmdHandler;

	/**
	 * Create new ContestServer
	 * @param port - listening port
	 */
	public ContestServer(int port){
		this.port = port;
		this.clients = new ArrayList<Client>();
		this.cmdHandler = new CommandsHandler(this);
	}

	/**
	 * Run server thread - starts contest server
	 */
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
                onClientConnect(client);
			} catch (IOException e) {
				System.err.println("Could not accept connection on port " + port);
				e.printStackTrace();
			}
		}
	}

    /**
     * Function executed on new client connection
     * @param client - newly connected client
     */
    protected void onClientConnect(Client client){}

    /**
     * Function executed on client disconnect
     * @param client - disconnected client
     */
    protected void onClientDisconnect(Client client){}

	/**
	 * Cleanly closes the connection with client
	 * @param client
	 */
	public void closeConnetion(Client client){
		client.close();
		clients.remove(client);
        onClientDisconnect(client);
	}

	/**
	 * Returns port on which the server listens
	 * @return port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns whether the server accepts new connections with customers, or not
	 */
	public boolean isAccepting() {
		return accepting;
	}

	/**
	 * Set whether the server accepts new connections with customers, or not
	 * @param accepting - boolean
	 */
	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}

	/**
	 * Returns currently connected clients
	 * @return List of Clients
	 */
	public List<Client> getClients() {
		return clients;
	}

	/**
	 * Returns CommandHandler that handle commands on this server
	 * @return CommandsHandler
	 */
	public CommandsHandler getCommandsHandler() {
		return cmdHandler;
	}
	
}
