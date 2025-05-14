package banking_oo.repo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import banking_oo.model.Customer;

public class CustomerRepository {
	private Vector<Customer> data = new Vector<>();
	private static CustomerRepository repo;

	private CustomerRepository() {
		read();
	}

	public static CustomerRepository getInstance() {
		if (repo == null)
			repo = new CustomerRepository();
		return repo;
	}

	public List<Customer> getData() {
		return Collections.unmodifiableList(data);
	}

	private void read() {
		try {
			var fis = new FileInputStream("Bank.dat");
			var dis = new DataInputStream(fis);
			var d = new String[6];
			while (true) {
				for (int i = 0; i < 6; i++) {
					d[i] = dis.readUTF();
				}
				data.add(Customer.createCustomerFromDb(d));
			}
		} catch (FileNotFoundException ex) {

		} catch (IOException ex) {

		}
	}

	public Customer find(String id) {
		for (var c : data)
			if (c.getId().equals(id))
				return c;
		return null;
	}

	public Customer findByName(String name) {
		for (var c : data)
			if (c.getName().equalsIgnoreCase(name))
				return c;
		return null;
	}

	public void setCustomer(int index, Customer customer) throws IOException {
		data.set(index, customer);
		save();
	}

	public void addCustomer(Customer customer) throws IOException {
		data.add(customer);
		save();
	}

	public void removeCustomer(Customer customer) throws IOException {
		data.remove(customer);
		save();
	}

	public void save() throws IOException {
		if (data.size() == 0)
			return;
		var fos = new FileOutputStream("Bank.dat");
		var dos = new DataOutputStream(fos);
		for (var d : data)
			for (var c : d.toDb())
				dos.writeUTF(c);
		dos.close();
		fos.close();
	}
}
