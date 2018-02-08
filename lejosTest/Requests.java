package lejosTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Requests implements Runnable{
	/**
	 * Created by
	 * Hadas Barel
	 * Channa Goldberg
	 * Meizy Gambash
	 * on 10/09/2017.
	 */
	private Socket client;
	private Response response;
	
	/*gets the socket and get instance of response: in Response class we used singleton,
	 *  because we create 4 motors, there is no need to allocated they for every client*/
	public Requests(Socket client){
		this.client=client;
		response = Response.getResponseInstance();
	}

	/*This function open the socket and pulling out the message,
	 * each message is handled accordingly*/
	@Override
	public void run() {
		BufferedReader reader=null;
		try {
			reader= new BufferedReader(new InputStreamReader(client.getInputStream()));
			String str=reader.readLine();
			switch(str){
			case "1":
				/*Sync with the app*/
				Thread.sleep(10000);	
				response.hello();
				break;
			case "2":
				/*Sync with the app*/
				Thread.sleep(1000);	
				response.happy();
				break;
			case "3":
				/*Sync with the app*/
				Thread.sleep(1000);
				response.wrong();
				break;
			case "4":
				/*Sync with the app*/
				Thread.sleep(3000);
				response.waiting();
				break;
			case "5":
				/*Sync with the app*/
				Thread.sleep(1000);
				response.won();
				Thread.sleep(5000);
				
				/*gameIsNotOver = false;*/
				ServerSide.ServerSideInstance().closeServer();
				break;
			}
		} catch (IOException e) {
			System.out.println("buffer reader failed");
		} catch (InterruptedException e) {
			System.out.println("cant sleep");
		}finally {
				try {
					if (reader!=null){
					reader.close();
					}
				} catch (IOException e) {
					System.out.println("closing buffer failed");
				}
			}
	}
}
