package com.nazar_protasov.swipehome.data.local.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.nazar_protasov.swipehome.data.local.appContext
import okio.Path.Companion.toPath
import java.io.File

actual fun createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            //
            val file = File(appContext.filesDir, "user_preferences.preferences_pb")
            file.absolutePath.toPath()
        }
    )
}