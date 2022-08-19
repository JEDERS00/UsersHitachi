package improving.hitachi.controllers;

import improving.hitachi.connection.H2Connection;
import improving.hitachi.pojos.UserPojo;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.env.Environment;

/**
 *
 * @author EduardoGallegos<JuanGallegos at improving.hitachi>
 */
public class UserController {
    
    private Environment env;
    
    public UserController(Environment env) {
        this.env = env;
    }

    public boolean addUser(UserPojo userPojo) throws RemoteException {
        boolean result = false;
        Connection conn = H2Connection.getConnection(this.env);
        try ( PreparedStatement ps = conn.prepareStatement("INSERT INTO users (first_name, last_name) VALUES (?, ?);");) {
            int row = 1;
            ps.setString(row++, userPojo.getFirstName());
            ps.setString(row++, userPojo.getLastName());
            result = ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            String errorMessage = ex.getMessage();
            int errorCode = ex.getErrorCode();
            if (23505 == errorCode) {//23505 Code of Unique index or primary key violation
                errorMessage = String.format("User [%s %s] already exist.", userPojo.getFirstName(), userPojo.getLastName());
            }
            throw new RemoteException(errorMessage);
        }
        return result;
    }

    public Optional<UserPojo> getUserById(Long userID) throws RemoteException {
        ResultSet rs = null;
        Optional<UserPojo> optionalUser = Optional.empty();
        Connection conn = H2Connection.getConnection(this.env);
        try ( PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?;");) {
            int row = 1;
            ps.setLong(row++, userID);
            rs = ps.executeQuery();
            if (rs.next()) {
                UserPojo up = new UserPojo(rs.getString("first_name"), rs.getString("last_name"));
                up.setId(rs.getLong("id"));
                optionalUser = Optional.of(up);
            }
        } catch (SQLException ex) {
            throw new RemoteException(ex.getMessage());
        } finally {
            closeResultSet(rs);
        }
        return optionalUser;
    }

    public List<UserPojo> getUsers() throws RemoteException {
        List<UserPojo> users = new ArrayList();
        Connection conn = H2Connection.getConnection(this.env);
        try ( PreparedStatement ps = conn.prepareStatement("SELECT * FROM users ORDER BY last_name DESC;");
              ResultSet rs = ps.executeQuery();) {
            UserPojo up;
            while (rs.next()) {
                up = new UserPojo(rs.getString("first_name"), rs.getString("last_name"));
                up.setId(rs.getLong("id"));
                users.add(up);
            }
        } catch (SQLException ex) {
            throw new RemoteException(ex.getMessage());
        }
        return users;
    }

    public boolean deleteUserById(Long userID) throws RemoteException {
        boolean result = false;
        Connection conn = H2Connection.getConnection(this.env);
        try ( PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?;");) {
            int row = 1;
            ps.setLong(row++, userID);
            result = ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            throw new RemoteException(ex.getMessage());
        }
        return result;
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
