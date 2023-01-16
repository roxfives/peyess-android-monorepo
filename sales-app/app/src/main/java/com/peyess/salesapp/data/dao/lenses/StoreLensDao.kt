package com.peyess.salesapp.data.dao.lenses

import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.dao.internal.firestore.ReadOnlyFirestoreDao

interface StoreLensesDao: ReadOnlyFirestoreDao<FSStoreLocalLens>
