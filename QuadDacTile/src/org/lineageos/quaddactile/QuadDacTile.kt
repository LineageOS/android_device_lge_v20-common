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
 * limitations under the License.
 */
package org.lineageos.quaddactile;

import android.content.Context
import android.content.SharedPreferences
import android.os.ServiceManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QuadDacTile : TileService() {

    val mSharedPreferences: SharedPreferences by lazy {
        createDeviceProtectedStorageContext().
                getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onStartListening() {
        super.onStartListening()

        updateTile(Utils.getQuadDacEnabled(mSharedPreferences))
    }

    override fun onClick() {
        super.onClick()

        val sharedPreferences = createDeviceProtectedStorageContext().
                getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        val enabled = !Utils.getQuadDacEnabled(mSharedPreferences)

        Utils.setQuadDacEnabled(mSharedPreferences, enabled)
        updateTile(enabled)
    }

    fun updateTile(enabled: Boolean) {
        qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}
