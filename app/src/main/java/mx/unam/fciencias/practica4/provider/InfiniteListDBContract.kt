package mx.unam.fciencias.practica4.provider

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import android.provider.BaseColumns._ID


class InfiniteListDBContract {

    companion object  InfiniteListEntry :BaseColumns {

        val AUTHORITY = "mx.unam.fciencias.inflist"

        val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

        const val PATH_INFINITE_LIST = "infinite_list"

        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INFINITE_LIST).build()

        const val TABLE_NAME = PATH_INFINITE_LIST

        const val COLUMN_LIST_SIZE = "list_size"

        const val COLUMN_INDEX_ID: Byte = 0

        const val COLUMN_INDEX_LIST_SIZE: Byte = 1

        val COLUMNS = arrayOf(_ID, COLUMN_LIST_SIZE)

        val CONTENT_TYPE = (ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_INFINITE_LIST)

        val CONTENT_ITEM_TYPE = (ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_INFINITE_LIST)

        fun buildListSizeUri(id: Long): Uri? {
            return ContentUris.withAppendedId(CONTENT_URI, id)
        }
    }
}