

public class Calculation {

	public static void main(String[] args) {
		int efail = 1000;
		if(args.length ==1){
						
			efail = Integer.parseInt(args[0]) + 500;
		}
		
		System.out.println(efail);
	}
}
