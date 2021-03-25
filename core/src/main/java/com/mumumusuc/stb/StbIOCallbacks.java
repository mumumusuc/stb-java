package com.mumumusuc.stb;

import java.nio.ByteBuffer;

public interface StbIOCallbacks {
    int read(ByteBuffer data);  // fill 'data' with 'size' bytes.  return number of bytes actually read

    void skip(int n);       // skip the next 'n' bytes, or 'unget' the last -n bytes if negative

    int eof();              // returns nonzero if we are at end of file/data
}
