package personal.progresscompaninon.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import personal.progresscompaninon.model.Note;
import personal.progresscompaninon.model.NoteType;
import personal.progresscompaninon.repository.NoteRepository;
import personal.progresscompaninon.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    SecurityContext mockSecurityContext;
    @Mock
    Authentication authentication;

    private Validator validator;
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        noteService = new NoteService(noteRepositoryMock, userRepositoryMock);

    }

    @Test
    void shouldAddNote() {
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(mockSecurityContext);
        Note validNote = Note.builder()
                .date(LocalDate.now())
                .topic("topic")
                .content("content")
                .noteType(NoteType.NOTE)
                .build();
        Set<ConstraintViolation<Note>> violations = validator.validate(validNote);

        Assert.assertTrue(violations.isEmpty());
        noteService.addNote(validNote);
        Mockito.verify(noteRepositoryMock).save(validNote);
    }


    @Test
    void shouldCatchViolationsWhenAddingInvalidNote() {
        //Blank topic and blank content
        Note invalidNote = Note.builder()
                .date(LocalDate.now())
                .topic("")
                .content("")
                .noteType(NoteType.NOTE)
                .build();
        Set<ConstraintViolation<Note>> violations = validator.validate(invalidNote);
        Assert.assertTrue(violations.size() == 2);
    }
}