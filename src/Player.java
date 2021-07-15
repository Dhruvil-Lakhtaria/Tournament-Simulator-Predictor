
public class Player{
	private String name,position;
	private double playerRating,shootingAbility;
	private int goals,jerseyNumber;
	private Team team;
	public Player(String n,String pos,int jerseyNo,double rating,double shooting)
	{
		this.name = n;
		this.position = pos;
		this.jerseyNumber = jerseyNo;
		this.playerRating = rating;
		this.shootingAbility = shooting;
		this.goals = 0;
	}
	public void setTeam(Team t)
	{
		this.team = t;
	}	
	public Team getTeam()
	{
		return this.team;
	}
	public void addGoal()
	{
		this.goals++;
	}
	public int getGoals()
	{
		return this.goals;
	}
	public double getPlayerRating()
	{
		return this.playerRating;
	}
	public double getShootingAbility()
	{
		return this.shootingAbility;
	}
	public String getName()
	{
		return this.name;
	}
	public void setPlayerRating(double r)
	{
		this.playerRating = r;
	}
	public void setShootingAbility(double s)
	{
		this.shootingAbility = s;
	}
	public void setName(String n)
	{
		this.name = n;
	}
	//player position and jerseyno were not even in the api so their getter and setter not reqiured.....delete at end.... 
	public String toString()
	{
		if(this.team.getCaptain().getName().equals(this.name))//getter for captain required in team class
		return this.jerseyNumber + " " + this.position + " " + this.name + "(c)";
		else
			return this.jerseyNumber + " " + this.position+ " " + this.name;
	}
}
