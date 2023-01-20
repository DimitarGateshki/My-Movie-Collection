package dal.database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {
    private final SQLServerDataSource dataSource;

    public DatabaseConnector(){
        dataSource= new SQLServerDataSource();
        dataSource.setDatabaseName("MyMovieCollection");
        dataSource.setUser("CSe22B_8");
        dataSource.setPassword("CSe22B_8");
        dataSource.setServerName("10.176.111.31");
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }
    public Connection getConnection() {
        try {
            return dataSource.getConnection("CSe22B_8", "CSe22B_8");
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }
    }

}
