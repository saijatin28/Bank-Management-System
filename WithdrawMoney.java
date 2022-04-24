import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
public class WithdrawMoney extends JInternalFrame implements ActionListener {

    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/test";  

    //  Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 
	private JPanel jpWith = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbWithdraw, lbPass;
	private JTextField txtNo, txtName, txtWithdraw, txtPass;
	private JComboBox cboMonth, cboDay, cboYear;
	private JButton btnSave, btnCancel;

	private int recCount = 0;
	private int rows = 0;
	private	int total = 0;
	private	int curr;
	private	int withdraw;
	private String passw, pass;

	// //String Type Array use to Load Records From File.
	// private String records[][] = new String [500][7];

	// private FileInputStream fis;
	// private DataInputStream dis;

	WithdrawMoney () {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Withdraw Money", false, true, false, true);
		setSize (400, 300);

		jpWith.setLayout (null);

		lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
	        lbName = new JLabel ("Person Name:");
		lbName.setForeground (Color.black);
	        lbName.setBounds (15, 55, 80, 25);
		lbDate = new JLabel ("With. Date:");
		lbDate.setForeground (Color.black);
		lbDate.setBounds (15, 90, 80, 25);
		lbWithdraw = new JLabel ("With. Amount:");
		lbWithdraw.setForeground (Color.black);
		lbWithdraw.setBounds (15, 125, 80, 25);
		lbPass = new JLabel ("Acc. Pass:");
		lbPass.setForeground (Color.black);
		lbPass.setBounds (15, 160, 80, 25);



		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		//Checking the Accunt No. Provided By User on Lost Focus of the TextBox.
		txtNo.addFocusListener (new FocusListener () {
			public void focusGained (FocusEvent e) { }
			public void focusLost (FocusEvent fe) {
				if (txtNo.getText().equals ("")) { }
				else {
					rows = 0;

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
					sql = "select name from data where acno='"+txtNo.getText()+"';";
					var rs = stmt.executeQuery(sql);
					if (rs.next()) {
						String name = rs.getString(1);
						txtName.setText(name);
					}
					
		//			System.out.println("Created table in given database..."); 
					
					// STEP 4: Clean-up environment 
					stmt.close(); 
					conn.close(); 
					} catch (Exception e) {
						e.printStackTrace();
					}
//					populateArray ();	//Load All Existing Records in Memory.
//					findRec ();		//Finding if Account No. Already Exist or Not.
				}
			}
		}
		);
		txtNo.setBounds (105, 20, 205, 25);

		txtName = new JTextField ();
		txtName.setEnabled (false);
		txtName.setBounds (105, 55, 205, 25);
		txtWithdraw = new JTextField ();
		txtWithdraw.setHorizontalAlignment (JTextField.RIGHT);
		txtWithdraw.setBounds (105, 125, 205, 25);
		txtPass = new JTextField ();
		txtPass.setHorizontalAlignment (JTextField.RIGHT);
		txtPass.setBounds (105, 160, 205, 25);
		

		//Restricting The User Input to only Numerics in Numeric TextBoxes.
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
		txtWithdraw.addKeyListener (new KeyAdapter() {
			public void keyTyped (KeyEvent ke) {
				char c = ke.getKeyChar ();
				if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep ();
					ke.consume ();
      				}
    			}
  		}
		);

		//Creating Date Option.
		String Months[] = {"January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"};
		cboMonth = new JComboBox(Months);
		cboDay = new JComboBox ();
		cboYear = new JComboBox ();
		for (int i = 1; i <= 31; i++) {
			String days = "" + i;
			cboDay.addItem (days);
		}
		for (int i = 2000; i <= 2015; i++) {
			String years = "" + i;
			cboYear.addItem (years);
		}

		//Aligning The Date Option Controls.
		cboMonth.setBounds (105, 90, 92, 25);
		cboDay.setBounds (202, 90, 43, 25);
		cboYear.setBounds (250, 90, 60, 25);

		//Aligning The Buttons.
		btnSave = new JButton ("Save");
		btnSave.setBounds (20, 190, 120, 25);
		btnSave.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (185, 190, 120, 25);
		btnCancel.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpWith.add (txtPass);
		jpWith.add (lbPass);
		jpWith.add (lbNo);
		jpWith.add (txtNo);
		jpWith.add (lbName);
		jpWith.add (txtName);
		jpWith.add (lbDate);
		jpWith.add (cboMonth);
		jpWith.add (cboDay);
		jpWith.add (cboYear);
		jpWith.add (lbWithdraw);
		jpWith.add (txtWithdraw);
		jpWith.add (btnSave);
		jpWith.add (btnCancel);

		//Adding Panel to Window.
		getContentPane().add (jpWith);


	//	populateArray ();	//Load All Existing Records in Memory.

		//In the End Showing the New Account Window.
		setVisible (true);

	}

	//Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed (ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnSave) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Id of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			}
			else if (txtWithdraw.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Withdraw Amount.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtWithdraw.requestFocus ();
			}
			else {
				withdraw = Integer.parseInt (txtWithdraw.getText ());
				pass = txtPass.getText();
			
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
					sql = "select pass, deposit from data where acno='"+txtNo.getText()+"';";
					var rs = stmt.executeQuery(sql);
					if (rs.next()) {
						curr = Integer.parseInt(rs.getString(2));
						passw = rs.getString(1);
					}
					if (!passw.equals(pass)) {
						JOptionPane.showMessageDialog (this, "Incorrect Password Entered, Try Again!",
						"BankSystem - Incorrect Password", JOptionPane.PLAIN_MESSAGE);
						txtPass.setText ("");
						txtPass.requestFocus ();	
					}
					else if (withdraw > curr) {
						JOptionPane.showMessageDialog (this, "Withdraw Amount can't greater than Actual Balance.",
								"BankSystem - Large Amount", JOptionPane.PLAIN_MESSAGE);
						txtWithdraw.setText ("");
						txtWithdraw.requestFocus ();
					}
					else if (curr == 0) {
						JOptionPane.showMessageDialog (this, txtName.getText () + " doesn't have any Amount in Balance.",
								"BankSystem - EmptyAccount", JOptionPane.PLAIN_MESSAGE);
						txtClear ();
					}
					else {
						sql = "update data set deposit = '"+Integer.toString(curr-withdraw)+"' where acno='"+txtNo.getText()+"';";
						stmt.executeUpdate(sql);
						JOptionPane.showMessageDialog (this, "Withdraw success.",
								"BankSystem - Success", JOptionPane.PLAIN_MESSAGE);
						sql = "insert into bs(acno, type, amt) values('"+txtNo.getText()+"', 'withdraw', '"+txtWithdraw.getText()+"');";
						stmt.executeUpdate(sql);
					}
		//			System.out.println("Created table in given database..."); 
					
					// STEP 4: Clean-up environment 
					stmt.close(); 
					conn.close(); 
					} catch (Exception e) {
						e.printStackTrace();
					}
					// BankStatement st = new BankStatement();
					// st.addToStatment("withdraw", txtNo.getText(), txtWithdraw.getText());
					// editRec ();	//Update the Contents of Array.
				}
			}
		if (obj == btnCancel) {
			txtClear ();
			setVisible (false);
			dispose();
		}

	}

	// //Function use to load all Records from File when Application Execute.
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
	// 			showRec (x);
	// 			break;
	// 		}
	// 	}
	// 	if (found == false) {
	// 		String str = txtNo.getText ();
	// 		txtClear ();
	// 		JOptionPane.showMessageDialog (this, "Account No. " + str + " doesn't Exist.",
	// 					"BankSystem - WrongNo", JOptionPane.PLAIN_MESSAGE);
	// 	}

	// }

	// //Function which display Record from Array onto the Form.
	// public void showRec (int intRec) {

	// 	txtNo.setText (records[intRec][0]);
	// 	txtName.setText (records[intRec][1]);
	// 	curr = Integer.parseInt (records[intRec][5]);
	// 	passw = records[intRec][6];
	// 	recCount = intRec;

	// }

	//Function use to Clear all TextFields of Window.
	void txtClear () {

		txtNo.setText ("");
		txtName.setText ("");
		txtWithdraw.setText ("");
		txtNo.requestFocus ();
		txtPass.setText("");
	}

	// //Function use to Edit an Element's Value of the Array.
	// public void editRec () {

	// 	records[recCount][0] = txtNo.getText ();
	// 	records[recCount][1] = txtName.getText ();
	// 	records[recCount][2] = "" + cboMonth.getSelectedItem ();
	// 	records[recCount][3] = "" + cboDay.getSelectedItem ();
	// 	records[recCount][4] = "" + cboYear.getSelectedItem ();
	// 	records[recCount][5] = "" + (curr - withdraw);
	// 	editFile ();	//Save This Array to File.
	
	// // }

	// //Function use to Save Records to File After editing the Record of User Choice.
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
	// 				"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
	// 	}
	
	// }

	//Function use to Lock all Buttons of Window.
	void btnEnable () {

		txtNo.setEnabled (false);
		cboMonth.setEnabled (false);
		cboDay.setEnabled (false);
		cboYear.setEnabled (false);
		txtWithdraw.setEnabled (false);
		btnSave.setEnabled (false);

	}

}	