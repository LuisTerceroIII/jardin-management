package com.jardin.api.model.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


//@Entity
//@Table(name = "user_role", schema = "public")
//@EntityListeners(AuditingEntityListener.class)
//public class UserRole {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(columnDefinition = "serial")
//    Long id;
//
//    @Column(name = "role",nullable = false)
//    String role;
//
//    public UserRole() {}
//
//    public UserRole(String role) {
//        this.role = role;
//    }
//
//    @Override
//    public String toString() {
//        return "UserRole{" +
//                "id=" + id +
//                ", role='" + role + '\'' +
//                '}';
//    }
//}
