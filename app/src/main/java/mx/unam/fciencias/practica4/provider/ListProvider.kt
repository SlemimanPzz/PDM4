package mx.unam.fciencias.practica4.provider

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import org.jetbrains.annotations.NotNull


class ListProvider : ContentProvider() {

    private var dbHelper: ListDbHelper? = null

    private val LIST_SIZE = 100

    private val LIST_SIZE_WITH_ID  = 101

    private var CONTENT_MATCHER: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        CONTENT_MATCHER.addURI(InfiniteListDBContract.AUTHORITY,
            InfiniteListDBContract.PATH_INFINITE_LIST,
            LIST_SIZE.toInt())

        CONTENT_MATCHER.addURI(InfiniteListDBContract.AUTHORITY,
            InfiniteListDBContract.PATH_INFINITE_LIST +"/#",
            LIST_SIZE_WITH_ID.toInt())

    }

    override fun onCreate(): Boolean {
        dbHelper = ListDbHelper(context)
        return true
    }

    override fun query(@NotNull uri : Uri, projection: Array<out String>?, selection: String?, selectionArgs : Array<out String>?, sortOrder: String?): Cursor? {
        val db = dbHelper?.readableDatabase
        var result : Cursor? = null
        when (CONTENT_MATCHER?.match(uri)) {
            LIST_SIZE ->
                result = db?.query(InfiniteListDBContract.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder)

            LIST_SIZE_WITH_ID ->
                result = db?.query(InfiniteListDBContract.TABLE_NAME,
                    projection,
                    InfiniteListDBContract.COLUMNS[0] + " = ?", //equiv ._ID
                    arrayOf(uri.pathSegments[1]),
                    null,
                    null,
                    null
                )
            else -> throw UnsupportedOperationException("Invalid URI: $uri")
        }
        val context: Context? = context
        if(context != null)
            result?.setNotificationUri(context.contentResolver, uri)
        return result;


    }

    override fun getType(@NotNull uri : Uri): String? {
        return when (CONTENT_MATCHER.match(uri)) {
            LIST_SIZE -> {
                null
            }
            LIST_SIZE_WITH_ID -> InfiniteListDBContract.CONTENT_TYPE
            else -> throw UnsupportedOperationException("Unknown type for $uri");
        }
    }

    override fun insert(@NotNull  uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper!!.writableDatabase
        var newId : Long

        when (CONTENT_MATCHER.match(uri)) {
            LIST_SIZE -> newId = db.insert(InfiniteListDBContract.TABLE_NAME, null, values)
            LIST_SIZE_WITH_ID -> {
                var values = values
                if (values == null) values = ContentValues()
                values.put(InfiniteListDBContract.COLUMNS[0], uri.pathSegments[1])
                newId = db.insert(InfiniteListDBContract.TABLE_NAME, null, values)
            }
            else -> throw UnsupportedOperationException("Can't insert on $uri")
        }

        if (newId <= 0) throw SQLException("Couldn't insert")
        return ContentUris.withAppendedId(InfiniteListDBContract.CONTENT_URI, newId)
    }

    override fun delete(@NotNull uri : Uri, selection : String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper!!.writableDatabase
        var deletedEntries: Int
        when (CONTENT_MATCHER.match(uri)) {
            LIST_SIZE -> deletedEntries = db.delete(
                InfiniteListDBContract.TABLE_NAME,
                selection,
                selectionArgs
            )
            LIST_SIZE_WITH_ID -> deletedEntries = db.delete(
                InfiniteListDBContract.TABLE_NAME,
                InfiniteListDBContract.COLUMNS[0] +" = ?",
                arrayOf(uri.pathSegments[1])
            )
            else -> throw UnsupportedOperationException("Can't delete on $uri")
        }
        var context = context
        if(context != null && deletedEntries > 0)
            context.contentResolver.notifyChange(uri, null);
        return deletedEntries;

    }

    override fun update(@NotNull uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper!!.writableDatabase
        var updatedEntries: Int

        when (CONTENT_MATCHER.match(uri)) {
            LIST_SIZE -> updatedEntries = db.update(
                InfiniteListDBContract.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
            LIST_SIZE_WITH_ID -> updatedEntries = db.update(
                InfiniteListDBContract.TABLE_NAME,
                values,
                InfiniteListDBContract.COLUMNS[0] + " = ?",
                arrayOf(uri.pathSegments[1])
            )
            else -> throw UnsupportedOperationException("Can't update $uri")
        }
        val context = context
        if (context != null && updatedEntries > 0) context.contentResolver.notifyChange(uri, null)
        return updatedEntries

    }


}