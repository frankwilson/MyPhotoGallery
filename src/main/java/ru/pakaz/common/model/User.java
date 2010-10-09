package ru.pakaz.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import ru.pakaz.photo.model.Album;

@Entity
@Table(name = "Users")
public class User implements UserDetails {
	static private Logger logger = Logger.getLogger( User.class );

    private static final long serialVersionUID = -3726553504772202870L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int userId;
    
    @Column(nullable=false, length=40, unique=true)
    private String login;
    
    @Column(nullable=false, length=32, unique=false)
    private String password;
    
    @Transient
    private String plainPassword;

    @Column(nullable=false, length=50, unique=true)
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
    
    @Column(name="created")
    @Temporal(value=TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name="updated")
    @Temporal(value=TemporalType.TIMESTAMP)
    private Date updated;
    
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
        name="UserRoles",
        joinColumns=@JoinColumn(name="userId"),
        inverseJoinColumns=@JoinColumn(name="roleId")
    )
    private Set<Role> roles;
    
    @Column
    private String activationCode;
    
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "deleted=0")
    private List<Album> albums = new ArrayList<Album>();
    
    @Transient
    private int unallocatedPhotosCount = -1;

    public String getLogin() {
        return this.login;
    }
    public void setLogin( String login ) {
        this.login = login;
    }

    public String getPlainPassword() {
    	return this.plainPassword;
    }
    public String getPassword() {
    	if( this.plainPassword != null )
    		return this.plainPassword;
    	else
    		return this.password;
    }
    public void setPassword( String password ) {
    	logger.debug( "Password: "+ password );
    	this.plainPassword = password;
    	
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        password = encoder.encodePassword( this.getPassword(), this.getLogin() );

        logger.debug( "Password hash: "+ password );
        
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
    
    public Date getCreated() {
        return created;
    }
    
    public void setCreated( Date created ) {
        this.created = created;
    }
    
    public Date getUpdated() {
        return updated;
    }
    
    public void setUpdated( Date updated ) {
        this.updated = updated;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles( Set<Role> roles ) {
        this.roles = roles;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grants = new ArrayList<GrantedAuthority>();

        logger.debug( "Somebody asking for authorities, we have "+ this.roles.size() +" roles" );
        
        for (Role role : this.roles) {
        	logger.debug( "Role: "+ role.getName() );
            grants.add( new GrantedAuthorityImpl( role.getName() ) );
        }

        return grants;
    }
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return getLogin();
    }
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return !getBlocked();
    }
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return !getTemporary();
    }
    public void setUnallocatedPhotosCount( int count ) {
        this.unallocatedPhotosCount = count;
    }
    public int getUnallocatedPhotosCount() {
        return unallocatedPhotosCount;
    }
}
