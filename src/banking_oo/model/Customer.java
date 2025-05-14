package banking_oo.model;

public class Customer {
	private String id;
	private String name;
	private String month;
	private int day;
	private int year;
	private int balance;

	public Customer(String id, String name, String month, int day, int year, int balance) {
		this.id = id;
		this.name = name;
		this.month = month;
		this.day = day;
		this.year = year;
		setBalance(balance);
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setBalance(int balance) {
		if (balance < 0)
			balance = 0;
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getYear() {
		return year;
	}

	public int getBalance() {
		return balance;
	}

}
