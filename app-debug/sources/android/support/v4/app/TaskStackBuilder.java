package android.support.v4.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.Iterator;

public final class TaskStackBuilder implements Iterable<Intent> {
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents = new ArrayList();
    private final Context mSourceContext;

    public interface SupportParentable {
        @Nullable
        Intent getSupportParentActivityIntent();
    }

    public android.support.v4.app.TaskStackBuilder addParentStack(android.content.ComponentName r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:11:0x002f in {5, 7, 10} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r4.mIntents;
        r0 = r0.size();
        r1 = r4.mSourceContext;	 Catch:{ NameNotFoundException -> 0x0021 }
        r1 = android.support.v4.app.NavUtils.getParentActivityIntent(r1, r5);	 Catch:{ NameNotFoundException -> 0x0021 }
    L_0x000c:
        if (r1 == 0) goto L_0x001f;	 Catch:{ NameNotFoundException -> 0x0021 }
    L_0x000e:
        r2 = r4.mIntents;	 Catch:{ NameNotFoundException -> 0x0021 }
        r2.add(r0, r1);	 Catch:{ NameNotFoundException -> 0x0021 }
        r2 = r4.mSourceContext;	 Catch:{ NameNotFoundException -> 0x0021 }
        r3 = r1.getComponent();	 Catch:{ NameNotFoundException -> 0x0021 }
        r2 = android.support.v4.app.NavUtils.getParentActivityIntent(r2, r3);	 Catch:{ NameNotFoundException -> 0x0021 }
        r1 = r2;
        goto L_0x000c;
        return r4;
    L_0x0021:
        r1 = move-exception;
        r2 = "TaskStackBuilder";
        r3 = "Bad ComponentName while traversing activity parent metadata";
        android.util.Log.e(r2, r3);
        r2 = new java.lang.IllegalArgumentException;
        r2.<init>(r1);
        throw r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.TaskStackBuilder.addParentStack(android.content.ComponentName):android.support.v4.app.TaskStackBuilder");
    }

    private TaskStackBuilder(Context a) {
        this.mSourceContext = a;
    }

    @NonNull
    public static TaskStackBuilder create(@NonNull Context context) {
        return new TaskStackBuilder(context);
    }

    @Deprecated
    public static TaskStackBuilder from(Context context) {
        return create(context);
    }

    @NonNull
    public TaskStackBuilder addNextIntent(@NonNull Intent nextIntent) {
        this.mIntents.add(nextIntent);
        return this;
    }

    @NonNull
    public TaskStackBuilder addNextIntentWithParentStack(@NonNull Intent nextIntent) {
        ComponentName target = nextIntent.getComponent();
        if (target == null) {
            target = nextIntent.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (target != null) {
            addParentStack(target);
        }
        addNextIntent(nextIntent);
        return this;
    }

    @NonNull
    public TaskStackBuilder addParentStack(@NonNull Activity sourceActivity) {
        Intent parent = null;
        if (sourceActivity instanceof SupportParentable) {
            parent = ((SupportParentable) sourceActivity).getSupportParentActivityIntent();
        }
        if (parent == null) {
            parent = NavUtils.getParentActivityIntent(sourceActivity);
        }
        if (parent != null) {
            ComponentName target = parent.getComponent();
            if (target == null) {
                target = parent.resolveActivity(this.mSourceContext.getPackageManager());
            }
            addParentStack(target);
            addNextIntent(parent);
        }
        return this;
    }

    @NonNull
    public TaskStackBuilder addParentStack(@NonNull Class<?> sourceActivityClass) {
        return addParentStack(new ComponentName(this.mSourceContext, sourceActivityClass));
    }

    public int getIntentCount() {
        return this.mIntents.size();
    }

    @Deprecated
    public Intent getIntent(int index) {
        return editIntentAt(index);
    }

    @Nullable
    public Intent editIntentAt(int index) {
        return (Intent) this.mIntents.get(index);
    }

    @Deprecated
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }

    public void startActivities() {
        startActivities(null);
    }

    public void startActivities(@Nullable Bundle options) {
        if (this.mIntents.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
        }
        ArrayList arrayList = this.mIntents;
        Intent[] intents = (Intent[]) arrayList.toArray(new Intent[arrayList.size()]);
        intents[0] = new Intent(intents[0]).addFlags(268484608);
        if (!ContextCompat.startActivities(this.mSourceContext, intents, options)) {
            Intent topIntent = new Intent(intents[intents.length - 1]);
            topIntent.addFlags(268435456);
            this.mSourceContext.startActivity(topIntent);
        }
    }

    @Nullable
    public PendingIntent getPendingIntent(int requestCode, int flags) {
        return getPendingIntent(requestCode, flags, null);
    }

    @Nullable
    public PendingIntent getPendingIntent(int requestCode, int flags, @Nullable Bundle options) {
        if (this.mIntents.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
        }
        ArrayList arrayList = this.mIntents;
        Intent[] intents = (Intent[]) arrayList.toArray(new Intent[arrayList.size()]);
        intents[0] = new Intent(intents[0]).addFlags(268484608);
        if (VERSION.SDK_INT >= 16) {
            return PendingIntent.getActivities(this.mSourceContext, requestCode, intents, flags, options);
        }
        return PendingIntent.getActivities(this.mSourceContext, requestCode, intents, flags);
    }

    @NonNull
    public Intent[] getIntents() {
        Intent[] intents = new Intent[this.mIntents.size()];
        if (intents.length == 0) {
            return intents;
        }
        intents[0] = new Intent((Intent) this.mIntents.get(0)).addFlags(268484608);
        for (int i = 1; i < intents.length; i++) {
            intents[i] = new Intent((Intent) this.mIntents.get(i));
        }
        return intents;
    }
}
