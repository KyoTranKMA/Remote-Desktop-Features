package VNCClient.VNCClientModule.controller;

import VNCClient.VNCClientModule.dto.HistoryLoginDto;
import VNCClient.VNCClientModule.dto.UserDto;

import java.util.List;

public interface IUserController {
    List<HistoryLoginDto> getListUserIp();
    public void addUser(UserDto user);
    void login(UserDto user);
    void logout(UserDto user);
}
