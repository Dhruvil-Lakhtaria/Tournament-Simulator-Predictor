import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("ENTER YOUR NAME:");
		String name = sc.next();
		sc.close();
		User u = new User(name);
		Tournament FIFAWORLDCUP = new Tournament(u,"C:\\Users\\Owner\\eclipse-workspace\\workAreaProject\\src\\teams.txt");
		FIFAWORLDCUP.start();
	}
} 