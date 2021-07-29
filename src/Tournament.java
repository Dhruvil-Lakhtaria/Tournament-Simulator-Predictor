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
    /**
     * (For advaith..) what are ur plans with these 3 Knockouts below? 
     * just thought id point out incase u missed something
     */
    // private Knockouts quarterFinals;
    // private Knockouts semiFinals;
    // private Knockouts finals;

    private final int INCREMENT_IN_GROUP_STAGE = 5;
    private final int INCREMENT_IN_KNOCKOUTS = 10;

    /**
     * 1. Perrforms check on user
     * 2. Invokes helper function buildTeam() (to help..build...team XD) 
     * 
     * Less chance that the user check over here will come out true as well,
     * Since the user constructor (which takes care of invalid names) was called before passing the user to tournament.
     * Nevertheless check still necessary
     * 
     * (Note there are no delays called here)
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
     * 
     * (Note that there are no Delays called here)
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
        }

        String data[] = content.split("\n");
        int n = data.length/18;//n is no of teams

        ArrayList<Team> teams = new ArrayList<Team>(n);

        for(int i = 0, j = 0; i < n; i++, j+=18)
        {
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
            for(int k = 7;k<18;k++)
            {
                /*d is data of each player in string array*/
                String d[] = data[j+k].split("\\|");
                players.add(new Player(d[0],d[2],Integer.parseInt(d[3]),Double.parseDouble(d[1]),Double.parseDouble(d[5])));

                if(Double.parseDouble(d[1]) > 9.5)
                    starPlayers++;

                if(d[0].equals(captainName)) {
                    captain = players.get(players.size()-1);
                }
            }

            teams.add(new Team(teamName, fifaCode, captain, manager, rank, starPlayers, players));

            for(Player p : teams.get(i).getPlayers()) {
                p.setTeam(teams.get(i));
            }
        }
        return teams;
    }

    /**
     * makeGroups behaviorally works the same as earlier (drawing at random from different pots with 2 teams each)
     * however now it prints out an initial "Making groups" with a carriage return at the end.
     * This is done because within the main program workFlow, 
     * after makeGroups() is called in start(),there is nothing else to be done other than outputting the groups
     * which is intern done by another helper function displayGroups()
     * 
     * Also note that there are no Delays called here.
     * 
     * (Old code commented for refference / if needed)
     */
    public void makeGroups(){
        ArrayList<Team> teamsOfGroup1 = new ArrayList<>();
        ArrayList<Team> teamsOfGroup2 = new ArrayList<>();

        for(int i=0; i<10; i+=2){
            int x = i + (int)(2*Math.random());
            teamsOfGroup1.add(allTeams.get(x));
            if(x == i){
                teamsOfGroup2.add(allTeams.get(x+1));
            }
            else if (x == i+1){
                teamsOfGroup2.add(allTeams.get(x-1));
            }
        }
        group1 = new GroupStage(teamsOfGroup1);
        group2 = new GroupStage(teamsOfGroup2);
        ArrayList<GroupStage> g1g2 = new ArrayList<>();
        g1g2.add(group1);
        g1g2.add(group2);
        System.out.print("         Making Groups     \r");
        // displayGroups();
        // for(GroupStage group : g1g2)
        // {
        // 	System.out.println("GROUP - " + (g1g2.indexOf(group) + 1) + "\n");
        //     for(Team t : group.getPlayingTeams()){
        //         System.out.println(t.getName());
        //     	}
        //     System.out.println();
        //     Delay.makeDelay(5000);
        // }
    }

    /**
     * displayGroups() merely formats the output better and shows all teams in both groups. 
     * this method is called twice in the program right before each group prediction is asked. 
     * 
     * This code actually calls a static method of Delay class called specificDelay, 
     * which plays a big part in the formatting (or atleast how to output appears on the screen)
     * 
     * (actually called just once in code but its in a loop so :P)
     */
    private void displayGroups() {
        Delay.specificDelay(30, 80);
        System.out.print("\n" + String.format("%-28s %-28s", Color.ANSI_UNDERLINE + "GROUP I" + Color.ANSI_RESET, Color.ANSI_UNDERLINE + "GROUP II" + Color.ANSI_RESET) + "\n");
        for (int i = 0; i < allTeams.size() / 2 ; ++i) {
            System.out.println(String.format("%-20s %-20s", group1.getPlayingTeams().get(i).getName(), group2.getPlayingTeams().get(i).getName()));
        }
        System.out.println(".".repeat(30));
    }

    public void start(){
        /**
         * Start with a message signifying the tournament part of project is starting 
         * followed by another call to specificDelay() (defined in Delay.java) who basically prints out the "..." at the end of "Starting the tournament"
         * after which, we display all the teams that have qualified for the tournament (all 1-10 teams are displayed in a list)
         */
        System.out.print("\n" + ("-".repeat(30)) + "\n\nStarting the tournament");
        Delay.specificDelay(3,500);
        System.out.println("\n" + Color.ANSI_UNDERLINE + "Teams Qualified for the Tournament:" + Color.ANSI_RESET);
        for(Team t : this.allTeams) {
        	System.out.println(String.format("%-5s", "(" + t.getRank() + ")") + t.getName());
        }
        /**
         * Shows a prompt to start forming groups, we take in the next line, 
         * We dont check for invalid inputs here, didnt think we need to be that strict about [ENTER]
         * We then make the groups by invoking makeGroups() and add said groups to an array of groups g1g2
         * g1g2 array of groups are then looped through, 
         * effectively performing all (showing details of teams, taking prediction & simulating) actions of each group
         * 
         * Note that makeGroups only makes the groups. does NOT print out the groups.
         */
        // Scanner sc = new Scanner(System.in);
        System.out.print(Color.ANSI_CYAN + "\n<Press [ENTER] to form Groups>" + Color.ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        String check = sc.nextLine();
        
        this.makeGroups();
        
        ArrayList<GroupStage> g1g2 = new ArrayList<>();
        g1g2.add(group1);
        g1g2.add(group2);

        for(GroupStage group : g1g2){
            /**
             * Invoked makeDelay (doesnt print anything, simply physical delay) at the beginning of every loop to seperate it from the remaining parts of the program
             * followed by printing out both groups (both groups are displayed both times, feels like a more whole picture of the group stages tbh..)
             */
        	// System.out.println("GROUP - " + (g1g2.indexOf(group) + 1) + "\n" );
        	// System.out.println(group.showPointsTable());
            Delay.makeDelay(100);
            displayGroups();
            /**
             * The do while loop below is used to repeatedly prompt the user for either a [TEAMNAME] or a [Y] (both case insensitive)
             * if they need to see details of a certain team or continute respectively.
             * 
             * if something that is not "Y" is entered, validatePrediction(String, Group) checks if the String entered is one of the Teams in current group
             * if it is a team, we find that team in the group and print its details by invoking toString()
             * else we print error message saying they entered an invalid team, and ask them to re-enter
             * 
             * The loop keeps on repeating until they enter "y/Y"
             */
            do {
                System.out.print(Color.ANSI_CYAN + "Enter [TEAMNAME] from group " + (g1g2.indexOf(group) + 1) + " whose details are required / press [Y] to continue: " + Color.ANSI_RESET);
                check = sc.nextLine();
                if(!check.equalsIgnoreCase("y")) {
                    if(this.validatePrediction(check, group)){
                        for (Team t : group.getPlayingTeams()) {
                            if (check.equalsIgnoreCase(t.getName())){
                                System.out.print(t);
                            }
                        }
                    }                    
                    else {
                        System.out.println(Color.ANSI_RED + "Invalid Team!" + Color.ANSI_RESET);
                    }
                }
            } while (!check.equalsIgnoreCase("y"));
            /**
             * We first prompt user for prediction for current group by first giving a prompt and then calling userInputAndValidation(Group)
             * which will keep asking the user for a prediction until he enters a valid team from the current group.
             * the private method returns the String containing prediction.
             * 
             * We then prompt the User to Shcedule the matches (not strict on [ENTER], anything will work)
             * after few output formatting lines, we invoke another makeDelay (doesnt output anything, only physical delay)
             * which is then followed by a specificDelay which will write over the previous text (hence why we kept normal delay earlier to allow user to read)
             * after calling the group.schedule() method, we print out the group using its toString().
             * 
             * Finally we prompt the user for Simulating the group (Details about output from simulate will be in GroupStage.java)
             * after simulating, we - once again sort the points table of the group for safety and 
             *                      - add all the goalScorers from that group to the list maintained in this class
             */
            System.out.print("\n" + Color.ANSI_CYAN + "Enter your prediction for group "+ "I".repeat(g1g2.indexOf(group) + 1) + ": " + Color.ANSI_RESET);
            String userPrediction = userInputAndValidation(group);
            user.setPredictedTeam(userPrediction);
            System.out.println("\n");
            
            System.out.print(Color.ANSI_CYAN + "<Press [ENTER] to Schedule Matches in Group " + "I".repeat(g1g2.indexOf(group) + 1) + ">" + Color.ANSI_RESET);
            check = sc.nextLine();
            System.out.println("\n" + Color.ANSI_UNDERLINE + "Schedule of Matches in Group " + "I".repeat(g1g2.indexOf(group) + 1) + Color.ANSI_RESET + ":");
            System.out.print("Scheduling Matches for Group " + "I".repeat(g1g2.indexOf(group) + 1) + "\r");
            Delay.makeDelay(450);
            Delay.specificDelay(30 + g1g2.indexOf(group), 70);
            group.schedule();
            System.out.println("\r" + " ".repeat(30 + g1g2.indexOf(group)) + "\r" + group);

            System.out.print(Color.ANSI_CYAN + "<Press [ENTER] to Simulate Group " + "I".repeat(g1g2.indexOf(group) + 1) + ">" + Color.ANSI_RESET);
            check = sc.nextLine();
            group.simulate();
            group.sortPointsTable();
            goalScorers.addAll(group.getGoalScorers());

            /**
             * We print out a statement signifying that a particular Group is over
             * and print out the teams that have qualified for next round. (We print an arrow next to our team if its qualified and update points as well)
             * if users predicted team is the one thats eliminated, after the qualified teams, we print a message saying so (no need to update points since team lost)
             * 
             * Finally after a small delay again, we print the users current score after current group.
             * And then the loop continues until all groups are over.
             */
            System.out.println("-".repeat(9) + "Group " + "I".repeat(g1g2.indexOf(group) + 1) + " Over" + ("-".repeat(9)));
            System.out.println(Color.ANSI_UNDERLINE + "\nTeams Qualified for Knockout Stage from GROUP " + "I".repeat(g1g2.indexOf(group) + 1) + Color.ANSI_RESET + ":");
            
            for(Team t : group.getQualifiedTeam()) {
                if (userPrediction.equalsIgnoreCase(t.getName())) {
                    System.out.println(t.getName() + " <------");
                    user.updatePoints(INCREMENT_IN_GROUP_STAGE*(4-group.getQualifiedTeam().indexOf(t)));
                }
                else{
                    System.out.println(t.getName());
                }
            }

            if(userPrediction.equalsIgnoreCase(group.getEliminatedTeam().getName())){
                String s = Color.ANSI_RED + "\nThe team that you selected (" + userPrediction + ") did not make it out of Group " + "I".repeat(g1g2.indexOf(group) + 1) + 
                     " and hence hasn't qualified for the Knockout Stage :(" + "\nBetter luck next time!" + Color.ANSI_RESET;
                // s += "Group - " + (1 + g1g2.indexOf(group)) +
                //         "\nThe team that you selected did NOT qualify for the Knockouts stage :(" +
                //         "\nBetter luck next time!\n";
                System.out.println(s);
            }

            Delay.makeDelay(500);
            System.out.println("\nUsers Points At End Of Group " + "I".repeat(g1g2.indexOf(group) + 1) + ": " + user.getPoints() + "\n");
        }


        /**Knockouts Stage*/
        ArrayList<Team> playingTeamsOfQuarterFinal = new ArrayList<>(group1.getQualifiedTeam());
        playingTeamsOfQuarterFinal.addAll(group2.getQualifiedTeam());

        k = new Knockouts(playingTeamsOfQuarterFinal);
        for(int i = 0; i < 3; i++) {

        	displayQualifiedTeams();
            System.out.println("\n");

            /**ask the user for his/her prediction
             * validate the prediction
             * set the user's prediction*/
            String uPrediction = userInputAndValidation(k);
            user.setPredictedTeam(uPrediction);
            
            /**schedule the matches and display the schedule*/
            k.schedule();
            System.out.println(k);

            /**simulate the matches in that stage*/
            k.simulate();

            /**add all goalScorers*/
            goalScorers.addAll(k.getGoalScorers());

            /**check if the predictedTeam has qualified for the next stage
             * if yes, increment the user's score
             * if no, then say better luck next time or something like that*/
            checkUserPredictionWithQualifiedTeams(uPrediction);

            /**display the user's score*/
            System.out.println(user);
            System.out.println("\n");
            Delay.makeDelay(5000);
        }
    }
    
    /**helper methods*/
    private void displayQualifiedTeams() {
        if(k.getPlayingTeams().size() == 8)
        {
        	System.out.println("QUARTER-FINAL TEAMS - ");
            for(Team t : k.getPlayingTeams()){
                System.out.println(t.getName() + " (" + t.getFifaCode() + ")");
            }
            System.out.print("\n");
        }
    	else if(k.getPlayingTeams().size() == 4)
    	{
           System.out.println("SEMI-FINAL TEAMS - ");
            for(Team t : k.getPlayingTeams()){
                System.out.println(t.getName() + " (" + t.getFifaCode() + ")");
            }
            System.out.print("\n");
        }

        /**this is for finals..
         * for the time being, i have kept it simple itself
         * but yeah, extra info could be displayed here
         * akheel dhruvil, tell me what extra things you wanna add*/
        else if(k.getPlayingTeams().size() == 2){
            System.out.println("FINALISTS - ");
            for(Team t : k.getPlayingTeams()){
                System.out.println(t.getName() + " (" + t.getFifaCode() + ")");
            }
        }

        else if (k.getPlayingTeams().size() == 1){
            System.out.println(k.getPlayingTeams().get(0).getName()
                    + " (" + k.getPlayingTeams().get(0).getFifaCode() + ")"
                    + "WON the Tournament!!\n");
            System.out.println("\n----other final details-----\n");
        }
        else{
            System.out.println("\n\n\nsomething is wrong\n\n");
        }

    }

    private void checkUserPredictionWithQualifiedTeams(String userPrediction) {
        boolean flag = false;
        for(Team t : k.getPlayingTeams()){
            if(userPrediction.equalsIgnoreCase(t.getName())){
                user.updatePoints(INCREMENT_IN_KNOCKOUTS);
                System.out.println("Hurray!! Your predicted team made it to the next stage!\n");
                flag = true;
                break;
            }
        }

        if(!flag){
            String s = "";
            if(k.getPlayingTeams().size() == 4){
                s += "semi-finals";
            }

            else if(k.getPlayingTeams().size() == 2)
                s += "finals";

            else {
                System.out.println("Your predicted Team did NOT win the final. Don't be disheartened," +
                        " Your team is the Runners-up of the Tournament.\n");
                return;
            }

            System.out.println("Unfortunately, your predicted team didn't qualify for the " + s +
                    "\nbetter luck next time\n");
        }
    }

    /**
     * validatePrediction(String, Group) loops through all the teams in a particular group and checks if the required team is in that list or not.
     * validatePrediction(String, Knockouts) performs the same function but for all the teams in a particular knockout stage.
     */
    private boolean validatePrediction(String userPrediction, GroupStage g) {
        for(Team t : g.getPlayingTeams()) {
            if(t.getName().equalsIgnoreCase(userPrediction)) return true;
        }
        return false;
    }

    private boolean validatePrediction(String userPrediction, Knockouts k) {
        for(Team t : k.getPlayingTeams()) {
            if(t.getName().equalsIgnoreCase(userPrediction)) return true;
        }
        return false;
    }

    /**
     * 1. Since tournament.start() already prompted once for the prediction, we first take the input within userInputAndValidation()
     * 2. then we repeatedly ask the user to enter a prediction until we recieves a valid one.
     *    we call validatePrediction(String, Group) to check if the predicted team is valid for a particular group.
     * 3. finally we return the valid prediction once we recieve
     */
    private String userInputAndValidation(GroupStage g){
        Scanner scanner = new Scanner(System.in);
        String prediction = scanner.nextLine();

        while(!validatePrediction(prediction, g)) {
            System.out.println(Color.ANSI_RED + "Invalid Team Name! (Re-enter the Prediction)" + Color.ANSI_RESET);
            System.out.print(Color.ANSI_CYAN + "Prediction: " + Color.ANSI_RESET);
            prediction = scanner.nextLine();
        }
        return prediction;
    }
    /**
     * Knockoutstage (havent reached here yet.)
     */
    private String userInputAndValidation(Knockouts k){
        String prediction;
        System.out.print("Prediction: ");
        Scanner scanner = new Scanner(System.in);
        prediction = scanner.nextLine();

        while(!validatePrediction(prediction, k)){
            System.out.println("Your prediction doesn't match with any team.");
            System.out.println("ReEnter the Team Name");
            System.out.print("Prediction: ");

            prediction = scanner.nextLine();
        }
        return prediction;
    }



    /**
     * Getters and Setters
     */
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
