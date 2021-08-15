package simulator;

import java.util.Scanner;
import simulator.ui.*;
public class User {
    private String name;
    private int points = 0;
    private String predictedTeam;

    public User(String name) {
        setName(name);
    }

    /**
     * setName will keep on looping, printing out "Invalid Name!" and asking for a new input until a valid input is sent
     * A valid name is the one which comprises only characters of the english alphabet (a -> z)
     * Then the name is assigned to the user
     */
    private void setName(String name) {
        while (!(name.strip().matches("^[a-zA-Z ]*$")) || name.strip().length() == 0){
            System.out.print(Color.ANSI_RED + "Invalid Name!\n" + Color.ANSI_CYAN + "Please Enter Again: " + Color.ANSI_RESET);
            name = new Scanner(System.in).nextLine();
        }
        this.name = name;
    }

    public void setPredictedTeam(String predictedTeam){
        this.predictedTeam = predictedTeam;
    }

    /**update the user points when their prediction matches the outcome of a particular tournament stage*/
    public void updatePoints(int points){
        if (points <= 0) System.out.println(Color.ANSI_YELLOW + "Points is negative or 0" + Color.ANSI_RESET);
        else this.points += points;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public String getPredictedTeam() {
        return predictedTeam;
    }

    public int getPoints() {
        return points;
    }
}
