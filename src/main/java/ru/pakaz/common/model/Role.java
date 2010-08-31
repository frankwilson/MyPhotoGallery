package ru.pakaz.common.model;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

/*
CREATE TABLE `Roles` (
 `id` int(11) NOT NULL,
 `name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
 `description` varchar(100) COLLATE utf8_bin DEFAULT NULL,
 `enabled` tinyint(1) DEFAULT '1',
 `created` datetime DEFAULT NULL,
 `updated` datetime DEFAULT NULL,
 UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
		
CREATE TABLE `UserRoles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
*/

@Entity
@Table(name="Roles")
@org.hibernate.annotations.Proxy(lazy=false)
public class Role implements Serializable {
	
	private static final long serialVersionUID = 65494535748578762L;
	
	@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
	
	@Column(name="name", nullable=false, length=20, unique=false)
    private String name;
	
	@Column(name="description", nullable=true, length=100, unique=false)
    private String description;
    
    @Column(name="enabled")
	private boolean enabled;
	
	@Column(name="created")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="updated")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date updated;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(
		name="UserRoles",
		joinColumns=@JoinColumn(name="roleId"),
		inverseJoinColumns=@JoinColumn(name="userId")
	)
	private Set<User> users;
	
	public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }
	
	public String getName() {
    	return name;
    }
    
    public void setName( String name ) {
    	this.name = name;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription( String description ) {
    	this.description = description;
    }
	
	public boolean getEnabled() {
    	return enabled;
    }
    
    public void setEnabled( boolean enabled ) {
    	this.enabled = enabled;
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
    
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
