package VNCClient.VNCClient.daoservice;

import VNCClient.VNCClient.dbservice.IDataProvider;
import VNCClient.VNCClient.entity.UserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static UserDao instance;
    IDataProvider dataProvider;

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {
    }

    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<UserEntity> loadEntityFromDB() {
        if (this.dataProvider == null) {
            throw new RuntimeException("DataProvider is null");
        }
        List<UserEntity> listUser = new ArrayList();
        try {
            final String sql = "Select * From users";
            this.dataProvider.open();
            ResultSet resultSet = this.dataProvider.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    UserEntity entity = new UserEntity();
                    entity.setUsername(resultSet.getString("username"));
                    entity.setPassword(resultSet.getString("password"));
                    entity.setFirstName(resultSet.getString("first_name"));
                    entity.setLastName(resultSet.getString("last_name"));
                    listUser.add(entity);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listUser;
    }
    public void addUser(UserEntity entity) {
        if (this.dataProvider == null) {
            throw new RuntimeException("DataProvider is null");
        }
        try {
            final String sql = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
            this.dataProvider.open();
            PreparedStatement preparedStatement = this.dataProvider.prepareStatement(sql);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getFirstName());
            preparedStatement.setString(4, entity.getLastName());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Add user success");
            } else {
                System.out.println("Add user failed");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.dataProvider.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
