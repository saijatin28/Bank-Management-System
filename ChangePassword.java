import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
public class ChangePassword extends JInternalFrame implements ActionListener {
	static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/test";  

    //  Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 
	private JPanel jpFind = new JPanel();
	private JLabel lbNo, lbPass, lbOldPass;
	private JTextField txtNo, txtPass, txtOldPass;
	private JButton btnChange, btnCancel;

	private int recNo;
	private int rows = 0;
	private	int total = 0;
    private String oldPass;

	//String Type Array use to Load Records From File.
	private String records[][] = new String [500][7];

	private FileInputStream fis;
	private DataInputStream dis;

	ChangePassword () {

		//super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Change Password", false, true, false, true);
		setSize (350, 235);

		jpFind.setLayout (null);

		lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
        lbPass = new JLabel ("Password:");
		lbPass.setBounds (15, 55, 80, 25);
        lbPass.setForeground(Color.black);
        lbOldPass = new JLabel ("Old Password:");
		lbOldPass.setBounds (15, 90, 80, 25);
        lbOldPass.setForeground(Color.black);
		

		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		txtNo.setBounds (125, 20, 200, 25);
		
		txtPass = new JTextField ();
		txtPass.setBounds (125, 55, 200, 25);
		txtOldPass = new JTextField ();
		txtOldPass.setBounds (125, 90, 200, 25);
		

		//Restricting The User Input to only Numerics.
		txtNo.addKeyListener (new KeyAdapter() {
			public void keyTyped (KeyEvent ke) {
				char c = ke.getKeyChar ();
				if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep ();
					ke.consume ();
      				}
    			}
  		}
		);

        txtNo.addFocusListener (new FocusListener () {
			public void focusGained (FocusEvent e) { }
			public void focusLost (FocusEvent fe) {
				if (txtNo.getText().equals ("")) { }
				else {
					rows = 0;
				//	populateArray ();	//Load All Existing Records in Memory.
				//	findRec ();		//Finding if Account No. Already Exist or Not.
				}
			}
		}
		);

		//Aligning The Buttons.
		btnChange = new JButton ("Change");
		btnChange.setBounds (20, 120, 120, 25);
		btnChange.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (200, 120, 120, 25);
		btnCancel.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpFind.add (lbNo);
		jpFind.add (txtNo);
		jpFind.add(lbPass);
        jpFind.add(txtPass);
        jpFind.add(txtOldPass);
        jpFind.add(lbOldPass);
		jpFind.add (btnChange);
		jpFind.add (btnCancel);

		//Adding Panel to Window.
		getContentPane().add (jpFind);

		//populateArray ();	//Load All Existing Records in Memory.

		//In the End Showing the New Account Window.
		setVisible (true);

	}

	//Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed (ActionEvent ae) {

		Object obj = ae.getSource();
		Connection conn = null; 
		Statement stmt = null; 
		try { 
			// STEP 1: Register JDBC driver 
			Class.forName(JDBC_DRIVER); 
				
			//STEP 2: Open a connection 
//			System.out.println("Connecting to database..."); 
			conn = DriverManager.getConnection(DB_URL,USER,PASS);  
			
			//STEP 3: Execute a query 
//			System.out.println("Creating table in given database..."); 
			stmt = conn.createStatement(); 
			String sql;
			sql = "select pass from data where acno='"+txtNo.getText()+"'";
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				oldPass = rs.getString(1);
			}
//			System.out.println("Created table in given database..."); 
			
			// STEP 4: Clean-up environment 
			stmt.close(); 
			conn.close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		if (obj == btnChange) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Id of Customer to Search.",
							"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			}
            else if (!txtOldPass.getText().equals(oldPass)) {
                JOptionPane.showMessageDialog (this, "Please! Enter Correct Password.",
							"BankSystem - Wrong password", JOptionPane.PLAIN_MESSAGE);
				txtOldPass.requestFocus();
            }
			else {
				//editRec(recNo);
				conn = null; 
                stmt = null; 
                try { 
                    // STEP 1: Register JDBC driver 
                    Class.forName(JDBC_DRIVER); 
                        
                    //STEP 2: Open a connection 
        //			System.out.println("Connecting to database..."); 
                    conn = DriverManager.getConnection(DB_URL,USER,PASS);  
                    
                    //STEP 3: Execute a query 
        //			System.out.println("Creating table in given database..."); 
                    stmt = conn.createStatement(); 
                    String sql;
                    sql = "update data set pass = '"+txtPass.getText()+"' where acno='"+txtNo.getText()+"'";
                    stmt.executeUpdate(sql);
        //			System.out.println("Created table in given database..."); 
                    
                    // STEP 4: Clean-up environment 
                    stmt.close(); 
                    conn.close(); 
                    JOptionPane.showMessageDialog (this, "Successfully updated details!",
							"BankSystem - Success", JOptionPane.PLAIN_MESSAGE);
                    txtClear ();
                    setVisible (false);
                    dispose();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
			}
		}
		if (obj == btnCancel) {
			txtClear ();
			setVisible (false);
			dispose();
		}

	}

    // void editRec(int recNo) {
    //     records[recNo][6] = txtPass.getText();
    //     editFile();
    // }

    // public void editFile () {

	// 	try {
	// 		FileOutputStream fos = new FileOutputStream ("Bank.dat");
	// 		DataOutputStream dos = new DataOutputStream (fos);
	// 		if (records != null) {
	// 			for (int i = 0; i < total; i++) {
	// 				for (int c = 0; c < 7; c++) {
	// 					dos.writeUTF (records[i][c]);
	// 					if (records[i][c] == null) break;
	// 				}
	// 			}
	// 			JOptionPane.showMessageDialog (this, "The File is Updated Successfully",
	// 					"BankSystem - Record Saved", JOptionPane.PLAIN_MESSAGE);
	// 			txtClear ();
	// 			dos.close();
	// 			fos.close();
	// 		}
	// 	}
	// 	catch (IOException ioe) {
	// 		JOptionPane.showMessageDialog (this, "There are Some Problem with File",
	// 					"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
	// 	}
	
	// }

	//Function use to load all Records from File when Application Execute.
	// void populateArray () {

	// 	try {
	// 		fis = new FileInputStream ("Bank.dat");
	// 		dis = new DataInputStream (fis);
	// 		//Loop to Populate the Array.
	// 		while (true) {
	// 			for (int i = 0; i < 7; i++) {
	// 				records[rows][i] = dis.readUTF ();
	// 			}
	// 			rows++;
	// 		}
	// 	}
	// 	catch (Exception ex) {
	// 		total = rows;
	// 		if (total == 0) {
	// 			JOptionPane.showMessageDialog (null, "Records File is Empty.\nEnter Records First to Display.",
	// 						"BankSystem - EmptyFile", JOptionPane.PLAIN_MESSAGE);
	// 			btnEnable ();
	// 		}
	// 		else {
	// 			try {
	// 				dis.close();
	// 				fis.close();
	// 			}
	// 			catch (Exception exp) { }
	// 		}
	// 	}

	// }

	// //Function use to Find Record by Matching the Contents of Records Array with ID TextBox.
	// void findRec () {

	// 	boolean found = false;
	// 	for (int x = 0; x < total; x++) {
	// 		if (records[x][0].equals (txtNo.getText())) {
	// 			found = true;
    //             oldPass = records[x][6];
    //             recNo = x;
	// 			showRec (x);
	// 			break;
	// 		}
	// 	}
	// 	if (found == false) {
	// 		JOptionPane.showMessageDialog (this, "Account No. " + txtNo.getText () + " doesn't Exist.",
	// 						"BankSystem - WrongNo", JOptionPane.PLAIN_MESSAGE);
	// 		txtClear ();
	// 	}

	// }

	//Function which display Record from Array onto the Form.
	public void showRec (int intRec) {

		txtNo.setText (records[intRec][0]);
	}

	//Function use to Clear all TextFields of Window.
	void txtClear () {

		txtNo.setText ("");
		txtNo.requestFocus ();
        txtOldPass.setText("");
        txtPass.setText("");
	}

	//Function use to Lock Controls of Window.
	void btnEnable () {

		txtNo.setEnabled (false);
		btnChange.setEnabled (false);

	}

}	