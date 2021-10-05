package project2GIVE_TO_STUDENTS;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/************************************************************************
 * 
 * The Rental class is a parent class that holds information of a rental. The
 * class is abstract. There is an abstract method here.
 * 
 * @author Original authors + Romandy Vu, Aidan Takace, and Scott Richards.
 * @version 2/10/2021
 ***********************************************************************/
public abstract class Rental implements Serializable {

    /* What is the purpose of this variable (search google). */
    /*
     * SerialVersionUID is used to ensure that during deserialization the same class
     * (that was used during serialize process) is loaded.
     */

    // This method is used to make sure the data of this object is the same when
    // "seralized" and the "deseralized".
    // Since this is a static FINAL ID, if the deseralization did not match with the
    // object before the seralization.
    // The data of the object is wrong. (Makes sure the data of the object is valid
    // before and after seralization/deseralization).
    private static final long serialVersionUID = 1L;

    /** The Name of person that is reserving the Rental. */
    protected String nameOfRenter;

    /** The date the Rental was rented on (the date the rental was placed). */
    protected GregorianCalendar rentedOn;

    /** The date the Rental was dueBack on (due date of the rental). */
    protected GregorianCalendar dueBack;

    /** The actual date the Rental was returned on. */
    protected GregorianCalendar actualDateReturned;

    /******************************************************************
     * 
     * Default Constructor of Rental class that creates an object where the instance
     * variables are empty.
     * 
     * @param Parameters are not needed (Default Constructor).
     * @return Doesn't return anything (Constructor).
     ****************************************************************/
    public Rental() {
    }

    /*****************************************************************
     * 
     * (From the rental) This abstract method uses the getCost() method of the child
     * classes depending on what child class it is.
     * 
     * @param checkOut a checkout date.
     * @return the cost of the rental in type double.
     *****************************************************************/
    public abstract double getCost(GregorianCalendar checkOut);

    /******************************************************************
     * 
     * (From the Rental class) A constructor that takes in input to set the name of
     * the renter, date that is rented, the due date of the rental, the date the
     * rental, and the date that the rental was return of object type "Rental".
     * 
     * 
     * @param nameOfRenter       the name of the renter.
     * @param rentedOn           the date when the rental was placed.
     * @param dueBack            the due date of the rental.
     * @param actualDateReturned the date when the rental was returned.
     * @returns Nothing due to this being a constructor.
     ******************************************************************/
    public Rental(String nameOfRenter, GregorianCalendar rentedOn, GregorianCalendar dueBack,
            GregorianCalendar actualDateReturned) {
        this.nameOfRenter = nameOfRenter;
        this.rentedOn = rentedOn;
        this.dueBack = dueBack;
        this.actualDateReturned = actualDateReturned;
    }

    /*******************************************************************
     * 
     * (From the Rental class) A method that returns the name of the renter.
     * 
     * @param None (getter method).
     * @return the name of the renter.
     *******************************************************************/
    public String getNameOfRenter() {
        return nameOfRenter;
    }

    /*******************************************************************
     * 
     * (From the Rental class) A method that sets the name of the renter.
     * 
     * @param nameOfRenter a String input of the name of the renter.
     * @return Nothing (setter method).
     *******************************************************************/
    public void setNameOfRenter(String nameOfRenter) {
        this.nameOfRenter = nameOfRenter;
    }

    /*******************************************************************
     * 
     * (From the Rental class) A method that returns the date that it rental is on.
     * 
     * @param None (getter method).
     * @return the date that it rental is on.
     ******************************************************************/
    public GregorianCalendar getRentedOn() {
        return rentedOn;
    }

    /*******************************************************************
     * 
     * (From the Rental class) A method that sets the date when the rental was
     * placed.
     * 
     * @param rentedOn the date when the rental was placed.
     * @return Nothing (a setter method).
     ******************************************************************/
    public void setRentedOn(GregorianCalendar rentedOn) {
        this.rentedOn = rentedOn;
    }

    /******************************************************************
     * 
     * (From the Rental class) A method that returns the date the rental was
     * returned.
     * 
     * @param None (getter method).
     * @return the date that the rental was returned.
     ******************************************************************/
    public GregorianCalendar getActualDateReturned() {
        return actualDateReturned;
    }

    /*********************************************************************
     * 
     * (From the Rental class) A method that sets the date that the rental was
     * returned.
     * 
     * @param the date that the rental was returned.
     * @return Nothing (setter method).
     *********************************************************************/
    public void setActualDateReturned(GregorianCalendar actualDateReturned) {
        this.actualDateReturned = actualDateReturned;
    }

    /***********************************************************************
     *
     * (From the Rental class) A getter method that returns the due date of the
     * rental.
     * 
     * @param None (getter method).
     * @return the due date of the rental.
     **********************************************************************/
    public GregorianCalendar getDueBack() {
        return dueBack;
    }

    /***********************************************************************
     *
     * (From the Rental class) A setter method that sets the due date of the rental.
     * 
     * @param dueBack the due date of the rental.
     * @return Nothing (setter method)
     ***********************************************************************/
    public void setDueBack(GregorianCalendar dueBack) {
        this.dueBack = dueBack;
    }

    // The following code is used for debugging only.
    // IntelliJ using the toString for displaying in debugger.
    /***********************************************************************
     * 
     * (From the Rental class) A method that returns a string of the renter's name,
     * the date that rental was placed, the due date of the rental, and the date the
     * rental was returned. (Used for debugging) (There is an override.)
     * 
     * @param None
     * @return a string that contains the renter's name, the date that the rental was
     *         placed, the due date of the rental, and the date the rental was
     *         returned in the format of RentUnit.
     ***********************************************************************/
    @Override
    public String toString() {
        /* Creates a format object class that holds a specific format of the date. */
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        /*
         * Checks the value of the date the rental was placed to determine if the date
         * should be empty from having no date or to create the string of that date in
         * the specified format.
         */
        String rentedOnStr;
        if (getRentedOn() == null)
            rentedOnStr = "";
        else
            rentedOnStr = formatter.format(getRentedOn().getTime());

        /*
         * Checks the value of the due date to determine if the date should be empty
         * from having no date or to create the string of that date in the specified
         * format.
         */
        String estdueBackStr;
        if (getDueBack() == null)
            estdueBackStr = "";
        else
            estdueBackStr = formatter.format(getDueBack().getTime());

        /*
         * Checks the value of the date the rental was returned to determine if the date
         * should be empty from having no date or to create the string of that date in
         * the specified format.
         */
        String actualDateReturnedStr;
        if (getActualDateReturned() == null)
            actualDateReturnedStr = "";
        else
            actualDateReturnedStr = formatter.format(getActualDateReturned().getTime());

        /*
         * Returns the name of the renter, date of the rental placed, due date of
         * rental, and acutal date where the format is in "MM/dd/yyyy".
         */
        return "RentUnit{" + "guestName='" + nameOfRenter + ' ' + ", rentedOn =" + rentedOnStr + ", dueBack ="
                + estdueBackStr + ", actualDateReturned =" + actualDateReturnedStr + '}';
    }
}
