package ch.skyguide.ei.prototype.foservice.forepository;

import java.util.HashSet;
import java.util.Set;

import ch.skyguide.ei.prototype.foservice.flightobject.FlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IFlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.flightobject.IUniversalTimeCoordinates;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;


public class FlightObjectRepository 
implements IFlightObjectRepository {
	
	
	public FlightObjectRepository(final String databasePath)
			throws IllegalArgumentException {

		if(databasePath == null) {
			
			throw new IllegalArgumentException("Non-initialized database path.");
		}
		
		m_databaseConnection = initialiseDatabase(databasePath);
	}
	
	@Override
	public void createFlightObject(IFlightObject flightObject)
			throws FlightObjectRepositoryException {
		
		if (flightObject == null) {
			
			throw new IllegalArgumentException("Non-initialised Flight Object");
		}
		
		m_databaseConnection.store(flightObject);
	}



	@Override
	public IFlightObject retrieveFlightObject(
			final IGloballyUniqueFlightIdentifier gufi)
			throws FlightObjectRepositoryException {

		if (gufi == null) {

			throw new IllegalArgumentException("Non-initialised gufi.");
		}

		final ObjectSet<IFlightObject> result = m_databaseConnection
				.query(new Predicate<IFlightObject>() {

					/**
					 * Serialization identifier
					 */
					private static final long serialVersionUID = 633927806269107263L;

					@Override
					public boolean match(IFlightObject flightObject) {

						return flightObject.getGloballyUniqueFlightIdentifier().equals(gufi);
					}
				});
		
		if(result.isEmpty()) {
			
			throw new FlightObjectRepositoryException("Flight Object not "
					+ "found in repository with GUFI: " + gufi.getIdentifier());
		}
		
		return result.get(0);
	}



	@Override
	public IFlightObject 
	retrieveFlightObject(final String aircraftIdentification,
			final String aerodromeOfDepartureDesignator,
			final String aerodromeOfDestinationDesignator,
			final IUniversalTimeCoordinates estimatedOffBlockTime)
			throws FlightObjectRepositoryException {

		if (aircraftIdentification == null) {

			throw new IllegalArgumentException("Non-initialised aircraftIdentification.");
		}

		if (aerodromeOfDepartureDesignator == null) {

			throw new IllegalArgumentException("Non-initialised aerodromeOfDepartureDesignator.");
		}

		if (aerodromeOfDestinationDesignator == null) {

			throw new IllegalArgumentException("Non-initialised aerodromeOfDestinationDesignator.");
		}

		if (estimatedOffBlockTime == null) {

			throw new IllegalArgumentException("Non-initialised estimatedOffBlockTime.");
		}

		final ObjectSet<IFlightObject> result = m_databaseConnection
				.query(new Predicate<IFlightObject>() {

					/**
					 * Unique serialization id.
					 */
					private static final long serialVersionUID = 2317397361553592829L;

					@Override
					public boolean match(IFlightObject flightObject) {

						final boolean matchArcid 
							= aircraftIdentification
							.equals(flightObject.getFlightIdentification()
									.getAircraftIdentification());
						
						final boolean matchAdep 
							= aerodromeOfDepartureDesignator
							.equals(flightObject.getDepartureInformation()
									.getAerodromeOfDeparture().getIdentifier());

						final boolean matchAdes 
						= aerodromeOfDestinationDesignator
						.equals(flightObject.getArrivalInformation()
								.getAerodromeOfDestination().getIdentifier());

						final boolean matchEobt 
						= estimatedOffBlockTime
						.equals(flightObject.getDepartureInformation()
								.getEstimatedTimeOfDeparture());

						return (matchArcid & matchAdep & matchAdes & matchEobt);
					}
				});
		
		if(result.size() > 0) {
			
			return result.get(0);
		}
		
		return null;
	}



	@Override
	public Set<IFlightObject> 
	retrieveFlightObject(final String aircraftIdentification)
			throws FlightObjectRepositoryException {

		if (aircraftIdentification == null) {

			throw new IllegalArgumentException("Non-initialised aircraftIdentification.");
		}

		final ObjectSet<IFlightObject> result = m_databaseConnection
				.query(new Predicate<IFlightObject>() {


					/**
					 * Unique serialization id.
					 */
					private static final long serialVersionUID = 3026616859348113313L;

					@Override
					public boolean match(IFlightObject flightObject) {

						return aircraftIdentification
							.equals(flightObject.getFlightIdentification()
									.getAircraftIdentification());
					}
				});
		
		return new HashSet<IFlightObject>(result);
	}

	@Override
	public Set<IFlightObject> retrieveFlightObject(
			final String aircraftIdentification,
			final String aerodromeOfDepartureDesignator,
			final String aerodromeOfDestinationDesignator)
			throws FlightObjectRepositoryException {
		
		if (aircraftIdentification == null) {

			throw new IllegalArgumentException("Non-initialised aircraftIdentification.");
		}

		if (aerodromeOfDepartureDesignator == null) {

			throw new IllegalArgumentException("Non-initialised aerodromeOfDepartureDesignator.");
		}

		if (aerodromeOfDestinationDesignator == null) {

			throw new IllegalArgumentException("Non-initialised aerodromeOfDestinationDesignator.");
		}

		final ObjectSet<IFlightObject> result = m_databaseConnection
				.query(new Predicate<IFlightObject>() {


					/**
					 * Unique serialization id.
					 */
					private static final long serialVersionUID = 8676736589174187708L;

					@Override
					public boolean match(IFlightObject flightObject) {

						final boolean matchArcid 
							= aircraftIdentification
							.equals(flightObject.getFlightIdentification()
									.getAircraftIdentification());
						
						final boolean matchAdep 
							= aerodromeOfDepartureDesignator
							.equals(flightObject.getDepartureInformation()
									.getAerodromeOfDeparture().getIdentifier());

						final boolean matchAdes 
						= aerodromeOfDestinationDesignator
						.equals(flightObject.getArrivalInformation()
								.getAerodromeOfDestination().getIdentifier());

						return (matchArcid & matchAdep & matchAdes);
					}
				});
		
		return new HashSet<IFlightObject>(result);
	}

	@Override
	public void updateFlightObject(IFlightObject flightObject)
			throws FlightObjectRepositoryException {

		m_databaseConnection.store(flightObject);
	}

	@Override
	public void deleteFlightObject(IFlightObject flightObject)
			throws FlightObjectRepositoryException {

		if (flightObject == null) {
			
			throw new IllegalArgumentException("Non-initialised Flight Object");
		}
		
		m_databaseConnection.delete(flightObject);
	}



	@Override
	public void deleteFlightObject(IGloballyUniqueFlightIdentifier gufi)
			throws FlightObjectRepositoryException {

		if (gufi == null) {
			
			throw new IllegalArgumentException("Non-initialised gufi");
		}
		
		final IFlightObject flightObject = retrieveFlightObject(gufi);
		
		m_databaseConnection.delete(flightObject);
	}
	
	public final void shutdown() {

		if (m_databaseConnection != null) {
		
			m_databaseConnection.close();
		}
	}
	
	@Override
	protected final void finalize() {
		
		shutdown();
	}

	private final ObjectContainer initialiseDatabase(
			final String databasePath) {

		assert databasePath != null;

		final EmbeddedConfiguration configuration = determineConfiguration();

		final ObjectContainer databaseConnection = Db4oEmbedded.openFile(
				configuration, databasePath);

		return databaseConnection;
	}

private final EmbeddedConfiguration determineConfiguration() {
		
		final EmbeddedConfiguration configuration = Db4oEmbedded
				.newConfiguration();

		configuration.common().objectClass(FlightObject.class).cascadeOnUpdate(true);
		configuration.common().objectClass(FlightObject.class).cascadeOnActivate(true);
		configuration.common().objectClass(FlightObject.class).objectField("m_globallyUniqueFlightIdentier").indexed(true);
		configuration.common().objectClass(FlightObject.class).objectField("m_flightIdentification").indexed(true);

		return configuration;
	}

	
	final private ObjectContainer m_databaseConnection;

}
