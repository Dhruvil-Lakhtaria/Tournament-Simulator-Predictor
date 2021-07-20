import java.util.ArrayList;

public class GroupStage implements TournamentStage {
	private ArrayList<Team>playingTeams;
	private ArrayList<Match>matches;
	private ArrayList<Row>pointsTable;
	private ArrayList<Player> goalScorers;
//	Constructor
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
//	Filling matches ARRAYLIST with scheduled fixtures...
	@Override
	public void schedule() {
//		here everywhere number represent the index of team in arraylist
		/*
		 * first setting teams consecutively
		 * like (0,1) , (2,3) , (4,0) , (1,2) ,(3,4)
		 * 5 matches done
		 */
		for(int i = 0;i<=8;i+=2)
		{
			matches.add(new Match(playingTeams.get(i%5),playingTeams.get((i+1)%5)));
		}
		/*
		 * setting teams alternatively 
		 * like 0,2....1,3.....2,4....3,0.....4,1
		 * 10 matches done
		 */
		for(int i = 0;i<=4;i++)
		{
			matches.add(new Match(playingTeams.get(i%5),playingTeams.get((i+2)%5)));
		}
		/*
		 * final schedule ---
		 * 0,1 -> 2,3 -> 4,0 -> 1,2 -> 3,4 -> 0,2 -> 1,3 -> 2,4 -> 3,0 -> 4,1 
		 */
	}

	@Override
	public void simulate() {
		int matchesCompleted = 0;
		/*
		 * keeps a count of matches completed and used to print points table after 2 matches
		 */
		System.out.println("SIMULATING-GROUPSTAGE:");
		for(Match match : this.matches)
		{
			System.out.println(match.getTeam1().getName() + " VS " + match.getTeam2().getName());
			match.play();
			Delay.loadingDelay(4);/*
			*it will print something like TeamA VS TeamB
			*							 ....(with delay effect)
			*
			*/
			 /*
			 *  adding goalscorers ...
			 */
			goalScorers.addAll(match.getWinningTeamScorers());
            goalScorers.addAll(match.getLosingTeamScorers());
            /*
             * updating points table...
             */
            for(Row r : pointsTable)
            {
            	if(r.getTeam().getName().equals(match.getWinner().getName()))
            	{
            		r.addWin();
            		r.setGoalsScored(match.getGoalsWinner());
            		r.setGoalsConceded(match.getGoalsLoser());
            	}
            	else if(r.getTeam().getName().equals(match.getLoser().getName()))
            	{
            		r.addLoss();
            		r.setGoalsScored(match.getGoalsLoser());
            		r.setGoalsConceded(match.getGoalsWinner());
            	}
            }
            matchesCompleted++;
            System.out.println(match);//matchstats
            Delay.makeDelay(1000);
          
//            points table at the end of every matchday
     
            if(matchesCompleted%2==0)
            {
            	System.out.println(this.showPointTable());
            }
            Delay.makeDelay(3000);
		}
	}

	public String showPointTable()
	{
		String s = "";
		for(Row r : pointsTable)
    	{
    		s = s.concat(r.toString()).concat("\n");
    	}
		return s;
	}
	@Override
	public ArrayList<Player> getGoalScorers(){
		return new ArrayList<Player>(this.goalScorers);
	}
	
/*
 * this is for getting the top four ranked teams
 * and used to passed them to Knockout constructor....
 */
	public ArrayList<Team> getQualifiedTeam()
	{
		ArrayList<Team>qt = new ArrayList<Team>();
		//after sorting remove last not including last team
		for(int i = 0;i<4;i++)
		{
			qt.add(pointsTable.get(i).getTeam());
		}
		return qt;
	}

	public String toString()
	{
		String res = "GROUPSTAGE SCHEDULE\n";
		for(Match match : this.matches)
		{
			res = res + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + "\n";
		}
		return res;
	}
}