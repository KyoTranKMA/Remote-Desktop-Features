package VNCClient.VNCClient.dbservice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDataProvider {
    void open() throws SQLException;

    void close() throws SQLException;

    ResultSet executeQuery(String sql) throws SQLException;

    int executeUpdate(String sql) throws SQLException;

    PreparedStatement prepareStatement(String sql);
}
