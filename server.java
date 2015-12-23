import java.io.*;
import java.net.*;
public class server{
	
	public class MyRunnable implements Runnable{
		private Socket client;
		public MyRunnable(Socket client){
			System.out.println("Here is the starting point of Thread.");
			this.client = client;		
		}
		public void run(){
			System.out.println("Run!!");
			System.out.println(client);
			//while(true){
        		//IO
        		//InputStreamReader isr = new InputStreamReader(client.getInputStream());
				//BufferedReader br = new BufferedReader(isr);
				//String str = br.readLine();
				//System.out.println(str);
        	//}
			
		}
    }

    public static void main(String args[]) throws IOException{
		new server().go();		
	}
	public void go() throws IOException{
		//initial
		int port = 12345;
		InetAddress addr = InetAddress.getByName("10.129.162.32");
		ServerSocket ser = new ServerSocket(port, 50, addr);
		//accept
		while(true){
			System.out.println("AAA");
			Socket client = ser.accept();
			OutputStream os = client.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			pw.println("Your are connected!");
			/*InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String str = br.readLine();
			System.out.println(str);*/
			//create thread
			Thread t = new Thread(new MyRunnable(client));
			t.start();
			System.out.println("BBB");
		}
	}
}