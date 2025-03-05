package banking;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import banking.db.Database;
import banking.model.Customer;

import java.io.*;

public class FindAccount extends JInternalFrame implements ActionListener {

	private JPanel jpFind = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbBal;
	private JTextField txtNo, txtName, txtDate, txtBal;
	private JButton btnFind, btnCancel;

	private Database db = new Database();

	FindAccount() {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super("Search Customer [By No.]", false, true, false, true);
		setSize(350, 235);

		jpFind.setLayout(null);

		lbNo = new JLabel("Account No:");
		lbNo.setForeground(Color.black);
		lbNo.setBounds(15, 20, 80, 25);
		lbName = new JLabel("Person Name:");
		lbName.setForeground(Color.black);
		lbName.setBounds(15, 55, 80, 25);
		lbDate = new JLabel("Last Transaction:");
		lbDate.setForeground(Color.black);
		lbDate.setBounds(15, 90, 100, 25);
		lbBal = new JLabel("Balance:");
		lbBal.setForeground(Color.black);
		lbBal.setBounds(15, 125, 80, 25);

		txtNo = new JTextField();
		txtNo.setHorizontalAlignment(JTextField.RIGHT);
		txtNo.setBounds(125, 20, 200, 25);
		txtName = new JTextField();
		txtName.setEnabled(false);
		txtName.setBounds(125, 55, 200, 25);
		txtDate = new JTextField();
		txtDate.setEnabled(false);
		txtDate.setBounds(125, 90, 200, 25);
		txtBal = new JTextField();
		txtBal.setHorizontalAlignment(JTextField.RIGHT);
		txtBal.setEnabled(false);
		txtBal.setBounds(125, 125, 200, 25);

		// Restricting The User Input to only Numerics.
		txtNo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
				if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep();
					ke.consume();
				}
			}
		});

		// Aligning The Buttons.
		btnFind = new JButton("Search");
		btnFind.setBounds(20, 165, 120, 25);
		btnFind.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(200, 165, 120, 25);
		btnCancel.addActionListener(this);

		// Adding the All the Controls to Panel.
		jpFind.add(lbNo);
		jpFind.add(txtNo);
		jpFind.add(lbName);
		jpFind.add(txtName);
		jpFind.add(lbDate);
		jpFind.add(txtDate);
		jpFind.add(lbBal);
		jpFind.add(txtBal);
		jpFind.add(btnFind);
		jpFind.add(btnCancel);

		// Adding Panel to Window.
		getContentPane().add(jpFind);

		db.populateArray(); // Load All Existing Records in Memory.

		// In the End Showing the New Account Window.
		setVisible(true);

	}

	// Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnFind) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Please! Provide Id of Customer to Search.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			} else {
				db.populateArray();
				findRec();
			}
		}
		if (obj == btnCancel) {
			txtClear();
			setVisible(false);
			dispose();
		}

	}

//	// Function use to Find Record by Matching the Contents of Records Array with ID
//	// TextBox.
	void findRec() {
		int fi = db.findRec(txtNo.getText());
		if (fi > -1)
			showRec(db.get(fi));
		else {
			JOptionPane.showMessageDialog(this, "Account No. " + txtNo.getText() + " doesn't Exist.",
					"BankSystem - WrongNo", JOptionPane.PLAIN_MESSAGE);
			txtClear();
		}
	}

	// Function which display Record from Array onto the Form.
	public void showRec(Customer records) {
		txtNo.setText(records.getNo());
		txtName.setText(records.getName());
		txtDate.setText(records.getDate());
		txtBal.setText(records.getBalance()+"");

	}

	// Function use to Clear all TextFields of Window.
	void txtClear() {

		txtNo.setText("");
		txtName.setText("");
		txtDate.setText("");
		txtBal.setText("");
		txtNo.requestFocus();

	}

	// Function use to Lock Controls of Window.
	void btnEnable() {

		txtNo.setEnabled(false);
		btnFind.setEnabled(false);

	}

}