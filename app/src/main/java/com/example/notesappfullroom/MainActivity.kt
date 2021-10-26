package com.example.notesappfullroom
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesappfullroom.DBRoom.Note
import com.example.notesappfullroom.DBRoom.NoteDatabase
import com.example.notesappfullroom.databinding.MainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainBinding
    lateinit var contect:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        contect = applicationContext
        var notes=db(contect).NoteDoa().getAllUserInfo()
        binding.recyclerView.adapter=RVAdapter(notes,this)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.btnAddNote.setOnClickListener {
            val note = binding.edNote.text.toString()
            val n = Note(0, note)
            if (note.isNotEmpty())
            {
                NoteDatabase.getInstance(applicationContext).NoteDoa().insertNote(n)
                binding.edNote.text.clear()
            }
            else
            {
                Toast.makeText(applicationContext, "please enter text", Toast.LENGTH_SHORT).show()
            }

        }
        }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           updateRecycler()
        }
    }

    fun updateRecycler(){
        var notes=db(contect).NoteDoa().getAllUserInfo()
        binding.recyclerView.adapter=RVAdapter(notes,this)
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
    }
    fun alertDialog(id:Int,note:String){

        val dialogBuild= AlertDialog.Builder(this)
        val lLayout= LinearLayout(this)
        val tvAlert= TextView(this)
        val edAlert= EditText(this)
        tvAlert.text=" Update Note  "
        edAlert.setSingleLine()
        edAlert.setText(note)
        lLayout.addView(tvAlert)
        lLayout.addView(edAlert)
        lLayout.setPadding(50,40,50,10)

        dialogBuild.setNegativeButton("cancel", DialogInterface.OnClickListener { _, _ ->

        })
        dialogBuild.setPositiveButton("save",DialogInterface.OnClickListener { _, _ ->
            db(contect).NoteDoa().updateNote(Note(id,edAlert.text.toString()))
            updateRecycler()
        })
        val aler=dialogBuild.create()
        aler.setView(lLayout)
        aler.show()
    }
    fun db(context: Context): NoteDatabase {
        return NoteDatabase.getInstance(context)
    }
}