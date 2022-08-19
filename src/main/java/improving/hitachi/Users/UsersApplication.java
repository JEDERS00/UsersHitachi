package improving.hitachi.Users;

import improving.hitachi.controllers.UserController;
import improving.hitachi.options.Menu;
import improving.hitachi.pojos.UserPojo;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author EduardoGallegos<JuanGallegos at improving.hitachi>
 */
@SpringBootApplication
@RestController
public class UsersApplication {
    
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
        new Menu().print();
    }

    @PostMapping("/addUser")
    @ResponseBody
    public ResponseEntity<?> addUser(@RequestBody UserPojo userPojo) {
        try {
            boolean added = new UserController(env).addUser(userPojo);
            return ResponseEntity.ok(added ? "User added successfully." : "Error adding user.");
        } catch (RemoteException re) {
            return ResponseEntity.badRequest().body("Error adding user: " + re.getMessage());
        }
    }

    @GetMapping("/getUsers")
    @ResponseBody
    public ResponseEntity<?> getUserById() {
        return getUsers();
    }
    
    @GetMapping("/getUsers/{userId}")
    @ResponseBody
    public ResponseEntity<?> getUserById(@PathVariable Map<String, String> pathVarsMap) {
        Optional<String> optionalUserID = Optional.ofNullable(StringUtils.trimToEmpty(pathVarsMap.get("userId")));
        if (optionalUserID.isPresent()) {//Search by user ID
            return getUserById(optionalUserID.get());
        } else {//Get all user ordered alphabetically by last name
            return getUsers();
        }
    }

    private ResponseEntity<?> getUserById(String userID) {
        try {
            if (StringUtils.isNumeric(userID)) {
                Optional<UserPojo> optionalUser = new UserController(env).getUserById(new Long(userID));
                if (optionalUser.isPresent()) {
                    return ResponseEntity.badRequest().body(optionalUser.get());
                } else {
                    return ResponseEntity.badRequest().body("User does not exist.");
                }
            } else {
                return ResponseEntity.badRequest().body("User ID is not numeric.");
            }
        } catch (RemoteException re) {
            return ResponseEntity.badRequest().body("Error trying to find user: " + re.getMessage());
        }
    }

    private ResponseEntity<?> getUsers() {
        try {
            List<UserPojo> users = new UserController(env).getUsers();
            return ResponseEntity.badRequest().body(users);
        } catch (RemoteException re) {
            return ResponseEntity.badRequest().body("Error trying to get users: " + re.getMessage());
        }
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public ResponseEntity<?> postBackups(@RequestParam(value = "USER_ID") String userID) {
        Optional<String> optionalUserID = Optional.ofNullable(userID);
        if (optionalUserID.isPresent()) {
            try {
                String id = optionalUserID.get();
                if (StringUtils.isNumeric(userID)) {
                    if (new UserController(env).deleteUserById(new Long(id))) {
                        return ResponseEntity.badRequest().body("User deleted successfully.");
                    } else {
                        return ResponseEntity.badRequest().body("User does not exist.");
                    }
                } else {
                    return ResponseEntity.badRequest().body("User ID is not numeric.");
                }

            } catch (RemoteException re) {
                return ResponseEntity.badRequest().body("Error trying to find user: " + re.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("User Id is required.");
        }
    }

}
