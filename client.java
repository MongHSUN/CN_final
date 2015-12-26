import java.io.*;
import java.net.*;

public class client{
	public static void main(String args[]) throws IOException{
		//initial
		String ip = "192.168.1.107",input;
		int port = 12345;
		Socket socket = new Socket(ip, port);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		//IO
		System.out.println(br.readLine());
		while(true){
			System.out.println(br.readLine());
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			input = userInput.readLine();
			pw.println(input);
			if(input.equals("logout"))
				break;
		}
		System.out.println(br.readLine());
	}
}