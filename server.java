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
		private PrintWriter pw;
		private BufferedReader br;
		public MyRunnable(Socket client ,PrintWriter pw, BufferedReader br){
			System.out.println("Here is the starting point of Thread.");
			this.client = client;
			this.pw = pw;
			this.br = br;		
		}
		public void registration(int my_count){
			boolean succeed=true;
			int tmp=0,i,flag;
			String mypassword;
			pw.println("Please enter your account ( new for registration ) :");
			while(succeed){
				try{
					String str = br.readLine();
					//new account
					if(str.equals("new")){
						while(true){
							flag = 0;
							if(tmp==0)
								pw.println("Please enter your new account :");
							String account = br.readLine();
							for(i=0;i<user_count;i++){
								if(account.equals(member[i].id)){
									pw.println("This account has been used ! Enter another again :");
									tmp=1;
									flag=1;
									break;
								}
							}
							if(flag==1)
								continue;
							pw.println("Please enter your password :");
							mypassword = br.readLine();
							succeed = false;
							member[my_count].id = account;
							member[my_count].password = mypassword;
							member[my_count].socket = client;
							member[my_count].live = true;
							break;
						}
					}
					//standord login
					else{
						pw.println("Please enter your password :");
						mypassword = br.readLine();
						for(i=0;i<user_count;i++)
							if(str.equals(member[i].id)&&mypassword.equals(member[i].password)){
								succeed = false;
								member[my_count].socket = client;
								member[my_count].live = true;
								member[my_count].id = member[i].id;
								member[my_count].password = member[i].password;
								break;
							}
						if(succeed)
							pw.println("Incorrect account or password ! Enter your account again :");
					}
				}catch (IOException e){
					//error do nothing
				}
			}
		}
		public void run(){
			pw.println("Your are connected!");
			int my_count=user_count;			//my structure id
			member[user_count] = new Member();
			member[user_count].live = false;
        	user_count++;
			System.out.println(client);
			System.out.println("user_count : "+user_count);
			registration(my_count);
			pw.println("Login succeed !");
			//server do read-and-write
        	while(true){
        		try{	
					String str = br.readLine();
					if (str.equals("logout")) {
						pw.println("You log out the system !");
						member[my_count].live = false;
						client.close();
						break;
					}
					else{
						System.out.println(member[my_count].id+" : "+str);
						pw.println(member[my_count].id+" : "+str);
					}
				}catch (IOException e){
					//error do nothing
				}
			}
			System.out.println("A user leave !");
		}
    }
    //main function
    public static void main(String args[]) throws IOException{
		new server().go();
	}
	public void go() throws IOException{
		//initial
		int port = 12345;
		InetAddress addr = InetAddress.getByName("192.168.1.107");
		ServerSocket ser = new ServerSocket(port, 50, addr);
		//accept
		while(true){
			System.out.println("Waiting new client...");
			Socket client = ser.accept();
			OutputStream os = client.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			//create thread
			Thread t = new Thread(new MyRunnable(client,pw,br));
			t.start();
		}
	}
}
