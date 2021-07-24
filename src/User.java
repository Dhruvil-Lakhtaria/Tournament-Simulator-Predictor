public class User {
    private String name;
    private int points = 0;
    private String predictedTeam;

    //constructor
    public User(String name) {
        setName(name);
    }

    //setters
    private void setName(String name) {
        if(name.equals(""))
            System.out.println("No user name is entered!");
        else if(!(name.matches("^[a-zA-Z]*$")))
            System.out.println("User Name has invalid characters. Must contain only alphabets\n");
        this.name = name;
    }

    public void setPredictedTeam(String predictedTeam){
        String[] arr = {"argentina", "brazil", "belgium", "england", "france", "portugal", "spain", "italy", "croatia", "denmark"};
        boolean flag = false;
        for(String s : arr) {
            if (s.equalsIgnoreCase(predictedTeam)) {
                flag = true;
                break;
            }
        }

        if(!flag)
            System.out.println("InValid Team input for Prediction!\n");
        this.predictedTeam = predictedTeam;
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

    @Override
    public String toString() {
        return "User Details" +
                "\nName: " + name +
                "\nPoints: " + points;
    }

    //other methods
    public void updatePoints(int points){
        if(points <= 0)
            System.out.println("Points is negative or 0");
        else this.points += points;
    }
}
