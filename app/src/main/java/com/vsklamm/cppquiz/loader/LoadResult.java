package com.vsklamm.cppquiz.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vsklamm.cppquiz.utils.RequestType;

public class LoadResult<S, T> {

    @NonNull
    public final ConnectSuccessType connectSuccessType;

    @Nullable
    public final S cppStandard;

    @Nullable
    public final T questionsIds;

    @NonNull
    public final RequestType requestType;

    public final boolean updated;

    LoadResult(@NonNull final ConnectSuccessType connectSuccessType,
               @Nullable final S cppStandard, @Nullable final T data,
               @NonNull final RequestType requestType, final boolean updated) {
        this.connectSuccessType = connectSuccessType;
        this.cppStandard = cppStandard;
        this.questionsIds = data;
        this.requestType = requestType;
        this.updated = updated;
    }
}
