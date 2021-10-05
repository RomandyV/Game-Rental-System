package project2GIVE_TO_STUDENTS;

import java.util.Calendar;
import java.util.GregorianCalendar;

/*******************************************************************************************
 * A class that inherits the from the Rental Class ("The Console class is a
 * Rental class").
 * 
 * @author Original authors + Romandy Vu, Aidan Takace, and Scott Richards.
 * @version 2/10/2021
 * 
 ******************************************************************************************/
public class Console extends Rental {

    /**
     * Represents the type of Console, where the consoles have to be in ConsoleType
     * enum file.
     */
    private ConsoleTypes consoleType;

    /************************************************************************************
     *
     * (From the Console class) The default constructor that creates a console
     * object where the instance variables are empty.
     * 
     * @param None (default constructor)
     * @return None. (default constructor)
     *************************************************************************************/
    public Console() {
    }

    /************************************************************************************
     * 
     * (From the Console class) A constructor that takes in input to set the name of
     * the renter, date that is rented, the due date of the rental, the date the
     * rental, the date that the rental was return, and the console type to an
     * object type "Console".
     * 
     * @param nameOfRenter       The name of the renter.
     * @param rentedOn           the date the rental was placed.
     * @param dueBack            the due date of the rental.
     * @param actualDateReturned the date the rental was returned.
     * @param consoleType        the console type (look at ConsoleType.java for
     *                           possible inputs).
     * @return None (constructor)
     ************************************************************************************/
    public Console(String nameOfRenter, GregorianCalendar rentedOn, GregorianCalendar dueBack,
            GregorianCalendar actualDateReturned, ConsoleTypes consoleType) {

        /* Uses the constructor class of Rental. */
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);

        /*
         * Sets the Console type input to the instance variable as the parent "Rental"
         * class does not contain instance variable "consoleType"
         */
        this.consoleType = consoleType;
    }

    /**********************************************************************************
     * 
     * (From the Console class) A method that returns the console type.
     * 
     * @param None (getter method)
     * @return the console type.
     ********************************************************************************/
    public ConsoleTypes getConsoleType() {
        return consoleType;
    }

    /********************************************************************************
     * 
     * (From the Console class) A method that sets the console type.
     * 
     * @param consoleType the console type to be set to should be a variable name
     *                    from ConsoleTypes.java.
     * @return None (setter method).
     *******************************************************************************/
    public void setConsoleType(ConsoleTypes consoleType) {
        this.consoleType = consoleType;
    }

    /*********************************************************************************
     * 
     * (From the Console class) A method that determines the cost of the rental of a
     * console.MIGHT BE A LOGICAL ERROR HERE (There is an override)
     * 
     * @param dueBack the due date of the rental that should be a date after the
     *                rented on date.
     * @return the cost of the rental.
     ********************************************************************************/
    @Override
    public double getCost(GregorianCalendar dueBack) {
        /* Creates a temporary variable of type GregorianCalendar. */
        GregorianCalendar gTemp = new GregorianCalendar();
        // The base cost of the rental.
        double cost = 5;

        if (dueBack != null) {
            /* Sets the temporary variable to the due date of the rental. */
            gTemp = (GregorianCalendar) dueBack.clone(); 

            /*
             * REMOVES 7 days to the temporary variable that holds the due date of the
             * rental.
             */
            for (int days = 0; days < 7; days++) // or gTemp.add(Calendar.DATE, -7);
                gTemp.add(Calendar.DATE, -1);

            /* Determines the additional cost of renting a specific console per day. */
            while (gTemp.after(rentedOn)) {
                while (gTemp.after(rentedOn)) {
                    if ((this.consoleType == ConsoleTypes.NintendoSwitch)
                            || (this.consoleType == ConsoleTypes.PlayStation4Pro)
                            || (this.consoleType == ConsoleTypes.SegaGenesisMini))
                        cost += 1.5;

                    if ((this.consoleType == ConsoleTypes.PlayStation4) || (this.consoleType == ConsoleTypes.XBoxOneS))
                        cost += 1;
                    gTemp.add(Calendar.DATE, -1);

                }
            }
        }
        return cost;
    }

    /********************************************************************************
     * 
     * A method that returns a string that hold console type, the renter's name, the
     * date that rental was placed, the due date of the rental, and the date the
     * rental was returned. (Overrides the Rental class toString method)
     *
     * @param None
     * @return a string that returns the console type and the return value of the
     *         parent toString() method where Rental class is the parent.
     ********************************************************************************/
    @Override
    public String toString() {

        /* Uses super to refer to the Rental Class (parent) method. */
        return "Console{" + " consoleType=" + consoleType + " " + super.toString() + '}';
    }
}
