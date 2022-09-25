package DataBase;

import Entity.Account;
import Entity.Customer;
import Entity.Operation;
import Louncher.MyMoney;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

import static DataBase.HibernateSessionFactoryUtil.getSessionFactory;

public class RunDb implements Financial {

    @Override
    public Operation addOperation(String customerId, String accId, Double sum, String category, String subcategory) {

            Customer customer = getItemByStringId(Customer.class, customerId);
            Account account = customer.getAccounts().stream().filter(e -> e.getIban().equals(accId)).findFirst().orElse(null);
            if (sum <=0 || account == null) {
                System.out.println("Wrong sum or account"); return null;
            }
            boolean flag = false;
            Operation operation = new Operation();
            operation.setDateTime(LocalDateTime.now());
            operation.setAccount(account);

            if (category.equalsIgnoreCase("income") && Arrays.toString(MyMoney.Income.values()).
                    contains(subcategory.toUpperCase())) {

                switch (subcategory.toLowerCase()) {
                    case "salary" -> operation.setIncomeCategory(MyMoney.Income.SALARY);
                    case "interests" -> operation.setIncomeCategory(MyMoney.Income.INTERESTS);
                    case "bonus" -> operation.setIncomeCategory(MyMoney.Income.BONUS);
                    case "savings" -> operation.setIncomeCategory(MyMoney.Income.SAVINGS);
                    case "other" -> operation.setIncomeCategory(MyMoney.Income.OTHER);
                }
                flag = true;
                account.setAmount(account.getAmount() + sum);
            }
            if (category.equalsIgnoreCase("expense") && Arrays.toString(MyMoney.Expense.values()).
                    contains(subcategory.toUpperCase()) && (sum <= account.getAmount())) {

                switch (subcategory.toLowerCase()) {
                    case "food" -> operation.setExpenseCategory(MyMoney.Expense.FOOD);
                    case "clothing" -> operation.setExpenseCategory(MyMoney.Expense.CLOTHING);
                    case "home" -> operation.setExpenseCategory(MyMoney.Expense.HOME);
                    case "health" -> operation.setExpenseCategory(MyMoney.Expense.HEALTH);
                    case "transport" -> operation.setExpenseCategory(MyMoney.Expense.TRANSPORT);
                    case "pets" -> operation.setExpenseCategory(MyMoney.Expense.PETS);
                    case "fun" -> operation.setExpenseCategory(MyMoney.Expense.FUN);
                    case "other" -> operation.setExpenseCategory(MyMoney.Expense.OTHER);
                }
                flag = true;
                account.setAmount(account.getAmount() - sum);
            }
            if (!flag)  {return null;}
                updateDb(account);
                operation.setSum(sum);
                return operation;
    }
    @Override
    public boolean getOperations(String userId, String dateFrom, String dateTo, @NotNull Statement statement) throws SQLException, IOException {
        StringBuilder accFilter = new StringBuilder();

        ResultSet result = statement.executeQuery("SELECT id FROM account WHERE customer_id = '"+ userId+"'");
             while (result.next()) {
                 accFilter.append("'");
                 accFilter.append(result.getString("id"));
                 accFilter.append("',");
        }
        accFilter.deleteCharAt(accFilter.lastIndexOf(","));
        String accounts = accFilter.toString();
        writeCsv(statement.executeQuery("SELECT * FROM operation WHERE account_id IN (" + accounts + ") AND datemark >= '" + dateFrom + "' AND datemark <= '" + dateTo + "'"));
        return true;
        //May be better to use below-mentioned query in production
        //COPY (SELECT * FROM operation WHERE account_id IN () AND date BETWEEN 'dateFrom' AND 'dateTo') TO '/path/file.csv' WITH CSV DELIMITER ',' HEADER;

    }

    private <T> T getItemByStringId (Class <T> clazz, String id) {
        SessionFactory factory = getSessionFactory();
        Transaction tx = null;
        T item = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            item = session.get(clazz, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return item;
    }
    private void writeCsv (ResultSet resultSet) throws SQLException, IOException {

        final String MIDDLE_DELIMITER = "\",\"";
        File file = new File("operation.csv");

        if (file.exists())  file.delete();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

        while (resultSet.next()) {

            String sb = '"' +
                    resultSet.getString("id").substring(24) +
                    MIDDLE_DELIMITER +
                    resultSet.getTimestamp("datemark") +
                    MIDDLE_DELIMITER +
                    resultSet.getString("expense") +
                    MIDDLE_DELIMITER +
                    resultSet.getString("income") +
                    MIDDLE_DELIMITER +
                    resultSet.getDouble("sum") +
                    MIDDLE_DELIMITER +
                    resultSet.getString("account_id") +
                    '"';
                bw.write(sb);
                bw.newLine();
            }
        }
    }
    public void updateDb(Object item) {
        SessionFactory factory = getSessionFactory();
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.merge( item );
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
    /*public List<?> readDbTable(String entityName) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(entityName);
        SessionFactory factory = getSessionFactory();
        Transaction tx = null;
        List<?> items = null;
        try (Session session = factory.openSession()) {

            tx = session.beginTransaction();
            items = session.createSelectionQuery("from " + entityName, aClass).getResultList();
            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return items;
    }*/
    /*public void saveToTable(@NotNull Object item) {
        SessionFactory factory = getSessionFactory();
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.persist(item);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }*/
}
