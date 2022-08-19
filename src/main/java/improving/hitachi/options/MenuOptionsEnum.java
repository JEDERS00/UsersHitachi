package improving.hitachi.options;

import java.util.Optional;

/**
 *
 * @author EduardoGallegos<JuanGallegos at improving.hitachi>
 */
public enum MenuOptionsEnum {
    ADD_USER(1, "Add a user."),
    RETURN_USER_BY_ID(2, "Returns a single user by id."),
    RETURN_USER_LIST(3, "Returns a list of all users."),
    DELETE_USER_BY_ID(4, "Delete a user by id."),
    EXIT(5, "Exit.");

    private int option;
    private String description;

    private MenuOptionsEnum(int option, String description) {
        this.option = option;
        this.description = description;
    }

    public static Optional<MenuOptionsEnum> getByOption(int option) {
        Optional<MenuOptionsEnum> optional = Optional.empty();
        for (MenuOptionsEnum menuOptionsEnum : MenuOptionsEnum.values()) {
            if (menuOptionsEnum.getOption() == option) {
                optional = Optional.of(menuOptionsEnum);
                break;
            }
        }
        return optional;
    }

    public int getOption() {
        return this.option;
    }

    public String getDescription() {
        return this.description;
    }

}
