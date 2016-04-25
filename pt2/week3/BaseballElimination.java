import java.util.HashMap;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
  private int[] wins;
  private int[] losses;
  private int[] remaining;
  private int[][] versus;
  private boolean[] isEliminated;
  private HashMap<String, Integer> teams;
  private HashMap<String, ArrayList<String>> eliminationCerts;

  // create a baseball division from given filename in format specified below
  public BaseballElimination(String filename) {

    In in = new In(filename);
    int count = in.readInt();
    wins = new int[count];
    losses = new int[count];
    remaining = new int[count];
    versus = new int[count][count];
    isEliminated = new boolean[count];
    teams = new HashMap<String, Integer>();
    eliminationCerts = new HashMap<String, ArrayList<String>>();

    for (int i = 0; i < count; i++) {
      teams.put(in.readString(), i);
      wins[i] = in.readInt();
      losses[i] = in.readInt();
      remaining[i] = in.readInt();
      for (int j = 0; j < count; j++) {
        versus[i][j] = in.readInt();
      }
    }

    triviallyEliminate();
    for (String t : teams()) {
      fancyEliminate(t);
    }
  }

  // number of teams
  public int numberOfTeams() {
    return teams.size();
  }

  // all teams
  public Iterable<String> teams() {
    return teams.keySet();
  }

  // number of wins for given team
  public int wins(String team) {
    if (teams.get(team) == null) { throw new IllegalArgumentException("Team not found."); }

    return wins[teams.get(team)];
  }

  // number of losses for given team
  public int losses(String team) {
    if (teams.get(team) == null) { throw new IllegalArgumentException("Team not found."); }

    return losses[teams.get(team)];
  }

  // number of remaining games for given team
  public int remaining(String team) {
    if (teams.get(team) == null) { throw new IllegalArgumentException("Team not found."); }

    return remaining[teams.get(team)];
  }

  // number of remaining games between team1 and team2
  public int against(String team1, String team2) {
    if (teams.get(team1) == null || teams.get(team2) == null) { throw new IllegalArgumentException("Team not found."); }

    return versus[teams.get(team1)][teams.get(team2)];
  }

  // is given team eliminated?
  public boolean isEliminated(String team) {
    if (teams.get(team) == null) { throw new IllegalArgumentException("Team not found."); }

    return isEliminated[teams.get(team)];
  }

  // subset R of teams that eliminates given team; null if not eliminated
  public Iterable<String> certificateOfElimination(String team) {
    if (teams.get(team) == null) { throw new IllegalArgumentException("Team not found."); }

    return eliminationCerts.get(team);

  }

  private void triviallyEliminate() {
    // for (int i = 0; i < numberOfTeams(); i++) {
    //   int maxWins = wins[i] + remaining[i];
    //   for (int j = 0; j < numberOfTeams(); j++) {
    //     if (maxWins < wins[j]) {
    //       isEliminated[i] = true;
    //     }
    //   }
    // }

    for (String t : teams()) {
      ArrayList<String> eliminatingTeams = new ArrayList<String>();
      int maxWins = wins(t) + remaining(t);
      for (String t2 : teams()) {
        if (maxWins < wins(t2)) {
          isEliminated[teams.get(t)] = true;
          eliminatingTeams.add(t2);
        }
      }
      if (eliminatingTeams.size() > 0) { eliminationCerts.put(t, eliminatingTeams); }
      else { eliminatingTeams = null; }
    }

  }

  private void fancyEliminate(String team) {
    if (teams.get(team) == null) { throw new IllegalArgumentException("Team not found."); }
    if (isEliminated(team)) { return; }

    int vertexCount = 2 + numberOfTeams() + ((numberOfTeams() - 1) * (numberOfTeams() - 1) + numberOfTeams() - 1) / 2;
    FlowNetwork fn = new FlowNetwork(vertexCount);
    int loops = 0;
    int teamId = teams.get(team);
    for (int i = 0; i < numberOfTeams(); i++) {
      for (int j = i + 1; j < numberOfTeams(); j++) {
        if (i == teamId || j == teamId) { }
        else {
          FlowEdge gamesEdge = new FlowEdge(0, loops + 2 + numberOfTeams(), versus[i][j]);
          FlowEdge winsEdge1 = new FlowEdge(loops + 2 + numberOfTeams(), i + 2, Integer.MAX_VALUE);
          FlowEdge winsEdge2 = new FlowEdge(loops + 2 + numberOfTeams(), j + 2, Integer.MAX_VALUE);
          fn.addEdge(gamesEdge);
          fn.addEdge(winsEdge1);
          fn.addEdge(winsEdge2);
        }
        loops += 1;
      }
    }
    for (int i = 0; i < numberOfTeams(); i++) {
      if (i == teamId) { continue; }
      FlowEdge sinkEdge = new FlowEdge(i + 2, 1, wins[teamId] + remaining[teamId] - wins[i]);
      fn.addEdge(sinkEdge);
    }
    FordFulkerson ff = new FordFulkerson(fn, 0, 1);
    ArrayList<String> eliminatingTeams = new ArrayList<String>();
    for (String t : teams()) {
      if (teams.get(t) == teamId) { continue; }
      if (ff.inCut(teams.get(t) + 2)) {
        isEliminated[teamId] = true;
        eliminatingTeams.add(t);
      }
    }
    if (eliminatingTeams.size() > 0) { eliminationCerts.put(team, eliminatingTeams); }
    else { eliminatingTeams = null; }

  }

  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      }
      else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }

}
