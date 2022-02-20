package personal.progresscompaninon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import personal.progresscompaninon.model.Note;
import personal.progresscompaninon.model.NoteType;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.repository.NoteRepository;
import personal.progresscompaninon.repository.UserRepository;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;


    public void addNote(Note note) {
        note.setUser(getLoggedInUser());
        if (validateNote(note)) {
            noteRepository.save(note);
        }
    }

    public List<Note> getNotes() {
        return getLoggedInUser().getNotes();
    }

    public Note editNote(Note newNote, Long id) {
        noteRepository.editNoteById(newNote, id);
        return newNote;
    }

    public List<Note> getNotesByTopic(String topic) {
        List<Note> notes = noteRepository.getNotesByTopicAndUser(topic, getLoggedInUser());
        return notes;
    }

    public boolean validateNote(Note note) {
        return note.getContent() != null
                && validateNoteType(note.getNoteType())
                && note.getDate() != null;
    }

    public boolean validateNoteType(NoteType type) {
        return type.equals(NoteType.NOTE)
                || type.equals(NoteType.SKILL);
    }

    public User getLoggedInUser() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
