package VNCClient.VNCClient.dbservice;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDataProvider implements IDataProvider {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySQLDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Connection connection;

    @Override
    public void open() throws SQLException {
        if (this.connection == null) {
            final String DB_URL = "jdbc:mysql://roundhouse.proxy.rlwy.net:40039/railway";
            final String USER_NAME = "root";
            final String PASSWORD = "YNBQVsoIkrZYZvtQGGRpNzPXCiJwjvJc";

            this.connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            System.out.println("Connected to the database");
        }
    }

    @Override
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        if (this.connection != null) {
            Statement statement = this.connection.createStatement();
            return statement.executeQuery(sql);
        }
        return null;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        if (this.connection != null) {
            Statement statement = this.connection.createStatement();
            return statement.executeUpdate(sql);
        }
        return 0;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) {
        try {
            return this.connection.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
