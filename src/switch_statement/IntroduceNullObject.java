package switch_statement;

public class IntroduceNullObject {
	class Company {
		// …
		private Customer customer;

		public Customer getCustomer() {
			return customer == null ? new NullCustomer() : customer;
		}
	}

	class NullCustomer extends Customer {
		public NullCustomer() {
			super("", BillingPlan.basic(), new NullPaymentHistory());
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

	class NullPaymentHistory extends PaymentHistory {
		public int getWeeksDelinquentInLastYear() {
			return 0;
		}
	}

	public void test() {
		Company site = new Company();
		Customer customer = site.getCustomer();
		System.out.println(customer.getName());
		System.out.println(customer.getPlan().getDetail());
		System.out.println(customer.getHistory().getWeeksDelinquentInLastYear());

	}

	public static void main(String[] a) {
		new IntroduceNullObject().test();
	}
}
