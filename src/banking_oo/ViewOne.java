package banking_oo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import banking_oo.model.Customer;
import banking_oo.repo.CustomerRepository;

import java.io.*;
import java.util.Vector;

public class ViewOne extends JInternalFrame implements ActionListener {

	private JPanel jpRec = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbBal;
	private JTextField txtNo, txtName, txtDate, txtBal, txtRec;
	private JButton btnFirst, btnBack, btnNext, btnLast;

	private Vector<Customer> data;
	private int curr;

	ViewOne() {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super("View Account Holders", false, true, false, true);
		setSize(350, 235);

		jpRec.setLayout(null);

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
		txtNo.setEnabled(false);
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

		// Aligninig The Navigation Buttons.
		btnFirst = new JButton("<<");
		btnFirst.setBounds(15, 165, 50, 25);
		btnFirst.addActionListener(this);
		btnBack = new JButton("<");
		btnBack.setBounds(65, 165, 50, 25);
		btnBack.addActionListener(this);
		btnNext = new JButton(">");
		btnNext.setBounds(225, 165, 50, 25);
		btnNext.addActionListener(this);
		btnLast = new JButton(">>");
		btnLast.setBounds(275, 165, 50, 25);
		btnLast.addActionListener(this);
		txtRec = new JTextField();
		txtRec.setEnabled(false);
		txtRec.setHorizontalAlignment(JTextField.CENTER);
		txtRec.setBounds(115, 165, 109, 25);

		// Adding the All the Controls to Panel.
		jpRec.add(lbNo);
		jpRec.add(txtNo);
		jpRec.add(lbName);
		jpRec.add(txtName);
		jpRec.add(lbDate);
		jpRec.add(txtDate);
		jpRec.add(lbBal);
		jpRec.add(txtBal);
		jpRec.add(btnFirst);
		jpRec.add(btnBack);
		jpRec.add(btnNext);
		jpRec.add(btnLast);
		jpRec.add(txtRec);

		// Adding Panel to Window.
		getContentPane().add(jpRec);

		// Load All Existing Records in Memory and Display them on Form.
		data = new Vector(CustomerRepository.getInstance().getData());
		curr = 0;
		showRec();

		// In the End Showing the New Account Window.
		setVisible(true);

	}

	// Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		if (obj == btnFirst)
			curr = 0;
		else if (obj == btnBack) {
			if (--curr < 0) {
				curr = 0;
				JOptionPane.showMessageDialog(this, "You are on First Record.", "BankSystem - 1st Record",
						JOptionPane.PLAIN_MESSAGE);
			}
		} else if (obj == btnNext) {
			if (++curr == data.size()) {
				curr = data.size() - 1;
				JOptionPane.showMessageDialog(this, "You are on Last Record.", "BankSystem - End of Records",
						JOptionPane.PLAIN_MESSAGE);
			}
		} else if (obj == btnLast)
			curr = data.size() - 1;
		showRec();
	}

	private void showRec() {
		var c = data.get(curr);
		txtNo.setText(c.getId());
		txtName.setText(c.getName());
		txtDate.setText(c.getDate());
		txtBal.setText(c.getBalance() + "");
		txtRec.setText((curr + 1) + "/" + data.size());
	}

	private void btnEnable() {
		btnFirst.setEnabled(false);
		btnBack.setEnabled(false);
		btnNext.setEnabled(false);
		btnLast.setEnabled(false);
	}
}