import java.util.Scanner;

/*
this is the main class for our waterworks project
used to call all the functions
 */

public class Main {


    public static void main(String[] args) {
        //declarations
        Scanner in = new Scanner(System.in);
        boolean menuDone = false;

        //User menu for administrating the Waterworks Administration System
        String menu = "User menu\n" +
                "1: create a new consumer\n" +
                "2: enter reading card information\n" +
                "3: calculate settlements\n" +
                "4: update consumer information\n";

        do {
            System.out.print(menu);

            if (in.hasNextInt()) {
                int menuOption = in.nextInt();

                switch (menuOption) {
                    case 1:
                        createConsumer(in);
                        break;
                    case 2:
                        enterReadingCard(in);
                        break;
                    case 3:
                        settlementCalc(in);
                        break;
                    case 4:

                    case 5:
                        menuDone = true;
                        break;
                }
            }
        } while (!menuDone);

    }


    public static boolean readingCardReturned(Scanner scanner) {
        boolean cardReturned = false, done = false;
        String token = "";
        System.out.println("Has the reading card been returned? Y/N");
        while (!done) {
            token = scanner.next();
            if (token.toUpperCase().equals("Y")) {
                cardReturned = true;
                done = true;
            } else if (token.toUpperCase().equals("N")) {
                cardReturned = false;
                done = true;
            } else {
                System.out.println("Please enter only Y or N");
            }
        }
        if (!cardReturned) {
            DB.insertSQL("insert into tblReadingCard(fldIsReturned)values(1)");
        } else {
            DB.insertSQL("insert into tblReadingCard(fldIsReturned)values(0)");
        }
        return cardReturned;
    }


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
        System.out.println("Consumer type");
        type = scanner.nextInt();

        /*
        For some reason the address does not work with the in.nextLine() function
        so we are using string concatenation. It was fixed when moved above the consumer type
        */

        System.out.println("Email");
        email = scanner.next();
        System.out.println("Telephone Number");
        telephoneNo = scanner.next();

        if (type != 1) {
            System.out.println("Company Name");
            companyName = scanner.nextLine();
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email + "\nCompany Name: " + companyName;
            DB.insertSQL("insert into tblConsumer(fldConsumerSegment, fldName, fldAddress, fldPhoneNo, fldEmail, fldCompanyname) values(" +
                    "'" + type + "', '" + name + "', '" + address + "', '" + telephoneNo + "', '" + email + "', '" + companyName + "')");
        } else {
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email;
            DB.insertSQL("insert into tblConsumer(fldConsumerSegment, fldName, fldAddress, fldPhoneNo, fldEmail) values(" +
                    "'" + type + "', '" + name + "', '" + address + "', '" + telephoneNo + "', '" + email + "')");
        }
        System.out.println(consumer);


    }


    /*
    This is where the Update Consumer Database function will be
     */
    //public static void updateConDatabase() {


    /*
    Receive reading card
    A reading card is entered into the system
     */
    public static boolean enterReadingCard(Scanner scanner) {
        boolean isVerified = false;
        double waterConsumption = 0, drainageWaterConsumption = 0, totalWaterConsumption = 0;
        int conID = 0;
        System.out.println("Enter ConsumerID");
        conID = scanner.nextInt();
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
                if(waterConsumption!=0 && drainageWaterConsumption!=0){
                    isVerified = true;
                }
            }
            else {
                System.out.println("Incorrect Entry\nTry Again");
                isVerified = false;
                scanner.next();
            }

        }
        //insert Water consumption data into the system
        DB.insertSQL("Insert tblReadingCard(fldConsumerID, fldWaterConsumption, fldDrainageWaterConsumption, fldTotalWaterConsumption)" +
                " values(" + conID + "," + waterConsumption + "," + drainageWaterConsumption + "," + totalWaterConsumption + ")");
        return isVerified;

    }

    //Check whether the consumer has paid the bill and push this information into the database
    public static void pushBills() {
        Scanner in = new Scanner(System.in);
        boolean billPaid = false, done = false;
        String token;
        System.out.println("Has the consumer paid the bill? Y/N");
        while (!done) {
            token = in.next();
            if (token.toUpperCase().equals("Y")) {
                billPaid = true;
                done = true;
            } else if (token.toUpperCase().equals("N")) {
                billPaid = false;
                done = true;
            } else {
                System.out.println("Please enter only Y or N");
            }
        }

        if (billPaid) {
            DB.insertSQL("insert into tblBills(fldBillPaid) values(0)");
        } else {
            DB.insertSQL("insert into tblBills(fldBillPaid) values(1)");
        }
    }


    //Send reading fee


    //Push Settlement Info


//This is where the Calculate Settlements function will be: 2.2, 2.3, 2.4

//Pull water consumption from Database, which is in a float form, and then cast it to a double.

    public static void settlementCalc(Scanner scanner) {

        //All local variables used
        int consumerID, readingCardID , fldConsumerSegment;
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

//        String temp;
//        while (!(temp = DB.getData()).equals(DB.NOMOREDATA) ){
//            totalWaterConsumption += Double.parseDouble(temp);
//       }


        //Pull group-data from Database and assign it to a string.
        /*
        Calculate water cost based on consumption and a hardcoded rate.
        Settlements are then calculated based on which consumer segment they are a part of.
        */
        waterCost = 18 * waterConsumption;

        //Calculate water cost plus drainage water consumption, then tax based on customer segment
        waterCost += drainageWaterConsumption * 32;

        switch(fldConsumerSegment){
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
/*
This is where the Notify Consumer function will be
    public static void notifyConsumer(Scanner scanner){
        int consumerID;
        System.out.println("Input ConsumerID");
        consumerID = scanner.nextInt();
//Pull consumer and Settlement info for labels and invoice
        DB.selectSQL("SELECT * FROM tblConsumer WHERE fldConsumerID=" + consumerID +"");
        String consumerInfo = DB.getDisplayData();
        String settlementInfo = DB.getDisplayData();
//Pull reminder counter from Database
        DB.selectSQL("SELECT ")
        int reminderCounter = DB.getDisplayData();
        DB.insertSQL("INSERT INTO tblReminders VALUES ('reminderCounter');
//Set flat reminder fee + add reminder fee based on the counter
        double reminderFee = 200;
        double totalReminderFee = reminderCounter * reminderFee;
//(create giro fld??)
//Print invoice
//OR create reminder with labels, print the label after.
    }
 */
    public static void clearData(){
        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                System.out.print(data);
            }
        } while(true);
    }
}