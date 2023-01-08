/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import static java.lang.System.exit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author ejer
 */
public class DataBase {
    private static final String username = "root";
    private static final String password = "";
    private static final String dataConn  = "jdbc:mysql://localhost:3300/testDatabase";
    static Connection con = null;
    static ResultSet rs = null;
    static boolean connected = false;
    static boolean verified = false;
    static int command = 0;
    static int currentBalance;
    static int amount;
    static int id;
    static Scanner in = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dataConn,username,password);
            System.out.println("Success!");
            connected = true;
        }catch(Exception e) {
            System.out.println("Failed!: "+e.getMessage());
        }finally {
            if (connected == true) {
            while(verified == false) {
             System.out.println("Enter your user name.");
             String name = in.next();
             System.out.println("Enter your user password.");
             String password = in.next();
             try {
                 Statement st = con.createStatement();
                rs = st.executeQuery("select * from users");
                while(rs.next()) {
                    if(name.equals(rs.getString("name"))) {
                        if(password.equals(rs.getString("password"))){
                            System.out.println("Successfully verified!");
                            verified = true;
                            id = rs.getInt("user_id");
                            break;
                        }
                    }
                }
                   System.out.println("Incorrect username or password!");
             } catch(Exception e) {
                 System.out.println("Failed!: "+e.getMessage());
             }
            }
            if(verified == true) {
                while(command != 4) {
                System.out.println("Enter a command \n1. Show account details\n2. Make a deposit\n3. Make a withdraw\n4. Quit");
                command = in.nextInt();
                switch(command){
                    case 1:
                        try {
                            PreparedStatement stmt = con.prepareStatement("select * from userInfo where user_id=?");
                            stmt.setString(1, ""+id);
                            rs = stmt.executeQuery();
                            while(rs.next()) {
                                System.out.println("Account No: "+rs.getInt("account_no")+"\nBalance: "+rs.getInt("account_balance"));
                            }
                            break;
                        }catch(Exception e) {
                            System.out.println("Failed! "+e.getMessage());
                            break;
                        }
                    case 2:
                        System.out.println("Enter the amount you want to deposit.");
                        amount = in.nextInt();
                        try{
                            PreparedStatement stmt = con.prepareStatement("select * from userInfo where user_id=?");
                            stmt.setString(1, ""+id);
                            rs = stmt.executeQuery();
                            while(rs.next()) {
                                currentBalance = rs.getInt("account_balance");
                            }
                            Statement st = con.createStatement();
                            st.executeUpdate("update userInfo set account_balance ="+(currentBalance + amount)+" where user_id="+id);
                            System.out.println("Successfully deposited!");
                            break;
                        }catch(Exception e) {
                            System.out.println("Failed! "+e.getMessage());
                            break;
                        }
                    case 3:
                        System.out.println("Enter the amount you want to withdraw.");
                        amount = in.nextInt();
                         try{
                            PreparedStatement stmt = con.prepareStatement("select * from userInfo where user_id=?");
                            stmt.setString(1, ""+id);
                            rs = stmt.executeQuery();
                            while(rs.next()) {
                                currentBalance = rs.getInt("account_balance");
                            }
                            Statement st = con.createStatement();
                            st.executeUpdate("update userInfo set account_balance ="+(currentBalance - amount)+" where user_id="+id);
                            System.out.println("Successfully Withdrawn!");
                            break;
                        }catch(Exception e) {
                            System.out.println("Failed! "+e.getMessage());
                            break;
                        }
                    case 4:
                        System.out.println("Thank you!");
                        exit(0);
                }
                
                   
            } 
                
        }
            } else {
                System.out.println("Failed to connect!");
            }
        // TODO code application logic here
    }
   }
}
