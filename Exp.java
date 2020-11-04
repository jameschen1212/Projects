import java.lang.Math;
public class Exp {
	public static void main(String[] args) {
		double lambda = Double.parseDouble(args[0]);
		int N = Integer.parseInt(args[1]);
		for(int i = 0; i < N; i++) {
			System.out.println(getExp(lambda));
		}
	}
	public static double getExp(double lambda) {
		double var = Math.random();
		return (-Math.log(1 - var))/lambda;
	}
}
