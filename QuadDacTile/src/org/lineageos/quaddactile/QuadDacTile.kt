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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.ServiceManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QuadDacTile : TileService() {

    private val mSharedPreferences: SharedPreferences by lazy {
        createDeviceProtectedStorageContext().
                getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
    }
    private val headsetPluggedReceiver: BroadcastReceiver = HeadsetPluggedReceiver()

    override fun onStartListening() {
        super.onStartListening()

        registerReceiver(headsetPluggedReceiver, IntentFilter(Intent.ACTION_HEADSET_PLUG))

        val available = getSystemService(AudioManager::class.java).isWiredHeadsetOn()
        val enabled = Utils.getQuadDacEnabled(mSharedPreferences)
        updateTile(available, enabled)
    }

    override fun onStopListening() {
        super.onStopListening()

        unregisterReceiver(headsetPluggedReceiver)
    }

    override fun onClick() {
        super.onClick()

        val enabled = !Utils.getQuadDacEnabled(mSharedPreferences)
        Utils.setQuadDacEnabled(mSharedPreferences, enabled)
        updateTile(true, enabled)
    }

    fun updateTile(available: Boolean, enabled: Boolean) {
        if (available) {
            qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            qsTile.label = getString(R.string.tile_label)
        } else {
            qsTile.state = Tile.STATE_UNAVAILABLE
            qsTile.label = getString(R.string.tile_label_unavailable)
        }
        qsTile.updateTile()
    }

    inner class HeadsetPluggedReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                when (intent.getIntExtra("state", 0)) {
                    0 -> updateTile(false, false)
                    1 -> updateTile(true, Utils.getQuadDacEnabled(mSharedPreferences))
                }
            }
        }
    }
}
