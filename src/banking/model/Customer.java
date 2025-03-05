package banking.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer {
	private String no;
	private String name;
	private Date openDate;
	private int balance;

	public Customer(String no, String name, Date openDate, int balance) {
		super();
		this.no = no;
		this.name = name;
		this.openDate = openDate;
		this.balance = balance;
	}

	public String getNo() {
		return no;
	}

	public String getName() {
		return name;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public int getBalance() {
		return balance;
	}
	public String getDate() {
		return new SimpleDateFormat("MMMM, dd yyyy").format(openDate);
	}

	public static Customer fromDb(String[] text) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("MMMM dd yyyy").parse(text[2] + " " + text[3] + " " + text[4]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Customer(text[0], text[1], date, Integer.parseInt(text[5]));
	}
	public static Customer fromUi(String... text) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("MMMM dd yyyy").parse(text[2] + " " + text[3] + " " + text[4]);
		} catch (Exception ex) {
		}
		return new Customer(text[0], text[1], date, Integer.parseInt(text[5]));
	}
	public String[] toDb() {
		String[] text = new String[6];
		text[0] = no;
		text[1] = name;
		var sm = new SimpleDateFormat("MMMM");
		text[2] = sm.format(openDate);
		var sd = new SimpleDateFormat("dd");
		text[3] = sd.format(openDate);
		var sy = new SimpleDateFormat("yyyy");
		text[4] = sy.format(openDate);
		text[5] = ""+ balance;
		return text;
	}
}
