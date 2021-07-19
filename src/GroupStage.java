import java.util.ArrayList;

public class GroupStage implements TournamentStage {
	ArrayList<Team>playingTeams;
	ArrayList<Match>matches;
	ArrayList<Row>pointsTable;
	
//	Constructor
	public GroupStage(ArrayList<Team>t)
	{
		this.playingTeams = t;
		matches = new ArrayList<Match>();
		pointsTable = new ArrayList<Row>();
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
		
	}

	@Override
	public ArrayList<Player> getGoalScorers() {
		
		return null;
	}

}
