package ru.pakaz.photo.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class of photo file
 * 
 * @author wilson
 *
 */

@Entity
@Table(name = "PhotoFiles")
public class PhotoFile implements Comparable<PhotoFile> {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int fileId;
    
    @Column
    private Date fileAddDate = new Date();

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Photo parentPhoto;
    
    @Column
    private int photoHeight;
    
    @Column
    private int photoWidth;
    
    @Column
    private boolean deleted = false;
    
    @Column
    private String filename = "";
    
    @Column
    private int filesize = 0;
/*
    private static String rootPhotoCatalog;

    public static String getRootPhotoCatalog() {
        return rootPhotoCatalog;
    }
    public static void setRootPhotoCatalog( String rootPhotoCatalog ) {
        PhotoFile.rootPhotoCatalog = rootPhotoCatalog;
    }
*/
    public int getFileId() {
        return this.fileId;
    }
    public void setFileId( int fileId ) {
        this.fileId = fileId;
    }
    public Date getFileAddDate() {
        return this.fileAddDate;
    }
    public void setFileAddDate( Date fileAddDate ) {
        this.fileAddDate = fileAddDate;
    }
    public Photo getParentPhoto() {
        return this.parentPhoto;
    }
    public void setParentPhoto( Photo parentPhoto ) {
        this.parentPhoto = parentPhoto;
    }
    public int getPhotoHeight() {
        return this.photoHeight;
    }
    public void setPhotoHeight( int photoHeight ) {
        this.photoHeight = photoHeight;
    }
    public int getPhotoWidth() {
        return this.photoWidth;
    }
    public void setPhotoWidth( int photoWidth ) {
        this.photoWidth = photoWidth;
    }
    
    public void setDeleted( boolean isDeleted ) {
        this.deleted = isDeleted;
    }
    public boolean getDeleted() {
        return this.deleted;
    }
    
    public void setFilename( String filename ) {
        this.filename = filename;
    }
    public String getFilename() {
        return this.filename;
    }

    public void setFilesize( int filesize ) {
        this.filesize = filesize;
    }
    public int getFilesize() {
        return filesize;
    }

    @Override
    public int compareTo(PhotoFile o) {
        if( this.photoWidth > o.getPhotoWidth() && this.photoHeight > o.getPhotoHeight() )
            return -1;
        else if( this.photoWidth < o.getPhotoWidth() && this.photoHeight < o.getPhotoHeight() )
            return 1;
        else
            return 0;
    }
}
