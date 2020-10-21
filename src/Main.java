import java.util.Scanner;

/*
this is the main class for our waterworks project
used to call all the functions
 */

public class Main {
    public static void main(String[] args) {

        createConsumer();
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
        System.out.println("Consumer type");
        type = in.nextInt();

        /*
        For some reason the address does not work with the in.nextLine() function
        so we are using string concatenation
        */
        System.out.println("Road Name");
        address = in.next();
        System.out.println("House Number");
        address += " " + in.next();
        System.out.println("Email");
        email = in.next();
        System.out.println("Telephone Number");
        telephoneNo = in.next();

        if (type != 1) {
            System.out.println("Company Name");
            companyName = in.next();
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email + "\nCompany Name: " + companyName;
        } else {
            consumer = "Name: " + name + "\nAddress: " + address + "\nTelephone Number: " + telephoneNo + "\nEmail: " + email;
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
    public static void enterReadingCard(){
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the water consumption data: ");
        double WaterConsumption = in.nextDouble();


    //Verify reading card
    //public static void verifyReadingCard() {



        //Push paid bills

        //Push unpaid bills

        //Send reading fee

        //Push Settlement Info
    }
}



/*
This is where the Calculate Settlements function will be
 */


/*
This is where the Notify Consumer function will be
 */
