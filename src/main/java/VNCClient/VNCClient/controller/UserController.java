package VNCClient.VNCClient.controller;

import VNCClient.VNCClient.dto.UserDto;
import VNCClient.VNCClient.model.UserModel;
import VNCClient.VNCClient.service.UserService;

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
    public List<UserDto> getListUser() {
        List<UserDto> resultList = new ArrayList();
        List<UserModel> listUser = UserService.getInstance().getListUser();
        for (UserModel user : listUser) {
            resultList.add(toDto(user));
        }
        return resultList;
    }
    @Override
    public void addUser(UserDto user) {
        UserService.getInstance().addUser(toModel(user));
    }

    @Override
    public void login(UserDto user) {
        UserService.getInstance().login(toModel(user));
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

}
