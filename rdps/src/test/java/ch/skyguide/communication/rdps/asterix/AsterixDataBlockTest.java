//------------------------------------------------------------------------------
// PROJECT:
//      skyguide BEAM
// LAYER:
//      Test Framework
// COPYRIGHT:
//      Copyright (c) 1998-2007 skyguide AG, Wangen bei Dübendorf, Switzerland
//      All rights reserved.
// REVISION:
//      
//------------------------------------------------------------------------------


//------------------------------------------------------------------------------
package ch.skyguide.communication.rdps.asterix;


//------------------------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ch.skyguide.communication.rdps.asterix.categories.cat062.REForCat062;
import ch.skyguide.communication.rdps.asterix.categories.cat062.REForCat062Container;
import ch.skyguide.communication.rdps.asterix.categories.cat062.TrackVelocity;
import ch.skyguide.communication.rdps.asterix.structure.AsterixMessage;
import ch.skyguide.message.structure.GenericDataBlock;
import ch.skyguide.message.structure.GenericRecord;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.dataitem.DataItem;


/**
 * Class containing test cases for the class.
 * 
 * Make sure that the rmi registry is running on port 1099.
 *  
 * @author Ross Wilson / Marc-Antoine Galilée
 */
public class AsterixDataBlockTest
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions
    

    @Test
    public void
    test62_65_CategoryBlocks() throws Exception {
        
        // 62_65_message.bin contains:
        // - 1 category 62 message
        // - 1 category 65 message
        File infile = new File(INPUT_FILE_DIRECTORY_PATH, "62_65_message.bin");
        
        AsterixMessage testInputMessage;

        try(FileInputStream inStream = new FileInputStream(infile)) {
            // read raw file and store its bytes for later check
            FileChannel inChannel = inStream.getChannel();
            
            matchOriginalAndReencoded(inChannel);
            
            List<GenericDataBlock> dataBlocks = new ArrayList<GenericDataBlock>();
            testInputMessage = new AsterixMessage(dataBlocks);
            testInputMessage.decode(inChannel);
            inChannel.close();
        }
        
        Assert.assertEquals(2, testInputMessage.getDataBlocks().size());
        
        final GenericDataBlock cat62Block = testInputMessage.getDataBlocks().get(0);
        
        Assert.assertEquals(62, cat62Block.getDataCategory());
        
        Assert.assertEquals(1, cat62Block.getRecords().size());
        
        // category 62 message
        
        GenericRecord r = cat62Block.getRecords().get(0);
        
        Assert.assertEquals("[7D, 11]", extractField(r, 1));
        Assert.assertEquals("[54, 60, 21]", extractField(r, 4));
        Assert.assertEquals("[FA, 6F, F0, FD, 42, 1B]", extractField(r, 6));
        Assert.assertEquals("[FE, D6, 2, A4]", extractField(r, 7));
        Assert.assertEquals("[24, C1]", extractField(r, 9));
        Assert.assertEquals("[[0, 0, 1], [15, A4, F3, DD, 8, 20], [EE, 4D], [82, 38], [C2, 8], [0, 60], [0, 0], [0, 0], [C0, 0], [EE, C9], [6, 5C], [1, 2C], [0, 47]]", extractField(r, 11));
        Assert.assertEquals("[0, 1]", extractField(r, 12));
        Assert.assertEquals("[[1], [21], [1], [0]]", extractField(r, 13));
        Assert.assertEquals("[[0], [0], [0], [0]]", extractField(r, 14));
        Assert.assertEquals("[F0]", extractField(r, 15));
        Assert.assertEquals("[[0], [0], [0], [0], [0], [0], [0], [0], [0], [0], [0], [0]]", extractField(r, 16));
        Assert.assertEquals("[2, 8]", extractField(r, 17));
        Assert.assertEquals("[2, 8]", extractField(r, 19));
        Assert.assertEquals("[0, 0]", extractField(r, 20));
        Assert.assertEquals("[[E0]]", extractField(r, 28));
        Assert.assertEquals("[[FE, D6, 2, A4]]", extractField(r, 34));
        Assert.assertEquals("[5, 60, 20, 2, 8]", extractField(r, 35));

        final GenericDataBlock cat65Block = testInputMessage.getDataBlocks().get(1);
        
        Assert.assertEquals(65, cat65Block.getDataCategory());
        
        Assert.assertEquals(1, cat65Block.getRecords().size());
        
        // category 65 message
        
        r = cat65Block.getRecords().get(0);
        
        Assert.assertEquals("[7D, 11]", extractField(r, 1));
        Assert.assertEquals("[2]", extractField(r, 2));
        Assert.assertEquals("[0]", extractField(r, 3));
        Assert.assertEquals("[54, 60, E7]", extractField(r, 4));
        Assert.assertEquals("[8]", extractField(r, 5));
    }
    
    /**
     * Tests the method to fill the Reserved Expansion Field with a velocity
     * value.
     */
    @Test
    public void
    testPrepareREForCat062()
    throws Exception
    {
        
        TrackVelocity trackVelocity = new TrackVelocity((short)1, (short)5, 5.5);
        REForCat062 re = new REForCat062((short) 62, 
                           5, 
                           "description",
                           new ArrayList<DataItem>()); 
                
        ByteBuffer buffer = REForCat062Container.getDefaultBuffer(
                            re.getSubTypeDescriptor(),
                            trackVelocity); 
        
        byte[] bytes = new byte[] { 
                0x6,  // length
                0x20, // item indicator 00100000 (only the 3rd item is present)
                0x0, 0x1, 0x0, 0x5 };   // values of the velocity
        
        Assert.assertEquals(ByteBuffer.wrap(bytes), buffer);
        
        //another test
        trackVelocity = new TrackVelocity((short)2, (short)7, 5.5);
        buffer = REForCat062Container.getDefaultBuffer(
                re.getSubTypeDescriptor(),
                trackVelocity);
        bytes[3] = 0x2;
        bytes[5] = 0x7;

        Assert.assertEquals(ByteBuffer.wrap(bytes), buffer);
        
    }

    /**
     * Tests the basic operation of the field specification. 
     * 
     * @throws Exception 
     * 
     */
    @Test
    public void 
    test30_252_CategoryBlocks()
    throws Exception
    {                           
        // asterixmessages.bin contains:
        // - 2 category 30 messages
        // - 1 category 252 message
        File infile = new File(INPUT_FILE_DIRECTORY_PATH, "asterixmessages.bin");
        
        AsterixMessage testInputMessage;
        try(FileInputStream inStream = new FileInputStream(infile)) {
            // read raw file and store its bytes for later check
            FileChannel inChannel = inStream.getChannel();
            
            matchOriginalAndReencoded(inChannel);
            
            List<GenericDataBlock> dataBlocks = new ArrayList<GenericDataBlock>();
            testInputMessage = new AsterixMessage(dataBlocks);
            testInputMessage.decode(inChannel);
            inChannel.close();
        }
        
        Assert.assertEquals(2, testInputMessage.getDataBlocks().size());
        
        final GenericDataBlock cat30Block = testInputMessage.getDataBlocks().get(0);
        
        Assert.assertEquals(30, cat30Block.getDataCategory());
        
        Assert.assertEquals(2, cat30Block.getRecords().size());
        
        // first category 30 message
        
        GenericRecord r = cat30Block.getRecords().get(0);
        
        Assert.assertEquals("[7D, 11]", extractField(r, 1));
        Assert.assertEquals("[32, C9]", extractField(r, 2));
        Assert.assertEquals("[0, 28]", extractField(r, 5));
        Assert.assertEquals("[46, 50, 80]", extractField(r, 6));
        Assert.assertEquals("[0, 0, 0, 0]", extractField(r, 7));
        Assert.assertEquals("[5, 3D, EE, 8F]", extractField(r, 8));
        Assert.assertEquals("[7, 6E, 13, 23]", extractField(r, 9));
        Assert.assertEquals("[3, 5D, 6, A0]", extractField(r, 10));
        Assert.assertEquals("[21, 3F]", extractField(r, 11));
        Assert.assertEquals("[4, 13]", extractField(r, 12));
        Assert.assertEquals("[4, 13]", extractField(r, 13));
        Assert.assertEquals("[4, 13]", extractField(r, 14));
        Assert.assertEquals("[[5], [4F], [13], [0]]", extractField(r, 15));
        Assert.assertEquals("[1]", extractField(r, 16));
        Assert.assertEquals("[4]", extractField(r, 17));
        Assert.assertEquals("[0, C2]", extractField(r, 18));
        
        // second category 30 message
        
        r = cat30Block.getRecords().get(1);
        
        Assert.assertEquals("[7D, 11]", extractField(r, 1));
        Assert.assertEquals("[32, C9]", extractField(r, 2));
        Assert.assertEquals("[0, 58]", extractField(r, 5));
        Assert.assertEquals("[46, 50, 80]", extractField(r, 6));
        Assert.assertEquals("[0, 0, 0, 0]", extractField(r, 7));
        Assert.assertEquals("[A, 1, EE, 80]", extractField(r, 8));
        Assert.assertEquals("[7, 33, FA, F8]", extractField(r, 9));
        Assert.assertEquals("[FF, 1D, 7, 25]", extractField(r, 10));
        Assert.assertEquals("[21, A]", extractField(r, 11));
        Assert.assertEquals("[5, F0]", extractField(r, 12));
        Assert.assertEquals("[5, F0]", extractField(r, 13));
        Assert.assertEquals("[5, F0]", extractField(r, 14));
        Assert.assertEquals("[[5], [4F], [13], [0]]", extractField(r, 15));
        Assert.assertEquals("[1]", extractField(r, 16));
        Assert.assertEquals("[0]", extractField(r, 17));
        Assert.assertEquals("[0, 0]", extractField(r, 18));
        
        final GenericDataBlock cat252Block = testInputMessage.getDataBlocks().get(1);
        
        Assert.assertEquals(252, cat252Block.getDataCategory());
        
        Assert.assertEquals(1, cat252Block.getRecords().size());
                
        // single category 252 message
        
        r = cat252Block.getRecords().get(0);
        
        Assert.assertEquals("[7D, 11]", extractField(r, 1));
        Assert.assertEquals("[32, C9]", extractField(r, 2));
        Assert.assertEquals("[46, 50, CD]", extractField(r, 3));
        Assert.assertEquals("[32]", extractField(r, 4));
        Assert.assertEquals("[[5], [0]]", extractField(r, 5));
    }     

    /**
     * Tests the basic operation of the field specification. 
     * 
     * @throws Exception 
     * 
     */
    @Test
    public void 
    test242_V2_CategoryBlock()
    throws Exception
    {                           
        
        File infile = new File(INPUT_FILE_DIRECTORY_PATH, "mv_242_v2_message.bin");
        
        AsterixMessage testInputMessage;
        try(FileInputStream inStream = new FileInputStream(infile)) {
            // read raw file and store its bytes for later check
            FileChannel inChannel = inStream.getChannel();

            matchOriginalAndReencoded(inChannel);
                    
            List<GenericDataBlock> dataBlocks = new ArrayList<GenericDataBlock>();
            testInputMessage = new AsterixMessage(dataBlocks);
            testInputMessage.decode(inChannel);
            inChannel.close();
        }
        
        Assert.assertEquals(1, testInputMessage.getDataBlocks().size());
        
        final GenericDataBlock cat242Block = testInputMessage.getDataBlocks().get(0);
        
        Assert.assertEquals(242, cat242Block.getDataCategory());
        
        Assert.assertEquals(1, cat242Block.getRecords().size());
        
        GenericRecord r = cat242Block.getRecords().get(0);
        
        Assert.assertEquals("[0, 80]", extractField(r, 1));
        Assert.assertEquals("[53, 7C, C8]", extractField(r, 2));
        Assert.assertEquals("[F, FF]", extractField(r, 3));
        Assert.assertEquals("[81, FF]", extractField(r, 4));
        Assert.assertEquals("[[F0, 11], [C8, 0]]", extractField(r, 5));
        Assert.assertEquals("[[1E, DF], [52, 36]]", extractField(r, 6));
        Assert.assertEquals("[[0, 0]]", extractField(r, 7));
        Assert.assertEquals("[[50, 0]]", extractField(r, 8));
        Assert.assertEquals("[[40, 3], [48, 0]]", extractField(r, 9));
        Assert.assertEquals("[0, 0, A0, 0, FF, FE, C0, 0]", extractField(r, 10));
        Assert.assertEquals("[F8, 0]", extractField(r, 11));
        Assert.assertEquals("[3, 80, FF, 62]", extractField(r, 12));
        Assert.assertEquals("[3, 8E, 47, 1C]", extractField(r, 13));
        Assert.assertEquals("[A0, 0, 0, 0]", extractField(r, 14));
        Assert.assertEquals("[80, 0]", extractField(r, 15));
        Assert.assertEquals("[0, 0]", extractField(r, 16));
        Assert.assertEquals("[30, 8E]", extractField(r, 17));
        Assert.assertEquals("[3E, 5D]", extractField(r, 18));
        Assert.assertEquals("[B8, D1]", extractField(r, 19));
        Assert.assertEquals("[8F, 80]", extractField(r, 20));
        Assert.assertEquals("[D, C]", extractField(r, 21));
        Assert.assertEquals("[41, 41]", extractField(r, 22));
        Assert.assertEquals("[[1], [4]]", extractField(r, 23));
        Assert.assertEquals("[[4]]", extractField(r, 24));
        Assert.assertEquals("[[5E], [40]]", extractField(r, 25));
        Assert.assertEquals("[AC, 6A, 12, 40]", extractField(r, 26));
        Assert.assertEquals("[[E8], [29]]", extractField(r, 27));
        Assert.assertEquals("[BC, 53, 1D, 10]", extractField(r, 28));
        Assert.assertEquals("[[1], [3]]", extractField(r, 29));
        Assert.assertEquals("[C1, 16, 57, F3]", extractField(r, 30));
        Assert.assertEquals("[4, 8]", extractField(r, 31));
        Assert.assertEquals("[2, 9C]", extractField(r, 32));
        Assert.assertEquals("[80, 0, 0, 0]", extractField(r, 33));
        Assert.assertEquals("[2C, 67, 4C, 7D]", extractField(r, 34));
        Assert.assertEquals("[[F4, ED], [F3, 8C]]", extractField(r, 35));
        Assert.assertEquals("[[C1, 6B], [38, 26]]", extractField(r, 36));
        Assert.assertEquals("[[C4], [A6], [A5]]", extractField(r, 37));
        Assert.assertEquals("[[CF]]", extractField(r, 38));
        Assert.assertEquals("[[80, D5], [A9, 51]]", extractField(r, 39));
        Assert.assertEquals("[[4E, BA], [B1, 8D]]", extractField(r, 40));
        Assert.assertEquals("[[5], [CF], [91], [BC], [EB], [CB], [F1], [87], [1B]]", extractField(r, 41));
        Assert.assertEquals("[[68], [E9], [BD], [1], [1D]]", extractField(r, 42));
        Assert.assertEquals("[[9A]]", extractField(r, 43));
        Assert.assertEquals("[[1B], [31], [50], [64], [99], [DC], [52], [F8], [E]]", extractField(r, 44));
        Assert.assertEquals("[[CA], [C], [73], [96], [A], [EA], [8F]]", extractField(r, 45));
        Assert.assertEquals("[[74], [69], [74], [69], [20], [20], [20]]", extractField(r, 46));
        Assert.assertEquals("[2]", extractField(r, 47));
        Assert.assertEquals("[5A]", extractField(r, 48));
        Assert.assertEquals("[[D], [44]]", extractField(r, 49));
        Assert.assertEquals("[1]", extractField(r, 50));
        Assert.assertEquals("[1]", extractField(r, 51));
        Assert.assertEquals("[[D], [44]]", extractField(r, 52));
        Assert.assertEquals("[41, 41]", extractField(r, 53));
        Assert.assertEquals("[41, 33, 32, 30]", extractField(r, 54));
        Assert.assertEquals("[4D]", extractField(r, 55));
        Assert.assertEquals("[[54], [44], [41]]", extractField(r, 56));
        Assert.assertEquals("[[41], [42], [43]]", extractField(r, 57));
        Assert.assertEquals("[[EC], [CD], [F6], [74], [72], [6F], [9]]", extractField(r, 58));
        Assert.assertEquals("[[41], [8A], [8E], [42], [56], [C2], [60]]", extractField(r, 59));
        Assert.assertEquals("[[A4], [18], [65], [5A], [DD], [BE], [A8]]", extractField(r, 60));
        Assert.assertEquals("[[44], [45], [46]]", extractField(r, 61));
        Assert.assertEquals("[6, 5C]", extractField(r, 62));
        Assert.assertEquals("[69, BA]", extractField(r, 63));
        Assert.assertEquals("[5F, 36]", extractField(r, 64));
        Assert.assertEquals("[2F, D0]", extractField(r, 65));
        Assert.assertEquals("[5, A0]", extractField(r, 66));
        Assert.assertEquals("[B2, F3]", extractField(r, 67));
        Assert.assertEquals("[5A, 85, D0, 46]", extractField(r, 68));
        Assert.assertEquals("[32, 19, 2A, C8]", extractField(r, 69));
        Assert.assertEquals("[[8, D1]]", extractField(r, 70));
        Assert.assertEquals("[97, 59]", extractField(r, 71));
        Assert.assertEquals("[1, DD, DE]", extractField(r, 72));
        Assert.assertEquals("[74, 69, 74, 69, 20, 20, 20, 20]", extractField(r, 73));
        Assert.assertEquals("[8F, DE]", extractField(r, 74));
        Assert.assertEquals("[0, FF]", extractField(r, 75));
        Assert.assertEquals("[0, FE]", extractField(r, 76));
        Assert.assertEquals("[90, 0]", extractField(r, 77));
        Assert.assertEquals("[0, FD]", extractField(r, 78));
        Assert.assertEquals("[0, FC]", extractField(r, 79));
        Assert.assertEquals("[0, FB]", extractField(r, 80));
        Assert.assertEquals("[0, FA]", extractField(r, 81));
        Assert.assertEquals("[0, F9]", extractField(r, 82));
        Assert.assertEquals("[80, 0, 2A, AB]", extractField(r, 83));
        Assert.assertEquals("[[74], [6F], [74], [6F], [20], [20], [20]]", extractField(r, 84));
        Assert.assertEquals("[68, 5, 26, 69]", extractField(r, 85));
        Assert.assertEquals("[88]", extractField(r, 86));
        Assert.assertEquals("[66]", extractField(r, 87));
        Assert.assertEquals("[[65], [1F], [25], [79]]", extractField(r, 88));
        Assert.assertEquals("[0, F8]", extractField(r, 89));
        Assert.assertEquals("[BE, 46]", extractField(r, 90));
        Assert.assertEquals("[ED, F7]", extractField(r, 91));
        Assert.assertEquals("[17, C3, D1, 4A]", extractField(r, 92));
        Assert.assertEquals("[DD, FF, 14, C2]", extractField(r, 93));
        Assert.assertEquals("[[5E, D7], [46, AA]]", extractField(r, 94));
        Assert.assertEquals("[[35, 7B], [50, 56]]", extractField(r, 95));
        Assert.assertEquals("[[0], [67], [57], [53]]", extractField(r, 96));
        Assert.assertEquals("[[7A, 5E], [BB, 45], [20, 95]]", extractField(r, 97));
        Assert.assertEquals("[[D8], [83], [E8]]", extractField(r, 98));
       
    }   
        
    
    //--------------------------------------------------------------------------
    // protected constructors

    //--------------------------------------------------------------------------
    // protected member functions

    //--------------------------------------------------------------------------
    // protected static member functions
    
    //--------------------------------------------------------------------------
    // protected data members

    //--------------------------------------------------------------------------
    // protected static data members

    //--------------------------------------------------------------------------
    // package member functions

    //--------------------------------------------------------------------------
    // private constructors

    //--------------------------------------------------------------------------
    // private member functions


    /**
     * *************************************************************************
     * extracts a DataField from a GenericRecord, checks it's not null, renders
     * it as String 
     *
     * @param r the GenericRecord to extract data from
     * @param frn the Field Reference Number of the data field to extract
     * @return the string representation for the matching field
     */
    private String extractField(GenericRecord r, int frn)
    {
        final DataField f = r.getDataField(frn);
        Assert.assertNotNull(f);
        return f.toString();
    }
    
     /**
     * *************************************************************************
     * read an Asterix message file, decode it and re-encode it and compare
     * the result to the original content, byte by byte. 
     *
     * @param inChannel an input file channel for the Asterix message file 
     * @throws Exception thrown on mismatch or I/O error
     */
    private void matchOriginalAndReencoded(FileChannel inChannel)
            throws Exception
    {
        List<GenericDataBlock> dataBlocks = new ArrayList<GenericDataBlock>();
        AsterixMessage testInputMessage = new AsterixMessage(dataBlocks);
        testInputMessage.decode(inChannel);
        inChannel.position(0);

        // read raw file and store its bytes for later check
        final ByteBuffer originalBytes = ByteBuffer.allocate((int) inChannel.size()); 
        inChannel.read(originalBytes);
        inChannel.position(0);

        //check decoding then encoding gives out exactly the same message
        final ByteBuffer reencodedByte = testInputMessage.encode();
        Assert.assertEquals(originalBytes.capacity(), reencodedByte.capacity());
        for(int i = 0; i < originalBytes.capacity(); ++i){
            Assert.assertEquals(originalBytes.get(i), reencodedByte.get(i));
        }
    }

    //--------------------------------------------------------------------------
    // private static member functions
        
    //--------------------------------------------------------------------------
    // private data members

    /**
     * Path to the test input files. 
     */
    private static final String INPUT_FILE_DIRECTORY_PATH = 
    "src/test/java/ch/skyguide/communication/rdps/asterix/";  

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    

}

