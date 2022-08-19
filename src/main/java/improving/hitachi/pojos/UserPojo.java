package improving.hitachi.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author EduardoGallegos<JuanGallegos at improving.hitachi>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPojo implements Serializable {

    @Value("#{props.string.backupId:Eduardo}")
    private String firstName;
    @Value("#{props.string.backupId:Gallegos}")
    private String lastName;
    private Long id;

    public UserPojo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
