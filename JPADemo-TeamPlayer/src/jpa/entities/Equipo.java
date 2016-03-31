/*
 * Equipo.java
 *
 * Copyright 2007 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of 
 * this software is authorized pursuant to the terms of the license 
 * found at http://developers.sun.com/berkeley_license.html.
 *
 * Original downloaded from: http://java.sun.com/developer/technicalArticles/J2SE/Desktop/persistenceapi/
 * Updated by Alvaro Monge to work with JPA 2.0 EclipseLink
 *
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.eclipse.persistence.annotations.CascadeOnDelete;

/**
 * Class Equipo annotated to be an Entity. In this case we have the default
 * behavior so the Entity name (and thus table name) is the same as the class
 * name.
 *
 * @author John O'Conner
 * @author Alvaro Monge
 */
@NamedQueries({
    @NamedQuery(name = Equipo.GET_BY_NAME, query = "SELECT e FROM Equipo e WHERE e.teamName = :name"),
    @NamedQuery(name = Equipo.DELETE_BY_NAME, query = "DELETE FROM Equipo e WHERE e.teamName = :name"),
    @NamedQuery(name = Equipo.DELETE_ALL, query = "DELETE FROM Equipo e")
// NOTE: While tempting... this does not work b/c JPQL SELECT clause must specify only single-valued expressions:
// @NamedQuery(name = Equipo.GET_BY_NAME, query = "SELECT t.roster FROM Equipo t WHERE t.teamName = :name"),@NamedQueries({
})
@Entity
public class Equipo implements Serializable {

    /**
     * Name of JPQL query string to retrieve the players in a named team.
     */
    public static final String GET_BY_NAME = "Equipo.get_by_name";
    /**
     * Name of JPQL query string to delete a team given its name.
     */
    public static final String DELETE_BY_NAME = "Equipo.delete_by_name";
    /**
     * Name of JPQL query string to delete all teams.
     */
    public static final String DELETE_ALL = "Equipo.delete";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @javax.persistence.Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String teamName;
    private String league;
    private String city;

    /* For a bidirectional relationship, the annotation below defines 
     * the inverse side of a ManyToOne relationship: a
     * team has many players and relates it to the owning side,
     * in this case the team field of a Persona. Thus, given a team T, then
     * the following must be true for every player P in T.players: T == P.getEquipo()
     */
    @OneToMany(mappedBy = "equipo", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @CascadeOnDelete   // NOTE: This is specific to JPA Provider EclipseLink, thus NOT portable.
    private Collection<Persona> roster;

    /* 
     * Alternatively, for a unidirectional relationship, we would remove the reference 
     * to Equipo from Persona and you must explicitly define the FK column, otherwise by default JPA will
     * generate a "join table" that unfortunately acts like a ManyToMany relationship in the database!!
     * 
     */
//    @OneToMany
//    @JoinColumn(name="team_fk")

    // Aqui iria la relacion de entrenador....
    //    @OneToOne
    //    private Persona entrenador;

    /**
     * Creates a new instance of Equipo
     */
    public Equipo() {
        roster = new HashSet<>();
    }

    /**
     * Creates a new instance of Equipo with some specified values
     *
     * @param name the name of the Equipo
     * @param city the name of the city where Equipo plays
     * @param league the name of the sports league in which the team
     * participates
     */
    public Equipo(String name, String city, String league) {
        this.teamName = name;
        this.city = city;
        this.league = league;
        roster = new HashSet<>();
    }

    /**
     * Gets the id of this Equipo
     *
     * @return the id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Sets the id of this Equipo to the specified value.
     *
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of this Equipo
     *
     * @return teamName the name of this Equipo
     */
    public String getEquipoName() {
        return teamName;
    }

    /**
     * Sets the name of the Equipo
     *
     * @param teamName the name of the Equipo
     */
    public void setEquipoName(String teamName) {
        this.teamName = teamName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the name of the sports league in which the Equipo participates.
     *
     * @return league the name of the league
     */
    public String getLeague() {
        return league;
    }

    /**
     * Sets the name of the sports league in which the Equipo participates
     *
     * @param league
     */
    public void setLeague(String league) {
        this.league = league;
    }

    /**
     * Access the collection of Persona objects making up the team
     *
     * @return a collection of Persona objects who make up the team
     */
    public Collection<Persona> getRoster() {
        return roster;
    }

    /**
     * Set the collection of players for this team. Also ensure that each
     * player's team is set to this team.
     * From:http://en.wikibooks.org/wiki/Java_Persistence/OneToMany "As the
     * relationship is bi-directional so as the application updates one side of
     * the relationship, the other side should also get updated, and be in
     * synch"
     *
     * @param players is the collection of players making up the team.
     */
    public void setRoster(Collection<Persona> players) {
        for (Persona p : players) {
            addPersona(p);
        }
    }

    /**
     * Add a persona to this team. Also ensure that the persona's team is set to
 this team.
     *
     * @param persona is the Persona to be added to the team.
     */
    public void addPersona(Persona persona) {
        roster.add(persona);
        if (persona.getEquipo() != this) {
            persona.setEquipo(this);
        }
    }

    /**
     * Remove a player from the team.
     *
     * @param player to be removed from the team.
     * @return whether of not the player's been removed successfully.
     */
    public boolean removePersona(Persona player) {
        boolean success = roster.remove(player);

        if (success && player.getEquipo() == this) {
            player.setEquipo(null);
        }

        return success;
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
     * Determines whether another object is equal to this Equipo. The result is
     * <code>true</code> if and only if the argument is not null and is a Equipo
 object that has the same id field values as this object.
     *
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
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
        return "Equipo[name=" + teamName + ", roster=" + roster + "]";
    }
}
