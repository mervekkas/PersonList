package com.example.personlist.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.personlist.database.DataBase;
import com.example.personlist.database.PersonsDao;
import com.example.personlist.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class NewPersonFragment extends Fragment {
    private Spinner spPhoneCode;
    private ArrayAdapter<String> adapterPhoneCode;
    private String[] phoneCodes = {"+90", "+1"};
    private DatePicker picker;
    private Button clickSaveUpdate, clickDatePicker;
    private EditText etName, etSurname, etMail, etPhoneNumber, etDateBirth, etNote;
    private TextView tvMailError, tvNameError, tvSurnameError, tvPhoneError, tvBirthError;
    private ImageView imgBackClick;
    private TextInputLayout txtlDate, txtlEmail, txtlName, txtlSurname;
    private ConstraintLayout clPhone;
    private TextWatcher textWatcher;
    private String stPhoneCode = "", stPhoneNumber = "", stDateOfBirth = "", stName = "", stSurname = "", stNote = "", stEmail = "";
    private NewPersonType type = NewPersonType.UNKNOWN;
    private int intpersonId = -1;
    private DataBase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_person_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArguments(getArguments());
        getViews(view);
        if (type == NewPersonType.EDIT) fillValues();

    }

    private void initArguments(Bundle savedInstanceState) {
        type = (NewPersonType) savedInstanceState.getSerializable("type");
        intpersonId = savedInstanceState.getInt("id");
        stName = savedInstanceState.getString("name");
        stSurname = savedInstanceState.getString("surname");
        stDateOfBirth = savedInstanceState.getString("date");
        stEmail = savedInstanceState.getString("mail");
        stPhoneNumber = savedInstanceState.getString("phoneNumber");
        stNote = savedInstanceState.getString("note");

    }

    private void fillValues() {
        etName.setText(stName);
        etSurname.setText(stSurname);
        etDateBirth.setText(stDateOfBirth);
        etMail.setText(stEmail);
        if (stPhoneNumber != null) {
            String[] phoneParse = stPhoneNumber.split(" ");
            stPhoneCode = phoneParse[0];
            stPhoneNumber = "";
            for (int i = 0; i < phoneParse.length - 1; i++) {
                stPhoneNumber += " " + phoneParse[i + 1];
            }
            etPhoneNumber.setText(stPhoneNumber);
        }
        etNote.setText(stNote);

        if (stPhoneCode != null) {
            int position = adapterPhoneCode.getPosition(stPhoneCode);
            spPhoneCode.setSelection(position);
        }

    }

    private void getViews(View v) {

        //layout
        txtlDate = v.findViewById(R.id.txtl_date);
        txtlEmail = v.findViewById(R.id.txtl_email);
        txtlName = v.findViewById(R.id.txtl_name);
        txtlSurname = v.findViewById(R.id.txtl_surname);
        clPhone = v.findViewById(R.id.cl_phone);

        //Edittext
        etName = v.findViewById(R.id.person_add_name);
        etSurname = v.findViewById(R.id.person_add_surname);
        etMail = v.findViewById(R.id.person_add_email);
        etPhoneNumber = v.findViewById(R.id.person_add_phone);
        etNote = v.findViewById(R.id.person_add_note);

        //TextView-İmgView
        tvMailError = v.findViewById(R.id.tv_email_error);
        tvNameError = v.findViewById(R.id.tv_name_error);
        tvSurnameError = v.findViewById(R.id.tv_surname_error);
        tvPhoneError = v.findViewById(R.id.tv_phone_error);
        tvBirthError = v.findViewById(R.id.tv_date_error);
        imgBackClick = v.findViewById(R.id.img_back);

        //spinner
        spPhoneCode = v.findViewById(R.id.sp_phone_code);
        adapterPhoneCode = new ArrayAdapter<String>(getContext(), R.layout.item_simple_spinner, phoneCodes);
        adapterPhoneCode.setDropDownViewResource(R.layout.item_simple_spinner);
        spPhoneCode.setAdapter(adapterPhoneCode);

        //datePicker
        picker = v.findViewById(R.id.datePicker1);
        etDateBirth = v.findViewById(R.id.person_add_date);
        clickDatePicker = v.findViewById(R.id.btn_date);

        db = new DataBase(getContext());

        clickSaveUpdate = v.findViewById(R.id.bt_save_update);

        initClick();
    }

    private void initClick() {
        spinnerItemSelected();
        clickDateOfBirth();
        clickDatePicker();
        clickSaveUpdate();
        clickBack();
    }

    private void clickSaveUpdate() {
        clickSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(etMail.getText().toString())
                        && isValidMinLenght(etName, tvNameError)
                        && isValidMinLenght(etSurname, tvSurnameError)
                        && isValidPhone(etPhoneNumber.getText().toString(), stPhoneCode)
                        && isValidControlDateOfBirth()) {
                    stName = etName.getText().toString();
                    stSurname = etSurname.getText().toString();
                    stPhoneNumber = stPhoneCode + " " + etPhoneNumber.getText().toString();
                    stDateOfBirth = etDateBirth.getText().toString();
                    stEmail = etMail.getText().toString();
                    stNote = etNote.getText().toString();

                    if (type == NewPersonType.NEW)
                        new PersonsDao().personAdd(db, stName, stSurname, stDateOfBirth, stEmail, stPhoneNumber, stNote);
                    else if (type == NewPersonType.EDIT) {

                        stName = etName.getText().toString().trim();
                        stSurname = etSurname.getText().toString().trim();
                        stPhoneNumber = stPhoneCode + " " + etPhoneNumber.getText().toString().trim();
                        stDateOfBirth = etDateBirth.getText().toString().trim();
                        stEmail = etMail.getText().toString().trim();
                        stNote = etNote.getText().toString().trim();
                        new PersonsDao().personUpdate(db, intpersonId, stName, stSurname, stDateOfBirth, stEmail, stPhoneNumber, stNote);
                    }

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_layout);
                    dialog.show();
                } else {
                    isValidControl(txtlEmail, tvMailError, isValidEmail(etMail.getText().toString()));
                    isValidControl(txtlName, tvNameError, isValidMinLenght(etName, tvNameError));
                    isValidControl(txtlSurname, tvSurnameError, isValidMinLenght(etSurname, tvSurnameError));
                    isValidControlPhone(isValidPhone(etPhoneNumber.getText().toString(), stPhoneCode));
                    isValidControl(txtlDate, tvBirthError, isValidControlDateOfBirth());
                }
            }
        });

    }

    private void textWatcher() {

        etPhoneNumber.addTextChangedListener(textWatcher = new TextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length() - etPhoneNumber.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!backspacingFlag) {
                    String string = s.toString();
                    String phone = string.replaceAll("[^\\d]", "");

                    if (!editedFlag) {
                        if (etPhoneNumber.getText().toString().length() > 0) {
                            etPhoneNumber.setSelection(etPhoneNumber.getText().length() - cursorComplement);
                        }
                        if (phone.length() >= 8) {
                            editedFlag = true;
                            String ans = phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6, 8) + " " + phone.substring(8);
                            etPhoneNumber.setText(ans);
                            etPhoneNumber.setSelection(etPhoneNumber.getText().length() - cursorComplement);
                        } else if (phone.length() >= 6) {
                            editedFlag = true;
                            String ans = phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6);
                            etPhoneNumber.setText(ans);
                            etPhoneNumber.setSelection(etPhoneNumber.getText().length() - cursorComplement);
                        } else if (phone.length() >= 3) {
                            editedFlag = true;
                            String ans = phone.substring(0, 3) + " " + phone.substring(3);
                            etPhoneNumber.setText(ans);
                            etPhoneNumber.setSelection(etPhoneNumber.getText().length() - cursorComplement);
                        }
                        stPhoneNumber = string;
                    } else
                        editedFlag = false;

                }
            }
        });
    }

    private void spinnerItemSelected() {
        spPhoneCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (stPhoneCode != null) {

                }
                if (parent.getSelectedItem().equals("+90")) {
                    stPhoneCode = "+90";
                    etPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                    textWatcher();

                } else {
                    stPhoneCode = parent.getSelectedItem().toString();
                    etPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                    if (textWatcher != null)
                        etPhoneNumber.removeTextChangedListener(textWatcher);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean isValidControlDateOfBirth() {
        if (stDateOfBirth == null || stDateOfBirth.equals(" "))
            return false;
        else
            return true;
    }

    private boolean isValidControl(TextInputLayout inputLayout, TextView textView, Boolean isValid) {
        if (!isValid) {
            inputLayout.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edittext_error));
            textView.setVisibility(View.VISIBLE);
            return false;
        } else {
            inputLayout.setBackground(getContext().getResources().getDrawable(R.drawable.bg_search_person_text));
            textView.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidPhone(String phone, String phoneCode) {
        if (phone.isEmpty())
            return false;
        else {
            if (phoneCode != "+90")
                return true;
            else if (phoneCode == "+90" && phone.length() < 13)
                return false;

            return true;
        }
    }

    private boolean isValidControlPhone(Boolean isValid) {
        if (isValid) {
            clPhone.setBackground(getContext().getResources().getDrawable(R.drawable.bg_search_person_text));
            tvPhoneError.setVisibility(View.GONE);
            return true;
        } else {
            clPhone.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edittext_error));
            tvPhoneError.setVisibility(View.VISIBLE);
            return false;
        }

    }

    private void clickDatePicker() {
        clickDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDateBirth.setText(picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());
                stDateOfBirth = etDateBirth.getText().toString();
                picker.setVisibility(View.GONE);
                clickDatePicker.setVisibility(View.GONE);
            }
        });
    }

    private void clickDateOfBirth() {
        etDateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setVisibility(View.VISIBLE);
                clickDatePicker.setVisibility(View.VISIBLE);
            }
        });
    }

    public boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            tvMailError.setText(R.string.email_null);
            return false;
        } else
            return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public boolean isValidMinLenght(final EditText editText, TextView errorText) {
        if (editText.getText().toString().trim().length() < 2) {
            errorText.setText(R.string.min_text_error);
            return false;
        } else
            return true;
    }

    public static NewPersonFragment newInstance(NewPersonType type, int id, String name, String surname,
                                                String date, String mail, String phoneNumber, String note) {

        Bundle args = new Bundle();
        args.putSerializable("type", type);
        args.putInt("id", id);
        args.putString("name", name);
        args.putString("surname", surname);
        args.putString("date", date);
        args.putString("mail", mail);
        args.putString("phoneNumber", phoneNumber);
        args.putString("note", note);
        NewPersonFragment fragment = new NewPersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NewPersonFragment newInstance(NewPersonType type) {

        Bundle args = new Bundle();
        args.putSerializable("type", type);

        NewPersonFragment fragment = new NewPersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stPhoneCode = "";
        stPhoneNumber = "";
        stDateOfBirth = "";
        stName = "";
        stSurname = "";
        stNote = "";
        stEmail = "";
        type = NewPersonType.UNKNOWN;
    }

    public enum NewPersonType {
        UNKNOWN(-1),
        NEW(0),
        EDIT(1);

        private int type;

        NewPersonType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }


    private void clickBack() {
        imgBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
