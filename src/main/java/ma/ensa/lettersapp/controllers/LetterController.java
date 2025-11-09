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
    public ResponseEntity<Letter> sendLetter(@RequestBody LetterDTO letterDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Letter savedLetter = letterService.sendLetter(letterDto, userDetails.getUsername());
            return new ResponseEntity<>(savedLetter, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/history")
    public ResponseEntity<List<Letter>> getUserHistory(@AuthenticationPrincipal UserDetails userDetails) {
        List<Letter> letters = letterService.getUserHistory(userDetails.getUsername());
        return ResponseEntity.ok(letters);
    }

    @CrossOrigin(origins = "*")
    @PatchMapping("/{letterId}/read")
    public ResponseEntity<Letter> markAsRead(@PathVariable Long letterId, @AuthenticationPrincipal UserDetails userDetails) {
        return letterService.markAsRead(letterId, userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
