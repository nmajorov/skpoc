//Source file: C:\\Data\\ClearCaseViews\\WILSON_SnapShotView\\tde\\XService\\Product\\ch\\skyguide\\XService\\Interfaces\\Asterix\\Structure\\Message.java

package ch.skyguide.communication.rdps.asterix.structure;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import ch.skyguide.message.structure.AbstractGenericUdpMessage;
import ch.skyguide.message.structure.GenericDataBlock;

/**
 * Models a binary message within the binary message format protocol.
 * 
 * @author R. Wilson
 * @version 0.1
 */
public class AsterixMessage
extends AbstractGenericUdpMessage
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /**
     * 
     */
    private static final long serialVersionUID = 7408475241073670237L;

    /***************************************************************************
    * Public defined constructor taking the data blocks contained in the 
    * message.
    * 
    * @param    dataBlocks
    *           The one to n data blocks contained in the message.
    *           
    */
    public AsterixMessage(List<GenericDataBlock> dataBlocks) 
    {
        super(dataBlocks);    
    }
    
    //--------------------------------------------------------------------------
    // public member functions

//    /***************************************************************************
//    * Creates and returns a copy of this object.
//    * The method performs a "deep copy" of this object.
//    * By convention, the object returned by this method should be independent
//    * of this object (which is being cloned). Typically, this means copying any
//    * mutable objects that comprise the internal "deep structure" of the object
//    * being cloned and replacing the references to these objects with
//    * references to the copies.
//    * 
//    * See also description of class <code>Object</code>.
//    * @see java.lang.Object
//    * @see java.lang.Cloneable
//    * 
//    * @return   A "deep copy" of this object.
//    */
//    @Override
//    public AsterixMessage
//    clone()
//    {
//        //----------------------------------------------------------------------
//        // Obtain an object by invoking the base class' clone method.
//        AsterixMessage clonedMessage;
//        
//        try {
//            clonedMessage = (AsterixMessage)super.clone();
//        }
//        catch (CloneNotSupportedException e) {
//            
//            throw new UnsupportedOperationException(e.getMessage());
//        }
//
//        //----------------------------------------------------------------------
//        // Copy attributes.
//        clonedMessage.m_dataBlocks = new ArrayList<AsterixDataBlock>();
//        
//        for(AsterixDataBlock dataBlock : m_dataBlocks) {
//            
//            clonedMessage.m_dataBlocks.add(dataBlock.clone());
//        }
//
//        return clonedMessage;
//    }
    
    /***************************************************************************
    * Decoding of the in buffer into messages.
    * 
    * @param    inBuffer
    *           The <code>ByteBuffer</code> containing a message to be decoded.
    *           
    *           
    */
    @Override
    public void decode(ByteBuffer inBuffer) 
    {
        int fieldLengthIndicator = 0;
            
        while(inBuffer.hasRemaining() && 
              inBuffer.remaining() >= 3 && 
              inBuffer.remaining() >=  fieldLengthIndicator) {
        
            inBuffer.mark();
            // This is only used to move along in the buffer ignoring the
            // category.
            fieldLengthIndicator = (short)(((inBuffer.get())<<24)>>>24);
            // Extract two bytes and deposit in an integer.
            fieldLengthIndicator = (((inBuffer.getShort())<<16)>>>16);
            
            // Restart the buffer.
            inBuffer.reset();
        
            // Calculate Data block length and decode blocks.
            if(inBuffer.remaining() >=  fieldLengthIndicator) {
                
                ByteBuffer dataBlockBuffer = inBuffer.slice();
                dataBlockBuffer.limit(fieldLengthIndicator);
                m_dataBlocks.
                add(AsterixDataBlock.decode(dataBlockBuffer));
                
                inBuffer.
                position(inBuffer.position() + fieldLengthIndicator);
                fieldLengthIndicator = 0;
            }
        }
    }
    
    /***************************************************************************
    * Decoding of the data channel into messages.
    * 
    * @param    inChannel
    */
    @Override
    public void decode(ReadableByteChannel inChannel) 
    {
        ByteBuffer readBuffer = ByteBuffer.allocate(READ_BUFFER_LENGTH);
        ByteBuffer workingBuffer = ByteBuffer.allocate(0);
        
        try {
            
            int fieldLengthIndicator = 0;
            
            while(inChannel.read(readBuffer) != -1) {
                
                readBuffer.flip(); // sets limit to position and position to zero.
                
                // Store old working buffer, calculate new working buffer, 
                // allocate and concationate.
                ByteBuffer tempBuffer = workingBuffer;
                workingBuffer = 
                ByteBuffer.allocate(tempBuffer.remaining() + 
                                    readBuffer.remaining());
                
                workingBuffer.put(tempBuffer);
                workingBuffer.put(readBuffer);
                workingBuffer.flip();

                while(workingBuffer.hasRemaining() && 
                      workingBuffer.remaining() >= 3 && 
                      workingBuffer.remaining() >=  fieldLengthIndicator) {
                
                    workingBuffer.mark();
                    // This is only used to move along in the buffer ignoring the
                    // category.
                    fieldLengthIndicator = (short)(((workingBuffer.get())<<24)>>>24);
                    // Extract two bytes and deposit in an integer.
                    fieldLengthIndicator = (((workingBuffer.getShort())<<16)>>>16);
                    
                    // Restart the buffer.
                    workingBuffer.reset();
                
                    // Calculate Data block length and decode blocks.
                    if(workingBuffer.remaining() >=  fieldLengthIndicator) {
                        
                        ByteBuffer dataBlockBuffer = workingBuffer.slice();
                        dataBlockBuffer.limit(fieldLengthIndicator);
                        m_dataBlocks.
                        add(AsterixDataBlock.decode(dataBlockBuffer));
                        
                        workingBuffer.
                        position(workingBuffer.position() + fieldLengthIndicator);
                        fieldLengthIndicator = 0;
                    }
                }
                
                readBuffer.clear(); // clears the buffer for the next read.
            }
        }
        catch(IOException exception) {
            
            exception.printStackTrace(System.err);
        }     
    }

    /***************************************************************************
    * @see ch.skyguide.message.Message#getType()
    */
    @Override
    public String
    getType()
    {
        return RADAR_MESSAGE_TYPE;
    }

    //--------------------------------------------------------------------------
    // public static member functions

    //--------------------------------------------------------------------------
    // protected constructors
    /***************************************************************************
    * Protected default constructor to prevent instantiation from the outside of
    * the package.
    * 
    */
    protected AsterixMessage() 
    {
        // There is nothing to do.      
    }
    
    //--------------------------------------------------------------------------
    // protected member functions

    //--------------------------------------------------------------------------
    // protected static member functions

    //--------------------------------------------------------------------------
    // protected data members
    
    //--------------------------------------------------------------------------
    // protected static data members

    //--------------------------------------------------------------------------
    // package constructors

    //--------------------------------------------------------------------------
    // package member functions

    //--------------------------------------------------------------------------
    // package static member functions

    //--------------------------------------------------------------------------
    // package data members

    //--------------------------------------------------------------------------
    // package static data members

    //--------------------------------------------------------------------------
    // private constructors

    //--------------------------------------------------------------------------
    // private member functions

    //--------------------------------------------------------------------------
    // private static member functions

    //--------------------------------------------------------------------------
    // private data members

    /**
    * The type of the message.
    */
    private static final String RADAR_MESSAGE_TYPE = "Radar Message";

    /**
    * The read buffer chuck that are read into the buffers.    
    */
    private final static int READ_BUFFER_LENGTH = 256;

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    
}
