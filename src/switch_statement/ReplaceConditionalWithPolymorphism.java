package switch_statement;

public final class ReplaceConditionalWithPolymorphism {

	public void test() {
		Employee abi = new Engineer(1200);
		Employee rika = new Salesman(1200, 500);
		Employee eri = new Manager(1200, 700);

		System.out.println("PayAmount " + abi.payAmount());
		System.out.println("PayAmount " + rika.payAmount());
		System.out.println("PayAmount " + eri.payAmount());
	}

	public static void main(String[] args) {
		new ReplaceConditionalWithPolymorphism().test();
	}

}

abstract class Employee {
	public Employee(int salary) {
		this.monthlySalary = salary;
	}

	protected int monthlySalary;

	abstract int payAmount();
}

class Engineer extends Employee {
	public Engineer(int salary) {
		super(salary);
	}

	@Override
	int payAmount() {
		return monthlySalary;
	}

}

class Salesman extends Employee {
	private int commission;

	public Salesman(int salary, int commision) {
		super(salary);
		this.commission = commision;
	}

	@Override
	int payAmount() {
		return monthlySalary + commission;
	}

}

class Manager extends Employee {
	private int bonus;

	public Manager(int salary, int bonus) {
		super(salary);
		this.bonus = bonus;
	}

	@Override
	int payAmount() {
		return monthlySalary + bonus;
	}

}
