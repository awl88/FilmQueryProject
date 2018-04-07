package com.skilldistillery.filmquery.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid";
	private static String user = "student";
	private static String pass = "student";

	public List<Film> search(String userInput) throws SQLException {
        List<Film> films = new ArrayList<>();
		try {
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT f.id, f.title, f.description, f.release_year, f.rating, l.name "
					+ "FROM film f JOIN language l ON f.language_id = l.id "
					+ "JOIN film_actor fa ON f.id = fa.film_id "
					+ "JOIN actor a ON fa.actor_id = a.id "
					+ "WHERE f.title LIKE ? OR f.description LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + userInput + "%");
			stmt.setString(2, "%" + userInput + "%");
			ResultSet filmResult = stmt.executeQuery();
			
			int count = 0;
			while (filmResult.next()) {
				Film film = new Film(); // Create the object
				// Here is our mapping of query columns to our object fields:
				int id = filmResult.getInt(1);
				film.setId(id);
				film.setTitle(filmResult.getString(2));
				film.setDescription(filmResult.getString(3));
				film.setReleaseYear(filmResult.getInt(4));
				film.setRating(filmResult.getString(5));
				film.setLanguage(filmResult.getString(6));
				film.setActors(getActorsByFilmId(id));
				films.add(film);
			    count++;
			}
			
			if (count <= 0) {
				System.out.println("No films were found!");
				System.out.println();
			}

			filmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Return the object
		return films;
    }
	
	
	@Override
	public Film getFilmById(int filmId) throws SQLException {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT f.id, f.title, f.description, f.release_year, f.rating, l.name "
					+ "FROM film f JOIN language l ON f.language_id = l.id "
					+ "JOIN film_actor fa ON f.id = fa.film_id "
					+ "JOIN actor a ON fa.actor_id = a.id "
					+ "WHERE f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			
			int count = 0;
			while (filmResult.next()) {
				film = new Film(); // Create the object
				// Here is our mapping of query columns to our object fields:
				film.setId(filmResult.getInt(1));
				film.setTitle(filmResult.getString(2));
				film.setDescription(filmResult.getString(3));
				film.setReleaseYear(filmResult.getInt(4));
				film.setRating(filmResult.getString(5));
				film.setLanguage(filmResult.getString(6));
				film.setActors(getActorsByFilmId(filmId));
			    count++;
			}
			
			if (count <= 0) {
				System.out.println("No films were found!");
				System.out.println();
			}
			
			filmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Return the object
		return film;
	}

	@Override
	public Actor getActorById(int actorId) throws SQLException {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();

			if (actorResult.next()) {
				actor = new Actor(); // Create the object
				// Here is our mapping of query columns to our object fields:
				actor.setId(actorResult.getInt(1));
				actor.setFirstName(actorResult.getString(2));
				actor.setLastName(actorResult.getString(3));
			}
			
			actorResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// Return the object
		}
		return actor;
	}

	@Override
	public List<Actor> getActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name "
					+ "FROM actor JOIN film_actor ON actor.id = film_actor.actor_id "
					+ "WHERE film_actor.film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Actor actor = new Actor();
				actor.setId(1);
				actor.setFirstName(rs.getString(2));
				actor.setLastName(rs.getString(3));
				actors.add(actor);
			}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Unable to load DB driver. Exiting");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
