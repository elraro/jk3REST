package eu.elraro.jk3rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Player {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private String coloredName;
	private int score;
	private int deaths;
	private int ping;
	private int time;
	
	public Player() {}

	public Player(String name, int score, int ping) {
		this.name = name;
		this.score = score;
		this.ping = ping;
	}

	public Player(String name, String coloredName, int score, int ping) {
		this.name = name;
		this.coloredName = coloredName;
		this.score = score;
		this.ping = ping;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColoredName() {
		return coloredName;
	}

	public void setColoredName(String coloredName) {
		this.coloredName = coloredName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}