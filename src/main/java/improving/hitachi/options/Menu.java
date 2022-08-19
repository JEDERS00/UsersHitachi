package improving.hitachi.options;

import improving.hitachi.utils.Utils;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author EduardoGallegos<JuanGallegos at improving.hitachi>
 */
public class Menu {

    private final String HTTPS_DOMAIN = "http://localhost:8080/";

    public void print() {
        String breakText = "----------------------------------------------------------------------------------------------";
        System.out.println("\n\n");
        System.out.println(breakText);
        System.out.println("--------------------------------------- HITACHI USERS --------------------------------------");
        System.out.println();
        System.out.println("Hi, welcome to the 1st version of Hitachi users.");
        System.out.println(breakText);

        MenuOptionsEnum moe = getMenuOption();
        while (MenuOptionsEnum.EXIT != moe) {
            switch (moe) {
                case ADD_USER:
                    addUser();
                    break;
                case RETURN_USER_BY_ID:
                    returnUserByID();
                    break;
                case RETURN_USER_LIST:
                    returnUsers();
                    break;
                case DELETE_USER_BY_ID:
                    deleteUserByID();
                    break;
            }
            moe = getMenuOption();
        }
        System.exit(0);
    }

    private MenuOptionsEnum getMenuOption() {
        String openingParentheses = "(";
        String closingParentheses = ") ";
        String lineBreak = "\n";
        String pleaseSelectOption = "Please select an option:";

        StringBuilder optionsSB = new StringBuilder(304);
        //This loop is to add all the options to show later
        for (MenuOptionsEnum menuOptionsEnum : MenuOptionsEnum.values()) {
            optionsSB.append(openingParentheses).append(menuOptionsEnum.getOption()).append(closingParentheses)
                    .append(menuOptionsEnum.getDescription()).append(lineBreak);
        }
        String options = optionsSB.toString();

        Optional<MenuOptionsEnum> optional = Optional.empty();
        Utils utils = new Utils();
        while (!optional.isPresent()) {
            System.out.println(lineBreak);
            System.out.println(pleaseSelectOption);
            System.out.println(options);
            optional = utils.getUserMenuOption();
        }
        return optional.get();
    }

    private void addUser() {
        System.out.println("Fist Name and Last Name needs to have between 1 and 50 characters.");
        Console console = System.console();

        Utils utils = new Utils();
        String firstName = utils.getFirstName(console);
        String lastName = utils.getLastName(console);

        try {
            JSONObject userData = new JSONObject();
            userData.put("firstName", firstName);
            userData.put("lastName", lastName);

            StringEntity entity = new StringEntity(userData.toString());
            HttpPost httpPost = new HttpPost(HTTPS_DOMAIN + "addUser");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(entity);
            try ( CloseableHttpClient httpClient = HttpClients.createDefault();  
                    CloseableHttpResponse chr = httpClient.execute(httpPost);) {
                System.out.println(EntityUtils.toString(chr.getEntity()));
            } catch (IOException ioe) {
                System.out.println("Error: " + ioe.getMessage());
            }
        } catch (JSONException jsone) {
            System.out.println("Error menu add user: " + jsone.getMessage());
        } catch (UnsupportedEncodingException uee) {
            System.out.println("Error menu add user: " + uee.getMessage());
        }
    }

    private void returnUserByID() {
        returnUsers(new Utils().getUserID());
    }

    private void returnUsers() {
        returnUsers(Optional.empty());
    }

    private void returnUsers(Optional<String> userID) {
        String userId = "";
        if (userID.isPresent()) {
            userId = "/" + userID.get();
        }
        HttpGet httpGet = new HttpGet(HTTPS_DOMAIN + "getUsers" + userId);
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();  
                CloseableHttpResponse chr = httpClient.execute(httpGet);) {
            System.out.println(EntityUtils.toString(chr.getEntity()));
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }

    private void deleteUserByID() {
        Optional<String> userID = new Utils().getUserID();
        List nameValuePairs = new ArrayList();
        nameValuePairs.add(new BasicNameValuePair("USER_ID", userID.get()));
        HttpPost httpPost = new HttpPost(HTTPS_DOMAIN + "deleteUser");
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();  
                CloseableHttpResponse chr = httpClient.execute(httpPost);) {
            System.out.println(EntityUtils.toString(chr.getEntity()));
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }

}
