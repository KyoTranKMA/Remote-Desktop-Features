package VNCClient.VNCClient.controller;

import VNCClient.VNCClient.dto.UserDto;

import java.util.List;

public interface IUserController {
    List<UserDto> getListUser();
    public void addUser(UserDto user);
    void login(UserDto user);
}
