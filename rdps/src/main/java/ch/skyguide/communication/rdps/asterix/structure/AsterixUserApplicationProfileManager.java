// ------------------------------------------------------------------------------
// PROJECT:
// Skyguide BEAM
// LAYER:
// System Interaction
// COPYRIGHT:
// Copyright (c) 1998-2007 Skyguide AG, Wangen bei Dubendorf, Switzerland
// All rights reserved.
// REVISION:
// $Id:$
// ------------------------------------------------------------------------------

// ------------------------------------------------------------------------------

package ch.skyguide.communication.rdps.asterix.structure;


// ------------------------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang.NotImplementedException;

import ch.skyguide.communication.dataitem.LoggingLengthPrefixUniversalRepetitiveDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalExtendedDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalFixedDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalRepetitiveDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalReservedDataItem;
import ch.skyguide.communication.rdps.asterix.categories.CartesianTrackPosition;
import ch.skyguide.communication.rdps.asterix.categories.DataCategoryType;
import ch.skyguide.communication.rdps.asterix.categories.IdentificationTag;
import ch.skyguide.communication.rdps.asterix.categories.ServiceIdentification;
import ch.skyguide.communication.rdps.asterix.categories.TimeOfMessage;
import ch.skyguide.communication.rdps.asterix.categories.TrackMode3A;
import ch.skyguide.communication.rdps.asterix.categories.TrackNumber;
import ch.skyguide.communication.rdps.asterix.categories.TypeOfMessage;
import ch.skyguide.communication.rdps.asterix.categories.UserNumber;
import ch.skyguide.communication.rdps.asterix.categories.cat030_032.ArtasTrackNumber;
import ch.skyguide.communication.rdps.asterix.categories.cat030_032.AsterixInteger;
import ch.skyguide.communication.rdps.asterix.categories.cat030_032.AsterixText;
import ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedData;
import ch.skyguide.communication.rdps.asterix.categories.cat062.CalculatedTrackVelocity;
import ch.skyguide.communication.rdps.asterix.categories.cat062.MeasuredInformation;
import ch.skyguide.communication.rdps.asterix.categories.cat062.ModeOfMovement;
import ch.skyguide.communication.rdps.asterix.categories.cat062.REForCat062;
import ch.skyguide.communication.rdps.asterix.categories.cat062.SystemTrackUpdateAges;
import ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAges;
import ch.skyguide.communication.rdps.asterix.categories.cat065.BatchNumber;
import ch.skyguide.communication.rdps.asterix.categories.cat242.LocalTrackNumber;
import ch.skyguide.communication.rdps.asterix.categories.cat242.MrtSource;
import ch.skyguide.communication.rdps.asterix.categories.cat243.FlowStatus;
import ch.skyguide.communication.rdps.asterix.categories.cat252.ServiceRelatedReport;
import ch.skyguide.communication.rdps.asterix.flightplan.AllocatedSSRCodes;
import ch.skyguide.communication.rdps.asterix.flightplan.CategoryOfTurbulence;
import ch.skyguide.communication.rdps.asterix.flightplan.ControlPosition;
import ch.skyguide.communication.rdps.asterix.flightplan.FlightCategory;
import ch.skyguide.message.structure.uap.DataCategoryUserApplicationProfile;
import ch.skyguide.message.structure.uap.UserApplicationProfile;


// ------------------------------------------------------------------------------

/*******************************************************************************
 * @author R. Wilson / Guillaume Fouillet / Marc-Antoine Galilée
 */
public final class AsterixUserApplicationProfileManager
{
    // --------------------------------------------------------------------------
    // public static final data members (constants)

    // --------------------------------------------------------------------------
    // public constructors

    // --------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
     * @param dataCategory
     *        The data category specifying the requested user application
     *        profile.
     * @return The <code>UserApplicationProfile</code> for the data category
     *         specified
     * @throws NotImplementedException
     *         Thrown when the user application profile is not implemented or
     *         supported.
     */
    public UserApplicationProfile getUserApplicationProfile(short dataCategory)
    {
        if (!m_UserApplicationProfiles.containsKey(Integer
                .valueOf(dataCategory))) {

            throw new NotImplementedException("The Asterix UAP Nr.: "
                    + dataCategory + ".");
        }

        return m_UserApplicationProfiles.get(Integer.valueOf(dataCategory));
    }

    // --------------------------------------------------------------------------
    // public static member functions

    /***************************************************************************
     * Retrieves the one and only instance of the User Application Profile
     * Manager.
     * 
     * @return The <code>UserApplicationProfileManager</code> instance.
     */
    public static AsterixUserApplicationProfileManager getInstance()
    {
        if (INSTANCE == null) {

            INSTANCE = new AsterixUserApplicationProfileManager();
        }

        return INSTANCE;
    }

    // --------------------------------------------------------------------------
    // protected constructors

    // --------------------------------------------------------------------------
    // protected member functions

    // --------------------------------------------------------------------------
    // protected static member functions

    // --------------------------------------------------------------------------
    // protected data members

    // --------------------------------------------------------------------------
    // protected static data members

    // --------------------------------------------------------------------------
    // package constructors

    // --------------------------------------------------------------------------
    // package member functions

    // --------------------------------------------------------------------------
    // package static member functions

    // --------------------------------------------------------------------------
    // package data members

    // --------------------------------------------------------------------------
    // package static data members

    // --------------------------------------------------------------------------
    // private constructors

    /***************************************************************************
    * 
    */
    private AsterixUserApplicationProfileManager()
    {
        initializeCat030();
        initializeCat062();
        initializeCat032();
        initializeCat252Service();
        initializeCat065Service();
        initializeFormerCat242Tracks();
        initializeCat242Tracks();
        initializeCat243Service();
    }

    // --------------------------------------------------------------------------
    // private member functions

    /***************************************************************************
     * Private helper method to initialize asterix category 030 user application
     * profile.
     */
    private void initializeCat030()
    {

        short byteRepresentation = DataCategoryType.CAT030
                .getByteRepresentation();

        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 56);

        uap.addDataItem(1, new IdentificationTag(byteRepresentation, 10,
                "Server Identification Tag", "n/a"));

        uap.addDataItem(2, new UserNumber(byteRepresentation, 15,
                "User Number", "n/a"));

        uap.addDataItem(3, new ServiceIdentification(byteRepresentation, 30,
                "Service Identification", "n/a"));

        uap.addDataItem(4, new TypeOfMessage(byteRepresentation, 35,
                "Type of Message", "n/a"));

        uap.addDataItem(5, new TrackNumber(byteRepresentation, 40,
                "Track Number", "n/a"));

        uap.addDataItem(6, new TimeOfMessage(byteRepresentation, 70,
                "Time Of Last Update", "1/128 s"));

        uap.addDataItem(7, new LoggingUniversalFixedDataItem(
                byteRepresentation, 170, "Track Ages", "1/4 s", 4));

        uap.addDataItem(8, new LoggingUniversalFixedDataItem(
                byteRepresentation, 100,
                "Calculated Track Position (Cartesian)", "1/32 or 1/64 nm", 4));

        uap.addDataItem(9, new LoggingUniversalFixedDataItem(
                byteRepresentation, 180, "Calculated Track Velocity (Polar)",
                "0.22kt/0.0055°", 4));

        uap.addDataItem(10, new LoggingUniversalFixedDataItem(
                byteRepresentation, 181,
                "Calculated Track Velocity (Cartesian)", "0.22kt", 4));

        uap.addDataItem(11, new TrackMode3A(byteRepresentation, 60,
                "Track Mode 3/A", "n/a"));

        uap.addDataItem(12, new LoggingUniversalFixedDataItem(
                byteRepresentation, 150, "Measured Track Mode C", "1/4 FL", 2));

        uap.addDataItem(13, new LoggingUniversalFixedDataItem(
                byteRepresentation, 130, "Calculated Track Altitude", "1/4 FL",
                2));

        uap.addDataItem(14, new LoggingUniversalFixedDataItem(
                byteRepresentation, 160, "Calculated Track Flight Level",
                "1/4 FL", 2));

        uap.addDataItem(15, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 80, "Artas Track Status", "n/a", 1));

        uap.addDataItem(16, new LoggingUniversalFixedDataItem(
                byteRepresentation, 90, "Artas Track Quality", "n/a", 1));

        uap.addDataItem(17, new LoggingUniversalFixedDataItem(
                byteRepresentation, 200, "Mode of Flight", "n/a", 1));

        uap.addDataItem(18, new LoggingUniversalFixedDataItem(
                byteRepresentation, 220, "Calculated Rate of Climb/Descent",
                "n/a", 2));

        uap.addDataItem(19, new LoggingUniversalFixedDataItem(
                byteRepresentation, 240, "Calculated Rate of Turn", "n/a", 1));

        uap.addDataItem(20, new LoggingUniversalFixedDataItem(
                byteRepresentation, 290, "Plot/Track Time Plot Ages", "n/a", 2));

        uap.addDataItem(21, new IdentificationTag(byteRepresentation, 260,
                "Radar Identification Tag", "n/a"));

        uap.addDataItem(22, new LoggingUniversalFixedDataItem(
                byteRepresentation, 360, "Measured Position",
                "1/128 nm/0.0055°", 4));

        uap.addDataItem(23, new LoggingUniversalFixedDataItem(
                byteRepresentation, 140, "Last Measured Mode C", "n/a", 2));

        uap.addDataItem(24, new LoggingUniversalFixedDataItem(
                byteRepresentation, 340, "Last Measured Mode 3/A", "1/4 FL", 2));
        uap.addDataItem(25, new LoggingUniversalFixedDataItem(
                byteRepresentation, 800, "Reserved Expansion Data Field",
                "n/a", 3));

        uap.addDataItem(26, new IdentificationTag(byteRepresentation, 390,
                "FPPS Identification Tag", "n/a"));

        uap.addDataItem(27, new AsterixText(byteRepresentation, 400,
                "Callsign", "n/a", 7));

        uap.addDataItem(28, new AsterixInteger(byteRepresentation, 410,
                "PLN Number", "n/a", 2));

        uap.addDataItem(29, new AsterixText(byteRepresentation, 440,
                "Departure Airport", "n/a", 4));

        uap.addDataItem(30, new AsterixText(byteRepresentation, 450,
                "Destination Airport", "n/a", 4));

        uap.addDataItem(31, new CategoryOfTurbulence(byteRepresentation, 435,
                "Category of Turbulence", "n/a"));

        uap.addDataItem(32, new AsterixText(byteRepresentation, 430,
                "Type of Aircraft", "n/a", 4));

        uap.addDataItem(33, new AllocatedSSRCodes(byteRepresentation, 460,
                "Allocated SSR Codes", "n/a"));

        uap.addDataItem(34, new LoggingUniversalFixedDataItem(
                byteRepresentation, 480, "Current Cleared Flight Level",
                "1/4 FL", 2));

        uap.addDataItem(35, new FlightCategory(byteRepresentation, 420,
                "Flight Category", "n/a"));

        uap.addDataItem(36, new ControlPosition(byteRepresentation, 490,
                "Current Control Position", "n/a"));

        uap.addDataItem(37, new TimeOfMessage(byteRepresentation, 20,
                "Time of Message", "1/128 s"));

        uap.addDataItem(38, new LoggingUniversalFixedDataItem(
                byteRepresentation, 382, "Aircraft Address", "n/a", 3));

        uap.addDataItem(39, new LoggingUniversalFixedDataItem(
                byteRepresentation, 384, "Aircraft Identification", "n/a", 6));

        uap.addDataItem(40, new LoggingUniversalFixedDataItem(
                byteRepresentation, 386,
                "Communication Capability and Flight Status", "n/a", 1));

        uap.addDataItem(41, new LoggingUniversalFixedDataItem(
                byteRepresentation, 110,
                "Estimated Accuracy of Track Position " + "(Cartesian)", "n/a",
                4));

        uap.addDataItem(42, new LoggingUniversalFixedDataItem(
                byteRepresentation, 190,
                "Estimated Accuracy of Track Velocity " + "(Polar)",
                "0.22kt/0.0055°", 4));

        uap.addDataItem(43, new LoggingUniversalFixedDataItem(
                byteRepresentation, 191,
                "Estimated Accuracy of Track Velocity " + "(Cartesian)",
                "0.22 kt", 4));

        uap.addDataItem(44, new LoggingUniversalFixedDataItem(
                byteRepresentation, 135,
                "Estimated Accuracy of Track Altitude", "n/a", 2));

        uap.addDataItem(45, new LoggingUniversalFixedDataItem(
                byteRepresentation, 165,
                "Estimated Accuracy of Calculated Track " + "Flight Level",
                "1/4 FL", 2));

        uap.addDataItem(46, new LoggingUniversalFixedDataItem(
                byteRepresentation, 230, "Estimated Accuracy of Rate of "
                        + "Climb/Descent", "5.86 ft/min", 2));

        uap.addDataItem(47, new LoggingUniversalFixedDataItem(
                byteRepresentation, 250, "Estimated Accuracy of Rate of Turn",
                "1/4°/s", 1));

        uap.addDataItem(48, new LoggingUniversalFixedDataItem(
                byteRepresentation, 210, "Mode of Flight Probabilities", "n/a",
                3));

        uap.addDataItem(49, new LoggingUniversalFixedDataItem(
                byteRepresentation, 120, "Track Mode 2 Code", "n/a", 2));

        uap.addDataItem(50, new ArtasTrackNumber(byteRepresentation, 50,
                "Artas Track Number", "n/a"));

        uap.addDataItem(51, new AsterixInteger(byteRepresentation, 270,
                "Local Track Number", "n/a", 2));

        uap.addDataItem(52, new LoggingUniversalFixedDataItem(
                byteRepresentation, 370, "Measured 3D Height", "1/4 FL", 2));

        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /***************************************************************************
     * Private helper method to initialize asterix category 032 user application
     * profile.
     */
    private void initializeCat032()
    {
        short byteRepresentation = DataCategoryType.CAT032
                .getByteRepresentation();

        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 18);

        uap.addDataItem(1, new IdentificationTag(byteRepresentation, 10,
                "Server Identification Tag", "n/a"));

        uap.addDataItem(2, new UserNumber(byteRepresentation, 15, "UserNumber",
                "n/a"));

        uap.addDataItem(3, new IdentificationTag(byteRepresentation, 18,
                "Data Source Identification Tag", "n/a"));

        uap.addDataItem(4, new TypeOfMessage(byteRepresentation, 35,
                "Type of Message", "n/a"));

        uap.addDataItem(5, new TimeOfMessage(byteRepresentation, 20,
                "Time of Message", "n/a"));

        uap.addDataItem(6, new TrackNumber(byteRepresentation, 40,
                "Track Number", "n/a"));

        uap.addDataItem(7, new ArtasTrackNumber(byteRepresentation, 50,
                "ARTAS Track Number", "n/a"));

        uap.addDataItem(8, new TrackMode3A(byteRepresentation, 60,
                "Track Mode 3/A", "n/a"));

        uap.addDataItem(9, new AsterixText(byteRepresentation, 400, "Callsign",
                "n/a", 7));

        uap.addDataItem(10, new AsterixInteger(byteRepresentation, 410,
                "PLN Number", "n/a", 2));

        uap.addDataItem(11, new FlightCategory(byteRepresentation, 420,
                "Flight Category", "n/a"));

        uap.addDataItem(12, new AsterixText(byteRepresentation, 440,
                "Departure Airport", "n/a", 4));

        uap.addDataItem(13, new AsterixText(byteRepresentation, 450,
                "Destination Airport", "n/a", 4));

        uap.addDataItem(14, new AsterixInteger(byteRepresentation, 480,
                "Current Cleared Flight Level", "0.25 FL", 2));

        uap.addDataItem(15, new ControlPosition(byteRepresentation, 490,
                "Current Control Position", "n/a"));

        uap.addDataItem(16, new AsterixText(byteRepresentation, 430,
                "Type Of Aircraft", "n/a", 4));

        uap.addDataItem(17, new CategoryOfTurbulence(byteRepresentation, 435,
                "Category of Turbulence", "n/a"));

        uap.addDataItem(18, new AllocatedSSRCodes(byteRepresentation, 460,
                "Allocated SSR Codes", "n/a"));

        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /***************************************************************************
     * Private helper method to initialize asterix category 062 user application
     * profile.
     */
    private void initializeCat062()
    {
    
        short byteRepresentation = DataCategoryType.CAT062
                .getByteRepresentation();
    
        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 35);
    
        // FN 1 - 062/010 - Data source identifier
        uap.addDataItem(1, new IdentificationTag(byteRepresentation, 10,
                "Data Source Identifier", "n/a"));
    
        // FN 2 - spare
    
        // FN 3 - 062/015 - Service identification not sent by BEST
        uap.addDataItem(3, new ServiceIdentification(byteRepresentation, 15,
                "Service Identification", "n/a"));
    
        // FN 4 - 062/070 - Time of track information
        uap.addDataItem(4, new TimeOfMessage(byteRepresentation, 70,
                "Time of track information", "1/128 s"));
    
        // FN 5 - 062/105 - Calculated Track Position (WGS-84) not sent by BEST
        uap.addDataItem(5, new LoggingUniversalFixedDataItem(
                byteRepresentation, 105, "Calculated Track Position (WGS-84)",
                "1/32 or 1/64 nm", 8));
    
        // FN 6 - 062/100 - Calculated Track Position (Cartesian)
        uap.addDataItem(6, new CartesianTrackPosition(
                byteRepresentation, 100, 6,
                "Calculated Track Position (Cartesian)", "1/32 or 1/64 nm"));
    
        // FN 7 - 062/185 - Calculated Track Velocity (Cartesian)
        uap.addDataItem(7, new CalculatedTrackVelocity(byteRepresentation, 185,
                "Calculated Track Velocity (Cartesian)", "n/a"));
    
        // FX - Field extension indicator
    
        // FN 8 - 062/210 - Calculated Acceleration (Cartesian) not sent by BEST
        uap.addDataItem(8, new LoggingUniversalFixedDataItem(
                byteRepresentation, 210, "Calculated Acceleration(Cartesian)",
                "0.25 m.s-2", 2));
    
        // FN 9 - 062/060 - Calculated Track Mode 3/A
        uap.addDataItem(9, new TrackMode3A(byteRepresentation, 60,
                "Track Mode 3/A", "n/a"));
    
        // FN 10 - 062/245 - Target identification not sent by BEST
        uap.addDataItem(10, new LoggingUniversalFixedDataItem(
                byteRepresentation, 245, "Target identification", "n/a", 7));
    
        // FN 11 - 062/380 - Aircraft derived data
        uap.addDataItem(
                11,
                new AircraftDerivedData(
                        byteRepresentation,
                        380,
                        "Aircraft derived data",
                        AircraftDerivedData
                                .initializeCat062_380SecondaryFields(byteRepresentation)));
    
        // FN 12 - 062/040 - Track number
        uap.addDataItem(12, new TrackNumber(byteRepresentation, 40,
                "Track Number", "n/a"));
    
        // FN 13 - 062/080 - Track status
        uap.addDataItem(13, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 80, "Track Status", "n/a", 1));
    
        // FN 14 - 062/290 - System Track Update Ages
        uap.addDataItem(
                14,
                new SystemTrackUpdateAges(
                        byteRepresentation,
                        290,
                        "System Track Update Ages",
                        SystemTrackUpdateAges
                                .initializeCat062_290SecondaryFields(byteRepresentation)));
    
        // FX - Field extension indicator
    
        // FN 15 - 062/200 - Mode of movement
        uap.addDataItem(15, new ModeOfMovement(byteRepresentation, 200,
                "Mode of movement", "n/a"));
    
        // FN 16 - 062/295 - Track data ages
        uap.addDataItem(
                16,
                new TrackDataAges(
                        byteRepresentation,
                        295,
                        "Track data ages",
                        TrackDataAges
                                .initializeCat062_295SecondaryFields(byteRepresentation)));
    
        // FN 17 - 062/136 - Measured flight level
        uap.addDataItem(17, new LoggingUniversalFixedDataItem(
                byteRepresentation, 136, "Measured Flight Level", "1/4 FL", 2));
    
        // FN 18 - 062/130 - Calculated track Geometric Altitude not sent by
        // BEST
        uap.addDataItem(18, new LoggingUniversalFixedDataItem(
                byteRepresentation, 130, "Calculated Track Geometric Altitude",
                "1/4 FL", 2));
    
        // FN 19 - 062/135 - Calculated track Barometric Altitude
        uap.addDataItem(19, new LoggingUniversalFixedDataItem(
                byteRepresentation, 135,
                "Calculated Track Barometric Altitude", "1/4 FL", 2));
    
        // FN 20 - 062/220 - Calculated Rate of Climb/Descent
        uap.addDataItem(20, new LoggingUniversalFixedDataItem(
                byteRepresentation, 220, "Calculated Rate of Climb/Descent",
                "n/a", 2));
    
        // FN 21 - 062/398 - Flight plan related data not sent by BEST
        uap.addDataItem(21, new LoggingUniversalFixedDataItem(
                byteRepresentation, 398, "Flight plan related data", "n/a", 4));
    
        // FX - Field extension indicator
    
        // FN 22 - 062/270 - Target size and orientation not sent by BEST
        uap.addDataItem(22, new LoggingUniversalFixedDataItem(
                byteRepresentation, 270, "Target size and orientation", "n/a",
                3));
    
        // FN 23 - 062/300 - Vehicle fleet identification not sent by BEST
        uap.addDataItem(23, new LoggingUniversalFixedDataItem(
                byteRepresentation, 300, "Vehicle fleet identification", "n/a",
                1));
    
        // FN 24 - 062/110 - Mode 5 Data reports and extended Mode 1 code not
        // sent by BEST
        uap.addDataItem(24, new LoggingUniversalFixedDataItem(
                byteRepresentation, 110,
                "Mode 5 Data reports and extended Mode 1 code", "n/a", 1));
    
        // FN 25 - 062/120 - Track Mode 2 Code not sent by BEST
        uap.addDataItem(25, new LoggingUniversalFixedDataItem(
                byteRepresentation, 120, "Track Mode 2 Code", "n/a", 2));
        // FN 26 - 062/510 - Composed track number not sent by BEST
        uap.addDataItem(26, new TrackNumber(byteRepresentation, 510,
                "Composed track Number", "n/a"));
    
        // FN 27 - 062/500 - Estimated accuracies not sent by BEST
        uap.addDataItem(27, new LoggingUniversalFixedDataItem(
                byteRepresentation, 500, "Estimated accuracies", "n/a", 1));
        // FN 28 - 062/340 - Measured Information
        uap.addDataItem(
                28,
                new MeasuredInformation(
                        byteRepresentation,
                        340,
                        "Measured Information",
                        MeasuredInformation
                                .initializeCat062_340SecondaryFields(byteRepresentation)));
    
        // FN 29 to 33 : spare fields
    
        // FN 34 : Reserved expansion field
        uap.addDataItem(
                34,
                new REForCat062(
                        byteRepresentation,
                        810,
                        "Reserved Expansion field",
                        REForCat062
                                .initializeCat062_RESecondaryFields(byteRepresentation)));
    
        // FN 35 : Reserved special purpose field
        uap.addDataItem(35, new LoggingUniversalReservedDataItem(
                byteRepresentation, 820, "Reserved Special purpose Data Field",
                "n/a", 1));
    
        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /***************************************************************************
     * Private helper method to initialize asterix category 065 user application
     * profile.
     */
    private void initializeCat065Service()
    {
    
        short byteRepresentation = DataCategoryType.CAT065
                .getByteRepresentation();
    
        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 7);
    
        uap.addDataItem(1, new IdentificationTag(byteRepresentation, 10,
                "Data Source Identifier", "n/a"));
    
        // TODO GFO : Check the object
        uap.addDataItem(2, new TypeOfMessage(byteRepresentation, 0,
                "Message Type", "n/a"));
    
        // FN 3 - Service identification is not sent by Best
        uap.addDataItem(3, new ServiceIdentification(
                byteRepresentation, 15, "Service Identification", "n/a"));
    
        uap.addDataItem(4, new TimeOfMessage(byteRepresentation, 30,
                "Time of Message", "n/a"));
    
        uap.addDataItem(5, new BatchNumber(byteRepresentation, 20,
                "Batch number"));
    
        // FN 6 - SDPS Configuration and status not sent by BEST
    
        // FN 7 - Service status report not sent by BEST
    
        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /***************************************************************************
     * Private helper method to initialize asterix category 242 V2 user
     * application profile. See
     * "MV Track Format 5 00 - 2012 02 14 (Released).doc".
     */
    private void initializeCat242Tracks()
    {
        short byteRepresentation = DataCategoryType.CAT242
                .getByteRepresentation();

        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 7);

        // FRN 1 - I242/010, MRT source
        uap.addDataItem(1, new MrtSource(
        		byteRepresentation, 10, "MRT source", "n/a"));
        // FRN 2 - I242/020, Time of track information
        uap.addDataItem(2, new TimeOfMessage(
                byteRepresentation, 20, "Time of track information", "1/128 s"));

        // FRN 3 - I242/030, Track number
        uap.addDataItem(3, new AsterixInteger(byteRepresentation, 30,
                "Track Number", "n/a", 2));

        // FRN 4 - I242/040, Local track number
        uap.addDataItem(4, new LocalTrackNumber(
                byteRepresentation, 40, "Local Track Number", "n/a"));

        // FRN 5 - I242/050, Track status
        uap.addDataItem(5, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 50, "Track status", "n/a", 2));

        // FRN 6 - I242/060, APW/DAIW track status
        uap.addDataItem(6, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 60, "APW/DAIW track status", "n/a", 2));

        // FRN 7 - I242/070, MSAW track status
        uap.addDataItem(7, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 70, "MSAW track status", "n/a", 2));

        // FRN 8 - I242/080, STCA track status
        uap.addDataItem(8, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 80, "STCA track status", "n/a", 2));

        // FRN 9 - I242/090, Flight plan status
        uap.addDataItem(9, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 90, "Flight plan status", "n/a", 2));

        // FRN 10 - I242/100, Track position in cartesian coordinates
        uap.addDataItem(10, new CartesianTrackPosition(
                byteRepresentation, 100,  8,
                "Track position in cartesian coordinates", "1/4096 NM"));

        // FRN 11 - I242/110, Track area membership
        uap.addDataItem(11, new LoggingUniversalFixedDataItem(
                byteRepresentation, 110, "Track area membership", "n/a", 2));

        // FRN 12 - I242/120, Track velocity in cartesian coordinates
        uap.addDataItem(12, new CalculatedTrackVelocity(
                byteRepresentation, 120,
                "Track velocity in cartesian coordinates", "1/16384 NM/s"));

        // FRN 13 - I242/121, Track velocity in polar coordinates
        uap.addDataItem(13, new LoggingUniversalFixedDataItem(
                byteRepresentation, 121, "Track velocity in polar coordinates",
                "1/16384 NM/s 360/65536°", 4));

        // FRN 14 - I242/130, Track altitude
        uap.addDataItem(14, new LoggingUniversalFixedDataItem(
                byteRepresentation, 130, "Track altitude", "5 ft", 4));

        // FRN 15 - I242/140, QNH area membership
        uap.addDataItem(15, new LoggingUniversalFixedDataItem(
                byteRepresentation, 140, "QNH area membership", "n/a", 2));

        // FRN 16 - I242/150, Rate of climb/descent
        uap.addDataItem(16, new LoggingUniversalFixedDataItem(
                byteRepresentation, 150, "Rate of climb/descent", "2^-10 FL/s",
                2));

        // FRN 17 - I242/160, Rate of turn
        uap.addDataItem(17, new LoggingUniversalFixedDataItem(
                byteRepresentation, 160, "Rate of turn", "1/4 °/s", 2));

        // FRN 18 - I242/170, Mode of flight
        uap.addDataItem(18, new LoggingUniversalFixedDataItem(
                byteRepresentation, 170, "Mode of flight", "n/a", 2));

        // FRN 19 - I242/180, Track mode 3/A
        uap.addDataItem(19, new LoggingUniversalFixedDataItem(
                byteRepresentation, 180, "Track mode 3/A", "n/a", 2));

        // FRN 20 - I242/190, Emergency mode 3/A
        uap.addDataItem(20, new LoggingUniversalFixedDataItem(
                byteRepresentation, 190, "Emergency mode 3/A", "n/a", 2));

        // FRN 21 - I242/200, Radar identification tag
        uap.addDataItem(21, new LoggingUniversalFixedDataItem(
                byteRepresentation, 200, "Radar identification tag", "n/a", 2));

        // FRN 22 - I242/201, Radar identification tag mnemonic
        uap.addDataItem(22, new LoggingUniversalFixedDataItem(
                byteRepresentation, 201, "Radar identification tag mnemonic",
                "ASCII", 2));

        // FRN 23 - I242/210, ACD sectors
        uap.addDataItem(23, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 210, "ACD sectors", "n/a", 1));

        // FRN 24 - I242/220, HLD sectors
        uap.addDataItem(24, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 220, "HLD sectors", "n/a", 1));

        // FRN 25 - I242/230, APW/DAIW sectors
        uap.addDataItem(25, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 230, "APW/DAIW sectors", "n/a", 1));

        // FRN 26 - I242/240, APW/DAIW vector
        uap.addDataItem(26, new LoggingUniversalFixedDataItem(
                byteRepresentation, 240, "APW/DAIW vector", "1/16384 NM/s", 4));

        // FRN 27 - I242/160, Rate of turn
        uap.addDataItem(27, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 160, "Rate of turn", "n/a", 1));

        // FRN 28 - I242/260, MSAW vector
        uap.addDataItem(28, new LoggingUniversalFixedDataItem(
                byteRepresentation, 260, "MSAW vector", "1/16384 NM/s", 4));

        // FRN 29 - I242/270, STCA sectors
        uap.addDataItem(29, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 270, "STCA sectors", "n/a", 1));

        // FRN 30 - I242/280, STCA vector
        uap.addDataItem(30, new LoggingUniversalFixedDataItem(
                byteRepresentation, 280, "STCA vector", "1/16384 NM/s", 4));

        // FRN 31 - I242/290, Ages (MV internal use only)
        uap.addDataItem(31, new LoggingUniversalFixedDataItem(
                byteRepresentation, 290, "Ages (MV internal use only)", "n/a",
                2));

        // FRN 32 - I242/185, previous Mode 3/A
        uap.addDataItem(32, new TrackMode3A(byteRepresentation, 185,
                "Previous Mode 3/A of the track", "N/A"));

        // FRN 33 - I242/300, Track mode C
        uap.addDataItem(33, new LoggingUniversalFixedDataItem(
                byteRepresentation, 300, "Track mode C", "n/a", 4));

        // FRN 34 - I242/9100, Reserved for future use
        uap.addDataItem(34, new LoggingUniversalFixedDataItem(
                byteRepresentation, 9100, "Reserved for future use", "n/a", 4));
        
        // FRN 35 - I242/9200, Reserved for future use
        uap.addDataItem(35, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 9200, "Reserved for future use", "n/a", 2));
        
        // FRN 36 - I242/9200, Reserved for future use
        uap.addDataItem(36, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 9200, "Reserved for future use", "n/a", 2));
        
        // FRN 37 - I242/9300, Reserved for future use
        uap.addDataItem(37, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 9300, "Reserved for future use", "n/a", 1));
        
        // FRN 38 - I242/9300, Reserved for future use
        uap.addDataItem(38, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 9300, "Reserved for future use", "n/a", 1));
        
        // FRN 39 - I242/9400, Reserved for future use
        uap.addDataItem(39, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 9400, "Reserved for future use", "n/a", 2));
        
        // FRN 40 - I242/9400, Reserved for future use
        uap.addDataItem(40, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 9400, "Reserved for future use", "n/a", 2));
        
        // FRN 41 - I242/9500, Reserved for future use
        uap.addDataItem(41, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 9500, "Reserved for future use", "n/a", 1));
        
        // FRN 42 - I242/9500, Reserved for future use
        uap.addDataItem(42, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 9500, "Reserved for future use", "n/a",
                1));
        
        // FRN 43 - I242/9500, Reserved for future use
        uap.addDataItem(43, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 9500, "Reserved for future use", "n/a",
                1));
        
        // FRN 44 - I242/9500, Reserved for future use
        uap.addDataItem(44, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 9500, "Reserved for future use", "n/a",
                1));
        
        // FRN 45 - I242/9500, Reserved for future use
        uap.addDataItem(45, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 9500, "Reserved for future use", "n/a", 1));

        // FRN 46 - I242/500, Callsign
        uap.addDataItem(46, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 500, "Callsign", "ASCII", 1));

        // FRN 47 - I242/510, Controlling sector
        uap.addDataItem(47, new AsterixInteger(byteRepresentation, 510,
                "Controlling sector", "n/a", 1));

        // FRN 48 - I242/520, Controlling logical sector
        uap.addDataItem(48,
                new LoggingUniversalFixedDataItem(byteRepresentation, 520,
                        "Controlling logical sector", "n/a", 1));

        // FRN 49 - I242/530, Controlling console
        uap.addDataItem(49, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 530, "Controlling console", "n/a", 1));

        // FRN 50 - I242/540, Handover sector
        uap.addDataItem(50, new AsterixInteger(byteRepresentation, 540,
                "Handover sector", "n/a", 1));

        // FRN 51 - I242/550, Handover logical sector
        uap.addDataItem(51, new AsterixInteger(byteRepresentation, 550,
                "Handover logical sector", "n/a", 1));

        // FRN 52 - I242/560, Handover console
        uap.addDataItem(52, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 560, "Handover console", "n/a", 1));

        // FRN 53 - I242/570, SI field
        uap.addDataItem(53, new LoggingUniversalFixedDataItem(
                byteRepresentation, 570, "SI field", "n/a", 2));

        // FRN 54 - I242/580, Aircraft type
        uap.addDataItem(54, new LoggingUniversalFixedDataItem(
                byteRepresentation, 580, "Aircraft type", "ASCII", 4));

        // FRN 55 - I242/590, Wake turbulence category
        uap.addDataItem(55,
                new LoggingUniversalFixedDataItem(byteRepresentation, 590,
                        "Wake turbulence category", "ASCII", 1));

        // FRN 56 - I242/600, Aircraft company
        uap.addDataItem(56, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 600, "Aircraft company", "ASCII", 1));

        // FRN 57 - I242/610, PTID1
        uap.addDataItem(57, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 610, "PTID1", "ASCII", 1));

        // FRN 58 - I242/620, PTID2
        uap.addDataItem(58, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 620, "PTID2", "ASCII", 1));

        // FRN 59 - I242/630, PTID3
        uap.addDataItem(59, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 630, "PTID3", "ASCII", 1));

        // FRN 60 - I242/640, PTID4
        uap.addDataItem(60, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 640, "PTID4", "ASCII", 1));

        // FRN 61 - I242/650, PTID5
        uap.addDataItem(61, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 650, "PTID5", "ASCII", 1));

        // FRN 62 - I242/660, FL1
        uap.addDataItem(62, new LoggingUniversalFixedDataItem(
                byteRepresentation, 660, "FL1", "1/4 FL", 2));

        // FRN 63 - I242/670, FL2
        uap.addDataItem(63, new LoggingUniversalFixedDataItem(
                byteRepresentation, 670, "FL2", "1/4 FL", 2));

        // FRN 64 - I242/680, FL3
        uap.addDataItem(64, new LoggingUniversalFixedDataItem(
                byteRepresentation, 680, "FL3", "1/4 FL", 2));

        // FRN 65 - I242/690, FL4
        uap.addDataItem(65, new LoggingUniversalFixedDataItem(
                byteRepresentation, 690, "FL4", "1/4 FL", 2));

        // FRN 66 - I242/700, FL5
        uap.addDataItem(66, new LoggingUniversalFixedDataItem(
                byteRepresentation, 700, "FL5", "1/4 FL", 2));

        // FRN 67 - I242/710, CFL
        uap.addDataItem(67, new LoggingUniversalFixedDataItem(
                byteRepresentation, 710, "CFL", "1/4 FL", 2));

        // FRN 68 - I242/720, ADEP
        uap.addDataItem(68, new LoggingUniversalFixedDataItem(
                byteRepresentation, 720, "ADEP", "ASCII", 4));

        // FRN 69 - I242/730, ADES
        uap.addDataItem(69, new LoggingUniversalFixedDataItem(
                byteRepresentation, 730, "ADES", "ASCII", 4));

        // FRN 70 - I242/740, Allocated SSR code
        uap.addDataItem(70, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 740, "Allocated SSR Code", "n/a", 2));

        // FRN 71 - I242/750, Allocated heading
        uap.addDataItem(71, new LoggingUniversalFixedDataItem(
                byteRepresentation, 750, "Allocated heading", "360/65536 °", 2));

        // FRN 72 - I242/760, Aircraft address
        uap.addDataItem(72, new LoggingUniversalFixedDataItem(
                byteRepresentation, 760, "Aircraft address", "n/a", 3));

        // FRN 73 - I242/770, Aircraft identification
        uap.addDataItem(73, new LoggingUniversalFixedDataItem(
                byteRepresentation, 770, "Aircraft identification", "ASCII", 8));

        // FRN 74 - I242/780, Communications/ACAS Capability and Flight Status
        uap.addDataItem(74, new LoggingUniversalFixedDataItem(
                byteRepresentation, 780,
                "Communications/ACAS Capability and Flight Status", "n/a", 2));

        // FRN 75 - I242/790, ADD Magnetic Heading
        uap.addDataItem(75, new LoggingUniversalFixedDataItem(
                byteRepresentation, 790,
                "Aircraft Derived Data Magnetic Heading (MHG)", "360/65536 °",
                2));

        // FRN 76 - I242/800, ADD Selected Altitude
        uap.addDataItem(76, new LoggingUniversalFixedDataItem(
                byteRepresentation, 800,
                "Aircraft Derived Data Selected Altitude (SAL)", "ft", 2));

        // FRN 77 - I242/810, ADD ACAS Resolution Advisory Report
        uap.addDataItem(
                77,
                new LoggingUniversalFixedDataItem(
                        byteRepresentation,
                        810,
                        "Aircraft Derived Data ACAS Resolution Advisory Report - simplified (ACS)",
                        "n/a", 2));

        // FRN 78 - I242/820, ADD Vertical Rate
        uap.addDataItem(78, new LoggingUniversalFixedDataItem(
                byteRepresentation, 820,
                "Aircraft Derived Data Vertical Rate (VR)", "FL/s", 2));

        // FRN 79 - I242/830, ADD Roll Angle
        uap.addDataItem(79, new LoggingUniversalFixedDataItem(
                byteRepresentation, 830,
                "Aircraft Derived Data Roll Angle (RAN)", "360/65536 °", 2));

        // FRN 80 - I242/840, ADD Ground Speed
        uap.addDataItem(80, new LoggingUniversalFixedDataItem(
                byteRepresentation, 840,
                "Aircraft Derived Data Ground Speed (GSP)", "1/16384 NM/s", 2));

        // FRN 81 - I242/850, ADD Indicated Speed
        uap.addDataItem(81, new LoggingUniversalFixedDataItem(
                byteRepresentation, 850,
                "Aircraft Derived Data Indicated Airspeed (IAR)",
                "1/16384 NM/s", 2));

        // FRN 82 - I242/860, ADD Mach Number
        uap.addDataItem(82, new LoggingUniversalFixedDataItem(
                byteRepresentation, 860,
                "Aircraft Derived Data Mach Number (MAC)", "Ma", 2));

        // FRN 83 - I242/870, Wind in polar coordinates
        uap.addDataItem(83, new LoggingUniversalFixedDataItem(
                byteRepresentation, 870, "Wind expressed in polar coordinates",
                "1/16384 NM/s - 360/65536 °", 4));

        // FRN 84 - I242/880, Previous Callsign
        uap.addDataItem(84, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 880,
                "Previous callsign of the track in ASCII format", "n/a", 1));

        // FRN 85 - I242/890, C-ATSU
        uap.addDataItem(85, new LoggingUniversalFixedDataItem(
                byteRepresentation, 890,
                "Current ATS unit assuming the flight", "n/a", 4));

        // FRN 85 - I242/890, C-ATSU
        uap.addDataItem(85, new LoggingUniversalFixedDataItem(
                byteRepresentation, 890,
                "Current ATS unit assuming the flight", "n/a", 4));

        // FRN 86 - I242/900, Transmitting physical sector (T-sector; T-PS)
        uap.addDataItem(
                86,
                new LoggingUniversalFixedDataItem(
                        byteRepresentation,
                        900,
                        "Identification of the transmitting physical sector " +
                        "for a correlated track which is transferred between " +
                        "2 sectors",
                        "n/a", 1));

        // FRN 87 - I242/910, Transmitting logical sector (T-sector; T-LS)
        uap.addDataItem(
                87,
                new LoggingUniversalFixedDataItem(
                        byteRepresentation,
                        910,
                        "Identification of the transmitting logical sector " +
                        "for a correlated track which is transferred between " +
                        "2 sectors",
                        "n/a", 1));

        // FRN 88 - I242/920, T-sector's consoles
        uap.addDataItem(88, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 920,
                "Consoles' list of the the physical transmitting sector",
                "n/a", 1));

        // FRN 89 - I242/930, ADD Barometric Pressure Setting
        uap.addDataItem(89, new LoggingUniversalFixedDataItem(
                byteRepresentation, 930,
                "barometric pressure setting of the aircraft", "n/a", 2));

        // FRN 90 - I242/9000, Reserved for future use
        uap.addDataItem(90, new LoggingUniversalFixedDataItem(
                byteRepresentation, 9000, "Reserved for future use", "n/a", 2));
        // FRN 91 - I242/9000, Reserved for future use
        uap.addDataItem(91, new LoggingUniversalFixedDataItem(
                byteRepresentation, 9000, "Reserved for future use", "n/a", 2));
        // FRN 92 - I242/9100, Reserved for future use
        uap.addDataItem(92, new LoggingUniversalFixedDataItem(
                byteRepresentation, 9100, "Reserved for future use", "n/a", 4));
        // FRN 93 - I242/9100, Reserved for future use
        uap.addDataItem(93, new LoggingUniversalFixedDataItem(
                byteRepresentation, 9100, "Reserved for future use", "n/a", 4));
        // FRN 94 - I242/9200, Reserved for future use
        uap.addDataItem(94, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 9200, "Reserved for future use", "n/a", 2));
        // FRN 95 - I242/9200, Reserved for future use
        uap.addDataItem(95, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 9200, "Reserved for future use", "n/a", 2));
        // FRN 96 - I242/9300, Reserved for future use
        uap.addDataItem(96, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 9300, "Reserved for future use", "n/a", 1));
        // FRN 97 - I242/9400, Reserved for future use
        uap.addDataItem(97, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 9400, "Reserved for future use", "n/a", 2));
        // FRN 98 - I242/9500, Reserved for future use
        uap.addDataItem(98, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 9500, "Reserved for future use", "n/a", 1));

        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /***************************************************************************
     * Private helper method to initialize asterix category 243 user application
     * profile. See "MV Track Format 5.00 - 2012.02.14(Released).doc".
     */
    private void initializeCat243Service()
    {
        short byteRepresentation = DataCategoryType.CAT243
                .getByteRepresentation();
    
        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 7);
    
        // FRN 1 - I243/010, MRT source
        uap.addDataItem(1, new MrtSource(
                byteRepresentation, 10, "Data Source Identifier", "n/a"));
    
        // FRN 2 - I242/020, Time of Message
        uap.addDataItem(2, new TimeOfMessage(
                byteRepresentation, 20, "Time of track information", "1/128 s"));
    
        // FRN 3 - I243/030, Flow status
        uap.addDataItem(3, new FlowStatus(
                byteRepresentation, 30, "Flow status", "n/a"));
    
        // FRN 4 - I243/040, Total number of strips
        uap.addDataItem(4, new LoggingUniversalFixedDataItem(
                byteRepresentation, 40, "Total number of strips", "n/a", 1));
    
        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /***************************************************************************
     * Private helper method to initialize asterix category 252 user application
     * profile.
     * 
     * TODO RJW : Check why this method is never used
     */
    @SuppressWarnings("unused")
    @Deprecated
    private void initializeCat252ConnectionControl()
    {

        short byteRepresentation = DataCategoryType.CAT252
                .getByteRepresentation();

        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 13);

        uap.addDataItem(1, new IdentificationTag(byteRepresentation, 10,
                "Server Identification Tag", "n/a"));

        uap.addDataItem(2, new UserNumber(byteRepresentation, 15, "UserNumber",
                "n/a"));

        uap.addDataItem(3, new TimeOfMessage(byteRepresentation, 20,
                "Time of Message", "n/a"));
        uap.addDataItem(4, new TypeOfMessage(byteRepresentation, 35,
                "Type of Message", "n/a"));
        uap.addDataItem(5, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 100, "Connection Related Report", "n/a", 1));

        uap.addDataItem(6, new LoggingUniversalFixedDataItem(
                byteRepresentation, 40, "Access Key", "n/a", 8));

        uap.addDataItem(7, new LoggingUniversalFixedDataItem(
                byteRepresentation, 45, "Role and Version", "n/a", 1));

        uap.addDataItem(8, new LoggingUniversalFixedDataItem(
                byteRepresentation, 50, "Default Connection Options", "n/a", 1));

        uap.addDataItem(9, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 60, "Geographical Area", "n/a", 6));

        uap.addDataItem(10, new LoggingUniversalFixedDataItem(
                byteRepresentation, 70, "Lower Limit", "n/a", 2));

        uap.addDataItem(10, new LoggingUniversalFixedDataItem(
                byteRepresentation, 80, "Upper Limit", "n/a", 2));

        uap.addDataItem(11, new LoggingUniversalFixedDataItem(
                byteRepresentation, 80, "Upper Limit", "n/a", 2));

        uap.addDataItem(12, new LoggingUniversalFixedDataItem(
                byteRepresentation, 90, "Preferred FPPS Identification Tag",
                "n/a", 2));

        uap.addDataItem(13, new LoggingUniversalFixedDataItem(
                byteRepresentation, 340, "Scaling Factor", "n/a", 1));

        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /***************************************************************************
     * Private helper method to initialize asterix category 252 user application
     * profile.
     */
    private void initializeCat252Service()
    {

        short byteRepresentation = DataCategoryType.CAT252
                .getByteRepresentation();

        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 45);

        uap.addDataItem(1, new IdentificationTag(byteRepresentation, 10,
                "Server Identification Tag", "n/a"));

        uap.addDataItem(2, new UserNumber(byteRepresentation, 15, "UserNumber",
                "n/a"));

        uap.addDataItem(3, new TimeOfMessage(byteRepresentation, 20,
                "Time of Message", "n/a"));
        uap.addDataItem(4, new TypeOfMessage(byteRepresentation, 35,
                "Type of Message", "n/a"));
        uap.addDataItem(5, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 110, "Service Identification", "n/a", 1));

        uap.addDataItem(6, new ServiceRelatedReport(byteRepresentation, 330,
                "Service Related Report", "n/a"));

        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    

    // --------------------------------------------------------------------------
    // private data members

    /***************************************************************************
     * Private helper method to initialize asterix category 242 user application
     * profile. See "MV Track Format 1.13 - 2010.11.10(Release).doc".
     */
    @Deprecated
    private void initializeFormerCat242Tracks()
    {
        short byteRepresentation = DataCategoryType.CAT242
                .getByteRepresentation();
    
        UserApplicationProfile uap = new DataCategoryUserApplicationProfile(
                byteRepresentation, 7);
    
        // FRN 1 - I242/010, MRT source
        uap.addDataItem(1, new LoggingUniversalFixedDataItem(
                byteRepresentation, 10, "MRT source", "n/a", 2));
        // FRN 2 - I242/020, Time of track information
        uap.addDataItem(2, new LoggingUniversalFixedDataItem(
                byteRepresentation, 20, "Time of track information", "1/128 s",
                3));
    
        // FRN 3 - I242/030, Track number
        uap.addDataItem(3, new AsterixInteger(byteRepresentation, 30,
                "Track Number", "n/a", 2));
    
        // FRN 4 - I242/040, Local track number
        uap.addDataItem(4, new LoggingUniversalFixedDataItem(
                byteRepresentation, 40, "Local Track Number", "n/a", 2));
    
        // FRN 5 - I242/050, Track status
        uap.addDataItem(5, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 50, "Track status", "n/a", 2));
    
        // FRN 6 - I242/060, APW/DAIW track status
        uap.addDataItem(6, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 60, "APW/DAIW track status", "n/a", 2));
    
        // FRN 7 - I242/070, MSAW track status
        uap.addDataItem(7, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 70, "MSAW track status", "n/a", 2));
    
        // FRN 8 - I242/080, STCA track status
        uap.addDataItem(8, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 80, "STCA track status", "n/a", 2));
    
        // FRN 9 - I242/090, Flight plan status
        uap.addDataItem(9, new LoggingUniversalExtendedDataItem(
                byteRepresentation, 90, "Flight plan status", "n/a", 2));
    
        // FRN 10 - I242/100, Track position in cartesian coordinates
        uap.addDataItem(10, new LoggingUniversalFixedDataItem(
                byteRepresentation, 100,
                "Track position in cartesian coordinates", "1/4096 NM", 8));
    
        // FRN 11 - I242/110, Track area membership
        uap.addDataItem(11, new LoggingUniversalFixedDataItem(
                byteRepresentation, 110, "Track area membership", "n/a", 2));
    
        // FRN 12 - I242/120, Track velocity in cartesian coordinates
        uap.addDataItem(12, new LoggingUniversalFixedDataItem(
                byteRepresentation, 120,
                "Track velocity in cartesian coordinates", "1/16384 NM/s", 4));
    
        // FRN 13 - I242/121, Track velocity in polar coordinates
        uap.addDataItem(13, new LoggingUniversalFixedDataItem(
                byteRepresentation, 121, "Track velocity in polar coordinates",
                "1/16384 NM/s 360/65536°", 4));
    
        // FRN 14 - I242/130, Track altitude
        uap.addDataItem(14, new LoggingUniversalFixedDataItem(
                byteRepresentation, 130, "Track altitude", "5 ft", 4));
    
        // FRN 15 - I242/140, QNH area membership
        uap.addDataItem(15, new LoggingUniversalFixedDataItem(
                byteRepresentation, 140, "QNH area membership", "n/a", 2));
    
        // FRN 16 - I242/150, Rate of climb/descent
        uap.addDataItem(16, new LoggingUniversalFixedDataItem(
                byteRepresentation, 150, "Rate of climb/descent", "2^-10 FL/s",
                2));
    
        // FRN 17 - I242/160, Rate of turn
        uap.addDataItem(17, new LoggingUniversalFixedDataItem(
                byteRepresentation, 160, "Rate of turn", "1/4 °/s", 2));
    
        // FRN 18 - I242/170, Mode of flight
        uap.addDataItem(18, new LoggingUniversalFixedDataItem(
                byteRepresentation, 170, "Mode of flight", "n/a", 2));
    
        // FRN 19 - I242/180, Track mode 3/A
        uap.addDataItem(19, new LoggingUniversalFixedDataItem(
                byteRepresentation, 180, "Track mode 3/A", "n/a", 2));
    
        // FRN 20 - I242/190, Emergency mode 3/A
        uap.addDataItem(20, new LoggingUniversalFixedDataItem(
                byteRepresentation, 190, "Emergency mode 3/A", "n/a", 2));
    
        // FRN 21 - I242/200, Radar identification tag
        uap.addDataItem(21, new LoggingUniversalFixedDataItem(
                byteRepresentation, 200, "Radar identification tag", "n/a", 2));
    
        // FRN 22 - I242/201, Radar identification tag mnemonic
        uap.addDataItem(22, new LoggingUniversalFixedDataItem(
                byteRepresentation, 201, "Radar identification tag mnemonic",
                "ASCII", 2));
    
        // FRN 23 - I242/210, ACD sectors
        uap.addDataItem(23, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 210, "ACD sectors", "n/a", 1));
    
        // FRN 24 - I242/220, HLD sectors
        uap.addDataItem(24, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 220, "HLD sectors", "n/a", 1));
    
        // FRN 25 - I242/230, APW/DAIW sectors
        uap.addDataItem(25, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 230, "APW/DAIW sectors", "n/a", 1));
    
        // FRN 26 - I242/240, APW/DAIW vector
        uap.addDataItem(26, new LoggingUniversalFixedDataItem(
                byteRepresentation, 240, "APW/DAIW vector", "1/16384 NM/s", 4));
    
        // FRN 27 - I242/160, Rate of turn
        uap.addDataItem(27, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 160, "Rate of turn", "n/a", 1));
    
        // FRN 28 - I242/260, MSAW vector
        uap.addDataItem(28, new LoggingUniversalFixedDataItem(
                byteRepresentation, 260, "MSAW vector", "1/16384 NM/s", 4));
    
        // FRN 29 - I242/270, STCA sectors
        uap.addDataItem(29, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 270, "STCA sectors", "n/a", 1));
    
        // FRN 30 - I242/280, STCA vector
        uap.addDataItem(30, new LoggingUniversalFixedDataItem(
                byteRepresentation, 280, "STCA vector", "1/16384 NM/s", 4));
    
        // FRN 31 - I242/290, Ages (MV internal use only)
        uap.addDataItem(31, new LoggingUniversalFixedDataItem(
                byteRepresentation, 290, "Ages (MV internal use only)", "n/a",
                2));
    
        // FRN 33 - I242/300, Track mode C
        uap.addDataItem(33, new LoggingUniversalFixedDataItem(
                byteRepresentation, 300, "Track mode C", "n/a", 4));
    
        // FRN 46 - I242/500, Callsign
        uap.addDataItem(46, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 500, "Callsign", "ASCII", 1));
    
        // FRN 47 - I242/510, Controlling sector
        uap.addDataItem(47, new AsterixInteger(byteRepresentation, 510,
                "Controlling sector", "n/a", 1));
    
        // FRN 48 - I242/520, Controlling logical sector
        uap.addDataItem(48,
                new LoggingUniversalFixedDataItem(byteRepresentation, 520,
                        "Controlling logical sector", "n/a", 1));
    
        // FRN 49 - I242/530, Controlling console
        uap.addDataItem(49, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 530, "Controlling console", "n/a", 1));
    
        // FRN 50 - I242/540, Handover sector
        uap.addDataItem(50, new AsterixInteger(byteRepresentation, 540,
                "Handover sector", "n/a", 1));
    
        // FRN 51 - I242/550, Handover logical sector
        uap.addDataItem(51, new AsterixInteger(byteRepresentation, 550,
                "Handover logical sector", "n/a", 1));
    
        // FRN 52 - I242/560, Handover console
        uap.addDataItem(52, new LoggingUniversalRepetitiveDataItem(
                byteRepresentation, 560, "Handover console", "n/a", 1));
    
        // FRN 53 - I242/570, SI field
        uap.addDataItem(53, new LoggingUniversalFixedDataItem(
                byteRepresentation, 570, "SI field", "n/a", 2));
    
        // FRN 54 - I242/580, Aircraft type
        uap.addDataItem(54, new LoggingUniversalFixedDataItem(
                byteRepresentation, 580, "Aircraft type", "ASCII", 4));
    
        // FRN 55 - I242/590, Wake turbulence category
        uap.addDataItem(55,
                new LoggingUniversalFixedDataItem(byteRepresentation, 590,
                        "Wake turbulence category", "ASCII", 1));
    
        // FRN 56 - I242/600, Aircraft company
        uap.addDataItem(56, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 600, "Aircraft company", "ASCII", 1));
    
        // FRN 57 - I242/610, PTID1
        uap.addDataItem(57, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 610, "PTID1", "ASCII", 1));
    
        // FRN 58 - I242/620, PTID2
        uap.addDataItem(58, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 620, "PTID2", "ASCII", 1));
    
        // FRN 59 - I242/630, PTID3
        uap.addDataItem(59, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 630, "PTID3", "ASCII", 1));
    
        // FRN 60 - I242/640, PTID4
        uap.addDataItem(60, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 640, "PTID4", "ASCII", 1));
    
        // FRN 61 - I242/650, PTID5
        uap.addDataItem(61, new LoggingLengthPrefixUniversalRepetitiveDataItem(
                byteRepresentation, 650, "PTID5", "ASCII", 1));
    
        // FRN 62 - I242/660, FL1
        uap.addDataItem(62, new LoggingUniversalFixedDataItem(
                byteRepresentation, 660, "FL1", "1/4 FL", 2));
    
        // FRN 63 - I242/670, FL2
        uap.addDataItem(63, new LoggingUniversalFixedDataItem(
                byteRepresentation, 670, "FL2", "1/4 FL", 2));
    
        // FRN 64 - I242/680, FL3
        uap.addDataItem(64, new LoggingUniversalFixedDataItem(
                byteRepresentation, 680, "FL3", "1/4 FL", 2));
    
        // FRN 65 - I242/690, FL4
        uap.addDataItem(65, new LoggingUniversalFixedDataItem(
                byteRepresentation, 690, "FL4", "1/4 FL", 2));
    
        // FRN 66 - I242/700, FL5
        uap.addDataItem(66, new LoggingUniversalFixedDataItem(
                byteRepresentation, 700, "FL5", "1/4 FL", 2));
    
        // FRN 67 - I242/710, CFL
        uap.addDataItem(67, new LoggingUniversalFixedDataItem(
                byteRepresentation, 710, "CFL", "1/4 FL", 2));
    
        // FRN 68 - I242/720, ADEP
        uap.addDataItem(68, new LoggingUniversalFixedDataItem(
                byteRepresentation, 720, "ADEP", "ASCII", 4));
    
        // FRN 69 - I242/730, ADES
        uap.addDataItem(69, new LoggingUniversalFixedDataItem(
                byteRepresentation, 730, "ADES", "ASCII", 4));
    
        // FRN 70 - I242/740, Allocated SSR code
        // currently not used -> implement it as soon as MV-NT uses it
    
        // FRN 71 - I242/750, Allocated heading
        uap.addDataItem(71, new LoggingUniversalFixedDataItem(
                byteRepresentation, 750, "Allocated heading", "360/65536 °", 2));
    
        // FRN 72 - I242/760, Aircraft address
        uap.addDataItem(72, new LoggingUniversalFixedDataItem(
                byteRepresentation, 760, "Aircraft address", "n/a", 3));
    
        // FRN 73 - I242/770, Aircraft identification
        uap.addDataItem(73, new LoggingUniversalFixedDataItem(
                byteRepresentation, 770, "Aircraft identification", "ASCII", 8));
    
        // FRN 74 - I242/780, Communications/ACAS Capability and Flight Status
        uap.addDataItem(74, new LoggingUniversalFixedDataItem(
                byteRepresentation, 780,
                "Communications/ACAS Capability and Flight Status", "n/a", 2));
    
        m_UserApplicationProfiles.put(Integer.valueOf(byteRepresentation), uap);
    }

    /**
     * The stored User Application Profile per Category Number.
     */
    private final Map<Integer, UserApplicationProfile> m_UserApplicationProfiles = new HashMap<>();

    // --------------------------------------------------------------------------
    // private static data members

    /**
     * The one and only instance of the User Application Profile Manager
     * Singleton
     */
    private static @Nullable
    AsterixUserApplicationProfileManager INSTANCE = null;

    // --------------------------------------------------------------------------
    // Static initialisation block
}
