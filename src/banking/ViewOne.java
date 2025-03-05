package banking;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import banking.db.Database;

import java.io.*;

public class ViewOne extends JInternalFrame implements ActionListener {

	private JPanel jpRec = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbBal;
	private JTextField txtNo, txtName, txtDate, txtBal, txtRec;
	private JButton btnFirst, btnBack, btnNext, btnLast;

	private int recCount = 0;

	private Database db = new Database();

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
		populateArray();
		showRec(0);

		// In the End Showing the New Account Window.
		setVisible(true);

	}

	// Function use By Buttons of Window to Perform Action as User Click Them.
	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnFirst) {
			recCount = 0;
			showRec(recCount);
		} else if (obj == btnBack) {
			recCount = recCount - 1;
			if (recCount < 0) {
				recCount = 0;
				showRec(recCount);
				JOptionPane.showMessageDialog(this, "You are on First Record.", "BankSystem - 1st Record",
						JOptionPane.PLAIN_MESSAGE);
			} else {
				showRec(recCount);
			}
		} else if (obj == btnNext) {
			recCount = recCount + 1;
			if (recCount == db.getRows()) {
				recCount = db.getRows() - 1;
				showRec(recCount);
				JOptionPane.showMessageDialog(this, "You are on Last Record.", "BankSystem - End of Records",
						JOptionPane.PLAIN_MESSAGE);
			} else {
				showRec(recCount);
			}
		} else if (obj == btnLast) {
			recCount = db.getRows() - 1;
			showRec(recCount);
		}

	}

	// Function use to load all Records from File when Application Execute.
	void populateArray() {
		db.populateArray();
		if (db.getRows() == 0)
			btnEnable();
	}

	// Function which display Record from Array onto the Form.
	public void showRec(int intRec) {
		var records = db.get(intRec);
		txtNo.setText(records.getNo());
		txtName.setText(records.getName());
		txtDate.setText(records.getDate());
		txtBal.setText(records.getBalance()+"");
		if (db.getRows() == 0) {
			txtRec.setText(intRec + "/" + db.getRows()); // Showing Record No. and Total Records.
			txtDate.setText("");
		} else {
			txtRec.setText((intRec + 1) + "/" + db.getRows()); // Showing Record No. and Total Records.
		}

	}

	// Function use to Lock all Buttons of Window.
	void btnEnable() {

		btnFirst.setEnabled(false);
		btnBack.setEnabled(false);
		btnNext.setEnabled(false);
		btnLast.setEnabled(false);

	}

}