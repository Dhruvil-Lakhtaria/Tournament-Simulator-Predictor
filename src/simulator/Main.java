package simulator;

import java.util.Scanner;

import simulator.tournament.Tournament;
import simulator.ui.*;
public class Main {

	/**
	 * 1. title
	 * 2. invoking User Constructor with input from user (Constructor takes care of invalid cases)
	 * 3. Instantiating Tournament called FIFA WORLD CUP (simultaneously creates the teams and all related objects)
	 * 4. starts the tournament
	 */
	public static void main(String[] args) {
		System.out.println(Color.ANSI_UNDERLINE + Color.ANSI_GREEN + "\nTOURNAMENT SIMULATOR & PREDICTOR" + Color.ANSI_RESET + ":\n");
		Scanner sc = new Scanner(System.in);
		System.out.print(Color.ANSI_CYAN + "Enter Your Name: " + Color.ANSI_RESET);
		String name = new String(sc.nextLine());
		User u = new User(name);
		Tournament FIFAWORLDCUP = new Tournament(u,"teams.txt");
		FIFAWORLDCUP.start();
		sc.close();
	}
} 
