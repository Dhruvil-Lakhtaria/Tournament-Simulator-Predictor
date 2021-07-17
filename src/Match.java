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
         * 
         */

        p1 += (team1.getCaptain().getPlayerRating() > team2.getCaptain().getPlayerRating()) ? 0.5 : -0.5;
        p2 += (team1.getCaptain().getPlayerRating() > team2.getCaptain().getPlayerRating()) ? -0.5 : 0.5;

        p1 += (team1.getAvgRating() > team2.getAvgRating()) ? 0.25 : -0.25;
        p2 += (team1.getAvgRating() > team2.getAvgRating()) ? -0.25 : 0.25;

        if (Math.random() * 100 >= p1){
            winner = team1;
            loser = team2;
        }
        else{
            winner = team2;
            loser = team1;
        }

        goalsWinner = 1 + (int) (Math.random() * 7);
        for (goalsLoser = 7; (goalsLoser = (int) (Math.random() * 7)) < goalsWinner;);

        determineGoalScorers();
    }

    private void determineGoalScorers(){
        double winGoalProbability = Math.random() * 10.0;
        double loseGoalProbability = Math.random()* 10.0;

        for (int i = 0; i < goalsWinner; ++i){
            Player scorer;
            for (; (scorer = winner.getPlayers().get((int) (Math.random() * 11))).getShootingAbility() < winGoalProbability;);
            scorer.addGoal();
            winningTeamScorers.add(scorer);
        }
        
        for (int i = 0; i < goalsLoser; ++i){
            Player scorer;
            for (; (scorer = loser.getPlayers().get((int) (Math.random() * 11))).getShootingAbility() < loseGoalProbability;);
            scorer.addGoal();
            losingTeamScorers.add(scorer);
        }
    }

    public String toString() {
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