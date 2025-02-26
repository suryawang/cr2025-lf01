package switch_statement;

public class BillingPlan {
	private String plan;
	public BillingPlan(String plan) {
		this.plan = plan;
	}
	public static BillingPlan basic() {
		return new BillingPlan("basic");
	}
	public BillingPlan getPlan() {
		return this;
	}
	public String getDetail() {
		return plan;
	}
}
