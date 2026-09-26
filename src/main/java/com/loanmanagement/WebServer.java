package com.loanmanagement;

import com.loanmanagement.controller.AppController;
import com.loanmanagement.model.User;
import com.loanmanagement.util.DBConnection;
import com.loanmanagement.util.PasswordUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

    private static final int PORT = 8080;

    private static final AppController controller =
            new AppController();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(PORT), 0
        );

        // Health check
        server.createContext("/api/health", WebServer::health);

        // Authentication
        server.createContext("/api/login", WebServer::login);

        // Users
        server.createContext("/api/users", WebServer::users);

        // Loan Types
        server.createContext("/api/loan-types", WebServer::loanTypes);

        // Customers
        server.createContext("/api/customers", WebServer::customers);

        server.setExecutor(null);

        System.out.println("======================================");
        System.out.println("Loan Management System Backend");
        System.out.println("Server started on port " + PORT);
        System.out.println("======================================");

        server.start();
    }

    // ============================================================
    // HEALTH CHECK
    // ============================================================

    private static void health(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"error\":\"Method not allowed\"}"
            );

            return;
        }

        sendResponse(
                exchange,
                200,
                "{\"status\":\"Backend is running successfully\"}"
        );
    }

    // ============================================================
    // LOGIN
    // ============================================================

    private static void login(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"error\":\"Method not allowed\"}"
            );

            return;
        }

        try {

            String body =
                    readRequestBody(exchange);

            Map<String, String> data =
                    parseFormData(body);

            String username =
                    data.get("username");

            String password =
                    data.get("password");

            if (username == null ||
                    password == null) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Username and password are required\"}"
                );

                return;
            }

            boolean success =
                    controller.login(
                            username,
                            password
                    );

            if (!success) {

                sendResponse(
                        exchange,
                        401,
                        "{\"error\":\"Invalid username or password\"}"
                );

                return;
            }

            User user =
                    controller.getUserByUsername(
                            username
                    );

            String response =
                    "{"
                            + "\"message\":\"Login successful\","
                            + "\"userId\":" + user.getUserId() + ","
                            + "\"username\":\""
                            + escapeJson(user.getUsername())
                            + "\","
                            + "\"role\":\""
                            + user.getRole()
                            + "\","
                            + "\"status\":\""
                            + user.getStatus()
                            + "\""
                            + "}";

            sendResponse(
                    exchange,
                    200,
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"error\":\"Login failed\"}"
            );
        }
    }

    // ============================================================
    // USERS
    // ============================================================

    private static void users(HttpExchange exchange)
            throws IOException {

        String method =
                exchange.getRequestMethod();

        try {

            if (method.equalsIgnoreCase("GET")) {

                getUsers(exchange);

            } else if (method.equalsIgnoreCase("POST")) {

                addUser(exchange);

            } else {

                sendResponse(
                        exchange,
                        405,
                        "{\"error\":\"Method not allowed\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"error\":\"User operation failed\"}"
            );
        }
    }

    private static void getUsers(HttpExchange exchange)
            throws SQLException, IOException {

        StringBuilder json =
                new StringBuilder();

        json.append("[");

        String sql =
                "SELECT user_id, username, role, status, created_at " +
                        "FROM users ORDER BY user_id";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet rs =
                        statement.executeQuery()
        ) {

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }

                first = false;

                json.append("{")
                        .append("\"userId\":")
                        .append(rs.getInt("user_id"))
                        .append(",")

                        .append("\"username\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("username")
                                )
                        )
                        .append("\",")

                        .append("\"role\":\"")
                        .append(rs.getString("role"))
                        .append("\",")

                        .append("\"status\":\"")
                        .append(rs.getString("status"))
                        .append("\",")

                        .append("\"createdAt\":\"")
                        .append(rs.getTimestamp("created_at"))
                        .append("\"")

                        .append("}");
            }
        }

        json.append("]");

        sendResponse(
                exchange,
                200,
                json.toString()
        );
    }

    private static void addUser(HttpExchange exchange)
            throws IOException, SQLException {

        String body =
                readRequestBody(exchange);

        String username =
                extractJsonValue(
                        body,
                        "username"
                );

        String password =
                extractJsonValue(
                        body,
                        "password"
                );

        String role =
                extractJsonValue(
                        body,
                        "role"
                );

        if (username == null ||
                password == null ||
                role == null) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"username, password and role are required\"}"
            );

            return;
        }

        String hashedPassword =
                PasswordUtil.hashPassword(password);

        String sql =
                "INSERT INTO users " +
                        "(username, password_hash, role, status) " +
                        "VALUES (?, ?, ?, 'ACTIVE')";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    username
            );

            statement.setString(
                    2,
                    hashedPassword
            );

            statement.setString(
                    3,
                    role
            );

            statement.executeUpdate();
        }

        sendResponse(
                exchange,
                201,
                "{\"message\":\"User created successfully\"}"
        );
    }

    // ============================================================
    // LOAN TYPES
    // ============================================================

    private static void loanTypes(HttpExchange exchange)
            throws IOException {

        String method =
                exchange.getRequestMethod();

        try {

            if (method.equalsIgnoreCase("GET")) {

                getLoanTypes(exchange);

            } else if (method.equalsIgnoreCase("POST")) {

                addLoanType(exchange);

            } else {

                sendResponse(
                        exchange,
                        405,
                        "{\"error\":\"Method not allowed\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"error\":\"Loan type operation failed\"}"
            );
        }
    }

    private static void getLoanTypes(HttpExchange exchange)
            throws SQLException, IOException {

        StringBuilder json =
                new StringBuilder();

        json.append("[");

        String sql =
                "SELECT loan_type_id, name, description, " +
                        "interest_rate, min_amount, max_amount, " +
                        "max_tenure_months, status " +
                        "FROM loan_types ORDER BY loan_type_id";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet rs =
                        statement.executeQuery()
        ) {

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }

                first = false;

                json.append("{")

                        .append("\"loanTypeId\":")
                        .append(rs.getInt("loan_type_id"))
                        .append(",")

                        .append("\"name\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("name")
                                )
                        )
                        .append("\",")

                        .append("\"description\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("description")
                                )
                        )
                        .append("\",")

                        .append("\"interestRate\":")
                        .append(
                                rs.getBigDecimal("interest_rate")
                        )
                        .append(",")

                        .append("\"minAmount\":")
                        .append(
                                rs.getBigDecimal("min_amount")
                        )
                        .append(",")

                        .append("\"maxAmount\":")
                        .append(
                                rs.getBigDecimal("max_amount")
                        )
                        .append(",")

                        .append("\"maxTenureMonths\":")
                        .append(
                                rs.getInt("max_tenure_months")
                        )
                        .append(",")

                        .append("\"status\":\"")
                        .append(rs.getString("status"))
                        .append("\"")

                        .append("}");
            }
        }

        json.append("]");

        sendResponse(
                exchange,
                200,
                json.toString()
        );
    }

    private static void addLoanType(HttpExchange exchange)
            throws IOException, SQLException {

        String body =
                readRequestBody(exchange);

        String name =
                extractJsonValue(
                        body,
                        "name"
                );

        String description =
                extractJsonValue(
                        body,
                        "description"
                );

        String interestRate =
                extractJsonValue(
                        body,
                        "interestRate"
                );

        String minAmount =
                extractJsonValue(
                        body,
                        "minAmount"
                );

        String maxAmount =
                extractJsonValue(
                        body,
                        "maxAmount"
                );

        String maxTenureMonths =
                extractJsonValue(
                        body,
                        "maxTenureMonths"
                );

        if (name == null ||
                interestRate == null ||
                minAmount == null ||
                maxAmount == null ||
                maxTenureMonths == null) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Required loan type fields are missing\"}"
            );

            return;
        }

        String sql =
                "INSERT INTO loan_types " +
                        "(name, description, interest_rate, min_amount, " +
                        "max_amount, max_tenure_months, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    name
            );

            statement.setString(
                    2,
                    description
            );

            statement.setBigDecimal(
                    3,
                    new java.math.BigDecimal(
                            interestRate
                    )
            );

            statement.setBigDecimal(
                    4,
                    new java.math.BigDecimal(
                            minAmount
                    )
            );

            statement.setBigDecimal(
                    5,
                    new java.math.BigDecimal(
                            maxAmount
                    )
            );

            statement.setInt(
                    6,
                    Integer.parseInt(
                            maxTenureMonths
                    )
            );

            statement.executeUpdate();
        }

        sendResponse(
                exchange,
                201,
                "{\"message\":\"Loan type created successfully\"}"
        );
    }

    // ============================================================
    // CUSTOMERS
    // ============================================================

    private static void customers(HttpExchange exchange)
            throws IOException {

        String method =
                exchange.getRequestMethod();

        try {

            if (method.equalsIgnoreCase("GET")) {

                getCustomers(exchange);

            } else if (method.equalsIgnoreCase("PUT")) {

                updateKyc(exchange);

            } else {

                sendResponse(
                        exchange,
                        405,
                        "{\"error\":\"Method not allowed\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"error\":\"Customer operation failed\"}"
            );
        }
    }

    private static void getCustomers(HttpExchange exchange)
            throws SQLException, IOException {

        StringBuilder json =
                new StringBuilder();

        json.append("[");

        String sql =
                "SELECT customer_id, user_id, full_name, email, phone, " +
                        "dob, address, monthly_income, pan_number, " +
                        "aadhaar_last4, employment_type, account_number, " +
                        "ifsc_code, bank_name, kyc_status, kyc_remarks, " +
                        "credit_score, existing_emi, status " +
                        "FROM customers ORDER BY customer_id";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet rs =
                        statement.executeQuery()
        ) {

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }

                first = false;

                json.append("{")

                        .append("\"customerId\":")
                        .append(rs.getInt("customer_id"))
                        .append(",")

                        .append("\"userId\":")
                        .append(rs.getInt("user_id"))
                        .append(",")

                        .append("\"fullName\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("full_name")
                                )
                        )
                        .append("\",")

                        .append("\"email\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("email")
                                )
                        )
                        .append("\",")

                        .append("\"phone\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("phone")
                                )
                        )
                        .append("\",")

                        .append("\"dob\":\"")
                        .append(rs.getDate("dob"))
                        .append("\",")

                        .append("\"address\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("address")
                                )
                        )
                        .append("\",")

                        .append("\"monthlyIncome\":")
                        .append(
                                rs.getBigDecimal("monthly_income")
                        )
                        .append(",")

                        .append("\"panNumber\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("pan_number")
                                )
                        )
                        .append("\",")

                        .append("\"aadhaarLast4\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("aadhaar_last4")
                                )
                        )
                        .append("\",")

                        .append("\"employmentType\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("employment_type")
                                )
                        )
                        .append("\",")

                        .append("\"accountNumber\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("account_number")
                                )
                        )
                        .append("\",")

                        .append("\"ifscCode\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("ifsc_code")
                                )
                        )
                        .append("\",")

                        .append("\"bankName\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("bank_name")
                                )
                        )
                        .append("\",")

                        .append("\"kycStatus\":\"")
                        .append(rs.getString("kyc_status"))
                        .append("\",")

                        .append("\"kycRemarks\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("kyc_remarks")
                                )
                        )
                        .append("\",")

                        .append("\"creditScore\":")
                        .append(
                                rs.getObject("credit_score") == null
                                        ? "null"
                                        : rs.getInt("credit_score")
                        )
                        .append(",")

                        .append("\"existingEmi\":")
                        .append(
                                rs.getBigDecimal("existing_emi")
                        )
                        .append(",")

                        .append("\"status\":\"")
                        .append(rs.getString("status"))
                        .append("\"")

                        .append("}");
            }
        }

        json.append("]");

        sendResponse(
                exchange,
                200,
                json.toString()
        );
    }

    // ============================================================
    // KYC VERIFICATION / REJECTION
    // ============================================================

    private static void updateKyc(HttpExchange exchange)
            throws IOException, SQLException {

        String path =
                exchange.getRequestURI().getPath();

        String prefix =
                "/api/customers/";

        if (!path.startsWith(prefix) ||
                !path.endsWith("/kyc")) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Invalid KYC URL\"}"
            );

            return;
        }

        String customerIdText =
                path.substring(
                        prefix.length(),
                        path.length() - "/kyc".length()
                );

        int customerId;

        try {

            customerId =
                    Integer.parseInt(
                            customerIdText
                    );

        } catch (NumberFormatException e) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Invalid customer ID\"}"
            );

            return;
        }

        String body =
                readRequestBody(exchange);

        String status =
                extractJsonValue(
                        body,
                        "status"
                );

        String remarks =
                extractJsonValue(
                        body,
                        "remarks"
                );

        if (status == null) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"KYC status is required\"}"
            );

            return;
        }

        if (!status.equals("VERIFIED") &&
                !status.equals("REJECTED")) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"KYC status must be VERIFIED or REJECTED\"}"
            );

            return;
        }

        if (status.equals("REJECTED") &&
                (remarks == null ||
                        remarks.isBlank())) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Remarks are required when KYC is rejected\"}"
            );

            return;
        }

        String sql =
                "UPDATE customers " +
                        "SET kyc_status = ?, " +
                        "kyc_remarks = ?, " +
                        "kyc_verified_by = ?, " +
                        "kyc_verified_at = NOW() " +
                        "WHERE customer_id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status
            );

            statement.setString(
                    2,
                    remarks
            );

            // Temporary officer ID.
            // Later this will come from authenticated session.
            statement.setInt(
                    3,
                    2
            );

            statement.setInt(
                    4,
                    customerId
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 0) {

                sendResponse(
                        exchange,
                        404,
                        "{\"error\":\"Customer not found\"}"
                );

                return;
            }
        }

        sendResponse(
                exchange,
                200,
                "{\"message\":\"KYC status updated successfully\"}"
        );
    }

    // ============================================================
    // REQUEST BODY
    // ============================================================

    private static String readRequestBody(
            HttpExchange exchange)
            throws IOException {

        InputStream inputStream =
                exchange.getRequestBody();

        return new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    // ============================================================
    // FORM DATA PARSER
    // ============================================================

    private static Map<String, String> parseFormData(
            String body) {

        Map<String, String> data =
                new HashMap<>();

        if (body == null ||
                body.isBlank()) {

            return data;
        }

        String[] pairs =
                body.split("&");

        for (String pair : pairs) {

            String[] keyValue =
                    pair.split("=", 2);

            if (keyValue.length == 2) {

                String key =
                        URLDecoder.decode(
                                keyValue[0],
                                StandardCharsets.UTF_8
                        );

                String value =
                        URLDecoder.decode(
                                keyValue[1],
                                StandardCharsets.UTF_8
                        );

                data.put(
                        key,
                        value
                );
            }
        }

        return data;
    }

    // ============================================================
    // SIMPLE JSON VALUE EXTRACTOR
    // ============================================================

    private static String extractJsonValue(
            String json,
            String key) {

        if (json == null) {
            return null;
        }

        String search =
                "\"" + key + "\"";

        int keyIndex =
                json.indexOf(search);

        if (keyIndex == -1) {
            return null;
        }

        int colonIndex =
                json.indexOf(
                        ":",
                        keyIndex
                );

        if (colonIndex == -1) {
            return null;
        }

        int firstQuote =
                json.indexOf(
                        "\"",
                        colonIndex
                );

        if (firstQuote == -1) {
            return null;
        }

        int secondQuote =
                json.indexOf(
                        "\"",
                        firstQuote + 1
                );

        if (secondQuote == -1) {
            return null;
        }

        return json.substring(
                firstQuote + 1,
                secondQuote
        );
    }

    // ============================================================
    // JSON ESCAPING
    // ============================================================

    private static String escapeJson(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    // ============================================================
    // SEND RESPONSE
    // ============================================================

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response)
            throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        "application/json"
                );

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Origin",
                        "*"
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try (
                OutputStream outputStream =
                        exchange.getResponseBody()
        ) {

            outputStream.write(bytes);
        }
    }
}