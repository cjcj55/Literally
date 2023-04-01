package com.cjcj55.literallynot;

import java.util.Arrays;

public class CircularByteBuffer {

    private byte[] buffer;
    private int writeIndex;
    private int readIndex;
    private boolean wrapped;

    public CircularByteBuffer(int bufferSize) {
        buffer = new byte[bufferSize];
    }

    public synchronized int write(byte[] data, int offset, int length) {
        int bytesWritten = 0;
        int remainingSpace = getRemainingSpace();
        if (length > remainingSpace) {
            int startIndex = writeIndex + remainingSpace;
            int endIndex = startIndex + length - remainingSpace;
            System.arraycopy(data, offset, buffer, startIndex, endIndex - startIndex);
            System.arraycopy(data, offset + endIndex - startIndex, buffer, 0, length - endIndex + startIndex);
            writeIndex = endIndex - buffer.length;
            wrapped = true;
            bytesWritten = length;
        } else {
            int endIndex = writeIndex + length;
            if (endIndex < buffer.length) {
                System.arraycopy(data, offset, buffer, writeIndex, length);
                writeIndex = endIndex;
                bytesWritten = length;
            } else {
                int startIndex = endIndex - buffer.length;
                System.arraycopy(data, offset, buffer, writeIndex, length - startIndex);
                System.arraycopy(data, offset + length - startIndex, buffer, 0, startIndex);
                writeIndex = startIndex;
                wrapped = true;
                bytesWritten = length;
            }
        }
        return bytesWritten;
    }


    public synchronized int read(byte[] data, int offset, int length) {
        int availableData = getAvailableData();
        if (length > availableData) {
            length = availableData;
        }
        if (length > 0) {
            if (readIndex + length <= buffer.length) {
                System.arraycopy(buffer, readIndex, data, offset, length);
                readIndex += length;
                if (readIndex == buffer.length) {
                    readIndex = 0;
                    wrapped = false;
                }
            } else {
                int startIndex = readIndex;
                int endIndex = startIndex + length - (buffer.length - startIndex);
                System.arraycopy(buffer, startIndex, data, offset, buffer.length - startIndex);
                System.arraycopy(buffer, 0, data, offset + buffer.length - startIndex, endIndex);
                readIndex = endIndex;
                wrapped = false;
            }
        }
        return length;
    }

    public synchronized byte[] readAll() {
        int availableData = getAvailableData();
        byte[] data = new byte[availableData];
        int offset = 0;
        while (availableData > 0) {
            int bytesRead = read(data, offset, availableData);
            offset += bytesRead;
            availableData -= bytesRead;
        }
        return data;
    }


    public synchronized void clear() {
        readIndex = 0;
        writeIndex = 0;
        wrapped = false;
    }

    public synchronized int getAvailableData() {
        if (wrapped) {
            return buffer.length - readIndex + writeIndex;
        } else {
            return writeIndex - readIndex;
        }
    }

    public synchronized int getRemainingSpace() {
        if (wrapped) {
            return readIndex - writeIndex;
        } else {
            return buffer.length - writeIndex;
        }
    }

}
