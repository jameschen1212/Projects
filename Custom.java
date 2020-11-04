import java.lang.Math;
public class Custom {
	public static double getCustom(double [] outcomes, double [] probabilities) {
		double rand = Math.random();
		double cumulative = 0.0;
		for(int i = 0; i < outcomes.length; i++) {
			cumulative += probabilities[i];
			if(cumulative >= rand) {
				return outcomes[i];
			}
		}
		return 0.0;
	}
	public static int getCustomInt(int [] outcomes, double [] probabilities) {
		double rand = Math.random();
		double cumulative = 0.0;
		for(int i = 0; i < outcomes.length; i++) {
			cumulative += probabilities[i];
			if(cumulative >= rand) {
				return outcomes[i];
			}
		}
		return 0;
	}
	public static void main(String[] args) {
		double[] outcome = new double[] {Double.parseDouble(args[0]), Double.parseDouble(args[2]), Double.parseDouble(args[4]), Double.parseDouble(args[6]), Double.parseDouble(args[8])};
		double[] prob = new double[] {Double.parseDouble(args[1]), Double.parseDouble(args[3]), Double.parseDouble(args[5]), Double.parseDouble(args[7]), Double.parseDouble(args[9])};
		int N = Integer.parseInt(args[10]);
		for(int i = 0; i < N; i++) {
			System.out.println(getCustom(outcome, prob));
		}
	}
}
