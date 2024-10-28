package com.accessa.ibora.sales.Sales;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.accessa.ibora.R;

public class SearchDialogFragment extends DialogFragment {

    private EditText editTextOption1;
    private OnSearchListener searchListener;

    public interface OnSearchListener {
        void onSearch(String query);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            searchListener = (OnSearchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSearchListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search, null);

        editTextOption1 = dialogView.findViewById(R.id.search_edit_text);


        editTextOption1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // You can leave this empty if you don't need it
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Start searching as the user types
                String query = s.toString();
                searchListener.onSearch(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // You can leave this empty if you don't need it
            }
        });

        // Find the number buttons and set OnClickListener
        Button button1 = dialogView.findViewById(R.id.button1);
        Button button2 = dialogView.findViewById(R.id.button2);
        Button button3 = dialogView.findViewById(R.id.button3);
        Button button4 = dialogView.findViewById(R.id.button4);
        Button button5 = dialogView.findViewById(R.id.button5);
        Button button6 = dialogView.findViewById(R.id.button6);
        Button button7 = dialogView.findViewById(R.id.button7);
        Button button8 = dialogView.findViewById(R.id.button8);
        Button button9 = dialogView.findViewById(R.id.button9);
        Button button0 = dialogView.findViewById(R.id.button0);
        Button buttonbackspace = dialogView.findViewById(R.id.buttonbackspace);
        Button buttonComma = dialogView.findViewById(R.id.buttonComma);
        Button buttonClear = dialogView.findViewById(R.id.buttonClear);
        Button buttonA = dialogView.findViewById(R.id.buttonInsertA);
        Button buttonB = dialogView.findViewById(R.id.buttonInsertB);
        Button buttonC = dialogView.findViewById(R.id.buttonInsertC);
        Button buttonD = dialogView.findViewById(R.id.buttonInsertD);
        Button buttonE = dialogView.findViewById(R.id.buttonInsertE);
        Button buttonF = dialogView.findViewById(R.id.buttonInsertF);
        Button buttonG = dialogView.findViewById(R.id.buttonInsertG);
        Button buttonH = dialogView.findViewById(R.id.buttonInsertH);
        Button buttonI = dialogView.findViewById(R.id.buttonInsertI);
        Button buttonJ = dialogView.findViewById(R.id.buttonInsertJ);
        Button buttonK = dialogView.findViewById(R.id.buttonInsertK);
        Button buttonL = dialogView.findViewById(R.id.buttonInsertL);
        Button buttonM = dialogView.findViewById(R.id.buttonInsertM);
        Button buttonN = dialogView.findViewById(R.id.buttonInsertN);
        Button buttonO = dialogView.findViewById(R.id.buttonInsertO);
        Button buttonP = dialogView.findViewById(R.id.buttonInsertP);
        Button buttonQ = dialogView.findViewById(R.id.buttonInsertQ);
        Button buttonR = dialogView.findViewById(R.id.buttonInsertR);
        Button buttonS = dialogView.findViewById(R.id.buttonInsertS);
        Button buttonT = dialogView.findViewById(R.id.buttonInsertT);
        Button buttonU = dialogView.findViewById(R.id.buttonInsertU);
        Button buttonV = dialogView.findViewById(R.id.buttonInsertV);
        Button buttonW = dialogView.findViewById(R.id.buttonInsertW);
        Button buttonX = dialogView.findViewById(R.id.buttonInsertX);
        Button buttonY = dialogView.findViewById(R.id.buttonInsertY);
        Button buttonZ = dialogView.findViewById(R.id.buttonInsertZ);
        Button buttonSpace = dialogView.findViewById(R.id.buttonInsertspace);
        // Set OnClickListener for number buttons
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "0");
            }
        });



        buttonbackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackspaceButtonClick();
            }
        });

        buttonComma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, ",");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick(v);
            }        });


        buttonSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, " ");
            }
        });
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "A");
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "B");
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "C");
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "D");
            }
        });
        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "E");
            }
        });
        buttonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "F");
            }
        });
        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "G");
            }
        });
        buttonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "H");
            }
        });
        buttonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "I");
            }
        });
        buttonJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "J");
            }
        });
        buttonK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "K");
            }
        });
        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "L");
            }
        });
        buttonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "M");
            }
        });
        buttonN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "N");
            }
        });
        buttonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "O");
            }
        });
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "P");
            }
        });
        buttonQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Q");
            }
        });
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "R");
            }
        });
        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "S");
            }
        });
        buttonT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "T");
            }
        });
        buttonU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "U");
            }
        });
        buttonV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "V");
            }
        });
        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "W");
            }
        });
        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "X");
            }
        });
        buttonY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Y");
            }
        });
        buttonZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Z");
            }
        });

        builder.setView(dialogView)
                .setTitle("Search")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String query = editTextOption1.getText().toString();
                        searchListener.onSearch(query);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SearchDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public void oncommentButtonClick(View view, String letter) {
        if (editTextOption1 != null) {
            // Insert the letter into the EditText
            editTextOption1.append(letter);
        }
    }
    private void onBackspaceButtonClick() {
        // Get the current text from editTextOption1
        Editable editable = editTextOption1.getText();

        // Get the length of the text
        int length = editable.length();

        // If there are characters in the text, remove the last character
        if (length > 0) {
            editable.delete(length - 1, length);
        }
    }
    public void onClearButtonClick(View view) {

        onclearButtonClick();


    }

    private void onclearButtonClick() {
        editTextOption1.setText(""); // Set the text of editTextOption1 to an empty string
    }
}
