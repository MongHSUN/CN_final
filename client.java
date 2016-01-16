import java.io.*;
import java.net.*;

public class client{

	public static boolean state=true;
	public static int file_flag=0;
	public static String ip = "127.0.0.1";

	public class MyRunnable implements Runnable{
		private Socket socket;
		private BufferedReader br;
		public MyRunnable(Socket socket,BufferedReader br){
			this.socket = socket;
			this.br = br;		
		}
		public void run(){
			while(state){
				try{
					String system_response = br.readLine();
					System.out.println(system_response);
					if(system_response.equals("SYSTEM : File download starts"))
						file_flag = 1;
					else if(system_response.equals("STSTEM : Invalid file name ! Download stops"))
						file_flag = 2;
				}catch (IOException e){/*error do nothing*/}
			}
		}
	}

	public void file(BufferedReader userInput,Socket socket){
    	try {
    		Socket file_socket = new Socket(ip,23456);
    		String file_name;
    		File file;
    		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
    		String account = userInput.readLine();
    		pw.println(account);
    		file_name = userInput.readLine();	
    		file = new File(file_name);
    		if(!file.isFile()){
    			System.out.println("STSTEM : Invalid file name ! Please enter again");
    			file_name = userInput.readLine();	
    			file = new File(file_name);	
    		}
    		pw.println(file_name);
      		FileInputStream fos = new FileInputStream(file);
      		OutputStream netOut = file_socket.getOutputStream();
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
      		file_socket.close();
    	} catch (Exception ex) {/*error do nothing*/} 
	}

	public void download(BufferedReader userInput,Socket socket){
		try{
			Socket file_socket = new Socket(ip,23456);
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			String file_name = userInput.readLine();
			pw.println(file_name);
			while(true){
				if(file_flag==1){
					file_flag=0;
					break;
				}
				else if(file_flag==2){
					file_flag=0;
					file_socket.close();
					return;
				}
			}
            File file = new File(file_name); 
           	if (file.exists()) file.delete(); 
           	file.createNewFile();	
           	RandomAccessFile raf = new RandomAccessFile(file, "rw"); 
           	InputStream netIn = file_socket.getInputStream();
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
           	file_socket.close();
		}catch (IOException e){/*error do nothing*/}
	}

	public static void main(String args[]) throws IOException{
		new client().go();
	}

	public void go() throws IOException{
		//initial
		String user_input,socket_input;
		Socket socket = new Socket(ip, 12345);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		OutputStream os = socket.getOutputStream();
		PrintWriter pw = new PrintWriter(os, true);
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
		System.out.println("Now you can enter the following commands : KNOCK MESSAGE FILE DOWNLOAD CHAT LOGOUT");
		//a thread to listen socket input
		Thread t = new Thread(new MyRunnable(socket,br));
		t.start();
		//main thread to listen user input
		while(state){
			user_input = userInput.readLine();
			pw.println(user_input);
			if(user_input.equals("LOGOUT"))
				state = false;
			else if(user_input.equals("FILE"))
				file(userInput,socket);
			else if(user_input.equals("DOWNLOAD"))
				download(userInput,socket);
		}
	}
}