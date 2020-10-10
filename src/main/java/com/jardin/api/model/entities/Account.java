package com.jardin.api.model.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "user_account", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_user_role")
    private UserRole role;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    public Account() {
    }

    public Account(Long id, UserRole role, String name, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
