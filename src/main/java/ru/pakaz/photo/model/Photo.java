package ru.pakaz.photo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ru.pakaz.common.model.User;

@Entity
@Table(name = "Photos")
public class Photo {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int photoId;
    
    @OneToOne
    private User user;
    
    @OneToOne
    private Album album;
    
    @Column(updatable=true)
    private String title;
    
    @Column
    private Date addDate = new Date();
    
    @Column
    private String fileName;
    
    @Column(updatable=true)
    private String description;
    
    @Column
    private boolean deleted = false;
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="parentPhoto")
    private List<PhotoFile> files = new ArrayList<PhotoFile>(); 

    @Transient
    private boolean isFilesSorted = false;
    
    public int getPhotoId() {
        return this.photoId;
    }
    public void setPhotoId( int photoId ) {
        this.photoId = photoId;
    }

    public User getUser() {
        return this.user;
    }
    public void setUser( User user ) {
        this.user = user;
    }

    public Album getAlbum() {
        return this.album;
    }
    public void setAlbum( Album album ) {
        this.album = album;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle( String title ) {
        this.title = title;
    }

    public Date getAddDate() {
        return this.addDate;
    }
    public void setAddDate( Date addDate ) {
        this.addDate = addDate;
    }

    public String getFileName() {
        return this.fileName;
    }
    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription( String description ) {
        this.description = description;
    }
    
    public void addPhotoFile( PhotoFile file ) {
        this.files.add( file );
    }
    public List<PhotoFile> getPhotoFilesList() {
    	if( !this.isFilesSorted ) {
    		Collections.sort(this.files);
    		
    		this.isFilesSorted = true;
    	}

        return this.files;
    }
    
    public void setDeleted( boolean isDeleted ) {
        this.deleted = isDeleted;
    }
}
