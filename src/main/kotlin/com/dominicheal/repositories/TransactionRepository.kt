package com.dominicheal.repositories

import com.dominicheal.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author Dominic
 * @since  09-Jul-17
 * Website: www.dominicheal.com
 * Github: www.github.com/DomHeal
 */
@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {

}