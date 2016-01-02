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
			this.client = client;
			this.pw = pw;
			this.br = br;		
		}
		public int registration(){
			boolean succeed=true;
			int tmp=0,i,flag,my_count=user_count;
			String mypassword;
			pw.println("SYSTEM : Please enter your account ( new for registration )");
			while(succeed){
				try{
					String str = br.readLine();
					//new account
					if(str.equals("new")){
						while(true){
							flag = 0;
							if(tmp==0)
								pw.println("SYSTEM : Please enter your new account");
							String account = br.readLine();
							for(i=0;i<user_count;i++){
								if(account.equals(member[i].id)){
									pw.println("SYSTEM : This account has been used ! Enter another again");
									tmp=1;
									flag=1;
									break;
								}
							}
							if(flag==1)
								continue;
							pw.println("SYSTEM : Please enter your password");
							mypassword = br.readLine();
							succeed = false;
							my_count = user_count;
							user_count++;
							member[my_count].id = account;
							member[my_count].password = mypassword;
							member[my_count].socket = client;
							member[my_count].live = true;
							break;
						}
					}
					//standord login
					else{
						pw.println("SYSTEM : Please enter your password");
						mypassword = br.readLine();
						for(i=0;i<user_count;i++)
							if(str.equals(member[i].id)&&mypassword.equals(member[i].password)){
								succeed = false;
								my_count = i;
								member[my_count].socket = client;
								member[my_count].live = true;
								break;
							}
						if(succeed)
							pw.println("SYSTEM : Incorrect account or password ! Enter your account again");
					}
				}catch (IOException e){/*error do nothing*/}
				System.out.println(member[my_count].id+" login !");
			}
			return my_count;
		}
		public void knock(int my_count){
			int status=0,i;
			try{
				pw.println("SYSTEM : Please enter the account you want to knock");
				String str = br.readLine();
				for(i=0;i<user_count;i++){
					if (str.equals(member[i].id)&&member[i].live==false) {
						status=1;
						break;
					}
					else if(str.equals(member[i].id)&&member[i].live==true){
						status=2;
						break;
					}
				}
				if(status==0)
					pw.println("SYSTEM : This account hasn't been registered !");
				else if(status==1)
					pw.println("SYSTEM : The user is offline !");
				else	
					pw.println("SYSTEM : The user is online !");
				System.out.println(member[my_count].id+" knocks "+str+"!");
			}catch (IOException e){/*error do nothing*/}
		}
		public void message(int my_count){
			int i,flag=0;
			try{
				pw.println("SYSTEM : Please enter the user you want to message");
				String name = br.readLine();
				for (i=0;i<user_count;i++){
					if(name.equals(member[i].id)&&member[i].live==true){
						flag = 1;
						break;
					}
					else if(name.equals(member[i].id)&&member[i].live==false){
						flag = 2;
						break;
					}
				}
				if(flag==1){
					pw.println("SYSTEM : Please enter the message you want to send");
					String input = br.readLine();
					OutputStream os_tmp = member[i].socket.getOutputStream();
					PrintWriter pw_tmp = new PrintWriter(os_tmp, true);
					pw_tmp.println(member[my_count].id+" : "+input);
					pw.println(member[my_count].id+" : "+input);
					System.out.println(member[my_count].id+" to "+member[i].id+" : "+input);
				}
				else if(flag==2)
					pw.println("SYSTEM : The user is offline !");
				else
					pw.println("SYSTEM : No such user !");
			}catch (IOException e){/*error do nothing*/}
		}
		public void file(int my_count){
			
		}
		public boolean logout(int my_count){
			try{
				pw.println("SYSTEM : You log out the system !");
				member[my_count].live = false;
				client.close();
				System.out.println(member[my_count].id+" leave !");
			}catch (IOException e){/*error do nothing*/}
			return false;
		}
		public void run(){
			boolean state=true;
			pw.println("SYSTEM : Your are connected!");
			System.out.println(client);
			System.out.println("user_count : "+user_count);
			int my_count = registration();
			pw.println("SYSTEM : Login succeed !");
			//server do read-and-write
        	while(state){
        		try{	
					String str = br.readLine();
					if (str.equals("LOGOUT")) 
						state = logout(my_count);
					else if (str.equals("KNOCK"))
						knock(my_count);
					else if (str.equals("MESSAGE"))
						message(my_count);
					else if (str.equals("FILE")){
						pw.println("SYSTEM : Coming soon ......");
					}
					else if (str.equals("CHAT")){
						pw.println("SYSTEM : Coming soon ......");
					}
					//other
					else{
						System.out.println(member[my_count].id+" : "+str);
						pw.println("SYSTEM : invalid command !");
					}
				}catch (IOException e){/*error do nothing*/}
			}
		}
    }
    //main function
    public static void main(String args[]) throws IOException{
		new server().go();
	}
	public void go() throws IOException{
		//initial
		int port = 12345,i;
		InetAddress addr = InetAddress.getByName("192.168.1.107");
		ServerSocket ser = new ServerSocket(port, 100, addr);
		for(i=0;i<100;i++)
			member[i] = new Member();
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
