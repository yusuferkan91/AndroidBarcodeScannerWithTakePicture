package com.example.els_v2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreatePdfActivity extends Activity {

    EditText pdf_title;
    Button btn_create_pdf;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpdf);
        pdf_title = (EditText) findViewById(R.id.pdf_title);
        btn_create_pdf = (Button) findViewById(R.id.btn_create);
        getWindow().setLayout((int) (250* this.getResources().getDisplayMetrics().density), (int) (150* this.getResources().getDisplayMetrics().density));
        pdf_title.requestFocus();
        btn_create_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdf_title.getText().length()>0){
                    Toast.makeText(CreatePdfActivity.this, "PDF Oluşturuluyor..", Toast.LENGTH_SHORT).show();

                    btn_create_pdf.setEnabled(false);
                    pdfCreate(pdf_title.getText().toString());

                    btn_create_pdf.setEnabled(true);
                }
            }
        });
    }
    public void pdfCreate(String title){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_hh-mm-ss-SSS");
        String name = "ELS-" + sdf.format(c.getTime()) + ".pdf";
        String pdfFilePath = "";
        try {

            OutputStream fos = null;
            String IMAGES_FOLDER_NAME = Environment.DIRECTORY_DOCUMENTS + File.separator;

            System.out.println("try:::::::::::");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = this.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "files/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, IMAGES_FOLDER_NAME);

                Uri uri = null;
                final Uri contentUri = MediaStore.Files.getContentUri("external");
                uri = resolver.insert(contentUri, contentValues);
                ParcelFileDescriptor pfd;
                System.out.println("try::if:::::::::");
                try {
                    assert uri != null;
                    pfd = getContentResolver().openFileDescriptor(uri, "w");
                    assert pfd != null;
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    pdfFilePath = dir.getPath() + "/";
                    System.out.println("pdfFilePath::" + pdfFilePath);
                    System.out.println("contentUri.getPath()::" + contentUri.getPath());
                    System.out.println("uri.getPath()::" + uri.getPath());
                    fos = new FileOutputStream(pdfFilePath+name);
                    System.out.println("try::if::try::::::::::"+ pfd.getFileDescriptor());
                }catch (Exception e){
                    System.out.println("if hata::" + e.getMessage());
                }
                System.out.println("if************************");
            } else {
                pdfFilePath = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS).toString() + File.separator;
                System.out.println("else*********************"+pdfFilePath);
                File file = new File(pdfFilePath);
                if (!file.exists()) {
                    file.mkdir();
                    System.out.println("else:::if:::");
                }
                File image = new File(pdfFilePath, name);
                fos = new FileOutputStream(image);
            }

            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            if(!document.isOpen())
                document.open();
            System.out.println("documents open??" +document.isOpen());

            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
            Chunk ch = new Chunk(title, f);

            Paragraph p1 = new Paragraph(ch);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);

            Paragraph pdf_title = new Paragraph();
            pdf_title.setAlignment(Element.ALIGN_RIGHT);
            String pdf_time = sdf.format(c.getTime()).split("_")[0] + "\n";
            pdf_title.add(pdf_time);

            document.add(pdf_title);
            document.add(new Paragraph("    "));
            PdfPTable table = new PdfPTable(2);

            for(String txt: BarkodClass.list_result){
                PdfPCell cell1 = new PdfPCell(new Paragraph(txt));

                table.addCell(cell1);

                Bitmap image = BarkodClass.list_bitmap.get(BarkodClass.list_result.indexOf(txt));
                Bitmap resized = Bitmap.createScaledBitmap(image, (int)(image.getWidth()*0.8), (int)(image.getHeight()*0.8), true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
                byte[] imageBytes = stream.toByteArray();
                Image myImg = Image.getInstance(imageBytes);

                table.addCell(myImg);

            }
            System.out.println("for end:::::::::");
            // Add table in document
            document.add(table);
            System.out.println("doc add:::");
            document.close();
            System.out.println("doc close::");
            fos.close();
            System.out.println("fos close::");
            Toast.makeText(this, "PDF Kaydedildi>>" + pdfFilePath + name, Toast.LENGTH_SHORT).show();
            System.out.println("PDF created in >> " + pdfFilePath+name);

        } catch (Exception e) {
            System.out.println("hata::" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println(pdfFilePath + name);
        File file = new File( pdfFilePath , name);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("hata mesajı::" + e.getMessage());
        }
    }

}
