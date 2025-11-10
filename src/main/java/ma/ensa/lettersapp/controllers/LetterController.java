package ma.ensa.lettersapp.controllers;


import ma.ensa.lettersapp.DTO.LetterDTO;
import ma.ensa.lettersapp.entities.Letter;
import ma.ensa.lettersapp.services.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/letters")
@CrossOrigin(origins = "*")
public class LetterController {

    private final LetterService letterService;

    @Autowired
    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity sendLetter(@RequestBody LetterDTO letterDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Letter savedLetter = letterService.sendLetter(letterDto, userDetails.getUsername());
            return new ResponseEntity<>(savedLetter, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/history")
    public ResponseEntity<List<LetterDTO>> getUserHistory(@AuthenticationPrincipal UserDetails userDetails) {
        List<Letter> letters = letterService.getUserHistory(userDetails.getUsername());
        List<LetterDTO> lettersDTOs = letters.stream().map(letter -> {
            LetterDTO dto = new LetterDTO();
            dto.setId(letter.getId());
            dto.setSenderUsername(letter.getSender().getUsername());
            dto.setRecipientUsername(letter.getRecipient().getUsername());
            dto.setTitle(letter.getTitle());
            dto.setContent(letter.getContent());
            dto.setSentAt(letter.getSentAt().toString());
            return dto;
        }).toList();
        return ResponseEntity.ok(lettersDTOs);
    }

    @CrossOrigin(origins = "*")
    @PatchMapping("/{letterId}/read")
    public ResponseEntity<Letter> markAsRead(@PathVariable Long letterId) {
        return letterService.markAsRead(letterId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/all/sender")
    public ResponseEntity<List<Letter>> getAllLetters(@AuthenticationPrincipal UserDetails userDetails) {
        List<Letter> letters = letterService.getAllLettersBySender(userDetails.getUsername());
        return ResponseEntity.ok(letters);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/all/recipient")
    public ResponseEntity<List<Letter>> getAllLettersByRecipient(@AuthenticationPrincipal UserDetails userDetails) {
        List<Letter> letters = letterService.getAllLettersByRecipient(userDetails.getUsername());
        return ResponseEntity.ok(letters);
    }
}
