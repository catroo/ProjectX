package am.util.opentype;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Reads a TrueType font file into a RandomAccessFile.
 * Created by Alex on 2018/9/5.
 */
public class FileOpenTypeReader implements OpenTypeReader {

    private final RandomAccessFile mFile;

    @SuppressWarnings("all")
    public FileOpenTypeReader(File font) throws IOException {
        mFile = new RandomAccessFile(font, "r");
    }

    @Override
    public void seek(long pos) throws IOException {
        mFile.seek(pos);
    }

    @Override
    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        final long pos = mFile.getFilePointer();
        final long len = mFile.length();
        long np = pos + n;
        if (np > len) {
            np = len;
        }
        mFile.seek(np);
        /* return the actual number of bytes skipped */
        return np - pos;
    }

    @Override
    public long getPointer() throws IOException {
        return mFile.getFilePointer();
    }

    @Override
    public long length() throws IOException {
        return mFile.length();
    }

    @Override
    public int read() throws IOException {
        return mFile.read();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return mFile.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return mFile.readShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return mFile.readUnsignedShort();
    }

    @Override
    public int readUnsignedInt24() throws IOException {
        final int ch1 = read();
        final int ch2 = read();
        final int ch3 = read();
        if ((ch1 | ch2 | ch3) < 0)
            throw new EOFException();
        return ((ch1 << 16) + (ch2 << 8) + ch3);
    }

    @Override
    public int readInt() throws IOException {
        return mFile.readInt();
    }

    @Override
    public int readUnsignedInt() throws IOException {
        return Math.abs(mFile.readInt());
    }

    @Override
    public float readFloat() throws IOException {
        return mFile.readFloat();
    }

    @Override
    public float readFloat2Dot14() throws IOException {
        final int ch1 = read();
        final int ch2 = read();
        final int f2 = (ch1 >> 6) & 0x3;
        final int dot14 = (((ch1 << 2) & 0xff) << 6) + ch2;
        if (f2 == 3)
            // 11
            return -1 + dot14 / 16384.0f;
        else if (f2 == 2)
            // 10
            return -2 + dot14 / 16384.0f;
        else
            // 01 or 00
            return f2 + dot14 / 16384.0f;
    }

    @Override
    public long readLong() throws IOException {
        return mFile.readLong();
    }

    @Override
    public void close() throws IOException {
        mFile.close();
    }
}
