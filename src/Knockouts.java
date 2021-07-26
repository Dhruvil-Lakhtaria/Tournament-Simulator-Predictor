import java.util.ArrayList;

public class Knockouts implements TournamentStage{
    private ArrayList<Team> playingTeams;
    private ArrayList<Match> matches;
    private ArrayList<Player> goalScorers;

    /**constructor*/
    Knockouts(ArrayList<Team> playingTeams){
        this.playingTeams = playingTeams;
        matches = new ArrayList<>();
        goalScorers = new ArrayList<>();
    }

    /**methods implemented from Interface
     * schedule()
     * simulate()
     * getGoalScorers()
     * */

    public void schedule(){
        /**
         * get size of playingTeams to help us identify quarters, semis and finale
         */
        int n = this.playingTeams.size();

        /**
         * adding Match objects in the right order of scheduling the matches
         */

        /**
         * what i have implemented below is an ideal bracket
         * so, in the quarters, the exact order (ideal) of scheduling the matches is
         * A1B4 A3B2 A2B3 A4B1
         * where A1 A2 A3 A4 is the top 4 teams of group A
         * and B1 B2 B3 B4 is the top 4 teams of group B
         *
         * important point to note here is
         * initially, the playingTeams array = {A1, A2, A3, A4, B1, B2, B3, B4}
         * the algo is based on this initial format for the quarters
         * so, whoever is creating Knockouts object,
         * make sure, you stick to this order while passing the playingTeam array in the constructor
         */

        if(n == 8){
            for(int i=0; i<8; i+=2){
                int j = i/4 + i%4;
                matches.add(new Match(playingTeams.get(j), playingTeams.get(7 - j)));
            }
        }

        /**
         * in semis and final, the matches are scheduled just the way they are placed in the playingTeams array
         */

        else{
            for(int i=0; i<n; i+=2){
                matches.add(new Match(playingTeams.get(i), playingTeams.get(i+1)));
            }
        }

        /**Matches are now scheduled!!*/

    }

    public void simulate(){

        for(Match match : matches){
            System.out.println(match.getTeam1().getName() + " VS " + match.getTeam2().getName());
            /**play the match*/
            match.play();

            /**delay*/
            Delay.loadingDelay(5);

            /**update the playingTeams array
             * basically, remove the losing team
             * by the end of the loop, the playingTeams array will be of half the size*/
            playingTeams.remove(match.getLoser());

            /**update the goalScorer array
             * add the goal scorers of the match (from both losing team & winning team)*/
            goalScorers.addAll(match.getWinningTeamScorers());
            goalScorers.addAll(match.getLosingTeamScorers());

            /**print Match stats*/
            System.out.println(match);
            Delay.makeDelay(4_000);
        }

        matches.clear();
    }

    public ArrayList<Player> getGoalScorers(){
        return new ArrayList<>(goalScorers);
    }

    /**toString()*/
    public String toString() {
        String s = "";

        if(playingTeams.size() == 8)
            s += "Quarter-Final\n";
        else if(playingTeams.size() == 4)
            s += "Semi-Final\n";
        else
            s += "Final\n";

        for(Match match : matches){
            s += match.getTeam1().getName() + " vs " + match.getTeam2().getName() + "\n";
        }
        return s;
    }

    /**getters & setters*/
    public ArrayList<Team> getPlayingTeams() {
        return new ArrayList<Team>(playingTeams);
    }

    public void setPlayingTeams(ArrayList<Team> playingTeams) {
        this.playingTeams = playingTeams;
    }

    public ArrayList<Match> getMatches() {
        return new ArrayList<>(matches);
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public void setGoalScorers(ArrayList<Player> goalScorers) {
        this.goalScorers = goalScorers;
    }

}
