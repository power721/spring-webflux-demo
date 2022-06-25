package cn.har01d.demo.springwebfluxdemo.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String email;
}
