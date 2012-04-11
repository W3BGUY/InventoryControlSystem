/*
 * @author Charles "w3bguy" Bastian
 * Class for all MySQL functions.
 * Part of the Inventory Tracking System, created for use at Hill College.
 * Initial Creation: 2011.11.21
 * Licensed to Hill College while I am employed there.
 */

package inventorytrackingsystem;

/* Import all the SQL stuff. */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.net.*;
import java.util.*;

public class mySqlStuff{
  private Connection mySqlConnect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
  private ResultSet checkItemNum = null;
  private String uName=null;
  private String pWord=null;
  private String databaseName="hill_inventory";
  private String tableName="inventory";

  EncryptUtils enc=new EncryptUtils();
  private Connection establishConnection() throws Exception{
    getSqlLoginInfo();
    Connection connect = null;
    try{
      // This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      connect = DriverManager.getConnection("jdbc:mysql://172.16.1.74/hill_inventory?user="+enc.base64decode(uName)+"&password="+enc.base64decode(pWord));
    }catch(Exception ex){
      ex.printStackTrace();
    }
    return connect;
  }

  private void getSqlLoginInfo(){
    try{
      File file = new File("Z:\\Home\\ITS\\sqlStuff.its");
      BufferedReader reader=new BufferedReader(new FileReader(file));
      String line=null;
      String[] lineArray=null;
      lineArray = new String[3];
      int i=0;
      while((line=reader.readLine())!=null){
        //System.out.println(i+" -> "+line);
        lineArray[i]=line;
        i++;
      }
      uName=lineArray[1];
      pWord=lineArray[2];
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  private String[] getAllInventoryData(){
    String[] inventoryArray = new String[33];
    try{
      mySqlConnect=establishConnection();
      statement = mySqlConnect.createStatement();
      resultSet = statement.executeQuery("select * from "+databaseName+"."+tableName);

      while (resultSet.next()) {
        /* It is possible to get the columns via name also possible to get the
         * columns via the column number which starts at 1
         * e.g. resultSet.getSTring(2);
         */

        inventoryArray[0] = resultSet.getString("itemID");
        inventoryArray[1] = resultSet.getString("location");
        inventoryArray[2] = resultSet.getString("building");
        inventoryArray[3] = resultSet.getString("room");
        inventoryArray[4] = resultSet.getString("dateAcquired");
        inventoryArray[5] = resultSet.getString("poNum");
        inventoryArray[6] = resultSet.getString("purchasePrice");
        inventoryArray[7] = resultSet.getString("vendorName");
        inventoryArray[8] = resultSet.getString("manufacturer");
        inventoryArray[9] = resultSet.getString("modelNum");
        inventoryArray[10] = resultSet.getString("serialNum");
        inventoryArray[11] = resultSet.getString("printerIP");
        inventoryArray[12] = resultSet.getString("printerDesc");
        inventoryArray[13] = resultSet.getString("computerDesc");
        inventoryArray[14] = resultSet.getString("monitorTypeSize");
        inventoryArray[15] = resultSet.getString("cpu");
        inventoryArray[16] = resultSet.getString("opticalDisk");
        inventoryArray[17] = resultSet.getString("hdSize");
        inventoryArray[18] = resultSet.getString("osType");
        inventoryArray[19] = resultSet.getString("mouse");
        inventoryArray[20] = resultSet.getString("memory");
        inventoryArray[21] = resultSet.getString("sound");
        inventoryArray[22] = resultSet.getString("officeType");
        inventoryArray[23] = resultSet.getString("speakers");
        inventoryArray[24] = resultSet.getString("allPrograms");
        inventoryArray[25] = resultSet.getString("comments");
        inventoryArray[26] = resultSet.getString("createdBy");
        inventoryArray[27] = resultSet.getString("createDate");
        inventoryArray[28] = resultSet.getString("lastEditedBy");
        inventoryArray[29] = resultSet.getString("lastEditDate");
        inventoryArray[30] = resultSet.getString("checkedOutTo");
        inventoryArray[31] = resultSet.getString("checkedOutDate");
        inventoryArray[32] = resultSet.getString("itemHistory");
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
    return inventoryArray;
  }

  public String[] checkInventoryData(String itemID){
    String[] inventoryArray = new String[33];
    try{
      mySqlConnect=establishConnection();
      statement = mySqlConnect.createStatement();
      checkItemNum = statement.executeQuery("select * from "+databaseName+"."+tableName+" WHERE itemID="+itemID+" LIMIT 1");

      checkItemNum.last();
      int rowCount=checkItemNum.getRow();
      System.out.println("numRows: "+rowCount+"\n");
      if(rowCount==0){
        inventoryArray[0] = "NULL";
        return inventoryArray;
      }else{
        resultSet = statement.executeQuery("select * from "+databaseName+"."+tableName+" WHERE itemID="+itemID+" LIMIT 1");
        while(resultSet.next()){
          /* It is possible to get the columns via name also possible to get the
           * columns via the column number which starts at 1
           * e.g. resultSet.getSTring(2);
           */

          inventoryArray[0] = resultSet.getString("itemID");
          inventoryArray[1] = resultSet.getString("location");
          inventoryArray[2] = resultSet.getString("building");
          inventoryArray[3] = resultSet.getString("room");
          inventoryArray[4] = resultSet.getString("dateAcquired");
          inventoryArray[5] = resultSet.getString("poNum");
          inventoryArray[6] = resultSet.getString("purchasePrice");
          inventoryArray[7] = resultSet.getString("vendorName");
          inventoryArray[8] = resultSet.getString("manufacturer");
          inventoryArray[9] = resultSet.getString("modelNum");
          inventoryArray[10] = resultSet.getString("serialNum");
          inventoryArray[11] = resultSet.getString("printerIP");
          inventoryArray[12] = resultSet.getString("printerDesc");
          inventoryArray[13] = resultSet.getString("computerDesc");
          inventoryArray[14] = resultSet.getString("monitorTypeSize");
          inventoryArray[15] = resultSet.getString("cpu");
          inventoryArray[16] = resultSet.getString("hdSize");
          inventoryArray[17] = resultSet.getString("opticalDisk");
          inventoryArray[18] = resultSet.getString("osType");
          inventoryArray[19] = resultSet.getString("mouse");
          inventoryArray[20] = resultSet.getString("memory");
          inventoryArray[21] = resultSet.getString("sound");
          inventoryArray[22] = resultSet.getString("officeType");
          inventoryArray[23] = resultSet.getString("speakers");
          inventoryArray[24] = resultSet.getString("allPrograms");
          inventoryArray[25] = resultSet.getString("comments");
          inventoryArray[26] = resultSet.getString("createdBy");
          inventoryArray[27] = resultSet.getString("createDate");
          inventoryArray[28] = resultSet.getString("lastEditedBy");
          inventoryArray[29] = resultSet.getString("lastEditDate");
          inventoryArray[30] = resultSet.getString("checkedOutTo");
          inventoryArray[31] = resultSet.getString("checkedOutDate");
          inventoryArray[32] = resultSet.getString("itemHistory");
        }
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
    return inventoryArray;
  }

  public boolean setNewInventoryItem(String[] newItemInfo){
    /*
     * USEAGE:
     * mySqlStuff sql=new mySqlStuff();
     * String[] dataToAdd = new String[]{"1111","This is another test item."};
     * sql.setNewInventoryItem(dataToAdd);
     */
    try{
      mySqlConnect=establishConnection();
      preparedStatement = mySqlConnect.prepareStatement("INSERT INTO "+databaseName+"."+tableName+" VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'created','2012-01-06 00:00:00','edited',default,'checked out','2012-01-06 00:00:00',default)");
      int i=0;
      int j=1;
      for(i=0;i<newItemInfo.length;i++){
        System.out.println(newItemInfo[i]);
        preparedStatement.setString(j, newItemInfo[i]);
        j++;
      }
      preparedStatement.executeUpdate();
      return true;
    }catch(Exception ex){
      ex.printStackTrace();
    }
    return false;
  }

  public boolean updateInventoryItem(String[] itemInfo){
    /*
     * USEAGE:
     * mySqlStuff sql=new mySqlStuff();
     * String[] dataToAdd = new String[]{"1111","This is another test item."};
     * sql.setNewInventoryItem(dataToAdd);
     */

    try{
      mySqlConnect=establishConnection();
      preparedStatement = mySqlConnect.prepareStatement("UPDATE "+databaseName+"."+tableName+" "
              + "SET location=?, "        //1
              + "building=?, "        //2
              + "room=?, "            //3
              + "dateAcquired=?, "    //4
              + "poNum=?, "           //5
              + "purchasePrice=?, "   //6
              + "vendorName=?, "      //7
              + "manufacturer=?, "    //8
              + "modelNum=?, "        //9
              + "serialNum=?, "       //10
              + "printerIP=?, "       //11
              + "printerDesc=?, "     //12
              + "computerDesc=?, "    //13
              + "monitorTypeSize=?, " //14
              + "cpu=?, "             //15
              + "opticalDisk=?, "     //16
              + "hdSize=?, "          //17
              + "osType=?, "          //18
              + "mouse=?, "           //19
              + "memory=?, "          //20
              + "sound=?, "           //21
              + "officeType=?, "      //22
              + "speakers=?, "        //23
              + "allPrograms=?, "     //24
              + "comments=?, "        //25
              + "lastEditedBy='USERSNAME', "
              + "lastEditDate='2012-01-06 00:00:00', "
              //+ "checkedOutTo=, "
              //+ "checkedOutDate=?, "
              + "itemHistory='UPDATED TODAY BY' "
              + "WHERE itemID=?");        // 0

      for(int i=1;i<itemInfo.length;i++){
        System.out.println(i+" -> "+itemInfo[i]);
        preparedStatement.setString(i, itemInfo[i]);
      }
      preparedStatement.setString(26, itemInfo[0]);
      preparedStatement.executeUpdate();
      return true;
    }catch(Exception ex){
      ex.printStackTrace();
    }
    return false;
  }
  /*
	public void readDataBase() throws Exception {
    mySqlConnect=establishConnection();
		try {
			// Statements allow to issue SQL queries to the database
			statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("select * from hill_inventory.inventory");
			writeResultSet(resultSet);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = mySqlConnect.prepareStatement("insert into hill_inventory.inventory values (default, ?, ?)");
			// "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
			// Parameters start with 1
			preparedStatement.setString(1, "1234");
			preparedStatement.setString(2, "TEst Item");
			preparedStatement.executeUpdate();

			preparedStatement = mySqlConnect.prepareStatement("SELECT itemID, itemName from hill_inventory.inventory");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

			// Remove again the insert comment
			preparedStatement = mySqlConnect.prepareStatement("delete from hill_inventory.inventory where itemID= ? ; ");
			preparedStatement.setString(1, "6252");
			preparedStatement.executeUpdate();

			resultSet = statement.executeQuery("select * from hill_inventory.inventory");
			writeMetaData(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// 	Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}



	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
*/
} /* close mySqlStuff class */