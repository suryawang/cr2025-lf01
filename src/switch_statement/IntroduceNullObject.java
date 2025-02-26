package switch_statement;

public class IntroduceNullObject {
	class Company {
		// …
		private Customer customer;

		public Customer getCustomer() {
			return customer;
		}
	}

	class Customer {

		private String name;
		private BillingPlan plan;
		private PaymentHistory history;

		public Customer(String name, BillingPlan plan, PaymentHistory history) {
			super();
			this.name = name;
			this.plan = plan;
			this.history = history;
		}

		public String getName() {
			return name;
		}

		public BillingPlan getPlan() {
			return plan;
		}

		public PaymentHistory getHistory() {
			return history;
		}
	}

	class PaymentHistory {
		public int getWeeksDelinquentInLastYear() {
			return 10;
		}
	}

	public void test() {
		Company site = new Company();
		Customer customer = site.getCustomer();
		String customerName;
		if (customer == null) {
			customerName = "N/A";
		} else {
			customerName = customer.getName();
		}

		// …
		BillingPlan plan;
		if (customer == null) {
			plan = BillingPlan.basic();
		} else {
			plan = customer.getPlan();
		}

		// …
		int weeksDelinquent;
		if (customer == null) {
			weeksDelinquent = 0;
		} else {
			weeksDelinquent = customer.getHistory().getWeeksDelinquentInLastYear();
		}
		System.out.println(customerName);
		System.out.println(plan.getDetail());
		System.out.println(weeksDelinquent);
	}

	public static void main(String[] a) {
		new IntroduceNullObject().test();
	}
}
