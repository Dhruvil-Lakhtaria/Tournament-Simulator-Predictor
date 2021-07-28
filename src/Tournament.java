import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tournament {
    private GroupStage group1;
    private GroupStage group2;
    private Knockouts k;
    // private Knockouts quarterFinals;
    // private Knockouts semiFinals;
    // private Knockouts finals;
    private ArrayList<Player> goalScorers;
    private ArrayList<Team> allTeams;
    private User user;

    private final int INCREMENT_IN_GROUP_STAGE = 5;
    private final int INCREMENT_IN_KNOCKOUTS = 10;
    public Tournament(User user, String filename) {
        if(user == null)
            System.out.println("User is Null!");

        this.user = user;
        this.allTeams = buildTeam(filename);
        this.goalScorers = new ArrayList<Player>();
    }

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

        ArrayList<Team>t = new ArrayList<Team>(n);

        for(int i = 0,j = 0;i<n;i++,j+=18)
        {
            String teamName = data[j],fifaCode = data[j+1].split("-")[1];
            int rank = Integer.parseInt(data[j+2].split("-")[1]);
            String managerName = data[j+4].split("-")[1];
            double managerRating = Double.parseDouble(data[j+4].split("-")[2]);
            Manager m = new Manager(managerName,managerRating);
            String captainName = data[j+5].split("-")[1];
            ArrayList<Player> p = new ArrayList<Player>();
            Player c = null;
            int  starPlayers = 0;
            for(int k = 7;k<18;k++)
            {
                /*d is data of each player in string array*/
                String d[] = data[j+k].split("\\|");
                p.add(new Player(d[0],d[2],Integer.parseInt(d[3]),Double.parseDouble(d[1]),Double.parseDouble(d[5])));

                if(Double.parseDouble(d[1]) > 9.5)
                    starPlayers++;

                if(d[0].equals(captainName)) {
                    c = p.get(p.size()-1);
                }
            }

            t.add(new Team(teamName, fifaCode,c,m,rank,starPlayers, p));

            for(Player pl : t.get(i).getPlayers()) {
                pl.setTeam(t.get(i));
            }
        }
        return t;
    }

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
        System.out.println("LOADING GROUPS : ");
        Delay.loadingDelay(3);
        for(GroupStage group : g1g2)
        {
        	System.out.println("GROUP - " + (g1g2.indexOf(group) + 1) + "\n");
            for(Team t : group.getPlayingTeams()){
                System.out.println(t.getName());
            	}
            System.out.println();
            Delay.makeDelay(5000);
        }
    }

    public void start(){

        /**Group Stage*/
        System.out.println("\nTOURNAMENT SIMULATOR & PREDICTOR: \n");
        System.out.println("Registered teams are:");
        for(Team t : this.allTeams)
        {
        	System.out.println(t.getName());
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Press  [ENTER] to form Groups : ");
        String check = sc.nextLine();
        
        this.makeGroups();
        
        ArrayList<GroupStage> g1g2 = new ArrayList<>();
        g1g2.add(group1);
        g1g2.add(group2);

        for(GroupStage group : g1g2){
        	System.out.println("GROUP - " + (g1g2.indexOf(group) + 1) + "\n" );
        	System.out.println(group.showPointsTable());
        	while(true)
            {
            	System.out.print("Enter [TEAMNAME] to see detail/[Y] to continue :");
            	check = sc.next();
            	if(check.toLowerCase().equals("y"))
            		break;
            	else
            	{
            		if(this.validatePrediction(check, group))
            		{
            			for(Team t : group.getPlayingTeams())
            			{
            				if(t.getName().equals(check)) {
            					System.out.println(t);
            					break;
            					}
            			}
            		}
            		else
            			System.out.println("Enter valid [TEAMNAME]");
            	}
            }
            System.out.println("ENTER YOUR PREDICTION FOR GROUP - "+ (g1g2.indexOf(group) + 1));
            String userPrediction = userInputAndValidation(group);
            user.setPredictedTeam(userPrediction);
            System.out.println("\n");
            
            /**schedule the matches and display the schedule*/
            System.out.println("SCHEDULING MATCHES FOR GROUP - "+ (g1g2.indexOf(group) + 1));
            group.schedule();
            Delay.loadingDelay(5);
            System.out.println(group);
            System.out.print("Press [ENTER] to Simulate Group - "+ (g1g2.indexOf(group) + 1) + ": ");
            check = sc.nextLine();
            /**simulate all the matches of that group*/
            System.out.println("SIMULATING GROUP - " + (g1g2.indexOf(group) + 1) + "\n");
            group.simulate();

            /**add all goalScorers*/
            goalScorers.addAll(group.getGoalScorers());

            /**display the teams that have qualified for Knockouts stage*/
            System.out.println("\nThe Teams that have qualified for the Knockouts stage from GROUP - "
            + (g1g2.indexOf(group) + 1) + " are: ");
            for(Team t : group.getQualifiedTeam()){
                System.out.println(t.getName());
            }
            System.out.println("\n");

            if(userPrediction.equalsIgnoreCase(group.getEliminatedTeam().getName())){
                String s = "";
                s += "Group - " + (1 + g1g2.indexOf(group)) +
                        "\nThe team that you selected did NOT qualify for the Knockouts stage :(" +
                        "\nBetter luck next time!\n";
                System.out.println(s);
            }
            else{
                for (Team t : group.getQualifiedTeam()){
                    if(userPrediction.equalsIgnoreCase(t.getName())){
                        int x = group.getQualifiedTeam().indexOf(t);
                        user.updatePoints(INCREMENT_IN_GROUP_STAGE*(4-x));
                    }
                }
            }

            System.out.println(user);
            System.out.println("\n");
            Delay.loadingDelay(5);

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
        // sc.close();
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

    /**validatePrediction() is overloaded
     * one for Knockouts
     * other for GroupStage*/
    private boolean validatePrediction(String userPrediction, GroupStage g) {
        for(Team t : g.getPlayingTeams()){
            if(t.getName().equalsIgnoreCase(userPrediction))
                return true;
        }
        return false;
    }
    private boolean validatePrediction(String userPrediction, Knockouts k) {
        for(Team t : k.getPlayingTeams()){
            if(t.getName().equalsIgnoreCase(userPrediction))
                return true;
        }
        return false;
    }

    /**userInputAndValidation() is overloaded
     * one for Knockouts
     * other for GroupStage*/
    private String userInputAndValidation(GroupStage g){
        String prediction;
        System.out.print("Prediction: ");
        Scanner scanner = new Scanner(System.in);
        prediction = scanner.nextLine();

        while(!validatePrediction(prediction, g)){
            System.out.println("Your prediction doesn't match with any team.");
            System.out.println("ReEnter the Team Name");
            System.out.print("Prediction: ");

            prediction = scanner.nextLine();
        }
        // scanner.close();
        return prediction;
    }

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
        // scanner.close();
        return prediction;
    }



    /**getters & setters*/
    public ArrayList<Team> getAllTeams() {
        return allTeams;
    }

    public void setAllTeams(ArrayList<Team> allTeams) {
        this.allTeams = allTeams;
    }

    public GroupStage getGroup1() {
        return group1;
    }

    public void setGroup1(GroupStage group1) {
        this.group1 = group1;
    }

    public GroupStage getGroup2() {
        return group2;
    }

    public void setGroup2(GroupStage group2) {
        this.group2 = group2;
    }
}
