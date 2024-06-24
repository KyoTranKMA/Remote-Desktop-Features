package VNCClient.VNCClient;

import VNCClient.VNCClient.controller.UserController;
import VNCClient.VNCClient.daoservice.UserDao;
import VNCClient.VNCClient.dbservice.IDataProvider;
import VNCClient.VNCClient.dbservice.MySQLDataProvider;
import VNCClient.VNCClient.dto.UserDto;
import VNCClient.VNCClient.service.UserService;
import VNCClient.VNCClient.view.VNCGUI;

import java.sql.SQLException;
import java.util.List;

import static javax.swing.SwingUtilities.invokeLater;

public class ClientModuleApplication {
    private void init() throws SQLException {
        IDataProvider dataProvider = new MySQLDataProvider();
        UserDao.getInstance().setDataProvider(dataProvider);
        UserService.getInstance().LoadUserFromDB();
    }
    private void login() {
        UserController userController = UserController.getInstance();
        List<UserDto> dto = userController.getListUser();
        System.out.println(dto.size());
    }
    private void signUp(){
        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername("Tran Quang Dieu");
        userDto.setPassword("Dieu");
        userDto.setFirstName("admin");
        userDto.setLastName("root");
        userController.addUser(userDto);
        userController.getListUser();
        System.out.println();
    }
    public static void main(String[] args) {
        try {
            ClientModuleApplication app = new ClientModuleApplication();
            app.init();
            app.signUp();
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
