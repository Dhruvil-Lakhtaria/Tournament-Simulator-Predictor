import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tournament {
    private GroupStage group1;
    private GroupStage group2;
    private Knockouts quarterFinals;
    private Knockouts semiFinals;
    private Knockouts finals;
    private ArrayList<Player> goalScorers;
    private ArrayList<Team> allTeams;
    private User user;

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
            if(allTeams.indexOf(t) < 5){
                teamsOfGroup1.add(t);
            }
            else
                teamsOfGroup2.add(t);
        }

        group1 = new GroupStage(teamsOfGroup1);
        group2 = new GroupStage(teamsOfGroup2);
    }

    public void start(){

        ArrayList<GroupStage> g1g2 = new ArrayList<>();
        g1g2.add(group1);
        g1g2.add(group2);

        for(GroupStage group : g1g2){
            System.out.println("PREDICTING & SIMULATING GROUP" + g1g2.indexOf(group) + 1);

            String userPrediction;
            System.out.print("Prediction: ");
            Scanner scanner = new Scanner(System.in);
            userPrediction = scanner.nextLine();

            while(!validatePrediction(userPrediction)){
                System.out.println("Your prediction doesn't match with any team.");
                System.out.println("ReEnter the Team Name");
                System.out.print("Prediction: ");

                userPrediction = scanner.nextLine();
            }

            user.setPredictedTeam(userPrediction);

            group.schedule();
            group.simulate();

            group.sortPointsTable();

            if(userPrediction.equalsIgnoreCase(group.getEliminatedTeam().getName())){
                String s = "";
                s += "Group" + 1 + g1g2.indexOf(group) +
                        "\nThe team that you selected in Group1 did NOT qualify for the Knockouts stage :(" +
                        "\nBetter luck next time!\n";
                System.out.println(s);
            }

            else{
                for (Team t : group.getQualifiedTeam()){
                    if(userPrediction.equalsIgnoreCase(t.getName())){
                        int x = group.getQualifiedTeam().indexOf(t);
                        user.updatePoints(5*(4-x));
                    }
                }
            }

            System.out.println(user);
            System.out.println("\n");
            Delay.loadingDelay(5);
        }
    }

    private boolean validatePrediction(String userPrediction) {
        for(Team t : allTeams){
            if(t.getName().equalsIgnoreCase(userPrediction))
                return true;
        }
        return false;
    }
}
