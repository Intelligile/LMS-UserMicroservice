package com.example.UserMicroserviceAPI.model;

import jakarta.persistence.*;

@Entity
@Table(name = "LicensorUser_LicensorAuthorities")
public class LicensorUserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long licesnr_user_authorities_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private LicensorUser user;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private LicensorAuthority authority;

    @Version
    private Long version;

    // Default constructor
    public LicensorUserAuthority() {
    }

    // Parameterized constructor
    public LicensorUserAuthority(LicensorUser user, LicensorAuthority authority) {
        this.user = user;
        this.authority = authority;
    }

    // Getters and setters
    public Long getLicesnrUserAuthoritiesId() {
        return licesnr_user_authorities_id;
    }

    public void setLicesnrUserAuthoritiesId(Long id) {
        this.licesnr_user_authorities_id = id;
    }

    public LicensorUser getUser() {
        return user;
    }

    public void setUser(LicensorUser user) {
        this.user = user;
    }

    public LicensorAuthority getAuthority() {
        return authority;
    }

    public void setAuthority(LicensorAuthority authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "LicensorUserAuthority{" +
                "id=" + licesnr_user_authorities_id +
                ", user=" + user +
                ", authority=" + authority +
                '}';
    }
}
