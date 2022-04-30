import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 

public class LoanManagement extends JInternalFrame implements ActionListener {
    
    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/test";  

    //  Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 
    private JPanel jpLoan = new JPanel();
    private JLabel lbNo, lbPass, lbPrinci, lbTot, lbEmi, lbIntr, lbTenure, lbRate;
    private JTextField txtNo, txtPass, txtPrinci, txtTot, txtEmi, txtIntr, txtTenure, txtRate;
    private JButton btnSubmit, btnCancel, btnExisting, btnNew, btnBack, btnNext, btnCalc, btnConf;
    private int i=0, recCount=0;
    private String acno="";
    // acno, principal, total payable, emi, total interest, tenure
    private String[][] loans = new String[10][6];

    LoanManagement() {
        	//super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super ("Loan Management", false, true, false, true);
		setSize (350, 300);

		jpLoan.setLayout (null);

		lbNo = new JLabel ("Account No:");
		lbNo.setForeground (Color.black);
		lbNo.setBounds (15, 20, 80, 25);
        lbPass = new JLabel ("Password:");
		lbPass.setForeground (Color.black);
		lbPass.setBounds (15, 55, 80, 25);
        lbPrinci = new JLabel("Principal:");
        lbPrinci.setForeground(Color.black);
        lbPrinci.setBounds(15, 20, 80, 25);
        lbTot = new JLabel("Payable:");
        lbTot.setForeground(Color.black);
        lbTot.setBounds(15, 55, 80, 25);
        lbIntr = new JLabel("Interest:");
        lbIntr.setForeground(Color.black);
        lbIntr.setBounds(15, 90, 80, 25);
        lbEmi = new JLabel("EMI:");
        lbEmi.setForeground(Color.black);
        lbEmi.setBounds(15, 120, 80, 25);
        lbTenure = new JLabel("Tenure");
        lbTenure.setForeground(Color.black);
        lbTenure.setBounds(15, 150, 80, 25);
        lbRate = new JLabel("rate:");
        lbRate.setForeground(Color.black);
        lbRate.setBounds(15, 180, 80, 25);

        txtRate = new JTextField();
        txtRate.setHorizontalAlignment(JTextField.RIGHT);
        txtRate.setBounds(125, 180, 200, 25);
        txtPrinci = new JTextField();
        txtPrinci.setHorizontalAlignment(JTextField.RIGHT);
        txtPrinci.setBounds(125, 20, 200, 25);
        txtTot = new JTextField();
        txtTot.setHorizontalAlignment(JTextField.RIGHT);
        txtTot.setBounds(125, 55, 200, 25);
        txtIntr = new JTextField();
        txtIntr.setHorizontalAlignment(JTextField.RIGHT);
        txtIntr.setBounds(125, 90, 200, 25);
        txtEmi = new JTextField();
        txtEmi.setHorizontalAlignment(JTextField.RIGHT);
        txtEmi.setBounds(125, 120, 200, 25);
        txtTenure = new JTextField();
        txtTenure.setHorizontalAlignment(JTextField.RIGHT);
        txtTenure.setBounds(125, 150, 200, 25);
		txtNo = new JTextField ();
		txtNo.setHorizontalAlignment (JTextField.RIGHT);
		txtNo.setBounds (125, 20, 200, 25);
		txtPass = new JTextField ();
		txtPass.setHorizontalAlignment (JTextField.RIGHT);
		txtPass.setBounds (125, 55, 200, 25);
        btnSubmit = new JButton ("Submit");
		btnSubmit.setBounds (20, 165, 120, 25);
		btnSubmit.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (200, 165, 120, 25);
		btnCancel.addActionListener (this);
        btnExisting = new JButton("Exisiting Loan(s)");
        btnExisting.setBounds(70, 45, 200, 25);
        btnExisting.addActionListener(this);
        btnNew = new JButton("New Loan");
        btnNew.setBounds(70, 85, 200, 25);
        btnNew.addActionListener(this);
        btnBack = new JButton ("<");
		btnBack.setBounds (65, 220, 50, 25);
		btnBack.addActionListener (this);
		btnNext = new JButton (">");
		btnNext.setBounds (225, 220, 50, 25);
		btnNext.addActionListener (this);
        btnCalc = new JButton("Calculate");
        btnCalc.setBounds(25, 240, 120, 25);
        btnCalc.addActionListener(this);
        btnConf = new JButton("Confirm");
        btnConf.setBounds(185, 240, 120, 25);
        btnConf.addActionListener(this);

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
        jpLoan.add(txtNo);
        jpLoan.add(txtPass);
        jpLoan.add(lbPass);
        jpLoan.add(lbNo);
        jpLoan.add(btnSubmit);
        jpLoan.add(btnCancel);

        getContentPane().add (jpLoan);

        setVisible (true);

    }

    public void actionPerformed (ActionEvent ae) {
        Object obj = ae.getSource();
        
        if (obj == btnCalc) {
            input(Double.parseDouble(txtPrinci.getText()), Integer.parseInt(txtTenure.getText()));
        }

        else if (obj == btnConf) {
            Connection conn = null; 
			Statement stmt = null; 
			try { 
				Class.forName(JDBC_DRIVER); 
				
				conn = DriverManager.getConnection(DB_URL,USER,PASS);  
				
				stmt = conn.createStatement(); 
				String sql;
				sql = "insert into loan(acno, principal, total, emi, intr, tenure) values('"+acno+"', '"+txtPrinci.getText()+"', '"+txtTot.getText()+"', '"+txtEmi.getText()+"', '"+txtIntr.getText()+"', '"+txtTenure.getText()+"');";
				stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog (this, "Loan successfully added!",
                "BankSystem - Success", JOptionPane.PLAIN_MESSAGE);
				
				stmt.close(); 
				conn.close(); 
			
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
        }

        else if (obj == btnNew) {
            jpLoan.removeAll();
            jpLoan.add(txtPrinci);
            jpLoan.add(txtTot);
            jpLoan.add(txtEmi);
            jpLoan.add(txtIntr);
            jpLoan.add(txtTenure);
            jpLoan.add(lbPrinci);
            jpLoan.add(lbTot);
            jpLoan.add(lbEmi);
            jpLoan.add(lbIntr);
            jpLoan.add(lbTenure);
            jpLoan.add(btnCalc);
            jpLoan.add(btnConf);
            jpLoan.add(txtRate);
            jpLoan.add(lbRate);
            txtTot.setEnabled(false);
            txtEmi.setEnabled(false);
            txtIntr.setEnabled(false);
            txtRate.setEnabled(false);
            jpLoan.repaint();
        }

        else if (obj == btnExisting) {
            recCount=0;
            jpLoan.removeAll();
            jpLoan.add(txtPrinci);
            jpLoan.add(txtTot);
            jpLoan.add(txtEmi);
            jpLoan.add(txtIntr);
            jpLoan.add(txtTenure);
            jpLoan.add(lbPrinci);
            jpLoan.add(lbTot);
            jpLoan.add(lbEmi);
            jpLoan.add(lbIntr);
            jpLoan.add(lbTenure);
            jpLoan.add(txtRate);
            jpLoan.add(lbRate);
            jpLoan.add(btnNext);
            jpLoan.add(btnBack);
            txtTot.setEnabled(false);
            txtEmi.setEnabled(false);
            txtIntr.setEnabled(false);
            txtRate.setEnabled(false);
            txtPrinci.setEnabled(false);
            txtTenure.setEnabled(false);
            jpLoan.repaint();
            Connection conn = null; 
			Statement stmt = null; 
			try { 
				Class.forName(JDBC_DRIVER); 
				
				conn = DriverManager.getConnection(DB_URL,USER,PASS);  
				
				stmt = conn.createStatement(); 
				String sql;
				sql = "select * from loan where acno='"+txtNo.getText()+"';";
				var rs = stmt.executeQuery(sql);
				i = 0;
				while (rs.next()) {
                        // acno, principal, total payable, emi, total interest, tenure
                    loans[i][0] = rs.getString(1);
                    loans[i][1] = rs.getString(2);
                    loans[i][2] = rs.getString(3);
                    loans[i][3] = rs.getString(4);
                    loans[i][4] = rs.getString(5);
                    loans[i][5] = rs.getString(6);
                    i+=1;
				}
				showRec(0);

				stmt.close(); 
				conn.close(); 
			
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
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
				sql = "select pass from data where acno='"+txtNo.getText()+"';";
				var rs = stmt.executeQuery(sql);
				acno = txtNo.getText();
				if (rs.next()) {
					String pass = rs.getString(1);
					if (!pass.equals(txtPass.getText())) {
						JOptionPane.showMessageDialog (this, "Incorrect password!",
						"BankSystem - Wrong Password", JOptionPane.PLAIN_MESSAGE);
					}
					else {
						jpLoan.removeAll();
                        jpLoan.add(btnNew);
                        jpLoan.add(btnExisting);
                        jpLoan.repaint();
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
			if (recCount == i) {
				recCount = i - 1;
				showRec (recCount);
				JOptionPane.showMessageDialog (this, "You are on Last Record.",
						"BankSystem - End of Records", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				showRec (recCount);
			}
		}

        else if (obj == btnCancel) {
            txtClear ();
			setVisible (false);
			dispose();
        }
    }
    void showRec(int rc) {
            // acno, principal, total payable, emi, total interest, tenure

        txtNo.setText(loans[rc][0]);
        txtPrinci.setText(loans[rc][1]);
        txtTot.setText(loans[rc][2]);
        txtEmi.setText(loans[rc][3]);
        txtIntr.setText(loans[rc][4]);
        txtTenure.setText(loans[rc][5]);
        txtRate.setText("12");
    }
    void txtClear () {

		txtNo.setText ("");
		txtPass.setText("");
		txtNo.requestFocus ();

	}
    static double PercentRatePerAnum = 12;
	
	public void input(double principalAmount, int loanTenure)
	{
		double totalAmount = principalAmount + (principalAmount * (PercentRatePerAnum/100));

		emi(principalAmount, loanTenure, totalAmount);
		
	}

	public void emi(double principalAmount, int loanTenure, double totalAmount)
	{

		double ratePerMonth = PercentRatePerAnum/(12*100) ;
		double rPlusOne = ratePerMonth +1;
		double ratePow = 1.0;

		for(int i = 0; i<=loanTenure; i++)
		{
			ratePow = ratePow * rPlusOne;
		}
				
		double totalEMI = (totalAmount * ratePerMonth * ratePow)/(ratePow - 1);
		
        txtRate.setText(String.format("%.2f",PercentRatePerAnum));
        txtEmi.setText(String.format("%.2f",totalEMI));
        txtIntr.setText(String.format("%.2f",totalEMI*loanTenure-principalAmount));
        txtTot.setText(String.format("%.2f", totalEMI*loanTenure));
	}
}
