import java.io.*;
import java.net.*;

public class client{
	public static void main(String args[]) throws IOException{
		//initial
		String ip = "114.45.55.123";
		int port = 12345;
		Socket socket = new Socket(ip, port);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		//IO
		while(true){
			System.out.println(br.readLine());
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			pw.println(userInput.readLine());
		}
	}
}