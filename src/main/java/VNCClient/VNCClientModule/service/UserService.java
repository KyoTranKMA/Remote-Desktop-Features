package VNCClient.VNCClientModule.service;


import VNCClient.VNCClientModule.daoservice.UserDao;
import VNCClient.VNCClientModule.entity.UserEntity;
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
    public List<UserModel> getListUser() {
        return this.LoadUserFromDB();
    }
    public void login(UserModel user) {
        UserDao.getInstance().login(toEntity(user));
    }
    public void addUser(UserModel user) {
        UserDao.getInstance().addUser(toEntity(user));
        listUser.add(user);
    }
    public List<UserModel> LoadUserFromDB() {
        List<UserEntity> entityList = UserDao.getInstance().loadEntityFromDB();
        for (UserEntity entity : entityList) {
            this.listUser.add(toModel(entity));
        }
        return List.copyOf(this.listUser);
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


}
