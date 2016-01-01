import java.io.*;
import java.net.*;

public class client{

	public static boolean state=true;

	public class MyRunnable implements Runnable{
		private Socket socket;
		private BufferedReader br;
		public MyRunnable(Socket socket,BufferedReader br){
			this.socket = socket;
			this.br = br;		
		}
		public void run(){
			while(state){
				try{
					System.out.println(br.readLine());
				}catch (IOException e){/*error do nothing*/}
			}
		}
	}

	public static void main(String args[]) throws IOException{
		new client().go();
	}

	public void go() throws IOException{
		//initial
		String ip = "192.168.1.107",user_input,socket_input;
		int port = 12345;
		Socket socket = new Socket(ip, port);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		//login flow
		System.out.println(br.readLine());
		socket_input = br.readLine();
		while(!socket_input.equals("SYSTEM : Login succeed !")){
			System.out.println(socket_input);
			user_input = userInput.readLine();
			pw.println(user_input);
			socket_input = br.readLine();
		}
		System.out.println(socket_input);
		//command flow
		System.out.println("Now you can enter the following commands : KNOCK MESSAGE FILE CHAT LOGOUT");
		//a thread to listen socket input
		Thread t = new Thread(new MyRunnable(socket,br));
		t.start();
		//main thread to listen user input
		while(state){
			user_input = userInput.readLine();
			pw.println(user_input);
			if(user_input.equals("LOGOUT"))
				state = false;
		}
	}
}