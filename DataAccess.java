import java.rmi.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.rmi.registry.*;
import java.time.LocalDateTime;


public class DataAccess {

	private DictionaryInterface dictionary;
	
	public DataAccess(String host) throws NotBoundException, RemoteException {
		Registry clientRegistry = LocateRegistry.getRegistry(host);
		dictionary = (DictionaryInterface)clientRegistry.lookup("dictionary");
		
		System.out.println(LocalDateTime.now().toString() + " Remote Object ---> "+ dictionary);
	}
	
	public DataAccess() throws NotBoundException, RemoteException {
			Registry clientRegistry = LocateRegistry.getRegistry();
			dictionary = (DictionaryInterface)clientRegistry.lookup("dictionary");
			System.out.println(LocalDateTime.now().toString() + " Remote Object ---> "+ dictionary);
	}
	
	public String ExecuteCommand(String command, String identity) throws RemoteException{

		if(!ValidatePacket(command))
			return "Invalid packet";

		String result = "";

		//each command's part is separated by a blank space
		String[] tokens = command.split(" ");

		//0th token is actual command i.e. put|get|delete
		switch (tokens[0].toLowerCase()) {
		case "put":
			if(tokens.length < 3)
				result = "Argument Missing for PUT";
			else
				result = dictionary.put(tokens[1], tokens[2], identity);
			break;

		case "get":
			result = dictionary.get(tokens[1], identity);
			break;

		case "delete":
			result = dictionary.delete(tokens[1], identity);
			break;

		case "print":
			result = dictionary.print();
			break;

		default:
			result = "Invalid Command <" + tokens[0] + ">";
			break;
		}
		return result;
	}
	
	private boolean ValidatePacket(String command){

		boolean isValidPacket = false;

		//minLen: "get a" = 5
		if(command.length() < 5 || command.length() > 2048){
			System.out.println(LocalDateTime.now().toString()+ " command is NOT in a correct LENGTH");
			return false;
		}

		Pattern pattern = Pattern.compile("^[A-Za-z]{3,6}\\s{0,1}\\w*\\s{0,1}\\w*");
		Matcher matcher = pattern.matcher(command);

		isValidPacket = matcher.find();
		if(!isValidPacket)
			System.out.println(LocalDateTime.now().toString()+ " command is NOT in a correct FORMAT");

		return isValidPacket;
	}
}
