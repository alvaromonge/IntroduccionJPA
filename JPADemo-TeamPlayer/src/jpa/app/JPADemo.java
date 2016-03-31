/*
 * Created by Dr. Alvaro Monge <alvaro.monge@csulb.edu>
 * This code is most likely for educational purposes only,
 * please check with me otherwise.
 */

package jpa.app;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import jpa.entities.Persona;
import jpa.entities.Equipo;

/**
 *
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 */
public class JPADemo {

    private static final Logger theLogger
            = Logger.getLogger(JPADemo.class.getName());

    // Create the EntityManager
    // sportsPU is a Persistence Unit as defined in persistence.xml that is
    // part of this application (it is the META-INF folder)
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportsPU");
    private static final EntityManager em = emf.createEntityManager();

    private static final Scanner input = new Scanner(System.in);
    
    /**
     * Constructor to setup the initial set of transient objects. 
     */
    JPADemo() {
        for (Persona player : PLAYERS_WARRIORS) {
            EQUIPOS[0].addPersona(player);  // addPersona is responsible for setting a player's team
        }

        for (Persona player : PLAYERS_LAKERS) {
            EQUIPOS[2].addPersona(player);  // addPersona is responsible for setting a player's team
        }

        for (Persona player : PLAYERS_CAVALIERS) {
            EQUIPOS[3].addPersona(player);  // addPersona is responsible for setting a player's team
        }

        for (Persona player : PLAYERS_CLIPPERS) {
            EQUIPOS[4].addPersona(player);
        }
    }

    /**
     * A menu-driven program giving user options of executing sample functions on the persistent data
     * @param args no arguments are used
     */
    public static void main(String[] args) {
        JPADemo demo = new JPADemo();
        demo.loadDatabase();

        String userInput;
        do {
            displayMenu();
            userInput = input.nextLine();
            processInput(demo, userInput);
        } while (! userInput.equalsIgnoreCase("terminar"));
    }

    /**
     * Given some user input of the function to be executed, sets it up and executes it.
     * @param demo The demo object that has the functions of this demo
     * @param userInput The user's choice of which function to execute
     */
    public static void processInput(JPADemo demo, String userInput) {
        String teamName;
        String playerFirstName;
        String playerLastName;
        
        switch (userInput.toLowerCase()) {
            case "reload":
                System.out.println("Reiniciando toda la BD.");
                demo.deleteDB();
                System.out.println("Reencargando la informacion inicial de la BD.");
                demo.loadDatabase();
                break;
            case "roster":
                System.out.print("Nombre del equipo: ");
                teamName = input.nextLine();
                Collection<Persona> roster = demo.getRoster(teamName);
                if (roster != null && !roster.isEmpty()) {
                    System.out.println("La alineación: ");
                    for (Persona player : roster) {
                        System.out.println(player);
                    }
                } else {
                    System.out.println("Ningun jugador!");
                }
                break;
            case "eliminar":
                System.out.println("Demo de eliminar un dato");
                System.out.print("Nombre de la persona: ");
                playerFirstName = input.nextLine();
                System.out.print("Apellido de la persona: ");
                playerLastName = input.nextLine();
                demo.remove(playerFirstName, playerLastName);
                break;
            case "consulta":
                System.out.println("Demo de consulta por medio de find");
                demo.find();
                break;
            case "delete":
                System.out.println("Demo de JPQL delete");
                System.out.print("Team name: ");
                teamName = input.nextLine();
                demo.delete(teamName);
                break;
            default:
                System.out.println("Esa opción no existe. Intente otra vez");
        }

    }

    /**
     * Display the menu of choices for the program
     */
    public static void displayMenu() {
        System.out.println();
        for (int i = 0; i < MENU_CHOICES.length; i++) {
            System.out.println(MENU_CHOICES[i] + ": " + CHOICE_SUMMARIES[i]);
        }
        System.out.print("\tSeleccion> ");
    }

    /**
     * Method to demonstrate deleting all database objects. This will also
     * clean up the EntityManager by detaching all objects known to be in it.
     */
    private void deleteDB() {
        em.getTransaction().begin();
        
        Query deletePersonas = em.createNamedQuery(Persona.DELETE_ALL);
        deletePersonas.executeUpdate();

        Query deleteTeams = em.createNamedQuery(Equipo.DELETE_ALL);
        deleteTeams.executeUpdate();
        
        em.getTransaction().commit();
        em.clear();
        
    }
            
    /**
     * Load the database tables with some initial records provided in the code.
     */
    private void loadDatabase() {
        // An EntityManager object is used to perform persistence tasks such as
        // starting transactions, persisting objects, creating queries, etc.
        em.getTransaction().begin();
        
        // prior to the statement below, each of the Equipo objects in the teams array
        // is a transient entity, i.e. just a regular non-persistent Java object.

        // All instances at this point are transient... they're "objects" not "entities"
        for (Equipo team : EQUIPOS) {
            em.persist(team);
        }

        // Now they are all persisted... even players due to the CascadeType (see relationship defined in Equipo.java)
        em.getTransaction().commit();
    }

    /**
     * Retrieves the players who are members of a team.
     * @param teamName The name of the team whose players is to be retrieved
     * @return a List of Personas who are members of the named team, or an empty list.
     * 
     * TODO: Swiss SU Assignment -- Change this so that it uses a JPQL query to retrieve 
     * players who are on the team. The JPQL query string should be defined in the Persona class.
     * 
     * DONE
     * 
     */
    private Collection<Persona> getRoster(String teamName) {
        // TypedQuery provide strong type checking
//        TypedQuery<Team> retrieveTeamQuery = em.createNamedQuery(Equipo.GET_BY_NAME, Equipo.class);
//        retrieveTeamQuery.setParameter("name", teamName);
//        List<Team> teams = retrieveTeamQuery.getResultList();
        
        TypedQuery<Persona> retrievePersonas = em.createNamedQuery(Persona.RETRIEVE_PLAYERS_TEAM, Persona.class);
        retrievePersonas.setParameter("teamName", teamName);
        
        List<Persona> players = retrievePersonas.getResultList();
        
//        return teams == null || teams.isEmpty() ? null : teams.get(0).getRoster();
        return players == null || players.isEmpty() ? null : players;
    }

    /**
     * Removes a Persona whose id (PK value) is known.
     * TODO: Swiss SU Assignment -- Modify this to take as parameter the name of player to be removed
     * 
     * DONE !
     * 
     */
    private void remove(String firstName, String lastName) { 
        em.getTransaction().begin();
//        Persona player = em.find(Persona.class, 5);
        TypedQuery<Persona> retrievePersonaByName = em.createNamedQuery(Persona.SEARCH_PLAYER_BY_NAME, Persona.class);
        retrievePersonaByName.setParameter("firstName", firstName)
                            .setParameter("lastName", lastName);
        
        List<Persona> players = retrievePersonaByName.getResultList();
        
        Persona playerToDelete = null;

        if (players != null && !players.isEmpty())
        {
            playerToDelete = (Persona) players.get(0);
        }

        if (playerToDelete != null)
        {
            System.out.println("Removing player from database: " + firstName + " " + lastName);
            em.remove(playerToDelete);
            theLogger.fine("CHECK DB... you'll see player w/ID name specified is removed");
        }
        
        em.getTransaction().commit(); // Before a commit, the remove was not guaranteed
    }
    
    /**
     * Demonstrates how to use JPQL to delete from the database. Deletes the Equipo object
 if it exists; otherwise, if it doesn't exist then it does nothing.
     * @param teamName the name of the Equipo to be deleted
     */
    private void delete(String teamName) {
        
        System.out.println("Demo WARNING: This will delete players on the team you're about to delete!");

        TypedQuery<Equipo> deleteStatement = em.createNamedQuery(Equipo.DELETE_BY_NAME, Equipo.class);
        deleteStatement.setParameter("name", teamName);

        em.getTransaction().begin();
        int count = deleteStatement.executeUpdate();
        theLogger.log(Level.FINE, "Number of teams deleted: {0}", count);
        em.getTransaction().commit();

        /**
         * TODO: Swiss SU Assignment -- Try deleting Teams with and without Personas! Check the database before and after you 
         * test this method. Explain what happens in each case.
         * 
         * DONE !
         * 
         * EXPLANATION:
         * 1) Deleting team without any player
         *    result: Team successfully deleted. The team IDs changed but Personas
         *            remained the same.
         * 2) Deleting team with players
         *    result: Team successfully deleted. Personas in the team are also deleted.
         */
    }

    /**
     * Method to attempt to find Personas with id (the PK) values 1 thru 10.
     * The retrieval stops as soon as a Persona with particular id is not found.
     */
    private void find() {
        // Create the EntityManager
        boolean playerFound = true;

        // em.find() requires the PK value by which an entity can be found
        // In the demo below, since we know PK values are auto generated
        // starting at 1, we attempt to find the first 10 players.
        System.out.println("The first players inserted in the database...");
        for (int primaryKey = 1; primaryKey <= 10 && playerFound; primaryKey++) {
            Persona player = em.find(Persona.class, primaryKey);
            if (player != null) {
                System.out.println(player);
            } else {
                playerFound = false;
            }
        }
    }
    
    /**
     * lakersPersonas is an array of Persona objects that will all be assigned to
 the Los Angeles Lakers Equipo object.
     */
    private static final Persona[] PLAYERS_LAKERS = new Persona[]{
        new Persona("Kobe", "Bryant", 24, "The Black Mamba"),
        new Persona("Magic", "Johnson", 32, "The Magician"),
        new Persona("Kareem", "Abdul-Jabbar", 33, "Sky Hook Expert")
    };

    /**
     * clippersPersonas is an array of Persona objects that will all be assigned to
 the Los Angeles Clippers Equipo object.
     */
    private static final Persona[] PLAYERS_CLIPPERS = new Persona[]{
        new Persona("Blake", "Griffin", 32, "The new Highlight Film"),
        new Persona("Jamal", "Crawford", 11, "It's raining three pointers"),
        new Persona("Chris", "Paul", 3, "The nonstop assist generator")

    };


    /**
     * cavaliersPersonas is an array of Persona objects that will all be assigned to
 the Cleveland Cavaliers Equipo object.
     */
    private static final Persona[] PLAYERS_CAVALIERS = new Persona[]{
        new Persona("LeBron", "James", 32, "The new Highlight Film"),
        new Persona("Kevin", "Love", 11, "It's raining three pointers"),
        new Persona("Kyrie", "Irving", 3, "The nonstop assist generator")    
    };



    /**
     * warriorsPersonas is an array of Persona objects that will all be assigned to
 the Golden State Warriors Equipo object.
     */
    private static final Persona[] PLAYERS_WARRIORS = new Persona[]{
        new Persona("Stephen", "Curry", 32, "The new Highlight Film"),
        new Persona("Leandro", "Barbosa", 11, "It's raining three pointers") 
    };
    
    /**
     * teams is an array of Equipo objects, with players to be assigned in a program
     */
    private static final Equipo[] EQUIPOS = new Equipo[]{
        new Equipo("Golden State Warriors", "Oakland", "West"),
        new Equipo("Heat", "Miami", "East"),
        new Equipo("Lakers", "Los Angeles", "West"),
        new Equipo("Cavaliers", "Cleveland", "East"),
        new Equipo("Clippers", "Los Angeles","West")
    };


    /**
     * Menu choices
     */
    private static final String MENU_CHOICES[] = {"reload", "consulta", "roster", "eliminar", "delete", "terminar"};
    
    /**
     * Descriptions of the menu choices
     */
    private static final String CHOICE_SUMMARIES[] = {
        "Reiniciar la BD",
        "Demo de consulta",
        "Consultar la alineación de un equipo",
        "Demo de eliminar un jugador",
        "Demo de ejecutar un JPQL operacion para eliminar un equipo",
        "Terminar este programa"
    };

    
}
