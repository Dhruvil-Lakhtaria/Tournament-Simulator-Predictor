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

    public int getLosses() {
        return losses;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public void updateGoalsScored(int goalsScored) {
        this.goalsScored += goalsScored;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public void updateGoalsConceded(int goalsConceded) {
        this.goalsConceded += goalsConceded;
    }

    public void addWin() {
        wins++;
        points += 3;
    }

    public void addWins(int wins) {
        this.wins += wins;
        points += wins*3;
    }

    public void setWins(int wins) {
        this.wins = wins;
        points = wins*3;
    }

    public void addLoss() {
        losses++;
    }

    public void addLosses(int losses) {
        this.losses += losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    /**
     * For Abdullah:
     * 
     * changed the formatting a liiiittle bit. to match the changes in GroupStage's showPointsTable() or smthn. 
     * Unfortunately i frgt to comment out ur inititial code (though its pretty much the same just changing the numbers in format specifiers)
     * So if ur fine w this then ok, but if ur not, idm pls change however u wish, its ur class afterall.
     * 
     * Just a headsup, whatever change u make in Row, ull have to make the appropriate change in GroupStage's showPointsTable() method as well
     */

    @Override
    public String toString() {
        return String.format("| %-10s | %12s | %14s | %-1s | %-1s | %6s |", team.getName(), Integer.toString(goalsScored) + " ".repeat(6), Integer.toString(goalsConceded) + " ".repeat(7),
                                wins, losses, Integer.toString(points) + " ".repeat(2));
    }
}
