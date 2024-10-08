package com.accessa.ibora.product.items;




import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.accessa.ibora.Buyer.Buyer;
import com.accessa.ibora.Constants;
import com.accessa.ibora.ItemsReport.DataModel;
import com.accessa.ibora.ItemsReport.CatDataModel;
import com.accessa.ibora.ItemsReport.ItemSummary;
import com.accessa.ibora.ItemsReport.PaymentMethodDataModel;
import com.accessa.ibora.Report.PaymentItem;
import com.accessa.ibora.Settings.Rooms.Rooms;
import com.accessa.ibora.product.Rooms.Table;
import com.accessa.ibora.product.couponcode.Coupon;
import com.accessa.ibora.sales.ticket.Checkout.PaymentDetails;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;

import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String CAT_TABLE_NAME = "Category";
    public static final String PREFERENCE_NAME = "YourPreferences";
    public static final String STATUS_KEY = "status";
    public static String FINANCIAL_COLUMN_TransId="Transid";
    public static String FINANCIAL_CashReturn="CashReturn";
    public static String RELATED_Option_ID="RelatedOptionId";
    public static final String CatName = "CatName";
    public static final String Color = "Color";
    // Table Names
    public static final String TABLE_NAME = "Items";
    public static final String TAX_TABLE_NAME = "Tax";
    public static final String VENDOR_TABLE_NAME = "Vendor";
    public static final String COST_TABLE_NAME = "Cost";
    public static final String TRANSACTION_TABLE_NAME = "Transactions";
    public static final String TRANSACTION_HEADER_TABLE_NAME = "TransactionHeader";
    public static final String TRANSACTION_STATUS_IN_PROGRESS = "InProgress";
    public static final String TRANSACTION_UNIT_PRICE = "UnitPrice";
    public static final String IS_SELECTED = "selected";
    public static final String IS_PAID = "Paidstatus";
    public static final String ORDERTYPE ="OrderType" ;
    public static final String NUMBER_OF_COVERS ="AmountCovers" ;

    public static final String INVOICE_SETTLEMENT_TABLE_NAME = "InvoiceSettlement";

    // Common column names
    public static final String _ID = "_id";
    public static String hasoptions ="hasoptions";
    public static String comment= "comment";
    public static String related_item="related_item";
    public static String related_item2= "related_item2";
    public static String related_item3= "related_item3";
    public static String related_item4= "related_item4";
    public static String related_item5= "related_item5";
    public static String Related_ITEM_ID="RelatedItemId";
    // Items table columns
    public static final String Name = "name";
    public static final String Category = "category";
    public static final String DESC = "description";
    public static final String Price = "price";
    public static final String RateDiscount="DiscountRate";
    public static final String AmountDiscount="DiscountAmount";
    public static final String Price2="price2";
    public static final String Price3="price3";
    public static final String Department = "Department";
    public static final String SubDepartment = "SubDepartment";
    public static final String Barcode = "Barcode";
    public static final String ShopNum="ShopNum";
    public static final String TillNum="TillNum";
    public static final String VAT = "VAT";
    public static final String LongDescription = "LongDescription";
    public static final String Quantity = "Quantity";
    public static final String ExpiryDate = "ExpiryDate";
    public static final String AvailableForSale = "AvailableForSale";
    public static final String SoldBy = "SoldBy";
    public static final String Image = "Image";
    public static final String SKU = "SKU";
    public static final String Cost = "Cost";
    public static final String Variant = "Variant";
    public static final String Weight = "Weight";
    public static final String UserId = "UserId";
    public static final String FINANCIAL_TABLE_NAME = "FinancialReportTable";
    public static String DateCreated ="DateCreated";
    public static final String LastModified = "LastModified";

    public static final String VARIANTS_TABLE_NAME = "Options";
    public static final String VARIANT_ID = "id";
    public static final String VARIANT_BARCODE = "barcode";
    public static final String VARIANT_DESC = "Desc";
    public static final String VARIANT_PRICE = "Price";
    public static final String VARIANT_ITEM_ID = "variantItemId";

    // Vendor table columns
    public static final String VendorID = "ID";
    public static final String CodeFournisseur = "CodeFournisseur";
    public static final String NomFournisseur = "NomFournisseur";
    public static final String PhoneNumber = "PhoneNumber";
    public static final String Street = "Street";
    public static final String Town = "Town";
    public static final String PostalCode = "PostalCode";
    public static final String Email = "Email";
    public static final String InternalCode = "InternalCode";
    public static final String Salesmen = "Salesmen";

    // Cost table columns
    public static final String CostID = "ID";
    public static final String SKUCost = "SKU";
    //UsersTable
    public static final String TABLE_NAME_Users = "Users";

    // Column names

    public static final String COLUMN_CASHOR_id = "cashorid";
    public static final String COLUMN_CASHOR_Shop ="ShopName" ;
    public static final String TRANSACTION_STATUS_COMPLETED = "Completed";
    public static final String TRANSACTION_STATUS_TRN = "TRN";
    public static final String TRANSACTION_STATUS_INPROGRESS = "InProgress";

    public static final String TRANSACTION_COPY_PRINTED = "NumberPrinter";
    public static final String RELATED_TRANSACTION_ID ="RelatedTransactionId" ;
    public static final String COLUMN_PIN = "pin";
    public static final String COLUMN_CASHOR_LEVEL = "cashorlevel";
     public static final String COLUMN_CASHOR_NAME = "cashorname";
    public static String COLUMN_CASHOR_ShopNum="shopnum";
    public static final String COLUMN_CASHOR_DEPARTMENT = "cashorDepartment";
    // Database Information
    private static final String DB_NAME = Constants.DB_NAME;
    public static final int DB_VERSION = 1;

    // Department table columns
    public static final String DEPARTMENT_TABLE_NAME = "Department";
    public static final String DEPARTMENT_ID = "_id";
    public static  final String  DEPARTMENT_DATE_CREATED= "DateCreated";
    public static final String DEPARTMENT_CODE = "DepartmentCode";
    public static final String DEPARTMENT_NAME = "DepartmentName";
    public static final String DEPARTMENT_LAST_MODIFIED = "LastModified";
    public static final String DEPARTMENT_CASHIER_ID = "CashierID";

    // Subdepartment table columns
    public static final String SUBDEPARTMENT_TABLE_NAME = "SubDepartment";
    public static final String SUBDEPARTMENT_ID = "_id";
    public static final String SUBDEPARTMENT_NAME = "SubDepartmentName";
    public static final String SUBDEPARTMENT_DEPARTMENT_ID = "DepartmentID";
    // Discount table columns
    public static final String DISCOUNT_TABLE_NAME = "Discount";
    public static final String DISCOUNT_ID = "_id";
    public static final String DISCOUNT_NAME = "Name";
    public static final String DISCOUNT_VALUE = "DiscountValue";
    public static final String DISCOUNT_TIMESTAMP = "Timestamp";
    public static final String DISCOUNT_USER_ID = "UserID";


    // Transaction table columns
    public static String TRANSACTION_STATUS_Saved = "Saved1";
    public static final String TRANSACTION_ID = "TranscationId";
    public static final String ITEM_ID = "ItemId";
    public static final String TRANSACTION_DATE = "TransactionDate";

    public static final String TRANSACTION_STATUS = "TransactionStatus";

    public static final String QUANTITY = "Quantity";
    public static final String TOTAL_PRICE = "TotalPrice";

    public static final String TRANSACTION_SHOP_NO = "ShopNo";
    public static final String TRANSACTION_TERMINAL_NO = "TerminalNo";
    public static final String TRANSACTION_DATE_CREATED = "DateCreated";
    public static final String TRANSACTION_DATE_MODIFIED = "DateModified";
    public static final String TRANSACTION_TIME_CREATED = "TimeCreated";
    public static final String TRANSACTION_TIME_MODIFIED = "TimeModified";
    public static final String TRANSACTION_CODE = "Code";
    public static final String TRANSACTION_DESCRIPTION = "Description";
    public static final String TRANSACTION_MRA_Invoice_Counter="Invoice_counter";
    public static final String TRANSACTION_QUANTITY = "Qte";
    public static final String TRANSACTION_DISCOUNT = "Discount";
    public static final String TRANSACTION_VAT_BEFORE_DISC = "VAT_Before_Disc";
    public static final String TRANSACTION_VAT_AFTER_DISC = "VAT_After_Disc";
    public static final String TRANSACTION_TOTAL_HT_A ="TOTALHT_A" ;
    public static final String TRANSACTION_TOTAL_TTC =  "TotalTTC";

    public static final String TRANSACTION_IS_TAXABLE = "IsTaxable";
    public static final String TRANSACTION_DATE_TRANSACTION = "DateTransaction";
    public static final String TRANSACTION_TIME_TRANSACTION = "TimeTransaction";
    public static final String TRANSACTION_BARCODE = "Barcode";
    public static String SyncStatus="SyncStatus";
    public static String Nature="Nature";
    public static String TaxCode="TaxCode";
    public static String Currency="Currency";
    public static String ItemCode="ItemCode";
    public static String PriceAfterDiscount="CurrentPrice";
    public static String Price2AfterDiscount="CurrentPrice2";
    public static String Price3AfterDiscount="CurrentPrice3";
    public static String TotalDiscount="TotalDiscount";
    public static String TotalDiscount2="TotalDiscount2";
    public static String TotalDiscount3="TotalDiscount3";
    public static final String TRANSACTION_WEIGHTS = "Weights";
    public static final String TRANSACTION_TOTAL_HT_B = "TotalHT_B";
    public static final String TRANSACTION_TYPE_TAX = "TYPETAX";
    private static final String TRANSACTION_RAYON = "Rayon";
    public static final String TRANSACTION_FAMILLE = "Famille";
    public static final String TRANSACTION_ID_SALES_D = "IDSalesD";
    public static final String TRANSACTION_TOTALIZER = "Totalizer";

    // Header fields

    private static final String TRANSACTION_MEMBER_CARD = "MemberCard";
    private static final String TRANSACTION_SUB_TOTAL = "SubTotal";
    public static final String TRANSACTION_CASHIER_CODE = "CashierCode";
    public static final String TRANSACTION_SHIFT_NUMBER ="ShiftNumber" ;
   public static final String TRANSACTION_TOTAL_PAID="TenderAmount";
    public static final String TRANSACTION_STATUS_DETAILS ="SavedStatusDetails";
    public static final String TRANSACTION_CASH_RETURN ="CashReturn";

    public static final String TRANSACTION_TOTAL_TX_1 = "Total_Tx_1";
    private static final String TRANSACTION_TOTAL_TX_2 = "Total_Tx_2";
    private static final String TRANSACTION_TOTAL_TX_3 = "Total_Tx_3";
    public static final String TRANSACTION_NATURE ="Nature" ;
    public static final String TRANSACTION_TAX_CODE ="TaxCode" ;
    public static final String TRANSACTION_CURRENCY ="Currency" ;
    public static final String TRANSACTION_ITEM_CODE = "ItemCode";
    public static final String TRANSACTION_TOTAL_DISCOUNT = "TotalDisc";
    public static final String TRANSACTION_MRA_QR = "MRA_Response";
    public static final String TRANSACTION_MRA_IRN = "MRA_IRN";
    public static final String TRANSACTION_MRA_Method = "MRA_Method";
    public static final String TRANSACTION_ITEM_QUANTITY = "QtyItem";
    public static final String TRANSACTION_SPLIT_TYPE ="SplitType" ;

    public static final String TRANSACTION_CLIENT_NAME = "ClientName";
    public static final String TRANSACTION_CLIENT_OTHER_NAME = "ClientOtherName";
    public static final String TRANSACTION_CLIENT_Company_NAME = "ClientCompanyName";
    public static final String TRANSACTION_CLIENT_ADR1 = "ClientAdr1";
    public static final String TRANSACTION_CLIENT_ADR2 = "ClientAdr2";
    public static final String TRANSACTION_CLIENT_VAT_REG_NO = "ClientVATRegNo";
    public static final String TRANSACTION_CLIENT_BRN = "ClientBRN";
    private static final String TRANSACTION_CLIENT_TEL = "ClientTel";
    public static final String TRANSACTION_INVOICE_REF = "InvoiceRef";
    private static final String TRANSACTION_IS_CASH_CREDIT = "IsCash_Credit";
    private static final String TRANSACTION_ID_SALESH = "IDSalesH";
    private static final String TRANSACTION_CLIENT_CODE = "ClientCode";
    private static final String TRANSACTION_LOYALTY = "Loyalty";
    public static final String TRANSACTION_TICKET_NO = "TranscationId";
    public static final String TRANSACTION_PREVIOUS_HASH ="PreviousHash" ;


    // Invoice Settlement table columns
    public static final String SETTLEMENT_ID = "SettlementId";
    public static final String SETTLEMENT_SHOP_NO = "ShopNo";
    public static final String SETTLEMENT_TERMINAL_NO = "TerminalNo";
    public static final String SETTLEMENT_DATE_TRANSACTION = "DateTransaction";
    public static String SETTLEMENT_Time_TRANSACTION="TimeTransaction";
    public static String SETTLEMENT_DATE_Modified="Date_Modified";
    public static final String SETTLEMENT_PAYMENT_CODE = "CodeModeDePaiement";
    public static final String SETTLEMENT_AMOUNT = "Amount";
    public static final String SETTLEMENT_TOTAL_AMOUNT = "TotalAmount";
    public static final String SETTLEMENT_GIFT_VOUCHER_NO = "GiftVoucherNo";
    public static final String SETTLEMENT_INVOICE_ID = "IDInvoiceSettlement";
    public static final String SETTLEMENT_REMARK = "Remark";
    public static final String SETTLEMENT_DATE_CREATED = "DateCreated";
    public static final String SETTLEMENT_Time_CREATED = "TimeCreated";
    public static final String SETTLEMENT_PAYMENT_NAME = "PaymentName";
    public static final String SETTLEMENT_CHEQUE_NO = "ChequeNo";

    // Company tabel

    public static final String TABLE_NAME_STD_ACCESS = "std_access";
    private static final String COLUMN_STD_ACCESS_ID = "std_access_id";
    public static final String COLUMN_SHOPNAME = "ShopName";
    public static final String COLUMN_VAT_NO = "vat_no";
    public static final String COLUMN_BRN_NO = "brn_no";
    public static final String COLUMN_ADR_1 = "adr_1";
    public static final String COLUMN_ADR_2 = "adr_2";
    public static final String COLUMN_ADR_3 = "adr_3";
    public static final String COLUMN_TEL_NO = "tel_no";
    public static final String COLUMN_FAX_NO = "fax_no";
    public static String COLUMN_Comp_ADR_1= "ComPanyAdress1";
    public static String COLUMN_Comp_ADR_2 ="ComPanyAdress2";
    public static final String COLUMN_SHOPNUMBER ="ShopNumber" ;
    public static String COLUMN_Comp_ADR_3= "ComPanyAdress3";
    public static String COLUMN_Comp_TEL_NO= "ComPanyphoneNumber";
    public static String COLUMN_Comp_FAX_NO= "ComPanyFaxNumber";

    public static String COLUMN_POS_Num="pos_num";
    public static String VAT_Type= "VatType";
    public static String COLUMN_TerminalNo="TerminalNumber";

    public static final String COLUMN_Logo = "Logo";
    public static final String COLUMN_COMPANY_NAME = "company_name";

    public static String COLUMN_Opening_Hours ="OpenningHours";

    // payment by qr

    public static final String TABLE_NAME_PAYMENTBYQY= "PaymentByQr";
    public static final String COLUMN_PAYMENT_ID= "PaymentMethodID";

    public static final String COLUMN_PAYMENT_METHOD= "PaymentMethod";

    public static final String COLUMN_QR_CODE_NUM= "QRString";

    // payment methods
    public static final String PAYMENT_METHOD_COLUMN_ID = "PaymentMethodId";
    public static final String PAYMENT_METHOD_COLUMN_NAME = "PaymentMethodName";
    public static final String PAYMENT_METHOD_COLUMN_ICON = "PaymentMethodIcon";
    public static final String PAYMENT_METHOD_COLUMN_DATE_CREATED = "PaymentMethodDateCreated";
    public static final String PAYMENT_METHOD_COLUMN_LAST_MODIFIED = "PaymentMethodLastModified";
    public static final String PAYMENT_METHOD_COLUMN_CASHOR_ID = "PaymentMethodCashorId";
    public static final String PAYMENT_METHOD_TABLE_NAME= "PaymentMethodTable";
    public static String OpenDrawer = "OpenDrawer";
    // buyer table

    public static final String BUYER_TABLE_NAME ="Buyer_Table" ;
    public static final String BUYER_ID ="Buyer_Id" ;
    public static final String BUYER_NAME = "Buyer_Name";
    public static final String BUYER_TAN = "Buyer_TAN";
    public static final String BUYER_BUSINESS_ADDR  ="Adresse";
    public static final String BUYER_DATE_CREATED ="DateCreated" ;
    public static final String BUYER_LAST_MODIFIED ="LastModified" ;
    public static final String BUYER_BRN ="Buyer_BRN" ;
    public static final String BUYER_TYPE ="Buyer_Type" ;
    public static final String BUYER_NIC ="Buyer_NIC" ;
    public static final String BUYER_PriceLevel="PriceLevel";
    public static String BUYER_Company_name="companyName";
    public static String BUYER_Profile="Buyer_Profile";
    public static String BUYER_Other_NAME="BuyerOtherName";


// POS Table

    public static final String TABLE_NAME_POS_ACCESS = "POSTable";
    private static final String COLUMN_POS_ID = "id";

    // Define the coupon code table name and columns
    public static final String COUPON_TABLE_NAME = "CouponTable";
    public static final String COUPON_ID = "id";
    public static final String COUPON_CODE = "code";
    public static final String COUPON_STATUS = "status";
    public static final String COUPON_START_DATE = "start_date";
    public static final String COUPON_END_DATE = "end_date";
    public static final String COUPON_CASHIER_ID = "cashier_id";
    public static final String COUPON_DATE_CREATED = "date_created";
    public static final String COUPON_TIME_CREATED = "time_created";
    public static final String COUPON_DISCOUNT = "discount";
    public static final String TRANSACTION_CLIENT_NIC = "ClientNIC";

    public static String hasSupplements="HasSupplements";
    public static String relatedSupplements="RelatedSupplements";
        //financial report table
    public static final String FINANCIAL_COLUMN_ID = "id";
    public static final String FINANCIAL_COLUMN_DATETIME = "date";
    public static final String FINANCIAL_COLUMN_CASHOR_ID = "cashiorid";
    public static final String FINANCIAL_COLUMN_TRANSACTION_CODE = "TransactionType";
    public static final String FINANCIAL_COLUMN_QUANTITY = "quantity";
    public static final String FINANCIAL_COLUMN_TOTAL ="total" ;
    public static final String FINANCIAL_COLUMN_TOTALIZER = "Totalizer";
    public static final String FINANCIAL_COLUMN_POSNUM = "TillNum";
    public static final String FINANCIAL_COLUMN_SHOP_NUMBER =" ShopNumber" ;
    public static final String FINANCIAL_COLUMN_CURRENT_TIME ="time" ;
    public static final String FINANCIAL_COLUMN_PAYMENT ="Payment" ;


    //default qr

    private static final String DEFAULT_PAYMENT_METHOD = "POP";
    private static final String DEFAULT_CatName = "Supplements";
    private static final String DEFAULT_MenuRepas = "Menu Repas";
    private static final String DEFAULT_Color = "#B7FFD7";
    private static final String DEFAULT_MenuColor = "#B9FFD7";
    private static final String DEFAULT_CASHOR_ID = "0";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    public static String getCurrentDateTime() {


        Date currentDate = new Date();
        return dateFormatter.format(currentDate);
    }
    private static final String DEFAULT_DATE_CREATED = getCurrentDateTime();;
    private static final String DEFAULT_LAST_MODIFIED = getCurrentDateTime();;
    private static final String DEFAULT_QR_CODE_NUM = "QRCodeNum";

    public static final String MERGED_SET_ID ="Merged_Set_ID" ;
    public static final String COUNTING_REPORT_TABLE_NAME = "CountReport";
    public static final String COUNTING_REPORT_ID = "id";
    public static final String COUNTING_REPORT_TOTALIZER_TOTAL = "totalizer_total";
    public static final String COUNTING_REPORT_TOTAL_VALUE = "total_value";
    public static final String COUNTING_REPORT_DIFFERENCE = "difference";
    public static final String COUNTING_REPORT_CASHIER_ID = "cashier_id";
    public static final String COUNTING_REPORT_DATETIME = "datetime";

    public static final String CASH_REPORT_TABLE_NAME = "CashReports";
    public static final String ROOMS = "rooms";
    public static final String TABLES = "tables";
    public static final String ID = "id";
    public static final String ROOM_NAME = "room_name";
    public static final String TABLE_COUNT = "table_count";
    public static final String TRANSACTION_COMMENT = "Comment";
    public static String TRANSACTION_SentToKitchen="SentToKitchen";

    public static final String TABLE_ID = "id";
    public static final String MERGED = "Merged";
    public static final String ROOM_ID = "room_id";
    public static final String TABLE_NUMBER = "table_number";
    public static final String SEAT_COUNT = "seat_count";
    public static final String WAITER_NAME = "waiter_name";
    public static final String STATUS  = "STATUS";

// Table for storing item variants

    public static final String OPTIONS_TABLE_NAME = "optionTable";
    public static String Variant_OPTION_ID="Option_id";
    public static final String OPTION_ID = "id" ;

    public static final String OPTION_NAME = "optionName";
    public static final String VARIANTS_OPTIONS_ID = "VariantOptionId";

    public static final String SUPPLEMENTS_OPTIONS_TABLE_NAME = "SupplementTableName";
    public static final String SUPPLEMENTS_TABLE_NAME = "SupplementTable";
    public static final String SUPPLEMENT_ID ="SupplementId" ;
    public static final String SUPPLEMENT_NAME = "RelatedSupplementID";
    public static final String SUPPLEMENT_DESCRIPTION = "SupplementDescriptions" ;
    public static final String SUPPLEMENT_PRICE = "SupplementPrice";
    public static final String SUPPLEMENT_OPTION_ID = "SupplementOptionId";
    public static final String SUPPLEMENT_OPTION_NAME = "SupplementOptionName";
    public static final String CAT_PRINTER_OPTION = "Printer_Option";
    private static final String CREATE_CAT_TABLE = "CREATE TABLE IF NOT EXISTS " + CAT_TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CatName + " TEXT UNIQUE NOT NULL, "
            + CAT_PRINTER_OPTION + " TEXT NOT NULL DEFAULT 0, " // Default value is 0 (false)
            + Color + " TEXT NOT NULL);";


    // Room table
    private static final String CREATE_ROOM_TABLE =
            "CREATE TABLE " + ROOMS + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ROOM_NAME + " TEXT, " +
                    TABLE_COUNT + " INTEGER);";

    // Table table
    private static final String CREATE_TABLE_TABLE =
            "CREATE TABLE " + TABLES + " (" +
                    TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ROOM_ID + " INTEGER, " +
                    TABLE_NUMBER + " INTEGER, " +
                    SEAT_COUNT + " INTEGER, " +
                    WAITER_NAME + " TEXT, " +
                    STATUS + " TEXT DEFAULT 'not_reserved' CHECK (" + STATUS + " IN ('reserved', 'not_reserved')), " +
                    MERGED + " INTEGER DEFAULT 0, " +
                    MERGED_SET_ID + " INTEGER DEFAULT 0, " +

                    "FOREIGN KEY (" + ROOM_ID + ") REFERENCES " + ROOMS + "(" + ROOM_ID + "));";

    // Creating Department table query
    private static final String CREATE_DEPARTMENT_TABLE = "CREATE TABLE " + DEPARTMENT_TABLE_NAME + "(" +
            DEPARTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DEPARTMENT_CODE + " TEXT UNIQUE NOT NULL, " +
            DEPARTMENT_NAME + " TEXT NOT NULL, " +
            DEPARTMENT_DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            DEPARTMENT_LAST_MODIFIED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_CASHOR_id + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_CASHOR_id + ") REFERENCES " +
            TABLE_NAME + "(" + COLUMN_CASHOR_id + "));";


    // Creating Subdepartment table query
    private static final String CREATE_SUBDEPARTMENT_TABLE = "CREATE TABLE " + SUBDEPARTMENT_TABLE_NAME + "(" +
            SUBDEPARTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SUBDEPARTMENT_NAME + " TEXT NOT NULL, " +
            SUBDEPARTMENT_DEPARTMENT_ID + " INTEGER NOT NULL, " +
            DEPARTMENT_CODE + " INTEGER NOT NULL, " +
            LastModified + " DATETIME NOT NULL, " +
            DEPARTMENT_CASHIER_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + SUBDEPARTMENT_DEPARTMENT_ID + ") REFERENCES " +
            DEPARTMENT_TABLE_NAME + "(" + DEPARTMENT_ID + "), " +
            "FOREIGN KEY (" + DEPARTMENT_CODE + ") REFERENCES " +
            DEPARTMENT_TABLE_NAME + "(" + DEPARTMENT_CODE + "), " +
            "FOREIGN KEY (" + DEPARTMENT_CASHIER_ID + ") REFERENCES " +
            TABLE_NAME_Users + "(" + COLUMN_CASHOR_id + "));";



    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Barcode + " TEXT(13) UNIQUE NOT NULL, "
            + Related_ITEM_ID + " TEXT  NULL, "
            + Name + " TEXT(26) NOT NULL CHECK(Name GLOB '[a-zA-Z0-9 ]*'), "
            + DESC + " TEXT NOT NULL, "
            + Category + " TEXT NOT NULL, "
            + Quantity + " INTEGER, "
            + Department + " TEXT NOT NULL, "
            + LongDescription + " TEXT NOT NULL, "
            + SubDepartment + " TEXT NOT NULL, "
            + Price + " DECIMAL(10, 2) NOT NULL, "
            + Price2 + " DECIMAL(10, 2), "
            + Price3 + " DECIMAL(10, 2), "
            + PriceAfterDiscount + " DECIMAL(10, 2) NOT NULL, "
            + Price2AfterDiscount + " DECIMAL(10, 2) NOT NULL, "
            + Price3AfterDiscount + " DECIMAL(10, 2) NOT NULL, "
            + VAT + " TEXT NOT NULL CHECK(VAT IN ('VAT 0%', 'VAT Exempted', 'VAT 15%')), "
            + ExpiryDate + " DATE, "
            + AvailableForSale + " BOOLEAN NOT NULL DEFAULT 1, "
            + SoldBy + " TEXT NOT NULL CHECK(SoldBy IN ('Each', 'Volume')), "
            + Image + " TEXT, "
            + SKU + " TEXT  , "
            + Variant + " TEXT NOT NULL, "
            + Cost + " DECIMAL(10, 2), "
            + Weight + " DECIMAL(10, 2), "
            + UserId + " INTEGER NOT NULL, "
            + Nature + " TEXT, "
            + TaxCode + " TEXT, "
            + Currency + " TEXT, "
            + ItemCode + " TEXT, "
            + SyncStatus +  " TEXT NOT NULL CHECK(SyncStatus IN ('Offline', 'Online')), "
            + RateDiscount + " TEXT, "
            + AmountDiscount + " TEXT, "
            + TotalDiscount + " DECIMAL(10, 2), "
            + TotalDiscount2 + " DECIMAL(10, 2), "
            + TotalDiscount3 + " DECIMAL(10, 2), "
            + DateCreated + " DATETIME NOT NULL, "
            + LastModified + " DATETIME NOT NULL, "
            + hasoptions + " BOOLEAN NOT NULL DEFAULT 0, "
            + comment + " TEXT, "
            + related_item + " TEXT, "
            + related_item2 + " TEXT, "
            + related_item3 + " TEXT, "
            + related_item4 + " TEXT, "
            + related_item5 + " TEXT, "
            + hasSupplements + " BOOLEAN NOT NULL DEFAULT 0, "
            + ShopNum + " INTEGER , "
            + TillNum + " INTEGER , "
            + relatedSupplements + " TEXT, "
            + "FOREIGN KEY (" + SKU + ", " + Cost + ") REFERENCES "
            + COST_TABLE_NAME + "(" + SKUCost + ", " + Cost + "), "
            + "FOREIGN KEY (" + UserId + ") REFERENCES "
            + TABLE_NAME_Users + "(" + COLUMN_CASHOR_id + "), "
            + "FOREIGN KEY (" + Barcode + ") REFERENCES "
            + COST_TABLE_NAME + "(" + Barcode + "));";



    private static final String CREATE_OPTIONS_TABLE = "CREATE TABLE " + OPTIONS_TABLE_NAME + " ("
            + OPTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + OPTION_NAME + " TEXT NOT NULL);";



    private static final String CREATE_VARIANTS_TABLE = "CREATE TABLE " + VARIANTS_TABLE_NAME + " ("
            + VARIANTS_OPTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VARIANT_ID + " INTEGER NOT NULL, "
            + VARIANT_ITEM_ID + " INTEGER NOT NULL, "
            + VARIANT_BARCODE + " TEXT(20) UNIQUE NOT NULL, "
            + VARIANT_DESC + " TEXT NOT NULL, "
            + VARIANT_PRICE + " DECIMAL(10, 2) NOT NULL, "
            + "FOREIGN KEY (" + VARIANT_ID + ") REFERENCES " + VARIANTS_TABLE_NAME + "(" + VARIANT_ID + "), "
            + "FOREIGN KEY (" + OPTION_ID + ") REFERENCES " + OPTIONS_TABLE_NAME + "(" + OPTION_ID + "));";


    // SQL statement to create the options table for supplements
    private static final String CREATE_SUPPLEMENTS_OPTIONS_TABLE = "CREATE TABLE " + SUPPLEMENTS_OPTIONS_TABLE_NAME + " ("
            + SUPPLEMENT_OPTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SUPPLEMENT_OPTION_NAME + " TEXT NOT NULL);";



    // SQL statement to create the supplements table
    private static final String CREATE_SUPPLEMENTS_TABLE = "CREATE TABLE " + SUPPLEMENTS_TABLE_NAME + " ("
            + SUPPLEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SUPPLEMENT_NAME + " INTEGER NOT NULL, "
            + SUPPLEMENT_DESCRIPTION + " TEXT NOT NULL, "
            + SUPPLEMENT_PRICE + " DECIMAL(10, 2) NOT NULL, "
            + VARIANT_BARCODE + " TEXT(20) UNIQUE NOT NULL, "
            + SUPPLEMENT_OPTION_ID + " INTEGER, " // Define the SUPPLEMENT_OPTION_ID column
            + "FOREIGN KEY (" + SUPPLEMENT_OPTION_ID + ") REFERENCES " + SUPPLEMENTS_OPTIONS_TABLE_NAME + "(" + SUPPLEMENT_OPTION_ID + "));";


    // Creating PaymentByQy table query
    private static final String CREATE_PAYMENTBYQY_TABLE = "CREATE TABLE " + TABLE_NAME_PAYMENTBYQY + "(" +
            COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PAYMENT_METHOD + " TEXT NOT NULL, " +
            COLUMN_CASHOR_id + " TEXT NOT NULL, " +
            DateCreated + " TEXT NOT NULL, " +
            LastModified + " TEXT NOT NULL, " +
            COLUMN_QR_CODE_NUM + " TEXT NOT NULL);";

    // Creating Users table query
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Users + " ("
            + COLUMN_CASHOR_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PIN + " TEXT, "
            + COLUMN_CASHOR_LEVEL + " INTEGER, "
            + COLUMN_CASHOR_NAME + " TEXT, "
            + COLUMN_CASHOR_Shop + " TEXT, "
            + COLUMN_CASHOR_DEPARTMENT + " TEXT,"
            + DateCreated + " DATETIME NOT NULL,"
            + LastModified + " DATETIME NOT NULL) ";


    // Creating Vendor table query
    private static final String CREATE_VENDOR_TABLE = "CREATE TABLE " + VENDOR_TABLE_NAME + "(" +
            VendorID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LastModified + " DATETIME NOT NULL, " +
            UserId + " INTEGER NOT NULL, " +
            CodeFournisseur + " TEXT NOT NULL, " +
            NomFournisseur + " TEXT NOT NULL, " +
            PhoneNumber + " TEXT, " +
            Street + " TEXT, " +
            Town + " TEXT, " +
            PostalCode + " TEXT, " +
            Email + " TEXT, " +
            InternalCode + " TEXT, " +
            Salesmen + " TEXT);";

    // Creating Cost table query
    private static final String CREATE_COST_TABLE = "CREATE TABLE " + COST_TABLE_NAME + "(" +
            CostID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Barcode + " TEXT(20) NOT NULL, " +
            SKUCost + " DECIMAL(10, 2) NOT NULL, " +
            Cost + " DECIMAL(10, 2) NOT NULL, " +
            LastModified + " DATETIME NOT NULL, " +
            UserId + " INTEGER NOT NULL, " +
            CodeFournisseur + " TEXT , " +
            "FOREIGN KEY (" + Barcode + ", " + SKUCost + ") REFERENCES " +
            TABLE_NAME + "(" + Barcode + ", " + SKUCost + "), " +
            "FOREIGN KEY (" + CodeFournisseur + ") REFERENCES " +
            VENDOR_TABLE_NAME + "(" + CodeFournisseur + "));";


    // Creating Discount table query
    private static final String CREATE_DISCOUNT_TABLE = "CREATE TABLE " + DISCOUNT_TABLE_NAME + "(" +
            DISCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DISCOUNT_NAME + " TEXT NOT NULL, " +
            DISCOUNT_VALUE + " DECIMAL(10, 2) NOT NULL, " +
            DISCOUNT_TIMESTAMP + " DATETIME NOT NULL, " +
            DISCOUNT_USER_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + DISCOUNT_USER_ID + ") REFERENCES " +
            TABLE_NAME_Users + "(" + COLUMN_CASHOR_id + "));";



    // Creating Transaction table query
    private static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TRANSACTION_TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ROOM_ID + " INTEGER, " +
            TABLE_ID + " INTEGER, " +
            IS_SELECTED + " INTEGER DEFAULT 0, " + // 0 for not selected, 1 for selected
            IS_PAID + " INTEGER DEFAULT 0, " +
            TRANSACTION_ID + " INTEGER NOT NULL, " +
            RELATED_Option_ID  + " TEXT, " +
            ITEM_ID + " INTEGER NOT NULL, " +
            TRANSACTION_DATE + " DATETIME NOT NULL, " +
            QUANTITY + " INTEGER NOT NULL, " +
            TOTAL_PRICE + " DECIMAL(10, 2) NOT NULL, " +
            VAT + " DECIMAL(10, 2) NOT NULL, " +
            VAT_Type + " TEXT NOT NULL CHECK(VatType IN ('VAT 0%', 'VAT Exempted', 'VAT 15%')), " +
            LongDescription + " TEXT NOT NULL, " +
            TRANSACTION_NATURE + " TEXT, " +
            TRANSACTION_TAX_CODE + " TEXT, " +
            TRANSACTION_CURRENCY + " TEXT, " +
            TRANSACTION_ITEM_CODE + " TEXT, " +
            TRANSACTION_TOTAL_DISCOUNT + " DECIMAL(10, 2), " +
            TRANSACTION_SHOP_NO + " TEXT, " +
            TRANSACTION_TERMINAL_NO + " TEXT, " +
            TRANSACTION_DATE_CREATED + " TEXT, " +
            TRANSACTION_DATE_MODIFIED + " TEXT, " +
            TRANSACTION_TIME_CREATED + " TEXT, " +
            TRANSACTION_TIME_MODIFIED + " TEXT, " +
            TRANSACTION_CODE + " TEXT, " +
            TRANSACTION_DESCRIPTION + " TEXT, " +
            TRANSACTION_UNIT_PRICE + " DECIMAL(10, 2), " +
            TRANSACTION_QUANTITY + " INTEGER, " +
            TRANSACTION_DISCOUNT + " DECIMAL(10, 2), " +
            TRANSACTION_VAT_BEFORE_DISC + " DECIMAL(10, 2), " +
            TRANSACTION_VAT_AFTER_DISC + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_HT_A + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_TTC + " DECIMAL(10, 2), " +
            TRANSACTION_IS_TAXABLE + " INTEGER, " +
            TRANSACTION_DATE_TRANSACTION + " TEXT, " +
            TRANSACTION_TIME_TRANSACTION + " TEXT, " +
            TRANSACTION_BARCODE + " TEXT, " +
            TRANSACTION_WEIGHTS + " TEXT, " +
            TRANSACTION_TOTAL_HT_B + " DECIMAL(10, 2), " +
            TRANSACTION_TYPE_TAX + " TEXT, " +
            TRANSACTION_RAYON + " TEXT, " +
            PriceAfterDiscount + " DECIMAL(10, 2),  " +
            TRANSACTION_FAMILLE + " TEXT, " +
            TRANSACTION_ID_SALES_D + " TEXT, " +
            TRANSACTION_TOTALIZER + " TEXT, " +
            TRANSACTION_COMMENT + " TEXT, " +
            TRANSACTION_SentToKitchen + " INTEGER DEFAULT 0, " + // 0 for not selected, 1 for selected
            TRANSACTION_STATUS + " TEXT NOT NULL CHECK(TransactionStatus IN ('VALID','Void','Cleared','Splitted')), " +
            "FOREIGN KEY (" + ROOM_ID + ") REFERENCES " + ROOMS + "(" + ID + ")," +

            "FOREIGN KEY (" + TABLE_ID + ") REFERENCES " + TABLES + "(" + TABLE_ID + ") , " +
            "FOREIGN KEY (" + ITEM_ID + ") REFERENCES " +
            TABLE_NAME + "(" + _ID + "));";



    private static final String CREATE_TRANSACTION_HEADER = "CREATE TABLE " + TRANSACTION_HEADER_TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRANSACTION_SHIFT_NUMBER  + " INTEGER, " +
            ROOM_ID + " INTEGER, " +
            TABLE_ID + " INTEGER, " +
            TRANSACTION_COPY_PRINTED  + " INTEGER, " +
            RELATED_TRANSACTION_ID  + " TEXT, " +
            ORDERTYPE + " TEXT, " +
             NUMBER_OF_COVERS + " INTEGER, " +
            TRANSACTION_SPLIT_TYPE + " TEXT NOT NULL DEFAULT 'Full' CHECK(" + TRANSACTION_SPLIT_TYPE + " IN ('Full', 'Splitted')), " +
            TRANSACTION_TICKET_NO + " INTEGER NOT NULL, " +
            TRANSACTION_SHOP_NO + " TEXT, " +
            TRANSACTION_TERMINAL_NO + " TEXT, " +
            TRANSACTION_DATE_CREATED + " TEXT, " +
            TRANSACTION_DATE_MODIFIED + " TEXT, " +
            TRANSACTION_TIME_CREATED + " TEXT, " +
            TRANSACTION_TIME_MODIFIED + " TEXT, " +
            TRANSACTION_PREVIOUS_HASH + " TEXT, " +
            TRANSACTION_MEMBER_CARD + " TEXT, " +
            TRANSACTION_SUB_TOTAL + " DECIMAL(10, 2), " +
            TRANSACTION_CASHIER_CODE + " TEXT, " +
            TRANSACTION_DATE_TRANSACTION + " TEXT, " +
            TRANSACTION_TIME_TRANSACTION + " TEXT, " +
            TRANSACTION_TOTAL_HT_A + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_TTC + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_PAID + " DECIMAL(10, 2), " +
            TRANSACTION_CASH_RETURN + " DECIMAL(10, 2), " +
            TRANSACTION_VAT_BEFORE_DISC + " DECIMAL(10, 2), " +
            TRANSACTION_VAT_AFTER_DISC + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_TX_1 + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_TX_2 + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_TX_3 + " DECIMAL(10, 2), " +
            TRANSACTION_TOTAL_DISCOUNT + " DECIMAL(10, 2), " +
            TRANSACTION_ITEM_QUANTITY + " INTEGER, " +
            TRANSACTION_TOTAL_HT_B + " DECIMAL(10, 2), " +
            TRANSACTION_CLIENT_NAME + " TEXT, " +
            TRANSACTION_CLIENT_OTHER_NAME + " TEXT, " +
            TRANSACTION_CLIENT_NIC + " TEXT, " +
            TRANSACTION_CLIENT_ADR1 + " TEXT, " +
            TRANSACTION_CLIENT_ADR2 + " TEXT, " +
            TRANSACTION_STATUS + " TEXT NOT NULL CHECK(TransactionStatus IN ('DRN','CRN','PRF', 'InProgress', 'Completed','TRN','CancelledOrder')), " +
            TRANSACTION_CLIENT_VAT_REG_NO + " TEXT, " +
            TRANSACTION_CLIENT_BRN + " TEXT, " +
            TRANSACTION_CLIENT_TEL + " TEXT, " +
            TRANSACTION_INVOICE_REF + " TEXT, " +
            TRANSACTION_IS_CASH_CREDIT + " TEXT, " +
            TRANSACTION_ID_SALESH + " TEXT, " +
            TRANSACTION_CLIENT_CODE + " TEXT, " +
            TRANSACTION_LOYALTY + " TEXT, " +
            TRANSACTION_MRA_QR + " TEXT, " +
            TRANSACTION_MRA_IRN + " TEXT, " +
            TRANSACTION_MRA_Invoice_Counter + " TEXT, " +
            TRANSACTION_MRA_Method + " TEXT," +
            "FOREIGN KEY (" + ROOM_ID + ") REFERENCES " + ROOMS + "(" + ID + ")," +
            "FOREIGN KEY (" + TABLE_ID + ") REFERENCES " + TABLES + "(" + TABLE_ID + "), " +
            "FOREIGN KEY (" + TRANSACTION_TICKET_NO + ") REFERENCES " +
            TRANSACTION_TABLE_NAME + "(" + TRANSACTION_ID + "));";



    // Create Invoice Settlement table query
    private static final String CREATE_INVOICE_SETTLEMENT_TABLE = "CREATE TABLE " + INVOICE_SETTLEMENT_TABLE_NAME + "(" +
            SETTLEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ROOM_ID + " INTEGER, " +
            TABLE_ID + " INTEGER, " +
            COLUMN_CASHOR_id + " INTEGER, "+
            SETTLEMENT_SHOP_NO + " TEXT, " +
            SETTLEMENT_TERMINAL_NO + " TEXT, " +
            SETTLEMENT_DATE_TRANSACTION + " TEXT, " +
            SETTLEMENT_Time_TRANSACTION + " TEXT, " +
            SETTLEMENT_PAYMENT_CODE + " TEXT, " +
            SETTLEMENT_AMOUNT + " DECIMAL(10, 2), " +
            SETTLEMENT_TOTAL_AMOUNT + " DECIMAL(10, 2), " +
            SETTLEMENT_GIFT_VOUCHER_NO + " TEXT, " +
            SETTLEMENT_INVOICE_ID + " TEXT, " +
            SETTLEMENT_REMARK + " TEXT, " +
            SETTLEMENT_DATE_CREATED + " TEXT, " +
            SETTLEMENT_Time_CREATED + " TEXT, " +
            SETTLEMENT_PAYMENT_NAME + " TEXT, " +
            SETTLEMENT_CHEQUE_NO + " TEXT, " +
            "FOREIGN KEY (" + ROOM_ID + ") REFERENCES " + ROOMS + "(" + ID + ")," +
            "FOREIGN KEY (" + TABLE_ID + ") REFERENCES " + TABLES + "(" + TABLE_ID + "), " +
            "FOREIGN KEY (" + SETTLEMENT_INVOICE_ID + ") REFERENCES " +
            TRANSACTION_TABLE_NAME + "(" + TRANSACTION_ID + "));";


    private static final String CREATE_STD_ACCESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STD_ACCESS + " ("
            + COLUMN_STD_ACCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_Comp_ADR_1 + " TEXT, "
            + COLUMN_Comp_ADR_2 + " TEXT, "
            + COLUMN_Comp_ADR_3 + " TEXT, "
            + COLUMN_Comp_TEL_NO + " TEXT, "
            + COLUMN_Comp_FAX_NO + " TEXT, "
            + COLUMN_SHOPNAME + " TEXT, "
            + COLUMN_SHOPNUMBER + " TEXT, "
            + COLUMN_Logo + " TEXT, "
            + COLUMN_VAT_NO + " TEXT, "
            + COLUMN_BRN_NO + " TEXT, "
            + COLUMN_ADR_1 + " TEXT, "
            + COLUMN_ADR_2 + " TEXT, "
            + COLUMN_ADR_3 + " TEXT, "
            + COLUMN_TEL_NO + " TEXT, "
            + COLUMN_FAX_NO + " TEXT, "
            + COLUMN_Opening_Hours + " TEXT, "
            + COLUMN_COMPANY_NAME + " TEXT, "
            + COLUMN_CASHOR_id + " INTEGER, "
            + COLUMN_POS_Num + " INTEGER, "
            + LastModified + " TEXT, "
            + DateCreated + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_SHOPNAME + ") REFERENCES " + TABLE_NAME_Users + "(" + COLUMN_CASHOR_Shop + ")"
            + ")";


    private static final String CREATE_POS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_POS_ACCESS + " ("
            + COLUMN_POS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TerminalNo + " TEXT, "
            + COLUMN_SHOPNAME + " TEXT, "
            + COLUMN_CASHOR_id + " INTEGER, "
            + LastModified + " TEXT, "
            + DateCreated + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_SHOPNAME + ") REFERENCES " + TABLE_NAME_Users + "(" + COLUMN_CASHOR_Shop + ")"
            + ")";


    private static final String CREATE_PAYMENT_METHOD_TABLE = "CREATE TABLE IF NOT EXISTS " + PAYMENT_METHOD_TABLE_NAME + " ("
            + PAYMENT_METHOD_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PAYMENT_METHOD_COLUMN_NAME + " TEXT NOT NULL, "
            + PAYMENT_METHOD_COLUMN_ICON + " TEXT, "
            + PAYMENT_METHOD_COLUMN_DATE_CREATED + " TEXT NOT NULL, "
            + PAYMENT_METHOD_COLUMN_LAST_MODIFIED + " TEXT , "
            + OpenDrawer + " BOOLEAN NOT NULL DEFAULT 1, "
            + PAYMENT_METHOD_COLUMN_CASHOR_ID + " TEXT NOT NULL, "
            + "FOREIGN KEY (" + PAYMENT_METHOD_COLUMN_CASHOR_ID + ") REFERENCES "
            + TABLE_NAME_Users + "(" + COLUMN_CASHOR_id + "));";



    public static final String CREATE_BUYER_TABLE = "CREATE TABLE " + BUYER_TABLE_NAME + "(" +
            BUYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            BUYER_NAME + " TEXT NOT NULL, " +
            BUYER_Other_NAME + " TEXT NOT NULL, " +
            BUYER_Company_name + " TEXT NOT NULL, " +
            BUYER_TAN + " TEXT , " +
            BUYER_BRN + " TEXT , " +
            BUYER_BUSINESS_ADDR + " TEXT, " +
            BUYER_TYPE + " TEXT NOT NULL, " +
            BUYER_NIC + " TEXT, " +
            BUYER_PriceLevel + " TEXT, " +
            BUYER_Profile + " TEXT, " +
            COLUMN_CASHOR_id + " TEXT, " +
            BUYER_DATE_CREATED +  " TEXT, " +
            BUYER_LAST_MODIFIED + " TEXT NOT NULL);";

    private static final String CREATE_COUPON_TABLE = "CREATE TABLE " + COUPON_TABLE_NAME + "(" +
            COUPON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COUPON_CODE + " TEXT NOT NULL, " +
            COUPON_STATUS + " TEXT NOT NULL, " +
            COUPON_START_DATE + " DATE NOT NULL, " +
            COUPON_END_DATE + " DATE NOT NULL, " +
            COUPON_CASHIER_ID + " INTEGER NOT NULL, " +
            COUPON_DATE_CREATED + " DATE NOT NULL, " +
            COUPON_TIME_CREATED + " TIME NOT NULL, " +
            COUPON_DISCOUNT + " REAL NOT NULL);";



    private static final String CREATE_FINANCIAL_TABLE = "CREATE TABLE IF NOT EXISTS " + FINANCIAL_TABLE_NAME + " ("
            + FINANCIAL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRANSACTION_SHIFT_NUMBER + " INTEGER, " // Set it to the current date
            + FINANCIAL_COLUMN_DATETIME + " DATE DEFAULT (date('now')), " // Set it to the current date
            + FINANCIAL_COLUMN_CURRENT_TIME + " TIME DEFAULT (time('now')), " // Add the CURRENT_TIME field
            + FINANCIAL_COLUMN_CASHOR_ID + " INTEGER NOT NULL, "
            + FINANCIAL_COLUMN_TRANSACTION_CODE + " TEXT NOT NULL, "
            + FINANCIAL_COLUMN_QUANTITY + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_TOTAL + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_TOTALIZER + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_POSNUM + " INTEGER NOT NULL, " // Add the POSNUM field
            + FINANCIAL_COLUMN_SHOP_NUMBER + " INTEGER NOT NULL, " // Add the SHOP_NUMBER field

            + FINANCIAL_COLUMN_PAYMENT + " TEXT NOT NULL" // Add the PAYMENT field
            + ");";


    private static final String CREATE_COUNTING_REPORT_TABLE = "CREATE TABLE " + COUNTING_REPORT_TABLE_NAME + "(" +
            COUNTING_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRANSACTION_SHIFT_NUMBER + " INTEGER, " +// Set it to the current date
            COUNTING_REPORT_TOTALIZER_TOTAL + " REAL NOT NULL, " +
            COUNTING_REPORT_TOTAL_VALUE + " REAL NOT NULL, " +
            COUNTING_REPORT_DIFFERENCE + " REAL NOT NULL, " +
            COUNTING_REPORT_CASHIER_ID + " INTEGER NOT NULL, " +
            COUNTING_REPORT_DATETIME + " DATETIME DEFAULT (DATETIME('now', 'localtime'))" +
            ");";
    // Define the new table name



    // Create the new table statement
    private static final String CREATE_CASH_REPORT_TABLE = "CREATE TABLE IF NOT EXISTS " + CASH_REPORT_TABLE_NAME + " ("
            + FINANCIAL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TRANSACTION_SHIFT_NUMBER + " INTEGER, " // Set it to the current date
            + FINANCIAL_COLUMN_DATETIME + " DATE DEFAULT (date('now')), " // Set it to the current date
            + FINANCIAL_COLUMN_CASHOR_ID + " INTEGER NOT NULL, "
            + FINANCIAL_COLUMN_QUANTITY + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_TOTAL + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_POSNUM + " INTEGER NOT NULL," // Add the POSNUM field
            +  FINANCIAL_COLUMN_TransId + " TEXT , "
             + FINANCIAL_CashReturn + " DECIMAL(10, 2) "
            + ");";





    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_OPTIONS_TABLE);
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_VENDOR_TABLE);
        db.execSQL(CREATE_COST_TABLE);
        db.execSQL(CREATE_DEPARTMENT_TABLE);
        db.execSQL(CREATE_SUBDEPARTMENT_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_DISCOUNT_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
        db.execSQL(CREATE_TRANSACTION_HEADER);
        db.execSQL(CREATE_INVOICE_SETTLEMENT_TABLE);
        db.execSQL(CREATE_STD_ACCESS_TABLE);
        db.execSQL(CREATE_PAYMENTBYQY_TABLE);
        db.execSQL(CREATE_PAYMENT_METHOD_TABLE);
        db.execSQL(CREATE_POS_TABLE);
        db.execSQL(CREATE_BUYER_TABLE);
        db.execSQL(CREATE_COUPON_TABLE);
        db.execSQL(CREATE_FINANCIAL_TABLE);
        db.execSQL(CREATE_COUNTING_REPORT_TABLE);
        db.execSQL(CREATE_CASH_REPORT_TABLE);
        db.execSQL(CREATE_ROOM_TABLE);
        db.execSQL(CREATE_TABLE_TABLE);
        db.execSQL(CREATE_SUPPLEMENTS_TABLE);
        db.execSQL(CREATE_SUPPLEMENTS_OPTIONS_TABLE);
        db.execSQL(CREATE_VARIANTS_TABLE);

        // Add default data
        addDefaultItem(db);
        addDefaultCat(db);
        addDefaultMenuCat(db);
        addDefaultSupplement(db, "Supplements", "#B7FFD7");
        addDefaultPaymentMethod(db, "POP", "1");
        addDefaultPaymentMethod(db, "Cheque", "1");
        addDefaultPaymentMethod(db, "Cash", "1");
        addDefaultPaymentMethod(db, "Credit Card", "1");
        addDefaultPaymentMethod(db, "Debit Card", "1");
        addDefaultPaymentMethod(db, "Coupon Code", "1");

        // Handle adding new columns
        addNewColumns(db);
    }

    private void addNewColumns(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN TRANSACTION_COPY_PRINTED INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN RELATED_TRANSACTION_ID TEXT");
            db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN ORDERTYPE TEXT");
            db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN NUMBER_OF_COVERS INTEGER DEFAULT 0");
        } catch (SQLException e) {
            // Handle exceptions, such as column already existing
            e.printStackTrace();
        }
    }






    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DB_VERSION) {
            try {
                db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN " + TRANSACTION_COPY_PRINTED + " INTEGER DEFAULT 0");
                Log.d("DatabaseHelper", "Added TRANSACTION_COPY_PRINTED column to " + TRANSACTION_HEADER_TABLE_NAME);

                db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN " + RELATED_TRANSACTION_ID + "  TEXT");
                Log.d("DatabaseHelper", "Added RELATED_TRANSACTION_ID column to " + TRANSACTION_HEADER_TABLE_NAME);

                db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN " + ORDERTYPE + "  TEXT");
                Log.d("DatabaseHelper", "Added ORDERTYPE column to " + TRANSACTION_HEADER_TABLE_NAME);

                db.execSQL("ALTER TABLE " + TRANSACTION_HEADER_TABLE_NAME + " ADD COLUMN " + NUMBER_OF_COVERS + "  INTEGER DEFAULT 0");
                Log.d("DatabaseHelper", "Added NUMBER_OF_COVERS column to " + TRANSACTION_HEADER_TABLE_NAME);

                db.execSQL("ALTER TABLE " + INVOICE_SETTLEMENT_TABLE_NAME + " ADD COLUMN " + COLUMN_CASHOR_id + "  INTEGER");
                Log.d("DatabaseHelper", "Added NUMBER_OF_COVERS column to " + INVOICE_SETTLEMENT_TABLE_NAME);

                Log.d("DB_VERSION", String.valueOf(DB_VERSION));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Add further version upgrades if needed for future versions
    }

    public String getOrderTypeByTransactionTicketNo(String transactionTicketNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { ORDERTYPE };
        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionTicketNo };

        Cursor cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String orderType = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    orderType = cursor.getString(cursor.getColumnIndex(ORDERTYPE));
                }
            } finally {
                cursor.close();
            }
        }

        return orderType;
    }

    public int getNumberOfCovers(String transactionTicketNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfCovers = 0;

        String[] projection = { NUMBER_OF_COVERS };
        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionTicketNo };

        Cursor cursor = null;
        try {
            cursor = db.query(
                    TRANSACTION_HEADER_TABLE_NAME,   // Table name
                    projection,                     // Columns to return
                    selection,                      // WHERE clause
                    selectionArgs,                  // WHERE clause arguments
                    null,                           // GROUP BY
                    null,                           // HAVING
                    null                            // ORDER BY
            );

            if (cursor != null && cursor.moveToFirst()) {
                numberOfCovers = cursor.getInt(cursor.getColumnIndexOrThrow(NUMBER_OF_COVERS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return numberOfCovers;
    }

    public Integer getTableIdByTransactionTicketNo(String transactionTicketNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { TABLE_ID };
        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionTicketNo };

        Cursor cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Integer tableId = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    tableId = cursor.getInt(cursor.getColumnIndex(TABLE_ID));
                }
            } finally {
                cursor.close();
            }
        }

        return tableId;
    }

    public Integer getRoomIdByTransactionTicketNo(String transactionTicketNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { ROOM_ID };
        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionTicketNo };

        Cursor cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Integer roomId = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    roomId = cursor.getInt(cursor.getColumnIndex(ROOM_ID));
                }
            } finally {
                cursor.close();
            }
        }

        return roomId;
    }



    public int getCopyPrinted( String transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int copiesPrinted = 0;
        String[] columns = { TRANSACTION_COPY_PRINTED };
        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionId };

        Cursor cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME, // The table to query
                columns,                        // The columns to return
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                           // Group by
                null,                           // Having
                null                            // Order by
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                copiesPrinted = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COPY_PRINTED));
            }
            cursor.close();
        }

        return copiesPrinted;
    }


    public void updateCopyPrinted(String transactionId, int copiesPrinted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int newval= copiesPrinted + 1;
        values.put(TRANSACTION_COPY_PRINTED, newval);

        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionId };

        int count = db.update(
                TRANSACTION_HEADER_TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count > 0) {
            // Successfully updated
            Log.d("COPY_PRINTED Update", "Successfully updated " + count + " rows.");
        } else {
            // Update failed or transaction ID not found
            Log.d("COPY_PRINTED Update", "Update failed or transaction ID not found.");
        }
    }

    public boolean hasItemsForTransactionId(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_ID + " = ?";
        String[] selectionArgs = { transactionId };

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean hasItems = false;

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            hasItems = count > 0;
            cursor.close();
        }

        return hasItems;
    }


    public void updateTransactionTableNumber(String currentTableId, String newTableId, String roomId, String newTransactionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_ID, newTableId);
        values.put(TRANSACTION_ID, newTransactionId); // Assuming TRANSACTION_ID is the column name for the transaction ID
        Log.d("newTransactionId", newTransactionId);
        Log.d("currentTableId", currentTableId);
        // Define the WHERE clause to identify the row(s) to be updated
        String whereClause = TABLE_ID + " = ? AND " + ROOM_ID + " = ? AND " + TRANSACTION_STATUS + " = ?";
        String[] whereArgs = {currentTableId, roomId, "InProgress"};


        // Update the table with the new table number and transaction ID
        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);

        // Optionally, you can check the number of rows affected
        if (rowsAffected > 0) {
            Log.d("updateTransactionTableNumber", "Transaction table number and transaction ID updated successfully.");
        } else {
            Log.d("updateTransactionTableNumber", "Failed to update transaction table number and transaction ID.");
        }
removeDuplicateTransactions(db,newTransactionId);
        // Close the database connection
        db.close();
    }



    public void updateTableNumber(String newTransactionId,String currentTableId, String newTableId, String roomId ) {
        SQLiteDatabase db = this.getWritableDatabase();
        String statusType= getLatestTransactionStatus(String.valueOf(roomId),newTableId);
        String latesttransId= getLatestTransactionId(String.valueOf(roomId),newTableId,statusType);


        ContentValues values = new ContentValues();
        values.put(TABLE_ID, newTableId);
        values.put(TRANSACTION_ID, latesttransId); // Assuming TRANSACTION_ID is the column name for the transaction ID

        // Define the WHERE clause to identify the row(s) to be updated
        String whereClause = TABLE_ID + " = ? AND " + ROOM_ID + " = ? AND " + IS_PAID + " = ?";
        String[] whereArgs = {currentTableId, roomId, "0"};

        // Update the table with the new table ID and transaction ID if IS_PAID is 0
        int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, whereClause, whereArgs);

        // Optionally, you can check the number of rows affected
        if (rowsAffected > 0) {
            Log.d("transfer", "Table number and transaction ID updated successfully."+ "roomId "+ roomId+ " " + newTransactionId);
        } else {
            Log.d("transfer", "Failed to update table number and transaction ID. The table may be paid or not found."+ "roomId "+ roomId+ " newTransactionId" + newTransactionId);
        }

        // Now perform the deletion if the update was successful
        if (rowsAffected > 0) {
            deleteTransactionsByConditions(db, roomId, currentTableId, newTransactionId);
        }
        // Close the database connection
        db.close();
    }

    public void updateTransactionTableNumberFornew(String currentTableId, String newTableId, String roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_ID, newTableId);

        // Define the WHERE clause to identify the row(s) to be updated
        String whereClause = TABLE_ID + " = ? AND " + ROOM_ID + " = ?";
        String[] whereArgs = {currentTableId, roomId};

        // Update the table with the new table number
        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);

        // Optionally, you can check the number of rows affected
        if (rowsAffected > 0) {
            Log.d("updateTransactionTableNumber", "Transaction table number updated successfully.");
        } else {
            Log.d("updateTransactionTableNumber", "Failed to update transaction table number.");
        }

        // Close the database connection
        db.close();
    }
    public void updateTableNumberfornew(String currentTableId, String newTableId, String roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_ID, newTableId);

        // Define the WHERE clause to identify the row(s) to be updated
        String whereClause = TABLE_ID + " = ? AND " + ROOM_ID + " = ? AND " + IS_PAID + " = ?";
        String[] whereArgs = {currentTableId, roomId, "0"};

        // Update the table with the new table ID if IS_PAID is 0
        int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, whereClause, whereArgs);

        // Optionally, you can check the number of rows affected
        if (rowsAffected > 0) {
            Log.d("transfer", "Table number updated successfully.");
        } else {
            Log.d("transfer", "Failed to update table number. The table may be paid or not found.");
        }

        // Close the database connection
        db.close();
    }

    public String getShopNumber(SQLiteDatabase db) {
        String shopNumber = null;
        String[] projection = {COLUMN_SHOPNUMBER};

        Cursor cursor = db.query(
                TABLE_NAME_STD_ACCESS,  // The table to query
                projection,             // The columns to return
                null,                   // No selection
                null,                   // No selectionArgs
                null,                   // Don't group the rows
                null,                   // Don't filter by row groups
                null                    // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            shopNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOPNUMBER));
            cursor.close();
        }

        return shopNumber;
    }
    private String getDateFilterForFinancialTableBasedOnReportType(String reportType) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        switch (reportType) {
            case "Daily":
                return "DATE(" + FINANCIAL_COLUMN_DATETIME + ") = '" + currentDate + "'";
            case "Weekly":
                calendar.add(Calendar.DAY_OF_YEAR, -7); // Subtract 7 days for a weekly range
                String oneWeekAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + FINANCIAL_COLUMN_DATETIME + ") >= '" + oneWeekAgo + "' AND DATE(" + FINANCIAL_COLUMN_DATETIME + ") <= '" + currentDate + "'";
            case "Monthly":
                calendar.add(Calendar.MONTH, -1); // Subtract 1 month for a monthly range
                String oneMonthAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + FINANCIAL_COLUMN_DATETIME + ") >= '" + oneMonthAgo + "' AND DATE(" + FINANCIAL_COLUMN_DATETIME + ") <= '" + currentDate + "'";
            case "Yearly":
                calendar.add(Calendar.YEAR, -1); // Subtract 1 year for a yearly range
                String oneYearAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + FINANCIAL_COLUMN_DATETIME + ") >= '" + oneYearAgo + "' AND DATE(" + FINANCIAL_COLUMN_DATETIME + ") <= '" + currentDate + "'";
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
    }

    public double getSumOfTotalForCouponCodeTransactionsByReportTypePerShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Coupon Code"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Coupon Code", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForCouponCodeTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Coupon Code"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Coupon Code"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForCouponCodeTransactionsByReportTypeAndCashierId(String reportType, int cashierId) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Coupon Code" and the cashier ID matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter +
                " AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Coupon Code", String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForPOPTransactionsByReportTypePerShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "POP"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"POP", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForPOPTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "POP"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"POP"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForPOPTransactionsByReportTypeAndCashierId(String reportType, int cashierId) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "POP" and the cashier ID matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter +
                " AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"POP", String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }
    public List<String> getAllCashierIds() {
        List<String> cashierIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CASHOR_id + " FROM " + TABLE_NAME_Users, null);
        if (cursor.moveToFirst()) {
            do {
                cashierIds.add(cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_id)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();  // Make sure to close the database
        return cashierIds;
    }

    public double getSumOfTotalForChequesTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Cheques"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cheque"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForChequesTransactionsByReportTypePerShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Cheques"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cheque", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForChequesTransactionsByReportTypeAndCashierId(String reportType, int cashierId) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Cheques" and the cashier ID matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter +
                " AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cheque", String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForDebitCardTransactionsByReportTypePerShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Debit Card"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Debit Card", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForDebitCardTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Debit Card"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Debit Card"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }
    public double getSumOfTotalForCreditCardTransactionsByReportTypePerShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Credit Card"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Credit Card", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }


    public double getSumOfTotalForCreditCardTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Credit Card"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Credit Card"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForCreditCardTransactionsByReportTypeAndCashierId(String reportType, int cashierId) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Credit Card" and the cashier ID matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter +
                " AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Credit Card", String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForDebitCardTransactionsByReportTypeAndCashierId(String reportType, int cashierId) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Debit Card" and the cashier ID matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter +
                " AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Debit Card", String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForCashTransactionsByReportTypePerShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALIZER is "Cash" and the shift number matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter +
                " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cash", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForCashTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALISER is "Cash" and the cashier ID matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter ;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cash"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }
    public double getSumOfTotalForCashTransactionsByReportTypeAndCashierId(String reportType, int cashierId) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TOTALISER is "Cash" and the cashier ID matches
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TOTALIZER + " = ? AND " + dateFilter +
                " AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cash", String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }
    public double getSumOfTransactionTotalTTC(String cashierCode, String reportType) {
        double sum = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType2(reportType);

        // Construct the SQL query
        String query = "SELECT SUM(" + TRANSACTION_TOTAL_TTC + ") as TotalSumTTC " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_CASHIER_CODE + " = ? " +
                " AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{cashierCode});
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(cursor.getColumnIndex("TotalSumTTC"));
        }
        cursor.close();
        db.close();

        return sum;
    }

    public double getSumOfTransactionTotalTTCPerShift(String cashierCode, String reportType, int shiftNumber) {
        double sum = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType2(reportType);

        // Construct the SQL query
        String query = "SELECT SUM(" + TRANSACTION_TOTAL_TTC + ") as TotalSumTTC " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_CASHIER_CODE + " = ? " +
                " AND " + TRANSACTION_SHIFT_NUMBER + " = ? " +

                " AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{cashierCode, String.valueOf(shiftNumber)});
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(cursor.getColumnIndex("TotalSumTTC"));
        }
        cursor.close();
        db.close();

        return sum;
    }

    public double getSumOfTransactionTotalHTAperShift(String cashierCode, String reportType, int shiftNumber) {
        double sum = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType2(reportType);

        // Construct the SQL query
        String query = "SELECT SUM(" + TRANSACTION_TOTAL_HT_A + ") as TotalSum " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_CASHIER_CODE + " = ? " +
                " AND " + TRANSACTION_SHIFT_NUMBER + " = ? " +
                " AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{cashierCode, String.valueOf(shiftNumber)});
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(cursor.getColumnIndex("TotalSum"));
        }
        cursor.close();
        db.close();

        return sum;
    }

    // Method to get the sum of TRANSACTION_TOTAL_HT_A based on TRANSACTION_CASHIER_CODE and report type
    public double getSumOfTransactionTotalHTA(String cashierCode, String reportType) {
        double sum = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType2(reportType);

        // Construct the SQL query
        String query = "SELECT SUM(" + TRANSACTION_TOTAL_HT_A + ") as TotalSum " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_CASHIER_CODE + " = ? " +
                " AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{cashierCode});
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(cursor.getColumnIndex("TotalSum"));
        }
        cursor.close();
        db.close();

        return sum;
    }

    // Method to get the date filter based on the report type
    private String getDateFilterForFinancialTableBasedOnReportType2(String reportType) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        switch (reportType) {
            case "Daily":
                return "DATE(" + TRANSACTION_DATE_CREATED + ") = '" + currentDate + "'";
            case "Weekly":
                calendar.add(Calendar.DAY_OF_YEAR, -7); // Subtract 7 days for a weekly range
                String oneWeekAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE_CREATED + ") >= '" + oneWeekAgo + "' AND DATE(" + TRANSACTION_DATE_CREATED + ") <= '" + currentDate + "'";
            case "Monthly":
                calendar.add(Calendar.MONTH, -1); // Subtract 1 month for a monthly range
                String oneMonthAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE_CREATED + ") >= '" + oneMonthAgo + "' AND DATE(" + TRANSACTION_DATE_CREATED + ") <= '" + currentDate + "'";
            case "Yearly":
                calendar.add(Calendar.YEAR, -1); // Subtract 1 year for a yearly range
                String oneYearAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE_CREATED + ") >= '" + oneYearAgo + "' AND DATE(" + TRANSACTION_DATE_CREATED + ") <= '" + currentDate + "'";
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
    }

    // Method to get the date filter based on the report type

    public double getSumOfTotalForLoanTransactionsByReportTypePerShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TRANSACTION_CODE is "Loan"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Loan", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForLoanTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TRANSACTION_CODE is "Loan"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Loan"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }
    public double getSumOfTotalForPickupTransactionsByReportTypeperShift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TRANSACTION_CODE is "Pick Up"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Pick Up", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForPickupTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TRANSACTION_CODE is "Loan"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Pick Up"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }
    public double getSumOfTotalForCashInTransactionsByReportTypepershift(String reportType, int shiftNumber) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TRANSACTION_CODE is "Cash In"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cash In", String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }

    public double getSumOfTotalForCashInTransactionsByReportType(String reportType) {
        double sumTotal = 0.0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterForFinancialTableBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum FINANCIAL_COLUMN_TOTAL where FINANCIAL_COLUMN_TRANSACTION_CODE is "Loan"
        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTAL + ") AS sumTotal " +
                "FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{"Cash In"});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of FINANCIAL_COLUMN_TOTAL from the cursor
            sumTotal = cursor.getDouble(cursor.getColumnIndex("sumTotal"));
        }

        cursor.close();
        db.close();

        return sumTotal;
    }
    public boolean updateTenderAmount(String transactionTicketNo, double newTenderAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_CASH_RETURN, newTenderAmount);

        // Define the WHERE clause and the corresponding arguments
        String whereClause = TRANSACTION_TICKET_NO + " = ?";
        String[] whereArgs = {transactionTicketNo};

        // Execute the update
        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, contentValues, whereClause, whereArgs);

        db.close();

        // Return true if at least one row was updated
        return rowsAffected > 0;
    }


    public int getPosNumFromFinancialTable(SQLiteDatabase db) {
        int posNum = -1; // Default value if the query does not return any result
        String query = "SELECT DISTINCT " + FINANCIAL_COLUMN_POSNUM +
                " FROM " + FINANCIAL_TABLE_NAME +
                " LIMIT 1"; // Assuming POSNUM is same for all rows, so fetching only the first one

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            posNum = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_POSNUM));
        }
        cursor.close();
        return posNum;
    }

    public boolean itemHasSupplementsOrOptions(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean hasSupplementsOrOptions = false;

        // Define the query to check if the item has supplements or options
        String query = "SELECT " + hasoptions + ", " + hasSupplements +
                " FROM " + TABLE_NAME +
                " WHERE " + _ID + " = ?";

        // Use try-with-resources to ensure the cursor is closed after use
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(itemId)})) {
            if (cursor.moveToFirst()) {
                boolean hasOptions = cursor.getInt(cursor.getColumnIndex(hasoptions)) == 1;
                boolean hasSupplement = cursor.getInt(cursor.getColumnIndex(hasSupplements)) == 1;
                hasSupplementsOrOptions = hasOptions || hasSupplement;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error checking item for supplements or options", e);
        }

        // Log the result
        Log.d("DatabaseHelper", "Item ID: " + itemId + ", hasSupplementsOrOptions: " + hasSupplementsOrOptions);

        return hasSupplementsOrOptions;
    }

    public String getBarcodeByItemId(String itemId) {
        String barcode = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = { Barcode };

        // Define the selection criteria
        String selection = _ID + " = ?";
        String[] selectionArgs = { itemId };

        // Query the database
        Cursor cursor = db.query(
                TABLE_NAME,      // The table to query
                projection,      // The columns to return
                selection,       // The columns for the WHERE clause
                selectionArgs,   // The values for the WHERE clause
                null,            // Don't group the rows
                null,            // Don't filter by row groups
                null             // The sort order
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                barcode = cursor.getString(cursor.getColumnIndex(Barcode));
            }
            cursor.close();
        }

        db.close();
        return barcode;
    }


    private int sumQuantityWithSameBarcodeAndTransactionId(SQLiteDatabase db, String transactionId, String barcode) {
        String sumQuery = "SELECT SUM(" + QUANTITY + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ? AND " +
                TRANSACTION_BARCODE + " = ?";

        Cursor cursor = db.rawQuery(sumQuery, new String[]{transactionId, barcode});

        int sum = 0;
        if (cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }

        cursor.close();
        return sum;
    }

    public void removeDuplicateTransactions(SQLiteDatabase db, String transactionId) {
        String query = "SELECT " + TRANSACTION_BARCODE +
                " FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ?" +
                " GROUP BY " + TRANSACTION_BARCODE +
                " HAVING COUNT(*) > 1";

        Cursor cursor = db.rawQuery(query, new String[]{transactionId});

        while (cursor.moveToNext()) {
            String barcode = cursor.getString(cursor.getColumnIndex(TRANSACTION_BARCODE));

            // Identify rows to consolidate (all rows with the same barcode)
            Cursor rowsToConsolidate = db.query(
                    TRANSACTION_TABLE_NAME,
                    new String[]{_ID, QUANTITY, PriceAfterDiscount, TRANSACTION_UNIT_PRICE, TRANSACTION_TOTAL_TTC, VAT, TRANSACTION_TOTAL_HT_A, TRANSACTION_VAT_BEFORE_DISC, TRANSACTION_VAT_AFTER_DISC, TRANSACTION_TOTAL_DISCOUNT},
                    TRANSACTION_ID + " = ? AND " + TRANSACTION_BARCODE + " = ?",
                    new String[]{transactionId, barcode},
                    null,
                    null,
                    null
            );

            int totalQuantity = 0;
            BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal totalTtc = BigDecimal.ZERO;
            BigDecimal vat = BigDecimal.ZERO;
            BigDecimal totalHtA = BigDecimal.ZERO;
            BigDecimal vatBeforeDisc = BigDecimal.ZERO;
            BigDecimal vatAfterDisc = BigDecimal.ZERO;
            BigDecimal totalDiscount = BigDecimal.ZERO;

            // Calculate sum of quantities and other fields for the rows to consolidate
            while (rowsToConsolidate.moveToNext()) {
                int quantity = rowsToConsolidate.getInt(rowsToConsolidate.getColumnIndex(QUANTITY));
                BigDecimal unitPrice = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(TRANSACTION_UNIT_PRICE)));
                BigDecimal sellingPrice = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(PriceAfterDiscount)));
                BigDecimal unitTtc = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(TRANSACTION_TOTAL_TTC)));
                BigDecimal unitVat = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(VAT)));
                BigDecimal unitHtA = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(TRANSACTION_TOTAL_HT_A)));
                BigDecimal unitVatBeforeDisc = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(TRANSACTION_VAT_BEFORE_DISC)));
                BigDecimal unitVatAfterDisc = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(TRANSACTION_VAT_AFTER_DISC)));
                BigDecimal unitDiscount = BigDecimal.valueOf(rowsToConsolidate.getDouble(rowsToConsolidate.getColumnIndex(TRANSACTION_TOTAL_DISCOUNT)));

                totalQuantity += quantity;
                totalPrice = sellingPrice.multiply(BigDecimal.valueOf(totalQuantity));
                totalTtc = unitTtc.multiply(BigDecimal.valueOf(totalQuantity));
                vat = unitVat.multiply(BigDecimal.valueOf(totalQuantity));
                totalHtA = unitHtA.multiply(BigDecimal.valueOf(totalQuantity));
                vatBeforeDisc = unitVatBeforeDisc.multiply(BigDecimal.valueOf(totalQuantity));
                vatAfterDisc = unitVatAfterDisc.multiply(BigDecimal.valueOf(totalQuantity));
                totalDiscount = unitDiscount.multiply(BigDecimal.valueOf(totalQuantity));
            }

            rowsToConsolidate.close();

            // Delete duplicate rows except the first one
            String deleteQuery = "DELETE FROM " + TRANSACTION_TABLE_NAME +
                    " WHERE " + TRANSACTION_ID + " = ? AND " +
                    TRANSACTION_BARCODE + " = ? AND " +
                    _ID + " NOT IN (SELECT MIN(" + _ID + ")" +
                    " FROM " + TRANSACTION_TABLE_NAME +
                    " WHERE " + TRANSACTION_ID + " = ? AND " +
                    TRANSACTION_BARCODE + " = ?)";

            db.execSQL(deleteQuery, new String[]{transactionId, barcode, transactionId, barcode});

            // Update the first row with the consolidated values
            ContentValues consolidatedValues = new ContentValues();
            consolidatedValues.put(QUANTITY, totalQuantity);
            consolidatedValues.put(TRANSACTION_QUANTITY, totalQuantity);
            consolidatedValues.put(TOTAL_PRICE, totalPrice.setScale(2, RoundingMode.HALF_UP).doubleValue());
            consolidatedValues.put(TRANSACTION_TOTAL_TTC, totalTtc.setScale(2, RoundingMode.HALF_UP).doubleValue());
            consolidatedValues.put(VAT, vat.setScale(2, RoundingMode.HALF_UP).doubleValue());
            consolidatedValues.put(TRANSACTION_TOTAL_HT_A, totalHtA.setScale(2, RoundingMode.HALF_UP).doubleValue());
            consolidatedValues.put(TRANSACTION_VAT_BEFORE_DISC, vatBeforeDisc.setScale(2, RoundingMode.HALF_UP).doubleValue());
            consolidatedValues.put(TRANSACTION_VAT_AFTER_DISC, vatAfterDisc.setScale(2, RoundingMode.HALF_UP).doubleValue());
            consolidatedValues.put(TRANSACTION_TOTAL_DISCOUNT, totalDiscount.setScale(2, RoundingMode.HALF_UP).doubleValue());

            db.update(
                    TRANSACTION_TABLE_NAME,
                    consolidatedValues,
                    TRANSACTION_ID + " = ? AND " + TRANSACTION_BARCODE + " = ?",
                    new String[]{transactionId, barcode}
            );
        }

        cursor.close();
    }




    public boolean resetMergedStatusToDefault(SQLiteDatabase db, String roomId, String tableId) {
        boolean isSuccess = false;

        try {
            // Define the ContentValues to reset the MERGED and MERGED_SET_ID columns to default value 0
            ContentValues values = new ContentValues();
            values.put(MERGED, 0);
            values.put(MERGED_SET_ID, 0);

            // Define the where clause and where arguments
            String whereClause = ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
            String[] whereArgs = {roomId, tableId};

            // Update the TABLES table with the default values
            int rowsUpdated = db.update(TABLES, values, whereClause, whereArgs);

            // Check if the update was successful
            if (rowsUpdated > 0) {
                isSuccess = true;
                Log.d("resetMergedStatusToDefault", "Successfully reset MERGED status to default value 0 for Room ID: " + roomId + ", Table ID: " + tableId);
            } else {
                Log.e("resetMergedStatusToDefault", "Failed to reset MERGED status to default value 0 for Room ID: " + roomId + ", Table ID: " + tableId);
            }

        } catch (Exception e) {
            Log.e("resetMergedStatusToDefault", "Error resetting MERGED status to default value 0", e);
        }

        return isSuccess;
    }


    public void deleteAllDataFromTable(String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();

        try {
            // Delete all rows from the specified table
            database.delete(tableName, null, null);
            Log.d("Delete", "All data deleted from " + tableName);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            Log.e("Delete", "Error deleting data from " + tableName + ": " + e.getMessage());
        } finally {
            // Close the database
            database.close();
        }
    }
    public void deleteAllDataFromTableTable(String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();

        try {
            // Delete all rows from the specified table
            database.delete(tableName, null, null);
            Log.d("Delete", "All data deleted from " + tableName);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            Log.e("Delete", "Error deleting data from " + tableName + ": " + e.getMessage());
        } finally {
            // Close the database
            database.close();
        }
    }
    public void deleteAllDataFromRoomsTable(String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();

        try {
            // Delete all rows from the specified table
            database.delete(tableName, null, null);
            Log.d("Delete", "All data deleted from " + tableName);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            Log.e("Delete", "Error deleting data from " + tableName + ": " + e.getMessage());
        } finally {
            // Close the database
            database.close();
        }
    }
    private List<String> extractTableIds(String newTableId) {
        List<String> tableIds = new ArrayList<>();

        // Split the newTableId string by the "+" symbol
        String[] tableNumbers = newTableId.split("\\+");

        for (String tableNum : tableNumbers) {
            // Trim and remove "T " prefix if it exists
            String cleanedTableNum = tableNum.trim().replace("T ", "");
            tableIds.add(cleanedTableNum);
        }

        return tableIds;
    }

    public Cursor getTransactionsForNetPrinting(String transactionId, String roomid, String tableid) {
        SQLiteDatabase db = getReadableDatabase();

        if (transactionId == null || transactionId.isEmpty() || roomid == null || roomid.isEmpty() || tableid == null || tableid.isEmpty()) {
            Log.e("Invalid parameters", "Invalid parameters: transactionId, roomid, or tableid is null or empty.");
            return null;
        }

        // Define your query with the join
        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                " INNER JOIN " + CAT_TABLE_NAME + " ON " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_FAMILLE + " = " + CAT_TABLE_NAME + "." + _ID +
                " WHERE " + TRANSACTION_TICKET_NO + "=? AND " +
                ROOM_ID + "=? AND " +
                TABLE_ID + "=? AND " +
                IS_PAID + "!=? AND " +
                TRANSACTION_STATUS + "='VALID' AND " +
                CAT_TABLE_NAME + "." + CAT_PRINTER_OPTION + "=? " +
                "ORDER BY " + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {transactionId, roomid, tableid, "1", "true"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        return cursor;
    }

    public void deleteTransactionsByConditions(SQLiteDatabase db, String roomId, String tableIdToDelete, String transactionIdToKeep) {
        SQLiteDatabase dbs = getWritableDatabase();

        try {
            // Define where clause and where args
            String whereClause = ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_TICKET_NO + " != ? AND (" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?)";
            String[] whereArgs = {roomId, tableIdToDelete, transactionIdToKeep, "InProgress", "PRF"};

            // Delete rows from TRANSACTION_HEADER table based on the conditions
            int rowsDeleted = dbs.delete(TRANSACTION_HEADER_TABLE_NAME, whereClause, whereArgs);
            Log.d("deleteT1", "roomId" + roomId + "tableIdToDelete" + tableIdToDelete+ "transactionIdToKeep" + transactionIdToKeep);

            // Log the number of rows deleted
            Log.d("deleteTransactionsByConditions", "Rows Deleted from TRANSACTION_HEADER based on conditions: " + rowsDeleted);

        } catch (Exception e) {
            Log.e("deleteTransactionsByConditions", "Error deleting transactions by conditions", e);
        }
    }



    public void updateTableIdInTransactions(SQLiteDatabase db,String cashierid, String roomid,String currentTableId, String newTableId,int totalcover) {
        try {
            Log.e("newTableId", "newTableId: " + newTableId);
            if (newTableId.startsWith("T T")) {
                newTableId = newTableId.replaceFirst("T", ""); // Remove first "T " from the beginning
                Log.e("newTableId1", "newTableId1: " + newTableId);
            }
            Log.e("newTableId2", "newTableId2: " + newTableId);
            // Get the transaction ID from the header table
            //String transactionId = getTransactionIdFromHeader(db, currentTableId, "InProgress");
            String statusType= getLatestTransactionStatus(String.valueOf(roomid),currentTableId);
            String latesttransId= getLatestTransactionId(String.valueOf(roomid),currentTableId,statusType);

            List<String> tableIds = extractTableIds(newTableId);

            for (String tableId : tableIds) {
                Log.d("extractTableIds", "Table ID: " + tableId);

                // Update TRANSACTION_ID in TRANSACTIONS table
                ContentValues valuesTransactionsLoop = new ContentValues();
                valuesTransactionsLoop.put(TRANSACTION_ID, latesttransId);
                Log.d("TRANSACTION_ID", "TRANSACTION_ID ID: " + latesttransId);

                // Define the WHERE clause to include TABLE_ID, IS_PAID, and STATUS
                String whereClauseTransactionsLoop = TABLE_ID + " = ? AND " + IS_PAID + " = ? AND " + TRANSACTION_STATUS + " = ?";
                String[] whereArgsTransactionsLoop = {tableId, "0", "VALID"};



                Log.d("updateTransactionTableIds", "Where Clause: " + whereClauseTransactionsLoop);
                Log.d("updateTransactionTableIds", "Where Args: " + Arrays.toString(whereArgsTransactionsLoop));

                int rowsUpdatedTransactionsLoop = db.update(TRANSACTION_TABLE_NAME, valuesTransactionsLoop,
                        whereClauseTransactionsLoop, whereArgsTransactionsLoop);

                // Log the number of rows updated
                Log.d("updateTransactionTableIds", "Rows Updated in TRANSACTIONS for table " + tableId + ": " + rowsUpdatedTransactionsLoop);
                deleteTransactionsByConditions(db,roomid,tableId,latesttransId);


            }


            if (latesttransId == null) {
                    Log.e("Transactions1", "Transaction ID not found for currentTableId: " + currentTableId);
                    return; // Exit the method if transactionId is still null
                }
            removeDuplicateTransactions(db, latesttransId);

            // Begin the transaction
            db.beginTransaction();

            // Update TABLE_ID in TRANSACTIONS table
            ContentValues valuesTransactions = new ContentValues();
            valuesTransactions.put(TABLE_ID, newTableId);

            String whereClauseTransactions = TRANSACTION_ID + " = ?";
            String[] whereArgsTransactions = {latesttransId};

            int rowsUpdatedTransactions = db.update(TRANSACTION_TABLE_NAME, valuesTransactions,
                    whereClauseTransactions, whereArgsTransactions);

            // Update TABLE_ID in TRANSACTION_HEADER table
            ContentValues valuesTransactionHeader = new ContentValues();
            valuesTransactionHeader.put(TABLE_ID, newTableId);
            valuesTransactionHeader.put(NUMBER_OF_COVERS, totalcover);

            String whereClauseTransactionHeader = TRANSACTION_TICKET_NO + " = ? AND (" +
                    TRANSACTION_STATUS + " = ? OR " +
                    TRANSACTION_STATUS + " = ?)";
            String[] whereArgsTransactionHeader = {latesttransId, "InProgress", "PRF"};

            int rowsUpdatedTransactionHeader = db.update(TRANSACTION_HEADER_TABLE_NAME, valuesTransactionHeader,
                    whereClauseTransactionHeader, whereArgsTransactionHeader);

            double totalamount=calculateTotalPriceByTransactionId(db,latesttransId);
            Log.d("totalamount", "totalamount: " + totalamount);
            double quantity=calculateTotalPriceByTransactionId(db,latesttransId);
            Log.d("quantity", "quantity: " + quantity);

           double totaltax=calculateTotalVATByTransactionId(db,latesttransId);

           Log.d("totaltax", "totaltax: " + totaltax);
            updateheaders(totalamount,totaltax,cashierid,roomid,newTableId,latesttransId);



            // Commit the transaction
            if (db.inTransaction()) {
                db.setTransactionSuccessful();
                db.endTransaction();
            }

            // Log the number of rows updated
            Log.d("updateTableIdInTransactions", "Rows Updated in TRANSACTIONS: " + rowsUpdatedTransactions);
            Log.d("updateTableIdInTransactions", "Rows Updated in TRANSACTION_HEADER: " + rowsUpdatedTransactionHeader);
        } catch (Exception e) {
            Log.e("updateTableIdInTransactions", "Error updating TABLE_ID: " + e.getMessage());
        } finally {
            // End the transaction if it's still active
            if (db.inTransaction()) {
                db.endTransaction();
            }
        }
    }
    public int getDistinctItemCountByTransactionId(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int distinctItemCount = 0;

        String query = "SELECT COUNT(DISTINCT " + ITEM_ID + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ? AND " + TRANSACTION_STATUS + " = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{transactionId, "VALID"});
            if (cursor.moveToFirst()) {
                distinctItemCount = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error retrieving distinct item count for transaction ID: " + transactionId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return distinctItemCount;
    }


    public void updateheaders(double totalAmount, double TaxtotalAmount, String Cashierid, String roomid, String tableid, String transactionIdInProgress) {
        try {
            // Get the current date and time
            String currentDate = getCurrentDate();
            String currentTime = getCurrentTime();

            // Calculate the total HT_A (priceWithoutVat) and total TTC (totalAmount)
            double totalTTC = totalAmount;
            double totaltax = TaxtotalAmount;
            double totalHT_A = totalTTC - totaltax;

            // Get the total quantity of items in the transaction
            int quantityItem = calculateTotalItemQuantity(transactionIdInProgress);

            // Save the transaction details in the TRANSACTION_HEADER table
            boolean success = updateTransactionHeader(
                    totalAmount,
                    currentDate,
                    currentTime,
                    totalHT_A,
                    totalTTC,
                    quantityItem,
                    totaltax,
                    Cashierid,
                    String.valueOf(roomid),
                    tableid
            );

            if (success) {
                Log.d("updateheader", "Transaction header updated successfully.");
            } else {
                Log.e("updateheader", "Failed to update transaction header.");
            }

        } catch (Exception e) {
            Log.e("updateheader", "Error updating transaction header", e);
        }
    }


    public String getTransactionIdFromHeader(SQLiteDatabase db, String currentTableId, String status) {
        String transactionId = null;
        Cursor cursor = null;

        try {
            // Define the query
            String query = "SELECT " + TRANSACTION_TICKET_NO +
                    " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                    " WHERE " + TABLE_ID + " = ? AND (" +
                    TRANSACTION_STATUS + " = ?)";

            // Execute the query
            cursor = db.rawQuery(query, new String[]{currentTableId, status});

            // Check if the cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                transactionId = cursor.getString(cursor.getColumnIndex(TRANSACTION_TICKET_NO));
            }

        } catch (Exception e) {
            Log.e("getTransactionIdFromHeader", "Error retrieving transaction ID: " + e.getMessage());
        } finally {
            // Close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d("gettransactionId: " ,transactionId);
        return transactionId;
    }


    public void getTotalVATAndUpdateTransactionHeader(String transactionId, double sumBeforeDisc, double sumAfterDisc,String selectedBuyerName,String selectedBuyerTAN,String selectedBuyerBRN,String selectedBuyerNIC) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Calculate total VAT
        String vatQuery = "SELECT " +
                "i." + PriceAfterDiscount + ", " +
                "i." + Price2AfterDiscount + ", " +
                "i." + Price3AfterDiscount + ", " +
                "i." + VAT + ", " +
                "t." + QUANTITY + ", " +
                "t." + TRANSACTION_TOTAL_HT_B + " " +
                "FROM " + TRANSACTION_TABLE_NAME + " t " +
                "INNER JOIN " + TABLE_NAME + " i ON t." + ITEM_ID + " = i." + _ID + " " +
                "WHERE t." + TRANSACTION_ID + " = '" + transactionId + "'";

        Cursor vatCursor = db.rawQuery(vatQuery, null);
        double totalVAT1 = 0.0;
        double totalVAT2 = 0.0;
        double totalVAT3 = 0.0;
        double totalHTB = 0.0;

        if (vatCursor != null && vatCursor.moveToFirst()) {
            do {
                double itemPrice = vatCursor.getDouble(vatCursor.getColumnIndex(PriceAfterDiscount));
                double itemPrice2 = vatCursor.getDouble(vatCursor.getColumnIndex(Price2AfterDiscount));
                double itemPrice3 = vatCursor.getDouble(vatCursor.getColumnIndex(Price3AfterDiscount));
                String vat = vatCursor.getString(vatCursor.getColumnIndex(VAT));
                int quantity = vatCursor.getInt(vatCursor.getColumnIndex(QUANTITY));
                double totalHTBItem = vatCursor.getDouble(vatCursor.getColumnIndex(TRANSACTION_TOTAL_HT_B));

                totalHTB += totalHTBItem;

                double vatRate = 0.0;
                switch (vat) {
                    case "VAT 0%":
                    case "VAT Exempted":
                        vatRate = 0.0;
                        break;
                    case "VAT 15%":
                        vatRate = 0.15;
                        break;
                }

                totalVAT1 += calculateTax(itemPrice * quantity, vatRate);
                totalVAT2 += calculateTax(itemPrice2 * quantity, vatRate);
                totalVAT3 += calculateTax(itemPrice3 * quantity, vatRate);

            } while (vatCursor.moveToNext());
            vatCursor.close();
        }

        // Calculate total number of items
        String itemQuantityQuery = "SELECT SUM(" + QUANTITY + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = '" + transactionId + "'";
        Cursor quantityCursor = db.rawQuery(itemQuantityQuery, null);
        int totalItemQuantity = 0;
        if (quantityCursor != null && quantityCursor.moveToFirst()) {
            totalItemQuantity = quantityCursor.getInt(0);
            quantityCursor.close();
        }

        // Calculate total discount
        String discountQuery = "SELECT SUM(" + TRANSACTION_TOTAL_DISCOUNT + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = '" + transactionId + "'";
        Cursor discountCursor = db.rawQuery(discountQuery, null);
        double totalDiscount = 0.0;
        if (discountCursor != null && discountCursor.moveToFirst()) {
            totalDiscount = discountCursor.getDouble(0);
            discountCursor.close();
        }

        // Update TRANSACTION_HEADER with the calculated values

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_CLIENT_NAME, selectedBuyerName);
        values.put(TRANSACTION_CLIENT_NIC, selectedBuyerNIC);
        values.put(TRANSACTION_CLIENT_BRN, selectedBuyerBRN);
        values.put(TRANSACTION_CLIENT_VAT_REG_NO, selectedBuyerTAN);
        values.put(TRANSACTION_VAT_AFTER_DISC, sumAfterDisc);
        values.put(TRANSACTION_VAT_BEFORE_DISC, sumBeforeDisc);
        values.put(TRANSACTION_TOTAL_TX_1, totalVAT1);
        values.put(TRANSACTION_TOTAL_TX_2, totalVAT2);
        values.put(TRANSACTION_TOTAL_TX_3, totalVAT3);
        values.put(TRANSACTION_ITEM_QUANTITY, totalItemQuantity);
        values.put(TRANSACTION_TOTAL_DISCOUNT, totalDiscount);
        values.put(TRANSACTION_TOTAL_HT_B, totalHTB);

        String whereClause = TRANSACTION_TICKET_NO + " = ?";
        String[] whereArgs = {transactionId};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);

        if (rowsAffected > 0) {

            Log.d("GetTotalVATAndUpdate", "Transaction header updated successfully for Transaction ID: " + transactionId);
        } else {
            Log.d("GetTotalVATAndUpdate", "Failed to update Transaction header for Transaction ID: " + transactionId);
        }

        db.close();
    }

    public double calculateTax(double totalPrice, double vatRate) {
        double totalExclusive = totalPrice / (1 + vatRate); // Removing VAT to get exclusive price
        double taxAmount = totalPrice - totalExclusive; // Calculate VAT based on exclusive price

        // Ensure that the tax amount is rounded to two decimal places
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return taxAmount;
    }



    public List<String> getRelatedItemsById(int itemId) {
        List<String> relatedItems = new ArrayList<>();

        // Define the columns you want to retrieve
        String[] columns = { "related_item", "related_item2", "related_item3", "related_item4", "related_item5" };

        // Selection criteria based on the item ID
        String selection = _ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        // Query the database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        // Check if cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Iterate through each column and add non-null values to the list
            for (String column : columns) {
                String value = cursor.getString(cursor.getColumnIndex(column));
                if (value != null) {
                    relatedItems.add(value);
                }
            }
            cursor.close();
        }

        // Close the database connection
        db.close();

        return relatedItems;
    }
    public List<String> getRelatedsupplementById(int itemId) {
        List<String> relatedItems = new ArrayList<>();

        // Define the columns you want to retrieve
        String[] columns = { "RelatedSupplements" };

        // Selection criteria based on the item ID
        String selection = _ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        // Query the database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        // Check if cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Iterate through each column and add non-null values to the list
            for (String column : columns) {
                String value = cursor.getString(cursor.getColumnIndex(column));
                if (value != null) {
                    relatedItems.add(value);
                }
            }
            cursor.close();
        }

        // Close the database connection
        db.close();

        return relatedItems;
    }
    public String getOptionNameById(int optionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {OPTION_NAME};
        String selection = OPTION_ID + "=?";
        String[] selectionArgs = {String.valueOf(optionId)};
        String optionName = null;

        Cursor cursor = db.query(OPTIONS_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            optionName = cursor.getString(cursor.getColumnIndex(OPTION_NAME));
            cursor.close();
        }
        return optionName;
    }
    public int getCatIdFromName(String catName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int catId = -1; // Default value if no matching cat name is found

        String query = "SELECT " + _ID + " FROM " + CAT_TABLE_NAME +
                " WHERE " + CatName + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{catName});

        if (cursor.moveToFirst()) {
            catId = cursor.getInt(cursor.getColumnIndex(_ID));
        }

        cursor.close();
        db.close();

        return catId;
    }

    public String getCatNameById(String id) {
        String catName = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns to be retrieved
        String[] columns = {CatName};

        // Define the selection criteria
        String selection = _ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                CAT_TABLE_NAME,   // The table to query
                columns,          // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Group by
                null,             // Having
                null              // Order by
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                catName = cursor.getString(cursor.getColumnIndexOrThrow(CatName));
            }
            cursor.close();
        }

        db.close();
        return catName;
    }

    public double getSumOfTransactionVATBeforeDiscByTransactionId(SQLiteDatabase db, String transactionId) {
        double sum = 0;
        String query = "SELECT SUM(" + TRANSACTION_VAT_BEFORE_DISC + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = '" + transactionId + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        cursor.close();

        return sum;
    }

    public double getSumOfTransactionVATAfterDiscByTransactionId(SQLiteDatabase db, String transactionId) {
        double sum = 0;
        String query = "SELECT SUM(" + TRANSACTION_VAT_AFTER_DISC + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = '" + transactionId + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        cursor.close();

        return sum;
    }


    // Add this method to your DatabaseHelper class
    public Cursor searchTables(String roomId, String searchQuery) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {ROOM_ID, TABLE_ID, MERGED, MERGED_SET_ID, TABLE_NUMBER};
        String selection = ROOM_ID + " = ? AND " + TABLE_NUMBER + " LIKE ?";
        String[] selectionArgs = {roomId, "%" + searchQuery + "%"};
        String sortOrder = TABLE_ID + " ASC";

        return db.query(TABLES, projection, selection, selectionArgs, null, null, sortOrder);
    }

    // Method to get all variants from the database
    public List<Options> getAllVariants() {
        List<Options> OptionList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                OPTION_ID,
                OPTION_NAME,

        };

        Cursor cursor = db.query(
                OPTIONS_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long optionId = cursor.getLong(cursor.getColumnIndexOrThrow(OPTION_ID));
                String optionname = cursor.getString(cursor.getColumnIndexOrThrow(OPTION_NAME));


                // Create a Variant object and add it to the list
                Options options = new Options(optionId, optionname);
                OptionList.add(options);

            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return OptionList;
    }

    public List<Options> getAllOptions1() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Options> optionsList = new ArrayList<>();

        // Query the database to get all options
        Cursor cursor = db.query(DatabaseHelper.OPTIONS_TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Options options = new Options();
                // Populate the Options object from the cursor
                options.setOptionId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.OPTION_ID)));
                options.setOptionName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.OPTION_NAME)));

                optionsList.add(options);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database connection if needed
         db.close();

        return optionsList;
    }



    public List<Options> getAllSupplements1() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Options> optionsList = new ArrayList<>();

        // Query the database to get all options
        Cursor cursor = db.query(DatabaseHelper.SUPPLEMENTS_OPTIONS_TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Options options = new Options();
                // Populate the Options object from the cursor
                options.setOptionId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SUPPLEMENT_OPTION_ID)));
                options.setOptionName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SUPPLEMENT_OPTION_NAME)));

                optionsList.add(options);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database connection if needed
        db.close();

        return optionsList;
    }
    public Cursor getAllOptions() {
        SQLiteDatabase db = this.getReadableDatabase();


        return db.query(OPTIONS_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getAllSupplements() {
        SQLiteDatabase db = this.getReadableDatabase();


        return db.query(SUPPLEMENTS_OPTIONS_TABLE_NAME , null, null, null, null, null, null);
    }




    public String getTransactionTicketNo(String roomId, String tableId) {
        String ticketNo = null;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {TRANSACTION_TICKET_NO};

        // Define the WHERE clause with your criteria
        String selection = ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND (" +
                TRANSACTION_STATUS + " = 'InProgress' OR " + TRANSACTION_STATUS + " = 'PRF')";

        // Define the selection arguments
        String[] selectionArgs = {String.valueOf(roomId), String.valueOf(tableId)};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the TRANSACTION_TICKET_NO from the cursor
            int ticketNoIndex = cursor.getColumnIndexOrThrow(TRANSACTION_TICKET_NO);
            ticketNo = cursor.getString(ticketNoIndex);

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return ticketNo;
    }

    public boolean isAtLeastOneItemSelected(String transactionId) {
        boolean atLeastOneSelected = false;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {IS_SELECTED};

        // Define the WHERE clause with your criteria
        String selection = TRANSACTION_ID + " = ? AND " + IS_SELECTED + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {transactionId, "1"};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // If there's at least one record, set atLeastOneSelected to true
            atLeastOneSelected = true;

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return atLeastOneSelected;
    }

    public boolean isAtLeastOneItemPaid(String transactionId) {
        boolean atLeastOnePaid = false;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {IS_PAID};

        // Define the WHERE clause with your criteria
        String selection = TRANSACTION_ID + " = ? AND " + IS_PAID + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {transactionId, "1"};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // If there's at least one record, set atLeastOnePaid to true
            atLeastOnePaid = true;

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return atLeastOnePaid;
    }

    // Method to check if all IS_PAID values are 1 for a given TRANSACTION_ID
    public boolean areAllItemsPaid(String transactionId) {
        boolean allPaid = true;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {IS_PAID};

        // Define the WHERE clause with your criteria
        String selection = TRANSACTION_ID + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {String.valueOf(transactionId)};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the IS_PAID values from the cursor and check if any is 0
            int isPaidIndex = cursor.getColumnIndexOrThrow(IS_PAID);
            do {
                int isPaid = cursor.getInt(isPaidIndex);
                if (isPaid != 1) {
                    allPaid = false;
                    break; // No need to continue checking if one item is not paid
                }
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return allPaid;
    }
    public boolean areAllItemsNotPaid(String transactionId) {
        boolean allNotPaid = true;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {IS_PAID};

        // Define the WHERE clause with your criteria
        String selection = TRANSACTION_ID + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {String.valueOf(transactionId)};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the IS_PAID values from the cursor and check if any is 1
            int isPaidIndex = cursor.getColumnIndexOrThrow(IS_PAID);
            do {
                int isPaid = cursor.getInt(isPaidIndex);
                if (isPaid == 1) {
                    allNotPaid = false;
                    break; // No need to continue checking if one item is paid
                }
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return allNotPaid;
    }

    public boolean areAllItemsNotSelected(String transactionId) {
        boolean allNotSelected = true;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {IS_SELECTED};

        // Define the WHERE clause with your criteria
        String selection = TRANSACTION_ID + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {String.valueOf(transactionId)};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the IS_SELECTED values from the cursor and check if any is 1
            int isSelectedIndex = cursor.getColumnIndexOrThrow(IS_SELECTED);
            do {
                int isSelected = cursor.getInt(isSelectedIndex);
                if (isSelected == 1) {
                    allNotSelected = false;
                    break; // No need to continue checking if one item is selected
                }
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return allNotSelected;
    }
    public List<String[]> getRoomAndTableIdsWithInProgressStatus() {
        SQLiteDatabase db = getReadableDatabase();
        List<String[]> roomAndTableIdsList = new ArrayList<>();

        String query = "SELECT " + ROOM_ID + ", " + TABLE_ID + " FROM " +
                TRANSACTION_HEADER_TABLE_NAME + " WHERE " +
                TRANSACTION_STATUS + " = ?";

        String[] selectionArgs = {"InProgress"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        while (cursor.moveToNext()) {
            String roomId = cursor.getString(cursor.getColumnIndex(ROOM_ID));
            String tableId = cursor.getString(cursor.getColumnIndex(TABLE_ID));
            String[] roomAndTableIds = {roomId, tableId};
            roomAndTableIdsList.add(roomAndTableIds);
        }

        cursor.close();
        db.close();

        return roomAndTableIdsList;
    }


    public List<String[]> getRoomAndTableIdsWithPRFStatus() {
        SQLiteDatabase db = getReadableDatabase();
        List<String[]> roomAndTableIdsList = new ArrayList<>();

        String query = "SELECT " + ROOM_ID + ", " + TABLE_ID + " FROM " +
                TRANSACTION_HEADER_TABLE_NAME + " WHERE " + TRANSACTION_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"PRF"});

        while (cursor.moveToNext()) {
            String roomId = cursor.getString(cursor.getColumnIndex(ROOM_ID));
            String tableId = cursor.getString(cursor.getColumnIndex(TABLE_ID));
            String[] roomAndTableIds = {roomId, tableId};
            roomAndTableIdsList.add(roomAndTableIds);
        }

        cursor.close();
        db.close();

        return roomAndTableIdsList;
    }

    public String getInProgressTransactionTicketNo() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct an SQL query to fetch the TRANSACTION_TICKET_NO
        String selectQuery = "SELECT " + TRANSACTION_TICKET_NO + " FROM " +
                TRANSACTION_HEADER_TABLE_NAME + " WHERE " + TRANSACTION_STATUS + " = 'InProgress'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        String transactionTicketNo = null;

        if (cursor != null && cursor.moveToFirst()) {
            transactionTicketNo = cursor.getString(cursor.getColumnIndex(TRANSACTION_TICKET_NO));
            cursor.close();
        }

        return transactionTicketNo;
    }

    // Method to get status and seat count based on roomId and tableId
    public Rooms getRoomDetails(String roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Rooms roomDetails = null;

        String query = "SELECT " +
                SEAT_COUNT + ", " +
                STATUS +
                " FROM " + TABLES +
                " WHERE " + ROOM_ID + " = ? AND " +
                TABLE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{roomId});

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve details from the cursor
            int seatCount = cursor.getInt(cursor.getColumnIndex(SEAT_COUNT));
            String status = cursor.getString(cursor.getColumnIndex(STATUS));

            // Create a RoomDetails object
            roomDetails = new Rooms( seatCount, status);

            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        return roomDetails;
    }

    public String getNextRoomId(String currentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String nextId = null;

        // You may need to adjust the query based on the structure of your IDs
        String query = "SELECT " + ID + " FROM " + ROOMS + " WHERE " + ID + " > ? ORDER BY " + ID + " LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{currentId});

        if (cursor.moveToFirst()) {
            nextId = cursor.getString(cursor.getColumnIndex(ID));
        }

        cursor.close();
        db.close();

        // If no next ID found, get the first ID in the table
        if (nextId == null) {
            nextId = getFirstRoomId();
        }

        // If still no next ID found, get the absolute first ID in the table
        if (nextId == null) {
            nextId = getAbsoluteFirstRoomId();
        }

        return nextId;
    }

    private String getFirstRoomId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String firstId = null;

        // You may need to adjust the query based on the structure of your IDs
        String query = "SELECT " + ID + " FROM " + ROOMS + " ORDER BY " + ID + " LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            firstId = cursor.getString(cursor.getColumnIndex(ID));
        }

        cursor.close();
        db.close();

        return firstId;
    }

    private String getAbsoluteFirstRoomId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String absoluteFirstId = null;

        // You may need to adjust the query based on the structure of your IDs
        String query = "SELECT " + ID + " FROM " + ROOMS + " ORDER BY " + ID + " ASC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            absoluteFirstId = cursor.getString(cursor.getColumnIndex(ID));
        }

        cursor.close();
        db.close();

        return absoluteFirstId;
    }


    public int getMaxRoomId() {
        SQLiteDatabase db = this.getReadableDatabase();
        int maxId = 0;

        Cursor cursor = db.rawQuery("SELECT MAX(" + ID + ") AS max_id FROM " + ROOMS, null);

        if (cursor != null && cursor.moveToFirst()) {
            maxId = cursor.getInt(cursor.getColumnIndex("max_id"));
            cursor.close();
        }

        return maxId;
    }

    public Cursor getRoomsForId(int roomId) {
        if (roomId == 0) {
            return getAllRooms();
        } else {
            SQLiteDatabase db = this.getReadableDatabase();

            String[] projection = {
                    DatabaseHelper.ID,
                    DatabaseHelper.ROOM_NAME,
                    DatabaseHelper.TABLE_COUNT,
                    // Add other columns you want to retrieve
            };

            String selection = DatabaseHelper.ID + " = ?";
            String[] selectionArgs = { String.valueOf(roomId) };

            Cursor cursor = db.query(
                    DatabaseHelper.ROOMS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            return cursor;
        }
    }


    public String getRoomNameForId(String roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String roomName = null;

        Cursor cursor = db.query(ROOMS, new String[]{ROOM_NAME}, ID + " = ?", new String[]{roomId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            roomName = cursor.getString(cursor.getColumnIndex(ROOM_NAME));
            cursor.close();
        }

        return roomName;
    }

    // Update room details
    public boolean updateRoomDetails(String roomId, String newRoomName, int newTableNumber, int newSeatCount, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NUMBER, newTableNumber);
        contentValues.put(SEAT_COUNT, newSeatCount);
        contentValues.put(STATUS, newStatus);

        // Update the record with the specified roomId
        int numRowsAffected = db.update(TABLE_NAME, contentValues, TABLE_ID + " = ?", new String[]{roomId});

        // Close the database connection
        db.close();

        // Check if the update was successful
        return numRowsAffected > 0;
    }

    public void updateTransactionBasedOnInProgressTicketNo(String transactionTicketNo, String pricelevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Determine which PriceAfterDiscount field to use based on the selected price level
        String priceAfterDiscountColumn;
        if ("Price Level 1".equals(pricelevel)) {
            priceAfterDiscountColumn = PriceAfterDiscount;
        } else if ("Price Level 2".equals(pricelevel)) {
            priceAfterDiscountColumn = Price2AfterDiscount;
        } else if ("Price Level 3".equals(pricelevel)) {
            priceAfterDiscountColumn = Price3AfterDiscount;
        } else {
            // Handle default case (e.g., use PriceAfterDiscount)
            priceAfterDiscountColumn = PriceAfterDiscount;
        }

        // Calculate the tax based on your tax calculation logic
        double calculatedTax = calculateTax(10.0, VAT); // Replace with your tax calculation logic

        // Construct an SQL query to update the TRANSACTION table
        String updateQuery = "UPDATE " + TRANSACTION_TABLE_NAME + " SET " +
                TOTAL_PRICE + " = (SELECT " + priceAfterDiscountColumn + " * QUANTITY FROM " + TABLE_NAME +
                " WHERE " + _ID + " = " + TRANSACTION_TABLE_NAME + "." + ITEM_ID + "), " +
                VAT + " = (SELECT Quantity * ? FROM " + TABLE_NAME +
                " WHERE " + _ID + " = " + TRANSACTION_TABLE_NAME + "." + ITEM_ID + "), " +
                VAT_Type + " = (SELECT " + VAT + " FROM " + TABLE_NAME +
                " WHERE " + _ID + " = " + TRANSACTION_TABLE_NAME + "." + ITEM_ID + ") " +
                "WHERE " + TRANSACTION_ID + " IN " +
                "(SELECT " + TRANSACTION_ID + " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = 'InProgress' AND " + TRANSACTION_TICKET_NO + " = ?)";

        db.execSQL(updateQuery, new Object[]{calculatedTax, transactionTicketNo});
    }

    // Update IS_SELECTED field for a specific item

    public double calculateTotalVATByTransactionId(SQLiteDatabase db, String transactionId) {
        double totalVATSum = 0.0;

        try {
            // Define the columns to be retrieved
            String[] columns = {VAT};

            // Define the selection and selection arguments
            String selection = TRANSACTION_ID + " = ?";
            String[] selectionArgs = {transactionId};

            // Execute the query to calculate the sum of VAT
            Cursor cursor = db.query(
                    TRANSACTION_TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            // Iterate through the cursor and sum up the VAT values
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    double vat = cursor.getDouble(cursor.getColumnIndex(VAT));
                    totalVATSum += vat;
                }
                cursor.close();
            }

            // Log the total sum
            Log.d("calculateTotalVATByTransactionId", "Total VAT Sum for Transaction ID " + transactionId + ": " + totalVATSum);

        } catch (Exception e) {
            Log.e("calculateTotalVATByTransactionId", "Error calculating total VAT by transaction id", e);
        }

        return totalVATSum;
    }

    public double getTotalAmount(String transactionId, String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalAmount = 0.0;
        String query = "SELECT SUM(" + TOTAL_PRICE + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ? AND " +
                ROOM_ID + " = ? AND " + TABLE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { transactionId, roomId, tableId });

        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalAmount;
    }


    public double calculateTotalPriceByTransactionId(SQLiteDatabase db, String transactionId) {
        double totalPriceSum = 0.0;

        try {
            // Define the columns to be retrieved
            String[] columns = {TOTAL_PRICE};

            // Define the selection and selection arguments
            String selection = TRANSACTION_ID + " = ?";
            String[] selectionArgs = {transactionId};

            // Execute the query to calculate the sum of TOTAL_PRICE
            Cursor cursor = db.query(
                    TRANSACTION_TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            // Iterate through the cursor and sum up the TOTAL_PRICE values
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    double price = cursor.getDouble(cursor.getColumnIndex(TOTAL_PRICE));
                    totalPriceSum += price;
                }
                cursor.close();
            }

            // Log the total sum
            Log.d("calculateTotalPriceByTransactionId", "Total Price Sum for Transaction ID " + transactionId + ": " + totalPriceSum);

        } catch (Exception e) {
            Log.e("calculateTotalPriceByTransactionId", "Error calculating total price by transaction id", e);
        }

        return totalPriceSum;
    }


    public boolean updateItemPaid(long itemId, boolean isPaid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_PAID, isPaid ? 1 : 0);

        int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, _ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();

        boolean isUpdateSuccessful = rowsAffected > 0;

        if (isUpdateSuccessful) {
            Log.d("DatabaseUpdate", "Item with ID " + itemId + " payment status updated successfully.");
        } else {
            Log.e("DatabaseUpdate", "Failed to update payment status for item with ID " + itemId);
        }

        return isUpdateSuccessful;
    }

    public double calculateTax(double unitPrice, String vatType) {
        double taxAmount = 0.0;

        if ("VAT 15%".equals(vatType)) {
            // To find the tax amount, first calculate the portion of the price that is VAT
            double vatPortion = unitPrice - (unitPrice / 1.15);

            // Then, round it to two decimal places to handle precision issues
            taxAmount = Math.round(vatPortion * 100.0) / 100.0;
        } // No need for an 'else' condition since taxAmount is already initialized to 0.0

        return taxAmount;
    }



    // Method to get the sum of PriceAfterDiscount where TransactionStatus is 'InProgress'
    public double getSumPriceAfterDiscountInProgress() {
        SQLiteDatabase db = this.getWritableDatabase();
        double sum = 0;

        String query = "SELECT SUM(" + PriceAfterDiscount + ") FROM " +
                TRANSACTION_TABLE_NAME + " t " +
                "INNER JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h " +
                "ON t." + TRANSACTION_ID + " = h." + TRANSACTION_ID +
                " WHERE h." + TRANSACTION_STATUS + " = 'InProgress'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return sum;
    }
    public  double calculateTotalAmount(String roomid,String tableid) {
        String statusType= getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
        Cursor cursor = getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);

        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }


        return totalAmount;
    }
    public  double calculateTotalAmounts(String roomid,String tableid) {
        Cursor cursor = getAllSplittedInProgressTransactions(roomid,tableid);
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
            // If totalAmount is still 0, use getAllInProgressTransactions
            if (totalAmount == 0.0) {
                cursor = getAllSplittedInProgressTransactions(roomid, tableid);

                if (cursor != null && cursor.moveToFirst()) {
                     totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
                    do {
                        double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                        totalAmount += totalPrice;
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public  double calculateTotalAmountsNotSelectedNotPaid(String transid,String roomid,String tableid) {
        Cursor cursor = getSplittedInProgressNotSelectedNotPaidTransactions(transid,roomid,tableid);
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
            Log.d("totalPriceColumnIndex", String.valueOf(totalPriceColumnIndex));
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
            // If totalAmount is still 0, use getAllInProgressTransactions
            if (totalAmount == 0.0) {
                cursor = getSplittedInProgressNotSelectedNotPaidTransactions(transid,roomid, tableid);

                if (cursor != null && cursor.moveToFirst()) {
                    totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
                    do {
                        double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                        totalAmount += totalPrice;
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public  double calculateTotalTaxAmounts(String roomid,String tableid) {
        Cursor cursor = getAllSplittedInProgressTransactions(roomid,tableid);
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.VAT);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public  double calculateTotalTaxAmountsNotSelectedNotPaid(String transid,String roomid,String tableid) {
        Cursor cursor = getSplittedInProgressNotSelectedNotPaidTransactions(transid,roomid,tableid);
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.VAT);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }

    public double getVAT(String transactionId, String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double vatAmount = 0.0;
        String query = "SELECT SUM(" + VAT + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ? AND " +
                ROOM_ID + " = ? AND " + TABLE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { transactionId, roomId, tableId });

        if (cursor.moveToFirst()) {
            vatAmount = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return vatAmount;
    }

    public  double calculateTotalTaxAmount(String roomid,String tableid) {
        String statusType= getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId=getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
        Cursor cursor =getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.VAT);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public String getNatureById(long itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { Nature };
        String selection = _ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String nature = null;
        if (cursor.moveToFirst()) {
            nature = cursor.getString(cursor.getColumnIndex(Nature));
        }

        cursor.close();
        return nature;
    }


    public String getCurrencyById(long itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { Currency };
        String selection = _ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String nature = null;
        if (cursor.moveToFirst()) {
            nature = cursor.getString(cursor.getColumnIndex(Currency));
        }

        cursor.close();
        return nature;
    }


    // Method to insert a counting report into the table
    public void insertCountingReport(double totalizerTotal, double totalValue, double difference, int cashierId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COUNTING_REPORT_TOTALIZER_TOTAL, totalizerTotal);
        values.put(COUNTING_REPORT_TOTAL_VALUE, totalValue);
        values.put(COUNTING_REPORT_DIFFERENCE, difference);
        values.put(COUNTING_REPORT_CASHIER_ID, cashierId);

        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String datetime = dateFormat.format(date);

        // Insert the current date and time
        values.put(COUNTING_REPORT_DATETIME, datetime);

        long newRowId = db.insert(COUNTING_REPORT_TABLE_NAME, null, values);
        db.close();
    }

    public double getTotalizerSumForCurrentDate(String currentDate) {
        double totalizerSum = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT SUM(" + FINANCIAL_COLUMN_TOTALIZER + ") FROM " + FINANCIAL_TABLE_NAME +
                " WHERE " + FINANCIAL_COLUMN_DATETIME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{currentDate});

        if (cursor.moveToFirst()) {
            totalizerSum = cursor.getDouble(0);
        } else {
            // Log a message or print to console to help with debugging
            Log.d("totalizer", "No records found for the date: " + currentDate);
        }

        cursor.close();
        db.close();

        return totalizerSum;
    }



    private void addDefaultItem(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_METHOD, DEFAULT_PAYMENT_METHOD);
        values.put(COLUMN_CASHOR_id , DEFAULT_CASHOR_ID);
        values.put(DateCreated , DEFAULT_DATE_CREATED);
        values.put(LastModified , DEFAULT_LAST_MODIFIED);
        values.put(COLUMN_QR_CODE_NUM, DEFAULT_QR_CODE_NUM);

        long result = db.insert(TABLE_NAME_PAYMENTBYQY, null, values);
        if (result == -1) {
            Log.e(TAG, "Error inserting default item into the database");
        }
    }
    private void addDefaultCat(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CatName, DEFAULT_CatName);
        values.put(Color , DEFAULT_Color);


        long result = db.insert(CAT_TABLE_NAME, null, values);
        if (result == -1) {
            Log.e(TAG, "Error inserting default item into the database");
        }
    }

    private void addDefaultMenuCat(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(CatName, DEFAULT_MenuRepas);
        values.put(Color , DEFAULT_MenuColor);


        long result = db.insert(CAT_TABLE_NAME, null, values);
        if (result == -1) {
            Log.e(TAG, "Error inserting default item into the database");
        }
    }
    public boolean isValidBarcode(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve (in this case, we only need COUPON_CODE)
        String[] projection = { COUPON_CODE };

        // Define the selection criteria (COUPON_CODE should match the provided barcode)
        String selection = COUPON_CODE + " = ?";
        String[] selectionArgs = { barcode };

        // Execute the query
        Cursor cursor = db.query(
                COUPON_TABLE_NAME,   // The table to query
                projection,          // The columns to return
                selection,           // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,                // Don't group the rows
                null,                // Don't filter by row groups
                null                 // The sort order
        );

        // Check if the cursor has any rows (i.e., a coupon with the provided barcode exists)
        boolean isValid = cursor.moveToFirst();

        // Close the cursor and the database
        cursor.close();
        db.close();

        return isValid;
    }
    public int getactualShiftNumber() {
        int shiftNumber = 1; // Default value indicating the starting shift number
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to retrieve the most recent transaction date
        String recentDateQuery = "SELECT MAX(" + TRANSACTION_DATE_CREATED + ") FROM " + TRANSACTION_HEADER_TABLE_NAME;

        Cursor cursor = db.rawQuery(recentDateQuery, null);

        String recentDate = null;
        if (cursor.moveToFirst()) {
            recentDate = cursor.getString(0);
        }
        cursor.close();

        // Get the current date in the same format as stored in TRANSACTION_DATE_CREATED
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Check if the most recent transaction date is different from the current date
        if (recentDate != null && !recentDate.equals(currentDate)) {
            shiftNumber = 1; // Reset shift number if dates are different
        } else {
            // Define the query to retrieve the maximum shift number
            String query = "SELECT MAX(" + TRANSACTION_SHIFT_NUMBER + ") FROM " + TRANSACTION_HEADER_TABLE_NAME;

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                // Get the value from the first column
                int maxShiftNumber = cursor.getInt(0);
                if (maxShiftNumber > 0) {
                    shiftNumber = maxShiftNumber ;
                }
            }
            cursor.close();
        }

        db.close();

        Log.d("getCurrentShiftNumber", "Current shift number: " + shiftNumber);

        return shiftNumber;
    }
    public int getCurrentShiftNumber() {
        int shiftNumber = 1; // Default value indicating the starting shift number
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to retrieve the most recent transaction date
        String recentDateQuery = "SELECT MAX(" + TRANSACTION_DATE_CREATED + ") FROM " + TRANSACTION_HEADER_TABLE_NAME;

        Cursor cursor = db.rawQuery(recentDateQuery, null);

        String recentDate = null;
        if (cursor.moveToFirst()) {
            recentDate = cursor.getString(0);
        }
        cursor.close();

        // Get the current date in the same format as stored in TRANSACTION_DATE_CREATED
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Check if the most recent transaction date is different from the current date
        if (recentDate != null && !recentDate.equals(currentDate)) {
            shiftNumber = 1; // Reset shift number if dates are different
        } else {
            // Define the query to retrieve the maximum shift number
            String query = "SELECT MAX(" + TRANSACTION_SHIFT_NUMBER + ") FROM " + TRANSACTION_HEADER_TABLE_NAME;

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                // Get the value from the first column
                int maxShiftNumber = cursor.getInt(0);
                if (maxShiftNumber > 0) {
                    shiftNumber = maxShiftNumber + 1; // Increment the shift number
                }
            }
            cursor.close();
        }

        db.close();

        Log.d("getCurrentShiftNumber", "Current shift number: " + shiftNumber);

        return shiftNumber;
    }






    public List<Integer> getShiftNumbersForToday() {
        List<Integer> shiftNumbers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String query = "SELECT DISTINCT " + TRANSACTION_SHIFT_NUMBER + " FROM " + TRANSACTION_HEADER_TABLE_NAME + " WHERE " + TRANSACTION_DATE_CREATED + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{currentDate});

        if (cursor.moveToFirst()) {
            do {
                int shiftNumber = cursor.getInt(cursor.getColumnIndex(TRANSACTION_SHIFT_NUMBER));
                shiftNumbers.add(shiftNumber);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return shiftNumbers;
    }

    public String getNICByTANandBRN(String tan) {
        SQLiteDatabase db = this.getReadableDatabase();
        String nic = null;

        // Define the columns you want to retrieve (in this case, only NIC)
        String[] projection = {BUYER_NIC};

        // Define the WHERE clause with placeholders for TAN and BRN
        String selection = BUYER_TAN + " = ?";

        // Specify the values to replace the placeholders
        String[] selectionArgs = {tan};

        // Query the database
        Cursor cursor = db.query(
                BUYER_TABLE_NAME,       // The table to query
                projection,             // The columns to return
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // Don't group the rows
                null,                   // Don't filter by row groups
                null                    // The sort order
        );

        // Check if there is a result
        if (cursor != null && cursor.moveToFirst()) {
            // Get the NIC from the cursor
            nic = cursor.getString(cursor.getColumnIndexOrThrow(BUYER_NIC));
            cursor.close(); // Close the cursor when done
        }

        // Close the database connection
        db.close();

        return nic; // Return the NIC or null if not found
    }
    private void addDefaultSupplement(SQLiteDatabase db, String paymentMethod, String cashOrId) {
        ContentValues values = new ContentValues();
        values.put(CatName, paymentMethod);
        values.put(Color, "#EBFFD0");


        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e(TAG, "Error inserting default item into the database");
        }
    }
    private void addDefaultPaymentMethod(SQLiteDatabase db, String paymentMethod, String cashOrId) {
        ContentValues values = new ContentValues();
        values.put(PAYMENT_METHOD_COLUMN_NAME, paymentMethod);
        values.put(PAYMENT_METHOD_COLUMN_CASHOR_ID, cashOrId);
        values.put(PAYMENT_METHOD_COLUMN_DATE_CREATED, getCurrentDateTime());
        values.put(PAYMENT_METHOD_COLUMN_LAST_MODIFIED, getCurrentDateTime());

        long result = db.insert(PAYMENT_METHOD_TABLE_NAME, null, values);
        if (result == -1) {
            Log.e(TAG, "Error inserting default item into the database");
        }
    }


    public long insertCoupon(Coupon couponData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Replace these lines with appropriate getters from your CouponData class
        values.put(COUPON_CODE, couponData.getCode());
        values.put(COUPON_STATUS, couponData.getStatus());
        values.put(COUPON_START_DATE, couponData.getStartDate());
        values.put(COUPON_END_DATE, couponData.getEndDate());
        values.put(COUPON_CASHIER_ID, couponData.getCashierId());
        values.put(COUPON_DATE_CREATED, couponData.getDateCreated());
        values.put(COUPON_TIME_CREATED, couponData.getTimeCreated());
        values.put(COUPON_DISCOUNT, couponData.getDiscount());

        long newRowId = db.insert(COUPON_TABLE_NAME, null, values);

        // No need to close the database connection here; let it be managed elsewhere

        return newRowId; // Return the newly inserted row ID or -1 if an error occurred
    }
    public boolean checkItemExists(String transactionId, String tableId, String roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exists = false;

        // Define the query to select the transaction status
        String statusQuery = "SELECT " + TRANSACTION_STATUS +
                " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_TICKET_NO + " = ?";

        // Use try-with-resources to ensure the cursor is closed after use
        try (Cursor statusCursor = db.rawQuery(statusQuery, new String[]{transactionId})) {
            if (statusCursor.moveToFirst()) {
                String status = statusCursor.getString(statusCursor.getColumnIndex(TRANSACTION_STATUS));

                // Check if the status is one of the allowed values
                if ("PRF".equals(status) || "InProgress".equals(status)) {
                    // Define the query to check for the item existence
                    String existsQuery = "SELECT 1 FROM " + TRANSACTION_TABLE_NAME +
                            " WHERE " + TRANSACTION_ID + " = ?" +
                            " AND " + TABLE_ID + " = ?" +
                            " AND " + ROOM_ID + " = ?";

                    // Use try-with-resources to ensure the cursor is closed after use
                    try (Cursor cursor = db.rawQuery(existsQuery, new String[]{transactionId, tableId, roomId})) {
                        // Check if any result was returned
                        exists = cursor.moveToFirst();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error checking item existence", e);
        }
        // Log whether the item exists or not
        Log.d("ItemExistence", "Transaction ID: " + transactionId + ", Table ID: " + tableId + ", Room ID: " + roomId + ", Exists: " + exists);

        return exists;
    }



    public void setTransactionSentToKitchen(String transactionId, String barcode) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_SentToKitchen, 1); // Set SentToKitchen as 1

        // Define the WHERE clause and arguments
        String whereClause;
        String[] whereArgs;

        if (barcode == null) {
            // If barcode is null, update based only on transaction ID
            whereClause = TRANSACTION_ID + " = ?";
            whereArgs = new String[]{ transactionId };
        } else {
            // If barcode is not null, update based on transaction ID and barcode
            whereClause = TRANSACTION_ID + " = ? AND " + TRANSACTION_BARCODE + " = ?";
            whereArgs = new String[]{ transactionId, barcode };
        }

        // Perform the update operation
        int rowsUpdated = db.update(TRANSACTION_TABLE_NAME, values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            // Log success message
            Log.d("UpdateTransaction", "Transaction SentToKitchen updated successfully for transaction ID: " + transactionId + (barcode != null ? " and barcode: " + barcode : ""));
        } else {
            // Log error message
            Log.e("UpdateTransactionFailed", "Failed to update transaction SentToKitchen for transaction ID: " + transactionId + (barcode != null ? " and barcode: " + barcode : "") + ". No rows updated.");
        }
    }
    public void updateSettlementInvoiceId(String oldInvoiceId, String newInvoiceId, String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SETTLEMENT_INVOICE_ID, newInvoiceId);

        // Define the WHERE clause
        String whereClause = SETTLEMENT_INVOICE_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] whereArgs = {oldInvoiceId, roomId, tableId};

        int rowsAffected = db.update(
                INVOICE_SETTLEMENT_TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (rowsAffected > 0) {
            Log.d("Database Update", "Settlement Invoice ID updated successfully. Old ID: " + oldInvoiceId + ", New ID: " + newInvoiceId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId);
        } else {
            Log.e("Database Update", "Failed to update settlement invoice ID. No matching rows found for ID: " + oldInvoiceId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId);
        }

        db.close();
    }

    public List<SettlementItem> getSettlementItemsByTransactionId(String transactionId) {
        List<SettlementItem> settlementItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + SETTLEMENT_PAYMENT_NAME + ", " + SETTLEMENT_AMOUNT +
                " FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " WHERE " + SETTLEMENT_INVOICE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{transactionId});

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndexOrThrow(SETTLEMENT_PAYMENT_NAME));
                double settlementAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(SETTLEMENT_AMOUNT));
                settlementItems.add(new SettlementItem(paymentName, settlementAmount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return settlementItems;
    }


    public void updateTransactionComment(String transactionId,  String comment,String itemId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_COMMENT, comment); // Assuming TRANSACTION_COMMENT is the column name for the comment

        // Define the WHERE clause to update the row for the given transaction ID and item ID
        String whereClause = TRANSACTION_ID + " = ? AND " + ITEM_ID + " = ?";
        String[] whereArgs = { transactionId, itemId };

        // Perform the update operation
        int rowsUpdated = db.update(TRANSACTION_TABLE_NAME, values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            // Log success message
            Log.d("UpdateTransaction", "Transaction comment updated successfully for transaction ID: " + transactionId + " and item ID: " + itemId);
        } else {
            // Log error message
            Log.e("UpdateTransactionFailed", "Failed to update transaction comment for transaction ID: " + transactionId + " and item ID: " + itemId + ". No rows updated.");
        }
    }





    public long insertTransaction(String related3optionid,int itemId, String Barcode, float weight, double taxwithoutdiscount, double totaldiscbeforetax, String shopnumber, String catId, String transactionId, String transactionDate, int quantity,
                                  double totalPrice, double vat, String longDescription, double unitPrice, double priceWithoutVat,
                                  String vatType, String posNum, String Nature, String ItemCode, String Currency, String taxCode,
                                  double priceAfterDiscount, double totalDiscount, String roomid, String tabeid, int isPaid) {

        SQLiteDatabase db = getWritableDatabase();
        if (db != null && db.isOpen()) {
            Log.d("open", "sucess");
        }else{
            Log.d("closed", "Failed to insert transaction");
        }
        ContentValues values = new ContentValues();

        values.put(ITEM_ID, itemId);
        values.put(TRANSACTION_ID, transactionId);
        values.put(RELATED_Option_ID, related3optionid);
        values.put(TRANSACTION_DATE, transactionDate);
        values.put(TRANSACTION_BARCODE, Barcode);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, roundUp(totalPrice, 2));
        values.put(VAT, roundUp(vat, 2));
        values.put(TRANSACTION_VAT_BEFORE_DISC, roundUp(taxwithoutdiscount, 2));
        values.put(TRANSACTION_SHOP_NO, shopnumber);
        values.put(LongDescription, longDescription);
        values.put(TRANSACTION_DESCRIPTION, longDescription);
        values.put(TRANSACTION_UNIT_PRICE, roundUp(unitPrice, 2));
        values.put(TRANSACTION_TOTAL_HT_A, roundUp(priceWithoutVat, 2));
        values.put(TRANSACTION_TOTAL_HT_B, roundUp(totaldiscbeforetax, 2));
        values.put(TRANSACTION_TOTAL_TTC, roundUp(totalPrice, 2));
        values.put(VAT_Type, vatType);
        values.put(TRANSACTION_TERMINAL_NO, posNum);
        values.put(TRANSACTION_NATURE, Nature);
        values.put(TRANSACTION_ITEM_CODE, ItemCode);
        values.put(TRANSACTION_CURRENCY, Currency);
        values.put(TRANSACTION_TAX_CODE, taxCode);
        values.put(PriceAfterDiscount, roundUp(totalPrice, 2));
        values.put(TRANSACTION_TYPE_TAX, taxCode);
        values.put(TRANSACTION_IS_TAXABLE, taxCode.equals("TC01") ? 1 : 0); // If taxCode is "TC01", set to 1, otherwise set to 0
        values.put(TRANSACTION_TOTALIZER, "SALES");
        values.put(TRANSACTION_FAMILLE, catId);
        values.put(TRANSACTION_VAT_AFTER_DISC, roundUp(vat, 2));

// Get current date and time
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

// Format date and time
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);

        values.put(TRANSACTION_DATE_TRANSACTION, formattedDate);
        values.put(TRANSACTION_TIME_TRANSACTION, formattedTime);
        values.put(TRANSACTION_DATE_CREATED, formattedDate);
        values.put(TRANSACTION_DATE_MODIFIED, formattedDate);
        values.put(TRANSACTION_TIME_CREATED, formattedTime);
        values.put(TRANSACTION_TIME_MODIFIED, formattedTime);
        values.put(TRANSACTION_CODE, transactionId);
        values.put(TRANSACTION_QUANTITY, quantity);
        values.put(TRANSACTION_WEIGHTS, weight);
        values.put(ROOM_ID, roomid);
        values.put(TABLE_ID, tabeid);
        values.put(TRANSACTION_TOTAL_DISCOUNT, roundUp(totalDiscount, 2));
        values.put(TRANSACTION_DISCOUNT, roundUp(totalDiscount, 2));
        values.put(TRANSACTION_STATUS, "VALID");
        values.put(IS_PAID, isPaid); // Add IS_PAID to the ContentValues

        long result = -1;
        try {
            result = db.insert(TRANSACTION_TABLE_NAME, null, values);
            if (result != -1) {
                Log.d("INSERT_SUCCESS", "Transaction inserted successfully with ID: " + result);
            } else {
                Log.d("INSERT_FAILURE", "Failed to insert transaction");
            }
        } catch (SQLException e) {
            Log.e("INSERT_ERROR_Transaction", e.getMessage());
        } finally {
            db.close();
        }
        return result;
    }

    private double roundUp(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.UP);
        return bd.doubleValue();
    }
    public boolean updateTransactionBuyerInfo(String transactionId, String invoiceRefIdentifier, String name, String tan, String compname, String Address, String brn, String cashierId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the transaction ID already exists
        String query = "SELECT COUNT(*) FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_TICKET_NO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{transactionId});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_CLIENT_NAME, name);
        values.put(TRANSACTION_INVOICE_REF,invoiceRefIdentifier);
        values.put(TRANSACTION_CLIENT_BRN, brn);
        values.put(TRANSACTION_CLIENT_VAT_REG_NO, tan);
        values.put(TRANSACTION_CLIENT_OTHER_NAME, compname);
        values.put(TRANSACTION_CLIENT_ADR1, Address);
        values.put(TRANSACTION_CASHIER_CODE, cashierId);


        if (count > 0) {
            // Transaction ID already exists, update the row
            int result = db.update(TRANSACTION_HEADER_TABLE_NAME, values, TRANSACTION_TICKET_NO + " = ?", new String[]{transactionId});
            return result > 0;
        } else {
            // Transaction ID does not exist, insert a new row
            long result = db.insert(TRANSACTION_HEADER_TABLE_NAME, null, values);
            return result != -1;
        }
    }
    public boolean updateOrderNumber(String transactionId, int newOrderNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NUMBER_OF_COVERS, newOrderNumber); // Replace ORDER_NUMBER_COLUMN with your actual column name

        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionId };

        try {
            int rowsAffected = db.update(
                    TRANSACTION_HEADER_TABLE_NAME,   // Table name
                    values,                         // Values to update
                    selection,                      // WHERE clause
                    selectionArgs                   // WHERE clause arguments
            );

            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("UpdateOrderNumber", "Error updating order number: " + e.getMessage(), e);
            return false;
        } finally {
            db.close();
        }
    }

    public boolean saveTransactionHeader(String Shopnumber, String transactionId, double totalAmount, String currentDate,
                                         String currentTime, double totalHT_A, double totalTTC, double taxAmount, int quantityItem, String cashierId, String transactionStatus, String posNum, String MRAMETHOD,String roomid,String tableid,double sumBeforeDisc,double sumAfterDisc) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the transaction ID already exists
        String query = "SELECT COUNT(*) FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_TICKET_NO + " = ?"+
                " AND " + ROOM_ID + " = ?" +
                " AND " + TABLE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{transactionId, roomid, tableid});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            // Transaction ID already exists, do not insert
            return false;
        }
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String status = preferences.getString(STATUS_KEY, "default_value");
         preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int servings = preferences.getInt("servings", 0);
        ContentValues values = new ContentValues();
        values.put(ORDERTYPE, status);
        //values.put(NUMBER_OF_COVERS, covernumber);
        values.put(TRANSACTION_SHOP_NO, Shopnumber);
        values.put(TRANSACTION_TICKET_NO, transactionId);
        values.put(TRANSACTION_TOTAL_TTC, totalAmount);
        values.put(TRANSACTION_DATE_CREATED, currentDate);
        values.put(TRANSACTION_TIME_CREATED, currentTime);
        values.put(TRANSACTION_TOTAL_HT_A, totalHT_A);
        values.put(TRANSACTION_TOTAL_TTC, totalTTC);
        values.put(TRANSACTION_TOTAL_TX_1, taxAmount);
        values.put(TRANSACTION_ITEM_QUANTITY, quantityItem);
        values.put(TRANSACTION_CASHIER_CODE, cashierId);
        values.put(TRANSACTION_STATUS, transactionStatus);
        values.put(TRANSACTION_SPLIT_TYPE, "Full");
        values.put(TRANSACTION_TERMINAL_NO, posNum);
        values.put(TRANSACTION_MRA_Method, MRAMETHOD);
        values.put(ROOM_ID, roomid);
        values.put(TABLE_ID, tableid);
        values.put(TRANSACTION_VAT_BEFORE_DISC , sumBeforeDisc );
        values.put(TRANSACTION_VAT_AFTER_DISC, sumAfterDisc );




        long result = db.insert(TRANSACTION_HEADER_TABLE_NAME, null, values);
        return result != -1;
    }

    // Method to insert a variant into the database
    public long insertVariant(long itemId, String barcode, String description, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_ID, itemId);
        values.put(VARIANT_BARCODE, barcode);
        values.put(VARIANT_DESC, description);
        values.put(VARIANT_PRICE, price);

        long variantId = db.insert(VARIANTS_TABLE_NAME, null, values);

        db.close();

        return variantId;
    }
    public void updateAllTransactionsHeaderStatus(String newTransactionStatus, String roomid, String tableid) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, newTransactionStatus);

        // Update the status of all transactions with the specified roomid, tableid, and 'InProgress' or 'PRF' status to the new status
        String whereClause = "(" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?) AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] whereArgs = {TRANSACTION_STATUS_IN_PROGRESS, "PRF", roomid, tableid};

        db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);
    }
    public void updateTransactionHeaderStatusfornew(String newTransactionStatus, String transactionId, String roomId, String tableId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, newTransactionStatus);

        // Update the status of the transaction with the specified transactionId, roomId, tableId, and 'InProgress' or 'PRF' status to the new status
        String whereClause = TRANSACTION_ID + " = ? AND (" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?) AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] whereArgs = {transactionId, TRANSACTION_STATUS_IN_PROGRESS, "PRF", roomId, tableId};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Log.d("Database Update", "Transaction status updated successfully. Transaction ID: " + transactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId + ", New Status: " + newTransactionStatus);
        } else {
            Log.e("Database Update", "Failed to update transaction status. No matching rows found for Transaction ID: " + transactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId + ", with status 'InProgress' or 'PRF'");
        }

        // Close the database connection
        db.close();
    }


    public void updateAllTransactionsStatus(String Type, String MRAMETHOD, String InvoiceRefIden, String roomId, String tableId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, Type);
        values.put(TRANSACTION_MRA_Method, MRAMETHOD);
        values.put(TRANSACTION_INVOICE_REF, InvoiceRefIden);
        values.put(RELATED_TRANSACTION_ID, InvoiceRefIden);

        // Update the status of all in-progress transactions to the new status based on roomId and tableId
        String whereClause = TRANSACTION_STATUS + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] whereArgs = {TRANSACTION_STATUS_IN_PROGRESS, roomId, tableId};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Log.d("UPDATE_STATUS", "Update successful. Rows affected: " + rowsAffected);
        } else {
            Log.d("UPDATE_STATUS", "Update failed. No rows affected.");
        }
    }

    public void updateTransactionStatusByTransactionId(String Type, String MRAMETHOD, String InvoiceRefIden, String transactionId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, Type);
        values.put(TRANSACTION_MRA_Method, MRAMETHOD);
        values.put(TRANSACTION_INVOICE_REF, InvoiceRefIden);

        // Update the status of the transaction with the given transaction ID
        String whereClause = TRANSACTION_ID + " = ?";
        String[] whereArgs = {transactionId};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Log.d("UPDATE_STATUS", "Update successful. Rows affected: " + rowsAffected);
        } else {
            Log.d("UPDATE_STATUS", "Update failed. No rows affected.");
        }
    }



    public boolean updateTransactionHeader(double totalAmount, String currentDate, String currentTime, double totalHT_a, double totalTTC, int quantityItem, double totaltax, String cashierId, String roomid, String tableid) {
        SQLiteDatabase db = this.getWritableDatabase();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String status = preferences.getString(STATUS_KEY, "default_value");

        ContentValues values = new ContentValues();
        //values.put(ORDERTYPE, status);
        values.put(TRANSACTION_TOTAL_TTC, totalAmount);
        values.put(TRANSACTION_DATE_MODIFIED, currentDate);
        values.put(TRANSACTION_DATE_TRANSACTION, currentDate);
        values.put(TRANSACTION_TIME_MODIFIED, currentTime);
        values.put(TRANSACTION_TIME_TRANSACTION, currentTime);
        values.put(TRANSACTION_TOTAL_HT_A, totalHT_a);
        values.put(TRANSACTION_TOTAL_TTC, totalTTC);  // Assuming this is intentional
        values.put(TRANSACTION_ITEM_QUANTITY, quantityItem);
        values.put(TRANSACTION_TOTAL_TX_1, totaltax);
        values.put(TRANSACTION_CASHIER_CODE, cashierId);

        String selection = TRANSACTION_STATUS + " IN (?, ?) AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] selectionArgs = {"InProgress", "PRF", roomid, tableid};

        Log.d("UpdateTransactionHeader", "Updating transaction header with values: " +
                "totalAmount=" + totalAmount + ", currentDate=" + currentDate + ", currentTime=" + currentTime +
                ", totalHT_a=" + totalHT_a + ", totalTTC=" + totalTTC + ", quantityItem=" + quantityItem +
                ", totaltax=" + totaltax + ", cashierId=" + cashierId + ", roomid=" + roomid + ", tableid=" + tableid);

        try {
            int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, selection, selectionArgs);

            if (rowsAffected > 0) {
                Log.d("UpdateTransactionHeader", "Successfully updated " + rowsAffected + " row(s).");
            } else {
                Log.d("UpdateTransactionHeader", "No rows updated.");
            }

            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("UpdateTransactionHeader", "Error updating transaction header: " + e.getMessage(), e);
            return false;
        }
    }

    public Cursor getAllInProgressTransactionsbytable(String transactionId, String roomid, String tableid) {
        SQLiteDatabase db = getReadableDatabase();

        if (transactionId == null || transactionId.isEmpty() || roomid == null || roomid.isEmpty() || tableid == null || tableid.isEmpty()) {
            Log.e("Invalid parameters", "Invalid parameters: transactionId, roomid, or tableid is null or empty.");
            return null;
        }

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_TICKET_NO + "=? AND " +
                ROOM_ID + "=? AND " +
                TABLE_ID + "=? AND " +
                IS_PAID + "!=? AND " +
                TRANSACTION_STATUS + "='VALID' " +
                "ORDER BY " + _ID + " ASC";

        String[] selectionArgs = {transactionId, roomid, tableid, "1"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        return cursor;
    }
    public Cursor RestoreViewAllInSplittedProgressTransactionsbytable(String transactionId, String roomid, String tableid) {
        SQLiteDatabase db = getReadableDatabase();

        if (transactionId == null || transactionId.isEmpty() || roomid == null || roomid.isEmpty() || tableid == null || tableid.isEmpty()) {
            Log.e("Invalid splitted parameters", "Invalid parameters: transactionId, roomid, or tableid is null or empty.");
            return null;
        }

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_TICKET_NO + "=? AND " +
                ROOM_ID + "=? AND " +
                TABLE_ID + "=? AND " +
                IS_PAID + "!=? AND " +
                TRANSACTION_STATUS + "='Splitted' " +
                "ORDER BY " + _ID + " ASC";

        String[] selectionArgs = {transactionId, roomid, tableid, "1"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                Log.i("RestoreView", "Query returned non-empty result set.");
            } else {
                Log.i("RestoreView", "Query returned an empty result set.");
            }
        } else {
            Log.e("RestoreView", "Cursor is null.");
        }

        return cursor;
    }



    public boolean flagTransactionItemsAsCleared(String transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define the values to update
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, "Cleared");

        // Define the selection criteria
        String selection = TRANSACTION_ID + "=?";
        String[] selectionArgs = { transactionId };

        // Perform the update
        try {
            int rowsAffected = db.update(DatabaseHelper.TRANSACTION_TABLE_NAME, values, selection, selectionArgs);

            if (rowsAffected > 0) {
                Log.i("DatabaseSuccess", "Successfully updated transaction items to Cleared status for transaction ID: " + transactionId);
                return true;
            } else {
                Log.w("DatabaseWarning", "No transaction items were updated for transaction ID: " + transactionId);
                return false;
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error updating transaction items to Cleared status for transaction ID: " + transactionId, e);
            return false;
        }
    }


    public Cursor getTransactionById1(String transactionId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + "=?";

        String[] selectionArgs = {transactionId};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Do not close the database connection here

        return cursor;
    }


    public boolean areAllItemsSelectedInTransactions(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Count the number of selected items in transactions with the specified room ID, table ID, and status
        String query = "SELECT COUNT(*) AS selectedCount FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + " = th." + TRANSACTION_TICKET_NO +
                " WHERE th." + ROOM_ID + " = ? AND th." + TABLE_ID + " = ? AND th." + TRANSACTION_STATUS + " IN (?, ?) AND t." + IS_SELECTED + " = 1";

        String[] selectionArgs = {roomId, tableId, "InProgress", "PRF"};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            int selectedCount = cursor.getInt(cursor.getColumnIndex("selectedCount"));
            cursor.close();

            // Get the total number of items in transactions with the specified room ID, table ID, and status
            int totalItemCount = getTotalItemsInTransactions(roomId, tableId, "InProgress", "PRF");

            // Check if all items are selected
            return selectedCount == totalItemCount;
        }

        return false;
    }

    private int getTotalItemsInTransactions(String roomId, String tableId, String... status) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the total number of items in transactions with the specified room ID, table ID, and status
        String query = "SELECT COUNT(*) AS totalItemCount FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + " = th." + TRANSACTION_TICKET_NO +
                " WHERE th." + ROOM_ID + " = ? AND th." + TABLE_ID + " = ? AND th." + TRANSACTION_STATUS + " IN (?, ?)";

        String[] selectionArgs = new String[status.length + 2];
        selectionArgs[0] = roomId;
        selectionArgs[1] = tableId;
        System.arraycopy(status, 0, selectionArgs, 2, status.length);

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            int totalItemCount = cursor.getInt(cursor.getColumnIndex("totalItemCount"));
            cursor.close();
            return totalItemCount;
        }

        return 0;
    }



    public boolean areAllTransactionsPaid(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the transaction IDs from the header table based on room, table, and status (InProgress or PRF)
        List<Long> transactionIds = getTransactionIds(roomId, tableId);

        // Check if all transactions with the specified IDs are paid
        for (Long transactionId : transactionIds) {
            if (!isTransactionPaid(transactionId)) {
                return false; // At least one transaction is not paid
            }
        }

        // All transactions are paid
        return true;
    }

    private List<Long> getTransactionIds(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Long> transactionIds = new ArrayList<>();

        // Get the transaction IDs from the header table based on room, table, and status (InProgress or PRF)
        String query = "SELECT " + TRANSACTION_TICKET_NO + " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + ROOM_ID + " = ?" +
                " AND " + TABLE_ID + " = ?" +
                " AND " + TRANSACTION_STATUS + " IN (?, ?)";

        String[] selectionArgs = {roomId, tableId, "InProgress", "PRF"};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long transactionId = cursor.getLong(cursor.getColumnIndex(TRANSACTION_TICKET_NO));
                transactionIds.add(transactionId);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return transactionIds;
    }

    private boolean isTransactionPaid(long transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if the transaction with the specified ID is paid
        String query = "SELECT " + IS_PAID + " FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ?";

        String[] selectionArgs = {String.valueOf(transactionId)};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            int isPaid = cursor.getInt(cursor.getColumnIndex(IS_PAID));
            cursor.close();
            return isPaid == 1;
        }

        return false;
    }






    public Cursor getInProgressRecord(String roomid, String tableid) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = ROOM_ID + " = ?" +
                " AND " + TABLE_ID + " = ?" +
                " AND (" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?)";

        String[] selectionArgs = {roomid, tableid, "InProgress", "PRF"};

        return db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }


    public String getLatestTransactionStatus(String roomId, String tableId) {
        SQLiteDatabase db = getReadableDatabase();
        // Query to get the latest transaction status from the header table based on room ID and table ID
        String query = "SELECT th." + TRANSACTION_STATUS + " FROM " + TRANSACTION_HEADER_TABLE_NAME + " AS th " +
                "WHERE th." + ROOM_ID + "=? AND th." + TABLE_ID + "=? AND (th." + TRANSACTION_STATUS + "=? OR th." + TRANSACTION_STATUS + "=?) " +
                "ORDER BY th." + _ID + " DESC LIMIT 1";

        String[] selectionArgs = {roomId, tableId, "InProgress", "PRF"};

        Cursor cursor = null;
        String latestTransactionStatus = null;

        try {
            cursor = db.rawQuery(query, selectionArgs);

            if (cursor.moveToFirst()) {
                int statusIndex = cursor.getColumnIndex(TRANSACTION_STATUS);
                latestTransactionStatus = cursor.getString(statusIndex);
                Log.d("TransactionFetchStatus", "Latest Transaction Status: " + latestTransactionStatus + " roomId=" + roomId + ", tableId=" + tableId);
            } else {
                Log.d("TransactionFetchStatus", "No transactions found for roomId=" + roomId + ", tableId=" + tableId);
            }
        } catch (Exception e) {
            Log.e("TransactionFetchStatus", "Error fetching latest transaction status", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

      //  db.close(); // Ensure the database connection is closed
        return latestTransactionStatus;
    }

    public Cursor getTransactionsByStatusAndId(String status, String transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT t.* FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + " = th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + " IN (?, ?)" +
                " AND t." + TRANSACTION_ID + " = ?" +
                " AND t." + IS_PAID + " <> ?"; // Change here

        String[] selectionArgs = {status, "PRF", transactionId, "1"};

        Log.d("Query", "Executing query: " + query);
        Log.d("Query", "With args: " + Arrays.toString(selectionArgs));

        Cursor cursor = db.rawQuery(query, selectionArgs);

        Log.d("Query", "Result count: " + cursor.getCount());
        db.close();
        return cursor;
    }
    public String getLatestTransactionId(String roomid, String tableid, String status) {
        // Validate the status
        if (!"PRF".equals(status) && !"InProgress".equals(status)) {
            Log.d("TransactionFetchTransId", "Invalid status: " + status);
            return null;
        }

        SQLiteDatabase db = getReadableDatabase();
        // Query to get the latest transaction ID from the header table based on room ID, table ID, and status
        String query = "SELECT th." + TRANSACTION_TICKET_NO + " FROM " + TRANSACTION_HEADER_TABLE_NAME + " AS th " +
                "WHERE th." + ROOM_ID + "=? AND th." + TABLE_ID + "=? AND th." + TRANSACTION_STATUS + "=? " +
                "ORDER BY th." + _ID + " DESC LIMIT 1";

        String[] selectionArgs = {roomid, tableid, status};

        Cursor cursor = null;
        String latestTransactionId = null;

        try {
            cursor = db.rawQuery(query, selectionArgs);

            if (cursor.moveToFirst()) {
                int transactionIdIndex = cursor.getColumnIndex(TRANSACTION_TICKET_NO);
                latestTransactionId = cursor.getString(transactionIdIndex);
                Log.d("TransactionFetchTransId", "Latest Transaction ID: " + latestTransactionId);
            } else {
                Log.d("TransactionFetchTransId", "No transactions found for roomid=" + roomid + ", tableid=" + tableid + ", status=" + status);
            }
        } catch (Exception e) {
            Log.e("TransactionFetchTransId", "Error fetching latest transaction ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
       // db.close();
        return latestTransactionId;
    }



    public Cursor getAllInProgressTransactions(String roomid, String tableid) {
        SQLiteDatabase db = getReadableDatabase();

        // First query to get the latest transaction ID from the header table
        String query1 = "SELECT th." + TRANSACTION_TICKET_NO + " FROM " + TRANSACTION_HEADER_TABLE_NAME + " AS th " +
                "WHERE (th." + TRANSACTION_STATUS + "=? OR th." + TRANSACTION_STATUS + "=?) AND " +
                "th." + ROOM_ID + "=? AND th." + TABLE_ID + "=? " +
                "ORDER BY th." + _ID + " DESC LIMIT 1";



        String[] selectionArgs1 = {"InProgress", "PRF", roomid, tableid};

        Cursor cursor1 = db.rawQuery(query1, selectionArgs1);

        if (cursor1 == null || !cursor1.moveToFirst()) {
            Log.d("TransactionFetchtrans", "No in-progress or PRF transactions found for roomid=" + roomid + ", tableid=" + tableid);
            return null;
        }

        int latestTransactionId = cursor1.getInt(cursor1.getColumnIndex(TRANSACTION_TICKET_NO));
        cursor1.close();

        Log.d("TransactionFetchtransiddel", "Latest Transaction ID: " + latestTransactionId);

        // Second query to get the transaction data using the latest transaction ID
        String query2 = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE t." + TRANSACTION_ID + "=? AND " +
                "t." + IS_PAID + "<> ? " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs2 = {String.valueOf(latestTransactionId), "1"};

        Cursor cursor2 = db.rawQuery(query2, selectionArgs2);

        if (cursor2 == null || !cursor2.moveToFirst()) {
            Log.d("TransactionFetch", "No transaction data found for transaction ID: " + latestTransactionId);
            return null;
        }

        // Log the details of the found transaction.
        do {
            int transactionId = cursor2.getInt(cursor2.getColumnIndex(TRANSACTION_ID));
            String transactionStatus = cursor2.getString(cursor2.getColumnIndex(TRANSACTION_STATUS));
            Log.d("TransactionFetch", "Transaction ID: " + transactionId + ", Status: " + transactionStatus);
        } while (cursor2.moveToNext());

        // Return the Cursor object.
        return cursor2;
    }





    public Cursor getAllSplittedInProgressTransactions(String roomid, String tableid) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE (th." + TRANSACTION_STATUS + "=? OR th." + TRANSACTION_STATUS + "=?) AND " +
                "t." + TRANSACTION_STATUS + "=? AND " +  // Add condition for VALID status in the transaction table
                "t." + ROOM_ID + "=? AND t." + TABLE_ID + "=? AND " + // Add conditions for roomid and tableid
                "t." + DatabaseHelper.IS_SELECTED + "=? AND " +  // Add condition for isSelected
                "t." + DatabaseHelper.IS_PAID + "=? " +  // Add condition for isPaid
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {"InProgress", "PRF", "VALID", roomid, tableid, "1", "0"};  // Add roomid, tableid, isSelected, and isPaid to selectionArgs

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // There are no in-progress, PRF, or VALID transactions.
            // Return zero.
            return null;
        } else {
            // There are in-progress, PRF, or VALID transactions.
            // Return the Cursor object.
            return cursor;
        }
    }


    public Cursor getSplittedInProgressNotSelectedNotPaidTransactions(String transactionId, String roomId, String tableId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE (" + "th." + TRANSACTION_STATUS + "=? OR th." + TRANSACTION_STATUS + "=?) AND " +
                "t." + TRANSACTION_STATUS + "=? AND " + // Add condition for VALID status in the transaction table
                "t." + ROOM_ID + "=? AND t." + TABLE_ID + "=? AND " +
                "t." + DatabaseHelper.TRANSACTION_ID + "=? AND " +  // Add condition for transactionId
                "t." + DatabaseHelper.IS_SELECTED + "=? AND " +  // Add condition for isSelected
                "t." + DatabaseHelper.IS_PAID + "=? " +  // Add condition for isPaid
                "ORDER BY t." + DatabaseHelper.TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {"InProgress", "PRF", "VALID", roomId, tableId, transactionId, "0", "0"};  // Add transactionId, isSelected, and isPaid to selectionArgs

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // There are no in-progress, PRF, or VALID transactions that match the criteria.
            // Return null.
            return null;
        } else {
            // There are in-progress, PRF, or VALID transactions that match the criteria.
            // Return the Cursor object.
            return cursor;
        }
    }


    public Cursor getAllInProgressTransactionsByType(String Type, String id) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + "=? AND t." + TRANSACTION_ID + "=? " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {Type, id};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // There are no in-progress transactions.
            // Return zero.
            return null;
        } else {
            // There are in-progress transactions.
            // Return the Cursor object.
            return cursor;
        }
    }


    public Cursor getSplittedTransactionsByType(String type, String id) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + "=? AND t." + TRANSACTION_STATUS + "='Splitted' AND t." + TRANSACTION_ID + "=? " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {type, id};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // No transactions with the status "splitted".
            return null;
        } else {
            // Return the Cursor object with transactions where the status is "splitted".
            return cursor;
        }
    }



    public Cursor getValidTransactionsByType(String type, String id) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + "=? AND t." + TRANSACTION_STATUS + "='VALID' AND t." + TRANSACTION_ID + "=? " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {type, id};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // No transactions with the status "valid".
            return null;
        } else {
            // Return the Cursor object with transactions where the status is "valid".
            return cursor;
        }
    }





    public Cursor getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    public Cursor getAllBuyer() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(BUYER_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getAllRooms() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(ROOMS, null, null, null, null, null, null);
    }
    public Cursor getAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLES, null, null, null, null, null, null);
    }
    public Cursor getAllTables1(String roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ROOM_ID + " = ?";
        String[] selectionArgs = {roomId};
        return db.query(TABLES, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getAllQR() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME_PAYMENTBYQY, null, null, null, null, null, null);
    }
    public Cursor getAllItemsByBarcode(String barcode) {
        SQLiteDatabase db = getReadableDatabase();
        String selection =  "Barcode LIKE ?";
        String[] selectionArgs = { "%" + barcode + "%" };
        return db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
    public Cursor getCashierByid(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection =  "cashorid LIKE ?";
        String[] selectionArgs = { "%" + id + "%" };
        return db.query(TABLE_NAME_Users, null, selection, selectionArgs, null, null, null);
    }
    public Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME_Users, null, null, null, null, null, null);
    }

    public Cursor getUserById(int userId) {
        SQLiteDatabase db = getReadableDatabase();

        // Define the selection clause
        String selection = "cashorid = ?";

        // Define the selection arguments
        String[] selectionArgs = { String.valueOf(userId) };

        // Perform the query
        return db.query(TABLE_NAME_Users, null, selection, selectionArgs, null, null, null);
    }
    public List<String> fetchCashierNames() {
        List<String> cashierNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT " + COLUMN_CASHOR_NAME + " FROM " + TABLE_NAME_Users;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String cashierName = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_NAME));
                cashierNames.add(cashierName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cashierNames;
    }

    public Cursor getAllDepartment() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DEPARTMENT_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getAllCoupon() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(COUPON_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getAllDiscounts() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DISCOUNT_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor searchDiscounts(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {DISCOUNT_ID, DISCOUNT_NAME, DISCOUNT_VALUE, DISCOUNT_TIMESTAMP};
        String selection = DISCOUNT_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = DISCOUNT_TIMESTAMP + " DESC";
        return db.query(DISCOUNT_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor getAllSubDepartment() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(SUBDEPARTMENT_TABLE_NAME, null, null, null, null, null, null);
    }


    public Cursor getDepartmentName(String departmentCode) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {DEPARTMENT_NAME};
        String selection = DEPARTMENT_CODE + " = ?";
        String[] selectionArgs = {departmentCode};
        return db.query(DEPARTMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    public Cursor login() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(SUBDEPARTMENT_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getAllCosts() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(COST_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getAllSubDepartmentby() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(SUBDEPARTMENT_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getUserByPIN(String enteredPIN) {
        SQLiteDatabase db = getReadableDatabase();
        String[] selectionArgs = {enteredPIN};
        String query = "SELECT * FROM " + TABLE_NAME_Users + " WHERE pin = ?";
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getAllVendor() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(VENDOR_TABLE_NAME, null, null, null, null, null, null);
    }
    public Cursor getAllPaymentMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(PAYMENT_METHOD_TABLE_NAME, null, null, null, null, null, null);
    }
    public Cursor searchItems(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {BUYER_ID, BUYER_NAME, BUYER_TAN,BUYER_BUSINESS_ADDR,BUYER_NIC};
        String selection = BUYER_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = BUYER_NAME + " ASC";
        return db.query(BUYER_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }
    public Cursor searchbuyer(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {_ID, Name, LongDescription,AvailableForSale, Category, Price};
        String selection = LongDescription + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = LongDescription + " ASC";
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }
    public Cursor searchRoom(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {ID, ROOM_NAME, TABLE_COUNT };
        String selection = ROOM_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = ROOM_NAME + " ASC";
        return db.query(ROOMS, projection, selection, selectionArgs, null, null, sortOrder);
    }
    public Cursor searchTable(String query, String roomid) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {ID, TABLE_ID, TABLE_NUMBER, ROOM_ID};
        String selection;
        String[] selectionArgs;

        if (roomid != null && !roomid.isEmpty()) {
            // If roomid is provided, include it in the selection
            selection = TABLE_NUMBER + " LIKE ? AND " + ROOM_ID + " = ?";
            selectionArgs = new String[]{"%" + query + "%", roomid};
        } else {
            // If roomid is not provided, only use the LIKE condition
            selection = TABLE_NUMBER + " LIKE ?";
            selectionArgs = new String[]{"%" + query + "%"};
        }

        String sortOrder = TABLE_NUMBER + " ASC";
        return db.query(TABLES, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor searchUser(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_CASHOR_id, COLUMN_CASHOR_NAME, COLUMN_CASHOR_DEPARTMENT, COLUMN_CASHOR_LEVEL, COLUMN_CASHOR_Shop};
        String selection = COLUMN_CASHOR_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = COLUMN_CASHOR_NAME + " ASC";
        return db.query(TABLE_NAME_Users, projection, selection, selectionArgs, null, null, sortOrder);
    }
    public Cursor searchCategory(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {DEPARTMENT_ID, DEPARTMENT_CODE, DEPARTMENT_NAME, DEPARTMENT_LAST_MODIFIED, COLUMN_CASHOR_id};
        String selection = DEPARTMENT_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = DEPARTMENT_NAME + " ASC";
        return db.query(DEPARTMENT_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }
    public Cursor searchCoupon(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COUPON_ID, COUPON_CODE, COUPON_DATE_CREATED, COUPON_END_DATE, COUPON_DISCOUNT};
        String selection = COUPON_CODE + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = COUPON_CODE + " ASC";
        return db.query(COUPON_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor searchVendor(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {VendorID, NomFournisseur, CodeFournisseur, LastModified, UserId};
        String selection = NomFournisseur + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = LastModified + " DESC";
        return db.query(VENDOR_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor searchSubDepartment(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {SUBDEPARTMENT_ID, SUBDEPARTMENT_NAME, LastModified, DEPARTMENT_CASHIER_ID, DEPARTMENT_CODE};
        String selection = SUBDEPARTMENT_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = LastModified + " DESC";
        return db.query(SUBDEPARTMENT_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);


    }


    public Cursor searchCost(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {Barcode, SKUCost, Cost, LastModified, UserId, CodeFournisseur};
        String selection = Barcode + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = LastModified + " DESC";
        return db.query(COST_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }
    public Cursor searchOptions(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {OPTION_NAME, OPTION_ID};
        String selection = OPTION_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};

        return db.query(OPTIONS_TABLE_NAME, projection, selection, selectionArgs, null, null,null);
    }
    public int getNumberOfCoversByTransactionTicketNo(String transactionTicketNo) {
        int numberOfCovers = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + NUMBER_OF_COVERS + " FROM " + TRANSACTION_HEADER_TABLE_NAME + " WHERE " + TRANSACTION_TICKET_NO + " = ?", new String[]{transactionTicketNo});

        if (cursor.moveToFirst()) {
            numberOfCovers = cursor.getInt(cursor.getColumnIndex(NUMBER_OF_COVERS));
        }

        cursor.close();
        db.close();

        return numberOfCovers;
    }





    public boolean isItemIdExists(String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(VARIANTS_TABLE_NAME, null, VARIANT_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean isVariantBarcodeExists(String optionid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SUPPLEMENTS_TABLE_NAME, null, SUPPLEMENT_OPTION_ID  + "=?",
                new String[]{optionid}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean isDepartmentCodeExists(String DeptCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DEPARTMENT_TABLE_NAME, null, DEPARTMENT_CODE + "=?",
                new String[]{DeptCode}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkBarcodeExists(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, Barcode + "=?",
                new String[]{barcode}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean checkBarcodeExistsForcost(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(COST_TABLE_NAME, null, Barcode + "=?",
                new String[]{barcode}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean checkBarcodeExistsForItems(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, Barcode + "=?",
                new String[]{barcode}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean checkIDExistsForUser(String cashorid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_Users, null, COLUMN_CASHOR_id + "=?",
                new String[]{cashorid}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean checkIDExistsForRooms(String roomid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ROOMS, null, TABLE_ID + "=?",
                new String[]{roomid}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkIDExistsForTable(String tableid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLES, null, TABLE_ID + "=?",
                new String[]{tableid}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // Method to get the sum of amount based on transactionId, roomId, and tableId
    public double getSumOfAmount(String transactionId, int roomId, String tableId) {
        SQLiteDatabase db = getReadableDatabase();
        double sum = 0.0;
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                    " WHERE " + SETTLEMENT_INVOICE_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
            String[] selectionArgs = {transactionId, String.valueOf(roomId), tableId};

            cursor = db.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                sum = cursor.getDouble(0);
            }

            Log.d("DatabaseHelper", "Sum of amount: " + sum + " for transactionId=" + transactionId + ", roomId=" + roomId + ", tableId=" + tableId);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching sum of amount for transactionId=" + transactionId + ", roomId=" + roomId + ", tableId=" + tableId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return sum;
    }
    public boolean isTransactionIdExists(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(INVOICE_SETTLEMENT_TABLE_NAME, null, SETTLEMENT_INVOICE_ID + "=?",
                new String[]{transactionId}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // Method to get the sum of settlement amounts and associated payment names
    public List<SettlementSummary> getSettlementSummaries(String transactionId, int roomId, String tableId) {
        SQLiteDatabase db = getReadableDatabase();

        // SQL query to get the sum of SETTLEMENT_AMOUNT and the SETTLEMENT_PAYMENT_NAME
        String query = "SELECT SUM(" + SETTLEMENT_AMOUNT + ") AS total_amount, " + SETTLEMENT_PAYMENT_NAME +
                " FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " WHERE " + SETTLEMENT_INVOICE_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?" +
                " GROUP BY " + SETTLEMENT_PAYMENT_NAME;

        String[] selectionArgs = {transactionId, String.valueOf(roomId), tableId};

        List<SettlementSummary> summaries = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, selectionArgs);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    double sum = cursor.getDouble(cursor.getColumnIndexOrThrow("total_amount"));
                    String paymentName = cursor.getString(cursor.getColumnIndexOrThrow(SETTLEMENT_PAYMENT_NAME));
                    summaries.add(new SettlementSummary(sum, paymentName));
                } while (cursor.moveToNext());
            }

            if (!summaries.isEmpty()) {
                Log.d("DatabaseHelper", "Sum fetch successful. Number of payment methods: " + summaries.size());
            } else {
                Log.d("DatabaseHelper", "No data found for transaction ID: " + transactionId + ", room ID: " + roomId + ", table ID: " + tableId);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching sum for transaction ID: " + transactionId + ", room ID: " + roomId + ", table ID: " + tableId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return summaries;
    }


    // Helper class to store the settlement summary
    public static class SettlementSummary {
        public double sum;
        public String paymentName;

        public SettlementSummary(double sum, String paymentName) {
            this.sum = sum;
            this.paymentName = paymentName;
        }
    }

    public void updateRelatedOptionId(String transactionId, String itemId, String newRelatedOptionId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RELATED_Option_ID, newRelatedOptionId);

        String selection = TRANSACTION_ID + " = ? AND " + ITEM_ID + " = ?" +
                " AND " + IS_PAID + " = 0" +
                " AND " + TRANSACTION_STATUS + " = 'VALID'";
        String[] selectionArgs = {transactionId, itemId};

        try {
            int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, selection, selectionArgs);
            if (rowsAffected > 0) {
                Log.d("DatabaseHelper", "Related Option ID updated successfully for Transaction ID: " + transactionId + " and Item ID: " + itemId);
            } else {
                Log.d("DatabaseHelper", "No rows affected. Transaction ID: " + transactionId + " and Item ID: " + itemId);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating Related Option ID for Transaction ID: " + transactionId + " and Item ID: " + itemId, e);
        } finally {
            db.close();
        }
    }


    public Cursor getTransactionByItemId(int itemId, String roomid, String tableid) {
        SQLiteDatabase db = getReadableDatabase();

        // Updated selection criteria
        String selection = TRANSACTION_TABLE_NAME + "." + ITEM_ID + " = ?" +
                " AND (" + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_STATUS + " = ?" +
                " OR " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_STATUS + " = ?)" +
                " AND " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_STATUS + " = ?" + // Ensure TransactionStatus is VALID
                " AND " + TRANSACTION_HEADER_TABLE_NAME + "." + ROOM_ID + " = ?" +
                " AND " + TRANSACTION_HEADER_TABLE_NAME + "." + TABLE_ID + " = ?" +
                " AND " + TRANSACTION_TABLE_NAME + "." + IS_PAID + " = ?" +
                " AND (" + TRANSACTION_TABLE_NAME + "." + RELATED_Option_ID + " IS NULL" +
                " OR " + TRANSACTION_TABLE_NAME + "." + RELATED_Option_ID + " = '')";

        ; // Add condition for IS_PAID

        String[] selectionArgs = {String.valueOf(itemId), "InProgress", "PRF", "VALID", roomid, tableid, "0"}; // "0" for not paid

        // Adding ORDER BY clause to get the latest data based on the highest ID
        String orderBy = " ORDER BY " + TRANSACTION_HEADER_TABLE_NAME + "._ID DESC";
        String limit = " LIMIT 1"; // Limits to the most recent record

        // Join query with ORDER BY and LIMIT
        String joinQuery = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                " JOIN " + TRANSACTION_HEADER_TABLE_NAME +
                " ON " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_ID + " = " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_TICKET_NO +
                " WHERE " + selection + orderBy + limit;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(joinQuery, selectionArgs);
            if (cursor != null) {
                Log.d("DatabaseHelper", _ID+"Data fetch successful. Number of records: " + cursor.getCount() + " item id" + itemId + ", room ID: " + roomid + ", table ID: " + tableid);
            } else {
                Log.d("DatabaseHelper", _ID +"No data found for item ID: " + itemId + ", room ID: " + roomid + ", table ID: " + tableid);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", _ID+"Error fetching data for item ID: " + itemId + ", room ID: " + roomid + ", table ID: " + tableid, e);
        }

        return cursor;
    }


    public String getOptionsBarcodeByItemId(String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String barcode = null;

        String query = "SELECT " + VARIANT_BARCODE + " FROM " + VARIANTS_TABLE_NAME +
                " WHERE " + VARIANT_ITEM_ID + " = ? LIMIT 1";

        try (Cursor cursor = db.rawQuery(query, new String[]{itemId})) {
            if (cursor != null && cursor.moveToFirst()) {
                barcode = cursor.getString(cursor.getColumnIndex(VARIANT_BARCODE));
                Log.d("DatabaseHelper", "Barcode found: " + barcode);
            } else {
                Log.d("DatabaseHelper", "No barcode found for item ID: " + itemId);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving barcode for item ID: " + itemId, e);
        }

        return barcode;
    }


    public int getLatestSentToKitchen(String barcode, String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int sentToKitchen = 0; // Default value

        // Define the query to retrieve the latest sentToKitchen value based on transaction ID and barcode
        String query = "SELECT t." + TRANSACTION_SentToKitchen +
                " FROM " + TRANSACTION_TABLE_NAME + " AS t" +
                " JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th" +
                " ON t." + TRANSACTION_ID + " = th." + TRANSACTION_TICKET_NO +
                " WHERE t." + TRANSACTION_ID + " = ?" +
                " AND t." + TRANSACTION_BARCODE + " = ?" +
                " ORDER BY t." + _ID + " DESC" +
                " LIMIT 1";

        // Use try-with-resources to ensure the cursor is closed after use
        try (Cursor cursor = db.rawQuery(query, new String[]{transactionId, barcode})) {
            if (cursor.moveToFirst()) {
                sentToKitchen = cursor.getInt(cursor.getColumnIndex(TRANSACTION_SentToKitchen));
                // Log the result
                Log.d("DatabaseHelper", "Latest sentToKitchen value for barcode " + barcode + " and transaction ID " + transactionId + ": " + sentToKitchen);
            } else {
                Log.d("DatabaseHelper", "No sentToKitchen value found for barcode " + barcode + " and transaction ID " + transactionId);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving latest sentToKitchen value", e);
        }

        return sentToKitchen;
    }




    public boolean updateNumberOfCovers(String transactionTicketNumber, int numberOfCovers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NUMBER_OF_COVERS, numberOfCovers);

        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionTicketNumber };

        try {
            int rowsAffected = db.update(
                    TRANSACTION_HEADER_TABLE_NAME,   // Table name
                    values,                         // Values to update
                    selection,                      // WHERE clause
                    selectionArgs                   // WHERE clause arguments
            );

            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("UpdateNumberOfCovers", "Error updating number of covers: " + e.getMessage(), e);
            return false;
        } finally {
            db.close();
        }
    }

    public void updateTransactionIds(String newTransactionId, String roomid, String tableid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, newTransactionId);

        String joinQuery = "UPDATE " + TRANSACTION_TABLE_NAME +
                " SET " + TRANSACTION_ID + " = ?" +
                " WHERE " + TRANSACTION_ID + " IN (" +
                " SELECT " + TRANSACTION_TICKET_NO +
                " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = ?" +
                " AND " + ROOM_ID + " = ?" +
                " AND " + TABLE_ID + " = ?" +
                ")";

        String[] selectionArgs = {newTransactionId, "InProgress", roomid, tableid};
        Log.d("updatettransid", "Table ID: " + tableid);
        db.execSQL(joinQuery, selectionArgs);
    }


    public void updateTransactionIdsforPRF(String newTransactionId, String roomid, String tableid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, newTransactionId);

        String joinQuery = "UPDATE " + TRANSACTION_TABLE_NAME +
                " SET " + TRANSACTION_ID + " = ?" +
                " WHERE " + TRANSACTION_ID + " IN (" +
                " SELECT " + TRANSACTION_TICKET_NO +
                " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = ?" +
                " AND " + ROOM_ID + " = ?" +
                " AND " + TABLE_ID + " = ?" +
                ")";

        String[] selectionArgs = {newTransactionId, "InProgress", roomid, tableid};

        db.execSQL(joinQuery, selectionArgs);
    }



    public Cursor getTransactionsByRoomAndTable(String status, String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + " IN (?, ?)" +
                " AND th." + ROOM_ID + " = ?" +  // Assuming TRANSACTION_ROOM_ID is the column for room ID
                " AND th." + TABLE_ID + " = ?";   // Assuming TRANSACTION_TABLE_ID is the column for table ID

        String[] selectionArgs = {status, "PRF", roomId, tableId};

        return db.rawQuery(query, selectionArgs);
    }
    public long getTransactionIdByRoomAndTable(String status, String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT th." + TRANSACTION_TICKET_NO +
                " FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                " JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + " IN (?, ?)" +
                " AND th." + ROOM_ID + " = ?" +
                " AND th." + TABLE_ID + " = ?";

        String[] selectionArgs = {status, "PRF", roomId, tableId};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        long transactionId = -1; // Initialize with an invalid value
        if (cursor != null && cursor.moveToFirst()) {
            transactionId = cursor.getLong(cursor.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
            cursor.close();
        }

        // Close the database connection
        db.close();

        return transactionId;
    }
    public boolean areNoItemsSelectedNorPaid(String transactionId) {
        boolean noItemsSelectedNorPaid = true;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {IS_SELECTED, IS_PAID};

        // Define the WHERE clause with your criteria
        String selection = TRANSACTION_ID + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {String.valueOf(transactionId)};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the IS_SELECTED and IS_PAID values from the cursor
            int isSelectedIndex = cursor.getColumnIndexOrThrow(IS_SELECTED);
            int isPaidIndex = cursor.getColumnIndexOrThrow(IS_PAID);

            do {
                int isSelected = cursor.getInt(isSelectedIndex);
                int isPaid = cursor.getInt(isPaidIndex);

                // Check if any item is selected or paid
                if (isSelected == 1 || isPaid == 1) {
                    noItemsSelectedNorPaid = false;
                    break; // No need to continue checking if one item is selected or paid
                }
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return noItemsSelectedNorPaid;
    }
    // Add this method to DatabaseHelper class

    public Cursor getTablesFilteredByMerged(String roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLES +
                " WHERE " + ROOM_ID + " = ? " +
                " AND (" + MERGED + " <> 1 OR " + MERGED_SET_ID + " <> 0)";

        return db.rawQuery(query, new String[]{roomId});
    }


    public boolean areAllItemsNotSelectedNotPaid(String transactionId) {
        boolean allNotSelectedNotPaid = true;
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {IS_SELECTED, IS_PAID};

        // Define the WHERE clause with your criteria
        String selection = TRANSACTION_ID + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {String.valueOf(transactionId)};

        // Execute the query
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the IS_SELECTED and IS_PAID values from the cursor
            int isSelectedIndex = cursor.getColumnIndexOrThrow(IS_SELECTED);
            int isPaidIndex = cursor.getColumnIndexOrThrow(IS_PAID);

            do {
                int isSelected = cursor.getInt(isSelectedIndex);
                int isPaid = cursor.getInt(isPaidIndex);

                // Check if any item is selected or paid
                if (isSelected == 1 || isPaid == 1) {
                    allNotSelectedNotPaid = false;
                    break; // No need to continue checking if one item is selected or paid
                }
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return allNotSelectedNotPaid;
    }

    public boolean areAllItemsNotSelectedAndPaid(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean allItemsNotSelectedAndPaid = false;

        try {
            // Get the transaction ID based on room, table, and status
            cursor = getTransactionsByRoomAndTable("InProgress", roomId, tableId);

            if (cursor.moveToFirst()) {
                do {
                    // Extract transaction ID from the cursor
                    int transactionId = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));

                    // Check if all items with this transaction ID are not selected and paid
                    allItemsNotSelectedAndPaid = checkAllItemsNotSelectedAndPaid(transactionId);

                    if (!allItemsNotSelectedAndPaid) {
                        // If at least one item is selected or not paid, break the loop
                        break;
                    }

                } while (cursor.moveToNext());
            }
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return allItemsNotSelectedAndPaid;
    }

    public boolean checkAllItemsNotSelectedAndPaid(int transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query the transaction table for items with the given transaction ID
            String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                    " WHERE " + TRANSACTION_ID + " = ? AND " + IS_PAID + " = 1";

            String[] selectionArgs = {String.valueOf(transactionId)};

            cursor = db.rawQuery(query, selectionArgs);

            // If the cursor is not empty, there are items that are selected or not paid
            return cursor.getCount() == 0;
        } finally {
            // Close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getInProgressTransactionIdnew(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String transactionId = null;

        // Use a parameterized query to handle string values safely
        String query = "SELECT " + TRANSACTION_TICKET_NO + " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_STATUS + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{roomId, tableId, "in progress"});

        if (cursor.moveToFirst()) {
            transactionId = cursor.getString(cursor.getColumnIndex(TRANSACTION_TICKET_NO));
        }

        cursor.close();
        db.close();

        return transactionId;
    }

    public double getTotalDiscountSumForInProgressTransaction(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalDiscountSum = 0;

        // Use a parameterized query to handle string values safely
        String query = "SELECT ROUND(SUM(t1." + TRANSACTION_TOTAL_DISCOUNT + "), 2) FROM " +
                TRANSACTION_TABLE_NAME + " t1" +
                " JOIN " + TRANSACTION_HEADER_TABLE_NAME + " t2 ON t1." + TRANSACTION_TICKET_NO + " = t2." + TRANSACTION_TICKET_NO +
                " WHERE t2." + ROOM_ID + " = ?" +
                " AND t2." + TABLE_ID + " = ?" +
                " AND t2." + TRANSACTION_STATUS + " = 'InProgress'";

        Cursor cursor = db.rawQuery(query, new String[]{roomId, tableId});

        if (cursor.moveToFirst()) {
            do {
                double discountValue = cursor.getDouble(0);
                Log.d("DiscountValue", "Discount value: " + discountValue);
                totalDiscountSum += discountValue;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        Log.d("TotalDiscountSum", "Total discount sum: " + totalDiscountSum);

        return totalDiscountSum;
    }






    public Cursor getTransactionsById( String transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " AND t." + TRANSACTION_ID + " = ?";

        String[] selectionArgs = {transactionId};

        return db.rawQuery(query, selectionArgs);
    }
    public Cursor getCompanyInfo(String shopName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_STD_ACCESS +
                " WHERE " + COLUMN_SHOPNAME + " = ?";

        if (shopName != null) {
            return db.rawQuery(query, new String[]{shopName});
        } else {
            // Handle the case where transactionId is null
            // You can choose to return null or an empty cursor, depending on your requirements
            return null;
        }
    }

    public Cursor getCompanyInfobyShopId(String shopNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_STD_ACCESS +
                " WHERE " + COLUMN_SHOPNUMBER + " = ?";

        if (shopNumber != null) {
            return db.rawQuery(query, new String[]{shopNumber});
        } else {
            // Handle the case where transactionId is null
            // You can choose to return null or an empty cursor, depending on your requirements
            return null;
        }
    }
    public Cursor getTransactionHeader(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " IN ('InProgress', 'PRF') " +
                " AND " + ROOM_ID + " = ?" +
                " AND " + TABLE_ID + " = ?";

        String[] selectionArgs = {roomId, tableId};

        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getTransactionById(String transactionId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_ID + "=? " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {transactionId};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // There are no transactions with the specified ID.
            // Return null.
            return null;
        } else {
            // There are transactions with the specified ID.
            // Return the Cursor object.
            return cursor;
        }
    }
    public Cursor getTransactionHeaderdetails(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ?";

        return db.rawQuery(query, new String[]{transactionId});
    }
    public Cursor getTransactionHeaderTotal(String roomid, String tableid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE (" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?)" +
                " AND " + ROOM_ID + " = ?" +
                " AND " + TABLE_ID + " = ?";

        String[] selectionArgs = {DatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS, "PRF", roomid, tableid};

        return db.rawQuery(query, selectionArgs);
    }
    public PaymentDetails getPaymentDetailsById(String paymentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + PAYMENT_METHOD_COLUMN_NAME +
                " FROM " + PAYMENT_METHOD_TABLE_NAME +
                " WHERE " + PAYMENT_METHOD_COLUMN_ID + " = ?";

        String[] selectionArgs = {paymentId};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        PaymentDetails paymentDetails = null;

        if (cursor.moveToFirst()) {
            String paymentName = cursor.getString(cursor.getColumnIndex(PAYMENT_METHOD_COLUMN_NAME));

            // Create a PaymentDetails object
            paymentDetails = new PaymentDetails(paymentName);
        }

        cursor.close();
        db.close();

        return paymentDetails;
    }



    public List<PaymentMethodDataModel> getPaymentMethodDataBasedOnReportType(String reportType) {
        List<PaymentMethodDataModel> paymentMethodDataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Implement your logic to fetch payment method data based on the selected report type
        // For now, return a dummy list
        String dateFilter = getDatesFilterBasedOnReportType(reportType);

        String query = "SELECT " + SETTLEMENT_PAYMENT_NAME + ", COUNT(*) AS paymentCount, SUM(" + SETTLEMENT_AMOUNT + ") AS totalAmount " +
                "FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " WHERE " + dateFilter +
                " GROUP BY " + SETTLEMENT_PAYMENT_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                int paymentCount = cursor.getInt(cursor.getColumnIndex("paymentCount"));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));

                // Create a PaymentMethodDataModel object and add it to the list
                PaymentMethodDataModel paymentMethodDataModel = new PaymentMethodDataModel(paymentName, paymentCount, totalAmount);
                paymentMethodDataList.add(paymentMethodDataModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return paymentMethodDataList;
    }
    public Cursor getTransactionSettlementById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " WHERE " + SETTLEMENT_INVOICE_ID + " = '" + id + "'";

        return db.rawQuery(query, null);
    }
    public Cursor getTransactionHeaderType(String type, String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = ? AND " + TRANSACTION_ID + " = ?";

        return db.rawQuery(query, new String[]{type, id});
    }
    public Cursor getTransactionHeaderids( String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ?";

        return db.rawQuery(query, new String[]{ id});
    }
    // Method to get variant price based on itemid
    public double getVariantPriceByItemId(String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double variantPrice = 0.0;

        String query = "SELECT " + VARIANT_PRICE +
                " FROM " + VARIANTS_TABLE_NAME +
                " WHERE " + VARIANT_ITEM_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{itemId});

        if (cursor != null && cursor.moveToFirst()) {
            variantPrice = cursor.getDouble(cursor.getColumnIndex(VARIANT_PRICE));
            cursor.close();
        }

        return variantPrice;
    }
    public double getSupplementPrice(String supplementId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double supplementPrice = 0.0;

        String query = "SELECT " + SUPPLEMENT_PRICE +
                " FROM " + SUPPLEMENTS_TABLE_NAME +
                " WHERE " + SUPPLEMENT_OPTION_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{supplementId});

        if (cursor != null && cursor.moveToFirst()) {
            supplementPrice = cursor.getDouble(cursor.getColumnIndex(SUPPLEMENT_PRICE));
            cursor.close();
        }

        return supplementPrice;
    }


    public double getItemPrice(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        double price = 0;

        String[] projection = {PriceAfterDiscount};
        String selection = _ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndex(PriceAfterDiscount));
        }

        cursor.close();
        db.close();

        return price;
    }

    public String getItemVAT(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String vat = null;

        String[] projection = {VAT};
        String selection = _ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int vatIndex = cursor.getColumnIndex(VAT);
                if (vatIndex != -1) {
                    vat = cursor.getString(vatIndex);
                }
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error while fetching VAT", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return vat;
    }

    public String getVatTypeById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String vatType = null;

        String[] projection = {VAT}; // Replace "VatType" with your actual column name for VAT type
        String selection = _ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            vatType = cursor.getString(cursor.getColumnIndex(VAT)); // Replace "VatType" with your actual column name for VAT type
        }

        cursor.close();
        db.close();

        return vatType;
    }


    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return timeFormat.format(date);
    }


    public int calculateTotalItemQuantity(String transactionId) {
        int totalQuantity = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + QUANTITY + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_ID  + " = ?";
            String[] selectionArgs = {String.valueOf(transactionId)};
            cursor = db.rawQuery(query, selectionArgs);

            if (cursor.moveToFirst()) {
                totalQuantity = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return totalQuantity;
    }

    public Cursor getDistinctVATTypes1(String transactionIdInProgress) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.VAT_Type};
        String selection = DatabaseHelper.TRANSACTION_ID + " = ?";
        String[] selectionArgs = {transactionIdInProgress};
        String orderBy = DatabaseHelper.VAT_Type + " ASC";

        return db.query(true, DatabaseHelper.TRANSACTION_TABLE_NAME, columns, selection, selectionArgs, DatabaseHelper.VAT_Type, null, orderBy, null);
    }

    public Cursor getDistinctVATTypes(String transactionIdInProgress, String roomid, String tableid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.VAT_Type};
        String selection = DatabaseHelper.TRANSACTION_ID + " = ?" +
                " AND " + DatabaseHelper.ROOM_ID + " = ?" +
                " AND " + DatabaseHelper.TABLE_ID + " = ?";
        String[] selectionArgs = {transactionIdInProgress, roomid, tableid};
        String orderBy = DatabaseHelper.VAT_Type + " ASC";

        return db.query(true, DatabaseHelper.TRANSACTION_TABLE_NAME, columns, selection, selectionArgs, DatabaseHelper.VAT_Type, null, orderBy, null);
    }
    public String getInProgressTransactionId(String roomid, String tableid) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Log the roomid and tableid for debugging
        Log.d("DatabaseHelpertransid", "roomid: " + roomid + ", tableid: " + tableid);

        if (roomid == null || tableid == null) {
            Log.e("DatabaseHelpertransid", "Room ID or Table ID is null");
            return null;
        }

        String[] columns = {DatabaseHelper.TRANSACTION_TICKET_NO};
        String selection = DatabaseHelper.ROOM_ID + " = ?" +
                " AND " + DatabaseHelper.TABLE_ID + " = ?" +
                " AND (" + DatabaseHelper.TRANSACTION_STATUS + " = ? OR " + DatabaseHelper.TRANSACTION_STATUS + " = ?)";
        String[] selectionArgs = {roomid, tableid, DatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS, "PRF"};

        Cursor cursor = null;
        String transactionId = null;

        try {
            cursor = db.query(DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                transactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TICKET_NO));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelpertransid", "Error while querying database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return transactionId;
    }



    // Method to insert user data into the Users table
    public long insertUserData(ContentValues values, ContentValues values1) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME_Users, null, values);
        long result1 = db.insert(DEPARTMENT_TABLE_NAME, null, values1);
        db.close();
        return result;
    }

    // Method to retrieve data from the std_access table
    public Cursor getStdAccessData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_STD_ACCESS, null, null, null, null, null, null);
        return cursor;
    }
    public boolean updateTransactionSplitType(String ticketNo, String splitType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_SPLIT_TYPE, splitType);
        values.put(TRANSACTION_STATUS, "InProgress");

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, TRANSACTION_TICKET_NO + "=?", new String[]{String.valueOf(ticketNo)});
        db.close();

        boolean isUpdateSuccessful = rowsAffected > 0;

        if (isUpdateSuccessful) {
            Log.d("DatabaseUpdate", "Transaction with Ticket No " + ticketNo + " split type updated to " + splitType);
        } else {
            Log.e("DatabaseUpdate", "Failed to update split type for Transaction with Ticket No " + ticketNo);
        }

        return isUpdateSuccessful;
    }

    // Update the SEAT_COUNT for a specific table
    public boolean updateSeatCount(String roomId, int tableNumber, int newSeatCount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("seat_count", newSeatCount);

        String whereClause = "room_id = ? AND table_number = ?";
        String[] whereArgs = {roomId, String.valueOf(tableNumber)};

        int rowsAffected = db.update("tables", values, whereClause, whereArgs);
        db.close();

        return rowsAffected > 0;
    }

    // Retrieve a table by room ID and table number
    public Table getTableByRoomAndNumber(String roomId, int tableNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Table table = null;

        String[] columns = {"table_number", "seat_count"}; // Add other columns as needed
        String selection = "room_id = ? AND table_number = ?";
        String[] selectionArgs = {roomId, String.valueOf(tableNumber)};

        Cursor cursor = db.query("tables", columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String seatCount = String.valueOf(cursor.getInt(cursor.getColumnIndex("seat_count")));
            // Retrieve other columns as needed

            table = new Table(tableNumber, seatCount); // Create a Table object with the retrieved data

            cursor.close();
        }

        db.close();

        return table;
    }

    public boolean isUserTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_Users;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }

        return true; // Return true by default if an error occurs
    }
    public boolean isCompanyTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_STD_ACCESS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }

        return true; // Return true by default if an error occurs
    }



    public Cursor getAlllevels() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_CASHOR_LEVEL + " FROM " + TABLE_NAME_Users, null);
        return cursor;
    }


    public   Cursor getDepartmentByName(String departmentName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DEPARTMENT_ID, DEPARTMENT_CODE, DEPARTMENT_NAME, DEPARTMENT_LAST_MODIFIED, COLUMN_CASHOR_id};
        String selection = DEPARTMENT_NAME + "=?";
        String[] selectionArgs = {departmentName};

        return db.query(DEPARTMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }


    public boolean isQrCodeExists(String qrCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_PAYMENTBYQY, null, COLUMN_QR_CODE_NUM + "=?",
                new String[]{qrCode}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;

    }

    public Cursor getQRByName(String qrName) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_PAYMENT_METHOD};
        String selection = COLUMN_QR_CODE_NUM + " = ?";
        String[] selectionArgs = {qrName};
        return db.query(TABLE_NAME_PAYMENTBYQY, columns, selection, selectionArgs, null, null, null);
    }

    public Cursor searchqr(String newText) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_PAYMENT_METHOD, COLUMN_PAYMENT_ID};
        String selection = COLUMN_PAYMENT_METHOD + " LIKE ?";
        String[] selectionArgs = {"%" + newText + "%"};
        String sortOrder = COLUMN_PAYMENT_METHOD + " ASC";
        return db.query(TABLE_NAME_PAYMENTBYQY, projection, selection, selectionArgs, null, null, sortOrder);
    }


    public Cursor searchpaymentmethod(String newText) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {PAYMENT_METHOD_COLUMN_NAME, PAYMENT_METHOD_COLUMN_ID,PAYMENT_METHOD_COLUMN_ICON};
        String selection = PAYMENT_METHOD_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + newText + "%"};
        String sortOrder = PAYMENT_METHOD_COLUMN_NAME + " ASC";
        return db.query(PAYMENT_METHOD_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    // Method to delete data based on roomId, tableId, and transactionId
    public void clearData(int roomId, int tableId, String transactionId) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String whereClause = ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + SETTLEMENT_INVOICE_ID + " = ?";
            String[] whereArgs = {String.valueOf(roomId), String.valueOf(tableId), transactionId};

            int rowsDeleted = db.delete(INVOICE_SETTLEMENT_TABLE_NAME, whereClause, whereArgs);

            Log.d("DatabaseHelper", "Deleted " + rowsDeleted + " rows where roomId=" + roomId + ", tableId=" + tableId + ", transactionId=" + transactionId);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting data for roomId=" + roomId + ", tableId=" + tableId + ", transactionId=" + transactionId, e);
        } finally {
            db.close();
        }
    }

    public void updateSettlementTransactionId(String oldTransactionId, String newTransactionId, String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SETTLEMENT_INVOICE_ID, newTransactionId);

        // Define the WHERE clause
        String whereClause = SETTLEMENT_INVOICE_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] whereArgs = {oldTransactionId, roomId, tableId};

        // Update the rows
        int rowsAffected = db.update(INVOICE_SETTLEMENT_TABLE_NAME, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Log.d("Database Update", "Transaction ID updated successfully in settlement table. Old Transaction ID: " + oldTransactionId +
                    ", New Transaction ID: " + newTransactionId + ", Room ID: " + roomId + ", Table ID: " + tableId);
        } else {
            Log.e("Database Update", "Failed to update transaction ID in settlement table. No matching rows found for Old Transaction ID: " + oldTransactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId);
        }

        // Close the database connection
        db.close();
    }

    public boolean insertSettlementAmount(String paymentName, double settlementAmount, String SettlementId, String PosNum, String transactionDate,String transactiontime,String roomid, String tableid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        SharedPreferences preferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);


        String cashorId = preferences.getString("cashorId", null); // Retrieve cashor's ID
        values.put(COLUMN_CASHOR_id, cashorId);
        values.put(SETTLEMENT_PAYMENT_NAME, paymentName);
        values.put(SETTLEMENT_AMOUNT, settlementAmount);
        values.put(SETTLEMENT_INVOICE_ID  , SettlementId);
        values.put(SETTLEMENT_TERMINAL_NO,PosNum);
        values.put(SETTLEMENT_DATE_TRANSACTION  , transactionDate);
        values.put(SETTLEMENT_DATE_CREATED  , transactionDate);
        values.put(SETTLEMENT_Time_TRANSACTION  , transactiontime);
        values.put(SETTLEMENT_Time_CREATED  , transactiontime);
        values.put(ROOM_ID, roomid);
        values.put(TABLE_ID, tableid);
        values.put(SETTLEMENT_SHOP_NO, getShopNumber());

        // Insert the values into the table
        long newRowId = db.insert(INVOICE_SETTLEMENT_TABLE_NAME, null, values);
        // Log the result
        if (newRowId == -1) {
            Log.e("InsertSettlementAmount", "Failed to insert settlement amount.");
        } else {
            Log.d("InsertSettlementAmount", "Settlement amount inserted successfully. Row ID: " + newRowId);
        }
        // Close the database connection
        db.close();

        // Return true if the row was inserted successfully, false otherwise
        return newRowId != -1;
    }
    public String getShopNumber() {
        SQLiteDatabase db = this.getReadableDatabase();
        String shopNumber = null;
        String query = "SELECT " + COLUMN_SHOPNUMBER + " FROM " + TABLE_NAME_STD_ACCESS;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            shopNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOPNUMBER));
            cursor.close();
        }
        return shopNumber;
    }

    public Cursor getDistinctDrawerconfig(String paymentName) {


        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.OpenDrawer};
        String selection = DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME + " = ?";
        String[] selectionArgs = {paymentName};
        String orderBy = DatabaseHelper.OpenDrawer + " ASC";

        return db.query(true, DatabaseHelper.PAYMENT_METHOD_TABLE_NAME, columns, selection, selectionArgs, DatabaseHelper.OpenDrawer, null, orderBy, null);
    }

    public Cursor getAllReceipt() {
        SQLiteDatabase db = getReadableDatabase();
        String sortOrder = TRANSACTION_DATE_MODIFIED + " DESC, " + TRANSACTION_TIME_MODIFIED + " DESC";
        return db.query(TRANSACTION_HEADER_TABLE_NAME, null, null, null, null, null, sortOrder);
    }

    public Cursor getAllReceipts(String excludedTransactionIdsString) {
        SQLiteDatabase db = getReadableDatabase();

        // Check if excludedTransactionIdsString is null
        if (excludedTransactionIdsString == null) {
            // If it's null, return all receipts without excluding any transaction IDs
            String sortOrder = TRANSACTION_DATE_MODIFIED + " DESC, " + TRANSACTION_TIME_MODIFIED + " DESC";
            return db.query(TRANSACTION_HEADER_TABLE_NAME, null,
                    null, null, null, null, sortOrder);
        }

        // Convert the comma-separated string of transaction IDs to an array
        String[] excludedTransactionIds = excludedTransactionIdsString.split(",");

        // Trim each transaction ID to remove leading/trailing whitespaces
        for (int i = 0; i < excludedTransactionIds.length; i++) {
            excludedTransactionIds[i] = excludedTransactionIds[i].trim();
        }

        // Build the WHERE clause to exclude specific transaction IDs
        StringBuilder whereClauseBuilder = new StringBuilder();
        if (excludedTransactionIds.length > 0) {
            whereClauseBuilder.append(TRANSACTION_ID).append(" NOT IN (");
            for (int i = 0; i < excludedTransactionIds.length; i++) {
                if (i > 0) {
                    whereClauseBuilder.append(", ");
                }
                whereClauseBuilder.append("?");
            }
            whereClauseBuilder.append(")");
        }

        String sortOrder = TRANSACTION_DATE_MODIFIED + " DESC, " + TRANSACTION_TIME_MODIFIED + " DESC";

        // Execute the query with the WHERE clause and excluded transaction IDs
        return db.query(TRANSACTION_HEADER_TABLE_NAME, null,
                whereClauseBuilder.toString(),
                excludedTransactionIds.length > 0 ? excludedTransactionIds : null,
                null, null, sortOrder);
    }


    public Cursor getAllReceiptwithoutQR() {
        SQLiteDatabase db = getReadableDatabase();
        String sortOrder = TRANSACTION_DATE_MODIFIED + " DESC, " + TRANSACTION_TIME_MODIFIED + " DESC";
        String selection = "MRA_Response IS NULL OR MRA_Response = ?";
        String[] selectionArgs = {"Request Failed"};

        return db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public Cursor getAllReceiptWithQR() {
        SQLiteDatabase db = getReadableDatabase();
        String sortOrder = TRANSACTION_DATE_MODIFIED + " DESC, " + TRANSACTION_TIME_MODIFIED + " DESC";
        String selection = "MRA_Response IS NOT NULL AND MRA_Response NOT LIKE ?";
        String[] selectionArgs = {"Request Failed%"};

        return db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public Cursor searchReceipt(String selectedItem) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {_ID, TRANSACTION_TICKET_NO,TRANSACTION_TOTAL_TTC, TRANSACTION_DATE_CREATED};
        String selection = TRANSACTION_TICKET_NO + " LIKE ?";
        String[] selectionArgs = {"%" + selectedItem + "%"};
        String sortOrder = TRANSACTION_DATE_CREATED + " DESC";
        return db.query(TRANSACTION_HEADER_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }



    public Cursor getTransactionDetails(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                TRANSACTION_ID,
                TRANSACTION_DATE,
                QUANTITY,
                TRANSACTION_TERMINAL_NO,
                TOTAL_PRICE
                // Add more columns as needed
        };

        String selection = TRANSACTION_ID + "=?";
        String[] selectionArgs = { transactionId };

        return db.query(
                TRANSACTION_TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    public String getTransactionStatus(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { TRANSACTION_STATUS };
        String selection = TRANSACTION_TICKET_NO + "=?";
        String[] selectionArgs = { transactionId };

        Cursor cursor = db.query(TRANSACTION_HEADER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String status = null;

        if (cursor != null && cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndex(TRANSACTION_STATUS));
            cursor.close();
        }

        return status;
    }
    public Cursor getValidTransactions(String transactionId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_ID + "=? AND t." + TRANSACTION_STATUS + "='VALID' " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {transactionId};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // No transactions with the status "valid".
            return null;
        } else {
            // Return the Cursor object with transactions where the status is "valid".
            return cursor;
        }
    }

    public Cursor getSplittedTransactions(String transactionId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_ID + "=? AND t." + TRANSACTION_STATUS + "='Splitted' " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {transactionId};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // No transactions with the status "splitted".
            return null;
        } else {
            // Return the Cursor object with transactions where the status is "splitted".
            return cursor;
        }
    }

    public Cursor getAllTransactions(String transactionId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_ID + "=? " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {transactionId};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor == null || !cursor.moveToFirst()) {
            // There are no in-progress transactions.
            // Return zero.
            return null;
        } else {
            // There are in-progress transactions.
            // Return the Cursor object.
            return cursor;
        }
    }

    public boolean setShiftNumberInFinancialTable(int shiftnumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("CurrentDate", "Current date: " + currentDate);

        // Prepare the content values
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_SHIFT_NUMBER, shiftnumber);

        // Update financial entries without a shift number and with a specific condition
        String selection = "DATE(" + FINANCIAL_COLUMN_DATETIME + ") = ? AND " + TRANSACTION_SHIFT_NUMBER + " IS NULL";
        String[] selectionArgs = {currentDate};

        int rowsAffected = db.update(FINANCIAL_TABLE_NAME, values, selection, selectionArgs);
        Log.d("RowsAffected", "Number of rows updated: " + rowsAffected);

        db.close();

        if (rowsAffected > 0) {
            Log.d("SetShiftNumberInFinancial", "Shift number " + shiftnumber + " set for today's completed financial entries.");
            return true;
        } else {
            Log.e("SetShiftNumberInFinancial", "No completed financial entries updated for today's date.");
            return false;
        }
    }


    public boolean setShiftNumberInCashReportTable(int shiftNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("CurrentDate", "Current date: " + currentDate);

        // Prepare the content values
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_SHIFT_NUMBER, shiftNumber);

        // Update cash report entries without a shift number and with the current date
        String selection = "DATE(" + FINANCIAL_COLUMN_DATETIME + ") = ? AND " + TRANSACTION_SHIFT_NUMBER + " IS NULL";
        String[] selectionArgs = {currentDate};

        int rowsAffected = db.update(CASH_REPORT_TABLE_NAME, values, selection, selectionArgs);
        Log.d("RowsAffected", "Number of rows updated: " + rowsAffected);

        db.close();

        if (rowsAffected > 0) {
            Log.d("SetShiftNumberInCashReport", "Shift number " + shiftNumber + " set for today's cash report entries.");
            return true;
        } else {
            Log.e("SetShiftNumberInCashReport", "No cash report entries updated for today's date.");
            return false;
        }
    }
    public void updateCashReportTransactionId(String oldTransactionId, String newTransactionId, String shiftNumber, String posNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FINANCIAL_COLUMN_TransId, newTransactionId);

        // Define the WHERE clause
        String whereClause = FINANCIAL_COLUMN_TransId + " = ? AND " + TRANSACTION_SHIFT_NUMBER + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?";
        String[] whereArgs = {oldTransactionId, shiftNumber, posNum};

        int rowsAffected = db.update(
                CASH_REPORT_TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (rowsAffected > 0) {
            Log.d("Database Update", "Transaction ID in Cash Report updated successfully. Old ID: " + oldTransactionId + ", New ID: " + newTransactionId +
                    ", Shift Number: " + shiftNumber + ", POS Number: " + posNum);
        } else {
            Log.e("Database Update", "Failed to update Transaction ID in Cash Report. No matching rows found for ID: " + oldTransactionId +
                    ", Shift Number: " + shiftNumber + ", POS Number: " + posNum);
        }

        db.close();
    }


    public boolean setShiftNumberInCountingReportTable(int shiftnumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("CurrentDate", "Current date: " + currentDate);

        // Prepare the content values
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_SHIFT_NUMBER, shiftnumber);

        // Update counting report entries without a shift number and with the current date
        String selection = "DATE(" + COUNTING_REPORT_DATETIME + ") = ? AND " + TRANSACTION_SHIFT_NUMBER + " IS NULL";
        String[] selectionArgs = {currentDate};

        int rowsAffected = db.update(COUNTING_REPORT_TABLE_NAME, values, selection, selectionArgs);
        Log.d("RowsAffected", "Number of rows updated: " + rowsAffected);

        db.close();

        if (rowsAffected > 0) {
            Log.d("SetShiftNumberInCounting", "Shift number " + shiftnumber + " set for today's counting report entries.");
            return true;
        } else {
            Log.e("SetShiftNumberInCounting", "No counting report entries updated for today's date.");
            return false;
        }
    }


    public boolean setShiftNumberInHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Get the maximum shift number for today
        String getMaxShiftQuery = "SELECT COALESCE(MAX(" + TRANSACTION_SHIFT_NUMBER + "), 0) FROM " + TRANSACTION_HEADER_TABLE_NAME + " WHERE " + TRANSACTION_DATE_CREATED + " = ?";
        Cursor cursor = db.rawQuery(getMaxShiftQuery, new String[]{currentDate});
        int maxShiftNumber = 0;
        if (cursor.moveToFirst()) {
            maxShiftNumber = cursor.getInt(0);
        }
        cursor.close();

        // Increment the shift number
        int newShiftNumber = maxShiftNumber + 1;

        // Prepare the content values
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_SHIFT_NUMBER, newShiftNumber);

        // Update transactions without a shift number and with status 'Completed'
        String selection = TRANSACTION_DATE_CREATED + " = ? AND " + TRANSACTION_SHIFT_NUMBER + " IS NULL AND " + TRANSACTION_STATUS + " = 'Completed'";
        String[] selectionArgs = {currentDate};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, selection, selectionArgs);

        db.close();

        if (rowsAffected > 0) {
            Log.d("SetShiftNumber", "Shift number " + newShiftNumber + " set for today's completed transactions.");
            return true;
        } else {
            Log.e("SetShiftNumber", "No completed transactions updated for today's date.");
            return false;
        }
    }



    public Cursor getPaymentmethodamounts(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.SETTLEMENT_AMOUNT,SETTLEMENT_PAYMENT_NAME};
        String selection = DatabaseHelper.SETTLEMENT_INVOICE_ID + " = ?";
        String[] selectionArgs = {transactionId};
        String orderBy = DatabaseHelper.SETTLEMENT_PAYMENT_NAME + " ASC";

        return db.query(true, DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME, columns, selection, selectionArgs, DatabaseHelper.SETTLEMENT_PAYMENT_NAME, null, orderBy, null);
    }


    public boolean insertcashReturn(String cashReturn, String tenderamount, String transaction_id, String qrMra, String mrairn, String MRAMETHOD) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_CASH_RETURN, cashReturn);
        values.put(TRANSACTION_TOTAL_PAID, tenderamount);
        values.put(TRANSACTION_MRA_QR, qrMra);
        values.put(TRANSACTION_MRA_IRN, mrairn);
        values.put(TRANSACTION_MRA_Method, MRAMETHOD);

        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = {transaction_id};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, selection, selectionArgs);

        if (rowsAffected > 0) {
            Log.d("InsertCashReturn", "Cash return details updated successfully for transaction ID: " + transaction_id);
            return true;
        } else {
            Log.e("InsertCashReturn", "Failed to update cash return details for transaction ID: " + transaction_id);
            return false;
        }
    }



    public Cursor searchTransactions(String newText) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {_ID, TRANSACTION_TICKET_NO, TRANSACTION_DATE_CREATED, TRANSACTION_TOTAL_TTC};
        String selection = TRANSACTION_DATE_CREATED + " LIKE ?";
        String[] selectionArgs = {"%" + newText + "%"};
        String sortOrder = TRANSACTION_DATE_CREATED + " ASC";
        return db.query(TRANSACTION_HEADER_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor getAllDistinctReceipt() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[] { "DISTINCT " + TRANSACTION_DATE_CREATED };
        String sortOrder = TRANSACTION_DATE_CREATED + " DESC";
        return db.query( TRANSACTION_HEADER_TABLE_NAME, columns, null, null, null, null, sortOrder);
    }

    public String getTransactionTaxCode(String transactionId, String uniqueId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                new String[]{TRANSACTION_TAX_CODE},
                TRANSACTION_ID + " = ? AND " + _ID + " = ?",
                new String[]{transactionId, uniqueId},
                null,
                null,
                null
        );

        String transactionTaxCode = "";

        if (cursor.moveToFirst()) {
            transactionTaxCode = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_TAX_CODE));
        }

        cursor.close();
        return transactionTaxCode;
    }

    public String getLatestItemId(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME,
                new String[]{ITEM_ID},
                TRANSACTION_ID + " = ?",
                new String[]{transactionId},
                null,
                null,
                _ID + " DESC",
                "1"
        );

        String latestItemId = "";

        if (cursor.moveToFirst()) {
            latestItemId = cursor.getString(cursor.getColumnIndex(ITEM_ID));
        }

        cursor.close();
        return latestItemId;
    }
    public void updateStatusToVoid(String roomid, String tableid) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Update the transaction status to "Void" for the specified room and table
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, "CancelledOrder");

        // Update rows in TRANSACTION_HEADER_TABLE_NAME where TRANSACTION_STATUS is "InProgress" or "PRF" for the specified room and table
        int rowsUpdated = db.update(TRANSACTION_HEADER_TABLE_NAME, values,
                "(" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?) AND " +
                        ROOM_ID + " = ? AND " + TABLE_ID + " = ?",
                new String[]{"InProgress", "PRF", roomid, tableid});

        // Optionally, you can check if any rows were updated
        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Status updated to Void for " + rowsUpdated + " rows");
        } else {
            Log.d("DatabaseHelper", "No rows updated");
        }

        db.close();
    }

    public void deleteDataByInProgressStatus(String roomid, String tableid) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get the transaction IDs with status "InProgress" or "PRF" for the specified room and table
        String query = "SELECT " + TRANSACTION_TICKET_NO + " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE (" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?) AND " +
                ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"InProgress", "PRF", roomid, tableid});

        // Delete corresponding rows from both tables using the obtained transaction IDs
        while (cursor.moveToNext()) {
            String transactionId = cursor.getString(cursor.getColumnIndex(TRANSACTION_TICKET_NO));

            // Delete from TRANSACTION_HEADER_TABLE_NAME where TRANSACTION_TICKET_NO is the obtained transactionId
            db.delete(TRANSACTION_HEADER_TABLE_NAME, TRANSACTION_TICKET_NO + " = ?", new String[]{transactionId});

            // Delete from TRANSACTION_TABLE_NAME where TRANSACTION_ID is the obtained transactionId
            db.delete(TRANSACTION_TABLE_NAME, TRANSACTION_ID + " = ?", new String[]{transactionId});
        }

        cursor.close();
        db.close();
    }



    // Update the transaction ID in the transaction table
    // Update the transaction ID in the transaction table for a specific transaction


    public void updateHeaderTransactionIdInProgress(String oldTransactionId, String newTransactionId, String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_TICKET_NO, newTransactionId);

        // Define the WHERE clause
        String whereClause = TRANSACTION_TICKET_NO + " = ? AND " + TRANSACTION_STATUS + " IN (?, ?) AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] whereArgs = {oldTransactionId, DatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS, "PRF", roomId, tableId};

        // Update for the specified oldTransactionId, 'InProgress', and 'PRF'
        int rowsAffected = db.update(
                TRANSACTION_HEADER_TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (rowsAffected > 0) {
            Log.d("Database Update", "Header Transaction ID updated successfully. Old ID: " + oldTransactionId +
                    ", New ID: " + newTransactionId + ", Room ID: " + roomId + ", Table ID: " + tableId);

            updateRelatedTransactionId(newTransactionId, oldTransactionId);
        } else {
            Log.e("Database Update", "Failed to update Header Transaction ID. No matching rows found for ID: " + oldTransactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId + ", with status 'InProgress' or 'PRF'");
        }

        db.close();
    }


    public void updateTransactionIdForSelected(String oldTransactionId, String newTransactionId, String roomId, String tableId, int paidStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, newTransactionId);
        values.put(IS_PAID, paidStatus); // Assuming IS_PAID is the column name for the paid status

        // Define the WHERE clause
        String whereClause = TRANSACTION_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_STATUS + " = ? AND " + IS_SELECTED + " = ?";
        String[] whereArgs = {oldTransactionId, roomId, tableId, "VALID", "1"};

        int rowsAffected = db.update(
                TRANSACTION_TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (rowsAffected > 0) {
            Log.d("Database Update", "Transaction ID and Paid Status updated successfully for selected rows. Old ID: " + oldTransactionId +
                    ", New ID: " + newTransactionId + ", Room ID: " + roomId + ", Table ID: " + tableId +
                    ", Paid Status: " + paidStatus);
        } else {
            Log.e("Database Update", "Failed to update transaction ID and Paid Status for selected rows. No matching rows found for ID: " + oldTransactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId + ", with status VALID and selected is 1");
        }

        db.close();
    }
    public boolean updateRelatedTransactionId(String transactionTicketNo, String relatedTransactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RELATED_TRANSACTION_ID, relatedTransactionId);

        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionTicketNo };

        try {
            int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, selection, selectionArgs);

            if (rowsAffected > 0) {
                Log.d("UpdateRelatedTransId", "Successfully updated " + rowsAffected + " row(s) with related transaction ID.");
            } else {
                Log.d("UpdateRelatedTransId", "No rows updated. Transaction ticket number may not exist.");
            }

            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("UpdateRelatedTransId", "Error updating related transaction ID: " + e.getMessage(), e);
            return false;
        }
    }
    public String getRelatedTransactionId(String transactionTicketNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + RELATED_TRANSACTION_ID + " FROM " + TRANSACTION_HEADER_TABLE_NAME + " WHERE " + TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = { transactionTicketNo };

        Cursor cursor = db.rawQuery(query, selectionArgs);
        String relatedTransactionId = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                relatedTransactionId = cursor.getString(cursor.getColumnIndex(RELATED_TRANSACTION_ID));
            }
            cursor.close();
        }

        return relatedTransactionId;
    }


    public void updateOnresetTransactionTransactionIdInProgress(String oldTransactionId, String newTransactionId, String roomId, String tableId, int paidstatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, newTransactionId);
        values.put(IS_PAID, paidstatus); // Assuming IS_PAID is the column name for the paid status

        // Define the WHERE clause
        String whereClause = TRANSACTION_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_STATUS + " = ?";
        String[] whereArgs = {oldTransactionId, roomId, tableId, "VALID"};

        int rowsAffected = db.update(
                TRANSACTION_TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (rowsAffected > 0) {
            Log.d("Database Update1", "Transaction ID and Paid Status updated successfully. Old ID: " + oldTransactionId +
                    ", New ID: " + newTransactionId + ", Room ID: " + roomId + ", Table ID: " + tableId +
                    ", Paid Status: " + paidstatus);

        } else {
            Log.e("Database Update1", "Failed to update transaction ID and Paid Status. No matching rows found for ID: " + oldTransactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId + ", with status VALID");
        }

        db.close();
    }



    public void updateTransactionTransactionIdInProgress(String oldTransactionId, String newTransactionId, String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, newTransactionId);

        // Define the WHERE clause
        String whereClause = TRANSACTION_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ?";
        String[] whereArgs = {oldTransactionId, roomId, tableId};

        int rowsAffected = db.update(
                TRANSACTION_TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (rowsAffected > 0) {
            Log.d("Database Update2", "Transaction ID updated successfully. Old ID: " + oldTransactionId + ", New ID: " + newTransactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId);
        } else {
            Log.e("Database Update2", "Failed to update transaction ID. No matching rows found for ID: " + oldTransactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId);
        }

        db.close();
    }




    public Cursor getTransactionHeaderForReceipt(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_TICKET_NO + " = ?";

        if (transactionId != null) {
            return db.rawQuery(query, new String[]{transactionId});
        } else {
            // Handle the case where transactionId is null
            // You can choose to return null or an empty cursor, depending on your requirements
            return null;
        }
    }
    public Cursor getReceiptsByStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = ?";

        return db.rawQuery(query, new String[]{status});
    }

    public Cursor getDistinctStatusCursor() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT " + TRANSACTION_STATUS + " FROM " + TRANSACTION_HEADER_TABLE_NAME;
        return db.rawQuery(query, null);
    }


    public Cursor getReceiptsByStatusAndDate(String status, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                // specify the columns you need to retrieve
                _ID,
                TRANSACTION_TICKET_NO,
                TRANSACTION_TOTAL_TTC,
                TRANSACTION_DATE_CREATED

                // ... other columns ...
        };

        String selection = TRANSACTION_STATUS + " = ? AND " + TRANSACTION_DATE_CREATED + " = ?";
        String[] selectionArgs = {status, date};

        return db.query(TRANSACTION_HEADER_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public List<PaymentItem> getFilteredPaymentItems(String startDate, String endDate) {
        List<PaymentItem> paymentItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        // Query the database to get the filtered payment items
        Cursor cursor = db.rawQuery("SELECT " + SETTLEMENT_PAYMENT_NAME + ", SUM(" + SETTLEMENT_AMOUNT + ") AS totalAmount, " + SETTLEMENT_DATE_TRANSACTION +
                " FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " WHERE " + SETTLEMENT_DATE_TRANSACTION + " BETWEEN ? AND ?" +
                " GROUP BY " + SETTLEMENT_PAYMENT_NAME, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
                String transactionDateString = cursor.getString(cursor.getColumnIndex(SETTLEMENT_DATE_TRANSACTION));
                Date transactionDate = parseDate(transactionDateString);

                PaymentItem item = new PaymentItem(paymentName, totalAmount, transactionDate);
                paymentItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return paymentItems;
    }
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getTotalSaleAmount(String startDate, String endDate) {
        SQLiteDatabase db = getReadableDatabase();

        // Query the database to calculate the total sale amount
        Cursor cursor = db.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " BETWEEN ? AND ?", new String[]{startDate, endDate});

        double totalAmount = 0.0;

        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalAmount;
    }

    public List<PaymentItem> getFilteredPaymentItemsByCashier(String startDateString, String endDateString, String selectedCashier) {
        List<PaymentItem> paymentItems = new ArrayList<>();

        // Query the database to get the filtered payment items for the specified date range and cashier
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_PAYMENT_NAME + ", SUM(" + INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_AMOUNT + ") AS totalAmount, " +
                INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_DATE_TRANSACTION +
                " FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " INNER JOIN " + TRANSACTION_HEADER_TABLE_NAME + " ON " +
                INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_INVOICE_ID + " = " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_TICKET_NO +
                " WHERE " + INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " +
                INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_DATE_TRANSACTION + " <= ? AND " +
                TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_CASHIER_CODE + " = ?" +
                " GROUP BY " + INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_PAYMENT_NAME;


        Cursor cursor = db.rawQuery(query, new String[]{startDateString, endDateString, selectedCashier});

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
                String transactionDateString = cursor.getString(cursor.getColumnIndex(SETTLEMENT_DATE_TRANSACTION));
                Date transactionDate = parseDate(transactionDateString); // Parse the date string to a Date object

                PaymentItem item = new PaymentItem(paymentName, totalAmount, transactionDate);
                paymentItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return paymentItems;
    }

    public double getTotalSaleAmountByCashier(String startDateString, String endDateString, String selectedCashier) {
        double totalAmount = 0.0;

        // Query the database to calculate the total sale amount for the specified date range and cashier
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(" + INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_AMOUNT + ") " +
                "FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " INNER JOIN " + TRANSACTION_HEADER_TABLE_NAME + " ON " +
                INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_INVOICE_ID + " = " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_CASHIER_CODE +
                " WHERE " + INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " +
                INVOICE_SETTLEMENT_TABLE_NAME + "." + SETTLEMENT_DATE_TRANSACTION + " <= ? AND " +
                TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_CASHIER_CODE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{startDateString, endDateString, selectedCashier});

        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalAmount;
    }
    public double getSumOfCashReturnForCurrentDate() {
        double sumCashReturn = 0.0;

        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String currentDate = dateFormat.format(new Date());

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the current date for debugging
        Log.d("ReportPopupDialog", "Current Date: " + currentDate);

        // Replace "your_table_name" with the actual name of your table
        String query = "SELECT SUM(" + TRANSACTION_CASH_RETURN + ") AS totalCashReturn " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE DATE(" + TRANSACTION_DATE_TRANSACTION + ") = DATE('" + currentDate + "')";

        // Log the SQL query for debugging
        Log.d("ReportPopupDialog", "SQL Query: " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                sumCashReturn = cursor.getDouble(cursor.getColumnIndex("totalCashReturn"));
            }
            cursor.close();
        }

        db.close();

        return sumCashReturn;
    }

    public double getSumglobalCashReturnPerShift(String reportType, int shiftNumber) {
        double sumCashReturn = 0.0;

        // Assuming TRANSACTION_DATE_CREATED is the column that represents the date of the transaction header
        String dateFilter = getDateFilterBasedOnReportTypeForTransactionHeader(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum TRANSACTION_CASH_RETURN with shift number as a filter
        String query = "SELECT SUM(" + TRANSACTION_CASH_RETURN + ") AS sumCashReturn " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + dateFilter +
                " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of TRANSACTION_CASH_RETURN from the cursor
            sumCashReturn = cursor.getDouble(cursor.getColumnIndex("sumCashReturn"));
        }

        cursor.close();
        db.close();

        return sumCashReturn;
    }

    public double getSumglobalCashReturn(String reportType) {
        double sumCashReturn = 0.0;

        // Assuming TRANSACTION_DATE_CREATED is the column that represents the date of the transaction header
        String dateFilter = getDateFilterBasedOnReportTypeForTransactionHeader(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum TRANSACTION_CASH_RETURN
        String query = "SELECT SUM(" + TRANSACTION_CASH_RETURN + ") AS sumCashReturn " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // Retrieve the sum of TRANSACTION_CASH_RETURN from the cursor
            sumCashReturn = cursor.getDouble(cursor.getColumnIndex("sumCashReturn"));
        }

        cursor.close();
        db.close();

        return sumCashReturn;
    }
    public double getSumCashReturn(String reportType, int cashierId) {
        double sumCashReturn = 0.0;

        // Assuming TRANSACTION_DATE_CREATED is the column that represents the date of the transaction header
        String dateFilter = getDateFilterBasedOnReportTypeForTransactionHeader(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum TRANSACTION_CASH_RETURN
        String query = "SELECT SUM(" + TRANSACTION_CASH_RETURN + ") AS sumCashReturn " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + dateFilter + " AND " + TRANSACTION_CASHIER_CODE + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of TRANSACTION_CASH_RETURN from the cursor
            sumCashReturn = cursor.getDouble(cursor.getColumnIndex("sumCashReturn"));
        }

        cursor.close();
        db.close();

        return sumCashReturn;
    }
    public double getSumTenderAmount(String reportType, int cashierId) {
        double sumtenderamount = 0.0;

        // Assuming TRANSACTION_DATE_CREATED is the column that represents the date of the transaction header
        String dateFilter = getDateFilterBasedOnReportTypeForTransactionHeader(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum TRANSACTION_CASH_RETURN
        String query = "SELECT SUM(" + TRANSACTION_TOTAL_PAID + ") AS sumtenderamount " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + dateFilter + " AND " + TRANSACTION_CASHIER_CODE + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of TRANSACTION_CASH_RETURN from the cursor
            sumtenderamount = cursor.getDouble(cursor.getColumnIndex("sumtenderamount"));
        }

        cursor.close();
        db.close();

        return sumtenderamount;
    }

    public double getSumOfCashReturnForCurrentMonth() {
        double sumCashReturn = 0.0;

        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();

        // Calculate the start of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startOfMonth = dateFormat.format(calendar.getTime());

        // Calculate the end of the month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endOfMonth = dateFormat.format(calendar.getTime());

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the start and end dates for debugging
        Log.d("ReportPopupDialog", "Start of Month: " + startOfMonth);
        Log.d("ReportPopupDialog", "End of Month: " + endOfMonth);

        // Construct the SQL query to sum the cash return for the current month
        String query = "SELECT SUM(" + TRANSACTION_CASH_RETURN + ") AS totalCashReturn " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE DATE(" + TRANSACTION_DATE_TRANSACTION + ") BETWEEN DATE('" + startOfMonth + "') AND DATE('" + endOfMonth + "')";

        // Log the SQL query for debugging
        Log.d("ReportPopupDialog", "SQL Query: " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                sumCashReturn = cursor.getDouble(cursor.getColumnIndex("totalCashReturn"));
            }
            cursor.close();
        }

        db.close();

        return sumCashReturn;
    }
    public double getSumOfCashReturnForCurrentYear() {
        double sumCashReturn = 0.0;

        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();

        // Calculate the start of the year
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        String startOfYear = dateFormat.format(calendar.getTime());

        // Calculate the end of the year
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        String endOfYear = dateFormat.format(calendar.getTime());

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the start and end dates for debugging
        Log.d("ReportPopupDialog", "Start of Year: " + startOfYear);
        Log.d("ReportPopupDialog", "End of Year: " + endOfYear);

        // Construct the SQL query to sum the cash return for the current year
        String query = "SELECT SUM(" + TRANSACTION_CASH_RETURN + ") AS totalCashReturn " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE DATE(" + TRANSACTION_DATE_TRANSACTION + ") BETWEEN DATE('" + startOfYear + "') AND DATE('" + endOfYear + "')";

        // Log the SQL query for debugging
        Log.d("ReportPopupDialog", "SQL Query: " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                sumCashReturn = cursor.getDouble(cursor.getColumnIndex("totalCashReturn"));
            }
            cursor.close();
        }

        db.close();

        return sumCashReturn;
    }

    public double getSumOfCashReturnForCurrentWeek() {
        double sumCashReturn = 0.0;

        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();

        // Calculate the start of the week (assuming week starts on Sunday)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        String startOfWeek = dateFormat.format(calendar.getTime());

        // Calculate the end of the week
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        String endOfWeek = dateFormat.format(calendar.getTime());

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the start and end dates for debugging
        Log.d("ReportPopupDialog", "Start of Week: " + startOfWeek);
        Log.d("ReportPopupDialog", "End of Week: " + endOfWeek);

        // Construct the SQL query to sum the cash return for the current week
        String query = "SELECT SUM(" + TRANSACTION_CASH_RETURN + ") AS totalCashReturn " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE DATE(" + TRANSACTION_DATE_TRANSACTION + ") BETWEEN DATE('" + startOfWeek + "') AND DATE('" + endOfWeek + "')";

        // Log the SQL query for debugging
        Log.d("ReportPopupDialog", "SQL Query: " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                sumCashReturn = cursor.getDouble(cursor.getColumnIndex("totalCashReturn"));
            }
            cursor.close();
        }

        db.close();

        return sumCashReturn;
    }

    public List<CatDataModel> getDataBasedOnTransactionFamilleAndShiftNumber(String reportType, int shiftNumber) {
        List<CatDataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the date filter based on report type
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        // Step 1: Find transaction IDs with "Menu Repas" and 'VALID'
        String menuRepasTransactionIdsQuery = "SELECT DISTINCT t." + TRANSACTION_ID + " " +
                "FROM " + TRANSACTION_TABLE_NAME + " t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? " +
                "AND t." + TRANSACTION_FAMILLE + " = '0' " + // Assuming '0' represents "Menu Repas"
                "AND t." + TRANSACTION_STATUS + " = 'VALID'";

        Cursor menuRepasCursor = db.rawQuery(menuRepasTransactionIdsQuery, new String[]{String.valueOf(shiftNumber)});

        List<String> menuRepasTransactionIds = new ArrayList<>();
        if (menuRepasCursor.moveToFirst()) {
            do {
                menuRepasTransactionIds.add(menuRepasCursor.getString(menuRepasCursor.getColumnIndex(TRANSACTION_ID)));
            } while (menuRepasCursor.moveToNext());
        }
        menuRepasCursor.close();

        // Step 2: Get data for transactions with status 'Splitted'
        if (!menuRepasTransactionIds.isEmpty()) {
            String transactionIdsPlaceholder = TextUtils.join(",", Collections.nCopies(menuRepasTransactionIds.size(), "?"));

            String splittedQuery = "SELECT t." + TRANSACTION_FAMILLE + ", SUM(t." + QUANTITY + ") AS totalQuantity, SUM(t." + TOTAL_PRICE + ") AS totalPrice " +
                    "FROM " + TRANSACTION_TABLE_NAME + " t " +
                    "WHERE t." + TRANSACTION_ID + " IN (" + transactionIdsPlaceholder + ") " +
                    "AND t." + TRANSACTION_STATUS + " = 'Splitted' " +
                    "GROUP BY t." + TRANSACTION_FAMILLE;

            Cursor splittedCursor = db.rawQuery(splittedQuery, menuRepasTransactionIds.toArray(new String[0]));

            while (splittedCursor.moveToNext()) {
                String familleName = splittedCursor.getString(splittedCursor.getColumnIndex(TRANSACTION_FAMILLE));
                int totalQuantity = splittedCursor.getInt(splittedCursor.getColumnIndex("totalQuantity"));
                double totalPrice = splittedCursor.getDouble(splittedCursor.getColumnIndex("totalPrice"));

                // Create a DataModel object and add it to the list
                CatDataModel dataModel = new CatDataModel(familleName, totalPrice, totalQuantity);
                dataList.add(dataModel);
            }
            splittedCursor.close();
        }

        // Step 3: Get valid items excluding "Menu Repas"
        String validItemsQuery = "SELECT t." + TRANSACTION_FAMILLE + ", SUM(t." + QUANTITY + ") AS totalQuantity, SUM(t." + TOTAL_PRICE + ") AS totalPrice " +
                "FROM " + TRANSACTION_TABLE_NAME + " t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? " +
                "AND t." + TRANSACTION_STATUS + " = 'VALID' " +
                "AND t." + TRANSACTION_FAMILLE + " != '0' " + // Exclude "Menu Repas"
                "GROUP BY t." + TRANSACTION_FAMILLE;

        Cursor validItemsCursor = db.rawQuery(validItemsQuery, new String[]{String.valueOf(shiftNumber)});

        while (validItemsCursor.moveToNext()) {
            String familleName = validItemsCursor.getString(validItemsCursor.getColumnIndex(TRANSACTION_FAMILLE));
            int totalQuantity = validItemsCursor.getInt(validItemsCursor.getColumnIndex("totalQuantity"));
            double totalPrice = validItemsCursor.getDouble(validItemsCursor.getColumnIndex("totalPrice"));

            // Check if this item is already in the list (e.g., if it was added from 'Splitted' transactions)
            boolean found = false;
            for (CatDataModel item : dataList) {
                if (item.getCategorycode().equals(familleName)) {
                    // Update the existing item
                    item.setTotalQuantity(item.getTotalQuantity() + totalQuantity);
                    item.setTotalPrice(item.getTotalPrice() + totalPrice);
                    found = true;
                    break;
                }
            }

            // If not found, add a new item
            if (!found) {
                CatDataModel dataModel = new CatDataModel(familleName, totalPrice, totalQuantity);
                dataList.add(dataModel);
            }
        }

        validItemsCursor.close();
        db.close();

        return dataList;
    }


    public List<CatDataModel> getDataBasedOnTransactionFamilleAndCashiorid(String reportType, int cashierId) {
        List<CatDataModel> dataList = new ArrayList<>();

        // Get the date filter based on report type
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the query
        String query = "SELECT " + TRANSACTION_FAMILLE + ", SUM(t." + QUANTITY + ") AS totalQuantity, SUM(t." + TOTAL_PRICE + ") AS totalPrice " +
                "FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_CASHIER_CODE + " = ? " +
                "GROUP BY " + TRANSACTION_FAMILLE;

        // Log the SQL query for debugging
        Log.d("ReportPopupDialog", "SQL Query: " + query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            do {
                String familleName = cursor.getString(cursor.getColumnIndex(TRANSACTION_FAMILLE));
                int totalQuantity = cursor.getInt(cursor.getColumnIndex("totalQuantity"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));

                // Create a DataModel object and add it to the list
                CatDataModel CatdataModel = new CatDataModel(familleName, totalPrice, totalQuantity);
                dataList.add(CatdataModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dataList;
    }


    public List<CatDataModel> getDataBasedOnTransactionFamille(String reportType) {
        List<CatDataModel> dataList = new ArrayList<>();

        // Assuming TRANSACTION_DATE is the column that represents the date of the transaction
        String dateFilter = getDateFilterBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Replace "your_table_name" with the actual name of your table
        String query = "SELECT " + TRANSACTION_FAMILLE + ", SUM(" + QUANTITY + ") AS totalQuantity, SUM(" + TOTAL_PRICE + ") AS totalPrice " +
                "FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + dateFilter +
                " GROUP BY " + TRANSACTION_FAMILLE;

        // Log the SQL query for debugging
        Log.d("ReportPopupDialog", "SQL Query: " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String familleName = cursor.getString(cursor.getColumnIndex(TRANSACTION_FAMILLE));
                int totalQuantity = cursor.getInt(cursor.getColumnIndex("totalQuantity"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));

                // Create a DataModel object and add it to the list
                CatDataModel CatdataModel = new CatDataModel(familleName, totalPrice, totalQuantity);
                dataList.add(CatdataModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dataList;
    }

    // Inside your DatabaseHelper class
    private String getDatesFilterBasedOnReportType1(String reportType, String tableAlias) {
        String dateFilter = "";

        if (reportType.equals("Daily")) {
            dateFilter = "strftime('%Y-%m-%d', " + tableAlias + "." + SETTLEMENT_DATE_TRANSACTION + ") = date('now')";
        } else if (reportType.equals("Weekly")) {
            dateFilter = "strftime('%W', " + tableAlias + "." + SETTLEMENT_DATE_TRANSACTION + ") = strftime('%W', 'now')";
        } else if (reportType.equals("Monthly")) {
            dateFilter = "strftime('%Y-%m', " + tableAlias + "." + SETTLEMENT_DATE_TRANSACTION + ") = strftime('%Y-%m', 'now')";
        } else if (reportType.equals("Yearly")) {
            dateFilter = "strftime('%Y', " + tableAlias + "." + SETTLEMENT_DATE_TRANSACTION + ") = strftime('%Y', 'now')";
        }

        return dateFilter;
    }

    private String getDatesFilterBasedOnReportType(String reportType) {
        String dateFilter = "";



        if (reportType.equals("Daily")) {
            // Assuming the date is stored as "yyyy-MM-dd HH:mm:ss" in the database
            dateFilter = "strftime('%Y-%m-%d', " + SETTLEMENT_DATE_TRANSACTION + ") = date('now')";
        } else if (reportType.equals("Weekly")) {
            // Assuming the date is stored as "yyyy-MM-dd HH:mm:ss" in the database
            dateFilter = "strftime('%W', " + SETTLEMENT_DATE_TRANSACTION + ") = strftime('%W', 'now')";
        } else if (reportType.equals("Monthly")) {
            // Assuming the date is stored as "yyyy-MM-dd HH:mm:ss" in the database
            dateFilter = "strftime('%Y-%m', " + SETTLEMENT_DATE_TRANSACTION + ") = strftime('%Y-%m', 'now')";
        } else if (reportType.equals("Yearly")) {
            // Assuming the date is stored as "yyyy-MM-dd HH:mm:ss" in the database
            dateFilter = "strftime('%Y', " + SETTLEMENT_DATE_TRANSACTION + ") = strftime('%Y', 'now')";
        }

        return dateFilter;
    }

    private String getDateFilterBasedOnReportTypeForTransactionHeader(String reportType) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        switch (reportType) {
            case "Daily":
                return "DATE(" + TRANSACTION_DATE_CREATED + ") = '" + currentDate + "'";
            case "Weekly":
                calendar.add(Calendar.DAY_OF_YEAR, -7); // Subtract 7 days for a weekly range
                String oneWeekAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE_CREATED + ") >= '" + oneWeekAgo + "' AND DATE(" + TRANSACTION_DATE_CREATED + ") <= '" + currentDate + "'";
            case "Monthly":
                calendar.add(Calendar.MONTH, -1); // Subtract 1 month for a monthly range
                String oneMonthAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE_CREATED + ") >= '" + oneMonthAgo + "' AND DATE(" + TRANSACTION_DATE_CREATED + ") <= '" + currentDate + "'";
            case "Yearly":
                calendar.add(Calendar.YEAR, -1); // Subtract 1 year for a yearly range
                String oneYearAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE_CREATED + ") >= '" + oneYearAgo + "' AND DATE(" + TRANSACTION_DATE_CREATED + ") <= '" + currentDate + "'";
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
    }


    // Helper method to generate the date filter based on the report type
    private String getDateFilterBasedOnReportType(String reportType) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        switch (reportType) {
            case "Daily":
                return "DATE(" + TRANSACTION_DATE + ") = '" + currentDate + "'";
            case "Weekly":
                calendar.add(Calendar.DAY_OF_YEAR, -7); // Subtract 7 days for a weekly range
                String oneWeekAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE + ") >= '" + oneWeekAgo + "' AND DATE(" + TRANSACTION_DATE + ") <= '" + currentDate + "'";
            case "Monthly":
                calendar.add(Calendar.MONTH, -1); // Subtract 1 month for a monthly range
                String oneMonthAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE + ") >= '" + oneMonthAgo + "' AND DATE(" + TRANSACTION_DATE + ") <= '" + currentDate + "'";
            case "Yearly":
                calendar.add(Calendar.YEAR, -1); // Subtract 1 year for a yearly range
                String oneYearAgo = dateFormat.format(calendar.getTime());
                return "DATE(" + TRANSACTION_DATE + ") >= '" + oneYearAgo + "' AND DATE(" + TRANSACTION_DATE + ") <= '" + currentDate + "'";
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
    }


    public List<DataModel> getDataBasedOnReportType(String reportType) {
        List<DataModel> dataList = new ArrayList<>();

        // Assuming TRANSACTION_DATE is the column that represents the date of the transaction
        String dateFilter = getDateFilterBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Replace "your_table_name" with the actual name of your table
        String query = "SELECT " + LongDescription + ", SUM(" + QUANTITY + ") AS totalQuantity, SUM(" + TOTAL_PRICE + ") AS totalPrice " +
                "FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + dateFilter +
                " GROUP BY " + LongDescription;

        // Log the SQL query for debugging
        Log.d("ReportPopupDialog", "SQL Query: " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndex(LongDescription));
                int totalQuantity = cursor.getInt(cursor.getColumnIndex("totalQuantity"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));

                // Create a DataModel object and add it to the list
                DataModel dataModel = new DataModel(itemName, totalPrice, totalQuantity);
                dataList.add(dataModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public List<DataModel> getDataBasedOnReportTypeAndShift(String reportType, int shiftNumber) {
        List<DataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Implement logic to get the date filter based on report type
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        // Step 1: Find transaction IDs with "Menu Repas" and "VALID"
        String transactionIdsQuery = "SELECT DISTINCT t." + TRANSACTION_ID + " " +
                "FROM " + TRANSACTION_TABLE_NAME + " t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? " +
                "AND t." + LongDescription + " = 'Menu Repas' " +
                "AND t." + TRANSACTION_STATUS + " = 'VALID'";

        Cursor transactionIdsCursor = db.rawQuery(transactionIdsQuery, new String[]{String.valueOf(shiftNumber)});

        List<String> transactionIds = new ArrayList<>();
        if (transactionIdsCursor.moveToFirst()) {
            do {
                transactionIds.add(transactionIdsCursor.getString(transactionIdsCursor.getColumnIndex(TRANSACTION_ID)));
            } while (transactionIdsCursor.moveToNext());
        }
        transactionIdsCursor.close();

        // Create a map to accumulate quantities and prices
        Map<String, DataModel> dataMap = new HashMap<>();

        // Step 2: Get data for transactions with status 'Splitted'
        if (!transactionIds.isEmpty()) {
            // Prepare placeholders for the IN clause
            String transactionIdsPlaceholder = TextUtils.join(",", Collections.nCopies(transactionIds.size(), "?"));

            String splittedQuery = "SELECT t." + LongDescription + ", SUM(t." + QUANTITY + ") AS totalQuantity, SUM(t." + TOTAL_PRICE + ") AS totalPrice " +
                    "FROM " + TRANSACTION_TABLE_NAME + " t " +
                    "WHERE t." + TRANSACTION_ID + " IN (" + transactionIdsPlaceholder + ") " +
                    "AND t." + TRANSACTION_STATUS + " = 'Splitted' " +
                    "GROUP BY t." + LongDescription;

            Cursor cursor = db.rawQuery(splittedQuery, transactionIds.toArray(new String[0]));

            if (cursor.moveToFirst()) {
                do {
                    String itemName = cursor.getString(cursor.getColumnIndex(LongDescription));
                    int totalQuantity = cursor.getInt(cursor.getColumnIndex("totalQuantity"));
                    double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));

                    // Add or update the item in the map
                    DataModel existingData = dataMap.get(itemName);
                    if (existingData != null) {
                        existingData.setTotalQuantity(existingData.getTotalQuantity() + totalQuantity);
                        existingData.setTotalPrice(existingData.getTotalPrice() + totalPrice);
                    } else {
                        DataModel dataModel = new DataModel(itemName, totalPrice, totalQuantity);
                        dataMap.put(itemName, dataModel);
                    }

                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        // Step 3: Get valid items excluding "Menu Repas"
        String validItemsQuery = "SELECT t." + LongDescription + ", SUM(t." + QUANTITY + ") AS totalQuantity, SUM(t." + TOTAL_PRICE + ") AS totalPrice " +
                "FROM " + TRANSACTION_TABLE_NAME + " t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? " +
                "AND t." + TRANSACTION_STATUS + " = 'VALID' " +
                "AND t." + LongDescription + " != 'Menu Repas' " +
                "GROUP BY t." + LongDescription;

        Cursor validItemsCursor = db.rawQuery(validItemsQuery, new String[]{String.valueOf(shiftNumber)});

        if (validItemsCursor.moveToFirst()) {
            do {
                String itemName = validItemsCursor.getString(validItemsCursor.getColumnIndex(LongDescription));
                int totalQuantity = validItemsCursor.getInt(validItemsCursor.getColumnIndex("totalQuantity"));
                double totalPrice = validItemsCursor.getDouble(validItemsCursor.getColumnIndex("totalPrice"));

                // Add or update the item in the map
                DataModel existingData = dataMap.get(itemName);
                if (existingData != null) {
                    existingData.setTotalQuantity(existingData.getTotalQuantity() + totalQuantity);
                    existingData.setTotalPrice(existingData.getTotalPrice() + totalPrice);
                } else {
                    DataModel dataModel = new DataModel(itemName, totalPrice, totalQuantity);
                    dataMap.put(itemName, dataModel);
                }

            } while (validItemsCursor.moveToNext());
        }

        validItemsCursor.close();
        db.close();

        // Convert the map to a list
        dataList.addAll(dataMap.values());

        return dataList;
    }





    public List<PaymentMethodDataModel> getPaymentMethodDataBasedOnReportTypeAndShift(String reportType, int shiftNumber) {
        List<PaymentMethodDataModel> paymentMethodDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Implement logic to get the date filter based on report type
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        // Use shift number in your query
        String query = "SELECT i." + SETTLEMENT_PAYMENT_NAME + ", COUNT(*) AS paymentCount, SUM(i." + SETTLEMENT_AMOUNT + ") AS totalAmount " +
                "FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " i " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h ON i." + SETTLEMENT_INVOICE_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? " +
                "GROUP BY i." + SETTLEMENT_PAYMENT_NAME;


        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                int paymentCount = cursor.getInt(cursor.getColumnIndex("paymentCount"));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));

                // Create a PaymentMethodDataModel object and add it to the list
                PaymentMethodDataModel paymentMethodDataModel = new PaymentMethodDataModel(paymentName, paymentCount, totalAmount);
                paymentMethodDataList.add(paymentMethodDataModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return paymentMethodDataList;
    }

    public List<DataModel> getDataForReportTypeAndCashier(String reportType, int cashierId) {

        List<DataModel> dataList = new ArrayList<>();

        // Assuming TRANSACTION_DATE is the column that represents the date of the transaction
        String dateFilter = getDateFilterBasedOnReportType(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Query the database to fetch data based on report type and cashier ID
        String query = "SELECT " + LongDescription + ", SUM(" + QUANTITY + ") AS totalQuantity, SUM(" + TOTAL_PRICE + ") AS totalPrice " +
                "FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_CASHIER_CODE + " = ? " +
                "GROUP BY " + LongDescription;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndex(LongDescription));
                int totalQuantity = cursor.getInt(cursor.getColumnIndex("totalQuantity"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"));
                // Populate data model from cursor and add to list
                DataModel data = new DataModel(itemName, totalPrice, totalQuantity);
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return dataList;
    }

    public List<PaymentMethodDataModel> getPaymentMethodDataForReportTypeAndShiftAndCashier(String reportType, int shiftNumber, int cashierId) {
        List<PaymentMethodDataModel> dataList = new ArrayList<>();

        // Query the database to fetch payment method data based on report type, shift number, and cashier ID
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        String query = "SELECT t." + SETTLEMENT_PAYMENT_NAME + ", COUNT(*) AS paymentCount, SUM(t." + SETTLEMENT_AMOUNT + ") AS totalAmount " +
                "FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS h ON t." + SETTLEMENT_INVOICE_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? AND h." + TRANSACTION_CASHIER_CODE + " = ? " +
                "GROUP BY t." + SETTLEMENT_PAYMENT_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shiftNumber), String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                int paymentCount = cursor.getInt(cursor.getColumnIndex("paymentCount"));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
                // Populate data model from cursor and add to list
                PaymentMethodDataModel data = new PaymentMethodDataModel(paymentName, paymentCount, totalAmount);
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return dataList;
    }

    public List<PaymentMethodDataModel> getPaymentMethodDataForReportTypeAndCashier(String reportType, int cashierId) {
        List<PaymentMethodDataModel> dataList = new ArrayList<>();

        // Query the database to fetch payment method data based on report type and cashier ID
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        String query = "SELECT t." + SETTLEMENT_PAYMENT_NAME + ", COUNT(*) AS paymentCount, SUM(t." + SETTLEMENT_AMOUNT + ") AS totalAmount " +
                "FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS h ON t." + SETTLEMENT_INVOICE_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_CASHIER_CODE + " = ? " +
                "GROUP BY t." + SETTLEMENT_PAYMENT_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                int paymentCount = cursor.getInt(cursor.getColumnIndex("paymentCount"));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
                // Populate data model from cursor and add to list
                PaymentMethodDataModel data = new PaymentMethodDataModel(paymentName, paymentCount, totalAmount);
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return dataList;
    }






    public double getTotalTaxBasedOnReportTypeAndCashier(String reportType, int cashierId) {
        // Query to get the total tax based on report type and cashier ID
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");
        double totalTax = 0.0;
        String query = "SELECT SUM(" + VAT + ") " +
                "FROM " + TRANSACTION_TABLE_NAME+ " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                " WHERE " + dateFilter + " AND h." + TRANSACTION_CASHIER_CODE + " = ? " ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            totalTax = cursor.getDouble(0);
        }
        cursor.close();
        db.close();

        return totalTax;



    }
    public double getTotalTaxBasedOnReportType(String reportType) {
        double totalTax = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        String dateFilter = getDateFilterBasedOnReportType(reportType);

        String query = "SELECT SUM(" + VAT + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + dateFilter;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            totalTax = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalTax;
    }
    public double getTotalAmountBasedOnReportType(String reportType) {
        double totalAmount = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        String dateFilter = getDateFilterBasedOnReportType(reportType);

        String query = "SELECT SUM(" + TRANSACTION_TOTAL_HT_A  + ") FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + dateFilter;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalAmount;
    }
    public double getTotalAmountBasedOnReportTypeAndCashier(String reportType, int cashierId) {
        double totalAmount = 0.0;

        // Use the new date filter method with table alias
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        String query = "SELECT SUM(t." + TRANSACTION_TOTAL_HT_A + ") " +
                "FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_CASHIER_CODE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalAmount;
    }


    public double getTotalTaxBasedOnReportTypeAndShift(String reportType, int shiftNumber) {
        double totalTax = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the date filter based on the report type
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");
        // Query to calculate the total VAT based on the report type and shift number
        // Query to calculate the total VAT based on the report type, shift number, and status
        String query = "SELECT SUM(t." + VAT + ") " +
                "FROM " + TRANSACTION_TABLE_NAME + " t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? " +
                " AND t." + TRANSACTION_STATUS + " = 'VALID'"; // Added status filter


        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shiftNumber)});
        if (cursor.moveToFirst()) {
            totalTax = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalTax;
    }

    public double getTotalAmountBasedOnReportTypeAndShift(String reportType, int shiftNumber) {
        double totalAmount = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the date filter based on the report type
        String dateFilter = getDatesFilterBasedOnReportType1(reportType, "h");

        // Query to calculate the total amount based on the report type, shift number, and status
        String query = "SELECT SUM(t." + TRANSACTION_TOTAL_HT_A + ") " +
                "FROM " + TRANSACTION_TABLE_NAME + " t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                "WHERE " + dateFilter + " AND h." + TRANSACTION_SHIFT_NUMBER + " = ? " +
                " AND t." + TRANSACTION_STATUS + " = 'VALID'"; // Added status filter


        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shiftNumber)});
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalAmount;
    }

    public boolean updateQRCodeNum(String tableName, String id, String paymentMethod, String qrCodeNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QR_CODE_NUM, qrCodeNum);

        String whereClause = COLUMN_PAYMENT_ID + " = ? AND " + COLUMN_PAYMENT_METHOD + " = ?";
        String[] whereArgs = {id, paymentMethod};

        int rowsAffected = db.update(tableName, values, whereClause, whereArgs);
        db.close();

        return rowsAffected > 0;
    }


    // Step 1: Define an update method in the DatabaseHelper class
    public void updateLocalData(Item localData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Step 2: Map the properties of the Item object to the database columns
        values.put(Barcode, localData.getBarcode());
        values.put(Name, localData.getName());
        values.put(DESC, localData.getDescription());
        values.put(Category, localData.getCategory());
        values.put(Quantity, localData.getQuantity());
        values.put(Department, localData.getDepartment());
        values.put(LongDescription, localData.getLongDescription());
        values.put(SubDepartment, localData.getSubDepartment());
        values.put(Price, localData.getPrice());
        values.put(VAT, localData.getVAT());
        values.put(ExpiryDate, localData.getExpiryDate());
        values.put(AvailableForSale, localData.getAvailableForSale());
        values.put(SoldBy, localData.getSoldBy());
        values.put(Image, localData.getImage());
        values.put(SKU, localData.getSKU());
        values.put(Variant, localData.getVariant());
        values.put(Cost, localData.getCost());
        values.put(Weight, localData.getWeight());
        values.put(UserId, localData.getUserId());
        values.put(DateCreated, localData.getDateCreated());
        values.put(LastModified, localData.getLastModified());

        // Step 3: Perform the update operation
        // Replace "your_table" with the actual table name in the SQLite database
        // Assuming you have an '_ID' column as the primary key to uniquely identify records
        int rowsAffected = db.update("your_table", values, _ID + "=?", new String[]{String.valueOf(localData.getId())});

        // Step 4: Check the result of the update operation
        if (rowsAffected > 0) {
            // Update successful
            Log.d("DATABASE", "Local data updated successfully: " + localData.getId());
        } else {
            // Update failed
            Log.e("DATABASE", "Failed to update local data: " + localData.getId());
        }

        // Step 5: Close the database connection
        db.close();
    }
    public int getQuantity(String transactionId, String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        int quantity = 0; // Default value if no match is found

        // Define the query to retrieve the quantity based on transactionId, barcode, and sentToKitchen status 0
        String query = "SELECT " + QUANTITY +
                " FROM " + TRANSACTION_TABLE_NAME +
                " WHERE " + TRANSACTION_ID + " = ? AND " +
                TRANSACTION_BARCODE + " = ? AND " +
                TRANSACTION_SentToKitchen + " = ?";

        // Use try-with-resources to ensure the cursor is closed after use
        try (Cursor cursor = db.rawQuery(query, new String[]{transactionId, barcode, "0"})) {
            if (cursor.moveToFirst()) {
                quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                // Log the result
                Log.d("DatabaseHelper", "Quantity for transaction ID: " + transactionId + ", barcode: " + barcode + ", sentToKitchen: 0 is " + quantity);
            } else {
                Log.d("DatabaseHelper", "No data found for transaction ID: " + transactionId + ", barcode: " + barcode + ", sentToKitchen: 0");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving quantity", e);
        }

        return quantity;
    }

    public void updateTransaction(int itemId, double newpriceWithoutVat, int newQuantity, double totaltaxbeforedisc, double totaltaxafterisc, String transactionid, double newTotalPrice, double newTotalDisc, double newVat, String vatType, String roomid, String tableid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.d("newQuantity1", String.valueOf(newQuantity));
        String statusType= getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        // Get current date and time
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Format date and time
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);

        values.put(TRANSACTION_TIME_TRANSACTION, formattedTime);
        values.put(TRANSACTION_DATE_MODIFIED, formattedDate);
        values.put(TRANSACTION_TIME_MODIFIED, formattedTime);
        values.put(QUANTITY, newQuantity);
        values.put(TRANSACTION_QUANTITY, newQuantity);
        values.put(TOTAL_PRICE, roundUp(newTotalPrice, 2));
        values.put(TRANSACTION_TOTAL_TTC, roundUp(newTotalPrice, 2));
        values.put(VAT, roundUp(newVat, 2));
        values.put(TRANSACTION_TOTAL_HT_A, roundUp(newpriceWithoutVat, 2));
        values.put(TRANSACTION_VAT_BEFORE_DISC, roundUp(totaltaxbeforedisc, 2));
        values.put(TRANSACTION_VAT_AFTER_DISC, roundUp(totaltaxafterisc, 2));
        values.put(TRANSACTION_TOTAL_DISCOUNT, roundUp(newTotalDisc, 2));
        values.put(VAT_Type, vatType);

        String selection = TRANSACTION_ID + " = ? AND " + ITEM_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_SentToKitchen + " = ?";
        String[] selectionArgs = {latesttransId, String.valueOf(itemId), roomid, tableid, "0"};

        // Retrieve the _ID of the row to be updated
        String idQuery = "SELECT " + _ID + " FROM " + TRANSACTION_TABLE_NAME + " WHERE " + selection;
        int updatedRowId = -1;

        try (Cursor cursor = db.rawQuery(idQuery, selectionArgs)) {
            if (cursor.moveToFirst()) {
                updatedRowId = cursor.getInt(cursor.getColumnIndex(_ID));
            }
        }

        int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, selection, selectionArgs);

        // Log the update operation
        if (rowsAffected > 0) {
            Log.d("DatabaseHelper", "Transaction updated successfully. Transaction ID: " + latesttransId + ", Item ID: " + itemId + ", _ID: " + updatedRowId);
        } else {
            Log.d("DatabaseHelper", "No transaction updated. Transaction ID: " + latesttransId + ", Item ID: " + itemId);
        }
    }





    public boolean areAllItemsSelected(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Get the transaction ID for the specified room and table with "InProgress" status
            cursor = getTransactionsByRoomAndTable("InProgress", roomId, tableId);

            if (cursor.moveToFirst()) {
                int transactionIdColumnIndex = cursor.getColumnIndex(TRANSACTION_TICKET_NO);
                int transactionId = cursor.getInt(transactionIdColumnIndex);

                // Check if all items for the obtained transaction ID are selected
                String selectedItemCountQuery = "SELECT COUNT(" + TRANSACTION_TABLE_NAME + "." + ITEM_ID + ") AS selectedCount" +
                        " FROM " + TRANSACTION_TABLE_NAME +
                        " WHERE " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_ID + " = ? AND " +
                        TRANSACTION_TABLE_NAME + "." + IS_SELECTED + " = 1";

                String[] selectedItemCountArgs = {String.valueOf(transactionId)};
                Cursor selectedItemCountCursor = db.rawQuery(selectedItemCountQuery, selectedItemCountArgs);

                if (selectedItemCountCursor.moveToFirst()) {
                    int selectedCount = selectedItemCountCursor.getInt(selectedItemCountCursor.getColumnIndex("selectedCount"));
                    return selectedCount > 0; // Return true if there is at least one selected item
                }
            }

            // If no transaction or selected item found, return false
            return false;
        } finally {
            // Close the cursor and the database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


    public void setItemsUnselected(String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause to update specific rows
        String whereClause = "EXISTS (SELECT 1 FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_HEADER_TABLE_NAME + "." + ROOM_ID + " = ?" +
                " AND " + TRANSACTION_HEADER_TABLE_NAME + "." + TABLE_ID + " = ?" +
                " AND " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_TICKET_NO + " = " +
                TRANSACTION_TABLE_NAME + "." + TRANSACTION_ID +
                " AND " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_STATUS + " IN (?, ?))";

        String[] whereArgs = {roomId, tableId, "InProgress", "PRF"};

        // Log roomId and tableId for debugging
        Log.d("setItemsUnselected", "RoomId: " + roomId + ", TableId: " + tableId);
        Log.d("setItemsUnselected", "RoomId: " + roomId + ", TableId: " + tableId);
        // Construct the SQL UPDATE query
        ContentValues values = new ContentValues();
        values.put(IS_SELECTED, 0);

        // Log the update query for debugging
        Log.d("setItemsUnselected", "UPDATE " + TRANSACTION_TABLE_NAME +
                " SET " + IS_SELECTED + " = 0 WHERE " + whereClause);

        // Execute the update
        int rowsUpdated = db.update(TRANSACTION_TABLE_NAME, values, whereClause, whereArgs);

        // Log the number of rows updated
        Log.d("setItemsUnselected", "Rows Updated: " + rowsUpdated);

        // Close the database connection
        db.close();
    }





    public boolean areNoItemsSelected(String roomId, String tableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Get the transaction ID for the specified room and table with "InProgress" status
            cursor = getTransactionsByRoomAndTable("InProgress", roomId, tableId);

            if (cursor.moveToFirst()) {
                int transactionIdColumnIndex = cursor.getColumnIndex(TRANSACTION_ID);
                int transactionId = cursor.getInt(transactionIdColumnIndex);

                // Check if there is at least one selected item for the obtained transaction ID
                String selectedItemCountQuery = "SELECT COUNT(" + TRANSACTION_TABLE_NAME + "." + ITEM_ID + ") AS selectedCount" +
                        " FROM " + TRANSACTION_TABLE_NAME +
                        " WHERE " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_ID + " = ? AND " +
                        TRANSACTION_TABLE_NAME + "." + IS_SELECTED + " = 1";

                String[] selectedItemCountArgs = {String.valueOf(transactionId)};
                Cursor selectedItemCountCursor = db.rawQuery(selectedItemCountQuery, selectedItemCountArgs);

                if (selectedItemCountCursor.moveToFirst()) {
                    int selectedCount = selectedItemCountCursor.getInt(selectedItemCountCursor.getColumnIndex("selectedCount"));
                    return selectedCount == 0; // Return true if there are no selected items
                }
            }

            // If no transaction found, return true
            return true;
        } finally {
            // Close the cursor and the database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


    public void updatePaidStatusForSelectedRowsById(String transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_PAID, 1);

        // Define the WHERE clause to update rows with the specified TRANSACTION_ID and IS_SELECTED is 0
        String selection = TRANSACTION_ID + " = ? AND " + IS_SELECTED + " = ?";
        String[] selectionArgs = {transactionId, "1"};

        // Update the rows
        db.update(TRANSACTION_TABLE_NAME, values, selection, selectionArgs);

        // Close the database connection
        db.close();
    }
    public void updatePaidStatusForNotSelectedRows(String transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_PAID, 1);

        // Define the WHERE clause to update rows with the specified TRANSACTION_ID and IS_SELECTED is 0
        String selection = TRANSACTION_ID + " = ? AND " + IS_SELECTED + " = ?";
        String[] selectionArgs = {transactionId, "0"};

        // Update the rows
        db.update(TRANSACTION_TABLE_NAME, values, selection, selectionArgs);

        // Close the database connection
        db.close();
    }




    public void updatePaidStatusForSelectedRows(String transactionId, String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_PAID, 1);

        // Define the WHERE clause to update rows with the specified transactionId, roomId, tableId, IS_SELECTED is 1,
        // and status is either "InProgress" or "Completed"
        String selection = TRANSACTION_ID + " = ? AND " + ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + IS_SELECTED + " = ? AND (" + TRANSACTION_STATUS + " = ? OR " + TRANSACTION_STATUS + " = ?)";
        String[] selectionArgs = {transactionId, roomId, tableId, "1", "InProgress", "PRF"};

        // Update the rows
        int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, selection, selectionArgs);

        if (rowsAffected > 0) {
            Log.d("Database Update", "Paid status updated successfully for selected rows. Transaction ID: " + transactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId);
        } else {
            Log.e("Database Update", "Failed to update paid status for selected rows. No matching rows found for Transaction ID: " + transactionId +
                    ", Room ID: " + roomId + ", Table ID: " + tableId + ", with status 'InProgress' or 'Completed' and selected is 1");
        }

        // Close the database connection
        db.close();
    }


    public void updateselectedtatusForSelectedRows(String roomId, String tableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SELECTED, 0);

        // Define the WHERE clause to update rows with the specified roomId, tableId, and IS_SELECTED is 1
        String selection = ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + IS_SELECTED + " = ?";
        String[] selectionArgs = {String.valueOf(roomId), String.valueOf(tableId), "1"};

        // Update the rows
        db.update(TRANSACTION_TABLE_NAME, values, selection, selectionArgs);

        // Close the database connection
        db.close();
    }

    public boolean updateItemSelectedperuniqueid(String itemId, boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SELECTED, isSelected ? 1 : 0);

        int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, _ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();

        boolean isUpdateSuccessful = rowsAffected > 0;

        if (isUpdateSuccessful) {
            Log.d("DatabaseUpdate", "Item with ID " + itemId + " updated successfully. IsSelected: " + isSelected);
        } else {
            Log.e("DatabaseUpdate", "Failed to update item with ID " + itemId + ". IsSelected: " + isSelected);
        }

        return isUpdateSuccessful;
    }
    public boolean updateItemSelected(long itemId, boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SELECTED, isSelected ? 1 : 0);

        int rowsAffected = db.update(TRANSACTION_TABLE_NAME, values, _ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();

        boolean isUpdateSuccessful = rowsAffected > 0;

        if (isUpdateSuccessful) {
            Log.d("DatabaseUpdate", "Item with ID " + itemId + " updated successfully. IsSelected: " + isSelected);
        } else {
            Log.e("DatabaseUpdate", "Failed to update item with ID " + itemId + ". IsSelected: " + isSelected);
        }

        return isUpdateSuccessful;
    }



    public void updateCounter(int newCounter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_MRA_Invoice_Counter, newCounter);

        // Update the counter value in the row
        db.update(TRANSACTION_HEADER_TABLE_NAME, values, null, null);

        db.close(); // Close the database connection
    }

    public List<Buyer> getAllBuyers() {
        List<Buyer> buyerList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(BUYER_TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract buyer information from the cursor
                    String name = cursor.getString(cursor.getColumnIndex(BUYER_NAME));
                    String othername = cursor.getString(cursor.getColumnIndex(BUYER_Other_NAME));
                    String tan = cursor.getString(cursor.getColumnIndex(BUYER_TAN));
                    String companyName = cursor.getString(cursor.getColumnIndex(BUYER_Company_name));
                    String brn = cursor.getString(cursor.getColumnIndex(BUYER_BRN));
                    String businessAddr = cursor.getString(cursor.getColumnIndex(BUYER_BUSINESS_ADDR));
                    String buyerType = cursor.getString(cursor.getColumnIndex(BUYER_TYPE));
                    String buyerProfile = cursor.getString(cursor.getColumnIndex(BUYER_Profile));
                    String nic = cursor.getString(cursor.getColumnIndex(BUYER_NIC));
                    String buyerPricelevel = cursor.getString(cursor.getColumnIndex(BUYER_PriceLevel));
                    String buyercashier = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_id));
                    String buyerdatecreated = cursor.getString(cursor.getColumnIndex(BUYER_DATE_CREATED));
                    String buyerlastmodified = cursor.getString(cursor.getColumnIndex(BUYER_LAST_MODIFIED));

                    Buyer buyer = new Buyer(name,othername, tan, brn, businessAddr, buyerType,buyerProfile, nic,companyName,buyerPricelevel,buyercashier,buyerdatecreated,buyerlastmodified);
                    buyerList.add(buyer);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return buyerList;
    }

    public boolean addRoom(Rooms room) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues roomValues = new ContentValues();
        roomValues.put("room_name", room.getRoomName());
        roomValues.put("table_count", room.getTableCount());

        // Insert data into the ROOMS table
        long roomId = db.insert(ROOMS, null, roomValues);

        // Check if the room insertion was successful
        if (roomId == -1) {
            db.close();
            return false;
        }

        // Insert data into the TABLES table for each table in the room
        for (int i = 1; i <= room.getTableCount(); i++) {
            ContentValues tableValues = new ContentValues();
            tableValues.put("room_id", roomId);  // Associate the table with the room using the ROOM_ID column
            tableValues.put("table_number", i);  // You can adjust this based on your table numbering logic
            tableValues.put("seat_count", 0);    // Adjust as needed
            tableValues.put("waiter_name", "");  // Adjust as needed

            // Insert data into the TABLES table
            long result = db.insert(TABLES, null, tableValues);

            // Check if the table insertion was successful
            if (result == -1) {
                db.close();
                return false;
            }
        }

        db.close();

        return true;
    }

    // Inside your DatabaseHelper class or wherever you handle database operations
    public int getTableCountForRoom(String roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int tableCount = 0;

        Cursor cursor = null;
        try {
            String[] projection = { "table_count" };
            String selection = "id=?";
            String[] selectionArgs = { roomId };

            cursor = db.query("rooms", projection, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                tableCount = cursor.getInt(cursor.getColumnIndexOrThrow("table_count"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return tableCount;
    }

    public boolean addBuyer(Buyer buyer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Buyer_Name", buyer.getNames());
        values.put(BUYER_Other_NAME, buyer.getNames());
        values.put("Buyer_TAN", buyer.getTan());
        values.put("companyName", buyer.getCompanyName());
        values.put("Buyer_BRN", buyer.getBrn());
        values.put("Adresse", buyer.getBusinessAddr());
        values.put("Buyer_Type", buyer.getBuyerType());
        values.put("Buyer_NIC", buyer.getNic());
        values.put(BUYER_Profile, buyer.getProfile());
        values.put(BUYER_PriceLevel, buyer.getPriceLevel());
        values.put(COLUMN_CASHOR_id, buyer.getCashiorId());
        values.put(BUYER_DATE_CREATED, buyer.getDatecreated());
        values.put(BUYER_LAST_MODIFIED, buyer.getLastmodified());



        long result = db.insert("Buyer_Table", null, values);

        db.close();

        return result != -1;
    }

    public int getSumNumberOfCoversByReportTypePerShift(String reportType, int shiftNumber) {
        int sumNumberOfCovers = 0;

        // Get the date filter based on the report type
        String dateFilter = getDateFilterBasedOnReportTypeForTransactionHeader(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum NUMBER_OF_COVERS including shift number filter
        String query = "SELECT SUM(" + NUMBER_OF_COVERS + ") AS sumNumberOfCovers " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + dateFilter + " AND " + TRANSACTION_SHIFT_NUMBER + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shiftNumber)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of NUMBER_OF_COVERS from the cursor
            sumNumberOfCovers = cursor.getInt(cursor.getColumnIndex("sumNumberOfCovers"));
        }

        cursor.close();
        db.close();

        return sumNumberOfCovers;
    }

    public int getSumNumberOfCoversbyreporttype(String reportType) {
        int sumNumberOfCovers = 0;

        // Assuming TRANSACTION_DATE_CREATED is the column that represents the date of the transaction header
        String dateFilter = getDateFilterBasedOnReportTypeForTransactionHeader(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum NUMBER_OF_COVERS
        String query = "SELECT SUM(" + NUMBER_OF_COVERS + ") AS sumNumberOfCovers " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + dateFilter;

        // Execute the query
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // Retrieve the sum of NUMBER_OF_COVERS from the cursor
            sumNumberOfCovers = cursor.getInt(cursor.getColumnIndex("sumNumberOfCovers"));
        }

        cursor.close();
        db.close();

        return sumNumberOfCovers;
    }

    public int getSumNumberOfCovers(String reportType, int cashierId) {
        int sumNumberOfCovers = 0;

        // Assuming TRANSACTION_DATE_CREATED is the column that represents the date of the transaction header
        String dateFilter = getDateFilterBasedOnReportTypeForTransactionHeader(reportType);

        SQLiteDatabase db = this.getReadableDatabase();

        // Log the date filter for debugging
        Log.d("ReportPopupDialog", "Date Filter: " + dateFilter);

        // Construct the SQL query to sum NUMBER_OF_COVERS
        String query = "SELECT SUM(" + NUMBER_OF_COVERS + ") AS sumNumberOfCovers " +
                "FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + dateFilter + " AND " + TRANSACTION_CASHIER_CODE + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the sum of NUMBER_OF_COVERS from the cursor
            sumNumberOfCovers = cursor.getInt(cursor.getColumnIndex("sumNumberOfCovers"));
        }

        cursor.close();
        db.close();

        return sumNumberOfCovers;
    }


    public String getCashierNameById(int cashierId) {
        String cashierName = "";

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to fetch the cashier name based on the cashier ID
        String query = "SELECT " + COLUMN_CASHOR_NAME + " FROM " + TABLE_NAME_Users + " WHERE " + COLUMN_CASHOR_id + " = ?";

        // Execute the query with the provided cashier ID
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cashierId)});

        if (cursor.moveToFirst()) {
            // Retrieve the cashier name from the cursor
            cashierName = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_NAME));
        }

        cursor.close();
        db.close();

        return cashierName;
    }





    public boolean insertmramethod(String transactionId, String mramethod,String irn,String Qr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_MRA_Method, mramethod);
        values.put(TRANSACTION_MRA_IRN, irn);
        values.put(TRANSACTION_MRA_QR, Qr);

        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = {transactionId};

        long newRowId = db.update(TRANSACTION_HEADER_TABLE_NAME, values, selection, selectionArgs);

        return newRowId != -1;
    }

    public long insertStdAccessData(String compAd1, String compAd2, String compAd3, String compTel, String brnValue,
                                    String compFaxNo, String shopName, String shopNumber, String logo, String vatNo,
                                    String adr1, String adr2, String adr3, String telNo, String faxNo,
                                    String openingHours, String companyName, int cashorId, String lastModified,
                                    String dateCreated, String posNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_Comp_ADR_1, compAd1);
        values.put(COLUMN_Comp_ADR_2, compAd2);
        values.put(COLUMN_Comp_ADR_3, compAd3);
        values.put(COLUMN_Comp_TEL_NO, compTel);
        values.put(COLUMN_BRN_NO, brnValue);
        values.put(COLUMN_Comp_FAX_NO, compFaxNo);
        values.put(COLUMN_SHOPNAME, shopName);
        values.put(COLUMN_SHOPNUMBER, shopNumber);
        values.put(COLUMN_Logo, logo);
        values.put(COLUMN_VAT_NO, vatNo);
        values.put(COLUMN_ADR_1, adr1);
        values.put(COLUMN_ADR_2, adr2);
        values.put(COLUMN_ADR_3, adr3);
        values.put(COLUMN_TEL_NO, telNo);
        values.put(COLUMN_FAX_NO, faxNo);
        values.put(COLUMN_Opening_Hours, openingHours);
        values.put(COLUMN_COMPANY_NAME, companyName);
        values.put(COLUMN_CASHOR_id, cashorId);
        values.put(LastModified, lastModified);
        values.put(DateCreated, dateCreated);
        values.put(COLUMN_POS_Num, posNum);

        // Insert the data into the std_access table
        long newRowId = db.insert(TABLE_NAME_STD_ACCESS, null, values);

        // Close the database connection
        db.close();

        return newRowId;
    }
    public void inserttablesDatas(int tableId, int roomId, int TableNumber, int seatCount, String waiterName, String status, String merged, String mergeSetid, int roomnum, int TillNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Validate roomnum and TillNum
        if (isShopAndTillValid(roomnum, TillNum)) {
            ContentValues values = new ContentValues();
            values.put(TABLE_ID, tableId);
            values.put(ROOM_ID, roomId);
            values.put(TABLE_NUMBER, TableNumber);
            values.put(SEAT_COUNT, seatCount);
            values.put(WAITER_NAME, waiterName);
            values.put(STATUS, status);
            values.put(MERGED, merged);
            values.put(MERGED_SET_ID, mergeSetid);

            // Check if the table ID exists in the table
            if (!checkIDExistsForTable(String.valueOf(tableId))) {
                // Insert the data into the tables table
                long result = db.insert(TABLES, null, values);
                if (result == -1) {
                    // Insertion failed
                    Log.e("Insertion", "Failed to insert data into tables table");
                } else {
                    // Insertion successful
                    Log.d("Insertion", "Data inserted successfully into tables table");
                }
            } else {
                // Table ID exists, update the existing row
                int result = db.update(TABLES, values, TABLE_ID + "=?", new String[]{String.valueOf(tableId)});
                if (result == -1) {
                    // Update failed
                    Log.e("Update", "Failed to update data in tables table");
                } else {
                    // Update successful
                    Log.d("Update", "Data updated successfully in tables table");
                }
            }
        } else {
            Log.e("Validation", "Invalid room number or till number. Data not inserted.");
        }

        db.close();
    }

    // This method validates if the room number and till number exist in the std_access table
    public boolean isShopAndTillValid(int roomnum, int tillNum) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_STD_ACCESS +
                " WHERE " + COLUMN_SHOPNUMBER + " = ? AND " + COLUMN_POS_Num + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(roomnum), String.valueOf(tillNum)});

        boolean isValid = false;
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            isValid = (count > 0);
        }

        cursor.close();
        db.close();

        return isValid;
    }


    public void insertroomsDatas(int id, String roomname, String tablecount, int roomnum, int tillnum) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Validate roomnum and tillnum
        if (isShopAndTillValid(roomnum, tillnum)) {
            ContentValues values = new ContentValues();
            values.put(_ID, id);
            values.put(ROOM_NAME, roomname);
            values.put(TABLE_COUNT, tablecount);

            // Check if the room ID exists in the table
            if (!checkIDExistsForRooms(String.valueOf(id))) {
                // Insert the data into the rooms table
                long result = db.insert(ROOMS, null, values);
                if (result == -1) {
                    // Insertion failed
                    Log.e("Insertion", "Failed to insert data into rooms table");
                } else {
                    // Insertion successful
                    Log.d("Insertion", "Data inserted successfully into rooms table");
                }
            } else {
                // Room ID exists, update the existing row
                int result = db.update(ROOMS, values, _ID + "=?", new String[]{String.valueOf(id)});
                if (result == -1) {
                    // Update failed
                    Log.e("Update", "Failed to update data in rooms table");
                } else {
                    // Update successful
                    Log.d("Update", "Data updated successfully in rooms table");
                }
            }
        } else {
            Log.e("Validation", "Invalid room number or till number. Data not inserted.");
        }

        db.close();
    }
    public void insertUserDatas(int cashorId, String pin, int cashorLevel, String cashorName, String cashorShop, String cashorDepartment, String dateCreated, String lastModified) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CASHOR_id, cashorId);
        values.put(COLUMN_PIN, pin);
        values.put(COLUMN_CASHOR_LEVEL, cashorLevel);
        values.put(COLUMN_CASHOR_NAME, cashorName);
        values.put(COLUMN_CASHOR_Shop, cashorShop);
        values.put(COLUMN_CASHOR_DEPARTMENT, cashorDepartment);
        values.put(DateCreated, dateCreated);
        values.put(LastModified, lastModified);

        // Check if the user ID exists in the table
        if (!checkIDExistsForUser(String.valueOf(cashorId))) {
            // Insert the data into the users table
            long result = db.insert(TABLE_NAME_Users, null, values);
            db.close();
            if (result == -1) {
                // Insertion failed
                Log.e("Insertion", "Failed to insert data into room table");
            } else {
                // Insertion successful
                Log.d("Insertion", "Data inserted successfully into room table");
            }
        } else {
            // User ID exists, update existing row
            int result = db.update(TABLE_NAME_Users, values, COLUMN_CASHOR_id + "=?", new String[]{String.valueOf(cashorId)});
            db.close();
            if (result == -1) {
                // Update failed
                Log.e("Update", "Failed to update data in room table");
            } else {
                // Update successful
                Log.d("Update", "Data updated successfully in room table");
            }
        }
    }



    public void insertItemsDatas(String id, int shopNumber, int tillNumber, String itemname, String RelatedSupplements, String Comment,
                                 String desc, String price, String price2, String price3, String ratediscount, String amountdiscount,
                                 String category, String barcode, float weight, String department, String subDepartment,
                                 String longDescription, String quantity, String expiryDate, String vAT, String availableForSale,
                                 String soldBy, String image, String variant, String sku, String cost, String userId,
                                 String dateCreated, String lastModified, String Hasoptions, String nature, String currency,
                                 String itemCode, String taxCode, String totalDiscount, String totalDiscount2,
                                 String totalDiscount3, double priceAfterDiscount, double priceAfterDiscount2,
                                 double priceAfterDiscount3, String Related_item, String Related_item2, String Related_item3,
                                 String Related_item4, String Related_item5, String HasSupplements, String syncStatus) {

        if (isShopAndTillValid(shopNumber, tillNumber)) {
            ContentValues contentValue = new ContentValues();
            contentValue.put(DatabaseHelper._ID, id);
            contentValue.put(DatabaseHelper.Name, itemname);
            contentValue.put(DatabaseHelper.comment, Comment);
            contentValue.put(DatabaseHelper.relatedSupplements, RelatedSupplements);
            contentValue.put(DatabaseHelper.DESC, desc);
            contentValue.put(DatabaseHelper.Price, price);
            contentValue.put(DatabaseHelper.Price2, price2);
            contentValue.put(DatabaseHelper.Price3, price3);
            contentValue.put(DatabaseHelper.RateDiscount, ratediscount);
            contentValue.put(DatabaseHelper.AmountDiscount, amountdiscount);
            contentValue.put(DatabaseHelper.hasoptions, Hasoptions);
            contentValue.put(DatabaseHelper.Category, category);
            contentValue.put(DatabaseHelper.Barcode, barcode);
            contentValue.put(DatabaseHelper.Department, department);
            contentValue.put(DatabaseHelper.SubDepartment, subDepartment);
            contentValue.put(DatabaseHelper.LongDescription, longDescription);
            contentValue.put(DatabaseHelper.Quantity, quantity);
            contentValue.put(DatabaseHelper.ExpiryDate, expiryDate);
            contentValue.put(DatabaseHelper.VAT, vAT);
            contentValue.put(DatabaseHelper.Weight, weight);
            contentValue.put(DatabaseHelper.AvailableForSale, availableForSale);
            contentValue.put(DatabaseHelper.SoldBy, soldBy);
            contentValue.put(DatabaseHelper.Image, image);
            contentValue.put(DatabaseHelper.Variant, variant);
            contentValue.put(DatabaseHelper.SKU, sku);
            contentValue.put(DatabaseHelper.Cost, cost);
            contentValue.put(DatabaseHelper.DateCreated, dateCreated);
            contentValue.put(DatabaseHelper.LastModified, lastModified);
            contentValue.put(DatabaseHelper.UserId, userId);
            contentValue.put(DatabaseHelper.Nature, nature);
            contentValue.put(DatabaseHelper.Currency, currency);
            contentValue.put(DatabaseHelper.ItemCode, itemCode);
            contentValue.put(DatabaseHelper.TaxCode, taxCode);
            contentValue.put(DatabaseHelper.TotalDiscount, totalDiscount);
            contentValue.put(DatabaseHelper.TotalDiscount2, totalDiscount2);
            contentValue.put(DatabaseHelper.TotalDiscount3, totalDiscount3);
            contentValue.put(DatabaseHelper.PriceAfterDiscount, priceAfterDiscount);
            contentValue.put(DatabaseHelper.Price2AfterDiscount, priceAfterDiscount2);
            contentValue.put(DatabaseHelper.Price3AfterDiscount, priceAfterDiscount3);
            contentValue.put(DatabaseHelper.related_item, Related_item);
            contentValue.put(DatabaseHelper.related_item2, Related_item2);
            contentValue.put(DatabaseHelper.related_item3, Related_item3);
            contentValue.put(DatabaseHelper.related_item4, Related_item4);
            contentValue.put(DatabaseHelper.related_item5, Related_item5);
            contentValue.put(DatabaseHelper.hasSupplements, HasSupplements);
            contentValue.put(DatabaseHelper.SyncStatus, syncStatus);

            try (SQLiteDatabase db = this.getWritableDatabase()) {
                // Check if the barcode exists in the table
                if (!checkBarcodeExistsForItems(barcode)) {
                    // Insert the data into the items table
                    long result = db.insert(TABLE_NAME, null, contentValue);
                    if (result == -1) {
                        Log.e("Insertion", "Failed to insert data into item table");
                    } else {
                        Log.d("Insertion", "Data inserted successfully into item table");
                    }
                } else {
                    // Barcode exists, update the existing row
                    int result = db.update(TABLE_NAME, contentValue, DatabaseHelper.Barcode + "=?", new String[]{barcode});
                    if (result == -1) {
                        Log.e("Update", "Failed to update data in item table");
                    } else {
                        Log.d("Update", "Data updated successfully in item table");
                    }
                }
            }
        } else {
            Log.e("Validation", "Invalid shop number or till number. Data not inserted.");
        }
    }


}

