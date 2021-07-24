import java.util.ArrayList;

public class Match {
    
    private Team team1, team2;
    private Team winner, loser;
    private int goalsWinner, goalsLoser;
    private ArrayList<Player> winningTeamScorers, losingTeamScorers;
    private ArrayList<Integer> winningTeamPlayerGoals, losingTeamPlayerGoals;

    // public static void main(String[] args) {

    //     Team[] t = teamBuilder.buildTeam();

    //     Match m1 = new Match(t[0], t[1]);
    //     m1.play();
    //     System.out.println(m1);
    // }

    Match(Team team1, Team team2){

        winningTeamScorers = new ArrayList<Player>();
        losingTeamScorers = new ArrayList<Player>();
        winningTeamPlayerGoals = new ArrayList<Integer>();
        losingTeamPlayerGoals = new ArrayList<Integer>();

        if (team1 == null || team2 == null){
            System.out.println("\nNull teams in Match constructor");
        }

        this.team1 = team1;
        this.team2 = team2;

        winner = null;
        loser = null;
        goalsWinner = 0;
        goalsLoser = 0;
    }

    public void play(){

        /**
         * p1 = team1's probability of winning and 
         * p2 = team2's probability of winning 
         * (initially both are equal)
         * 
         * in all the explanations, read p1 as p and ignore p2.
         */

        double p = 50.0;

        /**
         * Since Team ranks are reversed (like the lower the number => the stronger the team)
         * if the difference between team 1 and team 2 is positive, team1 is actually weaker than team2
         * hence instead of adding difference, we subtract the difference from 50.0
         */

        p -= team1.getRank() - team2.getRank();

        /**
         * "Math.abs(pX - 50) < 2" 
         * tells us whether the two teams have ranks close to each other ie they have 1 rank difference
         * in which case the full brunt of difference in manager ability will be felt (w = 1)
         * else if theyre not that close, only half the difference will be felt (w = 0.5)
         * the difference we are adding is swapped around for each, so that if for one its positive difference,
         * that team will get an increase while the other one's will decrease
         */

        p += (Math.abs(p - 50) < 2) ? team1.getManager().getAbility() - team2.getManager().getAbility() : (team1.getManager().getAbility() - team2.getManager().getAbility()) * 0.5;

        /**
         * as mentioned in the API, affect of starPlayers attribute will depend on the level of the manager
         * if the managers ability for either team is greater than the other 
         * then either 0.75 or 0.25 of the potential of star players of better manager will be added and subtracted from each team
         * if the team's manager Abilities are equal, 
         * then either 0.75 or 0.25 of the potential of difference in star players will be added and subtracted from each team
         * (in the case where they are equal, we do += for both instead of += for one and -= for the other)
         * (because we are adding the weighted difference of star players instead of just the weighted number of star players)
         * (so it will take care of - or + on its own)
         */

        if (team1.getManager().getAbility() > team2.getManager().getAbility()){
            p += (team1.getManager().getAbility() > 8.5) ? team1.getStarPlayers() * 0.75 : team1.getStarPlayers() * 0.25;
        }
        else if (team1.getManager().getAbility() < team2.getManager().getAbility()){
            p -= (team2.getManager().getAbility() > 8.5) ? team2.getStarPlayers() * 0.75 : team2.getStarPlayers() * 0.25;
        }
        else{
            p += (team1.getManager().getAbility() > 8.5) ? (team1.getStarPlayers() - team2.getStarPlayers()) * 0.75 : (team1.getStarPlayers() - team2.getStarPlayers()) * 0.25;
        }

        /**
         * for captain and avg Rating attributes, using the same logic (as mentioned in API)
         * if the value of one team is greater than the other, that team gets incremented a set amount while the other loses the same
         */

        p += (team1.getCaptain().getPlayerRating() > team2.getCaptain().getPlayerRating()) ? 0.5 : -0.5;

        p += (team1.getAvgRating() > team2.getAvgRating()) ? 0.25 : -0.25;

        /**
         * with the final p1 and p2 (probability of team1 and team2 winning respectively), think of everything below p1 till 0 being team1 and the rest being team2 
         * we generate random number from [0,100) and check if generated number < than the probability of one team
         * if yes, then that team wins
         * if not, then that implies generated number >= probability of the other team so the other team wins
         * (also added a check for 100 to make sure our logic in the algorithm is correct)
         * (if it is correct after testing we can just remove p2 and p1 and replace it with just one single p)
         */

        if (Math.random() * 100 < p){
            winner = team1;
            loser = team2;
        }
        else{
            winner = team2;
            loser = team1;
        }

        /**
         * for goals scored, we set a cap of 5 goals for a team to score. 
         * we randomize the number of goals scored but if its a super high number,
         * we run another random function to descide wether that super high score is counted or not (because they are rare)
         * then we assign the value for goalsLoser 
         * (assignment operator returns the value assigned)
         */

        boolean flag = true;
        while (flag){
            if ((goalsWinner = 1 + (int) (Math.random() * 5)) >= 4){
                if ((Math.random() * 3) < 2){
                    flag = false;
                }
            }
            else{
                flag = false;
            }
        }

        goalsLoser = (int) (Math.random() * goalsWinner);

        /**
         * called a private helper function since too much code in one function mightve led to mistakes
         * determineGoalScorers() as the name suggests, determins who all scored from each team
         */

        determineGoalScorers();
    }

    private void determineGoalScorers(){

        /**
         * ---------------------- initial method how implemented (can be found in earlier commit) --------------------------
         * (since not exactly specified but was discussed in meeting only, this can be ignored or kept for discussion if needed)
         * two for loops, one to account for all of the winning goals, the other to account for loosing.
         * for each goal we generate a random value from [0,10)
         * this is used as the least rating a player needs to score that particular goal
         * then we keep looping (using random indexs) until we come across a scorer with ability >= requirement
         * we add a goal to the tally of the scorer and if the scorer hasnt already scored, 
         * we add him to the ArrayList for his team's scorers
         * -----------------------------------------------------------------------------------------------------------------
         * 
         * we create two ArrayLists (one with probabilities of winners scoring and another with losers)
         * by probabilities we mean we weighted ability of the players in that team, where the weight of the ability is the value of the ability itself
         * ie, an abiliity 10 will have a weight 10 & an ability 5 will have a weight 5
         * since a player of ability 10 is way more likely to score than a player with ability 5.
         * we add these values repeatedly into the ArrayLists.
         */


        ArrayList<Double> winGoalProbabilities = new ArrayList<>();
        ArrayList<Double> loseGoalProbabilities = new ArrayList<>();

        for (Player p : winner.getPlayers()){
            if (!winGoalProbabilities.contains(p.getShootingAbility())){
                for (int i = 0; i < p.getShootingAbility(); ++i){
                    winGoalProbabilities.add(p.getShootingAbility());
                }
            }
        }

        for (Player p : loser.getPlayers()){
            if (!loseGoalProbabilities.contains(p.getShootingAbility())){
                for (int i = 0; i < p.getShootingAbility(); ++i){
                    loseGoalProbabilities.add(p.getShootingAbility());
                }
            }
        }

        /**
         * By here, the entering of values into the ArrayList is over, and now within two loops (one for winners one for losers)
         * we pick one of these values from the probabilities list as the probability. this probability is taken as the minimum ability a player must have to score
         * (By virtue of the abilities being their own weights, the higher weights obviously have more chance of being picked)
         * then we loop through the players of each team RANDOMLY until we come across a player who crosses that threshold ability/probability we set.
         * this player is chosen as the scorer.
         */


        for (int i = 0; i < goalsWinner; ++i){
            Player scorer;
            double winGoalProbability = winGoalProbabilities.get((int) (Math.random() * winGoalProbabilities.size()));
            for (; (scorer = winner.getPlayers().get((int) (Math.random() * winner.getPlayers().size()))).getShootingAbility() < winGoalProbability;);
            scorer.addGoal();
            if (!winningTeamScorers.contains(scorer)){
                winningTeamScorers.add(scorer);
                winningTeamPlayerGoals.add(1);
            }
            else{
                winningTeamPlayerGoals.set(winningTeamScorers.indexOf(scorer), winningTeamPlayerGoals.get(winningTeamScorers.indexOf(scorer)) + 1);
            }
        }
        
        for (int i = 0; i < goalsLoser; ++i){
            Player scorer;
            double loseGoalProbability = loseGoalProbabilities.get((int) (Math.random() * loseGoalProbabilities.size()));
            for (; (scorer = loser.getPlayers().get((int) (Math.random() * winner.getPlayers().size()))).getShootingAbility() < loseGoalProbability;);
            scorer.addGoal();
            if (!losingTeamScorers.contains(scorer)){
                losingTeamScorers.add(scorer);
                losingTeamPlayerGoals.add(1);
            }
            else{
                losingTeamPlayerGoals.set(losingTeamScorers.indexOf(scorer), losingTeamPlayerGoals.get(losingTeamScorers.indexOf(scorer)) + 1);
            }
        }

        /**
         * in the above section if the player hasnt scored yet ive added a goal 1 to the <winning/loosing>TeamPlayerGoals
         * and if the player already has scored, ive incremented his already existing value
         */
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
            s = winner.getFifaCode() + " " + goalsWinner + " - " + goalsLoser + " " + loser.getFifaCode() + "\n\n" +
                "Winning Goal Scorers:\n";
        }
        else{
            s = loser.getFifaCode() + " " + goalsLoser + " - " + goalsWinner + " " + winner.getFifaCode() + "\n\n" +
                "Winning Goal Scorers:\n";
        }

        for (Player p : winningTeamScorers){
            s += p.getName() + " x (" + winningTeamPlayerGoals.get(winningTeamScorers.indexOf(p)) + ")\n";
        }
        if (goalsLoser != 0){
            s += "\nLosing Goal Scorers:\n";
            for (Player p : losingTeamScorers){
                s += p.getName() + " x (" + losingTeamPlayerGoals.get(losingTeamScorers.indexOf(p)) + ")\n";
            }
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
        if (winner == null){
            System.out.println("\nMatch hasnt been played yet");
        }
        return winner;
    }

    public Team getLoser() {
        if (loser == null){
            System.out.println("\nMatch hasnt been played yet");
        }
        return loser;
    }

    public int getGoalsWinner() {
        if (winner == null){
            System.out.println("\nMatch hasnt been played yet");
        }
        return goalsWinner;
    }

    public int getGoalsLoser() {
        if (loser == null){
            System.out.println("\nMatch hasnt been played yet");
        }
        return goalsLoser;
    }

    public ArrayList<Player> getWinningTeamScorers() {
        if (winner == null){
            System.out.println("\nMatch hasnt been played yet");
        }
        return new ArrayList<Player>(winningTeamScorers);
    }

    public ArrayList<Player> getLosingTeamScorers() {
        if (loser == null){
            System.out.println("\nMatch hasnt been played yet");
        }
        return new ArrayList<Player>(losingTeamScorers);
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
            System.out.println("\nInvalid goalsWinner in setter");
        }
        this.goalsWinner = goalsWinner;
    }

    public void setGoalsLoser(int goalsLoser) {
        if (goalsLoser < 0){
            System.out.println("\nInvalid goalsLoser in setter");
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
