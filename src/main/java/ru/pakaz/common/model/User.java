package ru.pakaz.common.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;

import ru.pakaz.photo.model.Album;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int userId;
    
    @Column
    private String login;
    
    @Column
    private String password;

    @Column
    @Email
    private String email;
    
    @Column
    private String firstName;
    
    @Column
    private String lastName;
    
    @Column
    private String nickName;

    @Column
    private boolean temporary = false;
    
    @Column
    private boolean blocked = false;
    
    @Column
    private String activationCode;
    
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "deleted=0")
    private List<Album> albums = new ArrayList<Album>();

    public String getLogin() {
        return this.login;
    }
    public void setLogin( String login ) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword( String password ) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail( String email ) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return this.nickName;
    }
    public void setNickName( String nickName ) {
        this.nickName = nickName;
    }

    public int getUserId() {
        return this.userId;
    }

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public boolean getTemporary() {
		return this.temporary;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean getBlocked() {
		return this.blocked;
	}
	
	public List<Album> getAlbums() {
		return this.albums;
	}

	public void setActivationCode(String code) {
		this.activationCode = code;
	}

	public String getActivationCode() {
		return this.activationCode;
	}
}
