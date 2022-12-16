package com.peyess.salesapp.data.dao.lenses

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.dao.internal.firestore.ReadOnlyFirestoreDao
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity

interface StoreLensesDao: ReadOnlyFirestoreDao<FSStoreLocalLens>
