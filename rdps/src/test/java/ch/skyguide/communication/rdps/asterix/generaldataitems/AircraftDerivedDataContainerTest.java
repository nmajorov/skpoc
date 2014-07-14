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
package ch.skyguide.communication.rdps.asterix.generaldataitems;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ch.skyguide.communication.rdps.asterix.categories.DataCategoryType;
import ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedData;
import ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataContainer;
import ch.skyguide.communication.rdps.asterix.categories.cat062.ComAcasCapability.FlightStatus;
import ch.skyguide.communication.rdps.asterix.categories.cat062.ComAcasCapability.TransponderCOMCapability;
import ch.skyguide.communication.rdps.asterix.categories.cat062.TrackAngleRate.TurnIndicator;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.util.DataFileReader;
import ch.skyguide.util.resource.ResourceUtil;



//------------------------------------------------------------------------------


/**
 * Test cases for the class <code>AircraftDerivedDataContainer</code>.
 * 
 * @author Guillaume Fouillet
 */
public class AircraftDerivedDataContainerTest
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions
    
    
    //
    /**
     * Tests AircraftDerivedDataContainer serialization
     * 
     */
    @Test
    @Ignore("File Not found")
    public void 
    testBufferDataSerialization() 
    { 
        // test data
        short byteRepresentation = 
            DataCategoryType.CAT062.getByteRepresentation();        
        AircraftDerivedData dataItem = 
        new AircraftDerivedData(byteRepresentation, 
                380, 
                "Aircraft derived data", 
                AircraftDerivedData.initializeCat062_380SecondaryFields(
                        byteRepresentation));
        ByteBuffer buffer = createByteBuffer("AircraftDerivedData.bin");
        DataField dataField = dataItem.createAssociatedDataField(buffer);
        dataField.decode(buffer);
        ByteBuffer newBuffer = dataField.encode();
        AircraftDerivedDataContainer container = 
            new AircraftDerivedDataContainer(newBuffer, dataItem.getSubTypeDescriptor());
        Assert.assertEquals("11110101001001011101000100001100", container.getPrimaryField().toString());
        Assert.assertEquals("101010111100110111101111", container.getTargetAddress().toString());
        Assert.assertEquals("AFR12345", container.getTargetIdentification());
        Assert.assertEquals(Double.valueOf(180D), Double.valueOf(container.getMagneticHeading()*(360/Math.pow(2, 16))));
        Assert.assertEquals(Double.valueOf(450D), Double.valueOf(container.getIASMNoSpeed()*(3600/Math.pow(2, 14))));
        Assert.assertEquals(Short.valueOf((short) 0),  Short.valueOf(container.getTrueAirspeed()));
        Assert.assertEquals(Boolean.valueOf(false),  Boolean.valueOf(container.getSALsAS()));
        Assert.assertEquals("01",  container.getSALsource().toString());
        Assert.assertEquals(Integer.valueOf(20000),  Integer.valueOf(container.getSALSelectedAltitude()*25));
        Assert.assertEquals(0, container.getFinalStateSelectedAltitude());
        Assert.assertNull(container.getTrajectoryIntentStatus());
        Assert.assertNull(container.getTrajectoryIntentData());
        Assert.assertEquals(TransponderCOMCapability.A_B,  container.getComAcasCapability().getTransponderCOMCapability());
        Assert.assertEquals(FlightStatus.AIR,  container.getComAcasCapability().getFlightStatusCode());
        Assert.assertEquals(Boolean.valueOf(false),  Boolean.valueOf(container.getComAcasCapability().hasSpecificServiceCapability()));
        Assert.assertEquals(Boolean.valueOf(true),  Boolean.valueOf(container.getComAcasCapability().hasAltitudeReportingCapability()));
        Assert.assertEquals(Boolean.valueOf(true),  Boolean.valueOf(container.getComAcasCapability().hasAircraftIdentificationCapability()));
        Assert.assertNull(container.getAcasResolutionAdvisoryReport());
        Assert.assertEquals(Double.valueOf(625D), Double.valueOf(container.getBarometricVerticalRate()*6.25));
        Assert.assertEquals(Short.valueOf((short) 0),  Short.valueOf(container.getGeometricVerticalRate()));
        Assert.assertEquals(Integer.valueOf(90), Integer.valueOf(container.getRollAngle()/100));
        Assert.assertEquals(TurnIndicator.STRAIGHT,  container.getTrackAngleRate().getTurnIndicator());
        Assert.assertEquals(Integer.valueOf(10),  Integer.valueOf(container.getTrackAngleRate().getRateOfTurn()/4));
        Assert.assertEquals(Double.valueOf(450D), Double.valueOf(container.getGroundSpeed()*(3600/Math.pow(2, 14))));
        Assert.assertNull(container.getVelocityUncertainty());
        Assert.assertNull(container.getMetData());
        Assert.assertNull(container.getEmitterCategory());
        Assert.assertNull(container.getPositionData());
        Assert.assertNull(container.getGeometricAltitude());
        Assert.assertNull(container.getPositionUncertainty());
        Assert.assertNull(container.getModeSMBData());
        Assert.assertEquals(Integer.valueOf(500),  Integer.valueOf(container.getIndicatedAirspeed()));
        Assert.assertEquals(Integer.valueOf(1),  Integer.valueOf(container.getMachNumber()/125));
        Assert.assertNull(container.getBarometricPressureSetting());
    }
    
    /**
     * Tests AircraftDerivedDataContainer de-serialization
     * 
     */
    @Test
    @Ignore("File not found")
    public void 
    testBufferDataDeSerialization() 
    { 
        // test data
        short byteRepresentation = 
            DataCategoryType.CAT062.getByteRepresentation();        
        AircraftDerivedData dataItem = 
        new AircraftDerivedData(byteRepresentation, 
                380, 
                "Aircraft derived data", 
                AircraftDerivedData.initializeCat062_380SecondaryFields(
                        byteRepresentation));
        byte[] readBytes;
        try {
            readBytes = readByteFile("AircraftDerivedData.bin");
            ByteBuffer buffer = createByteBuffer("AircraftDerivedData.bin");
            DataField dataField = dataItem.createAssociatedDataField(buffer);
            dataField.decode(buffer);
            ByteBuffer newBuffer = dataField.encode();
            AircraftDerivedDataContainer container = 
                new AircraftDerivedDataContainer(newBuffer, dataItem.getSubTypeDescriptor());
            byte[] containerBytes = container.getBytes();
            Assert.assertEquals(readBytes.length, containerBytes.length);
            for (int i=0;i<readBytes.length;i++) {
                Assert.assertEquals(readBytes[i],  containerBytes[i]);
            }
        }
        catch (IOException e) {
            Assert.fail();
        }        
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
     * Creates a new <code>VisualMessageConnection</code> which reads from the
     * specified local file.
     *
     * @param    localFilename
     *           The name of the local file the connection will read from.
     * @return   The newly created new <code>VisualMessageConnection</code>.
     *
     * @throws   IOException
     *           If the connection cannot be created.
     */
    private ByteBuffer
    createByteBuffer(String localFilename)
    {
        try {
            byte[] bytes = readByteFile(localFilename);
            ByteBuffer buffer = 
            ByteBuffer.allocate(bytes.length);            
            buffer.put(bytes);
            buffer.flip();
            return buffer;
                       
        }
        catch(BufferOverflowException exception)
        {   // The buffer should not be filled after reaching its limit.
            throw new IllegalArgumentException(exception);
        }
        catch(IOException exception)
        {   // The buffer should not be filled after reaching its limit.
            throw new IllegalArgumentException(exception);
        }        
    }


    /**
     * Reads a message file from the directory of this class's package via the
     * class loader.
     *
     * @param    localFilename
     *           The name of the file in the directory of this class's package.
     *
     * @return   message byte
     *           The bytes array read from the file.
     *
     * @throws   IOException
     *           If the file cannot be read.
     */
    private byte[]
    readByteFile(String localFilename)
    throws FileNotFoundException, IOException
    {
        File messageFile = ResourceUtil.getFile(getClass().getPackage(), localFilename);
        assertNotNull(messageFile);
        return DataFileReader.readFile(messageFile);
    }
    
    
    //--------------------------------------------------------------------------
    // private static member functions
        
    //--------------------------------------------------------------------------
    // private data members

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    

}
