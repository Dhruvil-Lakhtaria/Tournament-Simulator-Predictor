import java.util.ArrayList;

public class Team {
    private String name, fifaCode;
    private Player captain;
    private Manager manager;
    private int rank, starPlayers;
    private ArrayList<Player> players;

    public Team(String name, String fifaCode, Player captain, Manager manager,
                int rank, int starPlayers, ArrayList<Player> players) {

        this.name = name;
        this.fifaCode = fifaCode;
        this.captain = captain;
        this.manager = manager;
        this.rank = rank;
        this.starPlayers = starPlayers;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public String getFifaCode() {
        return fifaCode;
    }

    public Player getCaptain() {
        return captain;
    }

    public Manager getManager() {
        return manager;
    }

    public int getRank() {
        return rank;
    }

    public int getStarPlayers() {
        return starPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public double getAvgRating() {
        double sum = 0;
        for(Player p : players) {
            sum += p.getPlayerRating();
        }
        return  sum/players.size();
    }

    @Override
    public String toString() {
        
        String out = "Team " + name + "\n" +
                    "Fifa Code: " + fifaCode + '\n' +
                    "World Ranking: " + rank + "\n" +
                    "Manager: " + manager.getName() + '\n' +
                    "Captain: " + captain.getName() + '\n' +
                    "Players:\n";

        for(Player p : players) {
            out += p + "\n";
        }

        return out;
    }
}
