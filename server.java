import java.io.*;
import java.net.*;
public class server{
	//structure
	public class Member{
		String id;
		String password;
		Socket socket;
		boolean live;	
	} 
	public static Member[] member = new Member[100];
	public static int user_count=0; 
	//thread
	public class MyRunnable implements Runnable{
		private Socket client;
		public MyRunnable(Socket client){
			System.out.println("Here is the starting point of Thread.");
			this.client = client;		
		}
		public void run(){
			int my_count=user_count;			//my structure id
			member[user_count] = new Member();
        	user_count++;
			System.out.println(client);
        	//IO
        	try{
        		OutputStream os = client.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				pw.println("Your are connected!");
				System.out.println("user_count : "+user_count);
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
    //main function
    public static void main(String args[]) throws IOException{
		new server().go();
	}
	public void go() throws IOException{
		//initial
		int port = 12345;
		InetAddress addr = InetAddress.getByName("114.45.55.123");
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