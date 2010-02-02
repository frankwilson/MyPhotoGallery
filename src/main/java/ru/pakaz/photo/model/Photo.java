package ru.pakaz.photo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    
    @Column
    private String title;
    
    @Column
    private Date addDate;
    
    @Column
    private String fileName;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="parentPhoto")
    private List<PhotoFile> files = new ArrayList<PhotoFile>(); 

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
    
    public void setPhotoFile( PhotoFile file ) {
        this.files.add( file );
    }
    
    public PhotoFile getPhotoFile( int index ) {
        return this.files.get( index );
    }
}
