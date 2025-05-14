package banking_oo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import banking_oo.model.Customer;
import banking_oo.repo.CustomerRepository;

import java.io.*;

public class DeleteCustomer extends JInternalFrame implements ActionListener {

	private JPanel jpDel = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbBal;
	private JTextField txtNo, txtName, txtDate, txtBal;
	private JButton btnDel, btnCancel;

	private CustomerRepository repo = CustomerRepository.getInstance();
	private Customer activeCustomer = null;

	DeleteCustomer() {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super("Delete Account Holder", false, true, false, true);
		setSize(350, 235);

		jpDel.setLayout(null);

		lbNo = new JLabel("Account No:");
		lbNo.setForeground(Color.black);
		lbNo.setBounds(15, 20, 80, 25);
		lbName = new JLabel("Person Name:");
		lbName.setForeground(Color.black);
		lbName.setBounds(15, 55, 90, 25);
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
		txtBal.setEnabled(false);
		txtBal.setHorizontalAlignment(JTextField.RIGHT);
		txtBal.setBounds(125, 125, 200, 25);

		// Aligning The Buttons.
		btnDel = new JButton("Delete");
		btnDel.setBounds(20, 165, 120, 25);
		btnDel.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(200, 165, 120, 25);
		btnCancel.addActionListener(this);

		// Adding the All the Controls to Panel.
		jpDel.add(lbNo);
		jpDel.add(txtNo);
		jpDel.add(lbName);
		jpDel.add(txtName);
		jpDel.add(lbDate);
		jpDel.add(txtDate);
		jpDel.add(lbBal);
		jpDel.add(txtBal);
		jpDel.add(btnDel);
		jpDel.add(btnCancel);

		// Restricting The User Input to only Numerics in Numeric TextBoxes.
		txtNo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
				if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep();
					ke.consume();
				}
			}
		});
		// Checking the Accunt No. Provided By User on Lost Focus of the TextBox.
		txtNo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent fe) {
				if (txtNo.getText().equals("")) {
				} else {
					findRec(); // Finding if Account No. Already Exist or Not.
				}
			}
		});

		// Adding Panel to Window.
		getContentPane().add(jpDel);

		// In the End Showing the New Account Window.
		setVisible(true);
	}

	// Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnDel) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Please! Provide Id of Customer.", "BankSystem - EmptyField",
						JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			} else {
				deletion(); // Confirm Deletion of Current Record.
			}
		}
		if (obj == btnCancel) {
			txtClear();
			setVisible(false);
			dispose();
		}

	}

	// Function use to Find Record by Matching the Contents of Records Array with ID
	// TextBox.
	private void findRec() {
		activeCustomer = repo.find(txtNo.getText());
		if (activeCustomer == null) {
			String str = txtNo.getText();
			txtClear();
			JOptionPane.showMessageDialog(this, "Account No. " + str + " doesn't Exist.", "BankSystem - WrongNo",
					JOptionPane.PLAIN_MESSAGE);
		} else
			showRec();
	}

	private void showRec() {
		txtNo.setText(activeCustomer.getId());
		txtName.setText(activeCustomer.getName());
		txtDate.setText(activeCustomer.getDate());
		txtBal.setText(activeCustomer.getBalance() + "");
	}

	private void deletion() {
		int reply = JOptionPane.showConfirmDialog(this,
				"Are you Sure you want to Delete\n" + txtName.getText() + " Record From BankSystem?",
				"Bank System - Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (reply == JOptionPane.YES_OPTION)
			delRec();
	}

	private void delRec() {
		try {
			if (activeCustomer != null)
				repo.removeCustomer(activeCustomer);
			JOptionPane.showMessageDialog(this, "Record has been Deleted Successfuly.", "BankSystem - Record Deleted",
					JOptionPane.PLAIN_MESSAGE);
			txtClear();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "There are Some Problem with File", "BankSystem - Problem",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	private void txtClear() {
		txtNo.setText("");
		txtName.setText("");
		txtDate.setText("");
		txtBal.setText("");
		txtNo.requestFocus();
	}

	void btnEnable() {
		txtNo.setEnabled(false);
		btnDel.setEnabled(false);
	}

}