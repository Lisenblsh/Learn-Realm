package com.lis.learnrealm.database

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.Flow

class Cat : RealmObject {
    @PrimaryKey
    var id = ""
    var url = ""
    var width = 0
    var height = 0
    var isShowed = false

}

object Database {
    val configuration = RealmConfiguration.create(schema = setOf(Cat::class))

    val realm = Realm.open(configuration)

    fun write(cat: Cat) {
        realm.writeBlocking {
            copyToRealm(cat)
        }
    }

    suspend fun writeAsync(cat: Cat) {
        realm.write {
            copyToRealm(cat)
        }
    }

    fun query(id:String? = null): Cat? {
        return if(id == null ){
            realm.query<Cat>().first().find()
        }else {
            realm.query<Cat>("id == $0", id).first().find()

        }

    }

    suspend fun queryAsync(): Flow<ResultsChange<Cat>> {
        return realm.query<Cat>()
            .asFlow()

    }

    suspend fun update(id: String) {
        realm.query<Cat>("id == $0", id).first().find()
            ?.also {
                realm.write {
                    findLatest(it)?.isShowed = true
                }
            }
    }

    suspend fun delete(id: String) {
        realm.write {
            delete(this.query<Cat>("id == $0", id))
        }
    }

}