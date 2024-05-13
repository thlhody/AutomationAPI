package objectData.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


@Getter
@Setter
public class RequestAccount {

    private String userName;
    private String password;


    public RequestAccount(HashMap<String, String> testData) {
        populateObject(testData);

    }

    private void populateObject(HashMap<String, String> testData) {
        for (String key : testData.keySet()) {

            switch (key) {
                case "userName":
                    setUserName(getPrepareValued(testData.get(key)));
                    break;
                case "password":
                    setPassword(testData.get(key));
                    break;
            }
        }
    }

    private String getPrepareValued(String value){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return value+dtf.format(now);
    }



}
