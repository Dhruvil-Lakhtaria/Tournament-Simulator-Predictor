import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tournament {
    private GroupStage group1;
    private GroupStage group2;
    private Knockouts k;
//    private Knockouts quarterFinals;
//    private Knockouts semiFinals;
//    private Knockouts finals;
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
                    c = new Player(d[0],d[2],Integer.parseInt(d[3]),Double.parseDouble(d[1]),Double.parseDouble(d[5]));
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

        for(Team t : allTeams){
            if(allTeams.indexOf(t)%2 == 0){
                teamsOfGroup1.add(t);
            }
            else
                teamsOfGroup2.add(t);
        }

        group1 = new GroupStage(teamsOfGroup1);
        group2 = new GroupStage(teamsOfGroup2);
    }

    public void start(){

        /**Group Stage*/
        ArrayList<GroupStage> g1g2 = new ArrayList<>();
        g1g2.add(group1);
        g1g2.add(group2);

        for(GroupStage group : g1g2){
            /**display the playingTeams
             * based on the playingTeams, the user will predict one team
             * which he thinks will make it to the next round*/
            System.out.println("Group" + (g1g2.indexOf(group) + 1));
            for(Team t : group.getPlayingTeams()){
                System.out.println(t.getName());
            }
            System.out.println("\n");

            System.out.println("PREDICTING & SIMULATING GROUP" + (g1g2.indexOf(group) + 1));
            System.out.println("Based on the above stats, pick one team which you think will qualify for Knockouts stage");
            String userPrediction = userInputAndValidation();
            user.setPredictedTeam(userPrediction);

            /**schedule the matches and display the schedule*/
            group.schedule();
            System.out.println(group);

            /**simulate all the matches of that group*/
            group.simulate();

            /**sort the points table*/
            group.sortPointsTable();

            /**display the teams that have qualified for Knockouts stage*/
            for(Team t : group.getQualifiedTeam()){
                System.out.println(t.getName());
            }

            if(userPrediction.equalsIgnoreCase(group.getEliminatedTeam().getName())){
                String s = "";
                s += "Group" + 1 + g1g2.indexOf(group) +
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
        ArrayList<Team> playingTeams = new ArrayList<>(group1.getQualifiedTeam());
        playingTeams.addAll(group2.getQualifiedTeam());

        k = new Knockouts(playingTeams);
        for(int i = 0; i < 3; i++) {

            /**display the playingTeams
             * based on the playingTeams, the user will predict one team
             * which he thinks will make it to the next round*/
            for (Team t : playingTeams) {
                System.out.println(t.getName());
            }
            System.out.println("\n");


            /**ask the user for his/her prediction
             * validate the prediction
             * set the user's prediction*/
            String uPrediction = userInputAndValidation();
            user.setPredictedTeam(uPrediction);


            /**schedule the matches and display the schedule*/
            k.schedule();
            System.out.println(k);


            /**simulate the matches in that stage*/
            k.simulate();


            /**after simulating all the matches in that stage
             * display the teams which qualified for the next stage*/
            displayQualifiedTeams();


            /**check if the predictedTeam has qualified for the next stage
             * if yes, increment the user's score
             * if no, then say better luck next time or something like that*/
            checkUserPredictionWithQualifiedTeams(uPrediction);


            /**display the user's score*/
            System.out.println(user);
            System.out.println("\n");
            Delay.loadingDelay(5);

//        /**display the playingTeams
//         * based on the playingTeams, the user will predict one team
//         * which he thinks will make it to the next round*//*
//        for(Team t : playingTeams){
//            System.out.println(t.getName());
//        }
//        System.out.println("\n");
//
//        *//**create the quarter final object
//         * schedule the quarters*//*
//        quarterFinals = new Knockouts(playingTeams);
//        quarterFinals.schedule();
//
//        *//**the quarter final is scheduled as follows:
//         * display the schedule*//*
//        System.out.println("the quarter final is scheduled as follows: ");
//        System.out.println(quarterFinals);
//
//        *//**ask the user for his prediction
//         * validate the prediction*//*
//        System.out.println("Based on the above stats, pick one team which you think will qualify for semis");
//        String userPrediction = userInputAndValidation();
//        user.setPredictedTeam(userPrediction);
//
//        *//**simulate the quarters*//*
//        quarterFinals.simulate();
//
//        *//**after simulating all the matches in that stage
//         * display the teams which qualified for the next stage*//*
//        System.out.println("The teams which have qualified for semis are: ");
//        for(Team t : quarterFinals.getPlayingTeams()){
//            System.out.println(t.getName() + " (" + t.getFifaCode() + ")");
//        }
//
//        *//**check if the predictedTeam has qualified for the next stage
//         * if yes, increment the user's score
//         * if no, then say better luck next time or something like that*//*
//        boolean flag = false;
//        for(Team t : quarterFinals.getPlayingTeams()){
//            if(userPrediction.equalsIgnoreCase(t.getName())){
//                user.updatePoints(INCREMENT_IN_QUARTERS);
//                System.out.println("Hurray!!\nYour predicted team made it to the next stage!");
//                flag = true;
//                break;
//            }
//        }
//        if(!flag){
//            System.out.println("Unfortunately, your predicted team didn't qualify for the semis" +
//                    "\nbetter luck next time");
//        }
//
//        *//**display the user scores*//*
//        System.out.println(user);
//        System.out.println("\n");
//        Delay.loadingDelay(5);*/
//
//       String uPrediction = userInputAndValidation();
//      user.setPredictedTeam(uPrediction);
//     k.schedule();
//        k.simulate();
//       checkUserPredictionWithQualifiedTeams(k, uPrediction);
        }

    }

    private void displayQualifiedTeams() {
        if(k.getPlayingTeams().size() == 4){
            System.out.println("Teams that won the Quarter-Final and qualified for Semi-Final are: ");
            for(Team t : k.getPlayingTeams()){
                System.out.println(t.getName() + " (" + t.getFifaCode() + ")");
            }
        }

        /**this is for finals..
         * for the time being, i have kept it simple itself
         * but yeah, extra info could be displayed here
         * akheel dhruvil, tell me what extra things you wanna add*/
        else if(k.getPlayingTeams().size() == 2){
            System.out.println("Teams that won the Semi-Final and will be playing the grand Finale are: ");
            for(Team t : k.getPlayingTeams()){
                System.out.println(t.getName() + " (" + t.getFifaCode() + ")");
            }
        }

    }

    private void checkUserPredictionWithQualifiedTeams(String userPrediction) {
        boolean flag = false;
        for(Team t : k.getPlayingTeams()){
            if(userPrediction.equalsIgnoreCase(t.getName())){
                user.updatePoints(INCREMENT_IN_KNOCKOUTS);
                System.out.println("Hurray!!\nYour predicted team made it to the next stage!");
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

            System.out.println("Unfortunately, your predicted team didn't qualify for the " + s +
                    "\nbetter luck next time");
        }
    }

    private boolean validatePrediction(String userPrediction) {
        for(Team t : allTeams){
            if(t.getName().equalsIgnoreCase(userPrediction))
                return true;
        }
        return false;
    }

    private String userInputAndValidation(){
        String prediction;
        System.out.print("Prediction: ");
        Scanner scanner = new Scanner(System.in);
        prediction = scanner.nextLine();

        while(!validatePrediction(prediction)){
            System.out.println("Your prediction doesn't match with any team.");
            System.out.println("ReEnter the Team Name");
            System.out.print("Prediction: ");

            prediction = scanner.nextLine();
        }
        return prediction;
    }
}
