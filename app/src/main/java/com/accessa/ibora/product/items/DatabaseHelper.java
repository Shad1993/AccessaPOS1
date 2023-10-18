package com.accessa.ibora.product.items;




import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.accessa.ibora.Buyer.Buyer;
import com.accessa.ibora.Constants;
import com.accessa.ibora.Report.PaymentItem;
import com.accessa.ibora.product.couponcode.Coupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


    public static final String INVOICE_SETTLEMENT_TABLE_NAME = "InvoiceSettlement";

    // Common column names
    public static final String _ID = "_id";

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
    public static final String COLUMN_PIN = "pin";
    public static final String COLUMN_CASHOR_LEVEL = "cashorlevel";
     public static final String COLUMN_CASHOR_NAME = "cashorname";
    public static String COLUMN_CASHOR_ShopNum="shopnum";
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
    private static final String TRANSACTION_DATE_MODIFIED = "DateModified";
    public static final String TRANSACTION_TIME_CREATED = "TimeCreated";
    private static final String TRANSACTION_TIME_MODIFIED = "TimeModified";
    private static final String TRANSACTION_CODE = "Code";
    private static final String TRANSACTION_DESCRIPTION = "Description";
    public static final String TRANSACTION_MRA_Invoice_Counter="Invoice_counter";
    private static final String TRANSACTION_QUANTITY = "Qte";
    public static final String TRANSACTION_DISCOUNT = "Discount";
    private static final String TRANSACTION_VAT_BEFORE_DISC = "VAT_Before_Disc";
    private static final String TRANSACTION_VAT_AFTER_DISC = "VAT_After_Disc";
    public static final String TRANSACTION_TOTAL_HT_A ="TOTALHT_A" ;
    public static final String TRANSACTION_TOTAL_TTC =  "TotalTTC";

    private static final String TRANSACTION_IS_TAXABLE = "IsTaxable";
    private static final String TRANSACTION_DATE_TRANSACTION = "DateTransaction";
    private static final String TRANSACTION_TIME_TRANSACTION = "TimeTransaction";
    private static final String TRANSACTION_BARCODE = "Barcode";
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


        //financial report table
    public static final String FINANCIAL_COLUMN_ID = "id";
    public static final String FINANCIAL_COLUMN_DATETIME = "date";
    public static final String FINANCIAL_COLUMN_CASHOR_ID = "cashiorid";
    public static final String FINANCIAL_COLUMN_TRANSACTION_CODE = "TransactionType";
    public static final String FINANCIAL_COLUMN_QUANTITY = "quantity";
    public static final String FINANCIAL_COLUMN_TOTAL ="total" ;
    public static final String FINANCIAL_COLUMN_TOTALIZER = "Totalizer";
    public static final String FINANCIAL_COLUMN_POSNUM = "TillNum";



    //default qr

    private static final String DEFAULT_PAYMENT_METHOD = "POP";
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


    public static final String COUNTING_REPORT_TABLE_NAME = "CountReport";
    public static final String COUNTING_REPORT_ID = "id";
    public static final String COUNTING_REPORT_TOTALIZER_TOTAL = "totalizer_total";
    public static final String COUNTING_REPORT_TOTAL_VALUE = "total_value";
    public static final String COUNTING_REPORT_DIFFERENCE = "difference";
    public static final String COUNTING_REPORT_CASHIER_ID = "cashier_id";
    public static final String COUNTING_REPORT_DATETIME = "datetime";

    public static final String CASH_REPORT_TABLE_NAME = "CashReports";
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
            + Price2 + " DECIMAL(10, 2), " // New field: Price2
            + Price3 + " DECIMAL(10, 2), " // New field: Price3
            + PriceAfterDiscount + " DECIMAL(10, 2) NOT NULL, "
            + Price2AfterDiscount + " DECIMAL(10, 2) NOT NULL, "
            + Price3AfterDiscount + " DECIMAL(10, 2) NOT NULL, "
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
            + Nature + " TEXT, "           // New field: Nature
            + TaxCode + " TEXT, "          // New field: TaxCode
            + Currency + " TEXT, "         // New field: Currency
            + ItemCode + " TEXT, "         // New field: ItemCode
            + SyncStatus +  " TEXT NOT NULL CHECK(SyncStatus IN ('Offline', 'Online')), "
            + RateDiscount + " TEXT, "
            + AmountDiscount + " TEXT, "
            + TotalDiscount + " DECIMAL(10, 2), " // New field: TotalDiscount
            + TotalDiscount2 + " DECIMAL(10, 2), " // New field: TotalDiscount
            + TotalDiscount3 + " DECIMAL(10, 2), " // New field: TotalDiscount
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
            TRANSACTION_STATUS + " TEXT NOT NULL CHECK(TransactionStatus IN ('DRN','CRN','PRF', 'InProgress', 'Completed','TRN')), " +
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
            BUYER_DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            BUYER_LAST_MODIFIED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

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
            + FINANCIAL_COLUMN_DATETIME + " DATE DEFAULT (date('now')), " // Set it to the current date
            + FINANCIAL_COLUMN_CASHOR_ID + " INTEGER NOT NULL, "
            + FINANCIAL_COLUMN_TRANSACTION_CODE + " TEXT NOT NULL, "
            + FINANCIAL_COLUMN_QUANTITY + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_TOTAL + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_TOTALIZER + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_POSNUM + " INTEGER NOT NULL" // Add the POSNUM field
            + ");";

    private static final String CREATE_COUNTING_REPORT_TABLE = "CREATE TABLE " + COUNTING_REPORT_TABLE_NAME + "(" +
            COUNTING_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
            + FINANCIAL_COLUMN_DATETIME + " DATE DEFAULT (date('now')), " // Set it to the current date
            + FINANCIAL_COLUMN_CASHOR_ID + " INTEGER NOT NULL, "
            + FINANCIAL_COLUMN_QUANTITY + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_TOTAL + " REAL NOT NULL, "
            + FINANCIAL_COLUMN_POSNUM + " INTEGER NOT NULL" // Add the POSNUM field
            + ");";





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
        db.execSQL(CREATE_BUYER_TABLE);
        db.execSQL(CREATE_COUPON_TABLE);
        db.execSQL(CREATE_FINANCIAL_TABLE);
        db.execSQL(CREATE_COUNTING_REPORT_TABLE);
        db.execSQL(CREATE_CASH_REPORT_TABLE);
        addDefaultItem(db);
        // Insert cheque details
        addDefaultPaymentMethod(db, "POP", "1");

        // Insert cheque details
        addDefaultPaymentMethod(db, "Cheque", "1");

        // Insert cash details
        addDefaultPaymentMethod(db, "Cash", "1");

        // Insert credit card details
        addDefaultPaymentMethod(db, "Credit Card", "1");

        // Insert debit card details
        addDefaultPaymentMethod(db, "Debit Card", "1");
        // Insert debit card details
        addDefaultPaymentMethod(db, "Coupon Code", "1");

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
        db.execSQL("DROP TABLE IF EXISTS " + BUYER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COUPON_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FINANCIAL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COUNTING_REPORT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CASH_REPORT_TABLE_NAME);

        onCreate(db);
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


    public long insertTransaction(int itemId, String transactionId, String transactionDate, int quantity,
                                  double totalPrice, double vat, String longDescription, double unitPrice, double priceWithoutVat,
                                  String vatType, String posNum, String Nature, String ItemCode, String Currency, String taxCode, double priceAfterDiscount, double totalDiscount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_ID, itemId);
        values.put(TRANSACTION_ID, transactionId);
        values.put(TRANSACTION_DATE, transactionDate);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, totalPrice);
        values.put(VAT, vat);
        values.put(LongDescription, longDescription);
        double roundedUnitPrice = Math.round(unitPrice * 100.0) / 100.0;
        values.put(TRANSACTION_UNIT_PRICE, roundedUnitPrice);
        values.put(TRANSACTION_TOTAL_HT_A, priceWithoutVat);
        values.put(TRANSACTION_TOTAL_TTC, totalPrice);
        values.put(VAT_Type, vatType);
        values.put(TRANSACTION_TERMINAL_NO, posNum);
        values.put(TRANSACTION_NATURE, Nature);
        values.put(TRANSACTION_ITEM_CODE, ItemCode);
        values.put(TRANSACTION_CURRENCY, Currency);
        values.put(TRANSACTION_TAX_CODE, taxCode);
        values.put(PriceAfterDiscount, priceAfterDiscount);
        // Round the totalDiscount to two decimal places
        double roundedTotalDiscount = Math.round(totalDiscount * 100.0) / 100.0;
        values.put(TRANSACTION_TOTAL_DISCOUNT, roundedTotalDiscount);
        return db.insert(TRANSACTION_TABLE_NAME, null, values);
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

    public boolean saveTransactionHeader(String Shopnumber, String transactionId, double totalAmount, String currentDate,
                                         String currentTime, double totalHT_A, double totalTTC, double taxAmount, int quantityItem, String cashierId, String transactionStatus, String posNum, String MRAMETHOD) {
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
        values.put(TRANSACTION_TERMINAL_NO, posNum);
        values.put(TRANSACTION_MRA_Method, MRAMETHOD);


        long result = db.insert(TRANSACTION_HEADER_TABLE_NAME, null, values);
        return result != -1;
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
    public void updateAllTransactionsStatus(String Type,String MRAMETHOD,String InvoiceRefIden) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_STATUS, Type);
        values.put(TRANSACTION_MRA_Method, MRAMETHOD);
        values.put(TRANSACTION_INVOICE_REF, InvoiceRefIden);


        // Update the status of all in-progress transactions to the new status
        String whereClause = TRANSACTION_STATUS + " = ?";
        String[] whereArgs = {TRANSACTION_STATUS_IN_PROGRESS};
        db.update(TRANSACTION_HEADER_TABLE_NAME, values, whereClause, whereArgs);
    }
    public boolean updateTransactionHeader( double totalAmount, String currentDate, String currentTime, double totalHT_a, double totalTTC, int quantityItem, double totaltax, String cashierId) {
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


        String selection = TRANSACTION_STATUS  + " = ?";
        String[] selectionArgs = {"InProgress"};

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




    public Cursor getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    public Cursor getAllBuyer() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(BUYER_TABLE_NAME, null, null, null, null, null, null);
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
    public Cursor getTransactionHeader() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = 'InProgress'";

        return db.rawQuery(query, null);
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
    public boolean insertSettlementAmount(String paymentName, double settlementAmount, String SettlementId, String PosNum, String transactionDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SETTLEMENT_PAYMENT_NAME, paymentName);
        values.put(SETTLEMENT_AMOUNT, settlementAmount);
        values.put(SETTLEMENT_INVOICE_ID  , SettlementId);
        values.put(SETTLEMENT_TERMINAL_NO,PosNum);
        values.put(SETTLEMENT_DATE_TRANSACTION  , transactionDate);
        values.put(SETTLEMENT_DATE_CREATED  , transactionDate);

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

    public void deleteDataByInProgressStatus() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get the transaction IDs with status "InProgress"
        String query = "SELECT " + TRANSACTION_TICKET_NO + " FROM " + TRANSACTION_HEADER_TABLE_NAME +
                " WHERE " + TRANSACTION_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"InProgress"});

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


    public void updateHeaderTransactionIdInProgress(String newTransactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_TICKET_NO, newTransactionId);
        db.update(TRANSACTION_HEADER_TABLE_NAME, values, TRANSACTION_STATUS + " = ?", new String[]{DatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS});
    }



    public void updateTransactionTransactionIdInProgress(String oldTransactionId, String newTransactionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, newTransactionId);

        db.update(
                TRANSACTION_TABLE_NAME,
                values,
                TRANSACTION_ID + " = ?",
                new String[]{oldTransactionId}
        );
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

                    // Create a Buyer object and add it to the list
                    Buyer buyer = new Buyer(name,othername, tan, brn, businessAddr, buyerType,buyerProfile, nic,companyName);
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

        long result = db.insert("Buyer_Table", null, values);

        db.close();

        return result != -1;
    }


    // In your DatabaseHelper class, add a method to update an item
    public void updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Name , item.getName());
        values.put(DESC , item.getDescription());
        values.put(Price , item.getPrice());
        // Add more columns as needed

        // Define the WHERE clause to identify the item by its unique identifier (e.g., Barcode)
        String whereClause = Barcode + "=?";
        String[] whereArgs = { item.getBarcode() };

        // Perform the update operation
        int rowsUpdated = db.update(TABLE_NAME, values, whereClause, whereArgs);

        // Close the database connection
        db.close();

        // Check if the update was successful
        if (rowsUpdated > 0) {
            Log.d("Database Update", "Item updated successfully: " + item.getBarcode());
        } else {
            Log.e("Database Update", "Failed to update item: " + item.getBarcode());
        }
    }
    public long insertItem(Item newItem) {
        // Get a writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to store the item's values
        ContentValues values = new ContentValues();
        values.put(Barcode , newItem.getBarcode());
        values.put(Name , newItem.getName());
        values.put(DESC , newItem.getDescription());
        values.put(Category , newItem.getCategory());
        values.put(Quantity , newItem.getQuantity());
        values.put(Department , newItem.getDepartment());
        values.put(LongDescription , newItem.getLongDescription());
        // Add other columns as needed

        // Insert the item into the database and get the inserted row ID
        long newRowId = db.insert(TABLE_NAME, null, values);

        // Close the database connection
        db.close();

        // Return the inserted row ID (or -1 if the insertion failed)
        return newRowId;
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

        // Insert the data into the users table
        db.insert(TABLE_NAME_Users, null, values);
        db.close();
    }

}

