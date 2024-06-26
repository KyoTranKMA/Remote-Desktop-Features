package VNCClient.VNCClientModule;

import VNCClient.VNCClientModule.controller.UserController;
import VNCClient.VNCClientModule.daoservice.UserDao;
import VNCClient.VNCClientModule.dbservice.IDataProvider;
import VNCClient.VNCClientModule.dbservice.MySQLDataProvider;
import VNCClient.VNCClientModule.dto.UserDto;
import VNCClient.VNCClientModule.service.UserService;
import VNCClient.VNCClientModule.view.MainFrame;
import VNCClient.VNCClientModule.view.VNCGUI;
import com.sun.tools.javac.Main;

import java.sql.SQLException;
import java.util.List;

import static javax.swing.SwingUtilities.invokeLater;

public class ClientModuleApplication {
    private void init() throws SQLException {
        IDataProvider dataProvider = new MySQLDataProvider();
        UserDao.getInstance().setDataProvider(dataProvider);
        UserService.getInstance().LoadUserFromDB();
    }
    public static void login(String username, String password){
        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userController.login(userDto);

        List<UserDto> dto = userController.getListUser();
    }
    public static void logout(String username){
        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userController.logout(userDto);
        System.out.println(username);
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
            invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
//                VNCGUI viewer = new VNCGUI();
//                viewer.setVisible(true);
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
