package project2GIVE_TO_STUDENTS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/******************************************f******************************
 * 
 * Helps the GUI with stuff like creating a new rental of class Console to add
 * to the list, or add a return date to a previous rental item.
 * 
 * * @author Original authors + Romandy Vu, Aidan Takace, Scott Richards.
 * 
 * @version 2/10/2021
 *
 ********************************************************************/
public class RentConsoleDialog extends JDialog implements ActionListener {
    private JTextField txtRenterName;
    private JTextField txtDateRentedOn;
    private JTextField txtDateDueDate;
    private JComboBox<ConsoleTypes> comBoxConsoleType;
    private JButton okButton;
    private JButton cancelButton;
    private int closeStatus;
    private Console console;
    public static final int OK = 0;
    public static final int CANCEL = 1;

    // ADDED so that it will add the rental to the list if true.
    public static boolean isInputValid = true;
    /*********************************************************************************************
     * 
     * A private Method that determines if year is a leap year to help the dateInvalid method.
     *
     * @param y -  the year
     * @return The return value will return true if the year is a leap year.
     ******************************************************************************************/
    private static boolean isLeapYear(int y){
        if (y % 400 == 0){
            return true;
        }
        else if (y % 100 == 0){
            return false;
        }
        else if (y % 4 == 0){
            return true;
        }
        else{
            return false;
        }

    }

    /*********************************************************************************************
     * 
     * (From RentConsoleDialog class) Private helper method to check to see if the
     * input strings of the rent on date and the due date are invalid when it is put
     * in the RentConsoleDialog pop-up.
     * 
     * @param rentDateString - the input string that was entered for the rent on
     *                       date textbox.
     * @param dueDateString  - the input string that was entered for the due date
     *                       textbox.
     * @return a boolean value that compares the dates to see if it was invalid or
     *         not.
     *********************************************************************************************/
    private boolean datesInvalid(String rentDateString, String dueDateString) {
        boolean result = false;
        int firstSlash = rentDateString.indexOf("/");
        int lastSlash = rentDateString.lastIndexOf("/");
        

        int rentDateMonth = Integer.parseInt(rentDateString.substring(0, firstSlash));
        int rentDateDay = Integer.parseInt(rentDateString.substring(firstSlash + 1, lastSlash));
        int rentDateYear = Integer.parseInt(rentDateString.substring(lastSlash + 1, rentDateString.length()));

        firstSlash = dueDateString.indexOf("/");
        lastSlash = dueDateString.lastIndexOf("/");

        int dueDateMonth = Integer.parseInt(dueDateString.substring(0, firstSlash));
        int dueDateDay = Integer.parseInt(dueDateString.substring(firstSlash + 1, lastSlash));
        int dueDateYear = Integer.parseInt(dueDateString.substring(lastSlash + 1, dueDateString.length()));

        boolean isLeapYear = isLeapYear(dueDateYear);

        if (rentDateDay > 31 || rentDateDay < 1 || rentDateMonth > 12 || rentDateMonth < 1 || dueDateDay > 31
                || dueDateDay < 1 || dueDateMonth > 12 || dueDateMonth < 1) {
            result = true;
        }
        else if((dueDateMonth == rentDateMonth + 1) &&  
                (((rentDateMonth == 9 || rentDateMonth == 4 || rentDateMonth == 6 || rentDateMonth == 11) && (rentDateDay == 31 && dueDateDay == 1)))){
            result = true;
        }
        else if (dueDateMonth == rentDateMonth + 1 && dueDateMonth == 3
                && isLeapYear && ((rentDateDay == 30 && dueDateDay <= 1) || (rentDateDay == 31 && dueDateDay <= 2))){
            result = true;
        }
        else if (dueDateMonth == rentDateMonth + 1 && dueDateMonth == 3
                && (!isLeapYear) && ((rentDateDay == 29 && dueDateDay <= 1) || (rentDateDay == 30 && dueDateDay <= 2)
                 || (rentDateDay == 31 && dueDateDay <= 3))){
            result = true;
        }
         else if (rentDateYear > dueDateYear) {
            result = true;
        } else if (rentDateYear < dueDateYear) {
        } else if (rentDateMonth > dueDateMonth) {
            result = true;
        } else if (rentDateMonth < dueDateMonth) {
        } else if (rentDateDay >= dueDateDay) {
            result = true;
        } else if (rentDateDay < dueDateDay) {
        }

        return result;

    }

    /*********************************************************
     * (From RentConsoleDialog class) Instantiate a Custom Dialog as 'modal' and
     * wait for the user to provide data and click on a button.
     * 
     * @param parent  reference to the JFrame application.
     * @param console an instantiated object to be filled with data.
     * @return None.
     *********************************************************/

    public RentConsoleDialog(JFrame parent, Console console) {
        // call parent and create a 'modal' dialog.
        super(parent, true);
        this.console = console;

        setTitle("Console dialog box");
        closeStatus = CANCEL;
        setSize(500, 200);

        // prevent user from closing window.
        // setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        txtRenterName = new JTextField("Roger", 30);
        txtDateRentedOn = new JTextField(25);
        txtDateDueDate = new JTextField(25);
        comBoxConsoleType = new JComboBox<>(ConsoleTypes.values());

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateNow = formatter.format(currentDate.getTime());
        currentDate.add(Calendar.DATE, 1);
        String datetomorrow = formatter.format(currentDate.getTime());

        txtDateRentedOn.setText(dateNow);
        txtDateDueDate.setText(datetomorrow);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(4, 2));

        textPanel.add(new JLabel("Name of Renter: "));
        textPanel.add(txtRenterName);
        textPanel.add(new JLabel("Date Rented On "));
        textPanel.add(txtDateRentedOn);
        textPanel.add(new JLabel("Date Due (est.): "));
        textPanel.add(txtDateDueDate);
        textPanel.add(new JLabel("ConsoleType"));
        textPanel.add(comBoxConsoleType);

        getContentPane().add(textPanel, BorderLayout.CENTER);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setVisible(true);
    }

    /**************************************************************
     * (From RentConsoleDialog class) Respond to either button clicks.
     * 
     * @param e the action event that was just fired.
     **************************************************************/
    public void actionPerformed(ActionEvent e) {
        isInputValid = true;

        JButton button = (JButton) e.getSource();

        // if OK clicked the fill the object.
        if (button == okButton) {
            // save the information in the object.
            closeStatus = OK;
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date d1 = null;
            Date d2 = null;
            try {
                // Tries to convert the string to the proper date.
                GregorianCalendar gregTemp = new GregorianCalendar();
                d1 = df.parse(txtDateRentedOn.getText());
                gregTemp.setTime(d1);
                console.setRentedOn(gregTemp);

                gregTemp = new GregorianCalendar();
                d2 = df.parse(txtDateDueDate.getText());
                gregTemp.setTime(d2);
                console.setDueBack(gregTemp);

                // If the rental date is after or on the due date, displays a message and won't
                // close dialog.
                String rentDateString = txtDateRentedOn.getText();
                String dueDateString = txtDateDueDate.getText();
                if (datesInvalid(rentDateString, dueDateString)) {
                    JOptionPane.showMessageDialog(this,
                            "Due date should be days after the rent date, and Month is between (1-12) and Day is between (1-31)");
                    isInputValid = false;
                    closeStatus = CANCEL;

                }

            } catch (ParseException e1) {
                // Check for formatting exceptions, flag any that come up.
                JOptionPane.showMessageDialog(this, "Invalid input, Try again.");
                isInputValid = false;
            }

            console.setNameOfRenter(txtRenterName.getText());
            console.setConsoleType((ConsoleTypes) comBoxConsoleType.getSelectedItem());

            // If the option for a Console was "NoSelection" it will display a message and
            // not close the dialog.
            if ((ConsoleTypes) comBoxConsoleType.getSelectedItem() == ConsoleTypes.NoSelection) {
                JOptionPane.showMessageDialog(null, "Select Console.");
                isInputValid = false;
                closeStatus = CANCEL;
            }
        }

        // Make the dialog disappear if the input is valid or the new rental was
        // cancelled.
        if (isInputValid) {
            dispose();
        }
    }

    /**************************************************************
     * (From RentConsoleDialog class) Return a String to let the caller know which
     * button was clicked.
     * 
     * @return an int representing the option OK or CANCEL.
     **************************************************************/
    public int getCloseStatus() {
        return closeStatus;
    }

}