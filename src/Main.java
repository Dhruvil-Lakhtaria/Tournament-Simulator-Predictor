import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("ENTER YOUR NAME:");
		String name = sc.next();
		User u = new User(name);
		Tournament FIFAWORLDCUP = new Tournament(u,"teams.txt");
		FIFAWORLDCUP.start();
		sc.close();
	}
} 