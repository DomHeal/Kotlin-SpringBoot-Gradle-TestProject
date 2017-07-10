package com.dominicheal.repositories

import com.dominicheal.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 * @author Dominic
 * @since  09-Jul-17
 * Website: www.dominicheal.com
 * Github: www.github.com/DomHeal
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByName(name: String): User

}