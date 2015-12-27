import java.io.*;
import java.net.*;

public class client{
	public static void main(String args[]) throws IOException{
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
		while(true){
			user_input = userInput.readLine();
			pw.println(user_input);
			if(user_input.equals("LOGOUT"))
				break;
			System.out.println(br.readLine());
		}
		System.out.println(br.readLine());
	}
}