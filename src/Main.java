import java.util.Scanner;

/*
this is the main class for our waterworks project
used to call all the functions
 */

public class Main {


    public static void main(String[] args) {

        createConsumer();
        boolean readingCardReceived = readingCardReturned();
        boolean verificationStatus = false;

        if (readingCardReceived) {
            verificationStatus = enterReadingCard();
        } else {
            //addReadingFee;
            //send staff member
        }
        if (verificationStatus) {
            //insert data
            //send consumer info and water consumption data
        }

    }


    public static boolean readingCardReturned() {
        Scanner in = new Scanner(System.in);
        boolean cardReturned = false, done = false;
        String token = "";
        System.out.println("Has the reading card been returned? Y/N");
        while (!done) {
            token = in.next();
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
        return cardReturned;
    }


    public static void createConsumer() {
        String consumer;

        String name;
        int type;
        String address;
        String telephoneNo;
        String email;
        String companyName;
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the consumer info");
        System.out.println("Name");
        name = in.nextLine();
        System.out.println("Address");
        address = in.nextLine();
        System.out.println("Consumer type");
        type = in.nextInt();

        /*
        For some reason the address does not work with the in.nextLine() function
        so we are using string concatenation. It was fixed when moved above the consumer type
        */

        System.out.println("Email");
        email = in.next();
        System.out.println("Telephone Number");
        telephoneNo = in.next();

        if (type != 1) {
            System.out.println("Company Name");
            companyName = in.nextLine();
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
    public static boolean enterReadingCard() {
        boolean isVerified = false;
        double WaterConsumption = 0;
        Scanner in = new Scanner(System.in);
        while (!isVerified) {
            System.out.println("Enter the water consumption data: ");
            System.out.println("Water Used");

            //Verify reading card

            if (in.hasNextDouble()) {
                WaterConsumption = in.nextDouble();
                System.out.printf("Water Used: %.3f", WaterConsumption);
                isVerified = true;
            } else {
                System.out.println("Wrong Data Type\nTry Again");
                isVerified = false;
                in.next();
            }
        }
        DB.insertSQL("Insert into tblReadingCard (Col3) values (" + WaterConsumption + ")");
        return isVerified;

    }

    //Push paid bills

    //Push unpaid bills

    //Send reading fee

    //Push Settlement Info
    public static void SettlementInfo() {



    }
}





//This is where the Calculate Settlements function will be: 2.2, 2.3, 2.4

//Pull water consumption from Database, which is in a float form, and then cast it to a double.
DB.selectSQL("SELECT fldWaterConsumption FROM tblReadingCards WHERE fldConsumerID=" + selectVariable + "");
double waterConsumption = DB.getDisplayData();

DB.selectSQL("SELECT fldDrainageWaterConsumption FROM tblReadingCards WHERE fldConsumerID=" + selectVariable + "");
double drainageWaterConsumption = DB.getDisplayData();

DB.selectSQL("SELECT fldWaterConsumption AND fldDrainageWaterConsumption FROM tblReadingCards WHERE fldConsumerID=" + selectVariable + "");
double totalWaterConsumption = DB.getDisplayData();

//Pull group-data from Database and assign it to a string.
DB.selectSQL("SELECT fldConsumerSegment FROM tblConsumer WHERE fldConsumerID=" + selectVariable + "");
String fldConsumerSegment = DB.getDisplayData();

//Calculate water cost based on consumption and a hardcoded rate. Settlements are then calculated based on which consumer segment they are a part of.
   double waterCost = 18 * waterConsumption

//Calculate water cost plus drainage water consumption, then tax based on customer segment
waterCost+=drainageWaterConsumption * 32

   If (fldConsumerSegment = "1")
   double settlementCalculated = waterCost * 1.25

   else if (fldConsumerSegment = "2")
   double settlementCalculated = waterCost * 1.12

   else if (fldConsumerSegment = "3")
   double settlementCalculated = waterCost * 1.19

//Insert newly calculated settlement data into SQL Database.
   DB.insertSQL("INSERT INTO tblBills(fldSettlement) VALUES('"+settlementCalculated +"')");





//This is where the Notify Consumer function will be
//Pull consumer and Settlement info for labels and invoice
DB.selectSQL("SELECT * FROM tblConsumer WHERE fldConsumerID=" + selectVariable + "");
String consumerInfo = DB.getDisplayData();

Double settlementInfo = DB.getDisplayData();

//Pull reminder counter from Database
String reminderCounter = DB.getDisplayData();

DB.insertSQL("INSERT INTO tblReminders VALUES ('reminderCounter');

//Set flat reminder fee + add reminder fee based on the counter
double reminderFee = 200
double totalReminderFee = reminderCounter * reminderFee

//(create giro fld??)

//Print invoice

//OR create reminder with labels, print the label after.



