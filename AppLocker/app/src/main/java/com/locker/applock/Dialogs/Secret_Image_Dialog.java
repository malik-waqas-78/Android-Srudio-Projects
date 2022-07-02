package com.locker.applock.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.locker.applock.Adapters.Custom_Adapter_Secret_Question;
import com.locker.applock.Interfaces.Secret_Image_Interface;
import com.locker.applock.Models.Secret_Image_Item;
import com.locker.applock.R;
import com.locker.applock.Utils.SharedPrefHelper;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.locker.applock.Utils.Constants.IS_SECRET_QUESTION_SET;
import static com.locker.applock.Utils.Constants.SECRET_BOOK;
import static com.locker.applock.Utils.Constants.SECRET_CAR;
import static com.locker.applock.Utils.Constants.SECRET_CITY;
import static com.locker.applock.Utils.Constants.SECRET_IMAGE_ID;
import static com.locker.applock.Utils.Constants.SECRET_MOVIE;
import static com.locker.applock.Utils.Constants.SECRET_PET;
import static com.locker.applock.Utils.Constants.SECRET_QUESTION_ANSWER;
import static com.locker.applock.Utils.Constants.SECRET_SONG;

public class Secret_Image_Dialog {
    private static final int SELECTED_IME_ACTION = EditorInfo.IME_ACTION_DONE;
    static int secret_img_val;
    private static Context mContext;
    private static Custom_Adapter_Secret_Question adapter_secret_question;

    public static void create_secret_image_dialog_box(Context context, String title) {
        mContext = context;
        secret_img_val = 0;
        Dialog dialog = new Dialog(context);
        SharedPrefHelper shared_pref_helper = new SharedPrefHelper(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_secret_image_dialog);
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) dialog.findViewById(R.id.secret_image_title)).setText(title);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_secret_image);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_secret_image);
        EditText answer = dialog.findViewById(R.id.edit_text_answer_secret_image);
        ScrollView scrollView = dialog.findViewById(R.id.scrollView_exit_dialog);
        answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    scrollView.scrollTo(0, scrollView.getBottom());
                }
            }
        });

        answer.setImeOptions(SELECTED_IME_ACTION);
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == SELECTED_IME_ACTION) {
                    if (secret_img_val != 0) {
                        if (answer.getText().toString().length() > 0) {
                            shared_pref_helper.Set_Int_AL(SECRET_IMAGE_ID, secret_img_val);
                            shared_pref_helper.Set_String_AL(SECRET_QUESTION_ANSWER, answer.getText().toString());
                            shared_pref_helper.Set_Boolean_AL(IS_SECRET_QUESTION_SET, true);
                            dialog.dismiss();
                        } else {
                            answer.setError(context.getResources().getString(R.string.select_answer_first));
                        }
                    } else {
                        answer.setError(context.getResources().getString(R.string.select_image_first));
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        btnConfirm.setOnClickListener(view -> {
            if (secret_img_val != 0) {
                if (answer.getText().toString().length() > 0) {
                    shared_pref_helper.Set_Int_AL(SECRET_IMAGE_ID, secret_img_val);
                    shared_pref_helper.Set_String_AL(SECRET_QUESTION_ANSWER, answer.getText().toString());
                    shared_pref_helper.Set_Boolean_AL(IS_SECRET_QUESTION_SET, true);
                    dialog.dismiss();
                } else {
                    answer.setError(context.getResources().getString(R.string.select_answer_first));
                }
            } else {
                answer.setError(context.getResources().getString(R.string.select_image_first));
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_secret_question);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        ArrayList<Secret_Image_Item> dataset = getList();
        adapter_secret_question = new Custom_Adapter_Secret_Question(dataset, context, new Secret_Image_Interface() {
            @Override
            public void OnClick(ArrayList<Secret_Image_Item> mDataSet, int adapterPosition) {
                if (mDataSet != null && (mDataSet.size() - 1) >= adapterPosition && mDataSet.get(adapterPosition) != null) {
                    Secret_Image_Item image_item = mDataSet.get(adapterPosition);
                    if (image_item.getSelected() == 8) {
                        image_item.setSelected(0);
                        for (int i = 0; i < mDataSet.size(); i++) {
                            if (i != adapterPosition) {
                                mDataSet.get(i).setSelected(8);
                            }
                        }
                        secret_img_val = mDataSet.get(adapterPosition).getSecretImage();
                        adapter_secret_question.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void initiateResetLockActivity() {
                //Do nothing here
            }
        });
        recyclerView.setAdapter(adapter_secret_question);
        dialog.show();
    }

    public static void create_forgot_image_dialog_box(Context context, String title, Secret_Image_Interface secret_image_interface) {
        secret_img_val = 0;
        mContext = context;
        Dialog dialog = new Dialog(context);
        SharedPrefHelper shared_pref_helper = new SharedPrefHelper(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_secret_image_dialog);
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView) dialog.findViewById(R.id.secret_image_title)).setText(title);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_secret_image);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_secret_image);
        EditText answer = dialog.findViewById(R.id.edit_text_answer_secret_image);

        if (!shared_pref_helper.Get_Boolean_AL(IS_SECRET_QUESTION_SET, false)) {
            answer.setError(context.getResources().getString(R.string.no_secret_image_set));
            dialog.dismiss();
            return;
        }

        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_secret_question);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        ArrayList<Secret_Image_Item> dataset = getList();
        adapter_secret_question = new Custom_Adapter_Secret_Question(dataset, context, new Secret_Image_Interface() {
            @Override
            public void OnClick(ArrayList<Secret_Image_Item> mDataSet, int adapterPosition) {
                Secret_Image_Item image_item = mDataSet.get(adapterPosition);
                if (image_item.getSelected() == 8) {
                    image_item.setSelected(0);
                    for (int i = 0; i < mDataSet.size(); i++) {
                        if (i != adapterPosition) {
                            mDataSet.get(i).setSelected(8);
                        }
                    }
                    secret_img_val = mDataSet.get(adapterPosition).getSecretImage();
                    adapter_secret_question.notifyDataSetChanged();
                }
            }

            @Override
            public void initiateResetLockActivity() {
                //Do nothing here
            }
        });
        recyclerView.setAdapter(adapter_secret_question);
        answer.setImeOptions(SELECTED_IME_ACTION);
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == SELECTED_IME_ACTION) {
                    int saved_val = shared_pref_helper.Get_Int_AL(SECRET_IMAGE_ID, 0);
                    String saved_answer = shared_pref_helper.Get_String_AL(SECRET_QUESTION_ANSWER, "-");
                    if (saved_val != 0 && secret_img_val != 0) {
                        if (secret_img_val == saved_val) {
                            if (saved_answer.equals(answer.getText().toString())) {
                                secret_image_interface.initiateResetLockActivity();
                                dialog.dismiss();
                            } else {
                                answer.setError(context.getResources().getString(R.string.wrong_answer));
                            }
                        } else {
                            answer.setError(context.getResources().getString(R.string.wrong_image));
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        btnConfirm.setOnClickListener(view -> {
            int saved_val = shared_pref_helper.Get_Int_AL(SECRET_IMAGE_ID, 0);
            String saved_answer = shared_pref_helper.Get_String_AL(SECRET_QUESTION_ANSWER, "-");
            if (saved_val != 0 && secret_img_val != 0) {
                if (secret_img_val == saved_val) {
                    if (saved_answer.equals(answer.getText().toString())) {
                        secret_image_interface.initiateResetLockActivity();
                        dialog.dismiss();
                    } else {
                        answer.setError(context.getResources().getString(R.string.wrong_answer));
                    }
                } else {
                    answer.setError(context.getResources().getString(R.string.wrong_image));
                }
            } else {
                answer.setError(context.getResources().getString(R.string.wrong_answer));
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void create_change_secret_image_dialog_box(Context context, String title) {
        mContext = context;
        secret_img_val = 0;
        Dialog dialog = new Dialog(context);
        SharedPrefHelper shared_pref_helper = new SharedPrefHelper(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_secret_image_dialog);
        dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView) dialog.findViewById(R.id.secret_image_title)).setText(title);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_secret_image);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_secret_image);
        EditText answer = dialog.findViewById(R.id.edit_text_answer_secret_image);

        if (!shared_pref_helper.Get_Boolean_AL(IS_SECRET_QUESTION_SET, false)) {
            Toasty.error(context
                    , context.getResources().getString(R.string.no_secret_image_set)
                    , Toasty.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }

        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_secret_question);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        ArrayList<Secret_Image_Item> dataset = getList();
        adapter_secret_question = new Custom_Adapter_Secret_Question(dataset, context, new Secret_Image_Interface() {
            @Override
            public void OnClick(ArrayList<Secret_Image_Item> mDataSet, int adapterPosition) {
                Secret_Image_Item image_item = mDataSet.get(adapterPosition);
                if (image_item.getSelected() == 8) {
                    image_item.setSelected(0);
                    for (int i = 0; i < mDataSet.size(); i++) {
                        if (i != adapterPosition) {
                            mDataSet.get(i).setSelected(8);
                        }
                    }
                    secret_img_val = mDataSet.get(adapterPosition).getSecretImage();
                    adapter_secret_question.notifyDataSetChanged();
                }
            }

            @Override
            public void initiateResetLockActivity() {
                //Do nothing here
            }
        });
        recyclerView.setAdapter(adapter_secret_question);
        answer.setImeOptions(SELECTED_IME_ACTION);
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                final int saved_val = shared_pref_helper.Get_Int_AL(SECRET_IMAGE_ID, 0);
                final String saved_answer = shared_pref_helper.Get_String_AL(SECRET_QUESTION_ANSWER, "-");
                if (id == SELECTED_IME_ACTION) {
                    if (secret_img_val == saved_val) {
                        if (saved_answer.equals(answer.getText().toString())) {
                            Secret_Image_Dialog.create_secret_image_dialog_box(context,
                                    context.getApplicationContext().getString(R.string.secret_image_title_for_change_new));
                        } else {
                            Toasty.error(context
                                    , context.getResources().getString(R.string.wrong_answer)
                                    , Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        Toasty.error(context
                                , context.getResources().getString(R.string.wrong_image)
                                , Toasty.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            final int saved_val = shared_pref_helper.Get_Int_AL(SECRET_IMAGE_ID, 0);
            final String saved_answer = shared_pref_helper.Get_String_AL(SECRET_QUESTION_ANSWER, "-");

            @Override
            public void onClick(View view) {
                if (secret_img_val == saved_val) {
                    if (saved_answer.equals(answer.getText().toString())) {
                        Secret_Image_Dialog.create_secret_image_dialog_box(context,
                                context.getApplicationContext().getString(R.string.secret_image_title_for_change_new));
                    } else {
                        Toasty.error(context
                                , context.getResources().getString(R.string.wrong_answer)
                                , Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(context
                            , context.getResources().getString(R.string.wrong_image)
                            , Toasty.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private static ArrayList<Secret_Image_Item> getList() {
        ArrayList<Secret_Image_Item> mDataset = new ArrayList<>();
        mDataset.add(new Secret_Image_Item(SECRET_BOOK, 8, mContext.getString(R.string.secret_book)));
        mDataset.add(new Secret_Image_Item(SECRET_MOVIE, 8, mContext.getString(R.string.secret_movie)));
        mDataset.add(new Secret_Image_Item(SECRET_SONG, 8, mContext.getString(R.string.secret_song)));
        mDataset.add(new Secret_Image_Item(SECRET_CAR, 8, mContext.getString(R.string.secret_car)));
        mDataset.add(new Secret_Image_Item(SECRET_CITY, 8, mContext.getString(R.string.secret_city)));
        mDataset.add(new Secret_Image_Item(SECRET_PET, 8, mContext.getString(R.string.secret_pet)));
        return mDataset;
    }

}
