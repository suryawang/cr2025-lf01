package switch_statement;

public final class ReplaceConditionalWithPolymorphism {
	
	public void test() {
		Employee abi = new Employee(EmployeeType.newType(EmployeeType.ENGINEER),1200, 0, 0);
		Employee rika = new Employee(EmployeeType.newType(EmployeeType.SALESMAN),1200, 500, 0);
		Employee eri = new Employee(EmployeeType.newType(EmployeeType.MANAGER),1200, 0, 700);
		System.out.println("PayAmount " + abi.payAmount());
		System.out.println("PayAmount " + rika.payAmount());
		System.out.println("PayAmount " + eri.payAmount());
	}
	public static void main(String[] args) {
		new ReplaceConditionalWithPolymorphism().test();
	}

}
class Employee {
  private EmployeeType type;
  public int getTypeCode() {
	return type.getTypeCode();
  }
  public Employee(EmployeeType type, int salary, int commission, int bonus) {
	  this.type = type;
	  this.monthlySalary = salary;
	  this.commission = commission;
	  this.bonus = bonus;
  }
  

  public int monthlySalary;
  public int commission;
  public int bonus;
  
  // TODO:
  // Replace this method with polymorphism
  
  public int payAmount() {
	switch (getTypeCode()) {
	  case EmployeeType.ENGINEER:
		return monthlySalary;
	  case EmployeeType.SALESMAN:
		return monthlySalary + commission;
	  case EmployeeType.MANAGER:
		return monthlySalary + bonus;
	  default:
		throw new RuntimeException("Incorrect Employee Code");
	}
  }
}

abstract class EmployeeType {
  static final int ENGINEER = 0;
  static final int SALESMAN = 1;
  static final int MANAGER = 2;

  abstract public int getTypeCode();
  public static EmployeeType newType(int code) {
	switch (code) {
	  case ENGINEER:
		return new Engineer();
	  case SALESMAN:
		return new Salesman();
	  case MANAGER:
		return new Manager();
	  default:
		throw new IllegalArgumentException("Incorrect Employee Code");
	}
  }
}
class Engineer extends EmployeeType {
  @Override public int getTypeCode() {
	return EmployeeType.ENGINEER;
  }
}
class Salesman extends EmployeeType {
  @Override public int getTypeCode() {
	return EmployeeType.SALESMAN;
  }
}
class Manager extends EmployeeType {
  @Override public int getTypeCode() {
	return EmployeeType.MANAGER;
  }
}