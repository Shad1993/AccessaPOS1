package com.accessa.ibora.Receipt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.bumptech.glide.Glide;

public class ReceiptBodyFragment extends Fragment {

    private boolean doubleBackToExitPressedOnce = false;
    private RecyclerView mRecyclerView;
    private CompletedTransactionsAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String cashierName,cashierId;
    private String cashorlevel,ShopName,LogoPath;
    private double totalAmount,TaxtotalAmount;
    private  String DateCreated,timeCreated,TenderAmount,CashReturn,PosNum;
    private  int CashiorId;
    private ReceiptAdapter receiptAdapter;
    private static final String POSNumber="posNumber";
    private SearchView searchView;
    private Spinner dateFilterSpinner;
    private String transactionId;
    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaction_details, container, false);

        // Retrieve the ID from arguments
        transactionId = getArguments().getString("id");



        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Set the transaction details to TextViews or any other views in your layout
        TextView transactionIdTextView = view.findViewById(R.id.transaction_id_text_view);
        TextView transactionDateTextView = view.findViewById(R.id.transaction_date_text_view);
        TextView totalPriceTextView = view.findViewById(R.id.total_price_text_view);
        TextView companyTextView = view.findViewById(R.id.companyName_text_view);
        TextView compAdressTextView = view.findViewById(R.id.compaddr1n2_text_view);
        TextView compAdress3TextView = view.findViewById(R.id.compaddr3_text_view);
        TextView ShopnameTextView = view.findViewById(R.id.shopName_text_view);
        TextView shopAdress1n2TextView = view.findViewById(R.id.adr1nadr1_text_view);
        TextView shopAdress3TextView = view.findViewById(R.id.adr3_text_view);
        TextView VATTextView = view.findViewById(R.id.VAT_text_view);
        TextView BRNTextView = view.findViewById(R.id.BRN_text_view);
        TextView CashierName= view.findViewById(R.id.cashior_text_view);
        TextView CashierID= view.findViewById(R.id.cashiorid_text_view);
        TextView PosNo= view.findViewById(R.id.POS_text_view);
        TextView VatTypes= view.findViewById(R.id.vattypeslabel_price_text_view);
        TextView VatAMount= view.findViewById(R.id.vatamount_price_text_view);
        TextView TenderTextview= view.findViewById(R.id.total_tender_text_view);
        TextView AmountPerMethodLabel=view.findViewById(R.id.total_pain_bymethod_text_view);
        TextView AmountPerMethodAmount=view.findViewById(R.id.total_tender_by_method_text_view);
        TextView CashReturnAmount=view.findViewById(R.id.cash_return_amount_text_view);
        TextView OpeninghoursTextview=view.findViewById(R.id.openning_text_view);
        TextView TelTextview=view.findViewById(R.id.tel_text_view);
        TextView FaxTextview=view.findViewById(R.id.fax_text_view);
        TextView compTelTextview=view.findViewById(R.id.comptel_text_view);
        TextView compFaxTextview=view.findViewById(R.id.compfax_text_view);
        View ReturnCashline=view.findViewById(R.id.cash_return_line);
        LinearLayout CashReturnLayout=view.findViewById(R.id.cash_return_layout);

        ImageView logo= view.findViewById(R.id.logoImageView);
        // ... Initialize other TextViews


        // Initialize the database helper
        mDatabaseHelper = new DatabaseHelper(getContext());
        SharedPreferences sharedPreference = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierName = sharedPreference.getString("cashorName", null);
        cashorlevel = sharedPreference.getString("cashorlevel", null);
        ShopName = sharedPreference.getString("ShopName", null);
        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);




        // Retrieve the total amount and total tax amount from the transactionheader table
        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
        if (cursorCompany != null && cursorCompany.moveToFirst()) {
            int columnCompanyNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_COMPANY_NAME);
            int columnCompanyAddressIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_1);
            int columnCompanyAddress2Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_2);
            int columnCompanyAddress3Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_3);
            int columnShopNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNAME);
            int columnShopAddressIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_1);
            int columnShopAddress2Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_2);
            int columnShopAddress3Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_3);
            int columnCompanyVATIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_VAT_NO);
            int columnCompanyBRNIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_BRN_NO);
            int columnLogoPathIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Logo);
            int columnOpeningHoursIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Opening_Hours);
            int columnTelNumIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_TEL_NO);
            int columnFaxIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_FAX_NO);
            int columncomptelIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_TEL_NO);
            int columnCompFaxIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_FAX_NO);

            String companyName = cursorCompany.getString(columnCompanyNameIndex);
            String CompanyAdress1= cursorCompany.getString(columnCompanyAddressIndex);
            String CompanyAdress2= cursorCompany.getString(columnCompanyAddress2Index);
            String CompanyAdress3= cursorCompany.getString(columnCompanyAddress3Index);
            String shopName = cursorCompany.getString(columnShopNameIndex);
            String ShopAdress= cursorCompany.getString(columnShopAddressIndex);
            String ShopAdress2= cursorCompany.getString(columnShopAddress2Index);
            String ShopAdress3= cursorCompany.getString(columnShopAddress3Index);
            String CompanyVatNo= cursorCompany.getString(columnCompanyVATIndex);
            String CompanyBRNNo= cursorCompany.getString(columnCompanyBRNIndex);
            String OpeningHours= cursorCompany.getString(columnOpeningHoursIndex);
            String TelNum= cursorCompany.getString(columnTelNumIndex);
            String FaxNum= cursorCompany.getString(columnFaxIndex);
            String compTelNum= cursorCompany.getString(columncomptelIndex);
            String compFaxNum= cursorCompany.getString(columnCompFaxIndex);

            LogoPath = cursorCompany.getString(columnLogoPathIndex);
            // Load and display the image using Glide
            Glide.with(requireContext())
                    .load(LogoPath) // Replace with the appropriate image resource or URL
                    .into(logo);

            companyTextView.setText(companyName);
            compAdressTextView.setText(CompanyAdress1+","+ CompanyAdress2);
            compAdress3TextView.setText(CompanyAdress3 );

            ShopnameTextView.setText(shopName);
            shopAdress1n2TextView.setText(ShopAdress+","+ ShopAdress2);
            shopAdress3TextView.setText(ShopAdress3 );

            VATTextView.setText("VAT  :" + CompanyVatNo);
            BRNTextView.setText("BRN  :" + CompanyBRNNo);
            OpeninghoursTextview.setText("Opening Hours between: " + OpeningHours );
            TelTextview.setText("Tel: " + TelNum );
            FaxTextview.setText("Fax: " + FaxNum );
            compTelTextview.setText("Tel: " + compTelNum );
            compFaxTextview.setText("Fax: " + compFaxNum );
            cursorCompany.close();

        }


        // Retrieve the total amount and total tax amount from the transactionheader table
        Cursor cursor1 = mDatabaseHelper.getTransactionHeader(transactionId);

        if (cursor1 != null && cursor1.moveToFirst()) {
            int columnIndexTotalAmount = cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
            int columnIndexTotalTaxAmount = cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);
            int columnIndexTimeCreated = cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_TIME_CREATED);
            int columnIndexDateCreated = cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED);
            int CashiorIdindex = cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_CASHIER_CODE);
            int TotalTenderindex = cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_PAID);
            int CashReturnindex = cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_CASH_RETURN);
            int cashierId1 = cursor1.getInt(CashiorIdindex);


            Cursor itemCursor = mDatabaseHelper.getCashierByid(cashierId1);
            if (itemCursor != null && itemCursor.moveToFirst()) {
                int cashiernameindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_NAME);
                int cashieridindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id);

                String Cashiername = itemCursor.getString(cashiernameindex);
                String Cashierid = itemCursor.getString(cashieridindex);

                CashierName.setText("Cashier name : "+ Cashiername);
                CashierID.setText("Cashier ID : "+ Cashierid);
            }
            if (itemCursor != null) {
                itemCursor.close();
            }

            totalAmount = cursor1.getDouble(columnIndexTotalAmount);
            TaxtotalAmount = cursor1.getDouble(columnIndexTotalTaxAmount);
            DateCreated = cursor1.getString(columnIndexDateCreated);
            timeCreated = cursor1.getString(columnIndexTimeCreated);
            TenderAmount = cursor1.getString(TotalTenderindex);
            CashReturn=cursor1.getString(CashReturnindex);

            String formattedTotalAmount = String.format("%.2f", totalAmount);
            String formattedTotalTAXAmount = String.format("%.2f", TaxtotalAmount);

            String Total = getString(R.string.Total);
            String TVA = getString(R.string.Vat);

            String TotalValue = "Rs " + formattedTotalAmount;
            String TotalVAT = "Rs " + formattedTotalTAXAmount;
            totalPriceTextView.setText(TotalValue);
            VatAMount.setText(TotalVAT);
            TenderTextview.setText("Rs " + TenderAmount);

            if (CashReturn != null) {
                int cash = Integer.parseInt(CashReturn);

                if (cash > 0) {
                    CashReturnAmount.setText("Rs " + CashReturn);
                    ReturnCashline.setVisibility(View.VISIBLE);
                    CashReturnLayout.setVisibility(View.VISIBLE);
                } else {
                    ReturnCashline.setVisibility(View.GONE);
                    CashReturnLayout.setVisibility(View.GONE);
                }
            }else{
                ReturnCashline.setVisibility(View.GONE);
                CashReturnLayout.setVisibility(View.GONE);
            }
            cursor1.close();
        }else {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
        // Fetch the transaction details from the database based on the transaction ID
        Cursor cursor = mDatabaseHelper.getTransactionDetails(String.valueOf(transactionId));

        // Check if the cursor contains valid data
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the transaction details from the cursor
            String transactionDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));
            double totalPrice = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));
            String POSNo= cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TERMINAL_NO));

            // ... Retrieve other transaction details


            transactionIdTextView.setText(String.valueOf(transactionId));
            transactionDateTextView.setText(transactionDate);

            PosNo.setText("POS Number: " + POSNo);
// Close the cursor
            cursor.close();

            Cursor vatCursor = mDatabaseHelper.getDistinctVATTypes(transactionId);
            if (vatCursor != null && vatCursor.moveToFirst()) {
                StringBuilder vatTypesBuilder = new StringBuilder();
                do {
                    int columnIndexVATType = vatCursor.getColumnIndex(DatabaseHelper.VAT_Type);
                    String vatType = vatCursor.getString(columnIndexVATType);
                    vatTypesBuilder.append(vatType).append(", ");
                } while (vatCursor.moveToNext());
                vatCursor.close();

                String vatTypes = vatTypesBuilder.toString().trim();
                if (vatTypes.endsWith(",")) {
                    vatTypes = vatTypes.substring(0, vatTypes.length() - 1);
                }
                VatTypes.setText(vatTypes);

            }

            Cursor TenderCursor = mDatabaseHelper.getPaymentmethodamounts(transactionId);
            StringBuilder labelBuilder = new StringBuilder();
            StringBuilder amountBuilder = new StringBuilder();
            String Totalpaid="",TotalAmountPaid="";
            if (TenderCursor != null && TenderCursor.moveToFirst()) {
                do {
                    int columnIndexPaymentName = TenderCursor.getColumnIndex(DatabaseHelper.SETTLEMENT_PAYMENT_NAME);
                    int columnIndexPaymentamount = TenderCursor.getColumnIndex(DatabaseHelper.SETTLEMENT_AMOUNT);
                    String PaymentName = TenderCursor.getString(columnIndexPaymentName);
                    String PaymentAmount = TenderCursor.getString(columnIndexPaymentamount);
                    Totalpaid= "Amount Paid in " + PaymentName;
                    TotalAmountPaid= "Rs " + PaymentAmount;
// Append the label and amount to their respective builders
                    labelBuilder.append(Totalpaid).append("\n");
                    amountBuilder.append(TotalAmountPaid).append("\n");

                } while (TenderCursor.moveToNext());

                TenderCursor.close();
            }

// Set the StringBuilder content to the TextViews

            AmountPerMethodLabel.setText(labelBuilder.toString());
            AmountPerMethodAmount.setText(amountBuilder.toString());


        } else {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor2 = mDatabaseHelper.getAllTransactions(transactionId);
        mAdapter = new CompletedTransactionsAdapter(getContext(), cursor2);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    // set text
    public void setText(String item) {
        TextView view = getView().findViewById(R.id.detailsText);
        view.setText(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        doubleBackToExitPressedOnce = false;
    }

    // Handle back button press
    public static ReceiptBodyFragment newInstance(String id) {
        ReceiptBodyFragment fragment = new ReceiptBodyFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

}