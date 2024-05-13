package objectData.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import objectData.model.BookModel;

import java.util.List;

@Getter
public class ResponseAccountSucces {

    @JsonProperty("userID")
    private String userID;
    @JsonProperty ("username")
    private String username;
    @JsonProperty ("books")
    private List<BookModel> books;


}
