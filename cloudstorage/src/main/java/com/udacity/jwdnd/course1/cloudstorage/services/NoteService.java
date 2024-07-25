package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteMoldel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<NoteMoldel> getNotes(int userid){
        return noteMapper.getNotes(userid);
    }

    public void addNote(NoteMoldel note, int userId){
        NoteMoldel newNote = new NoteMoldel();
        newNote.setUserId(userId);
        newNote.setNoteDescription(note.getNoteDescription());
        newNote.setNoteTitle(note.getNoteTitle());

        noteMapper.insertNote(newNote);
    }

    public void updateNote(NoteMoldel note) {
        noteMapper.updateNote(note);
    }

    public void deleteNote(int noteid){
        noteMapper.deleteNote(noteid);
    }
}
