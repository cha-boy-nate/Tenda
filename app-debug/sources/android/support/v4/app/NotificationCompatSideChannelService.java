package android.support.v4.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel.Stub;

public abstract class NotificationCompatSideChannelService extends Service {

    private class NotificationSideChannelStub extends Stub {
        NotificationSideChannelStub() {
        }

        public void notify(String packageName, int id, String tag, Notification notification) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.notify(packageName, id, tag, notification);
            } finally {
                restoreCallingIdentity(idToken);
            }
        }

        public void cancel(String packageName, int id, String tag) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancel(packageName, id, tag);
            } finally {
                restoreCallingIdentity(idToken);
            }
        }

        public void cancelAll(String packageName) {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), packageName);
            long idToken = clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancelAll(packageName);
            } finally {
                restoreCallingIdentity(idToken);
            }
        }
    }

    public abstract void cancel(String str, int i, String str2);

    public abstract void cancelAll(String str);

    void checkPermission(int r6, java.lang.String r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:8:0x0037 in {4, 5, 7} preds:[]
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
        r5 = this;
        r0 = r5.getPackageManager();
        r0 = r0.getPackagesForUid(r6);
        r1 = r0.length;
        r2 = 0;
    L_0x000a:
        if (r2 >= r1) goto L_0x0018;
    L_0x000c:
        r3 = r0[r2];
        r4 = r3.equals(r7);
        if (r4 == 0) goto L_0x0015;
    L_0x0014:
        return;
    L_0x0015:
        r2 = r2 + 1;
        goto L_0x000a;
    L_0x0018:
        r0 = new java.lang.SecurityException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "NotificationSideChannelService: Uid ";
        r1.append(r2);
        r1.append(r6);
        r2 = " is not authorized for package ";
        r1.append(r2);
        r1.append(r7);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.NotificationCompatSideChannelService.checkPermission(int, java.lang.String):void");
    }

    public abstract void notify(String str, int i, String str2, Notification notification);

    public IBinder onBind(Intent intent) {
        if (!intent.getAction().equals(NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL) || VERSION.SDK_INT > 19) {
            return null;
        }
        return new NotificationSideChannelStub();
    }
}
