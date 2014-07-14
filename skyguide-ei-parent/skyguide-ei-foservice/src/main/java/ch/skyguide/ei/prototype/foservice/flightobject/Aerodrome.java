package ch.skyguide.ei.prototype.foservice.flightobject;

public class Aerodrome implements IAerodrome {
	
	public Aerodrome() {
		
		m_aerodromeDesignator = null;
	}

	@Override
	public String getIcaoDesignator() {

		return m_aerodromeDesignator;
	}
	
	@Override
	public void setIcaoDesignator(String icaoDesignator) {
		
		m_aerodromeDesignator = icaoDesignator;
	}
	
	private String m_aerodromeDesignator;

}
