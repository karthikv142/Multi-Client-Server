/*
 * Reference link: http://www.di.ase.md/~aursu/ClientServerThreads.html
 */
package chat_room_server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI implements Runnable {

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;

	
	//From Client
	
	 // The client socket
	  private static Socket clientSocket = null;
	  // The output stream
	  private static PrintStream os = null;
	  // The input stream
	  private static DataInputStream is = null;

	  private static BufferedReader inputLine = null;
	  private static boolean closed = false;
	  
	  
	  /*
	   * Create a thread to read from the server. (non-Javadoc)
	   * 
	   * @see java.lang.Runnable#run()
	   */
	  public void run() {
	    /*
	     * Keep on reading from the socket till we receive "Bye" from the
	     * server. Once we received that then we want to break.
	     */
	    String responseLine;
	    System.out.println("Waiting for the server..");
	    try {
	      while ((responseLine = is.readLine()) != null) {
	    	  textArea.append("\n"+responseLine);
	        if (responseLine.indexOf("*** Bye") != -1)
	          break;
	      }
	      closed = true;
	    } catch (IOException e) {
	      System.err.println("IOException:  " + e);
	    }
	  }
	
	//end of client
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//from client main
		
		// The default port.
	    int portNumber = 2228;
	    // The default host.
	    String host = "localhost";

	    if (args.length < 2) {
	      System.out
	          .println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
	              + "Now using host=" + host + ", portNumber=" + portNumber);
	    } else {
	      host = args[0];
	      portNumber = Integer.valueOf(args[1]).intValue();
	    }

	    /*
	     * Open a socket on a given host and port. Open input and output streams.
	     */
	    try {
	      clientSocket = new Socket(host, portNumber);
	    //  inputLine = new BufferedReader(new InputStreamReader(System.in));
	      os = new PrintStream(clientSocket.getOutputStream());
	      is = new DataInputStream(clientSocket.getInputStream());
	    } catch (UnknownHostException e) {
	      System.err.println("Don't know about host " + host);
	    } catch (IOException e) {
	      System.err.println("Couldn't get I/O for the connection to the host "
	          + host);
	    }

	    /*
	     * If everything has been initialized then we want to write some data to the
	     * socket we have opened a connection to on the port portNumber.
	     */
	    if (clientSocket != null && os != null && is != null) {
	      try {
	    	  try {
					ClientGUI window = new ClientGUI();
			        new Thread(window).start();

					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        /* Create a thread to read from the server. */
	        while (!closed) {
	           // os.println(inputLine.readLine().trim());
	          }
	        /*
	         * Close the output stream, close the input stream, close the socket.
	         */
	        os.close();
	        is.close();
	        clientSocket.close();
	      } catch (IOException e) {
	        System.err.println("IOException:  " + e);
	      }
	    }
		
		//end of client main
		
		
/*		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 646, 473);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(529, 399, 97, 25);
		btnSend.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String message =textField.getText();
				textField.setText(null);
				os.println(message);
			}
			
		}		
		);
		frame.getContentPane().add(btnSend);
		
		textField = new JTextField();
		textField.setBounds(10, 400, 507, 22);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 628, 397);
		frame.getContentPane().add(scrollPane);
		textArea= new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
	}
}
