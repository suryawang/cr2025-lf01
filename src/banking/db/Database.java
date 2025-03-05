package banking.db;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Database {
	private FileInputStream fis;
	private DataInputStream dis;
	private int count = 0;
	private int rows = 0;
	private int total = 0;

	// String Type Array use to Load Records From File.
	private String records[][] = new String[500][6];

	// Function use to load all Records from File when Application Execute.
	public void populateArray() {
		try {
			if(rows>0) return;
			fis = new FileInputStream("Bank.dat");
			dis = new DataInputStream(fis);
			// Loop to Populate the Array.
			while (true) {
				for (int i = 0; i < 6; i++) {
					records[rows][i] = dis.readUTF();
				}
				rows++;
			}
		} catch (Exception ex) {
			total = rows;
			if (total == 0) {
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

	public int findRec(String no) {
		for (int x = 0; x < total; x++)
			if (records[x][0].equals(no))
				return x;
		return -1;
	}

	public String[] get(int i) {
		return records[i];
	}

	// Function use to Delete an Element from the Array.
	public boolean delRec(int recCount) throws IOException {
		try {
			if (records != null) {
				for (int i = recCount; i < total; i++) {
					for (int r = 0; r < 6; r++) {
						records[i][r] = records[i + 1][r];
						if (records[i][r] == null)
							break;
					}
				}
				total = total - 1;
				return deleteFile();
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
		}
		return false;
	}

	public boolean deleteFile() throws IOException {
		FileOutputStream fos = new FileOutputStream("Bank.dat");
		DataOutputStream dos = new DataOutputStream(fos);
		var status = false;
		if (records != null) {
			for (int i = 0; i < total; i++) {
				for (int r = 0; r < 6; r++) {
					dos.writeUTF(records[i][r]);
					if (records[i][r] == null)
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
