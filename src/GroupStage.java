import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class GroupStage implements TournamentStage {
	private ArrayList<Team> playingTeams;
	private ArrayList<Match> matches;
	private ArrayList<Row> pointsTable;
	private ArrayList<Player> goalScorers;

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

	/**
	 * schedule() creates Match objects and fills them into the matches ArrayList (The number represents the index of the team in the ArrayList)
	 * 
	 * 1. setting teams consecutively like (0,1), (2,3), (4,0), (1,2), (3,4) ------> 5 matches done
	 * 2. setting teams alternatively like (0,2), (1,3), (2,4), (3,0), (4,1) ------> 10 matches done
	 * 
	 * So that the final schedule is: (0,1), (2,3), (4,0), (1,2), (3,4), (0,2), (1,3), (2,4), (3,0), (4,1)
	 */
	
	public void schedule() {
		for(int i = 0; i <= 8; i += 2) matches.add(new Match(playingTeams.get(i % 5), playingTeams.get((i + 1) % 5)));

		for(int i = 0; i <= 4; i++) matches.add(new Match(playingTeams.get(i%5), playingTeams.get((i+2)%5)));
	}

	@Override
	public void simulate() {
		int matchCompleted = 0;
		Scanner sc = new Scanner(System.in);
		String check;

		System.out.print(Color.ANSI_CYAN + "\n<Press [1] if you want to be able to see details after every Match Day> " + Color.ANSI_RESET);
		int see = (sc.nextLine().strip().equalsIgnoreCase("1")) ? 1 : 0;

		/*
		 * we loop through every match that has been scheduled. 
		 * 
		 * 1. if the matchCompleted is an even number => one match day is over, so we print out details of that matchday.
		 * 2. we make a small delay before every match using makeDelay(), which does NOT print anything, only makes a physical delay.
		 * 	  after printing out the match up, we use specificDelay() (defined in Delay.java) to make it feel like the match is being played.
		 * 3. after playing the match (which doesnt print out anything or have any delays of its own) & incrementing matchCompleted,
		 * 	  we print out the actual match name and details.
		 * 4. We add the goalScorers from both teams in the match to the goalScorers list that were maintaining. 
		 * 	  We also immediatly sort the goalScorers list as well (though its being done in the showGoalScorers() method already, its safer to include here as well)
		 * 5. Update points table by looping through all rows in points table, if we come across a winner or looser we make changes accordingly
		 *    We also sort the points table immediately after a match.
		 * 6. After every matchday (If user chose to see) then we provide the menu for information. (It loops until [ENTER] is clicked to simulate next matchday)
		 */
		for(Match match : this.matches) {
			/**1 */
			if (matchCompleted % 2 == 0) System.out.println(Color.ANSI_UNDERLINE + "Match Day " + ((matchCompleted / 2) + 1) + Color.ANSI_RESET + ": ");

			/**2 */
			Delay.makeDelay(500);
			System.out.print("\n" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ":\r");
			Delay.specificDelay(match.getTeam1().getName().length() + 4 + match.getTeam2().getName().length(), 160);

			/**3 */
			match.play();
			matchCompleted++;
			System.out.print("\r" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ": " + match);

			/**4 */
			for (Player p : match.getWinningTeamScorers()) if (!goalScorers.contains(p)) goalScorers.add(p);
			for (Player p : match.getLosingTeamScorers()) if (!goalScorers.contains(p)) goalScorers.add(p);
			sortScorers();

			/**5 */
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

			/**6 */
            if(matchCompleted % 2 == 0 && see == 1) {
            	Delay.makeDelay(450);
				do {
					System.out.print("\n" + Color.ANSI_CYAN + Color.ANSI_UNDERLINE + "Information after Match Day " + (matchCompleted / 2) + Color.ANSI_RESET + Color.ANSI_CYAN + ":\n" + 
											String.format("%7s Points Table\n%7s Top Scorer\n%7s Schedule\n[ENTER] Continue Simulation\n", "[  1  ]", "[  2  ]", "[  3  ]") + 
											"Enter Choice: " + Color.ANSI_RESET);
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
	 * Sorts the points table based on their points and then goal difference
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
	public void setGoalScorer(ArrayList<Player>scorers)
	{
		this.goalScorers = scorers;
	}
	public ArrayList<Team> getPlayingTeams()
	{
		return new ArrayList<Team>(this.playingTeams);
	}
}
