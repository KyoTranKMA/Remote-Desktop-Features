package VNCClient.VNCClientModule.service;


import VNCClient.VNCClientModule.daoservice.HistoryLoginDao;
import VNCClient.VNCClientModule.daoservice.UserDao;
import VNCClient.VNCClientModule.entity.HistoryLoginEntity;
import VNCClient.VNCClientModule.entity.UserEntity;
import VNCClient.VNCClientModule.model.HistoryLoginModel;
import VNCClient.VNCClientModule.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserService instance;
    private final List<UserModel> listUser;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    private UserService() {
        this.listUser = new ArrayList<>();
    }
    public List<HistoryLoginModel> getListUserIp() {
        List<HistoryLoginEntity> historyLoginEntities = HistoryLoginDao.getInstance().loadUserIpFromDB();
        List<HistoryLoginModel> historyLoginModels = new ArrayList<>();
        for (HistoryLoginEntity entity : historyLoginEntities) {
            historyLoginModels.add(toModel(entity));
        }
        return historyLoginModels;
    }
    public void login(UserModel user) {
        UserDao.getInstance().login(toEntity(user));
    }
    public void logout(UserModel user) {
        UserDao.getInstance().logout(toEntity(user));
    }
    public void addUser(UserModel user) {
        UserDao.getInstance().addUser(toEntity(user));
        listUser.add(user);
    }

    private UserEntity toEntity(UserModel user) {
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setFirstName(user.getFirstname());
        entity.setLastName(user.getLastname());
        entity.setPassword(user.getPassword());
        return entity;
    }
    private UserModel toModel(UserEntity entity) {
        UserModel model = new UserModel();
        model.setFirstname(entity.getFirstName());
        model.setLastname(entity.getLastName());
        model.setPassword(entity.getPassword());
        return model;
    }
    private HistoryLoginEntity toEntity(HistoryLoginModel user) {
        HistoryLoginEntity HistoryLoginEntity = new HistoryLoginEntity();
        HistoryLoginEntity.setUsername(user.getUsername());
        HistoryLoginEntity.setIpAddress(user.getIpAddress());
        return HistoryLoginEntity;
    }
    private HistoryLoginModel toModel(HistoryLoginEntity entity) {
        HistoryLoginModel historyLoginModel = new HistoryLoginModel();
        historyLoginModel.setUsername(entity.getUsername());
        historyLoginModel.setIpAddress(entity.getIpAddress());
        return historyLoginModel;
    }



}
