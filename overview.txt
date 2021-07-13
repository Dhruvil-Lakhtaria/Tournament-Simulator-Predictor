   Tournament Simulator
Goal: To simulate a football league amongst top nation by scheduling match fixture and simulating the tournament 

Project is made more user-interactive and gamified by adding the feature of user to predict….

Java File/Classes: - 
Rough Overview (Subject to change) – 
Team – datatype representing each team
Player – datatype representing each player 
Scheduling part – contain static methods to schedule league fixtures and knockout fixtures
Tournament – class containing main method and representing whole tournament and the main application.
User  – A class keeping record of state of each user who is predicting (users prediction and points).
Input data – through text file 



FLOW OF APPLICATION –
First display teams and scheduled fixture 
User Prediction for group stage
Group stage fixture simulation 





Then after league stage schedule the knockouts
User prediction 
Simulate knockouts



10/7/2021

IT-150 Project

I. Program Flow:
	i)Game/project name and about.
	ii)Ask user his/her name.
	iii)Show user tournament groups.
	iv)User will be shown the participating team's data.
	loop {
	  v)User will be asked to make prediction for stage/stages.
	  vi)Run simulation for stage.
	  vii)Show results of the stage
	  viii)Update points.
	  ix)Show matchups for next stage
	}
	x)Show tournament winner 
	xi)username : points.

II. Program Structure/Basic API:
	i)Classes:
	
		User -
			fields: name, points, prediction
			methods: toString(),
				 getters and setters.

		Tournament -
			fields: Groups, Knockouts, Final (TBD), Player[] goalScorers, Team[] allTeams, User
			methods: start(), 
				 makeGroups(),
				 getters and setters.
		
		
		Interface TournamentStage {
			schedule()
			simulate()
			getGoalscorers()
		}
		
		Group implements TournamentStage - 			
			fields: Team[5], Match[] matches, Row[5] pointsTable
			methods: schedule(),			//creates Match objects (keeping in mind teams can't have consecutive matches)
								//fills in matches array and then prints the schedule
				 simulate(),			//runs all matches in matches
				 				//prints details of every match in each matchday
								//prints points table at end of every matchday
				 qualifiedTeams(),		//returns top 4 teams in pointsTable
		
		Knockouts implements TournamentStage - 
			fields: Team[] playingTeams, Match[] matches, Team[] qualified 
			methods: schedule(), 			//similar to Group.schedule (brackets order must be maintained)
				 simulate(),			//similar to Group.simulate() (no pointsTable to output)
				 qualifiedTeams(), 		//returns qualified teams.
		
		Row -
			fields: Team, points, goalsScored, goalsConceded, wins, losses
			methods: toString(),
				 getters and setters.
		
		Match -
			fields: Team team1, Team winner, goalsWinner, Player[] winningTeamScorers,
				Team team2, Team loser,	 goalsLoser,  Player[] losingTeamScorers,  String stadium (TBD)  
			methods: play(),			//fields determining outcomes
								//1. int teamRank
								//2. int starPlayers
								//3. double captainAbility 
								//4. double managerAbility
								//5. int ratingDiff
				 toString(),
				 getters and setters.

		Team -
			fields: Player[] players, String name, String fifaCode, Player Captain, Manager manager, double rank, int starPlayers	
			methods: toString(),
				 getters and setters.
		
		Player -	
			fields: String name, String Club, int playerRating, boolean isStarPlayer, double shootingAbility, int goals, boolean isCaptain, Team team 
			methods: toString(),
				 getters and setters.
		
		Manager -
			fields: String name, double ability, double experience
			methods: toString()
				 getters and setters




```
Group.simulate() output structure for reference:
					{
					  Match day #1
					  toString() of Match Object {
					  team x vs team y
					}
					  delay
					  team x won
					  delay
					  next match
					  .
					  .
					  . 
					  5 times
					  
					  print pointsTable 
					  delay
					  Match day #2
					  .
					  .
					  print pointsTable
					}
```
