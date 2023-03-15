package mx.unam.fciencias.practica4

import android.os.Bundle
import android.widget.Button
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @Nullable
    private val launchInfiniteListButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}