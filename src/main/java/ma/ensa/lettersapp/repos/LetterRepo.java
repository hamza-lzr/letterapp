package ma.ensa.lettersapp.repos;

import ma.ensa.lettersapp.entities.Letter;
import ma.ensa.lettersapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterRepo extends JpaRepository<Letter, Long> {

    List<Letter> findBySenderOrRecipientOrderBySentAtDesc(User sender, User recipient);

    List<Letter> findBySender(User sender);
}
