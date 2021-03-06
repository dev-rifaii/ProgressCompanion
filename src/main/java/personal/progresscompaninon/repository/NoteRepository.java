package personal.progresscompaninon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import personal.progresscompaninon.model.Note;
import personal.progresscompaninon.model.User;

import java.util.List;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> getNotesByTopicAndUser(String topic, User user);

    @Modifying
    @Query(value = "Update Note n " +
            "SET n.topic = :#{#note.topic}, " +
            "n.content = :#{#note.content} " +
            "WHERE n.id = :id")
    void editNoteById(@Param("note") Note note, @Param("id") Long id);
}
