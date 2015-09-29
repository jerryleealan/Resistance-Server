import java.net.*;
public class user {
	private String username;
	private Socket socket;
	private boolean spy;
	private boolean missionleader;
	private int ID;
	public user (String user,Socket s, int i)
	{
		ID=i;
		username=user;
		socket=s;
		spy=false;
		missionleader=false;
	}
	public int id()
	{
		return ID;
	}
	public boolean isMissionLeader()
	{
		return missionleader;
	}
	public void setLeader(boolean b)
	{
		missionleader=b;
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
