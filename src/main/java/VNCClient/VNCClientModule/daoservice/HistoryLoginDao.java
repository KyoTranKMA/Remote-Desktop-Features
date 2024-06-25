package VNCClient.VNCClientModule.daoservice;

import VNCClient.VNCClientModule.dbservice.IDataProvider;
import VNCClient.VNCClientModule.entity.HistoryLoginEntity;
import VNCClient.VNCClientModule.entity.UserEntity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoryLoginDao {
    private static HistoryLoginDao instance;
    IDataProvider dataProvider;

    public static HistoryLoginDao getInstance() {
        if (instance == null) {
            instance = new HistoryLoginDao();
        }
        return instance;
    }

    private HistoryLoginDao() {
    }

    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<HistoryLoginEntity> loadUserIpFromDB() {
        if (this.dataProvider == null) {
            throw new RuntimeException("DataProvider is null");
        }
        List<HistoryLoginEntity> listUser = new ArrayList();
        try {
            final String sql = "Select * From login_history";
            this.dataProvider.open();
            ResultSet resultSet = this.dataProvider.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    HistoryLoginEntity entity = new HistoryLoginEntity();
                    entity.setUsername(resultSet.getString("username"));
                    entity.setIpAddress(resultSet.getString("ip_address"));
                    listUser.add(entity);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listUser;
    }

}
