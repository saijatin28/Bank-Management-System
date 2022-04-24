import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 

public class FundTransfer extends JInternalFrame implements ActionListener{
	private JPanel jpTransfer = new JPanel();
    private JLabel lbNo, lbPass, lbTo, lbAmt, lbDate;
    private JTextField txtNo, txtPass, txtTo, txtAmt;
    private JComboBox cboMode;
    private JButton btnSubmit, btnCancel, btnSubTrans;
    private String sender;
   

    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/test";  

    //  Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 

    FundTransfer () {
        super ("Transfer Funds", false, true, false, true);
		setSize (350, 235);
        String modes[] = {"NEFT", "IMPS", "RTGS"} ;
        cboMode = new JComboBox(modes);

		jpTransfer.setLayout (null);
        
        lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
	    lbPass = new JLabel ("Password:");
		lbPass.setForeground (Color.black);
		lbPass.setBounds (15, 55, 80, 25);
        lbTo = new JLabel("To:");
        lbTo.setForeground(Color.black);
        lbTo.setBounds(15, 20, 80, 25);
        lbAmt = new JLabel("Amount:");
        lbAmt.setForeground(Color.black);
        lbAmt.setBounds(15, 55, 80, 25);
        lbDate = new JLabel ("Type:");
        lbDate.setForeground (Color.black);
        lbDate.setBounds (15, 90, 80, 25);

        txtTo = new JTextField();
        txtTo.setHorizontalAlignment(JTextField.RIGHT);
        txtTo.setBounds(125, 20, 200, 25);
        txtAmt = new JTextField();
        txtAmt.setHorizontalAlignment(JTextField.RIGHT);
        txtAmt.setBounds(125,55,200,25);
        txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		txtNo.setBounds (125, 20, 200, 25);
		txtPass = new JTextField ();
		txtPass.setHorizontalAlignment (JTextField.RIGHT);
		txtPass.setBounds (125, 55, 200, 25);
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

        btnSubmit = new JButton ("Submit");
		btnSubmit.setBounds (20, 165, 120, 25);
		btnSubmit.addActionListener (this);
        btnSubTrans = new JButton ("Submit");
		btnSubTrans.setBounds (20, 165, 120, 25);
		btnSubTrans.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (200, 165, 120, 25);
		btnCancel.addActionListener (this);


        jpTransfer.add(btnSubmit);
        jpTransfer.add(btnCancel);
        jpTransfer.add(txtPass);
        jpTransfer.add(txtNo);
        jpTransfer.add(lbPass);
        jpTransfer.add(lbNo);
        

        getContentPane().add(jpTransfer);
        setVisible(true);
    }

    public void actionPerformed (ActionEvent ae) {
        Object obj = ae.getSource();

        if (obj == btnSubTrans) {
            String acno = txtTo.getText(), amt = txtAmt.getText();

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
                sql = "select acno from data where acno = '"+acno+"';";
                var rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    sql = "select deposit from data where acno='"+sender+"';";
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String curamt = rs.getString(1);
                        int ca = Integer.parseInt(curamt);
                        int am = Integer.parseInt(amt);
                        if (ca < am) {
                            JOptionPane.showMessageDialog (this, "Insufficient funds!",
                            "BankSystem - No Funds", JOptionPane.PLAIN_MESSAGE);
                            setVisible(false);
                            dispose();
                        }
                        else {
                            sql = "update data set deposit='"+Integer.toString(ca-am)+"' where acno='"+sender+"';";
                            stmt.executeUpdate(sql);         
                            
                            sql = "select deposit from data where acno='"+acno+"';";
                            rs = stmt.executeQuery(sql);
                            int rcvbal=0;
                            if (rs.next()) {
                            rcvbal = Integer.parseInt(rs.getString(1)); }
                            sql = "update data set deposit='"+Integer.toString(rcvbal+am)+"' where acno='"+acno+"';";
                            stmt.executeUpdate(sql);
                            JOptionPane.showMessageDialog (this, "Funds Transferred Successfully.",
                            "BankSystem - Success", JOptionPane.PLAIN_MESSAGE);       
                            sql = "insert into bs(acno, type, amt) values('"+acno+"', 'deposit', '"+Integer.toString(am)+"');";
                            stmt.executeUpdate(sql);
                            sql = "insert into bs(acno, type, amt) values('"+sender+"', 'withdraw', '"+Integer.toString(am)+"');";
                            stmt.executeUpdate(sql);            
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog (this, "Account Doesn't exist",
                    "BankSystem - No Account", JOptionPane.PLAIN_MESSAGE);
                }
                stmt.close();
                conn.close();
            } catch(Exception e) {e.printStackTrace();}
        }

        else if (obj == btnCancel) {
            setVisible(false);
            dispose();
        }

        else if (obj == btnSubmit) {
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
                String acno = txtNo.getText(), pass = txtPass.getText();
                sender = acno;
                sql = "select pass from data where acno='"+acno+"';";
                var rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    if (rs.getString(1).equals(pass)) {
                        jpTransfer.remove(lbPass);
                        jpTransfer.remove(lbNo);
                        jpTransfer.remove(txtPass);
                        jpTransfer.remove(txtNo);
                        jpTransfer.add(lbDate);
                        jpTransfer.remove(btnSubmit);
                        jpTransfer.add(btnSubTrans);
                        cboMode.setBounds(125, 90, 92, 25);
                        jpTransfer.add(cboMode);
                        jpTransfer.add(lbTo);
                        jpTransfer.add(txtTo);
                        jpTransfer.add(lbAmt);
                        jpTransfer.add(txtAmt);
                        jpTransfer.repaint();  
                    }
                    else {
                        JOptionPane.showMessageDialog (this, "Incorrect Password!",
                        "BankSystem - Wrong Password", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog (this, "Account Doesn't exist",
                    "BankSystem - No Account", JOptionPane.PLAIN_MESSAGE);
                }
                //stmt.executeUpdate(sql);
    //			System.out.println("Created table in given database..."); 
                
                // STEP 4: Clean-up environment 
                stmt.close(); 
                conn.close(); 
             //   setVisible (false);
             //   dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }
    
}