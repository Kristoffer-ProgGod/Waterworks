import java.util.Scanner;

/*
this is the main class for our waterworks project
used to call all the functions
@authors Monir, Niclas, Mai, Kristoffer
@version 1.0
@since 21/10/2020
 */

public class Main {

    /**
     * Our main method which contains the menu used to select functions
     * @param args idk
     */

    public static void main(String[] args) {
        //declarations
        Scanner in = new Scanner(System.in);
        boolean menuDone = false;
        boolean cardReturned;

        //User menu for administrating the Waterworks Administration System
        String menu = "User menu\n" +
                "1: create a new consumer\n" +
                "2: enter reading card information\n" +
                "3: calculate settlements\n" +
                "4: update consumer information\n" +
                "5: get statistics\n" +
                "6: bills paid\n" +
                "7: notify consumer\n" +
                "8: exit program\n";

        do {
            System.out.print(menu);

            if (in.hasNextInt()) {
                int menuOption = in.nextInt();

                switch (menuOption) {
                    case 1:
                        createConsumer(in);
                        break;
                    case 2:
                        cardReturned = readingCardReturned(in);
                        if(cardReturned) {
                            enterReadingCard(in);
                        }
                        break;
                    case 3:
                        settlementCalc(in);
                        break;
                    case 4:
                        updateConsumer(in);
                        break;
                    case 5:
                        statistics(in);
                        break;
                    case 6:
                        billsPaid(in);
                        break;
                    case 7:
                        notifyConsumer(in);
                        break;
                    case 8:
                        menuDone = true;
                        break;
                }
            }
        } while (!menuDone);

    }

    /**
     * sees if a reading card has been filled out and adds appropriate reading fees in the database if not
     * @param scanner takes input for the various variables and strings
     * @return whether or not the reading card has been returned to the company
     */
    public static boolean readingCardReturned(Scanner scanner) {
        boolean cardReturned = false, done = false;
        int consumerID;
        String token = "";
        System.out.println("Input Consumer ID");
        consumerID = scanner.nextInt();

        System.out.println("Has the reading card been returned? Y/N");
        while(!done){
            token = scanner.next();
            if (token.toUpperCase().equals("Y")) {
                cardReturned = true;
                done = true;
            } else if (token.toUpperCase().equals("N")) {
                done = true;
            } else {
                System.out.println("Please enter only Y or N");
            }
        }
        if (!cardReturned) {
            DB.insertSQL("insert into tblReadingCard(fldIsReturned, fldConsumerID) values(0," + consumerID +" )");
            DB.updateSQL("Update tblBills set fldReadingFees = " + 200 + "where fldConsumerID = " + consumerID);
        } else {
            DB.insertSQL("insert into tblReadingCard(fldIsReturned)values(1)");
        }
        return cardReturned;
    }

    /**
     * used to create a new consumer/customer in the database
     * @param scanner takes input
     */
    public static void createConsumer(Scanner scanner) {
        String consumer;

        String name;
        int type;
        String address;
        String telephoneNo;
        String email;
        String companyName;
        scanner.nextLine();
        System.out.println("Please enter the consumer info");
        System.out.println("Name");
        name = scanner.nextLine();
        System.out.println("Address");
        address = scanner.nextLine();
        System.out.println("Consumer type\n1: Private\n2: Agriculture\n3: Industrial");
        type = scanner.nextInt();

        /*
        For some reason the address does not work with the in.nextLine() function
        so we are using string concatenation. It was fixed when moved above the consumer type
        */

        System.out.println("Email");
        email = scanner.next();
        System.out.println("Telephone Number");
        telephoneNo = scanner.next();


        scanner.nextLine();


        if (type != 1) {
            System.out.println("Company Name");
            companyName = scanner.nextLine();
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email + "\nCompany Name: " + companyName;
            DB.insertSQL("insert into tblConsumer(fldConsumerSegment, fldName, fldAddress, fldPhoneNo, fldEmail, fldCompanyName) values(" +
                    "'" + type + "', '" + name + "', '" + address + "', '" + telephoneNo + "', '" + email + "', '" + companyName + "')");
        } else {
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email;
            DB.insertSQL("insert into tblConsumer(fldConsumerSegment, fldName, fldAddress, fldPhoneNo, fldEmail) values(" +
                    "'" + type + "', '" + name + "', '" + address + "', '" + telephoneNo + "', '" + email + "')");
        }
        System.out.println(consumer);


    }

    /**
     * used to enter the reading card data into the database
     * how much of each type of water was used
     * input in cubic meters
     * @param scanner takes input
     */
    public static void enterReadingCard(Scanner scanner) {
        boolean isVerified = false;
        double waterConsumption = 0, drainageWaterConsumption = 0, totalWaterConsumption = 0;
        int conID = 0, segment = 0;
        System.out.println("Enter ConsumerID");
        conID = scanner.nextInt();
        DB.selectSQL("Select fldConsumerSegment from tblConsumer where fldConsumerID=" + conID);
        segment = Integer.parseInt(DB.getData());
        clearData();

        while (!isVerified) {
            System.out.println("Enter the water consumption data: ");
            System.out.println("Water Used");

            //Verify reading card
            if (scanner.hasNextDouble()) {
                waterConsumption = scanner.nextDouble();
                System.out.printf("Water Used: %.3f\n", waterConsumption);
                System.out.println("Drainage Water Used");
                drainageWaterConsumption = scanner.nextDouble();
                System.out.printf("Drainage Water Used: %.3f\n", drainageWaterConsumption);
                totalWaterConsumption = waterConsumption + drainageWaterConsumption;
                if (waterConsumption != 0 && drainageWaterConsumption != 0) {
                    isVerified = true;
                }
            } else {
                System.out.println("Incorrect Entry\nTry Again");
                isVerified = false;
                scanner.next();
            }

        }
        //insert Water consumption data into the system
        DB.insertSQL("Insert tblReadingCard(fldConsumerID, fldWaterConsumption, fldDrainageWaterConsumption, fldTotalWaterConsumption, fldConsumerSegment)" +
                " values(" + conID + "," + waterConsumption + "," + drainageWaterConsumption + "," + totalWaterConsumption + "," + segment + ")");
    }


    /**
     * sets the payment status of a bill for a single consumer and sets reminder fees
     * @param scanner takes input
     */
    public static void billsPaid(Scanner scanner) {
        boolean billPaid = false, done = false;
        int consumerID, billID;
        String token;
        System.out.println("Insert ConsumerID");
        consumerID = scanner.nextInt();
        System.out.println("Insert BillID");
        billID = scanner.nextInt();
        System.out.println("Has the consumer paid the bill? Y/N");
        while (!done) {
            token = scanner.next();
            if (token.toUpperCase().equals("Y")) {
                billPaid = true;
                done = true;
            } else if (token.toUpperCase().equals("N")) {
                done = true;
            } else {
                System.out.println("Please enter only Y or N");
            }
        }

        if (billPaid) {
            DB.updateSQL("Update tblBills set fldBillPaid = " + 1 + "where fldConsumerID = " + consumerID  + " and fldBillID = " + billID);
        } else {
            DB.updateSQL("Update tblBills set fldBillPaid = " + 0 + "where fldConsumerID = " + consumerID  + " and fldBillID = " + billID);
            DB.updateSQL("Update tblBills set fldReminderFees = " + 200 + "where fldConsumerID = " + consumerID  + " and fldBillID = " + billID);
            DB.updateSQL("Update tblBills set fldReminderCounter = " + 1 + "where fldConsumerID = " + consumerID  + " and fldBillID = " + billID);
            DB.insertSQL("Insert into tblReminders(fldConsumerID, fldReminderCounter) values(" + consumerID + ", " + 0 + ")");

        }
    }


    /**
     * calculates how much a consumer will have to pay for their water
     * based on which kind of consumer and which kind of water was consumed
     * and adds the bill to the database
     * @param scanner takes input yet again
     */
    public static void settlementCalc(Scanner scanner) {

        //All local variables used
        int consumerID, readingCardID, fldConsumerSegment;
        double waterConsumption, drainageWaterConsumption, totalWaterConsumption, waterCost, settlementCalculated = 0;
        System.out.println("Input Consumer ID");
        consumerID = scanner.nextInt();
        System.out.println("Input Reading Card ID");
        readingCardID = scanner.nextInt();


        DB.selectSQL("SELECT fldConsumerSegment FROM tblConsumer WHERE fldConsumerID=" + consumerID);


        String segment = DB.getData();

        fldConsumerSegment = Integer.parseInt(segment);
        System.out.println(fldConsumerSegment);

        DB.selectSQL("SELECT fldWaterConsumption FROM tblReadingCard WHERE fldConsumerID=" + consumerID + " and fldReadingCardID =" + readingCardID);
        waterConsumption = Double.parseDouble(DB.getData());

        DB.selectSQL("SELECT fldDrainageWaterConsumption FROM tblReadingCard WHERE fldConsumerID=" + consumerID + " and fldReadingCardID =" + readingCardID);
        drainageWaterConsumption = Double.parseDouble(DB.getData());

        //Use for statistics
        DB.selectSQL("SELECT fldTotalWaterConsumption FROM tblReadingCard WHERE fldConsumerID=" + consumerID + " and fldReadingCardID =" + readingCardID);
        totalWaterConsumption = Double.parseDouble(DB.getData());
        clearData();

       /*
        Calculate water cost based on consumption and a hardcoded rate.
        Settlements are then calculated based on which consumer segment they are a part of.
        */
        waterCost = 18 * waterConsumption;

        //Calculate water cost plus drainage water consumption, then tax based on consumer segment
        waterCost += drainageWaterConsumption * 32;

        switch (fldConsumerSegment) {
            case 1:
                settlementCalculated = waterCost * 1.25;
                break;
            case 2:
                settlementCalculated = waterCost * 1.12;
                break;
            case 3:
                settlementCalculated = waterCost * 1.19;
        }
        //Insert newly calculated settlement data into SQL Database.


        DB.insertSQL("INSERT INTO tblBills(fldSettlements, fldConsumerID) VALUES(" + settlementCalculated + "," + consumerID + ")");


    }

    /**
     * updates database with reminder fees if the bills have not been paid
     * and updates the reminder counter to keep track of how many have been sent
     *
     * For some reason when run we get the error code "Invalid column name 'fldReminderID'."
     * But the method still updates the reminder counter as intended and keeps running.
     *
     * @param scanner takes input
     */
    public static void notifyConsumer(Scanner scanner) {
        int consumerID, reminderCounter, reminderID;
        System.out.println("Input ConsumerID");
        consumerID = scanner.nextInt();
        System.out.println("Input ReminderID");
        reminderID = scanner.nextInt();


        //Pull consumer and Settlement info for labels and invoice
        DB.selectSQL("SELECT fldReminderCounter FROM tblReminders WHERE fldConsumerID=" + consumerID + " and fldReminderID= " + reminderID);
        reminderCounter = Integer.parseInt(DB.getData());
        clearData();

        //Pull reminder counter from Database
        DB.updateSQL("Update tblReminders set fldReminderCounter = " + (reminderCounter + 1) + " where fldConsumerID= " + consumerID  +
                " and fldReminderID= " + reminderID);
        //Set flat reminder fee + add reminder fee based on the counter
        double reminderFee = 200;
        double totalReminderFee = (reminderCounter +1) * reminderFee;
        DB.updateSQL("Update tblBills set fldReminderFees = " + totalReminderFee + " where fldConsumerID= " + consumerID +
                " and fldReminderID= " + reminderID);
    }

    /**
     * Tommy's do while loop put in a method so we can use it through out the code and easily clear
     * the DB.Select statements
     */
    public static void clearData() {
        do {
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)) {
                break;
            } else {
                System.out.print(data);
            }
        } while (true);
    }

    /**
     * prints average, max, min, and total consumption for each kind of water for a single consumer segment
     * also prints how many consumers are in a given segment
     * @param scanner takes input
     */
    public static void statistics(Scanner scanner) {

        System.out.println("Input Segment");
        int segment = scanner.nextInt();
        String average, minimum, maximum, sum, count;


        DB.selectSQL("Select AVG(fldWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        average = "Average for \nWater Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select AVG(fldDrainageWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        average += "Drainage Water Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select AVG(fldTotalWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        average += "Total Water Consumption: " + DB.getDisplayData();

        System.out.println(average);

        DB.selectSQL("Select SUM(fldWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        sum = "Sum for\nWater Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select SUM(fldDrainageWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        sum += "Drainage Water Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select SUM(fldTotalWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        sum += "Total Water Consumption: " + DB.getDisplayData();

        System.out.println(sum);

        DB.selectSQL("Select Count(fldConsumerID) from tblConsumer where fldConsumerSegment= " + segment);
        count = "Total Consumers in Segment: " + DB.getDisplayData();
        System.out.println(count);


        DB.selectSQL("Select MIN(fldWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        minimum = "Minimum for\nWater Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select MIN(fldDrainageWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        minimum += "Drainage Water Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select MIN(fldTotalWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        minimum += "Total Water Consumption: " + DB.getDisplayData();

        System.out.println(minimum);

        DB.selectSQL("Select MAX(fldWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        maximum = "Maximum for\nWater Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select MAX(fldDrainageWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        maximum += "Drainage Water Consumption: " + DB.getDisplayData();
        DB.selectSQL("Select MAX(fldTotalWaterConsumption) from tblReadingCard where fldConsumerSegment = " + segment);
        maximum += "Total Water Consumption: " + DB.getDisplayData();

        System.out.println(maximum);


    }

    /**
     * used to update a given consumers info using a consumerID
     * @param scanner takes input
     */
    public static void updateConsumer(Scanner scanner) {
        String consumer;
        int consumerID;
        System.out.println("Input ConsumerID");
        consumerID = scanner.nextInt();

        DB.selectSQL("Select fldConsumerSegment from tblConsumer where fldConsumerID = " + consumerID);
        int type = Integer.parseInt(DB.getData());
        clearData();
        String name;
        String address;
        String telephoneNo;
        String email;
        String companyName;
        scanner.nextLine();
        System.out.println("Please enter the updated consumer info");
        System.out.println("Name");
        name = scanner.nextLine();
        System.out.println("Address");
        address = scanner.nextLine();

        /*
        For some reason the address does not work with the in.nextLine() function
        so we are using string concatenation. It was fixed when moved above the consumer type
        */

        System.out.println("Email");
        email = scanner.next();
        System.out.println("Telephone Number");
        telephoneNo = scanner.next();

        scanner.nextLine();

        if (type != 1) {
            System.out.println("Company Name");
            companyName = scanner.nextLine();
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email + "\nCompany Name: " + companyName;
            DB.updateSQL("Update tblConsumer set fldName = '" + name + "', fldAddress = '" + address +"',"+
                    " fldPhoneNo = '" + telephoneNo + "'," + " fldEmail = '" + email+ "', fldCompanyName = '" + companyName+ "' where fldConsumerID = " + consumerID);
        } else {
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email;
            DB.updateSQL("Update tblConsumer set fldName = '" + name + "', fldAddress = '" + address+ "',"
                    + " fldPhoneNo = '"+ telephoneNo+ "', fldEmail = '" +email+ "' where fldConsumerID = " + consumerID);
        }
        System.out.println(consumer);


    }
}
