package ru.pakaz.common.model;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

/*
CREATE TABLE `Roles` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
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
    @GeneratedValue
    @Column(name = "id")
    private int roleId;
	
	@Column(nullable=false, length=20, unique=true)
    private String name;
	
	@Column(nullable=true, length=100, unique=false)
    private String description;
    
    @Column
	private boolean enabled;
	
	@Column
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date created;
	
	@Column
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date updated;
	
	public int getRoleId() {
        return roleId;
    }

    public void setRoleId( int id ) {
        this.roleId = id;
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
}
