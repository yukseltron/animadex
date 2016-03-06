/**
 * Created by hyuks_000 on 2016-03-06.
 */
import android.graphics.Bitmap;

import java.io.*;
import java.net.*;
import java.util.*;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;


public class AzureParser {
    public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" +
            "AccountName=animadex1;" +
            "AccountKey=v0JO6gbPAUY0gIKybKfiEQa0eiIuP4HDki72q9BiEapYbb1cCpHH/SKKIcHAbu2n3ehAjNi9Cw2apnVFXFYraQ==";

    public static void upload_file(){
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference("photos");

            String filePath = "C:\\Users\\hyuks_000\\Desktop\\meerkat.jpg";

            // Create or overwrite the "myimage.jpg" blob with contents from a local file.
            CloudBlockBlob blob = container.getBlockBlobReference("ahh.txt");

            for (ListBlobItem blobItem : container.listBlobs()){
                System.out.println(blobItem.getUri());
            }
            //blob.UploadFromFile(filePath);
            System.out.println("trying to upload");
            File source = new File(filePath);
            blob.upload(new FileInputStream(source), source.length());
            System.out.println("uploaded");
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        upload_file();
    }


}
