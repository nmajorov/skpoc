package ch.skyguide.ei.prototype.foservice.flightobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.skyguide.flightplan.structure.equipment.NavigationCommunicationEquipmentType;

public class NavigationCapabilities 
implements INavigationCapabilities {

	public NavigationCapabilities() {

	}
	
	@Override
	public final List<NavigationCommunicationEquipmentType> 
		getNavigationCommunicationEquipment() {

		return Collections
				.<NavigationCommunicationEquipmentType>unmodifiableList(m_navigationCommunicationEquipment);
	}

	@Override
	public final void setNavigationCommunicationEquipment(
			final List<NavigationCommunicationEquipmentType> navcomEquipment) 
	throws IllegalArgumentException {
		
		if(navcomEquipment == null) {
			
			throw new IllegalArgumentException("Non-initialized "
					+ "navigation/communication equipment");
		}

	}
	
	private final List<NavigationCommunicationEquipmentType> m_navigationCommunicationEquipment 
		= new ArrayList<NavigationCommunicationEquipmentType>();
}
