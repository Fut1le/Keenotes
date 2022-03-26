package com.ruya.takimi.keenotes.models;
import androidx.annotation.NonNull;


public class AcceptedValue {

    public boolean accepted;

    public <T extends AcceptedValue> T withAccepted(@NonNull final boolean id) {
        this.accepted = id;
        return (T) this;
    }

}
