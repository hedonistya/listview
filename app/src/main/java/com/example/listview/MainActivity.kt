package com.example.listview

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.listview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private var READ_CONTACTS_GRANTED = false
  private var REQUEST_CODE_READ_CONTACTS = 1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val hasReadContactsPermission =
      ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

    if (hasReadContactsPermission == PackageManager.PERMISSION_GRANTED) {
      READ_CONTACTS_GRANTED = true
    } else {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.READ_CONTACTS),
        REQUEST_CODE_READ_CONTACTS
      )
    }

    if (READ_CONTACTS_GRANTED) {
      loadContacts()
    }

//    val someText = listOf("Moscow", "New-York", "Tokyo", "Hong-Kong", "Berlin")
//
//    binding.lvContent.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, someText)
//
//    binding.lvContent.setOnItemClickListener{ parent, view, position, id ->
//      Toast.makeText(this, someText[position], Toast.LENGTH_LONG).show()
//    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      REQUEST_CODE_READ_CONTACTS -> {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          loadContacts()
        } else {
          Toast.makeText(this, "Give me permission! Bitch", Toast.LENGTH_LONG).show()
        }
        return
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  private fun loadContacts() {
    val cursor: Cursor? = contentResolver.query(
      ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
      null,
      null,
      null,
      null
    )
    startManagingCursor(cursor)

    val from = arrayOf(
      ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
      ContactsContract.CommonDataKinds.Phone.NUMBER,
      ContactsContract.CommonDataKinds.Phone._ID
    )

    val to = intArrayOf(android.R.id.text1, android.R.id.text2)

    val simple = SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to)
    binding.lvContent.adapter = simple
  }

}