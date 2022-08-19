package improving.hitachi.utils;

import improving.hitachi.options.MenuOptionsEnum;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author EduardoGallegos<JuanGallegos at improving.hitachi>
 */
public class Utils {

    public Optional<MenuOptionsEnum> getUserMenuOption() {
        Optional<MenuOptionsEnum> optional = Optional.empty();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String optionString = StringUtils.trimToEmpty(bufferedReader.readLine());
            int intOption = Integer.parseInt(optionString);
            optional = MenuOptionsEnum.getByOption(intOption);
        } catch (NumberFormatException nfe) {
            System.out.println("Option is not valid: " + nfe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
            optional = Optional.ofNullable(MenuOptionsEnum.EXIT);
        }
        return optional;
    }

    public String getFirstName(Console console) {
        return getName(console, "First");
    }

    public String getLastName(Console console) {
        return getName(console, "Last");
    }

    private String getName(Console console, String typeName) {
        String name = "";
        if (console != null) {
            String errorName = typeName + " Name needs to have between 1 and 50 characters.";
            boolean isValidName = false;
            String nameMessage = "Please enter the " + typeName + " Name: ";
            int nameLength;
            while (!isValidName) {
                name = console.readLine(nameMessage);
                nameLength = name.length();
                if (nameLength >= 1 && nameLength <= 50) {
                    break;
                } else {
                    System.out.println(errorName);
                }
            }
        }
        return name;
    }

    public Optional<String> getUserID() {
        String message = "Please enter user ID: ";
        String errorMessage = "Value is not numeric.";
        String userID = null;

        Console console = System.console();
        boolean isValidNumber = false;
        while (!isValidNumber) {
            userID = console.readLine(message);
            if (StringUtils.isNumeric(userID)) {
                break;
            } else {
                System.out.println(errorMessage);
            }
        }
        return Optional.ofNullable(userID);
    }

}
