import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
public class CardManagement extends JInternalFrame implements ActionListener {

    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/test";  

    //  Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 
    private JPanel jpCard = new JPanel();
    private JLabel lbNo, lbPass, lbCardNo, lbPin, lbLimit, lbEnabled;
    private JTextField txtNo, txtPass, txtCardNo, txtPin, txtLimit, txtEnabled;
    private JButton btnSubmit, btnCancel, btnSubmitCard, btnModify;
  //  private String records[][] = new String [500][7];
//	private FileInputStream fis;
//	private DataInputStream dis;
    private int rows = 0, total = 0, recNo=-1;
    private String pass;

    CardManagement() {
        super ("Manage Card", false, true, false, true);
        setSize (350, 350);

		jpCard.setLayout (null);

        lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
        lbPass = new JLabel ("Password:");
		lbPass.setBounds (15, 55, 80, 25);
        lbPass.setForeground(Color.black);
        lbCardNo = new JLabel ("Card No:");
		lbCardNo.setBounds (15, 55, 80, 25);
        lbCardNo.setForeground(Color.black);
        lbPin = new JLabel ("Pin:");
		lbPin.setBounds (15, 90, 80, 25);
        lbPin.setForeground(Color.black);
        lbLimit = new JLabel ("Limit:");
		lbLimit.setBounds (15, 120, 80, 25);
        lbLimit.setForeground(Color.black);
        lbEnabled = new JLabel ("Enabled:");
		lbEnabled.setBounds (15, 150, 80, 25);
        lbEnabled.setForeground(Color.black);

        txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		txtNo.setBounds (125, 20, 200, 25);
		txtPass = new JTextField ();
		txtPass.setBounds (125, 55, 200, 25);
        txtCardNo = new JTextField();
        txtCardNo.setBounds(125, 55, 200, 25);
        txtPin = new JTextField();
        txtPin.setBounds(125, 90, 200, 25);
        
        txtLimit = new JTextField();
        txtLimit.setBounds(125, 120, 200, 25);
        txtEnabled = new JTextField();
        txtEnabled.setBounds(125, 150, 200, 25);

        btnSubmit = new JButton ("Submit");
		btnSubmit.setBounds (20, 120, 120, 25);
		btnSubmit.addActionListener (this);
        btnSubmitCard = new JButton ("Submit");
		btnSubmitCard.setBounds (20, 185, 120, 25);
		btnSubmitCard.addActionListener (this);
        btnModify = new JButton ("Modify");
		btnModify.setBounds (20, 185, 120, 25);
		btnModify.addActionListener (this);
        
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (200, 120, 120, 25);
		btnCancel.addActionListener (this);

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

        jpCard.add (lbNo);
		jpCard.add (txtNo);
		jpCard.add(lbPass);
        jpCard.add(txtPass);
        jpCard.add(btnSubmit);
        jpCard.add(btnCancel);
       

        getContentPane().add (jpCard);
        setVisible (true);
        //populateArray();

    }

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
        if (obj == btnModify) {
            txtPin.setEnabled(true);
            txtLimit.setEnabled(true);
            txtEnabled.setEnabled(true);
            btnSubmitCard.setEnabled(true);
        }
        if (obj == btnSubmitCard) {
            if (txtLimit.getText().equals("")) {
                JOptionPane.showMessageDialog (this, "Please enter a limit!",
                "BankSystem - No Limit Entered", JOptionPane.PLAIN_MESSAGE);
            }
            else if (txtPin.getText().equals("")) {
                JOptionPane.showMessageDialog (this, "Please enter a pin!",
                "BankSystem - No Pin", JOptionPane.PLAIN_MESSAGE);
            }
            else if (txtEnabled.getText().equals("")) {
                JOptionPane.showMessageDialog (this, "Please enter t/f for enabled!",
                "BankSystem - enabled/disabled not specified", JOptionPane.PLAIN_MESSAGE);
            }
            else {
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
                    sql = "select * from carddata where acno='"+txtNo.getText()+"';";
                    var rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        sql = "update carddata set lmt='"+txtLimit.getText()+"', pin='"+txtPin.getText()+"', enabled='"+txtEnabled.getText()+"' where acno='"+txtNo.getText()+"';";
                    }
                    else {
                        sql =  "insert into CardData(acno, lmt, pin, enabled) values ('"+txtNo.getText()+"','"+txtLimit.getText()+"','"+txtPin.getText()+"','"+txtEnabled.getText()+"');";  
                    }
                    stmt.executeUpdate(sql);
        //			System.out.println("Created table in given database..."); 
                    
                    // STEP 4: Clean-up environment 
                    stmt.close(); 
                    conn.close(); 
                    JOptionPane.showMessageDialog (this, "Successfully updated card details!",
							"BankSystem - Success", JOptionPane.PLAIN_MESSAGE);
                    txtClear ();
                    setVisible (false);
                    dispose();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
            }
        }
        if (obj == btnSubmit) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Id of Customer to Search.",
							"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			}
			else {
                String acc = txtNo.getText();
                loadCardData();
                //findRec();
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
                        pass = rs.getString(1);
                    }
        //			System.out.println("Created table in given database..."); 
                    
                    // STEP 4: Clean-up environment 
                    stmt.close(); 
                    conn.close(); 
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                if (!txtPass.getText().equals(pass)) {
                    JOptionPane.showMessageDialog (this, "Incorrect Password Entered!",
                    "BankSystem - Incorrect Password", JOptionPane.PLAIN_MESSAGE);
                    txtNo.requestFocus();
                }
                else {
                    jpCard.remove(txtPass);
                    jpCard.remove(lbPass);
                    jpCard.remove(btnSubmit);
                    txtNo.setEnabled(false);
                    txtNo.setText(acc);
                    txtCardNo.setEnabled(false);
                    txtCardNo.setText(acc);
                    jpCard.add(lbLimit);
                    jpCard.add(lbCardNo);
                    jpCard.add(lbEnabled);
                    jpCard.add(lbPin);
                    jpCard.add(txtLimit);
                    jpCard.add(txtCardNo);
                    jpCard.add(txtEnabled);
                    jpCard.add(txtPin);
                    btnCancel.setBounds(200, 185, 120, 25);
                    if (recNo == -1) { 
                        // No Card
                        jpCard.add(btnSubmitCard);
                        jpCard.repaint();
                    }
                    else {
                        txtPin.setEnabled(false);
                        txtLimit.setEnabled(false);
                        txtEnabled.setEnabled(false);
                        jpCard.add(btnModify);
                        jpCard.add(btnSubmitCard);
                        btnSubmitCard.setEnabled(false);
                        btnSubmitCard.setBounds(110, 250, 120, 25);
                        jpCard.repaint();
                    }   
                }
			}
		}
		if (obj == btnCancel) {
			txtClear ();
			setVisible (false);
			dispose();
		}
    }

    void loadCardData () {
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
            String sql =  "select * from carddata where acno='"+txtNo.getText()+"';";
            var rs = stmt.executeQuery(sql);
            if (rs.next()) {
                recNo = 0;
                txtLimit.setText(rs.getString(2));
                txtPin.setText(rs.getString(3));
                txtEnabled.setText(rs.getString(4));
            }
            else {
                recNo = -1;
                JOptionPane.showMessageDialog (this, "No card for ac. " + txtNo.getText () + " exists. Create new Card.",
                "BankSystem - No Card", JOptionPane.PLAIN_MESSAGE);
            }
//			System.out.println("Created table in given database..."); 
            
            // STEP 4: Clean-up environment 
            stmt.close(); 
            conn.close(); 
         } catch (Exception e) {
             e.printStackTrace();
         }


        // try {
        //     FileReader reader = new FileReader("CardData.txt");
        //     BufferedReader bufferedReader = new BufferedReader(reader);
 
        //     String line;
        //     Boolean found=false;
 
        //     while ((line = bufferedReader.readLine()) != null) {
        //         String arr[] = line.split(" ", 100);
        //         String acno = arr[0];
        //         String pin = arr[1];
        //         String limit = arr[2];
        //         String enabled = arr[3];
        //         if (acno.equals(txtNo.getText())){
        //            txtLimit.setText(limit);
        //            txtPin.setText(pin);
        //            txtEnabled.setText(enabled);
        //            found = true;
        //            break;
        //         }
        //     }
        //     if (!found) {
        //         recNo = -1;
        //         JOptionPane.showMessageDialog (this, "No card for ac. " + txtNo.getText () + " exists. Create new Card.",
        //         "BankSystem - No Card", JOptionPane.PLAIN_MESSAGE);
        //     }
        //     reader.close();
 
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }


    // void findRec () {

	// 	boolean found = false;
	// 	for (int x = 0; x < total; x++) {
	// 		if (records[x][0].equals (txtNo.getText())) {
	// 			found = true;
    //             pass = records[x][6];
    //             recNo = x;
	// 			break;
	// 		}
	// 	}
	// 	if (found == false) {
	// 		JOptionPane.showMessageDialog (this, "Account No. " + txtNo.getText () + " doesn't Exist.",
	// 						"BankSystem - WrongNo", JOptionPane.PLAIN_MESSAGE);
	// 		txtClear ();
	// 	}

	// }
    void txtClear () {

		txtNo.setText ("");
		txtNo.requestFocus ();
        txtPass.setText("");
	}
}