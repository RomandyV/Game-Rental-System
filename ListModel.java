package project2GIVE_TO_STUDENTS;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**************************************************************************************
 * This is probably a class that helps the GUI function telling what to
 * change/update/show/do for the GUI.
 * 
 * @author Original authors + Romandy Vu, Aidan Takace, and Scott Richards.
 * @version 2/11/2021
 * 
 *************************************************************************************/
public class ListModel extends AbstractTableModel {

    /** holds all the rentals of type "Rental". */
    private ArrayList<Rental> listOfRentals;

    /** holds only the rentals that are to be displayed. */
    private ArrayList<Rental> filteredListRentals;

    /** current screen being displayed. */ 
    private ScreenDisplay display = ScreenDisplay.CurrentRentalStatus;

    /** Displays the title of the columns of current rentals. */
    private String[] columnNamesCurrentRentals = { "Renter\'s Name", "Est. Cost", "Rented On", "Due Date ", "Console",
            "Name of the Game" };

    /** Displays the title of the columns of rental returns. */
    private String[] columnNamesReturned = { "Renter\'s Name", "Rented On Date", "Due Date", "Actual date returned ",
            "Est. Cost", " Real Cost" };

    // ADDED
    /** Displays the title of the columns of everyThing Item. */
    private String[] columnNameEverything = { "Renter\'s Name", "rented on Date", "Due Date", "Acutal date returned",
            "Est Cost", "Real Cost", "Console", "Name of the Game" };
    // ADDED
    /** Displays the title of the columns of lateRentals Item info. */
    private String[] columnNameLateRentals = { "Renter\'s Name", "Est. Cost", "rented on Date", "Due Date",
            "Numbers of Days Late", "Console", "Name of the Game" };

    /** A formatter to convert the date in "MM/dd/yyyy" format. */
    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    /*********************************************************************************
     * 
     * (From ListModel class) The default constructor for ListModel Class.
     * 
     * @param None (default constructor).
     * @return None (default constructor).
     ********************************************************************************/
    public ListModel() {
        display = ScreenDisplay.CurrentRentalStatus;
        listOfRentals = new ArrayList<>();
        filteredListRentals = new ArrayList<>();
        updateScreen();
        createList();
    }

    /**********************************************************************************
     * 
     * (From ListModel class) A method that sets the display to the selected menu.
     * 
     * @param selected the menu that was selected.
     * @return None (setter method).
     *********************************************************************************/
    public void setDisplay(ScreenDisplay selected) {
        /*
         * If the display was recently on "Cap14DaysOverdue" it titles the renters name
         * as it puts the renters names in caps if days rented is greater than or equal
         * to 14.
         */
        if (display == ScreenDisplay.Cap14DaysOverdue) {
            titleRentersName();
        }
        display = selected;
        updateScreen();
    }

    /*********************************************************************************
     * 
     * (From ListModel class) A private method that changes the display to display
     * the data of what menu item was selcted.
     * 
     * @param None
     * @return None
     * @throws RunTimeException when the a certain display name type is invalid.
     *******************************************************************************/
    private void updateScreen() {
        switch (display) {
        // Displays data for the "Current Rental Screen".
        case CurrentRentalStatus:
            filteredListRentals = (ArrayList<Rental>) listOfRentals.stream().filter(n -> n.actualDateReturned == null)
                    .collect(Collectors.toList());

            Collections.sort(filteredListRentals, (n1, n2) -> n1.nameOfRenter.compareTo(n2.nameOfRenter));
            break;

        // Displays data for the "Returned Screen".
        case ReturnedItems:
            filteredListRentals = (ArrayList<Rental>) listOfRentals.stream().filter(n -> n.actualDateReturned != null)
                    .collect(Collectors.toList());

            Collections.sort(filteredListRentals, new Comparator<Rental>() {
                @Override
                public int compare(Rental n1, Rental n2) {
                    return n1.nameOfRenter.compareTo(n2.nameOfRenter);
                }
            });

            break;

        // Displays data for the "Within 7 days Screen".
        case DueWithInWeek:
            // Does a stream that filters rentals that are not returned and due in less than
            // 7 days.
            filteredListRentals = (ArrayList<Rental>) listOfRentals.stream().filter(
                    rental -> daysBetween(rental.rentedOn, rental.dueBack) <= 7 && rental.actualDateReturned == null)
                    .collect(Collectors.toList());
            break;

        // Displays data for the "Within 7 days Games First Screen".
        case DueWithinWeekGamesFirst:
            // Does a stream that filters rentals that are not returned and due in less than
            // 7 days.
            filteredListRentals = (ArrayList<Rental>) listOfRentals.stream().filter(
                    rental -> daysBetween(rental.rentedOn, rental.dueBack) <= 7 && rental.actualDateReturned == null)
                    .collect(Collectors.toList());

            /*
             * Sort the list where Game is first then console with renter's name in
             * alphabetical order in between the two diferent rentals.
             */

            Collections.sort(filteredListRentals, (n1, n2) -> {
                /* If they are the same rental objects, compare them by renter's name. */
                if (n1 instanceof Game && n2 instanceof Game || n1 instanceof Console && n2 instanceof Console) {
                    return n1.nameOfRenter.compareTo(n2.nameOfRenter);
                }
                /*
                 * If not, one of two cases, n1 is a game but n2 is not, or n2 is a game and n1
                 * is not, if n1 is a game returns a negative to be first of the list over n2
                 * (console) else the other way around.
                 */
                if (n1 instanceof Game) {
                    return -1;
                }

                return 1;

            });
            break;

        // Displays data for the "Cap all Rentals 14 days late Screen".
        case Cap14DaysOverdue:
            // Creates stream and filter rentals that are 7 days or more late.
            filteredListRentals = (ArrayList<Rental>) ((Stream<Rental>) listOfRentals.stream().filter(
                    rental -> daysBetween(rental.rentedOn, rental.dueBack) >= 7 && rental.actualDateReturned == null)
                    // Caps all the days that are 14 days or greater between rent date and due date.
                    .map(rental -> {
                        if (daysBetween(rental.rentedOn, rental.dueBack) >= 14) {
                            rental.setNameOfRenter(rental.nameOfRenter.toUpperCase());
                        }
                        return rental;
                    })).collect(Collectors.toList());

            /*
             * Puts the list by renter's name in alphabetical order with rentals that have
             * 14 or days greater between them first.
             */
            filteredListRentals.sort(new Comparator<Rental>() {
                @Override
                public int compare(Rental n1, Rental n2) {

                    // If both Capitalized compare them by name.
                    if (n1.nameOfRenter.equals(n1.nameOfRenter.toUpperCase())
                            && n2.nameOfRenter.equals(n2.nameOfRenter.toUpperCase())) {
                        return n1.nameOfRenter.compareTo(n2.nameOfRenter);
                    }

                    // Condition that n1 is capitalized and n2 is not.
                    if ((n1.nameOfRenter.equals(n1.nameOfRenter.toUpperCase()))) {
                        return -1;
                    }

                    // Condition that n2 is capitalized and n1 is not.
                    if (n2.nameOfRenter.equals(n2.nameOfRenter.toUpperCase())) {
                        return 1;
                    }

                    // When both are uncapitialized compare them regularly.
                    return n1.nameOfRenter.compareTo(n2.nameOfRenter);
                }
            });

            break;

        /// ADDED:
        case everyThingItem:
            filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                    // THERE IS NO FILTER(AS OF NOW) SINCE IT WILL CONTAIN ALL DATA ENTRIES.
                    .collect(Collectors.toList());
            Collections.sort(filteredListRentals, (n1, n2) -> n1.nameOfRenter.compareTo(n2.nameOfRenter));

            break;

        case lateRentals:
            // Creates an object that holds today's date.
            GregorianCalendar today = new GregorianCalendar();

            // Creates a stream to filter out the rentals that are late.
            filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                    .filter(rental -> daysBetween(rental.dueBack, today) > 0 && rental.actualDateReturned == null)
                    .collect(Collectors.toList());

            Collections.sort(filteredListRentals, (n1, n2) -> {
                /* If they are the same rental objects, compare them by days late. */
                if (n1 instanceof Game && n2 instanceof Game || n1 instanceof Console && n2 instanceof Console) {
                    // Determines the days late (highest number of days late goes first).
                    int value = -(daysBetween(n1.dueBack, today) - daysBetween(n2.dueBack, today));

                    // If there is a tie, then compare by renter's name.
                    if (value == 0) {
                        return n1.nameOfRenter.compareTo(n2.nameOfRenter);
                    }
                    // No tie, return whatever has the highest days is late first.
                    return value;

                }
                /*
                 * If n1 is a game but n2 is not, or n2 is a game and n1 is not, if n1 is a game
                 * returns a negative to be higher on the list than n2 (console).
                 */
                if (n1 instanceof Game) {
                    return -1;
                }

                return 1;
            });

            break;

        default:
            throw new RuntimeException("Update is in undefined state: " + display);
        }
        fireTableStructureChanged();
    }

    /*********************************************************************************************
     * 
     * (From ListModel class) Private helper method to count the number of days
     * between two GregorianCalendar dates. Note that this is the proper way to do
     * this; trying to use other classes/methods likely won't properly account for
     * leap days.
     * 
     * @param startDate - the beginning/starting day.
     * @param endDate   - the last/ending day.
     * @return int for the number of days between startDate and endDate.
     *********************************************************************************************/
    private int daysBetween(GregorianCalendar startDate, GregorianCalendar endDate) {
        // Determine how many days the Game was rented out.
        GregorianCalendar gTemp = new GregorianCalendar();
        gTemp = (GregorianCalendar) endDate.clone(); // gTemp = dueBack; does not work!!
        int daysBetween = 0;
        while (gTemp.compareTo(startDate) > 0) {
            gTemp.add(Calendar.DATE, -1); // this subtracts one day from gTemp.
            daysBetween++;
        }

        return daysBetween;
    }

    /*********************************************************************************************************
     * 
     * (From ListModel Class) A method that returns the name of the column depending
     * on the display.
     * 
     * @param col the index value of the column.
     * @return the name of the column of the display.
     * @throws RunTimeException when the display is invalid.
     *********************************************************************************************************/
    @Override
    public String getColumnName(int col) {
        switch (display) {
        case CurrentRentalStatus:
            return columnNamesCurrentRentals[col];
        case ReturnedItems:
            return columnNamesReturned[col];
        case DueWithInWeek:
            return columnNamesCurrentRentals[col];
        case Cap14DaysOverdue:
            return columnNamesCurrentRentals[col];
        case DueWithinWeekGamesFirst:
            return columnNamesCurrentRentals[col];

        // ADDED
        case everyThingItem:
            return columnNameEverything[col];
        case lateRentals:
            return columnNameLateRentals[col];

        }
        throw new RuntimeException("Undefined state for Col Names: " + display);
    }

    /********************************************************************************************************
     * 
     * (From ListModel class) A method that returns the length of the column
     * depending on the display. (There is an override).
     * 
     * @param None
     * @return the column length.
     * @throws IllegalArgumentException when the display is not valid.
     ********************************************************************************************************/
    @Override
    public int getColumnCount() {
        switch (display) {
        case CurrentRentalStatus:
            return columnNamesCurrentRentals.length;
        case ReturnedItems:
            return columnNamesReturned.length;
        case DueWithInWeek:
            return columnNamesCurrentRentals.length;
        case Cap14DaysOverdue:
            return columnNamesCurrentRentals.length;
        case DueWithinWeekGamesFirst:
            return columnNamesCurrentRentals.length;

        // ADDED:
        case everyThingItem:
            return columnNameEverything.length;

        case lateRentals:
            return columnNameLateRentals.length;

        }
        throw new IllegalArgumentException();
    }

    /****************************************************************************************************
     * 
     * (From ListModel class) A method that returns the number of rows. (There is an
     * override)
     * 
     * @param None
     * @return returns the number of rows.
     ***************************************************************************************************/
    @Override
    public int getRowCount() {
        return filteredListRentals.size(); // returns number of items in the arraylist
    }

    /****************************************************************************************************
     * 
     * (From ListModel class) A method that returns an object data of said row and
     * column. (There is an override.)
     * 
     * @param row the index of the row that is being looked at. Row index must be
     *            valid depending on the size. The row is the specific information
     *            of a rental object.
     * @param col the index of the column that is being looked at. Index must be
     *            valid depending on the size. The column is the information
     *            category like "renter's name"
     * @return information of a rental depending on the column (category) and row
     *         (data of a rental).
     * @throws IllegalArgumentException if the display is not a variable of
     *                                  ScreenDisplay.java.
     *****************************************************************************************************/
    @Override
    public Object getValueAt(int row, int col) {
        switch (display) {
        case CurrentRentalStatus:
            return currentRentScreen(row, col);
        case ReturnedItems:
            return rentedOutScreen(row, col);
        case DueWithInWeek:
            return currentRentScreen(row, col);
        case Cap14DaysOverdue:
            return currentRentScreen(row, col);
        case DueWithinWeekGamesFirst:
            return currentRentScreen(row, col);

        // ADDED:
        case everyThingItem:
            return everyThingItem(row, col);

        case lateRentals:
            return lateRentals(row, col);

        }
        throw new IllegalArgumentException();
    }

    /*******************************************************************************************************
     * 
     * (From ListModel Class) A method that returns said data depending on the
     * column and the row (helps getValueAt method).
     * 
     * @param row the index of the row that is being looked at. Row index must be
     *            valid depending on the size. The row is the specific information
     *            of a rental object.
     * @param col the index of the column that is being looked at. Index must be
     *            valid depending on the size. The column is the information
     *            category like "renter's name".
     * @return the specific info of a rentals information depending on what the
     *         column is.
     * @throws RunTimeException when the col index is out of bounds.
     ******************************************************************************************************/
    private Object lateRentals(int row, int col) {
        switch (col) {
        // Displays the renter's name.
        case 0:
            return filteredListRentals.get(row).nameOfRenter;
        // Displays the estimated cost.
        case 1:
            return (filteredListRentals.get(row).getCost(filteredListRentals.get(row).dueBack));

        // Displays the Date rented.
        case 2:
            return (formatter.format(filteredListRentals.get(row).rentedOn.getTime()));

        // Displays the due date.
        case 3:
            if (filteredListRentals.get(row).dueBack == null) {
                return "-";
            }
            return (formatter.format(filteredListRentals.get(row).dueBack.getTime()));

        // Displays the Numbers of days late.
        case 4:
            GregorianCalendar today = new GregorianCalendar();
            return daysBetween(filteredListRentals.get(row).dueBack, today);

        // Displays the console if rented.
        case 5:
            if (filteredListRentals.get(row) instanceof Console) {
                return (((Console) filteredListRentals.get(row)).getConsoleType());
            }

            else {
                if (filteredListRentals.get(row) instanceof Game)
                    if (((Game) filteredListRentals.get(row)).getConsole() != null) {
                        return ((Game) filteredListRentals.get(row)).getConsole();
                    }
                    else {
                        return "";
                    }
            }

            // Displays the Name of the Game if rented.
        case 6:
            if (filteredListRentals.get(row) instanceof Game) {
                return (((Game) filteredListRentals.get(row)).getNameGame());
            } else {
                return "";
            }

        default:
            throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    /*************************************************************************************************
     * 
     * (From ListModel class) A private method that returns data information of a
     * certain row and column (help getValueAt method) Helps with the GUI to display
     * all the info.
     * 
     * @param row the index of the row that is being looked at. Row index must be valid
     *            depending on the size. The row is the specific information of a
     *            rental object.
     * @param col the index of the column that is being looked. Index must be valid
     *            depending on the size. The column is the information category like
     *            "renter's name".
     * @return the specific info of a rentals information depending on what the
     *         column is.
     * @throws RunTimeException when the col index is out of bounds.
     ************************************************************************************************/
    private Object everyThingItem(int row, int col) {
        switch (col) {
        // Returns the data of the Renter's name to the 0 index (1st) column.
        case 0:
            return (filteredListRentals.get(row).nameOfRenter);

        // Returns the date rented to the index 1 (2nd) column.
        case 1:
            return (formatter.format(filteredListRentals.get(row).rentedOn.getTime()));

        // Returns the due date to index 2 (3rd) column.
        case 2:
            return (formatter.format(filteredListRentals.get(row).dueBack.getTime()));

        // Returns the Actual date returned to index 3 (4th) column, if null returns
        // "Not Returned".
        case 3:
            // Checks if a date is contained so that there won't be a null pointer
            // exception.
            if (filteredListRentals.get(row).actualDateReturned != null) {
                return (formatter.format(filteredListRentals.get(row).actualDateReturned.getTime()));
            } else {
                return "Not Returned";
            }

            // Returns the estimated cost to index 4 (5th) column.
        case 4:
            if (filteredListRentals.get(row).dueBack == null) {
                return "";
            } else {
                return (filteredListRentals.get(row).getCost(filteredListRentals.get(row).dueBack));
            }

            // Returns the real cost to index 5 (6th column) if null returns "Not Returned".
        case 5:
            if (filteredListRentals.get(row).actualDateReturned == null) {
                return "Not Returned";
            } else {
                return (filteredListRentals.get(row).getCost(filteredListRentals.get(row).actualDateReturned));
            }

            // Returns the Console name if rented to index 6 (5th column).
        case 6:
            if (filteredListRentals.get(row) instanceof Console) {
                return (((Console) filteredListRentals.get(row)).getConsoleType());
            }

            else {
                if (filteredListRentals.get(row) instanceof Game)
                    if (((Game) filteredListRentals.get(row)).getConsole() != null) {
                        return ((Game) filteredListRentals.get(row)).getConsole();
                    }
                    else {
                        return "";
                    }
            }

            // Returns the name of the Game to index 7 (6th column) if applicable.
        case 7:
            if (filteredListRentals.get(row) instanceof Game) {
                return (((Game) filteredListRentals.get(row)).getNameGame());
            } else {
                return "";
            }
        default:
            throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    /*************************************************************************************************
     * 
     * (From ListModel class) A private method that returns data information of a
     * certain row and column (helps getValueAt method).
     * 
     * @param row the index of the row that is being looked at. Row index must be valid
     *            depending on the size. The row is the specific information of a
     *            rental object.
     * @param col the index of the column that is being looked at. Index must be valid
     *            depending on the size. The column is the information category like
     *            "renter's name".
     * @return the specific info of a rentals information depending on what the
     *         column is.
     * @throws RunTimeException when the col index is out of bounds.
     *************************************************************************************************/
    private Object currentRentScreen(int row, int col) {
        switch (col) {
        // Returns the rental object renter's name.
        case 0:
            return (filteredListRentals.get(row).nameOfRenter);

        // Returns the rental object cost.
        case 1:
            return (filteredListRentals.get(row).getCost(filteredListRentals.get(row).dueBack));

        // Returns the rental object rental date.
        case 2:
            return (formatter.format(filteredListRentals.get(row).rentedOn.getTime()));

        // Returns the due date of the rental if applicable.
        case 3:
            if (filteredListRentals.get(row).dueBack == null)
                return "-";

            return (formatter.format(filteredListRentals.get(row).dueBack.getTime()));

        // If the rental is a console rental, returns the console rented.
        case 4:
            if (filteredListRentals.get(row) instanceof Console) {
                return (((Console) filteredListRentals.get(row)).getConsoleType());
            }

            /*
             * If the rental is a Game returns the console rented, if no console was rented
             * return an empty string.
             */
            else {
                if (filteredListRentals.get(row) instanceof Game)
                    if (((Game) filteredListRentals.get(row)).getConsole() != null) {
                        return ((Game) filteredListRentals.get(row)).getConsole();
                    }

                    else {
                        return "";
                    }
            }

            // If the rental is a game, returns the name of the game, else returns an empty
            // string.
        case 5:
            if (filteredListRentals.get(row) instanceof Game) {
                return (((Game) filteredListRentals.get(row)).getNameGame());
            }
            else {
                return "";
            }
        default:
            throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    /********************************************************************************************
     * 
     * (From ListModel class) A private method that returns a data information of a
     * certain row and column. (Helps getValueAt() method)
     * 
     * @param row the index of the row that is being looked at. Row index must be valid
     *            depending on the size. The row is the specific information of a
     *            rental.
     * @param col the index of the column that is being looked at. Index must be valid
     *            depending on the size. The column is the information category like
     *            "renter's name".
     * @return the specific info of a rentals information depending on what the
     *         column is.
     * @throws RunTimeException when the col index is out of bounds.
     ******************************************************************************************/
    private Object rentedOutScreen(int row, int col) {
        switch (col) {
        // Returns the name of the renter.
        case 0:
            return (filteredListRentals.get(row).nameOfRenter);

        // Returns the date the rental was placed.
        case 1:
            return (formatter.format(filteredListRentals.get(row).rentedOn.getTime()));

        // Returns the due date of the rental.
        case 2:
            return (formatter.format(filteredListRentals.get(row).dueBack.getTime()));

        // Returns the actual date returned.
        case 3:
            return (formatter.format(filteredListRentals.get(row).actualDateReturned.getTime()));

        // Returns the cost.
        case 4:
            return (filteredListRentals.get(row).getCost(filteredListRentals.get(row).dueBack));

        // Returns the date the rental was returned.
        case 5:
            return (filteredListRentals.get(row).getCost(filteredListRentals.get(row).actualDateReturned));

        default:
            throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    /*****************************************************************************************
     * 
     * (From ListModel class) A method that adds an object of type Rental to the
     * list of rentals.
     * 
     * @param a the object of type rental.
     * @return Nothing.
     ****************************************************************************************/
    public void add(Rental a) {
        listOfRentals.add(a);
        updateScreen();
    }

    /******************************************************************************************
     * 
     * (From ListModel class) A method that returns a Rental object from the
     * filtered list.
     * 
     * @param i is the index value of the rental list. Must be a valid index of the
     *          list.
     * @return the rental object of the specified index.
     * @throws IllegalArgumentException when the index value is the size of the list
     *                                  or greater.
     ******************************************************************************************/
    public Rental get(int i) {
        if (i >= filteredListRentals.size()) {
            throw new IllegalArgumentException();
        }
        return filteredListRentals.get(i);
    }

    /*******************************************************************************************
     * 
     * (From ListModel class) A method that updates adds a rental object to the
     * specified index.
     * 
     * @param index the index of the list of where to put the rental at.
     * @param unit  the name of the rental object.
     * @return None.
     ********************************************************************************************/
    public void update(int index, Rental unit) {
        updateScreen();
    }

    /*******************************************************************************************
     * 
     * (From ListModel class) A method that saves the database to a file.
     * 
     * @param filename the name of the save file.
     * @return None.
     * @throws RunTimeException when there is something wrong with saving the data.
     *******************************************************************************************/
    public void saveDatabase(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            System.out.println(listOfRentals.toString());
            os.writeObject(listOfRentals);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    /*****************************************************************************************
     * 
     * (From ListModel class) A method that loads the data from a file.
     * 
     * @param filename the name of the file being loaded.
     * @return None
     * @throws RuntimeException when there is a loading problem with the file being
     *                          loaded.
     *****************************************************************************************/
    public void loadDatabase(String filename) {
        listOfRentals.clear();

        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            listOfRentals = (ArrayList<Rental>) is.readObject();
            updateScreen();
            is.close();
        } catch (Exception ex) {
            throw new RuntimeException("Loading problem: " + display);

        }
    }

    /***************************************************************************************
     * 
     * (From ListModel class) A method that saves the text to a file, and return a
     * boolean value if successful.
     * 
     * @param filename the name of the file that the data is being saved to.
     * @return a boolean value indicating if the save is possible.
     * @throws IllegalArgumentException when file name is left blank.
     ***************************************************************************************/
    public boolean saveAsText(String filename) {
        if (filename.equals("")) {
            throw new IllegalArgumentException();
        }
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            out.println(listOfRentals.size());
            for (int i = 0; i < listOfRentals.size(); i++) {
                Rental unit = listOfRentals.get(i);
                out.println(unit.getClass().getName());
                out.println("Name is " + unit.getNameOfRenter());
                out.println("Rented on " + formatter.format(unit.rentedOn.getTime()));
                out.println("DueDate " + formatter.format(unit.dueBack.getTime()));

                if (unit.getActualDateReturned() == null)
                    out.println("Not returned!");
                else
                    out.println(formatter.format(unit.actualDateReturned.getTime()));

                if (unit instanceof Game) {
                    out.println(((Game) unit).getNameGame());
                    if (((Game) unit).getConsole() != null)
                        out.println(((Game) unit).getConsole());
                    else
                        out.println("No Console");
                }
                if (unit instanceof Console)
                    out.println(((Console) unit).getConsoleType());
            }
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /****************************************************************************
     * 
     * (From ListModel class) A method that loads data from a file text.
     * 
     * @param filename the name of file. The file that is opened must be created
     *                 from the program and unedited in order to work properly due
     *                 to the format used to read the data.
     * @return Nothing.
     * @throws IllegalArgumentException when file name is invalid.
     * @throws ParseException           when the process of getting the date is
     *                                  messed up.
     ****************************************************************************/
    public void loadFromText(String filename) {
        listOfRentals.clear();
        // Checks if file exist.
        if (filename == null) {
            throw new IllegalArgumentException("File is invalid.");

        }
        Scanner scanner = null;
        try {
            // Creates a scanner.
            scanner = new Scanner(new File(filename));

            // Date format to parse.
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            // Creates local variable for parameter of constructors.
            String nameOfRenter = "";
            GregorianCalendar rentedOn = new GregorianCalendar();
            GregorianCalendar dueBack = new GregorianCalendar();
            GregorianCalendar actualDateReturned = new GregorianCalendar();
            ConsoleTypes consoleType = ConsoleTypes.NoSelection;
            String nameGame = "";

            /*
             * Variables to read the line that matches with the data, only need 1, but it's
             * easier to know the order.
             */
            String rentLine;
            String dueDateLine;
            String returnLine;
            String consoleLine;

            // Skips the string that contains the data size (Rental objects) of the file.
            scanner.nextLine();

            // Loop the creates the rental objects of the data file.
            while (scanner.hasNextLine()) {
                // Finds the game or rental type.
                String objectLine = scanner.nextLine();
                // Gets the basic information that all rentals have.
                nameOfRenter = scanner.nextLine();
                nameOfRenter = nameOfRenter.replace("Name is ", "");
                rentLine = scanner.nextLine();
                rentLine = rentLine.replace("Rented on", "");
                rentedOn.setTime(df.parse(rentLine));
                dueDateLine = scanner.nextLine();
                dueDateLine = dueDateLine.replace("DueDate", "");
                dueBack.setTime(df.parse(dueDateLine));
                returnLine = scanner.nextLine();
                if (returnLine.equals("Not returned!")) {
                    actualDateReturned = null;
                } else {
                    actualDateReturned.setTime(df.parse(returnLine));
                }
                // Determines if the data is a Game rental.
                if (objectLine.contains(".Game")) {
                    nameGame = scanner.nextLine();
                    consoleLine = scanner.nextLine();
                    if (consoleLine.equals("No Console")) {
                        consoleType = null;
                    } else {
                        consoleType = ConsoleTypes.valueOf(consoleLine);
                    }
                    listOfRentals
                            .add(new Game(nameOfRenter, rentedOn, dueBack, actualDateReturned, nameGame, consoleType));
                }

                // Determines if the data is a Console rental.
                else {
                    consoleType = ConsoleTypes.valueOf(scanner.nextLine());
                    listOfRentals.add(new Console(nameOfRenter, rentedOn, dueBack, actualDateReturned, consoleType));
                }
                // Resets info for next data entries.
                rentedOn = new GregorianCalendar();
                dueBack = new GregorianCalendar();
                actualDateReturned = new GregorianCalendar();
                consoleType = ConsoleTypes.NoSelection;
            }

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Error with file");

        } catch (ParseException e) {
            // When the date cannot be converted.
            e.printStackTrace();
        }

        updateScreen();
    }

    /**************************************************************************************************
     * 
     * //ADDED (From the ListModel Class) A method that titles the renters name due
     * to the format that capitalizes the renters name if the days after the rent
     * date and due date is greater or equal to 14 days.
     * 
     * @param None.
     * @return None.
     **************************************************************************************************/
    private void titleRentersName() {
        filteredListRentals = (ArrayList<Rental>) ((Stream<Rental>) listOfRentals.stream().map(rental -> {

            if (daysBetween(rental.rentedOn, rental.dueBack) >= 14) {
                // Makes copy of the renters name.
                String name = rental.nameOfRenter.toLowerCase();

                // First capitalizes the first character of the String.
                String charToTitle = name.substring(0, 1).toUpperCase();
                name = name.substring(1, name.length());
                name = charToTitle + name;

                for (int i = 0; i < name.length(); i++) {
                    // Capitalizes the character after the space.
                    if (name.charAt(i) == ' ' && !(i == name.length() - 3 || i == name.length() - 2
                            || i == name.length() - 1 || i == name.length())) {
                        String stringBeforeCapLetter = name.substring(0, i + 1);
                        String stringAfterCapLetter = name.substring(i + 2, name.length());
                        charToTitle = name.substring(i + 1, i + 2).toUpperCase();
                        name = stringBeforeCapLetter + charToTitle + stringAfterCapLetter;
                    }
                }
                // Sets the renter name from the result.
                rental.setNameOfRenter(name);
            }
            return rental;
        })).collect(Collectors.toList());
    }

    /***************************************************************************
     *
     * (From ListModel class) A method used for testing. DO NOT MODIFY THIS
     * METHOD!!!!!!
     * 
     * @param None. (test method)
     * @return None. (test method)
     **************************************************************************/
    public void createList() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();
        GregorianCalendar g7 = new GregorianCalendar();
        GregorianCalendar g8 = new GregorianCalendar();

        try {
            Date d1 = df.parse("1/20/2020");
            g1.setTime(d1);
            Date d2 = df.parse("12/22/2020");
            g2.setTime(d2);
            Date d3 = df.parse("12/20/2019");
            g3.setTime(d3);
            Date d4 = df.parse("7/02/2020");
            g4.setTime(d4);
            Date d5 = df.parse("1/20/2010");
            g5.setTime(d5);
            Date d6 = df.parse("9/29/2020");
            g6.setTime(d6);
            Date d7 = df.parse("7/25/2020");
            g7.setTime(d7);
            Date d8 = df.parse("7/29/2020");
            g8.setTime(d8);

            Console console1 = new Console("Person1", g4, g6, null, ConsoleTypes.PlayStation4);
            Console console2 = new Console("Person2", g5, g3, null, ConsoleTypes.PlayStation4);
            Console console3 = new Console("Person5", g4, g8, null, ConsoleTypes.SegaGenesisMini);
            Console console4 = new Console("Person6", g4, g7, null, ConsoleTypes.SegaGenesisMini);
            Console console5 = new Console("Person1", g5, g4, g3, ConsoleTypes.XBoxOneS);

            Game game1 = new Game("Person1", g3, g2, null, "title1", ConsoleTypes.PlayStation4);
            Game game2 = new Game("Person1", g3, g1, null, "title2", ConsoleTypes.PlayStation4);
            Game game3 = new Game("Person1", g5, g3, null, "title2", ConsoleTypes.SegaGenesisMini);
            Game game4 = new Game("Person7", g4, g8, null, "title2", null);
            Game game5 = new Game("Person3", g3, g1, g1, "title2", ConsoleTypes.XBoxOneS);
            Game game6 = new Game("Person6", g4, g7, null, "title1", ConsoleTypes.NintendoSwitch);
            Game game7 = new Game("Person5", g4, g8, null, "title1", ConsoleTypes.NintendoSwitch);

            add(game1);
            add(game4);
            add(game5);
            add(game2);
            add(game3);
            add(game6);
            add(game7);

            add(console1);
            add(console2);
            add(console5);
            add(console3);
            add(console4);

            // create a bunch of them.
            int count = 0;
            Random rand = new Random(13);
            String guest = null;

            while (count < 300) { // changed this number to 300 for a complete test of the code.
                Date date = df.parse("7/" + (rand.nextInt(10) + 2) + "/2020");
                GregorianCalendar g = new GregorianCalendar();
                g.setTime(date);
                if (rand.nextBoolean()) {
                    guest = "Game" + rand.nextInt(5);
                    Game game;
                    if (count % 2 == 0)
                        game = new Game(guest, g4, g, null, "title2", ConsoleTypes.NintendoSwitch);
                    else
                        game = new Game(guest, g4, g, null, "title2", null);
                    add(game);

                } else {
                    guest = "Console" + rand.nextInt(5);
                    date = df.parse("7/" + (rand.nextInt(20) + 2) + "/2020");
                    g.setTime(date);
                    Console console = new Console(guest, g4, g, null, getOneRandom(rand));
                    add(console);
                }

                count++;
            }
        } catch (ParseException e) {
            throw new RuntimeException("Error in testing, creation of list");
        }
    }

    /***************************************************************************************************
     * 
     * (From ListModel class) A method that returns a random console. (Test method)
     * 
     * @param rand ???
     * @return a specific console.
     ***************************************************************************************************/
    public ConsoleTypes getOneRandom(Random rand) {

        int number = rand.nextInt(ConsoleTypes.values().length - 1);
        switch (number) {
        case 0:
            return ConsoleTypes.PlayStation4;
        case 1:
            return ConsoleTypes.XBoxOneS;
        case 2:
            return ConsoleTypes.PlayStation4Pro;
        case 3:
            return ConsoleTypes.NintendoSwitch;
        default:
            return ConsoleTypes.SegaGenesisMini;
        }
    }
}