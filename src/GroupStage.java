import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

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
		int matchDay = 1,matchCompleted = 0;
		String check;
		/*
		 * keeps a count of matches completed and used to print points table after 2 matches
		 */
		for(Match match : this.matches)
		{
			if(matchCompleted%2 == 0)
			{
				System.out.println("MATCHDAY - "+ matchDay);
				matchDay++;
				Delay.makeDelay(1000);
			}
			System.out.println(match.getTeam1().getName() + " VS " + match.getTeam2().getName());
			match.play();
			Delay.loadingDelay(3);
				for(Player p : match.getWinningTeamScorers())
				{
					boolean notPresent = true;
					for(Player P : this.goalScorers)
					{
						if(p.getName().equals(P.getName()))
						{
							notPresent = false;
							break;
						}
					}
					if(notPresent)
						this.goalScorers.add(p);
				}
				for(Player p : match.getLosingTeamScorers())
				{
					boolean notPresent = true;
					for(Player P : this.goalScorers)
					{
						if(p.getName().equals(P.getName()))
						{
							notPresent = false;
							break;
						}
					}
					if(notPresent)
						this.goalScorers.add(p);
				}
            /*
             * updating points table...
             */
            for(Row r : pointsTable)
            {
            	if(r.getTeam().getName().equals(match.getWinner().getName()))
            	{
            		r.addWin();
            		r.updateGoalsScored(match.getGoalsWinner());
            		r.updateGoalsConceded(match.getGoalsLoser());
            	}
            	else if(r.getTeam().getName().equals(match.getLoser().getName()))
            	{
            		r.addLoss();
            		r.updateGoalsScored(match.getGoalsLoser());
            		r.updateGoalsConceded(match.getGoalsWinner());
            	}
            }
            matchCompleted++;
//            sort the updated points table .... 
            System.out.println(match);//matchstats
            Delay.makeDelay(2000);
//            points table at the end of every matchday
     
            if(matchCompleted%2==0)
            {
            	System.out.print("Enter [1] for POINTS TABLE\n      [2] for TOP SCORER\n      [3] for SCHEDULE\n      [KEY] to continue :");
            	while(true)
            	{
            		int breakCondition = 0;
					Scanner sc = new Scanner(System.in);
            		check = sc.nextLine();
					sc.close();
            		switch(check)
            		{
            		case "1" : 	System.out.println(this.showPointsTable());
            					break;
            		case "2" :
            				   	System.out.println(this.showGoalScorer());
            				   	break;
            		case "3" : 	for(int i = matchCompleted;i<this.matches.size();i++)
			            		{
            						System.out.print(this.matches.get(i).getTeam1().getName() + " vs " + this.matches.get(i).getTeam2().getName() + "\n");
			            		}
            					break;
            		default : 
            					breakCondition = 1;
            					break;
            		}
            		if(breakCondition==1)
            			break;
            		else
            			System.out.print("SELECT FROM GIVEN OPTIONS : ");
            	}
            }
		}
	}

	public void sortPointsTable()
	{
		this.pointsTable.sort(new Comparator<Row>()
    	{
    		public int compare(Row a,Row b)
    		{
    			if(a.getPoints() > b.getPoints())
   					return -1;
    			else if(a.getPoints() < b.getPoints())
    				return 1;
    			else
    			{
    				if(a.getGoalsScored()-a.getGoalsConceded() > b.getGoalsScored()-b.getGoalsConceded())	
    					return -1;
    				else if(a.getGoalsScored()-a.getGoalsConceded() < b.getGoalsScored()-b.getGoalsConceded())
    					return 1;
    				else
    				{
    					if(a.getGoalsScored() > b.getGoalsScored())
    						return -1;
    					else if(a.getGoalsScored() < b.getGoalsScored())
    						return 1;
    					else
    					{
    						if(a.getTeam().getName().compareToIgnoreCase(b.getTeam().getName()) < 0)
    							return -1;
    						else if(a.getTeam().getName().compareToIgnoreCase(b.getTeam().getName()) > 0)
    							return 1;
    						else
    							return 0;
    					}
    				}
    			}
   			}
    	}
	    );
	}
	public void sortScorers()
	{
		this.goalScorers.sort(new Comparator<Player>()
				{
					public int compare(Player a,Player b)
					{
						if(a.getGoals() > b.getGoals())
							return -1;
						else if(a.getGoals() < b.getGoals())
							return 1;
						else
							return 0;
					}
				});
	}
	public String showPointsTable()
	{
		this.sortPointsTable();
		String s = String.format("\t%-20s %-20s %-20s %-20s %-20s %-20s\n", "Team","GoalsScored", "GoalsConceded",
                "W","L","Points");;
		for(Row r : pointsTable)
    	{
    		s = s.concat(r.toString()).concat("\n");
    	}
		return s;
	}
	public String showGoalScorer()
	{
		this.sortScorers();
		String s = "TOP SCORERS :\n";
		for(int i = 0;i<5&&i<this.goalScorers.size();i++)
		{
			s = s + Integer.toString(i+1) +"."+  this.goalScorers.get(i).getName() + " - " + this.goalScorers.get(i).getGoals() + "\n";
		}
		return s;
	}
	@Override
	public ArrayList<Player> getGoalScorers()
	{
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
//  just incase required for checking user 
//	predication with eliminated team
//	delete later if not required
	public Team getEliminatedTeam()
	{
		this.sortPointsTable();
		return this.pointsTable.get(4).getTeam();
	}
	
	/*
	 * note toString of Groupstage is used to print
	 *  the schedule of groupstage call it after sceduling only......
	 */
	
	public String toString()
	{
		String res = "GROUPSTAGE SCHEDULE\n";
		for(Match match : this.matches)
		{
			res = res + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + "\n";
		}
		return res;
	}
	public ArrayList<Team> getPlayingTeams()
	{
		return new ArrayList<Team>(this.playingTeams);
	}
}