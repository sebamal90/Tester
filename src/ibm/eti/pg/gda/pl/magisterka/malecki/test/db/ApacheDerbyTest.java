/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ibm.eti.pg.gda.pl.magisterka.malecki.test.db;

/**
 *
 * @author SebaTab
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ApacheDerbyTest {

 public void run(){
  try {
   Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
   Connection connection = DriverManager.getConnection("jdbc:derby:myDb;create=true;user=test;password=test");
   Statement stm = connection.createStatement();
   stm.execute("CREATE TABLE customers("+
        "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"+
        "name VARCHAR(256)," +
        "surname VARCHAR(256)," +
        "email VARCHAR(256)," +
        "phone_number VARCHAR(256))");
   stm.close();

   stm = connection.createStatement();
   stm.execute("INSERT INTO customers(name, surname, email, phone_number) VALUES('Mike', 'Normans', 'm.normans@gmail.com', '654-456-546')");
   stm.close();
   connection.close();
  } catch (ClassNotFoundException | SQLException e) {
   e.printStackTrace();
  }
  System.out.println("Row has been inserted...");
 }

 public static void main(String[] args) {
  new ApacheDerbyTest().run();
 }
}