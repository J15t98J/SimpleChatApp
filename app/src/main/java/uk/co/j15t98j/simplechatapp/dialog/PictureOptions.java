package uk.co.j15t98j.simplechatapp.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import uk.co.j15t98j.simplechatapp.R;

public class PictureOptions extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AppTheme);
        dialog.setTitle("Change profile picture")
                .setItems(R.array.picture_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i) {
                            case 0:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
                                    getActivity().startActivityForResult(takePicture, 1);
                                }

                                break;

                            case 1:
                                Intent selectPicture = new Intent();
                                selectPicture.setType("image/*");
                                selectPicture.setAction(Intent.ACTION_GET_CONTENT);
                                getActivity().startActivityForResult(Intent.createChooser(selectPicture, "Select Picture"), 2);

                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", null);
        return dialog.create();
    }
}
