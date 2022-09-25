package DataBase;

import Entity.Operation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public interface Financial {

    boolean getOperations(String userId, String dateFrom, String dateTo, @NotNull Statement statement) throws SQLException, IOException;

    Operation addOperation (String customerId, String accId, Double sum, String category, String subcategory);
}
