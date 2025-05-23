package banking_oo;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import banking_oo.model.Customer;
import banking_oo.repo.CustomerRepository;

public class ViewCustomer extends JInternalFrame {

	private JPanel jpShow = new JPanel();

	private DefaultTableModel dtmCustomer;
	private JTable tbCustomer;
	private JScrollPane jspTable;

	private CustomerRepository repo = CustomerRepository.getInstance();

	ViewCustomer() {

		// super(Title, Resizable, Closable, Maximizable, Iconifiable)
		super("View All Account Holders", false, true, false, true);
		setSize(475, 280);

		jpShow.setLayout(null);

		tbCustomer = makeTable();
		jspTable = new JScrollPane(tbCustomer);
		jspTable.setBounds(20, 20, 425, 200);

		// Adding the Table to Panel.
		jpShow.add(jspTable);

		// Adding Panel to Window.
		getContentPane().add(jpShow);

		// In the End Showing the New Account Window.
		setVisible(true);

	}

	private JTable makeTable() {
		var cols = new Vector<>(Arrays.asList("Account No.", "Customer Name", "Opening Date", "Bank Balance"));
		Vector<Vector<Object>> db = new Vector<>();
		for (var p : repo.getData())
			db.add(new Vector<>(Arrays.asList(p.getId(), p.getName(), p.getDate(), p.getBalance())));

		dtmCustomer = new DefaultTableModel(db, cols);

		tbCustomer = new JTable(dtmCustomer) {
			public boolean isCellEditable(int iRow, int iCol) {
				return false; // Disable All Columns of Table.
			}
		};
		// Sizing the Columns of Table.
		(tbCustomer.getColumnModel().getColumn(0)).setPreferredWidth(180);
		(tbCustomer.getColumnModel().getColumn(1)).setPreferredWidth(275);
		(tbCustomer.getColumnModel().getColumn(2)).setPreferredWidth(275);
		(tbCustomer.getColumnModel().getColumn(3)).setPreferredWidth(200);
		tbCustomer.setRowHeight(20);
		tbCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return tbCustomer;

	}

}