import java.util.Scanner;
import java.sql.*;

public class Main
{
    public static void main(String[] args)
    {
        boolean menu = true;
        while (menu)
        {
            int option = displayMenuAndPromptOption();
            switch (option) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    showAccount();
                    break;
                case 3:
                    alterAccount();
                    break;
                case 4:
                    removeAccount();
                    break;
                case 5:
                    showAllAccounts();
                    break;
                case 9:
                    System.out.println("Thank you for using Tabajara's Banking System :)");
                    menu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a valid option.");
            }
        }
    }

    public static int displayMenuAndPromptOption()
    {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        boolean validOption = false;

        // show main menu
        System.out.println("\n-------------------------");
        System.out.println("Tabajara's Banking System.\n");
        System.out.println("Enter your option: ");
        System.out.println("1 - Register account.");
        System.out.println("2 - Show account.");
        System.out.println("3 - Alter account.");
        System.out.println("4 - Remove account.");
        System.out.println("5 - Show all accounts.");
        System.out.println("9 - Exit system.\n");

        // ask for a valid option until it's valid
        while (!validOption)
        {
            try {
                System.out.print("Your option: ");
                int input = Integer.parseInt(scanner.nextLine());
                option = input;
                validOption = true;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid option. Please enter a valid option.");
            }
        }
        return option;
    }

    public static void registerAccount()
    {
        // variables
        Scanner scanner = new Scanner(System.in);
        String name;
        float balance = 0;
        boolean validOption = false;

        // get name
        System.out.println("\n-------------------------\n");
        System.out.println("You choose \"Register account\".\n");
        System.out.print("Insert the name: ");
        name = scanner.nextLine();

        // ask for a valid balance until its valid
        while (!validOption)
        {
            try
            {
                System.out.print("Insert balance: ");
                balance = Float.parseFloat(scanner.nextLine());
                validOption = true;
            }
            catch (Exception e)
            {
                System.out.println("Invalid balance. Please enter a valid balance.");
            }
        }

        // insert on the table
        try
        {
            // make connection
            Connection connection = SQLConnection.open();
            Statement s = connection.createStatement();

            // insert
            s.executeUpdate
            (
            "insert into Accounts (name, balance) values ('" + name + "', '" + balance + "')"
            );
            connection.close();
            System.out.println("Account registered successfully.");
            System.out.print("\nPress enter to continue: ");
            scanner.nextLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showAccount()
    {
        Scanner scanner = new Scanner(System.in);
        int id;
        boolean validOption = false;

        while (!validOption)
        {
            try
            {
                // get id
                System.out.println("\n-------------------------\n");
                System.out.println("You choose \"Show account\".\n");
                System.out.print("Enter a account id (Enter 0 to Exit): ");
                id = Integer.parseInt(scanner.nextLine());
                System.out.print("\n");

                // exit if id is 0
                if (id == 0)
                {
                    break;
                }

                // make connection
                Connection connection = SQLConnection.open();
                Statement s = connection.createStatement();

                // make query
                ResultSet rs = s.executeQuery("select * from Accounts where id = '" + id + "'");

                // check if the rs has a row
                if (rs.next())
                {
                    // show query
                    System.out.println("ID: " + rs.getInt("id") + "Name: " + rs.getString("name") + "Balance: " + rs.getFloat("balance"));
                }
                else
                {
                    System.out.println("No account found with ID " + id);
                }

                // close resources
                rs.close();
                s.close();
                connection.close();

                System.out.print("\nPress enter to continue: ");
                scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Invalid id. Enter a valid id.");
            }
        }
    }

    public static void alterAccount()
    {
        // variables
        Scanner scanner = new Scanner(System.in);
        String newName = null; // Inicialize com null
        float newBalance = -1;
        String newBalanceString;
        int id;
        String inputName;

        try
        {
            // make connection
            Connection connection = SQLConnection.open();
            Statement s = connection.createStatement();

            // get id
            System.out.print("Enter the id of the account to modify: ");
            id = Integer.parseInt(scanner.nextLine());

            // get new name
            System.out.print("Enter the new name for the account (Press Enter to keep the current name): ");
            inputName = scanner.nextLine();

            // Check if the inputName is not empty and set newName accordingly
            if (!inputName.isEmpty())
            {
                newName = inputName;
            }

            // get new balance
            System.out.print("Enter the new balance for the account (Press Enter to keep the current balance): ");
            newBalanceString = scanner.nextLine();

            // if the new value isn't null get the new one
            if (!newBalanceString.isEmpty())
            {
                newBalance = Float.parseFloat(newBalanceString);
            }

            // Construct the SQL query
            String query = "update Accounts set ";

            if (newName != null)
            {
                query += "name = '" + newName + "', ";
            }

            if (newBalance == -1)
            {
                query += "balance = balance where id = '" + id + "'";

            }
            else
            {
                query += "balance = '" + newBalance + "' where id = '" + id + "'";
            }

            // Execute the query
            s.executeUpdate(query);

            System.out.println("Account updated successfully.");
            System.out.print("\nPress enter to continue: ");
            scanner.nextLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void removeAccount()
    {
        // variables
        Scanner scanner = new Scanner(System.in);
        int id;

        try
        {
            // make connection
            Connection connection = SQLConnection.open();
            Statement s = connection.createStatement();

            // get id
            System.out.println("\n-------------------------\n");
            System.out.println("You choose \"Show account\".\n");
            System.out.print("Enter the id of the account to modify: ");
            id = Integer.parseInt(scanner.nextLine());

            // remove account
            s.executeUpdate
            (
            "delete from Accounts where id = '" + id + "'"
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.print("\nPress enter to continue: ");
        scanner.nextLine();
    }

    public static void showAllAccounts()
    {
        // variables
        Scanner scanner = new Scanner(System.in);
        boolean foundRecords = false;

        try
        {
            // make connection
            Connection connection = SQLConnection.open();
            Statement s = connection.createStatement();

            // make query
            ResultSet rs = s.executeQuery("select * from Accounts");
            System.out.println("");

            while (rs.next())
            {
                foundRecords = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float balance = rs.getFloat("balance");
                System.out.println("id: " + id + ", name: " + name + ", balance: "+ balance);
            }

            if (!foundRecords)
            {
                System.out.println("No records found on accounts.");
            }

            System.out.print("\nPress enter to continue: ");
            scanner.nextLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}