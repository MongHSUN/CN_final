import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class client extends JFrame implements ActionListener{

	public static boolean state=true;
	public static int file_flag=0;
	public static String ip;

	JFrame demo = new JFrame();
	//header
	JLabel account_lab = new JLabel("Account: ");
	JTextField account_text = new JTextField();
	JLabel password_lab = new JLabel("Password: ");
	JPasswordField password_text = new JPasswordField();
	JButton login_button = new JButton("Login");
	JButton clear_button = new JButton("Clear");
	JButton new_button = new JButton("New Account");
	JButton logout_button = new JButton("Logout");
	//middle
	//system
	JLabel system_lab = new JLabel("SYSTEM Message:");
	JLabel system_area = new JLabel();
	//private message
	JLabel private_lab = new JLabel("Private Message:");
	JTextArea private_area = new JTextArea();
	JLabel private_new = new JLabel("New Message:");
	JTextArea private_message = new JTextArea();
	JLabel private_to = new JLabel("User Name:");
	JTextField private_user = new JTextField();
	JButton private_button = new JButton("Send");
	JButton private_file = new JButton("Choose File");
	JLabel private_filename = new JLabel();
	JButton private_file_send = new JButton("Send");
	//chat room
	JLabel chat_lab = new JLabel("Chat Room: ");
	JButton chat_join = new JButton("Join Chat Room");
	JTextField chat_num = new JTextField();
	JTextArea chat_area = new JTextArea();
	JLabel chat_new = new JLabel("New Message:");
	JTextArea chat_message = new JTextArea();
	JLabel chat_to = new JLabel("User Name:");
	JTextField chat_user = new JTextField();
	JButton chat_button = new JButton("Send");
	JButton chat_file = new JButton("Choose File");
	JLabel chat_filename = new JLabel();
	JButton chat_file_send = new JButton("Send");
	//down
	//download file
	JLabel download_lab = new JLabel("Download File:");
	JTextField download_file = new JTextField();
	JButton download_button = new JButton("Download");
	//knock
	JButton knock = new JButton("Knock User");
	JTextField knock_user = new JTextField();
	JButton knock_chat = new JButton("Knock Chat Room");
	JTextField knock_chat_num = new JTextField();

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
    		while(!file.isFile()){
    			System.out.println("STSTEM : Invalid file name ! Please enter again");
    			file_name = userInput.readLine();
    			if(file_name.equals("quit")){
    				file_socket.close();
    				pw.println("quit");
    				return;
    			}	
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

	public void actionPerformed(ActionEvent event){
		/*if(event.getSource() == login_button){
			account = account_text.getText();
			password = password_text.getText();
			System.out.println(account);
			System.out.println(password);
			flag = 1;
		}
		else if(event.getSource() == clear_button){
			account_text.setText("");
			password_text.setText("");
		}
		else if(event.getSource() == new_button){
			account = "new";
			flag = 1;
		}*/
	}

	public void makeGUI(){
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setBounds(0, 0, 1130, 650);
		Container cont = demo.getContentPane();
		//cont.setBackground(new Color(229, 204, 255));
		cont.setLayout(null);
		//header
		account_lab.setBounds(10, 10, 70, 30);
		cont.add(account_lab);
		account_text.setBounds(70, 10, 150, 30);
		cont.add(account_text);
		password_lab.setBounds(240, 10, 70, 30);
		cont.add(password_lab);
		password_text.setBounds(310, 10, 150, 30);
		cont.add(password_text);
		login_button.setBounds(480, 10, 70, 30);
		cont.add(login_button);
		clear_button.setBounds(560, 10, 70, 30);
		cont.add(clear_button);
		logout_button.setBounds(640, 10, 80, 30);
		cont.add(logout_button);
		new_button.setBounds(950, 10, 150, 30);
		cont.add(new_button);
		//middle
		//system
		system_lab.setBounds(20, 50, 200, 30);
		cont.add(system_lab);		
		system_area.setOpaque(true);
		system_area.setBackground(new Color(192, 192, 192));
		JScrollPane system_scroll = new JScrollPane(system_area);
		system_scroll.setBounds(20, 80, 250, 300);
		cont.add(system_scroll);
		//private message
		private_lab.setBounds(290, 50, 200, 30);
		cont.add(private_lab);

		private_area.setLineWrap(true);
		private_area.setWrapStyleWord(true);
		private_area.setBackground(new Color(204, 220, 255));
		JScrollPane private_scroll = new JScrollPane(private_area);
		private_scroll.setBounds(290, 80, 400, 300);
		cont.add(private_scroll);
		//user name
		private_to.setBounds(290, 385, 150, 30);
		cont.add(private_to);

		private_user.setBounds(380, 385, 230, 30);
		cont.add(private_user);
		//new private message
		private_new.setBounds(290, 420, 150, 30);
		cont.add(private_new);

		private_message.setLineWrap(true);
		private_message.setWrapStyleWord(true);
		JScrollPane private_new_scroll = new JScrollPane(private_message);
		private_new_scroll.setBounds(380, 420, 230, 60);
		cont.add(private_new_scroll);
		
		private_button.setBounds(620, 420, 70, 30);
		cont.add(private_button);
		//new private file
		private_file.setBounds(290, 485, 100, 30);
		cont.add(private_file);

		private_filename.setBounds(400, 485, 210, 30);
		private_filename.setOpaque(true);
		private_filename.setBackground(Color.white);
		cont.add(private_filename);

		private_file_send.setBounds(620, 485, 70, 30);
		cont.add(private_file_send);
		//Chat Room
		chat_lab.setBounds(700, 50, 100, 30);
		cont.add(chat_lab);

		chat_join.setBounds(920, 50, 140, 30);
		cont.add(chat_join);

		chat_num.setBounds(1070, 50, 30, 30);
		cont.add(chat_num);
		//chat area
		chat_area.setLineWrap(true);
		chat_area.setWrapStyleWord(true);
		JScrollPane chat_area_scroll = new JScrollPane(chat_area);
		chat_area_scroll.setBounds(700, 80, 400, 300);
		cont.add(chat_area_scroll);
		//chat message
		chat_new.setBounds(700, 385, 150, 30);
		cont.add(chat_new);

		chat_message.setLineWrap(true);
		chat_message.setWrapStyleWord(true);
		JScrollPane chat_message_scroll = new JScrollPane(chat_message);
		chat_message_scroll.setBounds(790, 385, 230, 90);
		cont.add(chat_message_scroll);

		chat_button.setBounds(1030, 385, 70, 30);
		cont.add(chat_button);
		//new chat file
		chat_file.setBounds(700, 485, 100, 30);
		cont.add(chat_file);

		chat_filename.setBounds(810, 485, 210, 30);
		chat_filename.setOpaque(true);
		chat_filename.setBackground(Color.white);
		cont.add(chat_filename);

		chat_file_send.setBounds(1030, 485, 70, 30);
		cont.add(chat_file_send);
		//download
		download_lab.setBounds(20, 540, 90, 30);
		cont.add(download_lab);
		download_file.setBounds(110, 540, 100, 30);
		cont.add(download_file);
		download_button.setBounds(220, 540, 90, 30);
		cont.add(download_button);
		//knock
		knock.setBounds(400, 540, 120, 30);
		cont.add(knock);
		knock_user.setBounds(530, 540, 150, 30);
		cont.add(knock_user);
		knock_chat.setBounds(750, 540, 150, 30);
		cont.add(knock_chat);
		knock_chat_num.setBounds(910, 540, 30, 30);
		cont.add(knock_chat_num);
		//demo visible
		demo.setVisible(true);
	}

	public static void main(String args[]) throws IOException{
		ip = args[0];
		new client().go();
	}

	public void go() throws IOException{
		makeGUI();
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
		System.out.println("Now you can enter the following commands : KNOCK MESSAGE FILE DOWNLOAD CKNOCK CHAT LEAVE LOGOUT");
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