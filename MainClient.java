import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class MainClient {

	private static BufferedReader reader;
	private static String identity;
	private static String result;
	private static DataAccess da;
	
	public static void main(String[] args) {

		reader = new BufferedReader(new InputStreamReader(System.in));
		
		//gets a Username that is shown on server's log
		getIdentity();
		
		//get IP/hostname and gets remote object
		getHostAndCreateRemoteDataAccess();
		
		//validate command and then call the relevant remote method
		getCommandAndExecuteRemotely();
	}

	private static void getCommandAndExecuteRemotely() {
		String input = "";
		System.out.println("Write a Command (send \"help\" for instructions): ");
			
		while(true) {
			try {
				input = reader.readLine();

				if(input.equals("exit"))
					break;

				else if(input.toLowerCase().equals("help")){ 
					PrintManual();
				}
				else if(input.toLowerCase().equals("sendbatch")) {
					sendBatchCommandsToServer();
				}
				else{
					result = da.ExecuteCommand(input, identity);
					System.out.println(LocalDateTime.now().toString() + " " + result);
				}
			} 
			catch (Exception e) {
				System.out.println(LocalDateTime.now().toString()+ " exception occured: " + e.getMessage());
			}
		}
		System.out.println(LocalDateTime.now().toString()+ " client application terminated");
		
	}
	
	private static void sendBatchCommandsToServer() {
		
		String command;
		String response = "";
		try {
			for (int i = 0; i < 10; i++) {
				command = "PUT " + "KEY_" + i + " VALUE_" + i+ "\n";
				response = da.ExecuteCommand(command, identity);
				System.out.println(LocalDateTime.now().toString()+ " serverAnswered: "+ response );
			}
			for (int i = 3; i < 8; i++) {
				command = "GET " + "KEY_" + i + "\n";
				response = da.ExecuteCommand(command, identity);
				System.out.println(LocalDateTime.now().toString()+ " serverAnswered: "+ response );
			}
			for (int i = 1; i < 6; i++) {
				command = "DELETE " + "KEY_"+ i + "\n";
				response = da.ExecuteCommand(command, identity);
				System.out.println(LocalDateTime.now().toString()+ " serverAnswered: "+ response );
			}
		} 
		catch (Exception e) {
		}
		
	}
	
	private static void getHostAndCreateRemoteDataAccess() {
		
		String host = "";
		
		//repeat till a valid host entered
		while(true) {
			
			try {
				
				System.out.println("Enter the host IP/name of the remote server: ");
				host = reader.readLine();
				da = new DataAccess(host);
				return;
			} 
			catch (NotBoundException e) {
				System.out.println(LocalDateTime.now().toString() + " notBountException: "+e.getMessage());
				System.out.println("Make sure the IP/Hostname is correct");
			}
			catch(RemoteException e) {
				System.out.println(LocalDateTime.now().toString() + " remoteException: "+e.getMessage());
				System.out.println("Make sure the IP/Hostname is correct");
			}
			catch (Exception e){
				
			}
		}
	}

	private static void getIdentity() {
		try {
			System.out.println("Enter you Username/ID...(just to show different clients on server's log):");
			identity = reader.readLine();
		} 
		catch (Exception e) {
		}
	}
	
	private static void PrintManual(){
		System.out.println("To send a single packet use this pattern [command] [arg1] [arg2]");
		System.out.println("Where [command] : {put|get|delete|print} and [arg1] = key [arg2] = value");
		System.out.println("Use \"Print\" command to get all key-value pairs-> key1:val1;key2:val2;...");
		System.out.println("To send pre-populated KEY-VALUEs just type \"sendbatch\"");
		System.out.println("To close connection in UDP mode type \"exit\"");
		System.out.println("Please pay attention that commands ARE NOT case-sensitive but Key-Value pairs ARE case-sensitive");
	}

}
