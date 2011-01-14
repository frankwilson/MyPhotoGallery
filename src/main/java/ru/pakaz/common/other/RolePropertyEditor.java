package ru.pakaz.common.other;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import ru.pakaz.common.dao.RoleDao;
import ru.pakaz.common.model.Role;

public class RolePropertyEditor extends PropertyEditorSupport {
    static private Logger logger = Logger.getLogger( RolePropertyEditor.class );

    private RoleDao rolesManager;
    
    public RolePropertyEditor(RoleDao manager) {
        if(manager != null)
            this.rolesManager = manager;
    }

    @Override 
    public void setAsText(String text) {
        Role role = null;
        
        logger.debug( "We got text: "+ text );
        if( this.rolesManager == null )
            logger.debug("WTF? rolesManager is null!");
        else if(text != null) {
            try {
                role = this.rolesManager.getRoleById( Integer.parseInt(text));
            }
            catch( NumberFormatException nfe) {
                logger.error( nfe.getMessage() );
            }
        }

        if( role != null )
            logger.debug( "We got role with ID: "+ role.getRoleId() +" and name: "+ role.getName() );

        setValue(role);
    }
}
