package com.vsklamm.cppquiz.model;

import android.support.annotation.NonNull;

public class DumpDataType<T> {

    public final int dumpVersion;

    @NonNull
    public final String cppStandard;

    @NonNull
    public final T questions;

    public DumpDataType(final int dumpVersion,
                        @NonNull final String cppStandard,
                        @NonNull final T questions) {
        this.dumpVersion = dumpVersion;
        this.cppStandard = cppStandard;
        this.questions = questions;
    }
}
