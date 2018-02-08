package lejosTest;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerSide {
	/**
	 * Created by
	 * Hadas Barel
	 * Channa Goldberg
	 * Meizy Gambash
	 * on 10/09/2017.
	 */
	private int port;
	private boolean gameIsNotOver;
	private static ServerSide serverSideInstance = null;
	
	/*open server*/
	private ServerSide(){
		 port=10;
		 gameIsNotOver = true;
	}
	
	public static ServerSide ServerSideInstance (){
		if(serverSideInstance==null)
			serverSideInstance = new ServerSide();
		return serverSideInstance;
	}
	

	public void start(){

		ServerSocket server = null;
		try {
			server=new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("connection failed");
		}

		try {
			/*Can run at the same time only 3 thread's*/
			ExecutorService executor = Executors.newFixedThreadPool(3);
			/*only when app is finish, server stop wait for requests*/
			while(gameIsNotOver){
				Socket client = server.accept();
				executor.execute(new Thread(new Requests(client)));// call Request.run
				/*wait between requests*/
				Thread.sleep(5000);
			}
			server.close();
		} catch (Exception e) {
			System.out.println("accept request failed");
		}
	}
	
	/*close server and close motors*/
	public void closeServer(){
		gameIsNotOver = false;
		Response.getResponseInstance().close();
	}
}
