package org.isatools.isatab_v1.mapping;

import org.isatools.tablib.mapping.SectionTabMapper;
import org.isatools.tablib.schema.SectionInstance;
import org.isatools.tablib.utils.BIIObjectStore;

public class NMaterialTabMapping extends SectionTabMapper {
	
    public NMaterialTabMapping(BIIObjectStore store, SectionInstance sectionInstance) {
        super(store,sectionInstance);
    }
    
	@Override
	public boolean validateHeaders() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BIIObjectStore map() {
		// TODO Auto-generated method stub
		return null;
	}

}
