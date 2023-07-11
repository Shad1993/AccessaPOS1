package com.accessa.ibora.product.items;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.accessa.ibora.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Names
    public static final String TABLE_NAME = "Items";
    public static final String TAX_TABLE_NAME = "Tax";
    public static final String VENDOR_TABLE_NAME = "Vendor";
    public static final String COST_TABLE_NAME = "Cost";
    public static final String TRANSACTION_TABLE_NAME = "Transactions";
    public static final String TRANSACTION_HEADER_TABLE_NAME = "TransactionHeader";
    public static final String TRANSACTION_STATUS_IN_PROGRESS = "InProgress";
    public static final String TRANSACTION_UNIT_PRICE = "UnitPrice";


    private static final String INVOICE_SETTLEMENT_TABLE_NAME = "InvoiceSettlement";

    // Common column names
    public static final String _ID = "_id";

    // Items table columns
    public static final String Name = "name";
    public static final String Category = "category";
    public static final String DESC = "description";
    public static final String Price = "price";
    public static final String Department = "Department";
    public static final String SubDepartment = "SubDepartment";
    public static final String Barcode = "Barcode";
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
    public static String DateCreated ="DateCreated";
    public static final String LastModified = "LastModified";


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
    public static final String TRANSACTION_STATUS_INPROGRESS = "InProgress";
    public static final String COLUMN_PIN = "pin";
    public static final String COLUMN_CASHOR_LEVEL = "cashorlevel";
     public static final String COLUMN_CASHOR_NAME = "cashorname";
    public static final String COLUMN_CASHOR_DEPARTMENT = "cashorDepartment";
    // Database Information
    private static final String DB_NAME = Constants.DB_NAME;
    private static final int DB_VERSION = 1;

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
    public static String TRANSACTION_STATUS_Saved = "Saved";
    public static final String TRANSACTION_ID = "TranscationId";
    public static final String ITEM_ID = "ItemId";
    public static final String TRANSACTION_DATE = "TransactionDate";

    public static final String TRANSACTION_STATUS = "TransactionStatus";

    public static final String QUANTITY = "Quantity";
    public static final String TOTAL_PRICE = "TotalPrice";

    private static final String TRANSACTION_SHOP_NO = "ShopNo";
    public static final String TRANSACTION_TERMINAL_NO = "TerminalNo";
    public static final String TRANSACTION_DATE_CREATED = "DateCreated";
    private static final String TRANSACTION_DATE_MODIFIED = "DateModified";
    public static final String TRANSACTION_TIME_CREATED = "TimeCreated";
    private static final String TRANSACTION_TIME_MODIFIED = "TimeModified";
    private static final String TRANSACTION_CODE = "Code";
    private static final String TRANSACTION_DESCRIPTION = "Description";

    private static final String TRANSACTION_QUANTITY = "Qte";
    private static final String TRANSACTION_DISCOUNT = "Discount";
    private static final String TRANSACTION_VAT_BEFORE_DISC = "VAT_Before_Disc";
    private static final String TRANSACTION_VAT_AFTER_DISC = "VAT_After_Disc";
    private static final String TRANSACTION_TOTAL_HT_A ="TOTALHT_A" ;
    public static final String TRANSACTION_TOTAL_TTC =  "TotalTTC";

    private static final String TRANSACTION_IS_TAXABLE = "IsTaxable";
    private static final String TRANSACTION_DATE_TRANSACTION = "DateTransaction";
    private static final String TRANSACTION_TIME_TRANSACTION = "TimeTransaction";
    private static final String TRANSACTION_BARCODE = "Barcode";
    private static final String TRANSACTION_WEIGHTS = "Weights";
    private static final String TRANSACTION_TOTAL_HT_B = "TotalHT_B";
    private static final String TRANSACTION_TYPE_TAX = "TYPETAX";
    private static final String TRANSACTION_RAYON = "Rayon";
    private static final String TRANSACTION_FAMILLE = "Famille";
    private static final String TRANSACTION_ID_SALES_D = "IDSalesD";
    private static final String TRANSACTION_TOTALIZER = "Totalizer";

    // Header fields

    private static final String TRANSACTION_MEMBER_CARD = "MemberCard";
    private static final String TRANSACTION_SUB_TOTAL = "SubTotal";
    public static final String TRANSACTION_CASHIER_CODE = "CashierCode";

   public static final String TRANSACTION_TOTAL_PAID="TenderAmount";

    public static final String TRANSACTION_CASH_RETURN ="CashReturn";

    public static final String TRANSACTION_TOTAL_TX_1 = "Total_Tx_1";
    private static final String TRANSACTION_TOTAL_TX_2 = "Total_Tx_2";
    private static final String TRANSACTION_TOTAL_TX_3 = "Total_Tx_3";
    private static final String TRANSACTION_TOTAL_DISCOUNT = "TotalDisc";
    private static final String TRANSACTION_ITEM_QUANTITY = "QtyItem";

    private static final String TRANSACTION_CLIENT_NAME = "ClientName";
    private static final String TRANSACTION_CLIENT_OTHER_NAME = "ClientOtherName";
    private static final String TRANSACTION_CLIENT_ADR1 = "ClientAdr1";
    private static final String TRANSACTION_CLIENT_ADR2 = "ClientAdr2";
    private static final String TRANSACTION_CLIENT_VAT_REG_NO = "ClientVATRegNo";
    private static final String TRANSACTION_CLIENT_BRN = "ClientBRN";
    private static final String TRANSACTION_CLIENT_TEL = "ClientTel";
    private static final String TRANSACTION_INVOICE_REF = "InvoiceRef";
    private static final String TRANSACTION_IS_CASH_CREDIT = "IsCash_Credit";
    private static final String TRANSACTION_ID_SALESH = "IDSalesH";
    private static final String TRANSACTION_CLIENT_CODE = "ClientCode";
    private static final String TRANSACTION_LOYALTY = "Loyalty";
    public static final String TRANSACTION_TICKET_NO = "TranscationId";


    // Invoice Settlement table columns
    public static final String SETTLEMENT_ID = "SettlementId";
    public static final String SETTLEMENT_SHOP_NO = "ShopNo";
    public static final String SETTLEMENT_TERMINAL_NO = "TerminalNo";
    public static final String SETTLEMENT_DATE_TRANSACTION = "DateTransaction";
    public static final String SETTLEMENT_PAYMENT_CODE = "CodeModeDePaiement";
    public static final String SETTLEMENT_AMOUNT = "Amount";
    public static final String SETTLEMENT_TOTAL_AMOUNT = "TotalAmount";
    public static final String SETTLEMENT_GIFT_VOUCHER_NO = "GiftVoucherNo";
    public static final String SETTLEMENT_INVOICE_ID = "IDInvoiceSettlement";
    public static final String SETTLEMENT_REMARK = "Remark";
    public static final String SETTLEMENT_DATE_CREATED = "DateCreated";
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
    private static String COLUMN_Comp_ADR_1= "ComPanyAdress1";
    private static String COLUMN_Comp_ADR_2 ="ComPanyAdress2";
    private static String COLUMN_Comp_ADR_3= "ComPanyAdress3";
    private static String COLUMN_Comp_TEL_NO= "ComPanyphoneNumber";
    private static String COLUMN_Comp_FAX_NO= "ComPanyFaxNumber";
    public static String VAT_Type= "VatType";
    public static String COLUMN_TerminalNo="TerminalNumber";
    public static final String COLUMN_Logo = "Logo";
    public static final String COLUMN_COMPANY_NAME = "company_name";

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

// POS Table

    public static final String TABLE_NAME_POS_ACCESS = "POSTable";
    private static final String COLUMN_POS_ID = "id";


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
            + Barcode + " TEXT(20) UNIQUE NOT NULL, "
            + Name + " TEXT NOT NULL, "
            + DESC + " TEXT NOT NULL, "
            + Category + " TEXT NOT NULL, "
            + Quantity + " INTEGER, " // Allow NULL values for Quantity
            + Department + " TEXT NOT NULL, "
            + LongDescription + " TEXT NOT NULL, "
            + SubDepartment + " TEXT NOT NULL, "
            + Price + " DECIMAL(10, 2) NOT NULL, "
            + VAT + " TEXT NOT NULL CHECK(VAT IN ('VAT 0%', 'VAT Exempted', 'VAT 15%')), "
            + ExpiryDate + " DATE, " // Allow NULL values for ExpiryDate
            + AvailableForSale + " BOOLEAN NOT NULL DEFAULT 1, "
            + SoldBy + " TEXT NOT NULL CHECK(SoldBy IN ('Each', 'Volume')), "
            + Image + " TEXT, "
            + SKU + " TEXT NOT NULL, "
            + Variant + " TEXT NOT NULL, "
            + Cost + " DECIMAL(10, 2) NOT NULL, "
            + Weight + " DECIMAL(10, 2), " // Allow NULL values for Weight
            + UserId + " INTEGER NOT NULL, "
            + DateCreated + " DATETIME NOT NULL, "
            + LastModified + " DATETIME NOT NULL, "
            + "FOREIGN KEY (" + SKU + ", " + Cost + ") REFERENCES "
            + COST_TABLE_NAME + "(" + SKUCost + ", " + Cost + "), "
            + "FOREIGN KEY (" + UserId + ") REFERENCES "
            + TABLE_NAME_Users + "(" + COLUMN_CASHOR_id + "), "
            + "FOREIGN KEY (" + Barcode + ") REFERENCES "
            + COST_TABLE_NAME + "(" + Barcode + "));";

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
            TRANSACTION_ID + " INTEGER NOT NULL, " +
            ITEM_ID + " INTEGER NOT NULL, " +
            TRANSACTION_DATE + " DATETIME NOT NULL, " +
            QUANTITY + " INTEGER NOT NULL, " +
            TOTAL_PRICE + " DECIMAL(10, 2) NOT NULL, " +
            VAT + " DECIMAL(10, 2) NOT NULL, " +
            VAT_Type + " TEXT NOT NULL CHECK(VatType IN ('VAT 0%', 'VAT Exempted', 'VAT 15%')), " +
            LongDescription + " TEXT NOT NULL, " +
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
            TRANSACTION_FAMILLE + " TEXT, " +
            TRANSACTION_ID_SALES_D + " TEXT, " +
            TRANSACTION_TOTALIZER + " TEXT, " +
            "FOREIGN KEY (" + ITEM_ID + ") REFERENCES " +
            TABLE_NAME + "(" + _ID + "));";


    private static final String CREATE_TRANSACTION_HEADER = "CREATE TABLE " + TRANSACTION_HEADER_TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRANSACTION_TICKET_NO + " INTEGER NOT NULL, " +
            TRANSACTION_SHOP_NO + " TEXT, " +
            TRANSACTION_TERMINAL_NO + " TEXT, " +
            TRANSACTION_DATE_CREATED + " TEXT, " +
            TRANSACTION_DATE_MODIFIED + " TEXT, " +
            TRANSACTION_TIME_CREATED + " TEXT, " +
            TRANSACTION_TIME_MODIFIED + " TEXT, " +
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
            TRANSACTION_CLIENT_ADR1 + " TEXT, " +
            TRANSACTION_CLIENT_ADR2 + " TEXT, " +
            TRANSACTION_STATUS + " TEXT NOT NULL CHECK(TransactionStatus IN ('Saved', 'InProgress', 'Completed')), " +
            TRANSACTION_CLIENT_VAT_REG_NO + " TEXT, " +
            TRANSACTION_CLIENT_BRN + " TEXT, " +
            TRANSACTION_CLIENT_TEL + " TEXT, " +
            TRANSACTION_INVOICE_REF + " TEXT, " +
            TRANSACTION_IS_CASH_CREDIT + " TEXT, " +
            TRANSACTION_ID_SALESH + " TEXT, " +
            TRANSACTION_CLIENT_CODE + " TEXT, " +
            TRANSACTION_LOYALTY + " TEXT, " +
            "FOREIGN KEY (" + TRANSACTION_TICKET_NO + ") REFERENCES " +
            TRANSACTION_TABLE_NAME + "(" + TRANSACTION_ID + "));";


    // Create Invoice Settlement table query
    private static final String CREATE_INVOICE_SETTLEMENT_TABLE = "CREATE TABLE " + INVOICE_SETTLEMENT_TABLE_NAME + "(" +
            SETTLEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SETTLEMENT_SHOP_NO + " TEXT, " +
            SETTLEMENT_TERMINAL_NO + " TEXT, " +
            SETTLEMENT_DATE_TRANSACTION + " TEXT, " +
            SETTLEMENT_PAYMENT_CODE + " TEXT, " +
            SETTLEMENT_AMOUNT + " DECIMAL(10, 2), " +
            SETTLEMENT_TOTAL_AMOUNT + " DECIMAL(10, 2), " +
            SETTLEMENT_GIFT_VOUCHER_NO + " TEXT, " +
            SETTLEMENT_INVOICE_ID + " INTEGER, " +
            SETTLEMENT_REMARK + " TEXT, " +
            SETTLEMENT_DATE_CREATED + " TEXT, " +
            SETTLEMENT_PAYMENT_NAME + " TEXT, " +
            SETTLEMENT_CHEQUE_NO + " TEXT, " +
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
            + COLUMN_Logo + " TEXT, "
            + COLUMN_VAT_NO + " TEXT, "
            + COLUMN_BRN_NO + " TEXT, "
            + COLUMN_ADR_1 + " TEXT, "
            + COLUMN_ADR_2 + " TEXT, "
            + COLUMN_ADR_3 + " TEXT, "
            + COLUMN_TEL_NO + " TEXT, "
            + COLUMN_FAX_NO + " TEXT, "
            + COLUMN_COMPANY_NAME + " TEXT, "
            + COLUMN_CASHOR_id + " INTEGER, "
            + LastModified + " TEXT, "
            + DateCreated + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_COMPANY_NAME + ") REFERENCES " + TABLE_NAME_Users + "(" + COLUMN_CASHOR_Shop + ")"
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


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAX_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VENDOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DEPARTMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SUBDEPARTMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Users);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DISCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_HEADER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INVOICE_SETTLEMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STD_ACCESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PAYMENTBYQY);
        db.execSQL("DROP TABLE IF EXISTS " + PAYMENT_METHOD_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POS_ACCESS);

        onCreate(db);
    }


    public long insertTransaction(int itemId, String transactionId, String transactionDate, int quantity,
                                  double totalPrice, double vat, String longDescription, double unitPrice, double priceWithoutVat,
                                  String vatType, String posNum) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_ID, itemId);
        values.put(TRANSACTION_ID, transactionId);
        values.put(TRANSACTION_DATE, transactionDate);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, totalPrice);
        values.put(VAT, vat);
        values.put(LongDescription, longDescription);
        values.put(TRANSACTION_UNIT_PRICE, unitPrice);
        values.put(TRANSACTION_TOTAL_HT_A, priceWithoutVat);
        values.put(TRANSACTION_TOTAL_TTC, totalPrice);
        values.put(VAT_Type, vatType);
        values.put(TRANSACTION_TERMINAL_NO, posNum);
        return db.insert(TRANSACTION_TABLE_NAME, null, values);
    }
    public boolean saveTransactionHeader(String transactionId, double totalAmount, String currentDate,
                                         String currentTime, double totalHT_A, double totalTTC, double taxAmount, int quantityItem, String cashierId, String transactionStatus, String posNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the transaction ID already exists
        String query = "SELECT COUNT(*) FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_TICKET_NO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{transactionId});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            // Transaction ID already exists, do not insert
            return false;
        }

        ContentValues values = new ContentValues();
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
        values.put(TRANSACTION_TERMINAL_NO, posNum);


        long result = db.insert(TRANSACTION_HEADER_TABLE_NAME, null, values);
        return result != -1;
    }

    public void updateTransaction(int itemId, int newQuantity, double newTotalPrice, double newVat, String vatType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUANTITY, newQuantity);
        values.put(TOTAL_PRICE, newTotalPrice);
        values.put(VAT, newVat);
        values.put(VAT_Type, vatType);

        String selection = ITEM_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};

        db.update(TRANSACTION_TABLE_NAME, values, selection, selectionArgs);
    }
    public void updateAllTransactionsHeaderStatus(String transactionStatusCompleted) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, transactionStatusCompleted);

        // Update the status of all in-progress transactions to the new status
        String whereClause = TRANSACTION_STATUS + " = ?";
        String[] whereArgs = {TRANSACTION_STATUS_IN_PROGRESS};
        db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);

    }
    public void updateAllTransactionsStatus(String newStatus) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, newStatus);

        // Update the status of all in-progress transactions to the new status
        String whereClause = TRANSACTION_STATUS + " = ?";
        String[] whereArgs = {TRANSACTION_STATUS_IN_PROGRESS};
        db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);
    }
    public boolean updateTransactionHeader(String transactionIdInProgress, double totalAmount, String currentDate, String currentTime, double totalHT_a, double totalTTC, int quantityItem, double totaltax, String cashierId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_TOTAL_TTC, totalAmount);
        values.put(TRANSACTION_DATE_MODIFIED, currentDate);
        values.put(TRANSACTION_DATE_TRANSACTION, currentDate);
        values.put(TRANSACTION_TIME_MODIFIED, currentTime);
        values.put(TRANSACTION_TIME_TRANSACTION, currentTime);
        values.put(TRANSACTION_TOTAL_HT_A, totalHT_a);
        values.put(TRANSACTION_TOTAL_TTC, totalTTC);
        values.put(TRANSACTION_ITEM_QUANTITY, quantityItem);
        values.put(TRANSACTION_TOTAL_TX_1, totaltax);
        values.put(TRANSACTION_CASHIER_CODE, cashierId);


        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = {transactionIdInProgress};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, selection, selectionArgs);
        return rowsAffected > 0;
    }
    public Cursor getAllInProgressTransactions() {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + "=? " +
                "ORDER BY t." + TRANSACTION_DATE + " ASC";

        String[] selectionArgs = {"InProgress"};

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




        public Cursor getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
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

    public Cursor getAllDepartment() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DEPARTMENT_TABLE_NAME, null, null, null, null, null, null);
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
        String[] projection = {_ID, Name, LongDescription,AvailableForSale, Category, Price};
        String selection = LongDescription + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        String sortOrder = LongDescription + " ASC";
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
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


    public Cursor getTransactionByItemId(int itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = TRANSACTION_TABLE_NAME + "." + ITEM_ID + " = ?" +
                " AND " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(itemId), "InProgress"};

        String joinQuery = "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                " JOIN " + TRANSACTION_HEADER_TABLE_NAME +
                " ON " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_ID + " = " + TRANSACTION_HEADER_TABLE_NAME + "." + TRANSACTION_TICKET_NO +
                " WHERE " + selection;

        return db.rawQuery(joinQuery, selectionArgs);
    }



    public void updateTransactionIds(String newTransactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, newTransactionId);

        String joinQuery = "UPDATE " + TRANSACTION_TABLE_NAME +
                " SET " + TRANSACTION_ID + " = ?" +
                " WHERE " + TRANSACTION_ID + " IN (" +
                " SELECT " + TRANSACTION_TICKET_NO +
                " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = ?" +
                ")";

        String[] selectionArgs = {newTransactionId, "InProgress"};

        db.execSQL(joinQuery, selectionArgs);
    }






    public Cursor getTransactionsByStatusAndId(String status, String transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " AS t " +
                "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " AS th ON t." + TRANSACTION_ID + "=th." + TRANSACTION_TICKET_NO +
                " WHERE th." + TRANSACTION_STATUS + " = ?" +
                " AND t." + TRANSACTION_ID + " = ?";

        String[] selectionArgs = {status, transactionId};

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
    public Cursor getTransactionHeader(String transactionId) {
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

    public double getItemPrice(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        double price = 0;

        String[] projection = {Price};
        String selection = _ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndex(Price));
        }

        cursor.close();
        db.close();

        return price;
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



    public Cursor getDistinctVATTypes(String transactionIdInProgress) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.VAT_Type};
        String selection = DatabaseHelper.TRANSACTION_ID + " = ?";
        String[] selectionArgs = {transactionIdInProgress};
        String orderBy = DatabaseHelper.VAT_Type + " ASC";

        return db.query(true, DatabaseHelper.TRANSACTION_TABLE_NAME, columns, selection, selectionArgs, DatabaseHelper.VAT_Type, null, orderBy, null);
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
    public boolean insertSettlementAmount(String paymentName, double settlementAmount, String SettlementId,String PosNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SETTLEMENT_PAYMENT_NAME, paymentName);
        values.put(SETTLEMENT_AMOUNT, settlementAmount);
        values.put(SETTLEMENT_INVOICE_ID  , SettlementId);
        values.put(SETTLEMENT_TERMINAL_NO,PosNum);

        // Insert the values into the table
        long newRowId = db.insert(INVOICE_SETTLEMENT_TABLE_NAME, null, values);

        // Close the database connection
        db.close();

        // Return true if the row was inserted successfully, false otherwise
        return newRowId != -1;
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

    public Cursor getPaymentmethodamounts(String transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.SETTLEMENT_AMOUNT,SETTLEMENT_PAYMENT_NAME};
        String selection = DatabaseHelper.SETTLEMENT_INVOICE_ID + " = ?";
        String[] selectionArgs = {transactionId};
        String orderBy = DatabaseHelper.SETTLEMENT_PAYMENT_NAME + " ASC";

        return db.query(true, DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME, columns, selection, selectionArgs, DatabaseHelper.SETTLEMENT_PAYMENT_NAME, null, orderBy, null);
    }


    public boolean insertcashReturn(double cashReturn,double tenderamount, String transaction_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_CASH_RETURN, cashReturn);
        values.put(TRANSACTION_TOTAL_PAID, tenderamount);

        String selection = TRANSACTION_TICKET_NO + " = ?";
        String[] selectionArgs = {transaction_id};

        int rowsAffected = db.update(TRANSACTION_HEADER_TABLE_NAME, values, selection, selectionArgs);
        return rowsAffected > 0;
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


}


