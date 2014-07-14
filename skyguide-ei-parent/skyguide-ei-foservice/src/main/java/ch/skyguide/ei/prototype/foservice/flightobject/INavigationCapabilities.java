package ch.skyguide.ei.prototype.foservice.flightobject;

import java.util.List;

import ch.skyguide.flightplan.structure.equipment.NavigationCommunicationEquipmentType;

public interface INavigationCapabilities {
	
	public List<NavigationCommunicationEquipmentType> getNavigationCommunicationEquipment();
	
	public void setNavigationCommunicationEquipment(final List<NavigationCommunicationEquipmentType> navcomEquipment);

}
