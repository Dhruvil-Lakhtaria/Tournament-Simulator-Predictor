package simulator.components;

public class Row {
    private Team team;
    private int points, goalsScored, goalsConceded, wins, losses;

    public Row(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public int getPoints() {
        return points;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public int getWins() {
        return wins;
    }

    public void updateGoalsScored(int goalsScored) {
        this.goalsScored += goalsScored;
    }

    public void updateGoalsConceded(int goalsConceded) {
        this.goalsConceded += goalsConceded;
    }

    public void addWin() {
        wins++;
        points += 3;
    }

    public void addLoss() {
        losses++;
    }
    
    @Override
    public String toString() {
        return String.format("| %-10s | %12s | %14s | %-1s | %-1s | %6s |", team.getName(), Integer.toString(goalsScored) + " ".repeat(6), Integer.toString(goalsConceded) + " ".repeat(7),
                                wins, losses, Integer.toString(points) + " ".repeat(2));
    }
}
