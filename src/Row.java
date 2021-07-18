public class Row {
    private Team team;
    private int points, goalsScored, goalsConceded, wins, losses;

    public Row(Team team) {
        this.team = team;
        this.points = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
        this.wins = 0;
        this.losses = 0;
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

    public int getLosses() {
        return losses;
    }

    public void goalScored() {
        goalsScored++;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored += goalsScored;
    }

    public void goalConceded() {
        goalsConceded++;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded += goalsConceded;
    }

    public void addWin() {
        wins++;
        points += 3;
    }

    public void setWins(int wins) {
        this.wins += wins;
        points += wins*3;
    }

    public void addLoss() {
        losses++;
    }

    public void setLosses(int losses) {
        this.losses += losses;
    }

    @Override
    public String toString() {
        return String.format("\t%-20s %-20s %-20s %-20s %-20s %-20s", team.getName(), goalsScored, goalsConceded,
                                wins, losses, points);
    }
}
