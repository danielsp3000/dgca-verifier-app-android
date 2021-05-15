/*
 *  ---license-start
 *  eu-digital-green-certificates / dgca-verifier-app-android
 *  ---
 *  Copyright (C) 2021 T-Systems International GmbH and all other contributors
 *  ---
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  ---license-end
 *
 *  Created by climent on 5/7/21 4:14 PM
 */

package it.ministerodellasalute.verificaC19.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.ministerodellasalute.verificaC19.data.VerifierRepository
import it.ministerodellasalute.verificaC19.data.local.Preferences
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    verifierRepository: VerifierRepository,
    private val preferances: Preferences
) : ViewModel(){

    val fetchStatus: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        fetchStatus.addSource(verifierRepository.getCertificateFetchStatus()) {
            fetchStatus.value = it
        }
    }

    fun getDateLastSync() = preferances.dateLastFetch



}