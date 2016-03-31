/*
 * Persona.java
 *
 *
 * Copyright 2007 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of 
 * this software is authorized pursuant to the terms of the license 
 * found at http://developers.sun.com/berkeley_license.html.
 *
 *
 * Original downloaded from: http://java.sun.com/developer/technicalArticles/J2SE/Desktop/persistenceapi/
 * Updated by Alvaro Monge to work with JPA 2.0 EclipseLink
 *
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 * Entity class Persona
 *
 * @author John O'Conner
 * @author Alvaro Monge
 */
@Entity
@NamedQueries({
    @NamedQuery(name=Persona.DELETE_ALL, query="DELETE FROM Persona p"),
    @NamedQuery(name=Persona.RETRIEVE_PLAYERS_TEAM, query="SELECT p FROM Persona p WHERE p.equipo.teamName = :teamName"),
    @NamedQuery(name=Persona.SEARCH_PLAYER_BY_NAME, query="SELECT p FROM Persona p WHERE p.firstName = :firstName AND p.lastName = :lastName")
})
public class Persona implements Serializable {
    
    /**
     * JPQL query string to delete all players.
     */
    public static final String DELETE_ALL = "Persona.delete_all";
    
    /**
     * JPQL query string to retrieve all players from a equipo.
     */    
    public static final String RETRIEVE_PLAYERS_TEAM = "Persona.retrieve_players";
    
    /**
     * JPQL query string to retrieve a player with specified name.
     */    
    public static final String SEARCH_PLAYER_BY_NAME = "Persona.get_player_by_name";
    

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "nombre")
  private String firstName;
  @Column(name = "apellido", length = 50, nullable = false)
  private String lastName;
  private int jerseyNumber;
  @Transient
  private String quote;

  @ManyToOne
  private Equipo equipo;

  /**
   * Creates a new instance of Persona
   */
  public Persona() {
  }

  /**
   * Creates a new instance of Persona with some specified values
   *
   * @param firstName the Persona's first name
   * @param lastName the Persona's last name
   * @param jerseyNumber the Persona's jersey number
   * @param quote a quote about or by the Persona
   */
  public Persona(String firstName, String lastName, int jerseyNumber, String quote) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.jerseyNumber = jerseyNumber;
    this.quote = quote;
  }

  /**
   * Gets the id of this Persona
   *
   * @return the id
   */
  public Integer getId() {
    return this.id;
  }

  /**
   * Sets the id of this Persona to the specified value.
   *
   * @param id the new id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Sets the Persona's full name
   *
   * @param firstName the Persona's first name
   * @param lastName the Persona's last name
   */
  public void setName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   *
   * @return the player's last name.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Set the last name of the player
   *
   * @param name the last name of the player
   */
  public void setLastName(String name) {
    lastName = name;
  }

  /**
   * Gets the Persona's first name
   *
   * @return the first name of the player
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the Persona's first name
   *
   * @param name the value for the player's first name
   */
  public void setFirstName(String name) {
    firstName = name;
  }

  /**
   * Gets the jersey number of the player
   *
   * @return the jersey number
   */
  public int getJerseyNumber() {
    return jerseyNumber;
  }

  /**
   * Sets the jersey number of a player
   *
   * @param jerseyNumber the value to be used for the jersey number
   */
  public void setJerseyNumber(int jerseyNumber) {
    this.jerseyNumber = jerseyNumber;
  }

  /**
   * Access the last spoken words of this Persona; this is a transient property,
   * that is, it's a property that does not persist so it is annotated as
   * Transient.
   *
   * @return the words spoken last by this Persona
   */
  public String getLastSpokenWords() {
    return quote;
  }

  /**
   * Set the last spoken words of this player
   *
   * @param lastWords the value to be used for the words of a player
   */
  public void setLastSpokenWords(String lastWords) {
    quote = lastWords;
  }

  /**
   * Access the equipo that this Persona is a member of
   *
   * @return the equipo that this Persona is a member of.
   */
  public Equipo getEquipo() {
    return equipo;
  }

  /**
   * Sets the equipo that a player is a member of
   *
   * @param team the Equipo on which the player plays
   */
  public void setEquipo(Equipo team) {
    this.equipo = team;
  }

  /**
   * Returns a hash code value for the object. This implementation computes a
   * hash code value based on the id fields in this object.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  /**
   * Determines whether another object is equal to this Persona. The result is
   * <code>true</code> if and only if the argument is not null and is a Persona
   * object that has the same id field values as this object.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the argument;
   * <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Persona)) {
      return false;
    }
    Persona other = (Persona) object;
    return this.id != null && this.id.equals(other.id);
  }

  /**
   * Returns a string representation of the object. This implementation
   * constructs that representation based on the id fields.
   *
   * @return a string representation of the object.
   */
  @Override
  public String toString() {
    String player = String.format("[Name: %s %s, Jersey Number: %d, Equipo: %s]",
            firstName, lastName, jerseyNumber, equipo.getEquipoName());
    return player;
  }
}