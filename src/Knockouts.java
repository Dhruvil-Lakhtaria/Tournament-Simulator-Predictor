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
        int n = playingTeams.size();

        /**
         * adding Match objects in the right order of scheduling the matches
         * by right order, I mean, as per playingTeams array,
         * Team 1 vs team 8, Team 2 vs Team 7 etc.
         */
        for(int i=0; i < n/2; i++){
            matches.add(new Match(playingTeams.get(i), playingTeams.get(n - i - 1)));
        }

        /**Matches are now scheduled!!*/

    }

    public void simulate(){

        for(Match match : matches){
            /**play the match*/
            match.play();

            /**delay*/
            Delay.makeDelay(4_000);

            /**update the playingTeams array
             * basically, remove the losing team
             * by the end of the loop, the playingTeams array will be of half the size*/
            playingTeams.remove(match.getLoser());

            /**update the goalScorer array
             * add the goal scorers of the match (from both losing team & winning team)*/
            goalScorers.addAll(match.getWinningTeamScorers());
            goalScorers.addAll(match.getLosingTeamScorers());

            /**print Match stats*/
            System.out.println(match.toString());
        }
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

    public ArrayList<Player> getGoalScorers(){
        return new ArrayList<>(goalScorers);
    }

    public void setGoalScorers(ArrayList<Player> goalScorers) {
        this.goalScorers = goalScorers;
    }

}
