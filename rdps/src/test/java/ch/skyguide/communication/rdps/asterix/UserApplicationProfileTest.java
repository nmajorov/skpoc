/**
 * 
 */
package ch.skyguide.communication.rdps.asterix;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.skyguide.message.structure.uap.DataCategoryUserApplicationProfile;
import ch.skyguide.message.structure.uap.UserApplicationProfile;

/**
 * @author  Christoph Schmitz
 * @author  Nicolas Buisson
 *
 */
public class UserApplicationProfileTest
{
    private short dataCategory;
    private int dataItemCapacity;
    private static short i = 0;
    private int arrayOfDataItemCapacity[] = {0, 1, 42, 256, 257};
    
    @Before public void setUp() {
        dataCategory = 0;
        dataItemCapacity = arrayOfDataItemCapacity[i];
    }
    
    @After public void nextStep() {
        i++;
    }
    
    @Test(expected=IllegalArgumentException.class)
    @Ignore
    public void testUserApplicationProfileNull(){ 
        new DataCategoryUserApplicationProfile(dataCategory, dataItemCapacity);        
    } 
    
    @Test
    @Ignore
    public void testUserApplicationProfileItemNumberOne(){
        UserApplicationProfile profile =
            new DataCategoryUserApplicationProfile(dataCategory, dataItemCapacity);
        
        assertEquals(Short.valueOf(dataCategory),
                     Short.valueOf(profile.getDataCategory()));
    }
    
    @Test
    @Ignore
    public void testUserApplicationProfileItemNumberFortyTwo(){

        UserApplicationProfile profile =
            new DataCategoryUserApplicationProfile(dataCategory, dataItemCapacity);
        
        assertEquals(Short.valueOf(dataCategory),
                     Short.valueOf(profile.getDataCategory()));
    }
    
    @Test
    @Ignore
    public void testUserApplicationProfileItemNumberTwoHundredFiftySix(){
        UserApplicationProfile profile =
            new DataCategoryUserApplicationProfile(dataCategory, dataItemCapacity);
        
        assertEquals(Short.valueOf(dataCategory),
                     Short.valueOf(profile.getDataCategory()));
    }
    
    @Test(expected=IllegalArgumentException.class)
    @Ignore
    public void testUserApplicationProfileItemNumberTwoHundredFiftySeven(){
        new DataCategoryUserApplicationProfile(dataCategory, dataItemCapacity);
    }
    
}
