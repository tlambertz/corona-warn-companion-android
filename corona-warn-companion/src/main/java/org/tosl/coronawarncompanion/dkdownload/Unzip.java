/*
 * Corona-Warn-Companion. An app that shows COVID-19 Exposure Notifications details.
 * Copyright (C) 2020  Michael Huebler <corona-warn-companion@tosl.org> and other contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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