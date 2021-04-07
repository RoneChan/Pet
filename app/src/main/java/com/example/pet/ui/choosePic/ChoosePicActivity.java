package com.example.pet.ui.choosePic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pet.R;
import com.example.pet.control.ImageAdapter;

import java.util.ArrayList;

import static com.example.pet.ui.message.AddFragment.GET_PICTURE;

//https://blog.csdn.net/danpincheng0204/article/details/106778988?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-6.control&dist_request_id=1329188.405.16177937906200843&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-6.control
public class ChoosePicActivity extends AppCompatActivity {
    ArrayList<String> imageUrls;
    private ImageAdapter imageAdapter;
    ArrayList<String> chooseUris;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
        }

        imageAdapter=new ImageAdapter(this,imageUrls);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);

        Button button=findViewById(R.id.btn_choose_pic);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseUris= imageAdapter.getChooseUri();
                Intent intent = new Intent();
                intent.putExtra("number",chooseUris.size());
                for(int i=1;i<=chooseUris.size();i++){
                    intent.putExtra("url"+i,chooseUris.get(i-1));
                }
                ChoosePicActivity.this.setResult(GET_PICTURE,intent);
                finish();

            }
        });
    }

    public void btnChoosePhotosClick(View v){

        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
        Toast.makeText(this, "Total photos selected: "+selectedItems.size(), Toast.LENGTH_SHORT).show();
    }
}