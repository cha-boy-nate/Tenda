package com.google.android.gms.maps.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public abstract class UrlTileProvider implements TileProvider {
    private final int height;
    private final int width;

    public UrlTileProvider(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public abstract URL getTileUrl(int i, int i2, int i3);

    public final Tile getTile(int i, int i2, int i3) {
        i = getTileUrl(i, i2, i3);
        if (i == 0) {
            return NO_TILE;
        }
        try {
            i3 = this.width;
            int i4 = this.height;
            i = i.openStream();
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[4096];
            while (true) {
                int read = i.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            i2 = new Tile(i3, i4, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            i2 = 0;
        }
        return i2;
    }
}
