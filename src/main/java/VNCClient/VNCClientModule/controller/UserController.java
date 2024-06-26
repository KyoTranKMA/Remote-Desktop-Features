package VNCClient.VNCClientModule.controller;

import VNCClient.VNCClientModule.dto.HistoryLoginDto;
import VNCClient.VNCClientModule.dto.UserDto;
import VNCClient.VNCClientModule.model.HistoryLoginModel;
import VNCClient.VNCClientModule.model.UserModel;
import VNCClient.VNCClientModule.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserController implements IUserController{
    private static UserController instance;

    synchronized public static UserController getInstance() {
        if (UserController.instance == null) {
            UserController.instance = new UserController();
        }
        return instance;
    }

    private UserController() {
    }

    @Override
    public List<HistoryLoginDto> getListUserIp() {
        List<HistoryLoginDto> resultList = new ArrayList();
        List<HistoryLoginModel> listUser = UserService.getInstance().getListUserIp();
        for (HistoryLoginModel user : listUser) {
            resultList.add(toDto(user));
        }
        return resultList;
    }

    @Override
    public boolean addUser(UserDto user) {
       return UserService.getInstance().addUser(toModel(user));
    }

    @Override
    public boolean login(UserDto user) {
        return UserService.getInstance().login(toModel(user));
    }
    @Override
    public void logout(UserDto user) {
        UserService.getInstance().logout(toModel(user));
    }

    public UserModel toModel(UserDto dto){
        UserModel model = new UserModel();
        model.setFirstname(dto.getFirstName());
        model.setLastname(dto.getLastName());
        model.setUsername(dto.getUsername());
        model.setPassword(dto.getPassword());
        return model;
    }

    public UserDto toDto(UserModel model) {
        UserDto dto = new UserDto();
        dto.setUsername(model.getUsername());
        dto.setFirstName(model.getFirstname());
        dto.setPassword(model.getPassword());
        return dto;
    }
    public HistoryLoginDto toDto(HistoryLoginModel model){
        HistoryLoginDto dto = new HistoryLoginDto();
        dto.setIpAddess(model.getIpAddress());
        dto.setUsername(model.getUsername());
        return dto;
    }

}
