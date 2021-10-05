package project2GIVE_TO_STUDENTS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

/***************************************************************************************************
 *
 * A file that holds the code to the GUI. There is a generalization and an
 * implementation. Implements ListModel.java, RentConsoleDialog, RentGameDialog,
 * and ReturnedOnDialog.
 * 
 * @author Original authors + Romandy Vu, Aidan Takace, and Scott Richards.
 * @version 2/10/2021
 * 
 *************************************************************************************************/
public class GUIRentalStore extends JFrame implements ActionListener {

    /* INSTANCE VARIABLES */

    /** The menubar that will contain the file and action dropdown menu. */
    private JMenuBar menus;

    /** A drop down menu. */
    private JMenu fileMenu;

    /** Another drop down menu. */
    private JMenu actionMenu;

    /**
     * A menu item that changes the display to a certain screen or opens a new
     * window.
     */

    /** A menu item that opens a save file. */
    private JMenuItem openSerItem;

    /** A menu item that exits the GUI. */
    private JMenuItem exitItem;

    /** A menu item to save a file. */
    private JMenuItem saveSerItem;

    /** A menu item to opens a text file. */
    private JMenuItem openTextItem;

    /** A menu item that saves a file to a text. */
    private JMenuItem saveTextItem;

    /** A menu item that opens to place info for a new console rental. */
    private JMenuItem rentConsoleItem;

    /** A menu item that opens to place info for a new game rental. */
    private JMenuItem rentGameItem;

    /* A menu item that opens to place the info the the returned item. */
    private JMenuItem returnItem;

    /** A menu item that displays the currently rented items. */
    private JMenuItem currentRentedItemScn;

    /** A menu item that displays the returned items. */
    private JMenuItem currentReturnedItemScn;

    /** A menu item that displays data of rentals due within 7 days. */
    private JMenuItem withIn7ItemScn;

    /**
     * A menu item that displays rentals that are greater than 7 days with the
     * renter names in caps if greater than 14 days.
     */
    private JMenuItem sort30DaysItemScn;

    /**
     * A menu item that displays rentals due within the first week with game rentals
     * being first.
     */
    private JMenuItem sortGameItemScn;

    /** ADDED a menu item that displays everything. */
    private JMenuItem everyThingItem;

    /** ADDED (Task 4) a menu item that shows rentals past their due dates. */
    private JMenuItem lateRentals;

    /** Creates the JPanel. */
    private JPanel panel;

    /** a ListModel (From ListModel.java). */
    private ListModel dList;

    /** A JTable that will hold the ListModel. */
    private JTable jTable;

    /**
     * a ScrollPane to view the items of the list if all the list item can't be
     * displayed at once.
     */
    private JScrollPane scrollList;

    /*******************************************************************************************
     * 
     * The default constructor that creates the GUI.
     *
     * @param None. (default constructor)
     * @return Nothing. (default constructor)
     ******************************************************************************************/
    public GUIRentalStore() {
        // Creates the object and names the display name of the menu.
        menus = new JMenuBar();
        fileMenu = new JMenu("File");
        actionMenu = new JMenu("Action");
        openSerItem = new JMenuItem("Open File");
        exitItem = new JMenuItem("Exit");
        saveSerItem = new JMenuItem("Save File");
        openTextItem = new JMenuItem("Open Text");
        saveTextItem = new JMenuItem("Save Text");
        rentConsoleItem = new JMenuItem("Rent a Console");
        rentGameItem = new JMenuItem("Rent a Game");
        returnItem = new JMenuItem("Return of Game or Console");

        currentRentedItemScn = new JMenuItem("Current Rental Screen");
        currentReturnedItemScn = new JMenuItem("Returned screen");
        withIn7ItemScn = new JMenuItem("Within 7 Days Screen");
        sortGameItemScn = new JMenuItem("Within 7 Days Games First Screen");
        sort30DaysItemScn = new JMenuItem("Cap all Rentals 14 days late Screen");

        // ADDED
        everyThingItem = new JMenuItem("Everything Screen");
        lateRentals = new JMenuItem("Late Rentals");

        /* Adds the menu item to the drop down menu of file Menu. */
        fileMenu.add(openSerItem);
        fileMenu.add(saveSerItem);
        fileMenu.addSeparator();

        fileMenu.add(openTextItem);
        fileMenu.add(saveTextItem);
        fileMenu.addSeparator();

        fileMenu.add(exitItem);
        fileMenu.addSeparator();

        fileMenu.add(currentRentedItemScn);
        fileMenu.add(currentReturnedItemScn);
        fileMenu.add(withIn7ItemScn);
        fileMenu.add(sortGameItemScn);
        fileMenu.add(sort30DaysItemScn);

        // ADDED
        fileMenu.add(everyThingItem);
        fileMenu.addSeparator();
        fileMenu.add(lateRentals);
        fileMenu.addSeparator();

        /* Adds the menu item to the drop down menu of action Menu. */
        actionMenu.add(rentConsoleItem);
        actionMenu.add(rentGameItem);
        actionMenu.addSeparator();
        actionMenu.add(returnItem);

        /* Adds the drop down menu to the menu bar. */
        menus.add(fileMenu);
        menus.add(actionMenu);

        /* Adds an action listener to the menu. */
        openSerItem.addActionListener(this);
        saveSerItem.addActionListener(this);
        openTextItem.addActionListener(this);
        saveTextItem.addActionListener(this);
        exitItem.addActionListener(this);
        rentConsoleItem.addActionListener(this);
        rentGameItem.addActionListener(this);
        returnItem.addActionListener(this);

        currentRentedItemScn.addActionListener(this);
        currentReturnedItemScn.addActionListener(this);
        withIn7ItemScn.addActionListener(this);
        sortGameItemScn.addActionListener(this);
        sort30DaysItemScn.addActionListener(this);

        // ADDED
        everyThingItem.addActionListener(this);
        lateRentals.addActionListener(this);

        setJMenuBar(menus);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        dList = new ListModel();
        jTable = new JTable(dList);
        scrollList = new JScrollPane(jTable);
        panel.add(scrollList);
        add(panel);
        scrollList.setPreferredSize(new Dimension(1000, 800));

        // Sets the visibility and size of the GUI at launch.
        setVisible(true);
        setSize(1070, 950);
    }

    /************************************************************************************
     * 
     * (From GUIRentalStore class) A method that determines what to do when an
     * action is performed. (Clicked upon)
     * 
     * @param e the action input that was performed.
     * @return None.
     **********************************************************************************/
    public void actionPerformed(ActionEvent e) {
        // Temporary creates a variable that holds the action.
        Object comp = e.getSource();

        /*
         * Determines if the action performed was on the menu item and sets the display.
         */
        if (currentRentedItemScn == comp)
            dList.setDisplay(ScreenDisplay.CurrentRentalStatus);

        if (currentReturnedItemScn == comp)
            dList.setDisplay(ScreenDisplay.ReturnedItems);

        if (withIn7ItemScn == comp) {
            // Code maybe unused, but probably needed somewhere.
            GregorianCalendar dat = new GregorianCalendar();
            dList.setDisplay(ScreenDisplay.DueWithInWeek);
        }

        if (sortGameItemScn == comp)
            dList.setDisplay(ScreenDisplay.DueWithinWeekGamesFirst);

        if (sort30DaysItemScn == comp)
            dList.setDisplay(ScreenDisplay.Cap14DaysOverdue);

        if (openSerItem == comp || openTextItem == comp) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (openSerItem == comp)
                    dList.loadDatabase(filename);
                else if (openTextItem == comp)
                    dList.loadFromText((filename));
            }
        }

        if (saveSerItem == comp || saveTextItem == comp) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showSaveDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (saveSerItem == e.getSource())
                    dList.saveDatabase(filename);
                else if (saveTextItem == comp)
                    dList.saveAsText(filename);
            }
        }

        if (e.getSource() == exitItem) {
            System.exit(1);
        }
        if (e.getSource() == rentConsoleItem) {
            Console Console = new Console();
            RentConsoleDialog dialog = new RentConsoleDialog(this, Console);
            if (dialog.getCloseStatus() == RentConsoleDialog.OK && RentConsoleDialog.isInputValid) {
                dList.add(Console);
            }
        }
        if (e.getSource() == rentGameItem) {
            Game gameOnly = new Game();
            RentGameDialog dialog = new RentGameDialog(this, gameOnly);
            if (dialog.getCloseStatus() == RentGameDialog.OK && RentGameDialog.isInputValid) {
                dList.add(gameOnly);
            }
        }

        if (returnItem == e.getSource()) {
            int index = jTable.getSelectedRow();
            if (index != -1) {
                // Code below maybe unused but probably needed somewhere.
                GregorianCalendar dat = new GregorianCalendar();

                Rental unit = dList.get(index);
                // Code may seem unused, but it creates a pop up for the return date entry.
                ReturnedOnDialog dialog = new ReturnedOnDialog(this, unit);

                /*
                 * If button on ReturnedOnDialog is pressed and the input is valid displays the
                 * message.
                 */
                if (ReturnedOnDialog.hasReturnDate) {
                    JOptionPane.showMessageDialog(null,
                            "  Be sure to thank " + unit.nameOfRenter + "\n for renting with us. The price is:  "
                                    + unit.getCost(unit.actualDateReturned) + " dollars");
                    dList.update(index, unit);
                }

            }
        }
        // ADDED
        if (everyThingItem == e.getSource()) {
            dList.setDisplay(ScreenDisplay.everyThingItem);
        }

        if (lateRentals == e.getSource()) {
            dList.setDisplay(ScreenDisplay.lateRentals);
        }
    }

    // RUN This to create the GUI.
    public static void main(String[] args) {
        new GUIRentalStore();
    }
}