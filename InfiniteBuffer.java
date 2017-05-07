import java.util.*;
import java.io.*;


class InfiniteBuffer {

  // points to the beginning of the buffer
  private int base;

  // points to the next place to write in the buffer
  private int next;

  // these should not change after initialization
  private byte[] buffer;
  private int bufferSize;

    
  public InfiniteBuffer() {
    // the number 10000 is about 19-20 data chunks (one data chunk
    // for each packet) i think.
    this(10000);
  }


/**
 * Initializes the buffer using the specified size. 
 * @param bufferSize
 */
  public InfiniteBuffer(int bufferSize) {
    this.bufferSize = bufferSize;
    buffer = new byte[bufferSize];
    base = 0;
    next = 0;
  }

  /**
   * Gets the current size of the buffer.
   * @return
   */
  public final int getBufferSize(){
    return bufferSize;
  }

  /**
   * Advances the base and next parameters by an offset, keeping the distance between the same.
   * @param offset
   */
  public synchronized void setOffset(int offset){
    base+=offset;
    next+=offset;
  }

  /**
   * returns the base parameter.
   * @return
   */
  public final int getBase(){
    return base;
  }

  /**
   * advances the base parameter to the target value. next says the same.
   * @param newBase
   */
  public synchronized void advanceTo(int newBase){
    base = newBase;
  }

  /**
   * returns the next parameter.
   * @return
   */
  public final int getNext(){
    return next;
  }

  /**
   * for every item in data, place that item in the next available section.
   * may write over past data?
   * next is set to the position after the newly inserted data
   * 
   * @param data
   * @param dataOffset
   * @param len
   */
  public synchronized void append(byte[] data, int dataOffset, int len){
    // loop through, circularly, adding data to the end of the
    // buffer....
    for (int i=0; i<len; i++) {
      buffer[(next+i)%bufferSize] = data[i+dataOffset];
    }

    next = next+len;
  }
    
  /**
   * writes data to the appropriate buffer
   * @param data
   * @param baseOrigin
   * @param len
   */
  public synchronized void copyOut(byte[] data, int baseOrigin, int len){
    for(int i=0;i<len;i++)
      data[i] = buffer[(baseOrigin+i)%bufferSize];
  }

  /**
   * advances the base parameter by the given amount. next stays the same. 
   * @param addToBase
   */
  public synchronized void advance(int addToBase){
    base += addToBase;
  }
}
