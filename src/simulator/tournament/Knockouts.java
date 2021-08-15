package simulator.tournament;
import simulator.components.*;
import java.util.ArrayList;
import simulator.ui.*;

public class Knockouts implements TournamentStage{
    private ArrayList<Team> playingTeams;
    private ArrayList<Match> matches;
    private ArrayList<Player> goalScorers;
    private ArrayList<Team> qualifiedTeams;
    private ArrayList<Team> eliminatedTeams;

    Knockouts(ArrayList<Team> playingTeams){
        this.playingTeams = playingTeams;
        matches = new ArrayList<>();
        goalScorers = new ArrayList<>();
        qualifiedTeams = new ArrayList<>();
        eliminatedTeams = new ArrayList<>();
    }

    /**
     * schedule() creates Match objects and fills them into the matches ArrayList in the correct order (keeping the tournament brackets in mind)
     *
     * 1. we get the size of playingTeams ArrayList to know whether the stage is a quarter-final, semi-final or a final.
     * 2. if we are scheduling for the quarterfinals (n == 8): 
     *          - Initially, the playingTeams ArrayList = {A1, A2, A3, A4, B1, B2, B3, B4}
     *            where A1 A2 A3 A4 is the top 4 teams of group A
     *              &   B1 B2 B3 B4 is the top 4 teams of group B
     *          - The matchups in quarters are = {A1B4, A3B2, A2B3, A4B1}
     *    if we are scheduling for semifinals or finals (n != 8):
     *          - Matches are scheduled in the same way as they are placd in playingTeams ArrayList
     */
    @Override
    public void schedule(){
        int n = this.playingTeams.size();

        if(n == 8){
            for(int i=0; i<8; i+=2) {
                int j = i/4 + i%4;
                matches.add(new Match(playingTeams.get(j), playingTeams.get(7 - j)));
            }
        }
        else{
            for(int i=0; i<n; i+=2){
                matches.add(new Match(playingTeams.get(i), playingTeams.get(i+1)));
            }
        }

        qualifiedTeams.clear();
        eliminatedTeams.clear();
    }

    /**
     * simulate() loops for every match in matches ArrayList
     *
     * 1. Print out which match is being played
     * 2. playing the match and then printing the match
     * 3. making all required changes to qualified, eliminated and playing teams.
     * 4. adding goalscorers.
     *
     * After the for loop we set the playing teams for the next round.
     */
    public void simulate(){
        for(Match match : matches){
            /**1 */
            Delay.makeDelay(500);
            System.out.print("\n" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ":\r");
            Delay.specificDelay(match.getTeam1().getName().length() + 4 + match.getTeam2().getName().length(), 160);

            /**2 */
            match.play();
            System.out.print("\r" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ": " + match);

            /**3 */
            qualifiedTeams.add(match.getWinner());
            eliminatedTeams.add(match.getLoser());
            playingTeams.remove(match.getLoser());

            /**4 */
            ArrayList<Player> scorers = new ArrayList<>(match.getWinningTeamScorers());
            scorers.addAll(match.getLosingTeamScorers());
            for(Player p : scorers) if(!(goalScorers.contains(p))) this.goalScorers.add(p);
        }
        /**copy each team from qualifiedTeam to playingTeam to maintain the same order of matches */
        for(Team t : qualifiedTeams){
            playingTeams.set(qualifiedTeams.indexOf(t), t);
        }
        matches.clear();
    }

    public String toString() {
        String s = "";
        for(Match match : matches){
            s += match.getTeam1().getName() + " vs " + match.getTeam2().getName() + "\n";
        }
        return s;
    }

    public ArrayList<Team> getPlayingTeams() {
        return new ArrayList<Team>(playingTeams);
    }

    public ArrayList<Team> getQualifiedTeams() {
        return new ArrayList<Team>(qualifiedTeams);
    }

    public ArrayList<Team> getEliminatedTeams() {
        return new ArrayList<Team>(eliminatedTeams);
    }

    public ArrayList<Player> getGoalScorers(){
        return goalScorers;
    }
}
