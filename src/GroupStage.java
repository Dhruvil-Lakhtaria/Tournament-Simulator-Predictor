import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class GroupStage implements TournamentStage {
	private ArrayList<Team> playingTeams;
	private ArrayList<Match> matches;
	private ArrayList<Row> pointsTable;
	private ArrayList<Player> goalScorers;
	// Constructor
	public GroupStage(ArrayList<Team>teams)
	{
		this.playingTeams = teams;
		matches = new ArrayList<Match>();
		pointsTable = new ArrayList<Row>();
		goalScorers = new ArrayList<Player>();
		for(Team t1 : this.playingTeams)
		{
			pointsTable.add(new Row(t1));
		}
	}
	// Filling matches ARRAYLIST with scheduled fixtures...
	@Override
	public void schedule() {
		// here everywhere number represent the index of team in arraylist
		/*
		 * first setting teams consecutively
		 * like (0,1) , (2,3) , (4,0) , (1,2) ,(3,4)
		 * 5 matches done
		 */
		for(int i = 0; i <= 8; i += 2)
		{
			matches.add(new Match(playingTeams.get(i%5), playingTeams.get((i+1)%5)));
		}
		/*
		 * setting teams alternatively 
		 * like 0,2....1,3.....2,4....3,0.....4,1
		 * 10 matches done
		 */
		for(int i = 0; i <= 4; i++)
		{
			matches.add(new Match(playingTeams.get(i%5), playingTeams.get((i+2)%5)));
		}
		/*
		 * final schedule ---
		 * 0,1 -> 2,3 -> 4,0 -> 1,2 -> 3,4 -> 0,2 -> 1,3 -> 2,4 -> 3,0 -> 4,1 
		 */
	}

	/**
	 * for Dhruvil: (I have commented out every code of urs that I have replaced. so if u want to revert, u can just uncomment no problem :D)
	 * 
	 * I have added a small parameter see inside the simulate method. basically if the user wants to see every single detail then he can,
	 * but otherwise we can just let the simulation happen on its own and show him the details at the end. If you dont understand just run the code it might make sense.
	 * 
	 * I have removed int matchDay since its basically just matchCompleted/2, and we dont use it anywhere else except outputs,
	 * if you want to keep it still and use it np just uncomment it and change the "matchCompleted/2" to matchDay stuff.
	 * 
	 * I have also made a change to point 4, 5 & 6 (Check inside the for loop)
	 */
	@Override
	public void simulate() {
		// int matchDay = 0, matchCompleted = 0;
		int matchCompleted = 0;
		Scanner sc = new Scanner(System.in);
		String check;

		System.out.print(Color.ANSI_CYAN + "\n<Press [1] if you want to be able to see details after every Match Day> " + Color.ANSI_RESET);
		int see = (sc.nextLine().strip().equalsIgnoreCase("1")) ? 1 : 0;

		/*
		 * we loop through every match that has been scheduled. 
		 * 1. if the matchCompleted is an even number => one match day is over, so we print out details of that matchday.
		 * 2. we make a small delay before every match using makeDelay(), which does NOT print anything, only makes a physical delay.
		 * 	  after printing out the match up, we use specificDelay() (defined in Delay.java) to make it feel like the match is being played.
		 * 3. after playing the match (which doesnt print out anything or have any delays of its own) & incrementing matchCompleted,
		 * 	  we print out the actual match name and details.
		 */
		for(Match match : this.matches) {
			if (matchCompleted % 2 == 0) System.out.println(Color.ANSI_UNDERLINE + "Match Day " + ((matchCompleted / 2) + 1) + Color.ANSI_RESET + ": ");

			Delay.makeDelay(450);
			System.out.print("\n" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ":\r");
			Delay.specificDelay(match.getTeam1().getName().length() + 4 + match.getTeam2().getName().length(), 80);

			match.play();
			matchCompleted++;
			System.out.print("\r" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ": " + match);

			/**
			 * 4. We add the goalScorers from both teams in the match to the goalScorers list that were maintaining. 
			 * 	  We also immediatly sort the goalScorers list as well
			 * 	  Though its being done in the showGoalScorers() method already, think its safer to include here as well.
			 * 
			 * for Dhruvil: 
			 * I have commented out your previous code implementing this since I felt like you couldve used the property of ArrayLists here to check and insert easily.
			 * I have written code right above your commented code to show you what i mean.
			 * If you are okay with that, I suggest you uncomment the two lines i have written right below this comment block
			 * They basically do the same thing but its one line, makes the file less long and calm :P
			 * 
			 * (If you ARE going to make the change then itd be better if u would just delete the rest of the commented codes and
			 * my uncommented current code cos the file feels too large. Its totally up to you though :P)
			 */

			for (Player p : match.getWinningTeamScorers()) if (!goalScorers.contains(p)) goalScorers.add(p);
			for (Player p : match.getLosingTeamScorers()) if (!goalScorers.contains(p)) goalScorers.add(p);
			sortScorers();

			// for (Player p : match.getWinningTeamScorers()) {
			// 	if (!goalScorers.contains(p)) {
			// 		goalScorers.add(p);
			// 	}
			// }
			// for (Player p : match.getLosingTeamScorers()) {
			// 	if (!goalScorers.contains(p)) {
			// 		goalScorers.add(p);
			// 	}
			// }
			// sortScorers();

			// for(Player p : match.getWinningTeamScorers())
			// {
			// 	boolean notPresent = true;
			// 	for(Player P : this.goalScorers)
			// 	{
			// 		if(p.getName().equals(P.getName()))
			// 		{
			// 			notPresent = false;
			// 			break;
			// 		}
			// 	}
			// 	if(notPresent)
			// 		this.goalScorers.add(p);
			// }
			// for(Player p : match.getLosingTeamScorers())
			// {
			// 	boolean notPresent = true;
			// 	for(Player P : this.goalScorers)
			// 	{
			// 		if(p.getName().equals(P.getName()))
			// 		{
			// 			notPresent = false;
			// 			break;
			// 		}
			// 	}
			// 	if(notPresent)
			// 		this.goalScorers.add(p);
			// }

            /*
             * 5. Update points table by looping through all rows in points table, if we come across a winner or looser we make changes accordingly
			 *    We also sort the points table for safety
			 *    (Since currently we only sort the points table every time the points table is printed, if we didnt print it, then the points table will remain same.)
			 *    (Meaning when we do getQualifiedTeam() or getEliminatedTeam() elsewhere, we were getting wrong values.) 
             */
            for(Row r : pointsTable) {
            	if(r.getTeam().getName().equals(match.getWinner().getName())) {
            		r.addWin();
            		r.updateGoalsScored(match.getGoalsWinner());
            		r.updateGoalsConceded(match.getGoalsLoser());
            	}
            	else if(r.getTeam().getName().equals(match.getLoser().getName())) {
            		r.addLoss();
            		r.updateGoalsScored(match.getGoalsLoser());
            		r.updateGoalsConceded(match.getGoalsWinner());
            	}
            }
			sortPointsTable();
            // matchCompleted++;
        	// sort the updated points table .... 
            // System.out.println(match);	//matchstats
           	// points table at the end of every matchday

			/**
			 * 6. After every MatchDay we provide the Menu for that matchdays Info to the user. 
			 * 
			 * For Dhruvil: 
			 * 
			 * Earlier this part of the program was written using while (true) loop i think, 
			 * and breakCondition's value determined whether the program would break from the loop or not.
			 * However I have used a do-while loop to replace it as that is the main use of a do-while. (Ive also made it a bit more compact. If it doesnt make sense pls lmk.) 
			 * Unfortunately since i made that change a while ago i wasnt able to copy and save a commented version. 
			 * If you want to change it back simply uncomment the breakCondition initialization and change do while to while (true) and it should work though.
			 * 
			 * (Your call.)
			 */

            if(matchCompleted % 2 == 0 && see == 1) {
            	// System.out.print("Enter [1] for POINTS TABLE\n      [2] for TOP SCORER\n      [3] for SCHEDULE\n      [KEY] to continue :");
				// System.out.print("\n"+ Color.ANSI_CYAN + Color.ANSI_UNDERLINE + "Information after Match Day " + matchDay + Color.ANSI_UNDERLINE + ":\n[1] Points Table\n[2] Top Scorer\n[3] Schedule\n[ENTER] Continue Simulation");
            	Delay.makeDelay(450);
				do {
					System.out.print("\n" + Color.ANSI_CYAN + Color.ANSI_UNDERLINE + "Information after Match Day " + (matchCompleted / 2) + Color.ANSI_RESET + Color.ANSI_CYAN + ":\n" + 
											String.format("%7s Points Table\n%7s Top Scorer\n%7s Schedule\n[ENTER] Continue Simulation\n", "[  1  ]", "[  2  ]", "[  3  ]") + 
											"Enter Choice: " + Color.ANSI_RESET);
            		// int breakCondition = 0;
            		check = sc.nextLine();
            		switch (check.strip()) {
            			case "1":	System.out.println("\n" + this.showPointsTable()); break;
            			case "2":	System.out.print("\n" + this.showGoalScorer()); break;
            			case "3":	System.out.println("\n" + Color.ANSI_UNDERLINE + "Scheduled Matches Remaining" + Color.ANSI_RESET + ":");
									if (matchCompleted >= matches.size()) System.out.println("No more matches to be played!");
									for(int i = matchCompleted;i<this.matches.size(); i++, Delay.makeDelay(90)) System.out.println(this.matches.get(i).getTeam1().getName() + " vs " + this.matches.get(i).getTeam2().getName());
            						break;
						case "" : 	System.out.println(); break;
            			default : 	System.out.println("\n" + Color.ANSI_RED + "Invalid Choice!" + Color.ANSI_RESET); break;
            		}
					Delay.makeDelay(450);
            	} while (check.strip() != "");
            }
			else if (see != 1 && matchCompleted == 10) {
				Delay.makeDelay(450);
				System.out.println("\n" + this.showPointsTable());
				System.out.print("\n" + this.showGoalScorer());
			}
		}
	}

	/**
	 * For Dhruvil: 
	 * 
	 * Why are we comparing based on name? is there a situation possible where all of the other attributes are equal for two teams?
	 * if so, can we check for another form of comparison instead? 
	 * Because after 5 matchdays it might be weird to just send the team which starts with a better letter to the next round....
	 */
	public void sortPointsTable() {
		this.pointsTable.sort(
			new Comparator<Row>() {
				public int compare(Row a,Row b) {
					if(a.getPoints() > b.getPoints()) return -1;
					else if(a.getPoints() < b.getPoints()) return 1;
					else {
						if(a.getGoalsScored()-a.getGoalsConceded() > b.getGoalsScored()-b.getGoalsConceded()) return -1;
						else if(a.getGoalsScored()-a.getGoalsConceded() < b.getGoalsScored()-b.getGoalsConceded()) return 1;
						else {
							if(a.getGoalsScored() > b.getGoalsScored()) return -1;
							else if(a.getGoalsScored() < b.getGoalsScored()) return 1;
							else {
								if(a.getTeam().getName().compareToIgnoreCase(b.getTeam().getName()) < 0) return -1;
								else if(a.getTeam().getName().compareToIgnoreCase(b.getTeam().getName()) > 0) return 1;
								else return 0;
							}
						}
					}
				}
			}
	    );
	}

	public void sortScorers() {
		this.goalScorers.sort(
			new Comparator<Player>() {
				public int compare(Player a,Player b) {
					if(a.getGoals() > b.getGoals()) return -1;
					else if(a.getGoals() < b.getGoals()) return 1;
					else return 0;
				}
			}
		);
	}

	/**
	 * 1. As mentioned earlier, the sorting of pointsTable was only happening when u call showPointsTable.
	 * 	  Although its corrected now, still think its a good idea to include it here as well just for safety 
	 * 2. Formatted output of top heading/titles row
	 * 3. If it is the last row, We Underline the entire output so as to make it look like the table is closed
	 * 
	 * For Dhruvil:
	 * 
	 * Changed some formatting. might look weird but the output comes quite solid imo. Your call. This is your class afterall
	 * 
	 * (Just a heads up, whatever change u make in showPointsTable, ull have to account for those in the toString of Row.java as well)
	 */
	public String showPointsTable() {
		this.sortPointsTable();
		String s = "\t\t" + "_".repeat(63) + String.format("\n\t\t" + Color.ANSI_UNDERLINE + "| %-10s | %-12s | %-14s | %-1s | %-1s | %-6s |" + Color.ANSI_RESET + "\n",
								 "   Team", "Goals Scored", "Goals Conceded", "W", "L", "Points");
		for(Row r : pointsTable) {
			if (pointsTable.indexOf(r) == 4) s += "\t\t" + Color.ANSI_UNDERLINE + r.toString() + Color.ANSI_RESET + "\n";
			else s += "\t\t" + r.toString() + "\n";
    	}
		return s;
	}

	public String showGoalScorer() {
		this.sortScorers();
		String s = Color.ANSI_UNDERLINE + "Top Scorers" + Color.ANSI_RESET + ":\n";
		for(int i = 0; i < 5 && i < this.goalScorers.size(); i++, Delay.makeDelay(90)) {
			s = s + Integer.toString(i+1) +"."+ this.goalScorers.get(i).getName() + " - " + this.goalScorers.get(i).getGoals() + "\n";
		}
		return s;
	}

	/*
	 * Note: toString() of GroupStage only prints out the schedule of matches.
	 */
	public String toString() {
		String res = "";
		for(Match match : this.matches) {
			res = res + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + "\n";
		}
		return res;
	}
	
	/**
	 * Returns top 4 teams from points Table
	 * we call sortPointsTable() before doing the return operations
	 */
	public ArrayList<Team> getQualifiedTeam() {
		sortPointsTable();
		ArrayList<Team>qt = new ArrayList<Team>();
		//after sorting remove last not including last team
		for(int i = 0; i < 4; i++) {
			qt.add(pointsTable.get(i).getTeam());
		}
		return qt;
	}

	/**
	 * Returns the team at Row with index [LAST_INDEX] in the points table
	 */
	public Team getEliminatedTeam(){
		sortPointsTable();
		return this.pointsTable.get(4).getTeam();
	}

	@Override
	public ArrayList<Player> getGoalScorers()
	{
		return new ArrayList<Player>(this.goalScorers);
	}

	public ArrayList<Team> getPlayingTeams()
	{
		return new ArrayList<Team>(this.playingTeams);
	}
}
