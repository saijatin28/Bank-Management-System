import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
public class NewAccount extends JInternalFrame implements ActionListener {

	private JPanel jpInfo = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbDeposit, lbPass;
	private JTextField txtNo, txtName, txtDeposit, txtPass;
	private JComboBox cboMonth, cboDay, cboYear;
	private JButton btnSave, btnCancel;
 // JDBC driver name and database URL 
 static final String JDBC_DRIVER = "org.h2.Driver";   
 static final String DB_URL = "jdbc:h2:~/test";  
 
 //  Database credentials 
 static final String USER = "sa"; 
 static final String PASS = ""; 
	private int count = 0;
	private int rows = 0;
	// private	int total = 0;

	// //String Type Array use to Load Records From File.
	// private String records[][] = new String [500][7];

	// //String Type Array use to Save Records into File.
	// private String saves[][] = new String [500][7];

	// private FileInputStream fis;
	// private DataInputStream dis;

	NewAccount () {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Create New Account", false, true, false, true);
		//setSize (335, 235);
		setSize(400, 300);

		jpInfo.setBounds (0, 0, 500, 115);
		jpInfo.setLayout (null);

		lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
	        lbName = new JLabel ("Person Name:");
		lbName.setForeground (Color.black);
	        lbName.setBounds (15, 55, 80, 25);
		lbDate = new JLabel ("Deposit Date:");
		lbDate.setForeground (Color.black);
		lbDate.setBounds (15, 90, 80, 25);
		lbDeposit = new JLabel ("Dep. Amount:");
		lbDeposit.setForeground (Color.black);
		lbDeposit.setBounds (15, 125, 80, 25);
		lbPass = new JLabel("Password:");
		lbPass.setForeground (Color.black);
		lbPass.setBounds (15, 160, 80, 25);


		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		txtNo.setBounds (105, 20, 205, 25);
		txtName = new JTextField ();
		txtName.setBounds (105, 55, 205, 25);
		txtDeposit = new JTextField ();
		txtDeposit.setHorizontalAlignment (JTextField.RIGHT);
		txtDeposit.setBounds (105, 125, 205, 25);
		txtPass = new JTextField ();
		txtPass.setHorizontalAlignment(JTextField.RIGHT);
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
		txtDeposit.addKeyListener (new KeyAdapter() {
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
		cboMonth = new JComboBox (Months);
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
		btnSave.setBounds (20, 200, 120, 25);
		btnSave.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (185, 200, 120, 25);
		btnCancel.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpInfo.add(lbPass);
		jpInfo.add(txtPass);
		jpInfo.add (lbNo);
		jpInfo.add (txtNo);
		jpInfo.add (lbName);
		jpInfo.add (txtName);
		jpInfo.add (lbDate);
		jpInfo.add (cboMonth);
		jpInfo.add (cboDay);
		jpInfo.add (cboYear);
		jpInfo.add (lbDeposit);
		jpInfo.add (txtDeposit);
		jpInfo.add (btnSave);
		jpInfo.add (btnCancel);

		//Adding Panel to Window.
		getContentPane().add (jpInfo);

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
			else if (txtName.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Name of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtName.requestFocus ();
			}
			else if (txtDeposit.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Deposit Amount.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtDeposit.requestFocus ();
			}
			else if (txtPass.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Password of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtName.requestFocus ();
			}
			else {
				saveFile();
			//	BankStatement st = new BankStatement();
			//	st.addToStatment("depsoit", txtNo.getText(), txtDeposit.getText());
			//	populateArray ();	//Load All Existing Records in Memory.
			//	findRec ();		//Finding if Account No. Already Exist or Not.
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
	// 		if (total == 0) { }
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
	// 			JOptionPane.showMessageDialog (this, "Account No. " + txtNo.getText () + " is Already Exist.",
	// 						"BankSystem - WrongNo", JOptionPane.PLAIN_MESSAGE);
	// 			txtClear ();
	// 			break;
	// 		}
	// 	}
	// 	if (found == false) {
	// 		saveArray ();
	// 	}

	// }

	// //Function use to add new Element to Array.
	// void saveArray () {

	// 	saves[count][0] = txtNo.getText ();
	// 	saves[count][1] = txtName.getText ();
	// 	saves[count][2] = "" + cboMonth.getSelectedItem ();
	// 	saves[count][3] = "" + cboDay.getSelectedItem ();
	// 	saves[count][4] = "" + cboYear.getSelectedItem ();
	// 	saves[count][5] = txtDeposit.getText ();
	// 	saves[count][6] = txtPass.getText();
	// 	saveFile ();	//Save This Array to File.
	// 	count++;
	
	// }

	//Function use to Save new Record to the File.
	void saveFile () {
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
			String sql =  "insert into data(acno, name, mm, dd, yyyy, deposit, pass) values ('"+txtNo.getText()+"','"+txtName.getText()+"','"+cboMonth.getSelectedItem()+"','"+cboDay.getSelectedItem()+"','"+cboYear.getSelectedItem()+"','"+txtDeposit.getText()+"','"+txtPass.getText()+"');";  
			stmt.executeUpdate(sql);
//			System.out.println("Created table in given database..."); 
			
			// STEP 4: Clean-up environment 
			sql = "insert into bs(acno, type, amt) values('"+txtNo.getText()+"', 'deposit', '"+txtDeposit.getText()+"');";
			stmt.executeUpdate(sql);
			stmt.close(); 
			conn.close(); 
			JOptionPane.showMessageDialog (this, "Account No. " + txtNo.getText () + " created successfully.",
			"BankSystem - Success", JOptionPane.PLAIN_MESSAGE);
			
		 } catch (Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog (this, "Account No. " + txtNo.getText () + " is Already Exist.",
					"BankSystem - WrongNo", JOptionPane.PLAIN_MESSAGE);
		 }
		// try {
		// 	FileOutputStream fos = new FileOutputStream ("Bank.dat", true);
		// 	DataOutputStream dos = new DataOutputStream (fos);
		// 	dos.writeUTF (saves[count][0]);
		// 	dos.writeUTF (saves[count][1]);
		// 	dos.writeUTF (saves[count][2]);
		// 	dos.writeUTF (saves[count][3]);
		// 	dos.writeUTF (saves[count][4]);
		// 	dos.writeUTF (saves[count][5]);
		// 	dos.writeUTF (saves[count][6]);
		// 	JOptionPane.showMessageDialog (this, "The Record has been Saved Successfully",
		// 				"BankSystem - Record Saved", JOptionPane.PLAIN_MESSAGE);
		// 	txtClear ();
		// 	dos.close();
		// 	fos.close();
		// }
		// catch (IOException ioe) {
		// 	JOptionPane.showMessageDialog (this, "There are Some Problem with File",
		// 				"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
		//}

	}

	//Function use to Clear all TextFields of Window.
	void txtClear () {

		txtNo.setText ("");
		txtName.setText ("");
		txtDeposit.setText ("");
		txtPass.setText("");
		txtNo.requestFocus ();

	}
}	