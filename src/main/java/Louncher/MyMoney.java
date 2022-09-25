package Louncher;

import DataBase.RunDb;
import JdbcConnection.ConnectionPool;
import JdbcConnection.ConnectionPoolImplementation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class MyMoney {
    static String bdName, loginBd, passwordBd, userId;
    public enum Income { SALARY, INTERESTS, BONUS, SAVINGS, OTHER }
    public enum Expense { FOOD, CLOTHING, HOME, HEALTH, TRANSPORT, PETS, FUN, OTHER }
    private static final Logger logger = LoggerFactory.getLogger(MyMoney.class);

    public static void main(String @NotNull [] args) throws SQLException, IOException {

        logger.info("application started");
        bdName = args[0];
        loginBd = args[1];
        passwordBd = args[2];
        userId = args[3];
        RunDb instance = new RunDb();

          /*Customer customer1 = new Customer();
          customer1.setEmail ("user1@gmail.com");
          customer1.setName ("Mykola Chub");
          customer1.setPhone("+380(50)333-2211");

          Customer customer2 = new Customer();
          customer2.setEmail ("user2@gmail.com");
          customer2.setName ("Ihor Kozak");
          customer2.setPhone("+380(67)111-2233");

          Account acc1 = new Account();
          acc1.setIban("UA213223130000026007233566001");
          acc1.setAmount(10000d);
          acc1.setBank("PrivatBank");
          acc1.setCustomer(customer1);

          Account acc2 = new Account();
          acc2.setIban("UA213223130000026007233566002");
          acc2.setAmount(12000d);
          acc2.setBank("OTPBank");
          acc2.setCustomer(customer2);

          Account acc3 = new Account();
          acc3.setIban("UA213223130000026007233566003");
          acc3.setAmount(14000d);
          acc3.setBank("RaiffeizenBank");

          Account acc4 = new Account();
          acc4.setIban("UA213223130000026007233566041");
          acc4.setAmount(16000d);
          acc4.setBank("PrivatBank");

          Account acc5 = new Account();
          acc5.setIban("UA213223130000026007233566052");
          acc5.setAmount(18000d);
          acc5.setBank("OTPBank");

          Account acc6 = new Account();
          acc6.setIban("UA213223130000026007233566063");
          acc6.setAmount(20000d);
          acc6.setBank("RaiffeizenBank");

          Account [] accounts = {acc1,acc2,acc3,acc4,acc5,acc6};
          for (Account acc : accounts ) {
              instance.updateDb(acc);
          }
          acc3.setCustomer(customer1);
          acc4.setCustomer(customer2);
          instance.updateDb(acc3);
          instance.updateDb(acc4);*/


        //System.out.println(bdName + ",   " + loginBd+",   "+ passwordBd + ",   " + userId);
        Scanner in = new Scanner(System.in);
        System.out.println("Please choose mode Operation - 0/Balance - 1: ");
        int mode = in.nextInt();

        switch (mode) {
            case 0 -> instance.updateDb(instance.addOperation(userId,"UA213223130000026007233566001", 3000d, "expense", "food"));
            case 1 -> {
                final String CONNECTION_URL = String.format("jdbc:postgresql:%s", bdName);
                ConnectionPool connectionPool = ConnectionPoolImplementation.create(CONNECTION_URL, loginBd, passwordBd);
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement();
                if (instance.getOperations(userId,"2022-09-01", "2022-09-26", statement)) System.out.println("done!");
            }
        }
        logger.info("application finished");
    }
}
