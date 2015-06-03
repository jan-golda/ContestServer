package pl.regzand.contestserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandsHandler {
	
	Map<String, Command> commands;
	Map<Integer, String> errors;
	
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
	
	public Map<String, Command> getCommands() {
		return commands;
	}

	public Map<Integer, String> getErrors() {
		return errors;
	}

	public void addCommand(String name, Command command){
		this.commands.put(name.toUpperCase(), command);
	}
	
	public Command removeCommand(String name){
		return this.commands.remove(name.toUpperCase());
	}
	
	public Command getCommand(String name){
		return this.commands.get(name.toUpperCase());
	}
	
	public void addError(int code, String text){
		this.errors.put(code, text);
	}
	
	public String removeError(int code){
		return this.errors.remove(code);
	}
	
	public String getError(int code){
		return this.errors.get(code);
	}
	
	private void writeError(Client client, int code){
		String msg = errors.containsKey(code) ? errors.get(code) : errors.get(101);
		client.send("ERR "+code+" "+msg+"\n");
	}

}
