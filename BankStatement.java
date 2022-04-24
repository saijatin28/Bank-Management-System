import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
// load all transactions and display

public class BankStatement extends JInternalFrame implements ActionListener{

    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/test";  

    //  Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 
	private JPanel jpFind = new JPanel();
	private JLabel lbNo, lbPass, lbType, lbAmt;
	private JTextField txtNo, txtPass, txtAmt, txtType;
	private JButton btnSubmit, btnCancel, btnBack, btnNext;
	private int rows = 0;
	private	int total = 0, recCount=0, totRecs=0;
	private String records[][] = new String [500][7];
    private String[] allTrans = new String[100];

	private FileInputStream fis;
	private DataInputStream dis;
 
    BankStatement() {
        
		//super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Bank Statement", false, true, false, true);
		setSize (350, 235);

		jpFind.setLayout (null);

		lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
	    lbPass = new JLabel ("Password:");
		lbPass.setForeground (Color.black);
		lbPass.setBounds (15, 55, 80, 25);
	    lbType = new JLabel ("Type:");
		lbType.setForeground (Color.black);
		lbType.setBounds (15, 55, 80, 25);
        lbAmt = new JLabel ("Amount:");
		lbAmt.setForeground (Color.black);
		lbAmt.setBounds (15, 90, 80, 25);
	    

		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		txtNo.setBounds (125, 20, 200, 25);
		txtPass = new JTextField ();
		txtPass.setHorizontalAlignment (JTextField.RIGHT);
		txtPass.setBounds (125, 55, 200, 25);
        
        txtType = new JTextField ();
		txtType.setHorizontalAlignment (JTextField.RIGHT);
		txtType.setBounds (125, 55, 200, 25);
        txtType.setEnabled(false);
        txtAmt = new JTextField ();
		txtAmt.setHorizontalAlignment (JTextField.RIGHT);
		txtAmt.setBounds (125, 90, 200, 25);
        txtAmt.setEnabled(false);


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

		//Aligning The Buttons.
		btnSubmit = new JButton ("Submit");
		btnSubmit.setBounds (20, 165, 120, 25);
		btnSubmit.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (200, 165, 120, 25);
		btnCancel.addActionListener (this);
        btnBack = new JButton ("<");
		btnBack.setBounds (65, 165, 50, 25);
		btnBack.addActionListener (this);
		btnNext = new JButton (">");
		btnNext.setBounds (225, 165, 50, 25);
		btnNext.addActionListener (this);

		//Adding the All the Controls to Panel.
		jpFind.add (lbNo);
		jpFind.add (txtNo);
		jpFind.add (btnCancel);
        jpFind.add(btnSubmit);
        jpFind.add(lbPass);
        jpFind.add(txtPass);
        jpFind.add (btnBack);
		jpFind.add (btnNext);

		//Adding Panel to Window.
		getContentPane().add (jpFind);

	//	populateArray ();	//Load All Existing Records in Memory.

		//In the End Showing the New Account Window.
		setVisible (true);
    }
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
	// 			//btnEnable ();
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

    public void actionPerformed (ActionEvent ae) {
        Object obj = ae.getSource();
        if (obj == btnSubmit) {
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
				sql = "select pass from data where acno='"+txtNo.getText()+"';";
				var rs = stmt.executeQuery(sql);
				
				if (rs.next()) {
					String pass = rs.getString(1);
					if (!pass.equals(txtPass.getText())) {
						JOptionPane.showMessageDialog (this, "Incorrect password!",
						"BankSystem - Wrong Password", JOptionPane.PLAIN_MESSAGE);
					}
					else {
						txtNo.setEnabled(false);
						jpFind.remove(txtPass);
						jpFind.remove(lbPass);
						jpFind.remove(btnCancel);
						jpFind.remove(btnSubmit);
						jpFind.repaint();
						jpFind.add(txtType);
						jpFind.add(txtAmt);
						jpFind.add(lbAmt);
						jpFind.add(lbType);
						sql = "select * from bs where acno='"+txtNo.getText()+"';";
						rs = stmt.executeQuery(sql);
						int i = 0;
						while (rs.next()) {
							allTrans[i] = rs.getString(2)+" "+rs.getString(3);
							i++;
						}
						totRecs = i;
						showRec(0);
					}
				}
				else {
					JOptionPane.showMessageDialog (this, "Account doesn't exist!",
						"BankSystem - No Account", JOptionPane.PLAIN_MESSAGE);
				}
	//			System.out.println("Created table in given database..."); 
				
				// STEP 4: Clean-up environment 
				stmt.close(); 
				conn.close(); 
				// txtClear ();
				// setVisible (false);
				// dispose();
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
        }
        else if (obj == btnCancel) {
            txtClear ();
			setVisible (false);
			dispose();
        }
        else if (obj == btnBack) {
			recCount = recCount - 1;
			if (recCount < 0) {
				recCount = 0;
				showRec (recCount);
				JOptionPane.showMessageDialog (this, "You are on First Record.",
						"BankSystem - 1st Record", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				showRec (recCount);
			}
		}
        else if (obj == btnNext) {
			recCount = recCount + 1;
			if (recCount == totRecs) {
				recCount = totRecs - 1;
				showRec (recCount);
				JOptionPane.showMessageDialog (this, "You are on Last Record.",
						"BankSystem - End of Records", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				showRec (recCount);
			}
		}
    }
    void showRec(int rC) {
        String arr[] = allTrans[rC].split(" ", 100);
        String type = arr[0];
        String amt = arr[1];
        txtType.setText(type);
        txtAmt.setText(amt);
    }
    void txtClear () {

		txtNo.setText ("");
		txtPass.setText("");
		txtNo.requestFocus ();

	}


}
