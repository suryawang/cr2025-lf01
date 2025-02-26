package gslc;

public class Calculator {
	class InsuranceQuote {
		private Motorist motorist;

		public InsuranceQuote(Motorist motorist) {
			this.motorist = motorist;
		}

		public RiskFactor calculateMotoristRisk() {
			if (motorist.getPointsOnLicense() > 3 || motorist.getAge() < 25)
				return new HighRiskFactor();
			if (motorist.getPointsOnLicense() > 0)
				return new ModerateRiskFactor();
			return new LowRiskFactor();
		}

		public double calculateInsurancePremium(double insuranceValue) {
			return calculateMotoristRisk().calculate(insuranceValue);
		}
	}

	interface RiskFactor {
		double calculate(double insurance);
	}

	class LowRiskFactor implements RiskFactor {
		public double calculate(double insurance) {
			return insurance * 0.02;
		}
	}

	class ModerateRiskFactor implements RiskFactor {
		public double calculate(double insurance) {
			return insurance * 0.04;
		}
	}

	class HighRiskFactor implements RiskFactor {
		public double calculate(double insurance) {
			return insurance * 0.06;
		}
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