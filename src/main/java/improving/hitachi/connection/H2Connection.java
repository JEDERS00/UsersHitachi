package improving.hitachi.connection;

import improving.hitachi.Users.UsersApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author EduardoGallegos<JuanGallegos at improving.hitachi>
 */
@RestController
public class H2Connection {

    private static H2Connection h2Connection = null;
    private Connection conn = null;
    
    private H2Connection(Environment env) {
        try {
            Class.forName(env.getProperty("datasource.driver"));
            this.conn = DriverManager.getConnection(env.getProperty("datasource.url"), env.getProperty("datasource.db.user"), env.getProperty("datasource.db.pass"));
        } catch (ClassNotFoundException cnfe) {
            Logger.getLogger(H2Connection.class.getName()).log(Level.SEVERE, null, cnfe);
        } catch (SQLException ex) {
            Logger.getLogger(H2Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Connection getConn() {
        return this.conn;
    }

    public static synchronized Connection getConnection(Environment env) {
        if (h2Connection == null) {
            h2Connection = new H2Connection(env);
        }
        return h2Connection.getConn();
    }

}
