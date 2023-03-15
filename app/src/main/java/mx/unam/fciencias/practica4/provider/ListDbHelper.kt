package mx.unam.fciencias.practica4.provider

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID


class ListDbHelper( context: Context?, ) : SQLiteOpenHelper(context, "PDM_Practica_4.db", null, 1) {

    val DATABASE_VERSION: Byte = 1

    val DATABASE_NAME = "PDM Practica 4"

    val LIST_SIZE_LOADER_ID: Byte = 3

    override fun onCreate(db: SQLiteDatabase?) {
        var createQuery = java.lang.StringBuilder()
        createQuery.append("CREATE TABLE IF NOT EXISTS ")
            .append(InfiniteListDBContract.TABLE_NAME)
            .append(" (").append(InfiniteListDBContract.COLUMNS[0]) //equiv a ._ID
            .append(" INTEGER PRIMARY KEY DEFAULT 1, ")
            .append(InfiniteListDBContract.COLUMN_LIST_SIZE)
            .append(" INTEGER CHECK(")
            .append(InfiniteListDBContract.COLUMN_LIST_SIZE)
            .append(" > 0));");
        db?.execSQL(createQuery.toString())
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}