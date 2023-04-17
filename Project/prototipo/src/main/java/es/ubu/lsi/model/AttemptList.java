package es.ubu.lsi.model;

import java.util.List;

public class AttemptList{
    private List<Attempt> attempts;
    private List<Object> warnings;


    public List<Attempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<Attempt> attempts) {
        this.attempts = attempts;
    }

    public List<Object> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }
}

