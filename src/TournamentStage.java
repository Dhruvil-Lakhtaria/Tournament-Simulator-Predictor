import java.util.ArrayList;

public interface TournamentStage {
    void schedule();
    void simulate();
    ArrayList<Player> getGoalScorers();
}
