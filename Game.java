package project2GIVE_TO_STUDENTS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/********************************************************************************
 * The Game class inherits from the Rental class ("Game class is a Rental
 * class") It also holds a Console from ConsoleType.java.
 * 
 * @author Original authors + Romandy Vu, Aidan Takace, and Scott Richards.
 * @version 2/10/2021
 *
 ******************************************************************************/
public class Game extends Rental {

    /** represents the name of the game. */
    private String nameGame;

    /**
     * Represents the console the person rented to play the game, null if no console
     * was rented.
     */
    private ConsoleTypes console;

    /******************************************************************************
     * 
     * (From Game class) The default constructor.
     * 
     * @param None (default constructor)
     * @return None. (default constructor)
     *****************************************************************************/
    public Game() {
    }

    /*******************************************************************************
     * 
     * (From Game class) A constructor that takes in input to set the name of the
     * renter, date that is rented, the due date of the rental, the date the rental,
     * and the date that the rental was return to an object type "Game".
     * 
     * @param nameOfRenter       The name of the renter.
     * @param rentedOn           the date that the rental was placed.
     * @param dueBack            the due date of the rental.
     * @param actualDateReturned the date when the rental was returned.
     * @param nameGame           the name of the game.
     * @param console            the type of console that was rented, if null, then
     *                           no console was rented.
     *********************************************************************************/
    public Game(String nameOfRenter, GregorianCalendar rentedOn, GregorianCalendar dueBack,
            GregorianCalendar actualDateReturned, String nameGame, ConsoleTypes console) {

        /* Uses the constructor of Rental Class to set the instance variables. */
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);

        /*
         * Sets input of the name of the game to the instance variable as the Rental
         * class (parent) doesn't contain the instance variable.
         */
        this.nameGame = nameGame;

        /*
         * Sets input of the name of the game to the instance variable as the Rental
         * class (parent) doesn't contain the instance variable.
         */
        this.console = console;

    }

    /**********************************************************************************
     * 
     * (From Game class) A method that returns the name of the game.
     * 
     * @param None (getter method)
     * @return the name of the game.
     **********************************************************************************/
    public String getNameGame() {
        return nameGame;
    }

    /*************************************************************************************
     * 
     * (From Game class) A method that sets the the input to the name of the game
     * instance variable.
     * 
     * @param nameGame the name of the game that is to be replaced with.
     * @return None. (setter method).
     **************************************************************************************/
    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    /****************************************************************************************
     * 
     * (From Game class) A method that returns the type of console that is being
     * rented to play the game.
     * 
     * @param None (getter method)
     * @return the type of console that is being rented to play tha game. If return
     *         is null, the renter did not rent a console.
     **************************************************************************************/
    public ConsoleTypes getConsole() {
        return console;
    }

    /**************************************************************************************
     * 
     * (From the Game class) A method that sets the console that is being rented to
     * play the game of this rental.
     * 
     * @param console the type of console that is being rented to the game. If input
     *                is null, the renter did not rent a console the input should be
     *                a variable from COnsoleTypes.java.
     * @return None. (setter method)
     ***************************************************************************************/
    public void setConsole(ConsoleTypes console) {
        this.console = console;
    }

    /*****************************************************************************************
     * 
     * (From the Game class) A method that returns the cost of renting a game and
     * the console if applicable. (There is an override)
     * 
     * @param dueBack the due date of the rental that should be after the date
     *                rented on.
     * @return the overall cost of the game and a console. (if applicable)
     * 
     ******************************************************************************************/
    @Override
    public double getCost(GregorianCalendar dueBack) {
        /* Creates the cost variable. */
        double cost = 0;
        /*
         * Checks to see if there was a console rented to add the additional cost to
         * overall bill.
         */
        if (console != null) {
            // Creates a temporary variable of type console to get the cost.
            Console temp = new Console(this.nameOfRenter, rentedOn, this.dueBack, actualDateReturned, console);
            cost += temp.getCost(dueBack);
        }

        /* Creates a temporary variable of type Gregorian calendar. */
        GregorianCalendar gTemp = new GregorianCalendar();

        // The base cost of renting a game.
        cost += 3;

        // Sets the temporary variable to the due date of the rental.
        gTemp = (GregorianCalendar) dueBack.clone(); 

        // Code below was here, but not needed probably used for testing.
        // DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        // System.out.println(formatter.format(gTemp.getTime()));

        /* REMOVES? 7 days to the temporary variable. */
        for (int days = 0; days < 7; days++)
            gTemp.add(Calendar.DATE, -1);

        // System.out.println(formatter.format(gTemp.getTime()));
        // System.out.println(formatter.format(rentedOn.getTime()));

        /* The additional cost of renting. */
        while (gTemp.after(rentedOn)) {
            cost += .5;
            gTemp.add(Calendar.DATE, -1);
        }

        return cost;
    }

    /*********************************************************************************************
     * 
     * (From the Game class) A method that returns a string that contains the name
     * of the game, console type, the renter's name, the date that rental was
     * placed, the due date of the rental, and the date the rental was returned.
     * (There is an override)
     * 
     * @param None
     * @return a string of the name of the game, the console, and the result of the
     *         parent class (Rental Class) toString method.
     *********************************************************************************************/
    @Override
    public String toString() {
        return "Game{" + "name='" + nameGame + '\'' + ", player=" + console + super.toString() + '}';
    }
}
