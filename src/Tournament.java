import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tournament {
    private GroupStage group1;
    private GroupStage group2;
    private Knockouts k;
    private ArrayList<Player> goalScorers;
    private ArrayList<Team> allTeams;
    private User user;

    private final int INCREMENT_IN_GROUP_STAGE = 5;
    private final int INCREMENT_IN_KNOCKOUTS = 10;

    /**
     * 1. Performs check on user
     * 2. Invokes helper function buildTeam() (to help..build...team XD) 
     * 
     * Less chance that the user check over here will come out true as well,
     * Since the user constructor (which takes care of invalid names) was called before passing the user to tournament.
     * Nevertheless check still necessary
     */
    public Tournament(User user, String filename) {
        if(user == null)
            System.out.println(Color.ANSI_YELLOW + "User is Null!" + Color.ANSI_RESET);

        this.user = user;
        this.allTeams = buildTeam(filename);
        this.goalScorers = new ArrayList<Player>();
    }

    /**
     * 1. initializes a String content = ""
     * 2. appends everything in textfile into content (seperated by a "\n")
     * 3. splits content into data (splits according to "\n")
     * 4. Loops through data, reading values and instantiating objects
     *    - Stores the captains name seperately
     *    - While going through the players and instantiating Player objects, 
     *      if the current players name is the captains name, 
     *      then a refference to that player is assigned to "captain" attribute
     * 5. After creating and adding all Players and managers into the team,
     *    All players are looped through AGAIN, this time to set all of their "team" attribute values
     */
    private ArrayList<Team> buildTeam(String filename){
        File obj = new File(filename);
        Scanner readTeams;
        String content = "";

        try{
            readTeams = new Scanner(obj);
            while (readTeams.hasNextLine())
                content = content.concat(readTeams.nextLine()).concat("\n");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        String data[] = content.split("\n");
        ArrayList<Team> teams = new ArrayList<Team>(data.length/18);

        for(int i = 0, j = 0; i < data.length/18; i++, j+=18) {
            String teamName = data[j];
            String fifaCode = data[j+1].split("-")[1];
            int rank = Integer.parseInt(data[j+2].split("-")[1]);
            String managerName = data[j+4].split("-")[1];
            double managerRating = Double.parseDouble(data[j+4].split("-")[2]);
            Manager manager = new Manager(managerName,managerRating);
            String captainName = data[j+5].split("-")[1];
            ArrayList<Player> players = new ArrayList<Player>();
            Player captain = null;
            int  starPlayers = 0;
            
            for(int k = 7;k<18;k++) {
                String d[] = data[j+k].split("\\|");
                players.add(new Player(d[0],d[2],Integer.parseInt(d[3]),Double.parseDouble(d[1]),Double.parseDouble(d[5])));
                if(Double.parseDouble(d[1]) > 9.5) starPlayers++;
                if(d[0].equals(captainName)) captain = players.get(players.size()-1);
            }

            teams.add(new Team(teamName, fifaCode, captain, manager, rank, starPlayers, players));

            for(Player p : teams.get(i).getPlayers()) p.setTeam(teams.get(i));
        }

        return teams;
    }

    /**
     * makeGroups draws at random from different pots with 2 teams each
     * it prints out an initial "Making groups" with a carriage return at the end.
     * This is done because within the main program workFlow, 
     * after makeGroups() is called in start(),there is nothing else to be done other than outputting the groups
     * which is intern done by another helper function displayGroups()
     */
    private void makeGroups(){
        ArrayList<Team> teamsOfGroup1 = new ArrayList<>();
        ArrayList<Team> teamsOfGroup2 = new ArrayList<>();

        for(int i=0; i<10; i+=2){
            int x = i + (int) (2 * Math.random());
            teamsOfGroup1.add(allTeams.get(x));
            if(x == i) {
                teamsOfGroup2.add(allTeams.get(x+1));
            }
            else if (x == i+1) {
                teamsOfGroup2.add(allTeams.get(x-1));
            }
        }
        group1 = new GroupStage(teamsOfGroup1);
        group2 = new GroupStage(teamsOfGroup2);
        ArrayList<GroupStage> g1g2 = new ArrayList<>();
        g1g2.add(group1);
        g1g2.add(group2);
        System.out.print("         Making Groups     \r");
    }

    /**
     * displayGroups() merely formats the output better and shows all teams in both groups. 
     * this method is called twice in the program right before each group prediction is asked.
     */
    private void displayGroups() {
        Delay.specificDelay(30, 80);
        System.out.print("\n" + String.format("%-28s %-28s", Color.ANSI_UNDERLINE + "GROUP I" + Color.ANSI_RESET, Color.ANSI_UNDERLINE + "GROUP II" + Color.ANSI_RESET) + "\n");
        for (int i = 0; i < allTeams.size() / 2 ; ++i) System.out.println(String.format("%-20s %-20s", group1.getPlayingTeams().get(i).getName(), group2.getPlayingTeams().get(i).getName()));
        System.out.println(".".repeat(30));
    }

    /**
     * a. Start with a message signifying the tournament part of project is starting.
     *    after which, we display all the teams that have qualified for the tournament (all 1-10 teams are displayed in a list).
     * b. After prompting and recieving any key from the user to form groups, we invoke makeGroups() and add the two groups into g1g2 (an ArrayList of groups)
     * c. Loop through groups (explained in detail inside)
     * d. After prompting user to move on to KnockOut Stages, we initialize the teams that qualified into the knockout stages
     * e. Loop through knockoutrounds (explained in detail inside)
     * f. Finally we print that the tournament is over
     */
    public void start(){
        /**a */
        System.out.print("\n" + ("-".repeat(30)) + "\n\nStarting the tournament");
        Delay.specificDelay(3,500);
        System.out.println("\n" + Color.ANSI_UNDERLINE + "Teams Qualified for the Tournament:" + Color.ANSI_RESET);
        for(Team t : this.allTeams) System.out.println(String.format("%-5s", "(" + t.getRank() + ")") + t.getName());
        
        /**b */
        System.out.print(Color.ANSI_CYAN + "\n<Press [KEY] to form Groups>" + Color.ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        String check = sc.nextLine();
        this.makeGroups();
        ArrayList<GroupStage> g1g2 = new ArrayList<>();
        g1g2.add(group1);
        g1g2.add(group2);

        /**
         * c. For each group:
         * 
         * 1. Invoke a makeDelay() to seperate from other groups and then display the two group's teams.
         * 2. Prompt user for either a teamname that he/she wants information about (from said group) a "y/Y" to continue with simulation. 
         *    The information about the team is shown using toString() of the team object and The choice is shown until y/Y is entered.
         * 3. We then prompt the user for prediction (calls userInputAndValidation(Group) to loop until valid prediction is entered).
         * 4. We then prompt the user for scheduling the group folowed by one more prompt for simulating the group
         *    after each of those, group.schedule() and group.simulate() are called respectively
         *    after simulating, we also immediately sort the pointsTable of the group (for safety) and add all its goalScorers to the list maintained here.
         * 5. After each group we print out results of the group wrt the user: 
         *      5.i)    group is over
         *      5.ii)   which all teams qualified, 
         *      5.iii)  whether his team qualified, 
         *      5.iv)   his points.
         */
        for(GroupStage group : g1g2){
            /**1 */
            Delay.makeDelay(300);
            displayGroups();
            
            /**2 */
            do {
                System.out.print(Color.ANSI_CYAN + "Enter [TEAMNAME] from group I" + "I".repeat(g1g2.indexOf(group)) + " whose details are required / press [Y] to continue: " + Color.ANSI_RESET);
                check = sc.nextLine();
                if(!check.strip().equalsIgnoreCase("y")) {
                    if(this.validatePrediction(check.strip(), group)) for (Team t : group.getPlayingTeams()) if (check.strip().equalsIgnoreCase(t.getName()) || check.strip().equalsIgnoreCase(t.getFifaCode())) System.out.print(t);
                    else System.out.println(Color.ANSI_RED + "Invalid Team!" + Color.ANSI_RESET);
                }
            } while (!check.strip().equalsIgnoreCase("y"));
            
            /**3 */
            System.out.print("\n" + Color.ANSI_CYAN + "Enter your prediction for group I"+ "I".repeat(g1g2.indexOf(group)) + ": " + Color.ANSI_RESET);
            String userPrediction = userInputAndValidation(group);
            user.setPredictedTeam(userPrediction);
            System.out.println();
            
            /**4 */
            System.out.print(Color.ANSI_CYAN + "<Press [KEY] to Schedule Matches in Group I" + "I".repeat(g1g2.indexOf(group)) + ">" + Color.ANSI_RESET);
            check = sc.nextLine();
            System.out.println("\n" + Color.ANSI_UNDERLINE + "Schedule of Matches in Group I" + "I".repeat(g1g2.indexOf(group)) + Color.ANSI_RESET + ":");
            System.out.print("Scheduling Matches for Group I" + "I".repeat(g1g2.indexOf(group)) + "\r");
            Delay.makeDelay(450);
            Delay.specificDelay(30 + g1g2.indexOf(group), 70);
            group.schedule();
            System.out.println("\r" + " ".repeat(30 + g1g2.indexOf(group)) + "\r" + group);
            System.out.print(Color.ANSI_CYAN + "<Press [KEY] to Simulate Group I" + "I".repeat(g1g2.indexOf(group)) + ">" + Color.ANSI_RESET);
            check = sc.nextLine();
            group.simulate();
            group.sortPointsTable();
            goalScorers.addAll(group.getGoalScorers());

            /**5 */
            Delay.makeDelay(450);

            /**5.i) */
            System.out.println("\n" + ("-".repeat(9)) + "Group I" + "I".repeat(g1g2.indexOf(group)) + " Over" + ("-".repeat(9)));
            Delay.makeDelay(1000);

            /**5.ii) */
            System.out.println(Color.ANSI_UNDERLINE + "\nTeams Qualified for Knockout Stage from Group I" + "I".repeat(g1g2.indexOf(group)) + Color.ANSI_RESET + ":");
            for(Team t : group.getQualifiedTeam()) {
                if (userPrediction.strip().equalsIgnoreCase(t.getName()) || userPrediction.strip().equalsIgnoreCase(t.getFifaCode())) {
                    System.out.println(t.getName() + " <------");
                    user.updatePoints(INCREMENT_IN_GROUP_STAGE*(4-group.getQualifiedTeam().indexOf(t)));
                }
                else System.out.println(t.getName());
            }

            /**5.iii) */
            if(userPrediction.strip().equalsIgnoreCase(group.getEliminatedTeam().getName()) || userPrediction.strip().equalsIgnoreCase(group.getEliminatedTeam().getFifaCode())){
                Delay.makeDelay(1000);
                String s = Color.ANSI_RED + "\nThe team that you selected (" + group.getEliminatedTeam().getName().toUpperCase() + ") did not make it out of Group " + "I".repeat(g1g2.indexOf(group) + 1) + 
                     " and hence hasn't qualified for the Knockout Stage :(" + "\nBetter luck next time!" + Color.ANSI_RESET;
                System.out.println(s);
            }

            /**5.iv) */
            Delay.makeDelay(1000);
            System.out.println("\n" + user.getName() + "'s Points At End Of Group I" + "I".repeat(g1g2.indexOf(group)) + ": " + user.getPoints() + "\n");
        }

        /**d */
        Delay.makeDelay(2000);
        System.out.print(Color.ANSI_CYAN + "<Press [KEY] to Continue to Knockout Stages>" + Color.ANSI_RESET);
        check = sc.nextLine();
        ArrayList<Team> playingTeamsOfQuarterFinal = new ArrayList<>(group1.getQualifiedTeam());
        playingTeamsOfQuarterFinal.addAll(group2.getQualifiedTeam());
        k = new Knockouts(playingTeamsOfQuarterFinal);

        /**
         * e. For each KnockoutRound:
         * 
         * 1. We invoke makeDelay to seperate each Knockout Stage from one another and then print out the teams available for this coming knockout stage.
         * 2. We prompt and then take users input for Prediction for current stage.
         * 3. We prompt to start scheduling and simulating that stage (both in one prompt, different from how groups was done^)
         *    to reduce the number of breaks and make it feel bit more free.
         *    This way, the next Break or User moment will be only after everything about this stage is done.
         * 4. After getting the prompt, we schedule the stage, print the schedule, and simulate the stage (working of simulate() is in Knockouts.java)
         *    (All extra clutter near the schedule() and simulate() calls are for formatting) 
         * 5. After a stage has been simulated, we add the Knockouts.goalScorers to the list of goalScorers maintained here
         *    We then check whether the users team has moved on to the next stage (or won the final) and print out accordingly (Also prints out the user score)
         */
        for(int i = 0; i < 3; i++) {
            /**1 */
            Delay.makeDelay(450);
        	displayQualifiedTeams();

            /**2 */
            String s;
            switch (k.getPlayingTeams().size()) {
                case 8: s = "Quarter-Final"; break;
                case 4: s = "Semi-Final"; break;
                case 2: s = "Final"; break; 
                default:s = Color.ANSI_YELLOW + "Number of teams < 2 within k.playingTeams" + Color.ANSI_RESET; break;
            }
            System.out.print("\n" + Color.ANSI_CYAN + "Enter your prediction for "+ s + ": " + Color.ANSI_RESET);
            String userPrediction = userInputAndValidation(k);
            user.setPredictedTeam(userPrediction.strip());
            System.out.println();

            /**3 */
            System.out.print(Color.ANSI_CYAN + "<Press [KEY] to Schedule and Simulate Matches in " + s + ">" + Color.ANSI_RESET);
            check = sc.nextLine();
            if(k.getPlayingTeams().size() == 2) {
                System.out.println("\n" + Color.ANSI_UNDERLINE + "Final Match" + Color.ANSI_RESET + ":");
                System.out.print("Scheduling" + s + "\r");
            }
            else {
                System.out.println("\n" + Color.ANSI_UNDERLINE + "Schedule of Matches in " + s + Color.ANSI_RESET + ":");
                System.out.print("Scheduling Matches for " + s + "\r");	
            }

            /**4 */
            Delay.makeDelay(450);
            Delay.specificDelay(23 + s.length(), 70);
            k.schedule();
            System.out.println("\r" + " ".repeat(23 + s.length()) + "\r" + k);
            k.simulate();

            /**5 */
            goalScorers.addAll(k.getGoalScorers());
            checkUserPredictionWithQualifiedTeams(userPrediction,s);
        }

        /**f */
        System.out.print(("-".repeat(30)) + "\n");
        Delay.makeDelay(1000);
        System.out.print("\n" + Color.ANSI_GREEN + String.format("%23s", "TOURNAMENT OVER!") + Color.ANSI_RESET);

    }
    
    /**
     * Displayed the stage followed by the teams that qualified for that stage
     */
    private void displayQualifiedTeams() {
        String s;
        switch (k.getPlayingTeams().size()) {
            case 8: s = Color.ANSI_UNDERLINE + "Quarter-Final Teams" + Color.ANSI_RESET + ":"; break;
            case 4: s = Color.ANSI_UNDERLINE + "Semi-Final Teams" + Color.ANSI_RESET + ":"; break;
            case 2: s = Color.ANSI_UNDERLINE + "Final Teams" + Color.ANSI_RESET + ":"; break; 
            default:s = Color.ANSI_YELLOW + "Number of teams < 2 within k.playingTeams" + Color.ANSI_RESET; break;
        }
        System.out.println(s);
        Delay.specificDelay(20, (8 * 20) / 3);
        System.out.print("\n" + String.format("%-10s  %s", Color.ANSI_UNDERLINE + "Team Name" + Color.ANSI_RESET, Color.ANSI_UNDERLINE + "FIFA Code" + Color.ANSI_RESET) + "\n");
        for (Team t : k.getPlayingTeams()) {
            System.out.println(String.format("%-10s %6s", t.getName(), t.getFifaCode()));
        }
        System.out.println(".".repeat(20));
    }

    /**
     * We call three delays in this function (normal delays, only physical stopping of program, no output) to give user time to read
     * 
     * 1. After printing that the previous stage is over (and the 1st delay), we print the teams remaining for next round.
     * 2. Then after the 2nd delay, we print whether or not the users team was eliminated.
     * 3. Lastly the 3rd delay is followed by the user details
     */
    private void checkUserPredictionWithQualifiedTeams(String userPrediction, String kStage) {
        /**1 */
        Delay.makeDelay(450);
        System.out.println("\n" + ("-".repeat(31 - ((kStage.length() + 5)/2))) + kStage + " Over" + ("-".repeat(31 - ((kStage.length() + 5)/2))));
        Delay.makeDelay(1000);
        String x = (kStage == "Final") ? "\nWinner of Finals" : "\nTeams Qualified for Next Stage";
        System.out.println(Color.ANSI_UNDERLINE + x + Color.ANSI_RESET + ":");
            
        x = (kStage == "Final") ? "" : " <------";
        for (Team t : k.getQualifiedTeams()) {
            if (userPrediction.strip().equalsIgnoreCase(t.getName()) || userPrediction.strip().equalsIgnoreCase(t.getFifaCode())) {
                System.out.println(t.getName() + x);
                user.updatePoints(INCREMENT_IN_KNOCKOUTS);
            }
            else{
                System.out.println(t.getName());
            }
        }

        /**2 */
        for (Team t : k.getEliminatedTeams()) {
        Delay.makeDelay(450);
            if(userPrediction.strip().equalsIgnoreCase(t.getName()) || userPrediction.strip().equalsIgnoreCase(t.getFifaCode())){
                Delay.makeDelay(450);
                String s = "";
                if (kStage == "Finals") {
                    s = Color.ANSI_RED + "\nThe team that you selected (" + t.getName().toUpperCase() + ") lost the " + kStage + 
                               ".\nBut on the bright side, it is the second best team in the world! " + Color.ANSI_RESET;
                }
                else {
                    s = Color.ANSI_RED + "\nThe team that you selected (" + t.getName().toUpperCase() + ") did not make it out of " + kStage + 
                               " and hence hasn't qualified for the Next Stage :(" + Color.ANSI_RESET;
                }
                System.out.println(s);
            }
        }

        /**3 */
        Delay.makeDelay(450);
        System.out.println("\n" + user.getName() + "'s Points At End Of " + kStage + ": " + user.getPoints() + "\n");
    }

    /**
     * validatePrediction(String, Group) loops through all the teams in a particular group and checks if the required team is in that list or not.
     * validatePrediction(String, Knockouts) performs the same function but for all the teams in a particular knockout stage.
     */
    private boolean validatePrediction(String userPrediction, GroupStage g) {
        for(Team t : g.getPlayingTeams()) {
            if(t.getName().equalsIgnoreCase(userPrediction.strip()) || t.getFifaCode().equalsIgnoreCase(userPrediction.strip())) return true;
        }
        return false;
    }

    private boolean validatePrediction(String userPrediction, Knockouts k) {
        for(Team t : k.getPlayingTeams()) {
            if(t.getName().equalsIgnoreCase(userPrediction.strip()) || t.getFifaCode().equalsIgnoreCase(userPrediction.strip())) return true;
        }
        return false;
    }

    /**
     * 1. Since tournament.start() already prompted once for the prediction, we first take the input within userInputAndValidation()
     * 2. then we repeatedly ask the user to enter a prediction until we recieves a valid one.
     *    we call validatePrediction(String, Group) or validatePrediction(String, Knockouts) to check if the predicted team is valid for a particular group or knockout stage.
     * 3. finally we return the valid prediction once we recieve
     */
    private String userInputAndValidation(GroupStage g) {
        Scanner scanner = new Scanner(System.in);
        String prediction = scanner.nextLine();

        while(!validatePrediction(prediction, g)) {
            System.out.println(Color.ANSI_RED + "Invalid Team Name! (Re-enter the Prediction)" + Color.ANSI_RESET);
            System.out.print(Color.ANSI_CYAN + "Prediction: " + Color.ANSI_RESET);
            prediction = scanner.nextLine();
        }
        return prediction;
    }
    
    private String userInputAndValidation(Knockouts k) {
        Scanner scanner = new Scanner(System.in);
        String prediction = scanner.nextLine();

        while(!validatePrediction(prediction, k)) {
            System.out.println(Color.ANSI_RED + "Invalid Team Name! (Re-enter the Prediction)" + Color.ANSI_RESET);
            System.out.print(Color.ANSI_CYAN + "Prediction: " + Color.ANSI_RESET);
            prediction = scanner.nextLine();
        }
        return prediction;
    }

    public ArrayList<Team> getAllTeams() {
        return allTeams;
    }

    public GroupStage getGroup1() {
        return group1;
    }

    public GroupStage getGroup2() {
        return group2;
    }

    public void setAllTeams(ArrayList<Team> allTeams) {
        this.allTeams = allTeams;
    }

    public void setGroup1(GroupStage group1) {
        this.group1 = group1;
    }

    public void setGroup2(GroupStage group2) {
        this.group2 = group2;
    }
}
