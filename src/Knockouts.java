import java.util.ArrayList;

public class Knockouts implements TournamentStage{
    private ArrayList<Team> playingTeams;
    private ArrayList<Match> matches;
    private ArrayList<Team> qualified;

    /**constructor*/
    Knockouts(ArrayList<Team> playingTeams){
        this.playingTeams = playingTeams;
    }

    /**getters & setters*/
    public ArrayList<Team> getPlayingTeams() {
        return playingTeams;
    }

    public void setPlayingTeams(ArrayList<Team> playingTeams) {
        this.playingTeams = playingTeams;
    }


    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }


    public ArrayList<Team> getQualified() {
        return qualified;
    }

    public void setQualified(ArrayList<Team> qualified) {
        this.qualified = qualified;
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

    }

    public ArrayList<Player> getGoalScorers(){
        
    }
}
