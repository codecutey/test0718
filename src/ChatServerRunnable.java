import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;



public class ChatServerRunnable implements Runnable{
	
	private Socket socket;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private String currentName;
	
	
	public ChatServerRunnable(Socket socket) {
		super();
		this.socket = socket;
		
		try {
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	


	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}


	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}



	@Override
	public void run() {
		try {
			write("欢迎━(*｀∀´*)ノ! 来到聊天室");
			login();
			write("*输入【list users】获取在线用户列表*\n*输入【to all 消息内容】发送群聊消息*\n*输入【to 某个用户的昵称 消息内容】发送单聊消息*\n*输入【exit】选择退出*\n");
			while(true) {
				String content = dis.readUTF();
				if(content.equals("exit")) {
					System.out.println( currentName + "退出成功");
					break;
				}else if(content.equals("list users")) {
					getUsers();
					System.out.println( currentName + "选择展示在线用户列表");
				}else if(content.startsWith("to")) {
					sendMessage(content);
				}else {
					write("您输入的形式错误，请重新输入。");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void write(String str) throws IOException {
		SocketUtils.Outputwrite(socket, str);
	}
	
	
	public void login() throws IOException {  //实际就是在初始化map
		write("请输入昵称");
		while(true) {
			String name = dis.readUTF();
			synchronized (ChatServerRunnable.class) {
				if(ChatServer.getMap().containsKey(name)) {
					write("昵称已存在，请重新输入");
				}else {
					this.currentName = name;
					ChatServer.getMap().put(currentName, socket);
					setCurrentName(currentName);
					write(currentName + "登陆成功");
					System.out.println( currentName + "登陆成功");
					break;
				}
				
			}
		}
		
	}
	
	public void getUsers() throws IOException{
		Set<String> userset = ChatServer.getSet();
		Iterator it = userset.iterator();
		write("******在线用户******");
		while(it.hasNext()) {
			String user = (String) it.next();			
			write("【" + user + "】");			
			
		}
	}
	
	public void sendMessage(String str) throws IOException{
		int a = str.indexOf(" ", 3);
		String sendUser = str.substring(3, a);
		String message = str.substring(a+1);
		String dealmessage = dealMessage(message);
		if(sendUser.equals("all")) {
			sendAllMessage(dealmessage);
			System.out.println( currentName + "对全体发送信息.");
		}else {
			sendSpeMessage(sendUser , dealmessage);
		}
		
		
	}
	
	public void sendAllMessage(String str) throws IOException{
		for(String s : ChatServer.getSet()) {
			if(!s.equals(currentName)) {
				SocketUtils.Outputwrite(ChatServer.getMap().get(s), currentName + "对全体：" + str);
				
			}
		}
	}
	
	public void sendSpeMessage(String sendUser , String str) throws IOException{
		if(!ChatServer.getMap().containsKey(sendUser)) {
			write("您要单独聊天的用户" + "【" + sendUser + "】" + "不存在。");
		}else {
			Socket sock = ChatServer.getMap().get(sendUser);
			if(sock == null) {
				write("您要单独聊天的用户" + "【" + sendUser + "】" + "已下线。");
			}else {
				String formatstr = currentName + "对" + sendUser + "说：\n" +  str;
				SocketUtils.Outputwrite(sock, formatstr);
				System.out.println( currentName + "对" + sendUser + "发送信息.");
			}
		}
		

	}
	
	public String dealMessage(String str){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newStr = str + "\n发送时间： " + sdf.format(date);
		return newStr;
	}
	
	

}
