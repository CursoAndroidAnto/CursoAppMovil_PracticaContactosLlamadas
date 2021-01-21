package com.example.practicacontactosllamadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class contactDataValues {
    private String name;
    private int numberOfCalls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    public void setNumberOfCalls(int numberOfCalls) {
        this.numberOfCalls = numberOfCalls;
    }

    public contactDataValues(String name, int numberOfCalls) {
        this.name = name;
        this.numberOfCalls = numberOfCalls;
    }

    @Override
    public String toString() {
        return name;
    }
}


public class MainActivity extends AppCompatActivity {
    ListView lvContacts;
    TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionContacts();
        lvContacts = findViewById(R.id.lvContacts);
        txtMessage = findViewById(R.id.txtMessage);
    }

    public void readContacts(View view) {
        //Obtenemos los datos de los contactos: Nombre y veces contactado.
        String sCol[] = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.TIMES_CONTACTED};
        //Para recorrer el array de contactos, creamos un cursor
        Cursor cursorContacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, sCol, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        ArrayList<contactDataValues> contactArr = new ArrayList();

        //recorremos el array obtenido y guardamos los nombres en una lista
        while (cursorContacts.moveToNext()) {
            String contactName = cursorContacts.getString(cursorContacts.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            int numberCalls = cursorContacts.getInt(cursorContacts.getColumnIndexOrThrow(ContactsContract.Contacts.TIMES_CONTACTED));
            contactDataValues contactValues = new contactDataValues(contactName, numberCalls);
            contactArr.add(contactValues);
        }
        cursorContacts.close();

        //Mostrarmos en array de datos en el listView
        ArrayAdapter<contactDataValues> adapter = new ArrayAdapter<contactDataValues>(this, android.R.layout.simple_list_item_1, contactArr);
        this.lvContacts.setAdapter(adapter);

        this.lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                txtMessage.setText(contactArr.get(position).getName() + " te ha llamado " + contactArr.get(position).getNumberOfCalls() + " veces");
            }
        });
    }

    public void permissionContacts() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        }
    }
}