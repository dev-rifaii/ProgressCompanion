package personal.progresscompaninon.model;

public enum NoteType {
    NOTE("N"),
    SKILL("S");

    private String code;

    NoteType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}