package com.google.android.gms.dynamic;

final class zaa implements OnDelegateCreatedListener<T> {
    private final /* synthetic */ DeferredLifecycleHelper zari;

    zaa(DeferredLifecycleHelper deferredLifecycleHelper) {
        this.zari = deferredLifecycleHelper;
    }

    public final void onDelegateCreated(T t) {
        this.zari.zare = t;
        t = this.zari.zarg.iterator();
        while (t.hasNext()) {
            ((zaa) t.next()).zaa(this.zari.zare);
        }
        this.zari.zarg.clear();
        this.zari.zarf = null;
    }
}
