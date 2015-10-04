import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Server implements Runnable{
	private Socket connection;
	private int ID;
	private static volatile boolean acceptingnewconnections=true;
	private static volatile boolean accepting=false;
	private volatile boolean accepting2=true;
	private static volatile boolean game=true;
	
	private static volatile ArrayList<String>actions;
	
	private InputStreamReader isr;
	private static ServerSocket socket1;
	private static volatile ArrayList<user>users=new ArrayList<user>();
	private static volatile HashMap<Socket,user>hm=new HashMap<Socket,user>();
	public Server(Socket s, int i)
	{
		this.connection=s;
		this.ID=i;
	}
	public static void main(String[]args)
	{
		JFrame frame=new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				int confirm=JOptionPane.showOptionDialog(null,"Are you sure you want to exit?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
				if(confirm==0)
				{
					for(user u:users)
					{
						try{
							String returnCode="game"+(char)13;
							BufferedOutputStream os=new BufferedOutputStream(u.getSocket().getOutputStream());
							OutputStreamWriter osw=new OutputStreamWriter(os,"US-ASCII");
							osw.write(returnCode);
							osw.flush();
							u.getSocket().close();
						}
						catch(Exception e1){
							System.out.println(e1);
						}
					}
					try
					{
					socket1.close();
					}
					catch (Exception e1)
					{
						System.out.println(e1);
					}
					System.exit(0);
				}
			}
		});
		//
		JPanel mainscreen=new JPanel();
		JButton exit=new JButton("Main Screen Exit Button");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0)
			{
				for(user u:users)
				{
					try{
						String returnCode="game"+(char)13;
						BufferedOutputStream os=new BufferedOutputStream(u.getSocket().getOutputStream());
						OutputStreamWriter osw=new OutputStreamWriter(os,"US-ASCII");
						osw.write(returnCode);
						osw.flush();
						u.getSocket().close();
					}
					catch(Exception e1){
						System.out.println(e1);
					}
				}
				try
				{
				socket1.close();
				}
				catch (Exception e1)
				{
					System.out.println(e1);
				}
				System.exit(0);
			}
		});
		mainscreen.add(exit);
		//
		JButton start=new JButton();
		try
		{
			start.setText("<html><center>Host Name: "+InetAddress.getLocalHost().getHostName()+"<br><br><br>Click to Start Game<br>0 People Connected</center></html>");
		}
		catch (Exception e1)
		{
			System.out.println(e1);
			System.exit(0);
		}
		start.setFont(new Font("Times New Roman",Font.PLAIN,96));
		start.setEnabled(false);
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0)
			{
				acceptingnewconnections=false;
				try{
					Socket temporaryconnection=new Socket("localhost",1999);
					temporaryconnection.close();
				}
				catch(Exception e1)
				{
					System.out.println("fake client attempted to end .accept() method: "+e1);
				}
				frame.remove(start);
				frame.add(mainscreen);
				frame.repaint();
				frame.revalidate();
				//determine who are spys
				int[]spynum=new int[]{2,2,3,3,3,4};
				int indexpos=users.size()-5;
				ArrayList<user>spys=new ArrayList<user>();
				while(spys.size()<spynum[indexpos])
				{
					int rand=(int)(Math.random()*5);
					if(!users.get(rand).isSpy())
					{
						users.get(rand).setSpy();
						System.out.println(users.get(rand).getUsername()+" is a spy");
						spys.add(users.get(rand));
					}
				}
				for(user u:users)
				{
					try{
						String returnCode;
						if(!u.isSpy())
						{
							returnCode="start"+(char)13;
						}
						else
						{
							returnCode="spy";
							for(user v:spys)
							{
								if(!v.getUsername().equals(u.getUsername())||v.id()!=u.id())
									returnCode+=" "+v.getUsername();
							}
							returnCode+=(char)13;
						}
						BufferedOutputStream os=new BufferedOutputStream(u.getSocket().getOutputStream());
						OutputStreamWriter osw=new OutputStreamWriter(os,"US-ASCII");
						osw.write(returnCode);
						osw.flush();
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
			}
		});
		//start mission 1?
		frame.add(start);
		frame.setVisible(true);
		int port=1999;
		int count=0;
		try{
			socket1=new ServerSocket(port);
			System.out.println("Server Initialized");
			System.out.println("Hostname:"+ InetAddress.getLocalHost().getHostName());
			while(acceptingnewconnections)
			{
				Socket connection=socket1.accept();
				Runnable runnable=new Server(connection, ++count);
				if(count==1)
				start.setText("<html><center>Host Name: "+InetAddress.getLocalHost().getHostName()+"<br><br><br>Click to Start Game<br>1 Person Connected</center></html>");
				else
					start.setText("<html><center>Host Name: "+InetAddress.getLocalHost().getHostName()+"<br><br><br>Click to Start Game<br>"+count+" People Connected</center></html>");
				if(count>=5&&count<=10)
				{
					start.setEnabled(true);
				}
				else
				{
					start.setEnabled(false);
				}
				if(count==10)
				{
					acceptingnewconnections=false;
					start.doClick();
				}
				Thread thread=new Thread(runnable);
				thread.start();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		int leaderpos=0;
		/*
		for(int i=0;i<5;i++)
		{
			users.get(leaderpos%users.size()).setLeader(true);
			users.get((leaderpos-1)%users.size()).setLeader(false);
			try{
				String returnCode="lead"+(char)13;
				BufferedOutputStream os=new BufferedOutputStream(users.get(leaderpos%users.size()).getSocket().getOutputStream());
				OutputStreamWriter osw=new OutputStreamWriter(os,"US-ASCII");
				osw.write(returnCode);
				osw.flush();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		*/
	}
	public void run() {
		if(acceptingnewconnections){
			user u=new user("Default",connection, -1);
			try {
				BufferedInputStream is=new BufferedInputStream(connection.getInputStream());
				isr=new InputStreamReader(is);
				int character;
				StringBuffer process=new StringBuffer();
				while((character=isr.read())!=13){
					process.append((char)character);
				}
				u=new user(process.toString(),connection, ID);
				users.add(u);
				hm.put(connection,u);
				System.out.println(process+" "+ID);

				while(game)
				{
					if(accepting &&accepting2)
					{
						character=0;
						process=new StringBuffer();
						while((character=isr.read())!=13)
						{
							process.append((char)character);
						}
						
						System.out.println(u.getUsername()+" "+process.toString());
						accepting2=false;
					}else if(!accepting)
						accepting2=true;
				}
			}
			catch(Exception e)
			{
				System.out.println("Client"+ID+" generated "+e);
				users.remove(u);
				return;
			}
			return;
		}
	}
}
