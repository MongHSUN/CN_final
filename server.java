import java.io.*;
import java.net.*;

public class server{
	public static void main(String args[]) throws IOException{
		//initial
		int port = 12345;
		InetAddress addr = InetAddress.getByName("114.45.62.156");
		ServerSocket ser = new ServerSocket(port, 50, addr);
		//accept
		Socket client = ser.accept();
		OutputStream os = client.getOutputStream();
		PrintWriter pw = new PrintWriter(os, true);
		pw.println("Your are connected!");
		InputStreamReader isr = new InputStreamReader(client.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String str = br.readLine();
		System.out.println(str);
		//second
		Socket client2 = ser.accept();
		OutputStream os2 = client2.getOutputStream();
		PrintWriter pw2 = new PrintWriter(os2, true);
		pw2.println("Your are connected!");
		pw2.println(str);
		while(true){}
	}
}