package pl.regzand.contestserver;

public interface Command {
	public void handle(Client client, String[] args) throws CommandException;
}
