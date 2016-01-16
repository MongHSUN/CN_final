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
	public static ServerSocket file_ser;
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
			}
			System.out.println(member[my_count].id+" login !");
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
			try {
				Socket file_client = file_ser.accept();
				System.out.println(file_client);
				int flag=0,i=0;
				pw.println("SYSTEM : Please enter the account you want to transfer file");
				String name = br.readLine();
				for (i=0;i<user_count;i++)
					if(name.equals(member[i].id)&&member[i].live==true){
						flag = 1;
						break;
					}
				pw.println("SYSTEM : Please enter the file you want to transfer");
				String file_name = br.readLine();
            	File file = new File(file_name); 
           		if (file.exists()) file.delete(); 
           		file.createNewFile();	
           		RandomAccessFile raf = new RandomAccessFile(file, "rw"); 
           		InputStream netIn = file_client.getInputStream();
            	InputStream in =new BufferedInputStream(netIn);
           		byte[] buf = new byte[1024];
           		int num = in.read(buf);
           		while (num !=  -1) { 
                	raf.write(buf, 0, num); 
             		raf.skipBytes(num); 
              		num = in.read(buf);   		
         		} 
           		in.close();
           		raf.close();
           		file_client.close();
           		pw.println("SYSTEM : File transfer succeed");
           		if (flag==1){
           			OutputStream os_tmp = member[i].socket.getOutputStream();
					PrintWriter pw_tmp = new PrintWriter(os_tmp, true);
					pw_tmp.println("SYSTEM : "+member[my_count].id+" sends "+file_name+" to you");
           			System.out.println(member[my_count].id+" sends "+file_name+" to "+member[i].id);
      			}
      			else
      				System.out.println(member[my_count].id+" sends "+file_name+" to server");
      		} catch(Exception ex) {}/*error do nothing*/
		}
		public void download(int my_count){
			try{
				Socket file_client = file_ser.accept();
				System.out.println(file_client);
				File file;
				pw.println("SYSTEM : Please enter the file name you want to download");
				String file_name = br.readLine();
				file = new File(file_name);
    			if(!file.isFile()){
    				pw.println("STSTEM : Invalid file name ! Download stops");
    				file_client.close();
    				return;
    			}
    			pw.println("SYSTEM : File download starts");
    			FileInputStream fos = new FileInputStream(file);
      			OutputStream netOut = file_client.getOutputStream();
      			OutputStream doc = new BufferedOutputStream(netOut);
      			byte[] buf = new byte[1024];
      			int num = fos.read(buf);
      			while (num != -1) { 
        			doc.write(buf, 0, num); 
        			doc.flush(); 
        			num = fos.read(buf); 
      			}
      			doc.close();
      			fos.close();
      			file_client.close();
      			pw.println("SYSTEM : File download succeed");
      			System.out.println(member[my_count].id+" downloads "+file_name);	
      		} catch(Exception ex) {}/*error do nothing*/
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
					else if (str.equals("FILE"))
						file(my_count);
					else if (str.equals("DOWNLOAD"))
						download(my_count);
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
		int i;
		InetAddress addr = InetAddress.getByName("127.0.0.1");
		ServerSocket ser = new ServerSocket(12345, 100, addr);
		file_ser = new ServerSocket(23456, 100, addr);
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
