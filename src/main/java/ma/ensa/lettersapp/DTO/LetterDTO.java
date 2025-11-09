package ma.ensa.lettersapp.DTO;

import lombok.Data;

@Data
public class LetterDTO {

    private String recipientUsername;
    private String title;
    private String content;

}
