package VNCClient.VNCClient;

import VNCClient.VNCClient.controller.UserController;
import VNCClient.VNCClient.daoservice.UserDao;
import VNCClient.VNCClient.dbservice.IDataProvider;
import VNCClient.VNCClient.dbservice.MySQLDataProvider;
import VNCClient.VNCClient.dto.UserDto;
import VNCClient.VNCClient.service.UserService;
import VNCClient.VNCClient.view.VNCGUI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    private void signUp(){
        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername("tao");
        userDto.setPassword("tao");
        userDto.setFirstName("Tran Quang Dieu");
        userDto.setLastName("root");
        userController.addUser(userDto);
        userController.getListUser();
        System.out.println();
    }
    public static void main(String[] args) {
        try {
            ClientModuleApplication app = new ClientModuleApplication();
            app.init();
            app.login();
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
