package ma.ensa.lettersapp.services;

import ma.ensa.lettersapp.DTO.LetterDTO;
import ma.ensa.lettersapp.entities.Letter;
import ma.ensa.lettersapp.entities.User;
import ma.ensa.lettersapp.repos.LetterRepo;
import ma.ensa.lettersapp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class LetterService {

    private final LetterRepo letterRepository;
    private final UserRepo userRepository;

    @Autowired
    public LetterService(LetterRepo letterRepository, UserRepo userRepository) {
        this.letterRepository = letterRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Letter sendLetter(LetterDTO letterDto, String senderUsername) {
        User sender = userRepository.findByUsername(senderUsername);
        User recipient = userRepository.findByUsername(letterDto.getRecipientUsername());

        if (sender == null) {
            throw new IllegalArgumentException("Sender not found.");
        }
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient not found.");
        }
        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Cannot send a letter to yourself.");
        }

        Letter newLetter = new Letter();
        newLetter.setSender(userRepository.findByUsername(letterDto.getSenderUsername()));
        newLetter.setRecipient(userRepository.findByUsername(letterDto.getRecipientUsername()));
        newLetter.setTitle(letterDto.getTitle());
        newLetter.setContent(letterDto.getContent());
        newLetter.setSentAt(LocalDateTime.now());

        return letterRepository.save(newLetter);
    }

    public List<Letter> getUserHistory(String username) {
        User user = userRepository.findByUsername(username);
        return letterRepository.findBySender(user);
    }

    public List<Letter> getAllLettersBySender(String username) {
        User user = userRepository.findByUsername(username);
        return letterRepository.findBySender(user);

    }

    public List<Letter> getAllLettersByRecipient(String username) {
        User user = userRepository.findByUsername(username);
        return letterRepository.findBySender(user);
    }

    public Optional<Letter> markAsRead(Long letterId) {
        Letter letter = letterRepository.findById(letterId).orElse(null);
        if (letter == null) {
            return Optional.empty();
        }
        return Optional.of(letter);
    }


}