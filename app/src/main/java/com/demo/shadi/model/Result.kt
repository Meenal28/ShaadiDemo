package com.demo.shadi.model

import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Ignore

enum class STATUS {
    ACCPET,
    DECLINE
}

//@Entity(tableName = "Matches")
data class Result(
    val gender: String? = null,
    val name: Name? = null,
    val location: Location? = null,
    val email: String? = null,
    val dob: Dob = Dob(),
    val picture: Picture? = null,
    @Ignore var isAccepted : Boolean? = null
) {
    override fun equals(result: Any?): Boolean {
        if (this === result) return true
        val other = result as Result

        return gender == other.gender &&
                name == other.name &&
                location == other.location &&
                email == other.email &&
                dob == other.dob &&
                picture == other.picture
    }

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<Result> =
            object : DiffUtil.ItemCallback<Result>() {
                override fun areItemsTheSame(
                    @NonNull oldItem: Result,
                    @NonNull newItem: Result
                ): Boolean {
                    return oldItem.name === newItem.name
                }

                override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

