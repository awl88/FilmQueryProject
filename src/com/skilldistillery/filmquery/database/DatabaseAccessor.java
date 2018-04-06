package com.skilldistillery.filmquery.database;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.filmquery.entities.*;

public interface DatabaseAccessor {
  public Film getFilmById(int filmId) throws SQLException;
  public Actor getActorById(int actorId) throws SQLException;
  public List<Actor> getActorsByFilmId(int filmId);
}
