package org.tosl.coronawarncompanion.dkdownload;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {
    private static final String TAG = "Unzip";

    public static byte[] getUnzippedBytesFromZipFileBytes(byte[] zipFileBytes, String filename) throws IOException {
        //Log.d(TAG, "Zipped Data: "+zipFileBytes);
        byte[] tmpBuffer;
        byte[] result = null;
        ByteArrayInputStream byteStream = new ByteArrayInputStream(zipFileBytes);
        ZipInputStream zis = new ZipInputStream(byteStream);
        ZipEntry zipEntry;
        zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.getName().equals(filename)) {
                int bufferLen = 10*zipFileBytes.length;  //TODO: find a nicer way to do this. Unfortunately, zipEntry.getSize() returns -1.
                //Log.d(TAG, "Unzipping... Buffer Length: "+bufferLen);
                tmpBuffer = new byte[bufferLen];
                int pos = 0;
                int bytesRead = 0;
                while (pos < bufferLen && bytesRead != -1) {
                    bytesRead = zis.read(tmpBuffer, pos, bufferLen-pos);
                    if (bytesRead>0) {
                        pos += bytesRead;
                    }
                }
                result = Arrays.copyOf(tmpBuffer, pos);
                Log.d(TAG, "Unzipped file. Length: "+result.length);
                //noinspection UnusedAssignment
                tmpBuffer = null;
                break;
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return result;
    }
}