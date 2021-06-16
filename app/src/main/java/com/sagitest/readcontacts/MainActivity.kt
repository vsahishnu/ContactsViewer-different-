package com.sagitest.readcontacts

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_child.view.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


//Haven't added access to search view
//for Ref: https://youtu.be/T6ve7gUrVfc

class MainActivity : AppCompatActivity(), ContactAdapter.OnItemCLickListener {

    val contactList: MutableList<ContactDTO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { Manifest.permission.READ_CONTACTS },
                111
            )
        }

        contact_list.layoutManager = LinearLayoutManager(this)

        btn_read_contact.setOnClickListener {

            val contacts = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )

            if (contacts != null) {
                while (contacts.moveToNext()) {
                    val name =
                        contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number =
                        contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val obj = ContactDTO()
                    obj.name = name
                    obj.number = number

                    val photo_uri =
                        contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                    if (photo_uri != null) {
                        obj.image =
                            MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photo_uri))
                    }
                    contactList.add(obj)
                }
            }
            contact_list.adapter = ContactAdapter(contactList, this, this)
            contacts?.close()
        }
    }

    override fun onItemClick(position: Int) {
        val contact = contactList.get(position)
        val msg = "Name: ${contact.name} \nPhone No: ${contact.number}"

        val clb: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("EditText", msg)
        clb.setPrimaryClip(clip)

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

/*
searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var sv = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
                    Array(1){"%$p0%"},
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)!!
                ContactAdapter.changeCursor(sv)
                return false
            }
        })
 */

//for ref: https://youtu.be/T6ve7gUrVfc