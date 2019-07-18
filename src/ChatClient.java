import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		try(Socket socket = new Socket("127.0.0.1",8888);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
			
			ClientMessageReceiver messagereceiver = new ClientMessageReceiver(dis);
			Thread th = new Thread(messagereceiver);
			th.start();
			while(true) {
				String in = scanner.nextLine();
				if(in.equals("exit")) {
					System.out.println("退出成功");
					SocketUtils.Outputwrite(socket, in);
					break;
				}else {
					SocketUtils.Outputwrite(socket, in);
				}
			}
			messagereceiver.stop();
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
