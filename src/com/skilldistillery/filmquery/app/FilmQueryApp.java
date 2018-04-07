package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

	private void test() throws SQLException {
		Film film = db.getFilmById(1);
		System.out.println(film);
	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		System.out.println("~~Welcome~~");
		
		while (true) {
			System.out.println("Please select one of the follow:");
			System.out.println("1. Look up film by ID");
			System.out.println("2. Look up a film by a search keyword");
			System.out.println("0. Exit");
			int choice = input.nextInt();
			if (choice == 1) {
				System.out.print("Please enter a film ID: ");
				int id = input.nextInt();
				Film film = db.getFilmById(id);
				film.viewString();
				System.out.println();
			} else if (choice == 2) {
				System.out.print("Please enter a search keyword: ");
				String keyword = input.next();
				List<Film> films = db.search(keyword);
				for (int i = 0; i < films.size(); i++) {
					films.get(i).viewString();
				}
				System.out.println();
				db.search(keyword);
			} else if (choice == 0) {
				System.out.println("Goodbye!");
				break;
			} else {
				System.out.println("Not a valid option, try again.");
				continue;
			} 
		}
	}

}
