package ru.pakaz.photo.model;

import java.util.ArrayList;
import java.util.List;
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

import org.hibernate.annotations.Where;

import ru.pakaz.common.model.User;

@Entity
@Table(name = "Albums")
public class Album {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int albumId;
    
    @OneToOne
    private User user;
    
    @Column(length=64)
    private String title;
    
    @Column
    private String description;
    
    @OneToOne
    private Photo preview;
    
    @Column()
    private Date addDate = new Date();
    
    @Column
    private boolean deleted = false;
    
    @OneToMany(mappedBy="album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "deleted=0")
    private List<Photo> photos = new ArrayList<Photo>();

    public int getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId( int albumId ) {
        this.albumId = albumId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Photo getPreview() {
        return this.preview;
    }

    public void setPreview( Photo preview ) {
        this.preview = preview;
    }

    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate( Date addDate ) {
        this.addDate = addDate;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos( List<Photo> photos ) {
        this.photos = photos;
    }

    public void setDeleted(boolean isDeleted) {
        this.deleted = isDeleted;
        
        if( isDeleted == true ) {
            for( Photo albumPhoto : this.photos ) {
                albumPhoto.setDeleted( true );
            }
        }
    }

    public boolean isDeleted() {
        return deleted;
    }
}
