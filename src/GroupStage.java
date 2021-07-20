import java.util.ArrayList;

public class GroupStage implements TournamentStage {
	ArrayList<Team>playingTeams;
	ArrayList<Match>matches;
	ArrayList<Row>pointsTable;
	 private ArrayList<Player> goalScorers;
//	Constructor
	public GroupStage(ArrayList<Team>teams)
	{
		this.playingTeams = teams;
		matches = new ArrayList<Match>();
		pointsTable = new ArrayList<Row>();
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
			Delay.loadingDelay(4);/*
			it will print something like TeamA VS TeamB
										 ....(with delay effect)
			*/
			match.play();
			/*
			 * adding goalscorers ...
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
            	for(Row r : pointsTable)
            	{
            		System.out.println(r);
            	}
            }
            Delay.makeDelay(3000);
		}
	}

	@Override
	public ArrayList<Player> getGoalScorers() {
		
		return null;
	}

}
