package org.kwstudios.play.ragemode.statistics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.kwstudios.play.ragemode.database.MySQLConnector;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scores.PlayerPoints;
import org.kwstudios.play.ragemode.scores.RetPlayerPoints;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class MySQLStats {

	/**
	 * Adds the statistics from the given PlayerPoints instance to the database
	 * connection from the given MySQLConnector instance.
	 * 
	 * @param playerPoints
	 *            The PlayerPoints instance from which the statistics should be
	 *            gotten.
	 * @param mySQLConnector
	 *            The MySQLConnector instance which holds the Connection for the
	 *            database.
	 */
	public static void addPlayerStatistics(PlayerPoints playerPoints, MySQLConnector mySQLConnector) {

		testConnection();

		Connection connection = PluginLoader.getMySqlConnector().getConnection();

		Statement statement = null;
		String query = "SELECT * FROM rm_stats_players WHERE uuid LIKE '" + playerPoints.getPlayerUUID() + "';";

		int oldKills = 0;
		int oldAxeKills = 0;
		int oldDirectArrowKills = 0;
		int oldExplosionKills = 0;
		int oldKnifeKills = 0;

		int oldDeaths = 0;
		int oldAxeDeaths = 0;
		int oldDirectArrowDeaths = 0;
		int oldExplosionDeaths = 0;
		int oldKnifeDeaths = 0;

		int oldWins = 0;
		int oldScore = 0;
		int oldGames = 0;

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				oldKills = rs.getInt("kills");
				oldAxeKills = rs.getInt("axe_kills");
				oldDirectArrowKills = rs.getInt("direct_arrow_kills");
				oldExplosionKills = rs.getInt("explosion_kills");
				oldKnifeKills = rs.getInt("knife_kills");

				oldDeaths = rs.getInt("deaths");
				oldAxeDeaths = rs.getInt("axe_deaths");
				oldDirectArrowDeaths = rs.getInt("direct_arrow_deaths");
				oldExplosionDeaths = rs.getInt("explosion_deaths");
				oldKnifeDeaths = rs.getInt("knife_deaths");

				oldWins = rs.getInt("wins");
				oldScore = rs.getInt("score");
				oldGames = rs.getInt("games");
			}
		} catch (SQLException e) {
			System.out.println(
					ConstantHolder.RAGEMODE_PREFIX + Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID()))
							+ " has no statistics yet! Creating one special row for him...");
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		int newKills = oldKills + playerPoints.getKills();
		int newAxeKills = oldAxeKills + playerPoints.getAxeKills();
		int newDirectArrowKills = oldDirectArrowKills + playerPoints.getDirectArrowKills();
		int newExplosionKills = oldExplosionKills + playerPoints.getExplosionKills();
		int newKnifeKills = oldKnifeKills + playerPoints.getKnifeKills();

		int newDeaths = oldDeaths + playerPoints.getDeaths();
		int newAxeDeaths = oldAxeDeaths + playerPoints.getAxeDeaths();
		int newDirectArrowDeaths = oldDirectArrowDeaths + playerPoints.getDirectArrowDeaths();
		int newExplosionDeaths = oldExplosionDeaths + playerPoints.getExplosionDeaths();
		int newKnifeDeaths = oldKnifeDeaths + playerPoints.getKnifeDeaths();

		int newWins = (playerPoints.isWinner()) ? oldWins + 1 : oldWins;
		int newScore = oldScore + playerPoints.getPoints();
		int newGames = oldGames + 1;
		double newKD = (newDeaths != 0) ? (((double) newKills) / ((double) newDeaths)) : 1;

		statement = null;
		query = "REPLACE INTO rm_stats_players (name, uuid, kills, axe_kills, direct_arrow_kills, explosion_kills, knife_kills, deaths, axe_deaths, direct_arrow_deaths, explosion_deaths, knife_deaths, wins, score, games, kd) VALUES ("
				+ "'" + Bukkit.getPlayer(UUID.fromString(playerPoints.getPlayerUUID())).getName() + "', " + "'"
				+ playerPoints.getPlayerUUID() + "', " + Integer.toString(newKills) + ", "
				+ Integer.toString(newAxeKills) + ", " + Integer.toString(newDirectArrowKills) + ", "
				+ Integer.toString(newExplosionKills) + ", " + Integer.toString(newKnifeKills) + ", "
				+ Integer.toString(newDeaths) + ", " + Integer.toString(newAxeDeaths) + ", "
				+ Integer.toString(newDirectArrowDeaths) + ", " + Integer.toString(newExplosionDeaths) + ", "
				+ Integer.toString(newKnifeDeaths) + ", " + Integer.toString(newWins) + ", "
				+ Integer.toString(newScore) + ", " + Integer.toString(newGames) + ", " + Double.toString(newKD) + ");";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns an RetPlayerPoints instance with the statistics from the database
	 * connection for the given Player.
	 * 
	 * @param player
	 *            The Player instance for which the statistic should be gotten.
	 * @param mySQLConnector
	 *            The MySQLConnector which holds the Connection instance for the
	 *            database.
	 * @return
	 */
	public static RetPlayerPoints getPlayerStatistics(String player, MySQLConnector mySQLConnector) {

		testConnection();

		Connection connection = PluginLoader.getMySqlConnector().getConnection();

		Statement statement = null;
		String query = "SELECT * FROM rm_stats_players WHERE uuid LIKE '" + player + "';";

		int currentKills = 0;
		int currentAxeKills = 0;
		int currentDirectArrowKills = 0;
		int currentExplosionKills = 0;
		int currentKnifeKills = 0;

		int currentDeaths = 0;
		int currentAxeDeaths = 0;
		int currentDirectArrowDeaths = 0;
		int currentExplosionDeaths = 0;
		int currentKnifeDeaths = 0;

		int currentWins = 0;
		int currentScore = 0;
		int currentGames = 0;
		double currentKD = 0;

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				currentKills = rs.getInt("kills");
				currentAxeKills = rs.getInt("axe_kills");
				currentDirectArrowKills = rs.getInt("direct_arrow_kills");
				currentExplosionKills = rs.getInt("explosion_kills");
				currentKnifeKills = rs.getInt("knife_kills");

				currentDeaths = rs.getInt("deaths");
				currentAxeDeaths = rs.getInt("axe_deaths");
				currentDirectArrowDeaths = rs.getInt("direct_arrow_deaths");
				currentExplosionDeaths = rs.getInt("explosion_deaths");
				currentKnifeDeaths = rs.getInt("knife_deaths");

				currentWins = rs.getInt("wins");
				currentScore = rs.getInt("score");
				currentGames = rs.getInt("games");
				currentKD = rs.getDouble("kd");
			}
		} catch (SQLException e) {
			// System.out.println("Something went wrong!");
			return null;
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (currentGames == 0) {
			return null;
		}

		RetPlayerPoints retPlayerPoints = new RetPlayerPoints(player);
		retPlayerPoints.setKills(currentKills);
		retPlayerPoints.setAxeKills(currentAxeKills);
		retPlayerPoints.setDirectArrowKills(currentDirectArrowKills);
		retPlayerPoints.setExplosionKills(currentExplosionKills);
		retPlayerPoints.setKnifeKills(currentKnifeKills);

		retPlayerPoints.setDeaths(currentDeaths);
		retPlayerPoints.setAxeDeaths(currentAxeDeaths);
		retPlayerPoints.setDirectArrowDeaths(currentDirectArrowDeaths);
		retPlayerPoints.setExplosionDeaths(currentExplosionDeaths);
		retPlayerPoints.setKnifeDeaths(currentKnifeDeaths);

		retPlayerPoints.setWins(currentWins);
		retPlayerPoints.setPoints(currentScore);
		retPlayerPoints.setGames(currentGames);
		retPlayerPoints.setKD(currentKD);

		return retPlayerPoints;
	}
	
	/**
	 * Retrieves all rows from the mySQL database and returns them as a List of RetPlayerPoints.
	 * 
	 * @return A List of all RetPlayerPoints objects which are stored in the mySQL database.
	 */
	public static List<RetPlayerPoints> getAllPlayerStatistics() {
		List<RetPlayerPoints> rppList = new ArrayList<RetPlayerPoints>();
		
		testConnection();

		Connection connection = PluginLoader.getMySqlConnector().getConnection();

		Statement statement = null;
		String query = "SELECT * FROM rm_stats_players;";

		int currentKills = 0;
		int currentAxeKills = 0;
		int currentDirectArrowKills = 0;
		int currentExplosionKills = 0;
		int currentKnifeKills = 0;

		int currentDeaths = 0;
		int currentAxeDeaths = 0;
		int currentDirectArrowDeaths = 0;
		int currentExplosionDeaths = 0;
		int currentKnifeDeaths = 0;

		int currentWins = 0;
		int currentScore = 0;
		int currentGames = 0;
		double currentKD = 0;

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				currentKills = rs.getInt("kills");
				currentAxeKills = rs.getInt("axe_kills");
				currentDirectArrowKills = rs.getInt("direct_arrow_kills");
				currentExplosionKills = rs.getInt("explosion_kills");
				currentKnifeKills = rs.getInt("knife_kills");

				currentDeaths = rs.getInt("deaths");
				currentAxeDeaths = rs.getInt("axe_deaths");
				currentDirectArrowDeaths = rs.getInt("direct_arrow_deaths");
				currentExplosionDeaths = rs.getInt("explosion_deaths");
				currentKnifeDeaths = rs.getInt("knife_deaths");

				currentWins = rs.getInt("wins");
				currentScore = rs.getInt("score");
				currentGames = rs.getInt("games");
				currentKD = rs.getDouble("kd");
				
				String playerUUID = rs.getString("uuid");
				RetPlayerPoints retPlayerPoints = new RetPlayerPoints(playerUUID);
				retPlayerPoints.setKills(currentKills);
				retPlayerPoints.setAxeKills(currentAxeKills);
				retPlayerPoints.setDirectArrowKills(currentDirectArrowKills);
				retPlayerPoints.setExplosionKills(currentExplosionKills);
				retPlayerPoints.setKnifeKills(currentKnifeKills);

				retPlayerPoints.setDeaths(currentDeaths);
				retPlayerPoints.setAxeDeaths(currentAxeDeaths);
				retPlayerPoints.setDirectArrowDeaths(currentDirectArrowDeaths);
				retPlayerPoints.setExplosionDeaths(currentExplosionDeaths);
				retPlayerPoints.setKnifeDeaths(currentKnifeDeaths);

				retPlayerPoints.setWins(currentWins);
				retPlayerPoints.setPoints(currentScore);
				retPlayerPoints.setGames(currentGames);
				retPlayerPoints.setKD(currentKD);
				
				rppList.add(retPlayerPoints);
			}
		} catch (SQLException e) {
			// System.out.println("Something went wrong!");
			return null;
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rppList;
	}

	private synchronized static void testConnection() {
		Connection connection = PluginLoader.getMySqlConnector().getConnection();
		try {
			if (!connection.isValid(2)) {
				PluginLoader.getInstance().initStatistics();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
