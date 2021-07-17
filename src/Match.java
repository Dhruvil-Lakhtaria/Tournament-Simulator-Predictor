import java.util.ArrayList;

public class Match {
    
    private Team team1, team2;
    private Team winner, loser;
    private int goalsWinner, goalsLoser;
    private ArrayList<Player> winningTeamScorers, losingTeamScorers;

    Match(Team team1, Team team2){

        if (team1 == null || team2 == null){
            System.out.println("\nNull teams in Match constructor");
        }

        this.team1 = team1;
        this.team2 = team2;
    }

    public void play(){

        /**
         * p1 = team1's probability of winning and 
         * p2 = team2's probability of winning 
         * (initially both are equal)
         */

        double p1 = 50.0;
        double p2 = 50.0;

        /**
         * Since Team ranks are reversed (like the lower the number => the stronger the team)
         * if the difference between team 1 and team 2 is positive, team1 is actually weaker than team2
         * hence instead of adding difference, we subtract the difference from 50.0
         */

        p1 -= team1.getRank() - team2.getRank();
        p2 -= team1.getRank() - team2.getRank();

        /**
         * "Math.abs(pX - 50) < 2" 
         * tells us whether the two teams have ranks close to each other ie they have 1 rank difference
         * in which case the full brunt of difference in manager ability will be felt (w = 1)
         * else if theyre not that close, only half the difference will be felt (w = 0.5)
         * the difference we are adding is swapped around for each, so that if for one its positive difference,
         * that team will get an increase while the other one's will decrease
         */

        p1 += (Math.abs(p1 - 50) < 2) ? team1.getManager().getAbility() - team2.getManager().getAbility() : (team1.getManager().getAbility() - team2.getManager().getAbility()) * 0.5;
        p2 += (Math.abs(p2 - 50) < 2) ? team2.getManager().getAbility() - team1.getManager().getAbility() : (team2.getManager().getAbility() - team1.getManager().getAbility()) * 0.5;

        /**
         * as mentioned in the API, affect of starPlayers attribute will depend on the level of the manager
         * if the managers ability for either team is greater than the other 
         * then either 0.75 or 0.25 of the potential of star players of better manager will be added and subtracted from each team
         * if the teams are equal, 
         * then either 0.75 or 0.25 of the potential of difference in star players will be added and subtracted from each team
         * (in the case where they are equal, we do += for both instead of += for one and -= for the other)
         * (because we are adding the weighted difference of star players instead of just the weighted number of star players)
         * (so it will take care of - or + on its own)
         */

        if (team1.getManager().getAbility() > team2.getManager().getAbility()){
            p1 += (team1.getManager().getAbility() > 8.5) ? team1.getStarPlayers() * 0.75 : team1.getStarPlayers() * 0.25;
            p2 -= (team1.getManager().getAbility() > 8.5) ? team1.getStarPlayers() * 0.75 : team1.getStarPlayers() * 0.25;
        }
        else if (team1.getManager().getAbility() < team2.getManager().getAbility()){
            p2 += (team2.getManager().getAbility() > 8.5) ? team2.getStarPlayers() * 0.75 : team2.getStarPlayers() * 0.25;
            p1 -= (team2.getManager().getAbility() > 8.5) ? team2.getStarPlayers() * 0.75 : team2.getStarPlayers() * 0.25;
        }
        else{
            p1 += (team1.getManager().getAbility() > 8.5) ? (team1.getStarPlayers() - team2.getStarPlayers()) * 0.75 : (team1.getStarPlayers() - team2.getStarPlayers()) * 0.25;
            p2 += (team1.getManager().getAbility() > 8.5) ? (team1.getStarPlayers() - team2.getStarPlayers()) * 0.75 : (team1.getStarPlayers() - team2.getStarPlayers()) * 0.25;
        }

        /**
         * for captain and avg Rating attributes, using the same logic (as mentioned in API)
         * if the value of one team is greater than the other, that team gets incremented a set amount while the other loses the same
         */

        p1 += (team1.getCaptain().getPlayerRating() > team2.getCaptain().getPlayerRating()) ? 0.5 : -0.5;
        p2 += (team1.getCaptain().getPlayerRating() > team2.getCaptain().getPlayerRating()) ? -0.5 : 0.5;

        p1 += (team1.getAvgRating() > team2.getAvgRating()) ? 0.25 : -0.25;
        p2 += (team1.getAvgRating() > team2.getAvgRating()) ? -0.25 : 0.25;

        /**
         * with the final p1 and p2 (probability of team1 and team2 winning respectively)
         * we generate random number from [0,100) and check if generated number >= than the probability of one team
         * if yes, then that team wins
         * if not, then that implies generated number <= probability of the other team so the other team wins
         */

        if (Math.random() * 100 >= p1){
            winner = team1;
            loser = team2;
        }
        else{
            winner = team2;
            loser = team1;
        }

        /**
         * for goals scored, we set a 6 goals for a team to score. 
         * so after generating the goals for the winning team (1 is added because the team cant win unless they atleast score one goal)
         * and then we keep looping as long as goalsLoser >= goalsWinner (losing team has to score less)
         * (assignment operator returns the value assigned)
         */

        goalsWinner = 1 + (int) (Math.random() * 7);
        for (goalsLoser = 7; (goalsLoser = (int) (Math.random() * 7)) >= goalsWinner;);

        /**
         * called a private helper function since too much code in one function mightve led to mistakes
         * determineGoalScorers() as the name suggests, determins who all scored from each team
         */

        determineGoalScorers();
    }

    private void determineGoalScorers(){

        /**
         * (since not exactly specified but was discussed in meeting only, this is subject to discussion if needed)
         * two for loops, one to account for all of the winning goals, the other to account for loosing.
         * for each goal we generate a random value from [0,10)
         * this is used as the least rating a player needs to score that particular goal
         * then we keep looping (using random indexs) until we come across a scorer with ability >= requirement
         * we add a goal to the tally of the scorer and if the scorer hasnt already scored, 
         * we add him to the ArrayList for his team's scorers
         */

        for (int i = 0; i < goalsWinner; ++i){
            Player scorer;
            double winGoalProbability = Math.random() * 10.0;
            for (; (scorer = winner.getPlayers().get((int) (Math.random() * 11))).getShootingAbility() < winGoalProbability;);
            scorer.addGoal();
            if (!winningTeamScorers.contains(scorer)){
                winningTeamScorers.add(scorer);
            }
        }
        
        for (int i = 0; i < goalsLoser; ++i){
            Player scorer;
            double loseGoalProbability = Math.random()* 10.0;
            for (; (scorer = loser.getPlayers().get((int) (Math.random() * 11))).getShootingAbility() < loseGoalProbability;);
            scorer.addGoal();
            if (!losingTeamScorers.contains(scorer)){
                losingTeamScorers.add(scorer);
            }
        }
    }

    public String toString() {

        /**
         * first case is if team1 won and team2 lost and other case is vice versa
         * couldve just done winner vs loser order output 
         * but then it would be weird that the winner is always on one side
         * after showing the teams playing and the score line, we show goalscorers for winning and then losing.
         * (if needed we can make them side by side, but not a priority for now)
         */

        String s;
        if (team1 == winner){
            s = winner.getName() + " vs " + loser.getName() + "\n" + 
                winner.getFifaCode() + " " + goalsWinner + " - " + goalsLoser + " " + loser.getFifaCode() + "\n\n" +
                "Winning Goal Scorers:\n";
        }
        else{
            s = loser.getName() + " vs " + winner.getName() + "\n" + 
                loser.getFifaCode() + " " + goalsWinner + " - " + goalsLoser + " " + winner.getFifaCode() + "\n\n" +
                "Winning Goal Scorers:\n";
        }

        for (Player p : winningTeamScorers){
            s += p.getName() + "\n";
        }
        s += "Losing Goal Scorers:\n";
        for (Player p : losingTeamScorers){
            s += p.getName() + "\n";
        }

        return s;
    }

    /**
     * getters and setters
     * (checked for input values for setters)
     * (couldnt think of an invalid case for the winningTeamScorers and losingTeamScorers ArrayLists, any insight?)
     */

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Team getWinner() {
        return winner;
    }

    public Team getLoser() {
        return loser;
    }

    public int getGoalsWinner() {
        return goalsWinner;
    }

    public int getGoalsLoser() {
        return goalsLoser;
    }

    public ArrayList<Player> getWinningTeamScorers() {
        return winningTeamScorers;
    }

    public ArrayList<Player> getLosingTeamScorers() {
        return losingTeamScorers;
    }

    public void setTeam1(Team team1) {
        if (team1 == null){
            System.out.println("\nnull team in setter");
        }
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        if (team1 == null){
            System.out.println("\nnull team in setter");
        }
        this.team2 = team2;
    }

    public void setWinner(Team winner) {
        if (winner == null){
            System.out.println("\nnull winner in setter");
        }
        this.winner = winner;
    }

    public void setLoser(Team loser) {
        if (loser == null){
            System.out.println("\nnull loser in setter");
        }
        this.loser = loser;
    }

    public void setGoalsWinner(int goalsWinner) {
        if (goalsWinner < 0){
            System.out.println("Invalid goalsWinner in setter");
        }
        this.goalsWinner = goalsWinner;
    }

    public void setGoalsLoser(int goalsLoser) {
        if (goalsLoser < 0){
            System.out.println("Invalid goalsLoser in setter");
        }
        this.goalsLoser = goalsLoser;
    }

    public void setWinningTeamScorers(ArrayList<Player> winningTeamScorers) {
        this.winningTeamScorers = winningTeamScorers;
    }

    public void setLosingTeamScorers(ArrayList<Player> losingTeamScorers) {
        this.losingTeamScorers = losingTeamScorers;
    }
}
