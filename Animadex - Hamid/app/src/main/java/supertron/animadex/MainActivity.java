package supertron.animadex;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.utils.ObjectUtils;
import com.example.camera.R;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.net.*;
import java.io.*;

import com.cloudinary.*;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

public class MainActivity extends ActionBarActivity {
    Button b1,b2;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.button);
        iv=(ImageView)findViewById(R.id.imageView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(bp);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        InputStream is = new ByteArrayInputStream(stream.toByteArray());

        Map config = new HashMap();
        config.put("cloud_name", "dvnq7uunf");
        config.put("api_key", "421726614586681");
        config.put("api_secret", "Pi6prfu2uXGfrsokGAyfutLdw0A");
        Cloudinary cloudinary = new Cloudinary(config);
        System.out.println("Connecting to cloudinary");

        try {
            Map upload = cloudinary.uploader().upload(is, ObjectUtils.emptyMap());
            String publicID = (String) upload.get("public_id");
            System.out.println(publicID);

            String base_url = "http://40.76.35.232/find/";

            String image_url = publicID;

            URL url = new URL(base_url + image_url);
            Scanner s = new Scanner(url.openStream());
            String json = s.nextLine();
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}