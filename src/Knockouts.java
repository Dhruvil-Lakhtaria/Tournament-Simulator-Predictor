import java.util.ArrayList;

public class Knockouts implements TournamentStage{
    private ArrayList<Team> playingTeams;
    private ArrayList<Match> matches;
    private ArrayList<Player> goalScorers;
    private ArrayList<Team> qualifiedTeams;
    private ArrayList<Team> eliminatedTeams;
    

    /**constructor*/
    Knockouts(ArrayList<Team> playingTeams){
        this.playingTeams = playingTeams;
        matches = new ArrayList<>();
        goalScorers = new ArrayList<>();
        qualifiedTeams = new ArrayList<>();
        eliminatedTeams = new ArrayList<>();
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
            /**copy all the teams from qualifiedTeams to playingTeams in the same order for the next stage 
             * dont do playingTeams = qualifiedTeam
             * cause then, both will reference the same object in memeory, which is not desirable.
             * could have avoided the first foor loop and just written qualifiedTeams instead of playingTeam
             * but this was done intentionally.
             * though logically, it is the same thing, you don't use the term "qualifiedTeams", even before a match is scheduled
             * so if we stick to this definition -->
             * playingTeams is the list of current teams that are playing that stage.
             * qualifiedTeams is the list of teams qualified from the previous stage.
            */
            for(Team t : qualifiedTeams){
                playingTeams.set(qualifiedTeams.indexOf(t), t);
            }
            for(int i=0; i<n; i+=2){
                matches.add(new Match(playingTeams.get(i), playingTeams.get(i+1)));
            }
        }

        qualifiedTeams.clear();
        eliminatedTeams.clear();

        /**Matches are now scheduled!!*/

    }

    /**
     * 1. Print out which match is being played (also doing a lil formatting to show that its being played)
     * 2. playing the match and then printing the match
     * 3. making all required changes to qualified, eliminated and playing teams.
     * 4. adding goalscorers.(For Advaith: your previous code was good and worked, i just made it a bit shorter. if u want you can compare)
     *    If no issue then delete comments or wtv ur call.
     * 5. then the loop continues for every match^
     */
    public void simulate(){

        for(Match match : matches){
            // System.out.println(match.getTeam1().getName() + " VS " + match.getTeam2().getName());
            Delay.makeDelay(450);
            System.out.print("\n" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ":\r");
			Delay.specificDelay(match.getTeam1().getName().length() + 4 + match.getTeam2().getName().length(), 80);

            /**play the match*/
            match.play();
			System.out.print("\r" + match.getTeam1().getName() + " vs " + match.getTeam2().getName() + ": " + match);

            // Delay.loadingDelay(3);

            /**
             * Adding winner to qualifiedTeams, Adding loser to eliminatedTeams, Removing loser from playingTeams
             */
            qualifiedTeams.add(match.getWinner());
            eliminatedTeams.add(match.getLoser());
            playingTeams.remove(match.getLoser());

            /**update the goalScorer array
             * add the goal scorers of the match (from both losing team & winning team)*/
            // ArrayList<Player> scorers = new ArrayList<>();
            // scorers.addAll(match.getWinningTeamScorers());
            // scorers.addAll(match.getLosingTeamScorers());
            // for(Player p : scorers){
            //     if(!(scorers.contains(p))){
            //         this.goalScorers.add(p);
            //     }
            // }
            ArrayList<Player> scorers = new ArrayList<>(match.getWinningTeamScorers());
            scorers.addAll(match.getLosingTeamScorers());
            for(Player p : scorers) if(!(goalScorers.contains(p))) this.goalScorers.add(p);
			
            /**print Match stats*/
            // System.out.println(match);
            // Delay.makeDelay(4_000);
        }
        matches.clear();
    }

    /**
     * For Advaith: 
     * 
     * I have commented out the printing of stage since thats being done in the call-ee itself. If you are ok with this, delete the comments and lmk
     * If not u can delete mine and uncomment urs, and make the necessary changes in Tournament.java
     */
    public String toString() {
        String s = "";
        // if(playingTeams.size() == 8)
        //     s += "Quarter-Finals\n";
        // else if(playingTeams.size() == 4)
        //     s += "Semi-Finals\n";
        // else
        //     s += "Finals\n";
        for(Match match : matches){
            s += match.getTeam1().getName() + " vs " + match.getTeam2().getName() + "\n";
        }
        return s;
    }
    
    public ArrayList<Player> getGoalScorers(){
        return new ArrayList<>(goalScorers);
    }

    /**getters & setters*/
    public ArrayList<Team> getPlayingTeams() {
        return new ArrayList<Team>(playingTeams);
    }

    public ArrayList<Team> getQualifiedTeams() {
        return new ArrayList<Team>(qualifiedTeams);
    }

    public ArrayList<Team> getEliminatedTeams() {
        return new ArrayList<Team>(eliminatedTeams);
    }

    public ArrayList<Match> getMatches() {
        return new ArrayList<>(matches);
    }

    public void setGoalScorers(ArrayList<Player> goalScorers) {
        this.goalScorers = goalScorers;
    }

    public void setPlayingTeams(ArrayList<Team> playingTeams) {
        this.playingTeams = playingTeams;
    }

    public void setQualifiedTeams(ArrayList<Team> qualifiedTeams) {
        this.qualifiedTeams = qualifiedTeams;
    }

    public void setEliminatedTeams(ArrayList<Team> eliminatedTeams) {
        this.eliminatedTeams = eliminatedTeams;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }
}
