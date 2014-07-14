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
package ch.skyguide.communication.rdps.asterix.structure;


//------------------------------------------------------------------------------

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Test cases for the class <code>AsterixFieldSpecificationType</code>.
 * 
 * @author Ross Wilson
 */
public class AsterixFieldSpecificationTypeTest
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions
    

    /**
     * Tests the one byte field specification. 
     * 
     */
    @Test
    @Ignore
    public void 
    testOneByteFieldSpecification() 
    {                           
        ByteBuffer buffer = ByteBuffer.allocate(3);
                
        List<AsterixFieldSpecificationType> fieldSpecifications =
        new ArrayList<>();
        
        for(int index = 0; index < 256; index++) {
            
            byte spec = (byte)(((index)<<8)>>>8);
            buffer.put((byte)0xFF);
            buffer.put((byte)0xFF);
            buffer.put(spec);
            buffer.flip();
            System.out.println(String.format
            ("Start of Field Specification byte: %X", Byte.valueOf(spec)));
            AsterixFieldSpecificationType fieldSpecification = 
            new AsterixFieldSpecificationType(buffer);
            fieldSpecifications.add(fieldSpecification);
            
            for(int fieldReferenceNumber = fieldSpecification.rewind(); 
                fieldReferenceNumber >= 1; 
                fieldReferenceNumber = fieldSpecification.nextFieldReferenceNumber()) {
                
                System.out.
                println("Field Reference Number (FRN): " + fieldReferenceNumber);
            }
            
            byte[] reconstructedSpec = fieldSpecification.toByteArray();
            String reconstructedSpecString = "";
            for(int i = 0; i < reconstructedSpec.length; i++) {
                
                reconstructedSpecString += String.format
                ("%X, ", Byte.valueOf(reconstructedSpec[i]));
            }
            
            System.out.println("Reconstruction of Field Specification byte: [" + reconstructedSpecString + "]");
            buffer.flip();
            int i = 0;
            while(buffer.remaining() != 0) {
                i = buffer.position();
                Assert.assertEquals
                (Byte.valueOf(buffer.get()), Byte.valueOf(reconstructedSpec[i]));
            }
            System.out.println(String.format
            ("End of Field Specification byte: %X", Byte.valueOf(spec)));

            buffer.clear();
        }
        
        System.out.println("Cardinality: " + fieldSpecifications.get(0).cardinality());
        Assert.assertEquals
        ("Wrong cardinality",
         Integer.valueOf(0),
         Integer.valueOf(fieldSpecifications.get(0).cardinality()));
        System.out.println("End: " + fieldSpecifications.get(0).end());
        Assert.assertEquals
        (Integer.valueOf(0),
         Integer.valueOf(fieldSpecifications.get(0).end()));
        System.out.println("Size: " + fieldSpecifications.get(0).size());
        System.out.println("Rewind: " + fieldSpecifications.get(0).rewind());
        Assert.assertEquals
        (Integer.valueOf(-1),
         Integer.valueOf(fieldSpecifications.get(0).rewind()));
    }   
    
    /**
     * Tests the basic operation of the field specification. 
     * 
     */
    @Test
    public void 
    testFieldSpecification() 
    {                           
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte)0xFC);
        
        buffer.flip();
        
        AsterixFieldSpecificationType fieldSpecification = 
        new AsterixFieldSpecificationType(buffer);
        
        System.out.println("length: " + fieldSpecification.end());
        
        for(int fieldReferenceNumber = fieldSpecification.rewind(); 
            fieldReferenceNumber >= 1; 
            fieldReferenceNumber = fieldSpecification.nextFieldReferenceNumber()) {
            
            System.out.
            println("Field Reference Number (FRN): " + fieldReferenceNumber);
        }
    }   

    /**
     * Tests the basic operation of the field specification. 
     * 
     */
    @Test
    public void 
    testFieldSpecification2() 
    {                           
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte)0xFF);
        buffer.put((byte)0xFC);
        
        buffer.flip();
        
        AsterixFieldSpecificationType fieldSpecification = 
        new AsterixFieldSpecificationType(buffer);
        
        System.out.println("length: " + fieldSpecification.end());
        
        for(int fieldReferenceNumber = fieldSpecification.rewind(); 
            fieldReferenceNumber >= 1; 
            fieldReferenceNumber = fieldSpecification.nextFieldReferenceNumber()) {
            
            System.out.
            println("Field Reference Number (FRN): " + fieldReferenceNumber);
        }
    }   

    /**
     * Tests the basic operation of the field specification. 
     * 
     */
    @Test
    public void 
    testFieldSpecification3() 
    {                           
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte)-105);
        buffer.put((byte)95);
        buffer.put((byte)-19);
        buffer.put((byte)3);
        buffer.put((byte)2);
        
        buffer.flip();
        
        AsterixFieldSpecificationType fieldSpecification = 
        new AsterixFieldSpecificationType(buffer);
        
        System.out.println("length: " + fieldSpecification.end());
        
        for(int fieldReferenceNumber = fieldSpecification.rewind(); 
            fieldReferenceNumber >= 1; 
            fieldReferenceNumber = fieldSpecification.nextFieldReferenceNumber()) {
            
            System.out.
            println("Field Reference Number (FRN): " + fieldReferenceNumber);
        }
    }   

    /**
     * Tests the getBytes operation of the field specification. 
     * 
     */
    @Test
    public void 
    testgetBytes() 
    {                           
        ByteBuffer buffer = ByteBuffer.allocate(5);
        AsterixFieldSpecificationType fieldSpecification = null;
        
        // 1st Byte all set and second zero.
        buffer.clear();
        buffer.put((byte)0xFE);
        buffer.put((byte)0x00);
        buffer.flip();
        
        fieldSpecification = new AsterixFieldSpecificationType(buffer);
        
        Assert.assertEquals
        (Integer.valueOf(1),
         Integer.valueOf(fieldSpecification.getBytes()));
        
        // Nothing set in the field specification.
        buffer.clear();
        buffer.put((byte)0x00);
        buffer.flip();
        
        fieldSpecification = new AsterixFieldSpecificationType(buffer);
        
        Assert.assertEquals
        (Integer.valueOf(1),
         Integer.valueOf(fieldSpecification.getBytes()));
        
        // First byte set in the field specification.
        buffer.clear();
        buffer.put((byte)0xFC);
        buffer.flip();
        
        fieldSpecification = new AsterixFieldSpecificationType(buffer);
        
        Assert.assertEquals
        (Integer.valueOf(1),
         Integer.valueOf(fieldSpecification.getBytes()));
        
        // Until 3rd byte set in the field specification.
        buffer.clear();
        buffer.put((byte)0xCF);
        buffer.put((byte)0xFF);
        buffer.put((byte)0xF0);
        buffer.flip();
        
        fieldSpecification = new AsterixFieldSpecificationType(buffer);
        
        Assert.assertEquals
        (Integer.valueOf(3), 
         Integer.valueOf(fieldSpecification.getBytes()));
        
        // Until 4th byte set in the field specification.
        buffer.clear();
        buffer.put((byte)0xCF);
        buffer.put((byte)0xFF);
        buffer.put((byte)0xF1);
        buffer.put((byte)0x10);
        buffer.flip();
        
        fieldSpecification = new AsterixFieldSpecificationType(buffer);
        
        Assert.assertEquals
        (Integer.valueOf(4),
         Integer.valueOf(fieldSpecification.getBytes()));
    }
    
    /**
     * Tests the getBytes operation of the field specification. 
     * 
     */
    @Test
    public void 
    testSpecificallySpecifiedGetBytes() 
    {                           
        AsterixFieldSpecificationType fieldSpecification 
        	= new AsterixFieldSpecificationType();
        
        Assert.assertNotNull(fieldSpecification);

        // B01 0x FB -> 1111 1011, No. 1
        fieldSpecification.enableField(1);
        fieldSpecification.enableField(2);
        fieldSpecification.enableField(3);
        fieldSpecification.enableField(4);

        fieldSpecification.enableField(5);
        fieldSpecification.disableField(6);
        fieldSpecification.enableField(7);
        // Fx
        
        // B02 0xFF 1111 1111, No. 8
        fieldSpecification.enableField(8);
        fieldSpecification.enableField(9);
        fieldSpecification.enableField(10);
        fieldSpecification.enableField(11);
        
        fieldSpecification.enableField(12);
        fieldSpecification.enableField(13);
        fieldSpecification.enableField(14);
        // Fx
        
        // B03 0x CB 1100 1011, No 15
        fieldSpecification.enableField(15);
        fieldSpecification.enableField(16);
        fieldSpecification.disableField(17);
        fieldSpecification.disableField(18);

        fieldSpecification.enableField(19);
        fieldSpecification.disableField(20);
        fieldSpecification.enableField(21);
        // Fx
        
        // B04 0x 01 0000 0001, No. 22
        fieldSpecification.disableField(22);
        fieldSpecification.disableField(23);
        fieldSpecification.disableField(24);
        fieldSpecification.disableField(25);
        
        fieldSpecification.disableField(26);
        fieldSpecification.disableField(27);
        fieldSpecification.disableField(28);
        // Fx
        
        // B05 0x 09 0000 1001, No. 29
        fieldSpecification.disableField(29);
        fieldSpecification.disableField(30);
        fieldSpecification.disableField(31);
        fieldSpecification.disableField(32);
        
        fieldSpecification.enableField(33);
        fieldSpecification.disableField(34);
        fieldSpecification.disableField(35);
        // Fx

        // B06 0x 01 0000 0001, No. 36
        fieldSpecification.disableField(36);
        fieldSpecification.disableField(37);
        fieldSpecification.disableField(38);
        fieldSpecification.disableField(39);
        
        fieldSpecification.disableField(40);
        fieldSpecification.disableField(41);
        fieldSpecification.disableField(42);
        // Fx

        // B07 0x 0B 0000 1011, No. 43
        fieldSpecification.disableField(43);
        fieldSpecification.disableField(44);
        fieldSpecification.disableField(45);
        fieldSpecification.disableField(46);
        
        fieldSpecification.enableField(47);
        fieldSpecification.disableField(48);
        fieldSpecification.enableField(49);
        // Fx

        // B08 0x 01 0000 0001, No. 50
        fieldSpecification.disableField(50);
        fieldSpecification.disableField(51);
        fieldSpecification.disableField(52);
        fieldSpecification.disableField(53);
        
        fieldSpecification.disableField(54);
        fieldSpecification.disableField(55);
        fieldSpecification.disableField(56);
        // Fx

        // B09 0x 01 0000 0001, No. 57
        fieldSpecification.disableField(57);
        fieldSpecification.disableField(58);
        fieldSpecification.disableField(59);
        fieldSpecification.disableField(60);
        
        fieldSpecification.disableField(61);
        fieldSpecification.disableField(62);
        fieldSpecification.disableField(63);
        // Fx

        // B10 0x 01 0000 0001, No. 64
        fieldSpecification.disableField(64);
        fieldSpecification.disableField(65);
        fieldSpecification.disableField(66);
        fieldSpecification.disableField(67);
        
        fieldSpecification.disableField(68);
        fieldSpecification.disableField(69);
        fieldSpecification.disableField(70);
        // Fx

        // B11 0x 6F 0110 1111, No. 71
        fieldSpecification.disableField(71);
        fieldSpecification.enableField(72);
        fieldSpecification.enableField(73);
        fieldSpecification.disableField(74);
        
        fieldSpecification.enableField(75);
        fieldSpecification.enableField(76);
        fieldSpecification.enableField(77);
        // Fx

        // B12 0x F9 1111 1001, No. 78
        fieldSpecification.enableField(78);
        fieldSpecification.enableField(79);
        fieldSpecification.enableField(80);
        fieldSpecification.enableField(81);
        
        fieldSpecification.enableField(82);
        fieldSpecification.disableField(83);
        fieldSpecification.disableField(84);
        // Fx

        // B13 0x 09 0000 1000, No. 85
        fieldSpecification.disableField(85);
        fieldSpecification.disableField(86);
        fieldSpecification.disableField(87);
        fieldSpecification.disableField(88);

        fieldSpecification.enableField(89);
        fieldSpecification.disableField(90);
        fieldSpecification.disableField(91);
        // Fx

        // B14 0x 00 0000 0000, No. 92
        fieldSpecification.disableField(92);
        fieldSpecification.disableField(93);
        fieldSpecification.disableField(94);
        fieldSpecification.disableField(95);
        
        fieldSpecification.disableField(96);
        fieldSpecification.disableField(97);
        fieldSpecification.disableField(98);
        // Fx
        
        for(int fieldReferenceNumber = fieldSpecification.rewind(); 
                fieldReferenceNumber >= 1; 
                fieldReferenceNumber = fieldSpecification.nextFieldReferenceNumber()) {
                
                System.out.
                println("Field Reference Number (FRN): " + fieldReferenceNumber);
        }

        final byte[] byteArray = fieldSpecification.toByteArray();
        
        // 0x FB FF	CB 01 09 01	11 01 01 01 6F F9 09 00
        Assert.assertEquals(13, byteArray.length);
        Assert.assertEquals((byte)0xFB, (byte)byteArray[0]);
        Assert.assertEquals((byte)0xFF, (byte)byteArray[1]);
        Assert.assertEquals((byte)0xCB, (byte)byteArray[2]);
        Assert.assertEquals((byte)0x01, (byte)byteArray[3]);
        Assert.assertEquals((byte)0x09, (byte)byteArray[4]);
        Assert.assertEquals((byte)0x01, (byte)byteArray[5]);
        Assert.assertEquals((byte)0x0B, (byte)byteArray[6]);
        Assert.assertEquals((byte)0x01, (byte)byteArray[7]);
        Assert.assertEquals((byte)0x01, (byte)byteArray[8]);
        Assert.assertEquals((byte)0x01, (byte)byteArray[9]);
        Assert.assertEquals((byte)0x6F, (byte)byteArray[10]);
        Assert.assertEquals((byte)0xF9, (byte)byteArray[11]);
        Assert.assertEquals((byte)0x08, (byte)byteArray[12]);
        
}
    
    /**
     * Tests the basic operation of the field specification. 
     * 
     */
    @Test
    public void 
    testFieldSpecificationToBytes() 
    {                           
        AsterixFieldSpecificationType fieldSpecification = 
        new AsterixFieldSpecificationType();
        
        for(int i=1; i < 15; i++) {
        	
        	fieldSpecification.enableField(i);
        }
        
        System.out.println("length: " + fieldSpecification.end());
        
        for(int fieldReferenceNumber = fieldSpecification.rewind(); 
            fieldReferenceNumber >= 1; 
            fieldReferenceNumber = fieldSpecification.nextFieldReferenceNumber()) {
            
            System.out.
            println("Field Reference Number (FRN): " + fieldReferenceNumber);
        }
        
        byte[] packed = fieldSpecification.toByteArray();
        
        Assert.assertEquals((byte)0xFF, (byte)packed[0]);
        Assert.assertEquals((byte)0xFE, (byte)packed[1]);
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

    //--------------------------------------------------------------------------
    // private static member functions
        
    //--------------------------------------------------------------------------
    // private data members

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    

}
