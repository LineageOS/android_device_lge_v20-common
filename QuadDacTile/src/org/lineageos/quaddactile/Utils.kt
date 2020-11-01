/*
 * Copyright (C) 2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.lineageos.quaddactile

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcel
import android.os.ServiceManager

import org.lineageos.quaddac.IQuadDacService

object Utils {
    private val mQuadDacService: IQuadDacService by lazy {
        IQuadDacService.Stub.asInterface(
            ServiceManager.getService(Constants.QUAD_DAC_SERVICE))
    }

    fun getQuadDacEnabled(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(Constants.QUAD_DAC_ENABLED, false)
    }

    fun setQuadDacEnabled(sharedPreferences: SharedPreferences, enabled: Boolean) {
        mQuadDacService.setEnabled(enabled)
        sharedPreferences.edit().putBoolean(Constants.QUAD_DAC_ENABLED, enabled).apply()
    }
}
