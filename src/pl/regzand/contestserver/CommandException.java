package pl.regzand.contestserver;

public class CommandException extends Exception {
	private static final long serialVersionUID = -2618748781086564224L;
	
	public final int code;
	
	public CommandException(int code){
		this.code = code;
	}

}
