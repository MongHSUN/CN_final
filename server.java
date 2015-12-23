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
			System.out.println(client);
        	//IO
        	try{
        		OutputStream os = client.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				pw.println("Your are connected!");
        	} catch (IOException e){
				//error do nothing
			}
			//server do read-and-write
        	while(true){
        		try{	
					OutputStream os = client.getOutputStream();
					PrintWriter pw = new PrintWriter(os, true);
					InputStreamReader isr = new InputStreamReader(client.getInputStream());
					BufferedReader br = new BufferedReader(isr);
					String str = br.readLine();
					System.out.println(str);
					pw.println("server receive msg : "+str);
				}catch (IOException e){
					//error do nothing
				}
			}
		}
    }

    public static void main(String args[]) throws IOException{
		new server().go();		
	}
	public void go() throws IOException{
		//initial
		int port = 12345;
		InetAddress addr = InetAddress.getByName("114.45.61.130");
		ServerSocket ser = new ServerSocket(port, 50, addr);
		//accept
		while(true){
			System.out.println("Waiting new client...");
			Socket client = ser.accept();
			//create thread
			Thread t = new Thread(new MyRunnable(client));
			t.start();
		}
	}
}