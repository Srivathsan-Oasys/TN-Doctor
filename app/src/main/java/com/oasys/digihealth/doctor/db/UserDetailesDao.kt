package com.oasys.digihealth.doctor.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oasys.digihealth.doctor.ui.login.model.login_response_model.UserDetails


@Dao
abstract class UserDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUserDetails(userDetailsResponseContent: UserDetails?)

    @get:Query("SELECT * FROM UserDetails")
    abstract val userDetails: UserDetails?

    @Query("DELETE FROM UserDetails")
    abstract fun deleteUserDetails()


    fun deleteAndInsert(userDetails: UserDetails?) {
        deleteUserDetails()
        insertUserDetails(userDetails)
    }
}