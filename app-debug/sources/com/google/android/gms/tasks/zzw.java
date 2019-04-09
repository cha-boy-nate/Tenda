package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class zzw implements Continuation<Void, List<TResult>> {
    private final /* synthetic */ Collection zzae;

    zzw(Collection collection) {
        this.zzae = collection;
    }

    public final /* synthetic */ Object then(@NonNull Task task) throws Exception {
        if (this.zzae.size() == null) {
            return Collections.emptyList();
        }
        task = new ArrayList();
        for (Task result : this.zzae) {
            task.add(result.getResult());
        }
        return task;
    }
}
