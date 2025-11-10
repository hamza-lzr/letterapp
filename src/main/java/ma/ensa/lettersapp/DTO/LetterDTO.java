package ma.ensa.lettersapp.DTO;

import lombok.Data;
import ma.ensa.lettersapp.entities.Letter;


@Data
public class LetterDTO {

    private Long id;
    private String recipientUsername;
    private String title;
    private String content;
    private String senderUsername;
    private String sentAt;


}
