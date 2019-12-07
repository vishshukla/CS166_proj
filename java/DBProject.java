/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of DBProject
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public DBProject (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end DBProject

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public ResultSet executeQuery (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);
      return rs;
   }//end executeQuery

   public int executeQuery2 (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);
      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
    if(outputHeader){
       for(int i = 1; i <= numCol; i++){
      System.out.print(rsmd.getColumnName(i) + "\t");
       }
       System.out.println();
       outputHeader = false;
    }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         // System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }

   
   public static void checkEmpty(String type,String str) throws Exception {
      if(str.length() == 0 || str == null) throw new Exception("Invalid input! " + type + " cannot be empty.");
   }

   public static void checkDigit(String type,String str) throws Exception {
      if(str.matches(".*\\d.*")) throw new Exception("Invalid input! " + type + " can not have any numbers.");
   }

   public static void checkAlpha(String type,String str) throws Exception {
      if(str.matches(".*[a-zA-Z]+.*")) throw new Exception("Invalid input! " + type + " can not have any alpha characters.");
   }

   public static void checkDate(String type, String str) throws Exception {
      String dateRegex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$"; 
        Pattern pattern = Pattern.compile(dateRegex); 
        Matcher checker = pattern.matcher((CharSequence)str);
        if(!checker.matches()) throw new Exception("Invalid input! " + type + " is not in correct date format (MM/DD/YYYY)");
   }

     public static void checkTorF(String type,String str) throws Exception {
      if(!str.equals("T") || !str.equals("F")) throw new Exception("Invalid input. " + type + " has to be T or F.");
   }



   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            DBProject.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      DBProject esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the DBProject object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new DBProject (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking"); 
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range"); 
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

            switch (readChoice()){
				   case 1: addCustomer(esql); break;
				   case 2: addRoom(esql); break;
				   case 3: addMaintenanceCompany(esql); break;
				   case 4: addRepair(esql); break;
				   case 5: bookRoom(esql); break;
				   case 6: assignHouseCleaningToRoom(esql); break;
				   case 7: repairRequest(esql); break;
				   case 8: numberOfAvailableRooms(esql); break;
				   case 9: numberOfBookedRooms(esql); break;
				   case 10: listHotelRoomBookingsForAWeek(esql); break;
				   case 11: topKHighestRoomPriceForADateRange(esql); break;
				   case 12: topKHighestPriceBookingsForACustomer(esql); break;
				   case 13: totalCostForCustomer(esql); break;
				   case 14: listRepairsMade(esql); break;
				   case 15: topKMaintenanceCompany(esql); break;
				   case 16: numberOfRepairsForEachRoomPerYear(esql); break;
				   case 17: keepon = false; break;
				   default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
   
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice
   
   public static void addCustomer(DBProject esql){
	  // Given customer details add the customer in the DB 

      try {
         String maxFind = "SELECT MAX(CustomerID) FROM CUSTOMER";
         ResultSet customerID = esql.executeQuery(maxFind);

         int nextID = 0;
         if(customerID.next()) {
            nextID = customerID.getInt(1) + 1;
         }

         System.out.println("Enter first name:");
         String fname = in.readLine();
         checkEmpty("First Name", fname);
         checkDigit("First Name", fname);

         System.out.println("Enter last name:");
         String lname = in.readLine();
         checkEmpty("Last Name", lname);
         checkDigit("Last Name", lname);

         System.out.println("Enter address:");
         String address = in.readLine();

         System.out.println("Enter phone number:");
         String phNo = in.readLine();
         checkAlpha("Phone Number",phNo);

         System.out.println("Enter DOB:");
         String dob = in.readLine();
         checkDate("Date of Birth", dob);

         System.out.println("Enter gender (Male,Female,Other):");
         String gender = in.readLine();
         
         String q = String.format("INSERT INTO CUSTOMER (customerID, fName, lName, Address, phNo, DOB, gender) VALUES('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s');",nextID,fname,lname,address,phNo,dob,gender);

         esql.executeUpdate(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end addCustomer

   public static void addRoom(DBProject esql){
	  // Given room details add the room in the DB
      // Your code goes here.
        try {
         System.out.println("Enter hotel ID:");
         String hotelID = in.readLine();
         checkEmpty("Hotel ID", hotelID);
         checkAlpha("Hotel ID", hotelID);

         System.out.println("Enter roomNo:");
         String roomNo = in.readLine();
         checkEmpty("Room Number", roomNo);
         checkAlpha("Room Number", roomNo);

         System.out.println("Enter roomType:");
         String roomType = in.readLine();
         checkEmpty("Room Type", roomType);
         
         String q = String.format("INSERT INTO ROOM (hotelID, roomNo, roomType) VALUES('%1$s','%2$s','%3$s');", hotelID, roomNo, roomType);

         esql.executeUpdate(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end addRoom

   public static void addMaintenanceCompany(DBProject esql){
      // Given maintenance Company details add the maintenance company in the DB
      try {
         String maxFind = "SELECT MAX(cmpID) FROM MaintenanceCompany";
         ResultSet cmpID = esql.executeQuery(maxFind);

         int nextID = 0;
         if(cmpID.next()) {
            nextID = cmpID.getInt(1) + 1;
         }

         System.out.println(nextID);

         System.out.println("Enter Maintenance Company name:");
         String name = in.readLine();
         checkEmpty("Maintenance Company", name);

         System.out.println("Enter address:");
         String address = in.readLine();

         System.out.println("Is the maintenance company certified? (T/F)");
         String certified = in.readLine();
         checkTorF("Certification", certified);
         checkEmpty("Ceritifcation", certified);
         
         String q = String.format("INSERT INTO MaintenanceCompany (cmpID, name, address, isCertified) VALUES('%1$s','%2$s','%3$s','%4$s');",nextID,name,address,certified);

         esql.executeUpdate(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){
	  // Given repair details add repair in the DB
      try {
         String maxFind = "SELECT MAX(rID) FROM Repair";
         ResultSet rID = esql.executeQuery(maxFind);

         int nextID = 0;
         if(rID.next()) {
            nextID = rID.getInt(1) + 1;
         }

         System.out.println("Enter HotelID:");
         String hotelID = in.readLine();
         checkEmpty("Hotel ID", hotelID);
         checkAlpha("Hotel ID", hotelID);

         System.out.println("Enter RoomNo:");
         String roomNo = in.readLine();
         checkEmpty("Room No", roomNo);
         checkAlpha("Room No", roomNo);

         System.out.println("Enter Maintenance Company ID:");
         String mCompany = in.readLine();
         checkEmpty("Maintenance Company ID", roomNo);
         checkAlpha("Maintenance Company ID", roomNo);

         System.out.println("Enter repairDate:");
         String repairDate = in.readLine();
         checkDate("Repair Date", repairDate);

         System.out.println("Enter description:");
         String description = in.readLine();

         System.out.println("Enter repairType:");
         String repairType = in.readLine();
         
         String q = String.format("INSERT INTO Repair (rID, hotelID, roomNo, mCompany, repairDate, description, repairType) VALUES('%1$s','%2$s','%3$s','%4$s', '%5$s', '%6$s', '%7$s');",nextID,hotelID,roomNo,mCompany,repairDate,description,repairType);

         esql.executeUpdate(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end addRepair

   public static void bookRoom(DBProject esql){
	  // Given hotelID, roomNo and customer Name create a booking in the DB 
      try {
         String maxFind = "SELECT MAX(bID) FROM Booking";
         ResultSet bID = esql.executeQuery(maxFind);

         int nextID = 0;
         if(bID.next()) {
            nextID = bID.getInt(1) + 1;
         }

         System.out.println("Enter Customer ID:");
         String customer = in.readLine();
         checkEmpty("Customer ID", customer);
         checkAlpha("Customer ID", customer);

         System.out.println("Enter hotel ID:");
         String hotelID = in.readLine();
         checkEmpty("Hotel ID", hotelID);
         checkAlpha("Hotel ID", hotelID);

         System.out.println("Enter RoomNo to book:");
         String roomNo = in.readLine();
         checkEmpty("Room No", roomNo);
         checkAlpha("Room No", roomNo);

         System.out.println("Enter booking date:");
         String bookingDate = in.readLine();
         checkDate("Booking Date", bookingDate);
         checkEmpty("Booking Date", bookingDate);

         System.out.println("Enter number of people:");
         String noOfPeople = in.readLine();
         checkAlpha("No of people", noOfPeople);

         System.out.println("Enter price:");
         String price = in.readLine();
         checkAlpha("Price", price);
         checkEmpty("Price", price);
         
         String q = String.format("INSERT INTO Booking (bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price) VALUES('%1$s','%2$s','%3$s','%4$s', '%5$s', '%6$s', '%7$s');",nextID,customer,hotelID,roomNo,bookingDate,noOfPeople,price);

         esql.executeUpdate(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){
	  // Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      try {
         String maxFind = "SELECT MAX(asgID) FROM Assigned";
         ResultSet asgID = esql.executeQuery(maxFind);

         int nextID = 0;
         if(asgID.next()) {
            nextID = asgID.getInt(1) + 1;
         }

         System.out.println(nextID);

         System.out.println("Enter staff ID:");
         String staffID = in.readLine();
         checkEmpty("Staff ID", staffID);
         checkAlpha("Staff ID", staffID);

         System.out.println("Enter hotel ID:");
         String hotelID = in.readLine();
         checkEmpty("Hotel ID", hotelID);
         checkAlpha("Hotel ID", hotelID);

         System.out.println("Enter RoomNo to assign:");
         String roomNo = in.readLine();
         checkEmpty("Room No", roomNo);
         checkAlpha("Toom No", roomNo);

         String q = String.format("INSERT INTO Assigned (asgID, staffID, hotelID, roomNo) VALUES('%1$s','%2$s','%3$s','%4$s');",nextID,staffID,hotelID,roomNo);

         esql.executeUpdate(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
      try {
         String maxFind = "SELECT MAX(reqID) FROM Request";
         ResultSet reqID = esql.executeQuery(maxFind);

         int nextID = 0;
         if(reqID.next()) {
            nextID = reqID.getInt(1) + 1;
         }

         System.out.println(nextID);

         System.out.println("Enter hotel ID:");
         String hotelID = in.readLine();
         checkEmpty("Hotel ID", hotelID);
         checkAlpha("Hotel ID", hotelID);

         System.out.println("Enter staff ID:");
         String staffID = in.readLine();
         checkEmpty("Staff ID", staffID);
         checkAlpha("Staff ID", staffID);

         System.out.println("Enter room No:");
         String roomNo = in.readLine();
         checkEmpty("Room No", roomNo);
         checkAlpha("Room No", roomNo);

         System.out.println("Enter repair ID:");
         String repairID = in.readLine();
         checkEmpty("Repair ID", repairID);
         checkAlpha("Repair ID", repairID);

         System.out.println("Enter request date:");
         String requestDate = in.readLine();
         checkDate("Request Date", requestDate);

         System.out.println("Enter a description:");
         String description = in.readLine();

         String q = String.format("INSERT INTO Request (reqID, managerID, repairID,requestDate,description) VALUES('%1$s','%2$s','%3$s','%4$s','%5$s');",nextID,staffID,repairID,requestDate,description);

         esql.executeUpdate(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms available 
      try {
         System.out.println("Enter hotelID:");
         String hid = in.readLine();
         checkEmpty("Hotel ID", hid);
         checkAlpha("Hotel ID", hid);

         String q = "SELECT Count(R.roomNo) FROM Room R WHERE R.roomNo IN ( SELECT R.roomNo FROM Room R WHERE R.HotelID = ";
         q += hid;
         q += " ) AND R.roomNo NOT IN ( SELECT R.roomNo FROM Booking B, Room R WHERE B.roomNo = R.roomNo AND B.hotelID = ";
         q += hid;
         q += " );";

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms booked
      try {
         System.out.println("Enter hotelID:");
         String hid = in.readLine();
         checkEmpty("Hotel ID", hid);
         checkAlpha("Hotel ID", hid);
         
         String q = "SELECT Count(*) FROM Booking B WHERE B.HotelID = ";
         q += hid + ";";

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      try {
         System.out.println("Enter hotelID:");
         String hid = in.readLine();
         checkEmpty("Hotel ID", hid);
         checkAlpha("Hotel ID", hid);

         System.out.println("Enter Date (in MM/DD/YYYY):");
         String date = in.readLine();
         checkDate("Date", date);
         date = date.replace("/","");

         String q = "SELECT B.roomNo, B.bookingDate FROM Booking B WHERE B.HotelID = ";
         q += hid;
         q += " AND B.bookingDate BETWEEN TO_DATE(\' ";
         q += date;
         q += " \', 'MMDDYYYY') AND TO_DATE(\' ";
         q += date;
         q += " \', 'MMDDYYYY') + 7";

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
	  // List Top K Rooms with the highest price for a given date range
      try {
         System.out.println("Enter Starting Date:");
         String ds = in.readLine();
         checkDate("Start date",ds);
         ds = ds.replace("/","");

         System.out.println("Enter Ending Date:");
         String de = in.readLine();
         checkDate("End date",de);
         de = de.replace("/","");
         
         System.out.println("Enter number of rooms to return:");
         String rmno = in.readLine();
         checkEmpty("Rooms to return", rmno);
         checkAlpha("Rooms to return", rmno);

         String q = "SELECT DISTINCT B.hotelID, R.roomNo, R.roomType, B.price FROM Booking B, Room R WHERE B.roomNo = R.roomNo AND B.bookingDate BETWEEN TO_DATE(\' ";
         q += ds;
         q += "\','MMDDYYYY') AND TO_DATE(\' ";
         q += de;
         q += "\','MMDDYYYY') ORDER BY B.price DESC LIMIT ";
         q += rmno;
         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer Name, List Top K highest booking price for a customer 
      // Your code goes here.
      try {
         System.out.println("Enter Customer first Name:");
         String cfn = in.readLine();
         checkEmpty("First Name", cfn);
         checkDigit("First Name", cfn);
         
         System.out.println("Enter Customer last Name:");
         String cln = in.readLine();
         checkEmpty("Last Name", cln);
         checkDigit("Last Name", cln);

         System.out.println("Enter number of bookings:");
         String b = in.readLine();
         checkEmpty("No. of Bookings", b);
         checkAlpha("No. of Bookings", b);

         String q = "SELECT B.price FROM Booking B, Customer C WHERE B.customer = C.customerID AND C.fname =  \'";
         q += cfn;
         q += "\' AND C.lname = \'";
         q += cln;
         q += "\' ORDER BY B.price DESC LIMIT ";
         q += b;

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      try {
         System.out.println("Enter Hotel ID:");
         String hid = in.readLine();
         checkEmpty("Hotel ID", hid);
         checkAlpha("Hotel ID", hid);
         
         System.out.println("Enter Customer first Name:");
         String cfn = in.readLine();
         checkEmpty("Customer First Name", cfn);
         checkDigit("Customer First Name", cfn);

         System.out.println("Enter Customer last Name:");
         String cln = in.readLine();
         checkEmpty("Customer Last Name", cln);
         checkDigit("Customer Last Name", cln);

         System.out.println("Enter starting date:");
         String ds = in.readLine();
         checkDate("Starting Date", ds);
         ds = ds.replace("/","");

         System.out.println("Enter ending date:");
         String de = in.readLine();
         checkDate("Ending Date", de);
         de = de.replace("/","");

         String q = "SELECT SUM(B.price) FROM Booking B, Customer C WHERE B.hotelID = \'";
         q += hid;
         q += "\' AND B.customer = C.customerID AND C.fname = \'";
         q += cfn;
         q += "\' AND C.lname = \'";
         q += cln;
         q += "\' AND B.bookingDate BETWEEN TO_DATE(\'";
         q += ds;
         q += "\','MMDDYYYY') AND TO_DATE(\' ";
         q += de;
         q += "\','MMDDYYYY') ";

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      // Your code goes here.
      try {
         System.out.println("Enter Maintainance Company Name:");
         String mn = in.readLine();
         checkEmpty("Maintainance Company",mn);

         String q = String.format("SELECT R.rID, R.repairType, R.hotelID, R.roomNo FROM Repair R, MaintenanceCompany M WHERE M.name = '%s' AND M.cmpID = R.mCompany", mn);

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      try {
         System.out.println("Enter Number of Maintainance Companies to return:");
         String mn = in.readLine();
         checkEmpty("Number", mn);
         checkAlpha("Number", mn);

         String q = String.format("SELECT M.name, Count(M.name) FROM MaintenanceCompany M, Repair R WHERE M.cmpID = R.mCompany GROUP BY M.name ORDER BY Count(M.name) DESC Limit %s",mn);

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      try {
         System.out.println("Enter Hotel ID:");
         String hid = in.readLine();
         checkEmpty("Number", hid);
         checkAlpha("Number", hid);

         System.out.println("Enter Room No:");
         String rmno = in.readLine();
         checkEmpty("Number", rmno);
         checkAlpha("Number", rmno);

         String q = "SELECT EXTRACT (YEAR FROM R.repairDate), Count(R.repairType) FROM Repair R WHERE R.roomNo = ";
         q += rmno;
         q += " AND R.hotelID = ";
         q += hid;
         q += " GROUP BY EXTRACT(YEAR FROM R.repairDate) ORDER BY EXTRACT (YEAR FROM R.repairDate) DESC";

         esql.executeQuery2(q);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end listRepairsMade

}//end DBProject