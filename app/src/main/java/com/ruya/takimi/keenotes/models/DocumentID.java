package com.ruya.takimi.keenotes.models;

import androidx.annotation.NonNull;

public class DocumentID {
    public String documentId;

    public <T extends DocumentID> T withId(@NonNull final String id){
        this.documentId=id;
        return (T)this;
    }


}
