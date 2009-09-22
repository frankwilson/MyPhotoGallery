package ru.pakaz.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String email;
    
    @Column
    private String firstName;
    
    @Column
    private String lastName;
    
    @Column
    private String nickName;

    /**
     * @return the login
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin( String login ) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword( String password ) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail( String email ) {
        this.email = email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    /**
     * @return the nickName
     */
    public String getNickName() {
        return this.nickName;
    }

    /**
     * @param nickName the nickName to set
     */
    public void setNickName( String nickName ) {
        this.nickName = nickName;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return this.userId;
    }
}
