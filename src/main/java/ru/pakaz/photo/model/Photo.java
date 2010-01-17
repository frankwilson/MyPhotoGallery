package ru.pakaz.photo.model;

import java.util.Date;
import javax.persistence.CascadeType;
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
    
    @Column
    private int originalHeight;
    
    @Column
    private int originalWidth;

    @Column
    private int originalFileSize;

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

    public int getOriginalHeight() {
        return this.originalHeight;
    }

    public void setOriginalHeight( int originalHeight ) {
        this.originalHeight = originalHeight;
    }

    public int getOriginalWidth() {
        return this.originalWidth;
    }

    public void setOriginalWidth( int originalWidth ) {
        this.originalWidth = originalWidth;
    }

    public int getOriginalFileSize() {
        return this.originalFileSize;
    }

    public void setOriginalFileSize( int originalFileSize ) {
        this.originalFileSize = originalFileSize;
    }
    
    public void fillInfoFromFile() {
        
    }
}
