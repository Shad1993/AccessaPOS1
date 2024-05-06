package com.accessa.ibora.Sync.MasterSync;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.accessa.ibora.product.items.Item;

public class MSSQLDataInserter {
    private static final Logger logger = Logger.getLogger(MSSQLDataInserter.class.getName());

    private static final String _user = "sa";
    private static final String _pass = "Logi2131";
    private static final String _DB = "IboraPOS1";
    private static final String _server = "192.168.1.89";
    private static final String JDBC_URL = "jdbc:sqlserver://" + _server + ";databaseName=" + _DB;
    private static final String USERNAME = _user;
    private static final String PASSWORD = _pass;

    // Method to insert an item into the POSITEMS table
    public static void insertItem(Item item) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String insertQuery = "INSERT INTO POSITEMS (name, ItemCode, TaxCode, description, longDescription, quantity, department, subDepartment, category, Nature, Discount, RateDiscount, TotalDiscount, price, price2, price3, priceAfterDiscount, barcode, weight, expiryDate, hascomment, hasoption, relateditem, relateditem2, relateditem3, relateditem4, relateditem5, VAT, Currency, soldBy, image, SKU, variant, UserId, DateCreated, LastModified, cost, AvailableForSale) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, item.getName());
                preparedStatement.setString(2, item.getItemCode());
                preparedStatement.setString(3, item.getTaxCode());
                preparedStatement.setString(4, item.getDescription());
                preparedStatement.setString(5, item.getLongDescription());
                preparedStatement.setFloat(6, item.getQuantity());
                preparedStatement.setString(7, item.getDepartment());
                preparedStatement.setString(8, item.getSubDepartment());
                preparedStatement.setString(9, item.getCategory());
                preparedStatement.setString(10, item.getNature());
                preparedStatement.setString(11, item.getDiscount());
                preparedStatement.setString(12, item.getRateDiscount());
                preparedStatement.setFloat(13, item.getTotalDiscount());
                preparedStatement.setFloat(14, item.getPrice());
                preparedStatement.setFloat(15, item.getPrice2());
                preparedStatement.setFloat(16, item.getPrice3());
                preparedStatement.setFloat(17, item.getPriceAfterDiscount());
                preparedStatement.setString(18, item.getBarcode());
                preparedStatement.setFloat(19, item.getWeight());
                preparedStatement.setString(20, item.getExpiryDate());
                preparedStatement.setString(21, item.gethascomment());
                preparedStatement.setBoolean(22, item.gethasoptions());
                preparedStatement.setString(23, item.getRelateditem());
                preparedStatement.setString(24, item.getRelateditem2());
                preparedStatement.setString(25, item.getRelateditem3());
                preparedStatement.setString(26, item.getRelateditem4());
                preparedStatement.setString(27, item.getRelateditem5());
                preparedStatement.setString(28, item.getVAT());
                preparedStatement.setString(29, item.getCurrency());
                preparedStatement.setString(30, item.getSoldBy());
                preparedStatement.setString(31, item.getImage());
                preparedStatement.setString(32, item.getSKU());
                preparedStatement.setString(33, item.getVariant());
                preparedStatement.setString(34, item.getUserId());
                preparedStatement.setString(35, item.getDateCreated());
                preparedStatement.setString(36, item.getLastModified());
                preparedStatement.setFloat(37, item.getCost());
                preparedStatement.setBoolean(38, item.getAvailableForSale());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            // Log any SQLException that occurs during insertion
            logger.log(Level.SEVERE, "Error inserting item into database", e);
        }
    }
}