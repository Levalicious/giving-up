package network.commands;

public class Ping extends NetworkCommand {
    @Override
    public String execute(String[] args) {
        return "pong";
    }
}
