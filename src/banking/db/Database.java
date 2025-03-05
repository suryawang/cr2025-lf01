package banking.db;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import banking.model.Customer;

public class Database {
	private FileInputStream fis;
	private DataInputStream dis;
	private int rows = 0;

	// String Type Array use to Load Records From File.
	private Customer records[] = new Customer[500];

	// Function use to load all Records from File when Application Execute.
	public void populateArray() {
		try {
			if (rows > 0)
				return;
			fis = new FileInputStream("Bank.dat");
			dis = new DataInputStream(fis);
			// Loop to Populate the Array.
			while (true) {
				String[] temp = new String[6];
				for (int i = 0; i < 6; i++) {
					temp[i] = dis.readUTF();
				}
				records[rows] = Customer.fromDb(temp);
				rows++;
			}
		} catch (Exception ex) {
			if (rows == 0) {
				JOptionPane.showMessageDialog(null, "Records File is Empty.\nEnter Records First to Display.",
						"BankSystem - EmptyFile", JOptionPane.PLAIN_MESSAGE);
			} else {
				try {
					dis.close();
					fis.close();
				} catch (Exception exp) {
				}
			}
		}

	}

	public int getRows() {
		return rows;
	}

	public int findRec(String no) {
		for (int x = 0; x < rows; x++)
			if (records[x].getNo().equals(no))
				return x;
		return -1;
	}

	public int findRecByName(String name) {
		for (int x = 0; x < rows; x++)
			if (records[x].getName().equalsIgnoreCase(name))
				return x;
		return -1;
	}

	public Customer get(int i) {
		return records[i];
	}

	public void set(int index, Customer value) {
		records[index] = value;
	}

	public void add(Customer value) {
		set(rows, value);
		rows++;
	}

	// Function use to Delete an Element from the Array.
	public boolean delRec(int recCount) throws IOException {
		try {
			if (records != null) {
				for (int i = recCount; i < rows; i++) {
					for (int r = 0; r < 6; r++) {
						records[i] = records[i + 1];
						if (records[i] == null)
							break;
					}
				}
				rows = rows - 1;
				return save();
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
		}
		return false;
	}

	public boolean save() throws IOException {
		FileOutputStream fos = new FileOutputStream("Bank.dat");
		DataOutputStream dos = new DataOutputStream(fos);
		var status = false;
		if (records != null) {
			for (int i = 0; i < rows; i++) {
				var t = records[i].toDb();
				for (int r = 0; r < 6; r++) {
					dos.writeUTF(t[r]);
					if (records[i] == null)
						break;
				}
			}
			status = true;
		}
		dos.close();
		fos.close();
		return status;
	}
}
