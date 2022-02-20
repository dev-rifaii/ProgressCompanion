package personal.progresscompaninon.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.progresscompaninon.model.Note;
import personal.progresscompaninon.service.NoteService;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public List<Note> notes(@RequestParam(required = false) String topic) {
        if (topic != null) {
            return noteService.getNotesByTopic(topic);
        }
        return noteService.getNotes();
    }

    @PostMapping
    public ResponseEntity<?> addNote(@RequestBody Note note) {

        noteService.addNote(note);
        return new ResponseEntity<>("Note added", HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Note> editNote(@RequestBody Note newNote, @PathVariable Long id) {
        noteService.editNote(newNote, id);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }
}





