import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class User {
    private String name;
    private int points = 0;
    private String predictedTeam;

    public User(String name) {
        setName(name);
    }

    /**
     * setName will keep on looping, printing out "Invalid Name!" and asking for a new input until a valid input is sent
     * Then the name is assigned to the user
     * (the previous version of the code advaith wrote is commented in just in case)
     */
    private void setName(String name) {
        while (!(name.strip().matches("^[a-zA-Z ]*$")) || name.strip().length() == 0){
            System.out.print(Color.ANSI_RED + "Invalid Name!\n" + Color.ANSI_CYAN + "Please Enter Again: " + Color.ANSI_RESET);
            name = new Scanner(System.in).nextLine();
        }
        this.name = name;
        // if(name.equals(""))
        //     System.out.println("No user name is entered!");
        // else if(!(name.matches("^[a-zA-Z]*$")))
        //     System.out.println("User Name has invalid characters. Must contain only alphabets\nPlease Enter Again: ");
        // this.name = name;
    }

    /**
     * All checks made inside setPredictedTeam are just for safety, 
     * The way this method is called from other classes pretty much ensures no chance for invalid teams to be entered
     * But check is necessary nonetheless 
     * 
     * (For refference, in Tournament class' start method, userInputAndValidation() is called before setPredictedTeam()
     * and since userInputAndValidation() accounts (and loops for) invalid cases, we can rest assured we get valid team names here)
     */
    public void setPredictedTeam(String predictedTeam){
        String[] arr = {"argentina", "brazil", "belgium", "england", "france", "portugal", "spain", "italy", "croatia", "denmark",
                        "arg",       "bra",    "bel",     "eng",     "fra",    "por",      "spa",   "ita",   "cro",     "den"};
        boolean flag = false;
        for(String s : arr) {
            if (s.equalsIgnoreCase(predictedTeam.strip())) {
                flag = true;
                break;
            }
        }

        if(!flag) {
            System.out.println(Color.ANSI_YELLOW + "Invalid Team input for Prediction!" + Color.ANSI_RESET);
        }

        this.predictedTeam = predictedTeam;
    }

    /**
     * Check here is for safety too, less chance it will happen.
     */
    public void updatePoints(int points){
        if (points <= 0) System.out.println(Color.ANSI_YELLOW + "Points is negative or 0" + Color.ANSI_RESET);
        else this.points += points;
    }

    @Override
    public String toString() {
        return "User Details" +
                "\nName: " + name +
                "\nPoints: " + points;
    }

    //getters
    public String getName() {
        return name;
    }

    public String getPredictedTeam() {
        return predictedTeam;
    }

    public int getPoints() {
        return points;
    }
}
