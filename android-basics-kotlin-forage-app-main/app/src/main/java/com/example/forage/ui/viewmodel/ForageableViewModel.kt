/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forage.ui.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.example.forage.data.ForageableDao
import com.example.forage.model.Forageable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/**
 * Shared [ViewModel] to provide data to the [ForageableListFragment], [ForageableDetailFragment],
 * and [AddForageableFragment] and allow for interaction the the [ForageableDao]
 */

// : pass a ForageableDao value as a parameter to the view model constructor
class ForageableViewModel(
    // Pass dao here
    private val forageableDao: ForageableDao
): ViewModel() {

    // : create a property to set to a list of all forageables from the DAO
     val forageableList : LiveData<List<Forageable>> = forageableDao.getForageables().asLiveData()

    //  : create method that takes id: Long as a parameter and retrieve a Forageable from the
    //  database by id via the DAO.
    fun getforageable( id: Long): LiveData<Forageable> =
        forageableDao.getForageable(id).asLiveData()

    fun addForageable(
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = Forageable(
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )

    // \: launch a coroutine and call the DAO method to add a Forageable to the database within it
        viewModelScope.launch {
            forageableDao.insert(forageable)
        }


    }

    fun updateForageable(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = Forageable(
            id = id,
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )
        viewModelScope.launch(Dispatchers.IO) {
            // : call the DAO method to update a forageable to the database here
            forageableDao.update(forageable)
        }
    }

    fun deleteForageable(forageable: Forageable) {
        viewModelScope.launch(Dispatchers.IO) {
            // : call the DAO method to delete a forageable to the database here
            forageableDao.delete(forageable)
        }
    }

    fun isValidEntry(name: String, address: String): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }
}

// : create a view model factory that takes a ForageableDao as a property and
//  creates a
class ForageableViewModelFactory(private val forageableDao: ForageableDao)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ForageableViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ForageableViewModel(forageableDao) as T
        }
        else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}


