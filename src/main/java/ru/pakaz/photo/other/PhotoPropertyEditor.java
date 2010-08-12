package ru.pakaz.photo.other;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Photo;

public class PhotoPropertyEditor extends PropertyEditorSupport {
    static private Logger logger = Logger.getLogger( PhotoPropertyEditor.class );

    private PhotoDao photoManager;
    
    public PhotoPropertyEditor(PhotoDao manager) {
        if(manager != null)
            this.photoManager = manager;
    }

    @Override 
    public void setAsText(String text) {
        Photo photo = null;
        
        logger.debug( "We got text: "+ text );
        if( this.photoManager == null )
            logger.debug("WTF? photoManager is null!");
        else if(text != null && Integer.parseInt(text) != 0)
            photo = this.photoManager.getPhotoById( Integer.parseInt(text));

        if( photo != null )
            logger.debug( "We got photo with ID: "+ photo.getPhotoId() +" and title: "+ photo.getTitle() );
        
        setValue(photo);
    }
}
