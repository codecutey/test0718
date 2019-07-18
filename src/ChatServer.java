import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ChatServer {
	private static Map<String,Socket> map = new HashMap();

	public static Map<String, Socket> getMap() {
		return map;
	}
	
	

	public static void setMap(Map<String, Socket> map) {
		ChatServer.map = map;
	}
	
	public static Set<String> getSet() {
		Set<String> set = map.keySet();
		return set;
		
	}
	
	public static void main(String[] args) {
				
		try (ServerSocket ss = new ServerSocket(8888)){			
			System.out.println("  ♪(^∇^*)__ChatRoom聊天室启动__(*^∇^)♪  ");
			while(true) {
				Socket s = ss.accept();
				ChatServerRunnable csr = new ChatServerRunnable(s);
				Thread t = new Thread(csr);
				t.start();		
				
				System.out.println("用户 " + "【ip地址： " + s.getInetAddress().getHostAddress() + "】"+ "\n");
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}


}
