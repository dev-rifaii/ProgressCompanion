package personal.progresscompaninon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import personal.progresscompaninon.model.Note;
import personal.progresscompaninon.model.NoteType;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.repository.NoteRepository;
import personal.progresscompaninon.repository.UserRepository;

import java.util.ArrayList;
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

    public Note editNote(Note newNote, Long id) {
        noteRepository.editNoteById(newNote, id);
        return newNote;
    }

    public List<Note> getNotesByTopic(String topic) {
        List<Note> notes = noteRepository.getNoteByTopic(topic, getLoggedInUser().getId());
        return notes;
    }

    public boolean validateNote(Note note) {
        return note.getContent() != null
                && validateNoteType(note.getNoteType())
                && note.getDate() != null;
    }

    public boolean validateNoteType(NoteType type) {
        return type.equals(NoteType.NOTE) || type.equals(NoteType.SKILL);
    }

    public User getLoggedInUser() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
