package com.dominicheal.model

import java.io.Serializable
import javax.persistence.*

/**
 * @author Dominic
 * @since  09-Jul-17
 * Website: www.dominicheal.com
 * Github: www.github.com/DomHeal
 */
@Entity
@Table(name="USER")
class User : Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id : Long = 0;
//
//    @NotEmpty
//    @Column(name="USERNAME", nullable=false)
//    private String name;

}