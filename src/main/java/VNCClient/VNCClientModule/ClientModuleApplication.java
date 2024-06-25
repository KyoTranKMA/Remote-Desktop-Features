package VNCClient.VNCClientModule;

import VNCClient.VNCClientModule.controller.UserController;
import VNCClient.VNCClientModule.daoservice.UserDao;
import VNCClient.VNCClientModule.dbservice.IDataProvider;
import VNCClient.VNCClientModule.dbservice.MySQLDataProvider;
import VNCClient.VNCClientModule.dto.UserDto;
import VNCClient.VNCClientModule.service.UserService;
import VNCClient.VNCClientModule.view.VNCGUI;

import java.sql.SQLException;
import java.util.List;

import static javax.swing.SwingUtilities.invokeLater;

public class ClientModuleApplication {
    private void init() throws SQLException {
        IDataProvider dataProvider = new MySQLDataProvider();
        UserDao.getInstance().setDataProvider(dataProvider);
        UserService.getInstance().LoadUserFromDB();
    }
    private void login(){
        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername("tao");
        userDto.setPassword("tao");
        userController.login(userDto);

        List<UserDto> dto = userController.getListUser();
    }
    private void logout(){
        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername("tao");
        userController.logout(userDto);
    }

    private void signUp(){
        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername("tao");
        userDto.setPassword("tao");
        userDto.setFirstName("Tran Quang Dieu");
        userDto.setLastName("root");
        userController.addUser(userDto);
        userController.getListUser();
    }
    public static void main(String[] args) {
        try {
            ClientModuleApplication app = new ClientModuleApplication();
            app.init();
            app.logout();
            invokeLater(() -> {
                VNCGUI viewer = new VNCGUI();
                viewer.setVisible(true);
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
