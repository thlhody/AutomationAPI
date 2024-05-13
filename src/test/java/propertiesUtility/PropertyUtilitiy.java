package propertiesUtility;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertyUtilitiy {

    private Properties properties;

    public PropertyUtilitiy(String path) {
        loadFile(path);
    }

    @SneakyThrows(Exception.class)
    private void loadFile(String path) {
        properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/" + path + ".properties");
        properties.load(fileInputStream);
    }

    public HashMap<String, String> getAllData() {
        HashMap<String, String> testData = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            testData.put(key, properties.getProperty(key));
        }
        return testData;
    }
}
