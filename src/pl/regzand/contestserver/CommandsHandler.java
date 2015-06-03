package pl.regzand.contestserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandsHandler {
	
	protected Map<String, Command> commands;
	protected Map<Integer, String> errors;

	/**
	 * Create new CommandsHandler and sets default errors
	 * @param contestServer
	 */
	public CommandsHandler(ContestServer contestServer) {
		this.commands = new HashMap<String, Command>();
		this.errors = new HashMap<Integer, String>();
		
		// default errors
		errors.put(100, "internal server error");
		errors.put(101, "unknown error");
		errors.put(102, "unknown command");
		errors.put(103, "wrong syntax");

		errors.put(110, "authorization required");
		errors.put(111, "not enough permissions");
		errors.put(112, "wrong username");
		errors.put(113, "wrong password");
		errors.put(114, "already logedin");
	}

	/**
	 * Handle given command
	 * @param client - client that performed given command
	 * @param cmd - command
	 */
	public void handleCommand(Client client, String cmd){
		try {
			
			String[] args = cmd.split(" ");
			
			if(!commands.containsKey(args[0]))
				throw new CommandException(102);
			
			commands.get(args[0]).handle(client, Arrays.copyOfRange(args, 1, args.length));
			
		} catch (CommandException e) {
			writeError(client, e.code);
		} catch (Exception e) {
			writeError(client, 100);
			System.err.println("Command '"+cmd+"' -> 100 internal server error");
			e.printStackTrace();
		} 
	}

	/**
	 * Returns registered commands
	 * @return
	 */
	public Map<String, Command> getCommands() {
		return commands;
	}

	/**
	 * Returns registered errors
	 * @return
	 */
	public Map<Integer, String> getErrors() {
		return errors;
	}

	/**
	 * Register new command
	 * @param name - command name
	 * @param command - command handler
	 */
	public void addCommand(String name, Command command){
		this.commands.put(name.toUpperCase(), command);
	}

	/**
	 * Unregister command by name
	 * @param name - namo of command to unregister
	 * @return unregistered command
	 */
	public Command removeCommand(String name){
		return this.commands.remove(name.toUpperCase());
	}

	/**
	 * Return registered command by name
	 * @param name - command name
	 * @return registered command
	 */
	public Command getCommand(String name){
		return this.commands.get(name.toUpperCase());
	}

	/**
	 * Register new error
	 * @param code - code of new error
	 * @param text - text of new error
	 */
	public void addError(int code, String text){
		this.errors.put(code, text);
	}

	/**
	 * Unregister error by code
	 * @param code
	 * @return unregistered error
	 */
	public String removeError(int code){
		return this.errors.remove(code);
	}

	/**
	 * Returns error by code
	 * @param code
	 * @return
	 */
	public String getError(int code){
		return this.errors.get(code);
	}

	/**
	 * Sends error text to client according to error code
	 * @param client
	 * @param code
	 */
	private void writeError(Client client, int code){
		String msg = errors.containsKey(code) ? errors.get(code) : errors.get(101);
		client.send("ERR "+code+" "+msg+"\n");
	}

}
