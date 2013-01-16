package client;// This file contains material supporting section 3.7 of the textbook:// "Object Oriented Software Engineering" and is issued under the open-source// license found at www.lloseng.com import ocsf.client.*;import common.*;import java.io.*;/** * This class overrides some of the methods defined in the abstract superclass * in order to give more functionality to the client. *  * @author Dr Timothy C. Lethbridge * @author Dr Robert Lagani&egrave; * @author Fran&ccedil;ois B&eacute;langer * @version July 2000 */public class ChatClient extends AbstractClient {	// Instance variables **********************************************	/**	 * The interface type variable. It allows the implementation of the display	 * method in the client.	 */	ChatIF clientUI;	String id;	// Constructors ****************************************************	/**	 * Constructs an instance of the chat client.	 * 	 * @param host	 *            The server to connect to.	 * @param port	 *            The port number to connect on.	 * @param clientUI	 *            The interface type variable.	 */	public ChatClient(String host, String id, int port, ChatIF clientUI)			throws IOException {		super(host, port); // Call the superclass constructor		this.clientUI = clientUI;		this.id = id;	}	// Instance methods ************************************************	/**	 * This method handles all data that comes in from the server.	 * 	 * @param msg	 *            The message from the server.	 */	public void handleMessageFromServer(Object msg) {			clientUI.display(msg.toString());	}	/**	 * This method handles all data coming from the UI	 * 	 * @param message	 *            The message from the UI.	 */	public void handleMessageFromClientUI(String message) {		if (message.startsWith(Character.toString('#'))) {			handleCmdClientUI(message);		} else {			try {				sendToServer(message);			} catch (IOException e) {				clientUI.display("Could not send message to server.  Terminating client.");				quit();			}		}	}	public void handleCmdClientUI(String cmd) {		if (cmd.startsWith("#quit")) {			if (this.isConnected()) {				clientUI.display("Deconnexion en cours...");				logOff();			}			System.exit(0);		} else if (cmd.startsWith("#logoff")) {			if (this.isConnected()) {				clientUI.display("Deconnexion en cours...");				logOff();			} else {				clientUI.display("Vous n'etes pas connecter a un serveur");			}		} else if (cmd.startsWith("#sethost")) {			if (this.isConnected()) {				clientUI.display("Cette fonction est indisponible lorsque vous etes connecte");			} else {				setHost(cmd.substring(9));				clientUI.display("L'host a ete modifie");			}		} else if (cmd.startsWith("#setport")) {			if (this.isConnected()) {				clientUI.display("Cette fonction est indisponible lorsque vous etes connecte");			} else {				setPort(Integer.parseInt(cmd.substring(9)));				clientUI.display("Le port a ete modifie");			}		} else if (cmd.startsWith("#login")) {			if (this.isConnected()) {				clientUI.display("Cette fonction est indisponible lorsque vous etes connecte");			} else {				try {					openConnection();					this.id = cmd.substring(7);					sendToServer("#login "+id);				} catch (IOException e) {					// TODO Auto-generated catch block					e.printStackTrace();				}			}		} else if (cmd.startsWith("#getport")) {			clientUI.display("Le port est : " + getPort());		} else if (cmd.startsWith("#gethost")) {			clientUI.display("L'host est : " + getHost());		}	}	/**	 * This method terminates the client.	 */	public void logOff() {		try {			sendToServer("#logoff");			closeConnection();		} catch (IOException e) {			clientUI.display("Could not send message to server.  Terminating client.");			quit();		}	}	public void quit() {		try {			closeConnection();		} catch (IOException e) {		}		System.exit(0);	}	protected void connectionException(Exception exception) {		clientUI.display("Une erreur est survenue et le serveur a ferme");		System.exit(0);	}}// End of ChatClient class