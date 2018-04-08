package com.skilldistillery.filmquery.database;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.filmquery.entities.*;

public interface DatabaseAccessor {
  public Film getFilmById(int filmId) throws SQLException;
  public List<Actor> getActorsByFilmId(int filmId);
  public List<Film> getFilmByKeyword(String search) throws SQLException;
}
