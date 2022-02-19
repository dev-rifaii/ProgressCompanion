package personal.progresscompaninon.controller;

import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.progresscompaninon.model.Note;
import personal.progresscompaninon.service.NoteService;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @GetMapping
    public List<Note> notes() {
        return noteService.getLoggedInUser().getNotes();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNote(@RequestBody Note note) {

        noteService.addNote(note);
        return new ResponseEntity<>("Note added", HttpStatus.OK);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Note> editNote(@RequestBody Note newNote, @PathVariable Long id) {
        noteService.editNote(newNote, id);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }


    @GetMapping("/get/{topic}")
    public List<Note> getNotesByTopic(@PathVariable String topic) {
        return noteService.getNotesByTopic(topic);
    }

}





