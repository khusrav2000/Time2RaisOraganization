package com.example.organization;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.RetUtils;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.Messages;
import com.example.organization.data.model.Photo;
import com.example.organization.data.model.SendInitiatorInformation;
import com.example.organization.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InitiatorProfile extends AppCompatActivity implements View.OnClickListener{

    ImageView backgroundProfileImage;
    ImageView profileImage;
    TextView initiatorName;
    TextView initiatorEmail;
    LinearLayout addPhotoInitiator;
    EditText editInitiatorName;
    EditText editInitiatorEmail;
    EditText editInitiatorPhone;
    EditText editInitiatorAddress;
    EditText editInitiatorZipCode;
    EditText editAboutUs;
    Button buttonSaveInitiator;
    EditText initiatorOldPassword;
    EditText initiatorNewPassword;
    EditText initiatorConfirmPassword;
    Button buttonChangePassword;

    Button addCover;
    FrameLayout addProfileImage;

    LinearLayout layoutImagesGallery;

    Uri add_cover_uri              = null;
    Uri add_icon_uri               = null;
    String imageEncoded;

    Button logOut;

    List<String> imagesEncodedList;
    List<Uri>  galleryUri;

    final int PICK_IMAGE_MULTIPLE  = 1;
    final int PICK_IMAGE_ADD_COVER = 2;
    final int PICK_IMAGE_ADD_ICON  = 3;

    private boolean emailIsExists = false;

    InitiatorInformation initiatorInformation;



    final String STORAGE_URL = "https://drive.google.com/uc?export=download&id=";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiator_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.profile_toolbar);

        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.back_button);

        // Инициализация данных поля профиля организатора.
        backgroundProfileImage          = findViewById(R.id.background_profile_image);
        profileImage                    = findViewById(R.id.profile_image);
        initiatorName                   = findViewById(R.id.initiator_name);
        initiatorEmail                  = findViewById(R.id.initiator_email);
        addPhotoInitiator               = findViewById(R.id.add_photo_initiator);
        editInitiatorName               = findViewById(R.id.edit_initiator_name);
        editInitiatorEmail              = findViewById(R.id.edit_initiator_email);
        editInitiatorPhone              = findViewById(R.id.edit_initiator_phone);
        editInitiatorAddress            = findViewById(R.id.edit_initiator_address);
        editInitiatorZipCode            = findViewById(R.id.edit_initiator_zip_code);
        editAboutUs                     = findViewById(R.id.edit_about_us);
        buttonSaveInitiator             = findViewById(R.id.save_initiator);
        initiatorOldPassword            = findViewById(R.id.initiator_old_password);
        initiatorNewPassword            = findViewById(R.id.initiator_new_password);
        initiatorConfirmPassword        = findViewById(R.id.initiator_confirm_password);
        buttonChangePassword            = findViewById(R.id.button_change_password);

        // Фотографии профиля организатора.
        layoutImagesGallery             = findViewById(R.id.profile_gallery_photos);

        addCover                        = findViewById(R.id.add_cover);
        addProfileImage                 = findViewById(R.id.add_profile_image);

        logOut                          = findViewById(R.id.log_out);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

        addCover.setOnClickListener(this);
        addProfileImage.setOnClickListener(this);
        addPhotoInitiator.setOnClickListener(this);

        // Получения данных о пользователе.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator organization = retrofit.create(Initiator.class);
        Call call = organization.getProfileById(LoginDataSource.getInitiator().getToken());
        System.out.println(" WILL WORK -----------------------------------");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                // TODO: Инициализировать данные о пользователе.
                if (response.isSuccessful()) {
                    System.out.println(" All PROFILE INF DOWNLOAD -------------------");
                    fillInitiatorInformation((InitiatorInformation) response.body());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        buttonSaveInitiator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitiatorInformation();
            }
        });
    }

    private void startLogin() {
        LoginDataSource.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.add_cover: // Изменения задного фона профиля.
                openGallery(PICK_IMAGE_ADD_COVER);
                break;
            case R.id.add_profile_image: // Изменения фотографии профиля.
                openGallery(PICK_IMAGE_ADD_ICON);
                break;
            case R.id.add_photo_initiator: // Добавления фотографий в галерею пользователя
                openGalleryMultiple(PICK_IMAGE_MULTIPLE);
                break;

        }
    }

    // Выбор фотографий из галереи пользователя, одно фотография.
    private void openGallery(int requestCode){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, requestCode);
    }

    // Выбор фотграфий из галереи пользователя, множественный выбор.
    private void openGalleryMultiple(int requestCode) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), requestCode);

    }

    public Uri getUri(Intent data){

        Uri uri = null;
        if (data.getData()!= null)
            uri = data.getData();
        else
        {
            if (data.getClipData() != null) {

                ClipData mClipData = data.getClipData();
                ClipData.Item item = mClipData.getItemAt(0);
                uri = item.getUri();


            }
        }
        return uri;
    }

    // Этот метод сработаеть после выбора фотографий пользователем.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_ADD_COVER && resultCode == RESULT_OK && null != data)
            {
                add_cover_uri = getUri(data);

                if(add_cover_uri != null)
                    Picasso.get().load(add_cover_uri)
                            .fit()
                            .centerCrop()
                            .into(backgroundProfileImage);
            }
            if (requestCode == PICK_IMAGE_ADD_ICON && resultCode == RESULT_OK && null != data)
            {
                add_icon_uri = getUri(data);

                if(add_icon_uri != null)
                    Picasso.get().load(add_icon_uri)
                            .fit()
                            .transform(new CircleTransform())
                            .centerCrop()
                            .into(profileImage);
            }

            if (requestCode == PICK_IMAGE_ADD_COVER && resultCode == RESULT_OK && null != data)
            {
                if (data.getData()!= null)
                    add_cover_uri = data.getData();
                else
                {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ClipData.Item item = mClipData.getItemAt(0);
                        add_cover_uri = item.getUri();
                    }
                }

                Picasso.get().load(add_cover_uri)
                        .fit()
                        .centerCrop()
                        .into(backgroundProfileImage);

                //backProfilePhoto.setImageURI(add_cover_uri);

            }

            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData() !=null){

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    galleryUri = new ArrayList<Uri>();

                    galleryUri.add(mImageUri);


                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        galleryUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            galleryUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri,
                                    filePathColumn,
                                    null,
                                    null,
                                    null);

                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();


                        }

                    }
                }
                LayoutInflater inflater = LayoutInflater.from(this);

                for (Uri uri : galleryUri){

                    View view = inflater.inflate(R.layout.gv_item, layoutImagesGallery, false);

                    ImageView imageView = view.findViewById(R.id.ivGallery);
                    imageView.setClipToOutline(true);
                    imageView.setImageURI(uri);


                    layoutImagesGallery.addView(view);
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    // Сохранения новых данных о пользователе(Организаторе).
    private void setInitiatorInformation() {
        if (validateData()) {

            SendInitiatorInformation sendInitiatorInformation = new SendInitiatorInformation(
                    0, editInitiatorEmail.getText().toString(),
                    "", editInitiatorAddress.getText().toString(),
                    0.0, 0.0, 0 , editInitiatorPhone.getText().toString(),
                    editInitiatorName.getText().toString(),
                    Integer.parseInt(editInitiatorZipCode.getText().toString()),
                    editAboutUs.getText().toString(), "",
                    "",""
            );
            initiatorInformation.setEmail(sendInitiatorInformation.getEmail());

            /*sendInitiatorInformation.setEmail(editInitiatorEmail.getText().toString());
            sendInitiatorInformation.setAddress(editInitiatorAddress.getText().toString());
            sendInitiatorInformation.setPhone(editInitiatorPhone.getText().toString());
            sendInitiatorInformation.setName(editInitiatorName.getText().toString());
            sendInitiatorInformation.setZipCode(Integer.parseInt(editInitiatorZipCode.getText().toString()));
            sendInitiatorInformation.setAbout(editAboutUs.getText().toString());*/

            addImages();

            Retrofit retrofit = NetworkClient.getRetrofitClient();
            Initiator organization = retrofit.create(Initiator.class);
            Call call = organization.setProfileInformation(LoginDataSource.getInitiator().getToken(), sendInitiatorInformation);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    System.out.println("----" + response.body().toString());
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        }
    }


    // Запуск загрузки фотографий.
    public void addImages(){
        Image image = new Image();
        image.execute();
    }

    // Загрузка фотографий на гугл драйв через сервер.
    private class Image extends AsyncTask<String, Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... param) {

            Retrofit retrofit = NetworkClient.getRetrofitClient();
            Initiator iOrganization = retrofit.create(Initiator.class);
            System.out.println(" SEND IMAGES -------------------------------------------");
            if (add_icon_uri != null) {
                System.out.println("GO TO -----------------------------");
                MultipartBody.Part part = RetUtils.prepareFilePart(getCtx(), "image", add_icon_uri);
                Call call = iOrganization.addProfileIcon(LoginDataSource.getInitiator().getToken(), part);

                try {
                    Response response = call.execute();
                    System.out.println("+++++" + response);
                    if (response.isSuccessful()) {
                        Messages m = (Messages) response.body();
                        System.out.println(" --------------------------------------- " + m.toString());
                        //sendInitiatorInformation.setIconUrl(m.getMessages());
                    }
                } catch (IOException e) {
                    System.out.println("CAAAATCK" );
                    e.printStackTrace();
                }

            }
            if (add_cover_uri != null) {
                MultipartBody.Part part = RetUtils.prepareFilePart(getCtx(), "image", add_cover_uri);
                Call call = iOrganization.addProfileCover(LoginDataSource.getInitiator().getToken(), part);

                try {
                    Response response = call.execute();

                    if (response.isSuccessful()) {
                        Messages m = (Messages) response.body();
                        //sendInitiatorInformation.setBackgroundImageUrl(m.getMessages());
                    }
                } catch (IOException e) {

                }

            }
            if (galleryUri != null) {
                List<MultipartBody.Part> partList = new ArrayList<>();
                if (galleryUri.size() > 0) {
                    for (Uri uri : galleryUri) {
                        partList.add(RetUtils.prepareFilePart(getCtx(), "image", uri));
                    }

                    Call call = iOrganization.addGalleryPhotos(LoginDataSource.getInitiator().getToken(), partList);
                    try {
                        Response response = call.execute();

                        if (response.isSuccessful()) {
                            List<Photo> photos = (List<Photo>) response.body();

                            //sendInitiatorInformation.setPhotos(photos);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return true;
        }
    }

    //
    public void fillInitiatorInformation(InitiatorInformation initiatorInformationT) {
        initiatorInformation = initiatorInformationT;

        Picasso picasso = Picasso.get();
        picasso.load(STORAGE_URL + initiatorInformation.getBackgroundImageUrl())
                .fit()// Длина и ширина фотографии будет таким каким задано в XML файле.
                .centerCrop()// Get center. Берём ценрт фотографии, все что помешается в заданный размер.
                .into(backgroundProfileImage);

        picasso.load(STORAGE_URL + initiatorInformation.getIconUrl())
                .fit()
                .placeholder(R.drawable.photo)
                .error(R.drawable.no_photo)
                .centerCrop()
                .transform(new CircleTransform())
                .into(profileImage);


        LayoutInflater inflater = LayoutInflater.from(this);

        for (Photo photo : initiatorInformation.getPhotos()){

            if (!photo.getUrl().isEmpty()){

                View view = inflater.inflate(R.layout.gv_item, layoutImagesGallery, false);

                ImageView imageView = view.findViewById(R.id.ivGallery);
                imageView.setClipToOutline(true);
                picasso.load(STORAGE_URL + photo.getUrl())
                        .fit()
                        .centerCrop()
                        .into(imageView);


                layoutImagesGallery.addView(view);
            }

        }

        // Проверка на то, чтобы полученные поля не были
        if (initiatorInformation.getName() != null){
            initiatorName.setText(initiatorInformation.getName());
        }

        if (initiatorInformation.getEmail() != null) {
            initiatorEmail.setText(initiatorInformation.getEmail());
        }

        if (initiatorInformation.getName() != null) {
            editInitiatorName.setText(initiatorInformation.getName());
        }

        if (initiatorInformation.getEmail() != null) {
            editInitiatorEmail.setText(initiatorInformation.getEmail());
        }

        if (initiatorInformation.getPhone() != null) {
            editInitiatorPhone.setText(initiatorInformation.getPhone());
        }

        if (initiatorInformation.getAddress() != null) {
            editInitiatorAddress.setText(initiatorInformation.getAddress());
        }

        if (true) {
            editInitiatorZipCode.setText(String.valueOf(initiatorInformation.getZipCode()));
        }

        if (initiatorInformation.getAbout() != null) {
            editAboutUs.setText(initiatorInformation.getAbout());
        }
    }

    private boolean validateData() {
        String validRexEmail = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        String validRexPhone = "(\\+1|0)[0-9]{10}";
        String name = editInitiatorName.getText().toString();
        if (name.equals("")) {

            editInitiatorName.setError( getString(R.string.initiator_name_error));
            editInitiatorName.requestFocus();
            return false;
        }

        String email = editInitiatorEmail.getText().toString();
        // Проверка на правилность почты.
        if (email.equals("")) {

            editInitiatorEmail.setError( getString(R.string.email_error));
            editInitiatorEmail.requestFocus();
            return false;
        }
        else if (!email.matches(validRexEmail)){
            editInitiatorEmail.setError( getString(R.string.incorrect_email));
            editInitiatorEmail.requestFocus();
            return false;
        }

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator initiator = retrofit.create(Initiator.class);
        Call call = initiator.validationEmil(email);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                System.out.println("code="+response.code());
                if(response.code() != 404 ) {

                    emailIsExists = true;
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                emailIsExists = true;
            }
        });

        if (emailIsExists && !email.equals(initiatorInformation.getEmail())){
            editInitiatorEmail.setError( getString(R.string.email_error_exists));
            editInitiatorEmail.requestFocus();
            emailIsExists = false;
            return false;
        }

        String phone = editInitiatorPhone.getText().toString();
        // Проверка на правилность номера телефона.
        if (phone.equals("")) {

            editInitiatorPhone.setError( getString(R.string.phone_error));
            editInitiatorPhone.requestFocus();
            return false;
        }
        else if (!phone.matches(validRexPhone)){
            editInitiatorPhone.setError( getString(R.string.incorrect_phone));
            editInitiatorPhone.requestFocus();
            return false;
        }
        return true;
    }

    public Context getCtx()
    {
        return getApplicationContext();
    }
}
