import java.net.*;
public class user {
	private String username;
	private Socket socket;
	private boolean spy;
	public user (String user,Socket s)
	{
		username=user;
		socket=s;
		spy=false;
	}
	public void setSpy()
	{
		spy=true;
	}
	public boolean isSpy()
	{
		return spy;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}
