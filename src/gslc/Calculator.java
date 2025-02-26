package gslc;

public class Calculator {
	class InsuranceQuote {
		private Motorist motorist;

		public InsuranceQuote(Motorist motorist) {
			this.motorist = motorist;
		}

		public RiskFactor calculateMotoristRisk() {
			if (motorist.getPointsOnLicense() > 3 || motorist.getAge() < 25)
				return RiskFactor.HIGH_RISK;
			if (motorist.getPointsOnLicense() > 0)
				return RiskFactor.MODERATE_RISK;

			return RiskFactor.LOW_RISK;
		}

		public double calculateInsurancePremium(double insuranceValue) {
			RiskFactor riskFactor = calculateMotoristRisk();
			switch (riskFactor) {
			case LOW_RISK:
				return insuranceValue * 0.02;
			case MODERATE_RISK:
				return insuranceValue * 0.04;
			default:
				return insuranceValue * 0.06;
			}
		}
	}

	enum RiskFactor {
		LOW_RISK, MODERATE_RISK, HIGH_RISK
	}

	class Motorist {
		private int points;
		private int age;

		public Motorist(int age) {
			this.points = 0;
			this.age = age;
		}

		public void addPoints(int points) {
			this.points += points;
		}

		public int getAge() {
			return this.age;
		}

		public int getPointsOnLicense() {
			return points;
		}
	}

	public void test() {
		Motorist a = new Motorist(30);
		Motorist b = new Motorist(31);
		Motorist c = new Motorist(37);
		b.addPoints(1);
		c.addPoints(5);
		InsuranceQuote i1 = new InsuranceQuote(a);
		InsuranceQuote i2 = new InsuranceQuote(b);
		InsuranceQuote i3 = new InsuranceQuote(c);
		System.out.println(i1.calculateInsurancePremium(1000));
		System.out.println(i2.calculateInsurancePremium(1000));
		System.out.println(i3.calculateInsurancePremium(1000));
	}

	public static void main(String[] args) {
		new Calculator().test();
	}
}